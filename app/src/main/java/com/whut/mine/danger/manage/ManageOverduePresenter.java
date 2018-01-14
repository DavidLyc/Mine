package com.whut.mine.danger.manage;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.JsonArray;
import com.whut.mine.data.ManageDoingOverItem;
import com.whut.mine.network.NetFactory;
import com.whut.mine.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ManageOverduePresenter extends ManageBasePresenter<ManageOverdueContract.View>
        implements ManageOverdueContract.Presenter {

    ManageOverduePresenter(@NonNull ManageOverdueContract.View view, @NonNull Context context) {
        super(view, context);
        mView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        if (isFirstLoad) {
            mView.showProgressDialog();
            isFirstLoad = false;
            loadData();
        }
    }

    @Override
    public void unsubscribe() {
        mDisposables.clear();
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void loadData() {
        mNowItemNum = 0;
        mDisposables.add(NetFactory.getInstance()
                .isAvailableByPing()
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean result) throws Exception {
                        if (result) {
                            loadDoingDanger();
                        } else {
                            ToastUtil.getInstance().showToast("无法连接到服务器！");
                            mView.cancelProgressDialog();
                        }
                    }
                })
        );
    }

    @Override
    public void loadDoingDanger() {
        mDisposables.add(NetFactory.getInstance()
                .getOverdueDangers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JsonArray>() {
                    @Override
                    public void accept(JsonArray jsonArray) throws Exception {
                        mView.cancelProgressDialog();
                        if (jsonArray.size() > 5) {
                            mJsonArray = jsonArray;
                            mTotalItemNum = jsonArray.size();
                            loadMoreCacheData();
                        } else {
                            ToastUtil.getInstance().showToast("没有隐患数据");
                        }
                    }
                })
        );
    }

    @Override
    public void loadMoreCacheData() {
        if (mJsonArray != null && mNowItemNum < mTotalItemNum) {
            List<ManageDoingOverItem> itemList = new ArrayList<>();
            for (int i = 0; mNowItemNum < mTotalItemNum && i < CONVERTNUM; ++i) {
                itemList.add(getDoingOverItemFromJson());
                mNowItemNum++;
            }
            mView.showDangerItems(itemList);
        }
    }

}
