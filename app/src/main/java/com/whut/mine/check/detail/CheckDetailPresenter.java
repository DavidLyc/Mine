package com.whut.mine.check.detail;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.JsonArray;
import com.whut.mine.data.CheckTableFirstItem;
import com.whut.mine.data.CheckTableSecondItem;
import com.whut.mine.entity.CheckTableFirstIndex;
import com.whut.mine.entity.CheckTableSecondIndex;
import com.whut.mine.entity.Institution;
import com.whut.mine.entity.SafetyCheckTable;
import com.whut.mine.entity.SafetyCheckTableDetail;
import com.whut.mine.entity.SafetyCheckTableInfo;
import com.whut.mine.network.NetFactory;
import com.whut.mine.util.FileUtils;
import com.whut.mine.util.ImageUtils;
import com.whut.mine.util.TimeUtils;
import com.whut.mine.util.ToastUtil;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;

public class CheckDetailPresenter implements CheckDetailContract.Presenter {

    private CheckDetailContract.View mView;
    private Context mContext;
    private SafetyCheckTableInfo mSafetyCheckTableInfo;
    private List<SafetyCheckTableDetail> mSafetyCheckTableDetails;
    private CompositeDisposable mDisposables;
    private boolean mFirstLoad = true;
    private Long mCheckTableID;
    private List<CheckTableSecondItem> mSecondItems;
    private String mLabelPhotoUrl = null;
    private String mLabelPhotoTime;
    private List<String> mPostImages;

    CheckDetailPresenter(@NonNull CheckDetailContract.View view, @NonNull Context context) {
        mView = view;
        mContext = context;
        mView.setPresenter(this);
        mDisposables = new CompositeDisposable();
    }

    @Override
    public void subscribe() {
        if (mFirstLoad) {
            mFirstLoad = false;
            mPostImages = new ArrayList<>();
            Activity activity = ((CheckDetailFragment) mView).getActivity();
            mCheckTableID = activity.getIntent().getLongExtra("checktableid", 0);
            String checkTime = activity.getIntent().getStringExtra("checktime");
            if (checkTime == null) {
                generateDataInDB();
            } else {
                generateSavedCheckResult(checkTime);
            }
        }
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
    public void saveCheckTableInDao(final Boolean isShowToast) {
        if (mLabelPhotoUrl == null) {
            ToastUtil.getInstance().showToast("请拍摄标记照片后进行保存操作！");
            return;
        }
        mView.showProgressDialog();
        mDisposables.add(Observable.create(new ObservableOnSubscribe<Boolean>() {
                    @Override
                    public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                        //获得数据
                        mPostImages.clear();
                        setCheckTableInfo();
                        setCheckTableDetail(false);
                        //存入数据库
                        SafetyCheckTableInfo.saveCheckTableInfoInDatabase(mSafetyCheckTableInfo);
                        SafetyCheckTableDetail.saveCheckTableDetailsInDB(mSafetyCheckTableDetails);
                        e.onNext(true);
                        e.onComplete();
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean b) throws Exception {
                                if (isShowToast) {
                                    ToastUtil.getInstance().showToast("保存成功！");
                                }
                                mView.cancelProgressDialog();
                            }
                        })
        );
    }

    @Override
    public void postCheckTable() {
        if (mLabelPhotoUrl == null) {
            ToastUtil.getInstance().showToast("请拍摄标记照片后进行提交操作！");
            return;
        }
        //show loading
        mView.showProgressDialog();
        //获得数据
        mPostImages.clear();
        setCheckTableInfo();
        if (!setCheckTableDetail(true)) {
            mView.cancelProgressDialog();
            return;
        }
        //make json
        final JsonArray jsonResult = new JsonArray();
        JsonArray tempJson = new JsonArray();
        int checkTableID = mSafetyCheckTableInfo.getCheckTableID();
        tempJson.add(String.valueOf(checkTableID));
        tempJson.add(SafetyCheckTable.getCheckTableNameByID(checkTableID));
        tempJson.add(String.valueOf(mSafetyCheckTableInfo.getCheckTime()));
        tempJson.add(String.valueOf(mSafetyCheckTableInfo.getDataTime()));
        tempJson.add(String.valueOf(mSafetyCheckTableInfo.getPersonInChargeNum()));
        tempJson.add(String.valueOf(mSafetyCheckTableInfo.getPersonInChargeName()));
        tempJson.add(String.valueOf(mSafetyCheckTableInfo.getPeopleForCheck()));
        tempJson.add(String.valueOf(mSafetyCheckTableInfo.getSuggestion()));
        tempJson.add(ImageUtils.getPicJson(Collections.singletonList(mSafetyCheckTableInfo.getValidatePic())));
        tempJson.add(String.valueOf(mSafetyCheckTableInfo.getCheckCatogoryNum()));
        tempJson.add(String.valueOf(Institution.getInstitutionNumByName((mSafetyCheckTableInfo.getInstitutionChecked()))));
        jsonResult.add(tempJson);
        JsonArray detailJson = new JsonArray();
        for (CheckTableSecondItem item : mSecondItems) {
            tempJson = new JsonArray();
            tempJson.add(String.valueOf(item.getFirstIndexID()));
            tempJson.add(String.valueOf(item.getFirstIndexName()));
            tempJson.add(String.valueOf(item.getSecondIndexID()));
            tempJson.add(String.valueOf(item.getSecondIndexName()));
            tempJson.add(String.valueOf(item.getCheckStatus()));
            tempJson.add(String.valueOf(item.getHidDangerInfo()));
            tempJson.add(String.valueOf(item.getHidDangerType()));
            tempJson.add(ImageUtils.getPicJson(item.getPhotoUrl()));
            detailJson.add(tempJson);
        }
        jsonResult.add(detailJson);
        //发送json数据
        mDisposables.add(NetFactory.getInstance()
                .postDetailJson(jsonResult)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        );
        //发送图片
        mDisposables.add(NetFactory.getInstance()
                .uploadImages(mPostImages, 0)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean result) throws Exception {
                        mView.cancelProgressDialog();
                        if (result) {
                            //发送成功后删除保存的数据
                            deleteCheckData();
                            FileUtils.deleteImage(mPostImages);
                            ToastUtil.getInstance().showToast("提交成功");
                            mView.finishActivity();
                        } else {
                            saveCheckTableInDao(false);
                            ToastUtil.getInstance().showToast("网络异常，已保存该检查表，提交失败");
                        }
                    }
                })
        );
    }

    private void setCheckTableInfo() {
        mSafetyCheckTableInfo = mView.getCheckTableInfo();
        mSafetyCheckTableInfo.setCheckTableID(Integer.parseInt(String.valueOf(mCheckTableID)));
        mSafetyCheckTableInfo.setCheckTime(mLabelPhotoTime);
        mSafetyCheckTableInfo.setDataTime(mLabelPhotoTime);
        //从sp读取数据
        SharedPreferences pref = mContext.getSharedPreferences("mine", MODE_PRIVATE);
        String userNum = pref.getString("user_num", "user_num");
        String userName = pref.getString("user_name", "user_name");
        mSafetyCheckTableInfo.setPersonInChargeNum(userNum);
        mSafetyCheckTableInfo.setPersonInChargeName(userName);
        mSafetyCheckTableInfo.setSuggestion("无");
        mSafetyCheckTableInfo.setValidatePic(mLabelPhotoUrl);
        mPostImages.add(mLabelPhotoUrl);
    }

    private Boolean setCheckTableDetail(Boolean isCheckingInfo) {
        mSafetyCheckTableDetails = new ArrayList<>();
        for (CheckTableSecondItem secondItem : mSecondItems) {
            //检查信息完整性
            if (isCheckingInfo && secondItem.getCheckStatus() == 0 && secondItem.getHidDangerInfo().trim().isEmpty()) {
                ToastUtil.getInstance().showToast("请填写不合格项隐患说明！");
                return false;
            }
            secondItem.setCheckTime(mLabelPhotoTime);
            mSafetyCheckTableDetails.add(SafetyCheckTableDetail.getCheckTableDetailBySecondItem(secondItem
                    , Integer.parseInt(String.valueOf(mCheckTableID))));
            mPostImages.addAll(secondItem.getPhotoUrl());
        }
        return true;
    }

    @Override
    public void saveShotPhoto(final String result, final int startCount) {
        mView.showProgressDialog();
        mDisposables.add(
                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull final ObservableEmitter<String> s) throws Exception {
                        Bitmap bitmap = BitmapFactory.decodeFile(result);
                        String url;
                        if (mLabelPhotoUrl == null) {
                            url = ImageUtils.getLabelImageStorePath(mContext, mCheckTableID);
                        } else {
                            url = ImageUtils.getHiddenImageStorePath(mContext, mCheckTableID, startCount);
                        }
                        ImageUtils.saveAsJPEG(bitmap, url);
                        FileUtils.deleteFile(result);
                        s.onNext(url);
                        s.onComplete();
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String url) throws Exception {
                                if (mLabelPhotoUrl == null) {
                                    mLabelPhotoUrl = url;
                                    mLabelPhotoTime = TimeUtils.getTodaynyrsfm();
                                    mView.setPhotoLabel();
                                } else {
                                    mView.cancelProgressDialog();
                                    mView.addPhotoUrlToSecondItem(Collections.singletonList(url));
                                }
                            }
                        })
        );
    }

    @Override
    public void saveAlbumPhoto(final List<AlbumFile> albumFiles, final int startCount) {
        mView.showProgressDialog();
        mDisposables.add(
                Observable.create(new ObservableOnSubscribe<List<String>>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<List<String>> e) throws Exception {
                        List<String> urls = new ArrayList<>();
                        for (int i = 0; i < albumFiles.size(); i++) {
                            Bitmap bitmap = BitmapFactory.decodeFile(albumFiles.get(i).getPath());
                            String url = ImageUtils.getHiddenImageStorePath(mContext, mCheckTableID, startCount + i);
                            ImageUtils.saveAsJPEG(bitmap, url);
                            urls.add(url);
                        }
                        e.onNext(urls);
                        e.onComplete();
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<String>>() {
                            @Override
                            public void accept(List<String> url) throws Exception {
                                mView.cancelProgressDialog();
                                mView.addPhotoUrlToSecondItem(url);
                            }
                        })
        );
    }

    @Override
    public void deleteCheckData() {
        SafetyCheckTableInfo.deleteDataByTime(mLabelPhotoTime);
        SafetyCheckTableDetail.deleteDetailsByTime(mLabelPhotoTime);
    }

    private void generateDataInDB() {
        mSecondItems = new ArrayList<>();
        mDisposables.add(
                Observable.create(new ObservableOnSubscribe<List<MultiItemEntity>>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<List<MultiItemEntity>> e) throws Exception {
                        List<MultiItemEntity> entities = new ArrayList<>();
                        List<CheckTableFirstItem> firstItems = CheckTableFirstIndex.getCheckTableFirstItemsByID(mCheckTableID);
                        for (CheckTableFirstItem firstItem : firstItems) {
                            List<CheckTableSecondItem> secondItems = CheckTableSecondIndex.getSecondItemsByFirstIndexID(
                                    firstItem.getFirstIndexID());
                            mSecondItems.addAll(secondItems);
                            for (CheckTableSecondItem secondItem : secondItems) {
                                firstItem.addSubItem(secondItem);
                            }
                            entities.add(firstItem);
                        }
                        e.onNext(entities);
                        e.onComplete();
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<MultiItemEntity>>() {
                            @Override
                            public void accept(List<MultiItemEntity> entities) throws Exception {
                                mView.setRecyclerviewEntity(entities, false);
                            }
                        })
        );
    }

    private void generateSavedCheckResult(final String checkTime) {
        mView.showProgressDialog();
        mSecondItems = new ArrayList<>();
        mDisposables.add(Observable.zip(Observable.create(new ObservableOnSubscribe<List<MultiItemEntity>>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<List<MultiItemEntity>> e) throws Exception {
                        List<MultiItemEntity> entities = new ArrayList<>();
                        List<CheckTableFirstItem> firstItems = CheckTableFirstIndex.getCheckTableFirstItemsByID(mCheckTableID);
                        mSecondItems = SafetyCheckTableDetail.getSecondItemsByCheckTime(checkTime);
                        for (CheckTableFirstItem firstItem : firstItems) {
                            for (CheckTableSecondItem secondItem : mSecondItems) {
                                if (secondItem.getFirstIndexID() == firstItem.getFirstIndexID()) {
                                    firstItem.addSubItem(secondItem);
                                }
                            }
                            entities.add(firstItem);
                        }
                        e.onNext(entities);
                        e.onComplete();
                    }
                }), Observable.create(new ObservableOnSubscribe<SafetyCheckTableInfo>() {
                    @Override
                    public void subscribe(ObservableEmitter<SafetyCheckTableInfo> e) throws Exception {
                        e.onNext(SafetyCheckTableInfo.getCheckInfoByTime(checkTime));
                        e.onComplete();
                    }
                }), new BiFunction<List<MultiItemEntity>, SafetyCheckTableInfo, BindCheckResult>() {
                    @Override
                    public BindCheckResult apply(List<MultiItemEntity> entities, SafetyCheckTableInfo info) throws Exception {
                        return new BindCheckResult(entities, info);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<BindCheckResult>() {
                            @Override
                            public void accept(BindCheckResult result) throws Exception {
                                mLabelPhotoUrl = result.info.getValidatePic();
                                mLabelPhotoTime = checkTime;
                                mView.setCheckInfo(result.info);
                                mView.setRecyclerviewEntity(result.entities, true);
                                mView.cancelProgressDialog();
                            }
                        })
        );
    }

    class BindCheckResult {
        List<MultiItemEntity> entities;
        SafetyCheckTableInfo info;

        BindCheckResult(List<MultiItemEntity> entities, SafetyCheckTableInfo info) {
            this.entities = entities;
            this.info = info;
        }
    }

}
