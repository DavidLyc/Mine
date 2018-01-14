package com.whut.mine.danger.rectify;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.google.gson.JsonArray;
import com.whut.mine.data.RectifyListItem;
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
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RectifyOverduePresenter implements RectifyOverdueContract.Presenter {

    private RectifyOverdueContract.View mView;
    private Context mContext;
    private CompositeDisposable mDisposables;
    private int mNowItemNum;
    private int mTotalItemNum;
    private final int CONVERTNUM = 50;
    private JsonArray mJsonArray = null;
    private final static String Rectify_ConfirmState = "2";
    private Boolean mIsFirstLoad = true;

    RectifyOverduePresenter(@NonNull RectifyOverdueContract.View view, @NonNull Context context) {
        mView = view;
        mContext = context;
        mView.setPresenter(this);
        mDisposables = new CompositeDisposable();
    }

    @Override
    public void subscribe() {
        if (mIsFirstLoad) {
            mIsFirstLoad = false;
            mView.showProgressDialog();
            loadData();
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
    public void loadData() {
        mNowItemNum = 0;
        mDisposables.add(NetFactory.getInstance()
                .isAvailableByPing()
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean result) throws Exception {
                        mView.cancelProgressDialog();
                        if (result) {
                            loadOverdueAlarmInfo();
                        } else {
                            ToastUtil.getInstance().showToast("无法连接到服务器！");
                            mView.showNoData();
                        }
                    }
                }));
    }

    @Override
    public Boolean loadMoreCacheData() {
        if (mJsonArray != null && mNowItemNum < mTotalItemNum) {
            List<RectifyListItem> itemList = new ArrayList<>();
            for (int i = 0; mNowItemNum < mTotalItemNum && i < CONVERTNUM; ++i) {
                JsonArray json = mJsonArray.get(mNowItemNum).getAsJsonArray();
                RectifyListItem item = new RectifyListItem();
                item.setHiddenId(json.get(0).getAsString());
                item.setCheckmemo(json.get(1).getAsString());
                item.setChecktime(json.get(2).getAsString());
                item.setCheckCatogoryNum(json.get(3).getAsString());
                if (json.get(4).getAsString().equals("-9999")) {
                    item.setInstructionNum("无指令号");
                } else {
                    String[] temp = json.get(4).getAsString().trim().split(";");
                    String tempInstructionNum = "安令字[" + temp[0] + "]第(" + temp[1] + ")号";
                    item.setInstructionNum(tempInstructionNum);
                }
                item.setConfirmPersonInstitution(json.get(5).getAsString());
                item.setRectifactionEndDate(json.get(6).getAsString());
                itemList.add(item);
                mNowItemNum++;
            }
            mView.showOverdueAlarms(itemList);
            return true;
        }
        return false;
    }

    @Override
    public void loadOverdueAlarmInfo() {
        mDisposables.add(NetFactory.getInstance()
                .getRectifyInfo(Rectify_ConfirmState)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonArray>() {
                    @Override
                    public void accept(JsonArray jsonArray) throws Exception {
                        mView.cancelProgressDialog();
                        mJsonArray = jsonArray;
                        mTotalItemNum = jsonArray.size();
                        loadMoreCacheData();
                    }
                })
        );
    }

    @Override
    public void saveRectifyInfo(List<RectifyListItem> items) {
        JsonArray json = new JsonArray();
        List<String> postImages = new ArrayList<>();
        String completedTime = TimeUtils.getTodaynyrsfm();
        for (RectifyListItem item : items) {
            JsonArray temp = new JsonArray();
            item.setRectifactionForAlarmCompletedTime(completedTime);
            temp.add(item.getHiddenId());
            temp.add(ImageUtils.getPicJson(item.getPhotoUrl()));
            temp.add(item.getRectifyDescription());
            temp.add(item.getRectifactionForAlarmCompletedTime());
            postImages.addAll(item.getPhotoUrl());
            json.add(temp);
        }
        postRectifyInfo(json, postImages);
    }

    @Override
    public void postRectifyInfo(JsonArray json, List<String> urls) {
        mView.showProgressDialog();
        mDisposables.add(NetFactory.getInstance()
                .postRectifyJson(json)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        );
        mDisposables.add(NetFactory.getInstance()
                .uploadImages(urls, 1)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean result) throws Exception {
                        mView.cancelProgressDialog();
                        if (result) {
                            ToastUtil.getInstance().showToast("整改成功！");
                            mView.clearAllItems();
                            loadData();
                        } else {
                            ToastUtil.getInstance().showToast("整改失败！");
                            mView.showErrorDialog();
                        }
                    }
                }));
    }

    @Override
    public void saveShotPhoto(final String result, final RectifyListItem item) {
        mView.showProgressDialog();
        mDisposables.add(
                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull final ObservableEmitter<String> s) throws Exception {
                        Bitmap bitmap = BitmapFactory.decodeFile(result);
                        final String url = ImageUtils.getRectifyImageStorePath(mContext, Long.valueOf(item.getHiddenId())
                                , item.getPhotoUrl().size());
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
                                mView.cancelProgressDialog();
                                mView.addPhotoToRectifyListItem(Collections.singletonList(url));
                            }
                        })
        );
    }

    @Override
    public void saveAlbumPhoto(final List<AlbumFile> albumFiles, final RectifyListItem item) {
        mView.showProgressDialog();
        mDisposables.add(
                Observable.create(new ObservableOnSubscribe<List<String>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<String>> e) throws Exception {
                        List<String> urls = new ArrayList<>();
                        int startCount = item.getPhotoUrl().size();
                        for (int i = 0; i < albumFiles.size(); i++) {
                            Bitmap bitmap = BitmapFactory.decodeFile(albumFiles.get(i).getPath());
                            String url = ImageUtils.getRectifyImageStorePath(mContext, Long.valueOf(item.getHiddenId())
                                    , startCount + i);
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
                            public void accept(List<String> strings) throws Exception {
                                mView.cancelProgressDialog();
                                mView.addPhotoToRectifyListItem(strings);
                            }
                        })
        );
    }
}
