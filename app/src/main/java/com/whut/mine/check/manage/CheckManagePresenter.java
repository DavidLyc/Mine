package com.whut.mine.check.manage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.whut.mine.data.CheckManageItem;
import com.whut.mine.data.CheckTableSecondItem;
import com.whut.mine.entity.Institution;
import com.whut.mine.entity.SafetyCheckTable;
import com.whut.mine.entity.SafetyCheckTableDetail;
import com.whut.mine.entity.SafetyCheckTableInfo;
import com.whut.mine.network.NetFactory;
import com.whut.mine.util.FileUtils;
import com.whut.mine.util.ImageUtils;
import com.whut.mine.util.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CheckManagePresenter implements CheckManageContract.Presenter {

    private CheckManageContract.View mView;
    private Context mContext;
    private CompositeDisposable mDisposables;

    CheckManagePresenter(@NonNull CheckManageContract.View view, @NonNull Context context) {
        mView = view;
        mContext = context;
        mView.setPresenter(this);
        mDisposables = new CompositeDisposable();
    }

    @Override
    public void subscribe() {
        loadSavedCheckTable();
    }

    @Override
    public void unsubscribe() {
        mDisposables.clear();
    }

    @Override
    public void destroy() {
        mView = null;
        mContext = null;
    }

    @Override
    public void loadSavedCheckTable() {
        mDisposables.add(Observable.create(new ObservableOnSubscribe<List<CheckManageItem>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<CheckManageItem>> e) throws Exception {
                        e.onNext(SafetyCheckTableInfo.getAllCheckManageItem());
                        e.onComplete();
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<CheckManageItem>>() {
                            @Override
                            public void accept(List<CheckManageItem> items) throws Exception {
                                mView.setRecyclerViewItems(items);
                            }
                        })
        );
    }

    @Override
    public void sendJson(final List<CheckManageItem> items) {
        if (items.size() == 0) {
            Toast.makeText(mContext, "请选择至少一条隐患进行提交！", Toast.LENGTH_SHORT).show();
            return;
        }
        mView.showProgressDialog();
        List<JsonArray> jsonArrayList = new ArrayList<>();
        final List<String> imageUrls = new ArrayList<>();
        for (CheckManageItem checkManageItem : items) {
            SafetyCheckTableInfo info = SafetyCheckTableInfo.getCheckInfoByTime(checkManageItem.getCheckTime().substring(5));
            List<CheckTableSecondItem> secondItems = SafetyCheckTableDetail.getSecondItemsByCheckTime(checkManageItem.getCheckTime().substring(5));
            JsonArray jsonResult = new JsonArray();
            JsonArray tempJson = new JsonArray();
            int checkTableID = info.getCheckTableID();
            tempJson.add(String.valueOf(checkTableID));
            tempJson.add(SafetyCheckTable.getCheckTableNameByID(checkTableID));
            tempJson.add(String.valueOf(info.getCheckTime()));
            tempJson.add(String.valueOf(info.getDataTime()));
            tempJson.add(String.valueOf(info.getPersonInChargeNum()));
            tempJson.add(String.valueOf(info.getPersonInChargeName()));
            tempJson.add(String.valueOf(info.getPeopleForCheck()));
            tempJson.add(String.valueOf(info.getSuggestion()));
            tempJson.add(ImageUtils.getPicJson(Collections.singletonList(info.getValidatePic())));
            //添加图片
            imageUrls.add(info.getValidatePic());
            tempJson.add(String.valueOf(info.getCheckCatogoryNum()));
            tempJson.add(String.valueOf(Institution.getInstitutionNumByName((info.getInstitutionChecked()))));
            jsonResult.add(tempJson);
            JsonArray detailJson = new JsonArray();
            for (CheckTableSecondItem secondItem : secondItems) {
                tempJson = new JsonArray();
                tempJson.add(String.valueOf(secondItem.getFirstIndexID()));
                tempJson.add(String.valueOf(secondItem.getFirstIndexName()));
                tempJson.add(String.valueOf(secondItem.getSecondIndexID()));
                tempJson.add(String.valueOf(secondItem.getSecondIndexName()));
                tempJson.add(String.valueOf(secondItem.getCheckStatus()));
                if (secondItem.getCheckStatus() == 0 && secondItem.getHidDangerInfo().isEmpty()) {
                    mView.cancelProgressDialog();
                    Toast.makeText(mContext, "隐患信息填写不完整，请进入详情页检查！", Toast.LENGTH_SHORT).show();
                    return;
                }
                tempJson.add(String.valueOf(secondItem.getHidDangerInfo()));
                tempJson.add(String.valueOf(secondItem.getHidDangerType()));
                List<String> urls = secondItem.getPhotoUrl();
                tempJson.add(ImageUtils.getPicJson(urls));
                //添加图片
                imageUrls.addAll(urls);
                detailJson.add(tempJson);
            }
            jsonResult.add(detailJson);
            jsonArrayList.add(jsonResult);
        }
        for (JsonArray json : jsonArrayList) {
            mDisposables.add(NetFactory.getInstance()
                    .postDetailJson(json)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            );
        }
        mDisposables.add(NetFactory.getInstance()
                .uploadImages(imageUrls, 0)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean result) throws Exception {
                        mView.cancelProgressDialog();
                        if (result) {
                            //发送成功后删除保存的数据
                            for (CheckManageItem item : items) {
                                SafetyCheckTableInfo.deleteDataByTime(item.getCheckTime().substring(5));
                                SafetyCheckTableDetail.deleteDetailsByTime(item.getCheckTime().substring(5));
                            }
                            FileUtils.deleteImage(imageUrls);
                            ToastUtil.getInstance().showToast("提交成功");
                            loadSavedCheckTable();
                        } else {
                            Toast.makeText(mContext, "提交失败，请检查网络连接！", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
        );
    }

    @Override
    public void deleteCheckRecord(final List<CheckManageItem> items) {
        mDisposables.add(
                Observable.create(new ObservableOnSubscribe<Boolean>() {
                    @Override
                    public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                        for (CheckManageItem item : items) {
                            String checkTime = item.getCheckTime().substring(5);
                            SafetyCheckTableInfo.deleteDataByTime(checkTime);
                            SafetyCheckTableDetail.deleteDetailsByTime(checkTime);
                        }
                        e.onNext(true);
                        e.onComplete();
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean a) throws Exception {
                                Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                                loadSavedCheckTable();
                            }
                        })
        );
    }

}
