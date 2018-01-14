package com.whut.mine.danger.manage;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.JsonArray;
import com.whut.mine.data.ManageTodoItem;
import com.whut.mine.entity.Institution;
import com.whut.mine.entity.User;
import com.whut.mine.network.NetFactory;
import com.whut.mine.util.TimeUtils;
import com.whut.mine.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;

public class ManageTodoPresenter extends ManageBasePresenter<ManageTodoContract.View>
        implements ManageTodoContract.Presenter {

    ManageTodoPresenter(@NonNull ManageTodoContract.View view, @NonNull Context context) {
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
                            loadTodoDanger();
                        } else {
                            ToastUtil.getInstance().showToast("无法连接到服务器！");
                            mView.cancelProgressDialog();
                        }
                    }
                })
        );
    }

    @Override
    public void loadMoreCacheData() {
        if (mJsonArray != null && mNowItemNum < mTotalItemNum) {
            List<ManageTodoItem> itemList = new ArrayList<>();
            for (int i = 0; mNowItemNum < mTotalItemNum && i < CONVERTNUM; ++i) {
                JsonArray json = mJsonArray.get(mNowItemNum).getAsJsonArray();
                ManageTodoItem item = new ManageTodoItem(getSubString(String.valueOf(json.get(0)))
                        , "隐患描述：" + getSubString(String.valueOf(json.get(1)))
                        , "详情：" + getSubString(String.valueOf(json.get(2))).substring(0, 19) + " / "
                        + Categories[Integer.parseInt(getSubString(String.valueOf(json.get(3)))) - 1] + " / "
                        + Institution.getInstitutionNameByNum(getSubString(String.valueOf(json.get(4)))));
                itemList.add(item);
                mNowItemNum++;
            }
            mView.showDangerItems(itemList);
        }
    }

    @Override
    public CharSequence[] searchPeopleTip(String inputPeople, String instNum) {
        if (inputPeople.isEmpty()) {
            return null;
        }
        List<String> peopleList = User.getUserInfoBySearchName(inputPeople, instNum);
        return peopleList.toArray(new CharSequence[peopleList.size()]);
    }

    @Override
    public void sendRectInstruction(List<ManageTodoItem> selectedItems, RectifyMsg msg) {
        String rectChargeInstNum = msg.getRecInstNum();
        String rectChargePeopleNum = msg.getRecPeopleNum();
        if (rectChargeInstNum == null || rectChargePeopleNum == null) {
            ToastUtil.getInstance().showToast("整改信息不完整，请重新输入或点击查询按钮！");
            return;
        }
        mView.showProgressDialog();
        JsonArray jsonArray = new JsonArray();
        StringBuilder selectedItemNumbs = new StringBuilder();
        for (ManageTodoItem item : selectedItems) {
            selectedItemNumbs.append(item.getHiddenID()).append(",");
        }
        selectedItemNumbs = new StringBuilder(selectedItemNumbs.substring(0, selectedItemNumbs.length() - 1));
        SharedPreferences pref = mContext.getSharedPreferences("mine", MODE_PRIVATE);
        String userNum = pref.getString("user_num", "user_num");
        String userInst = pref.getString("user_inst", "user_inst");
        jsonArray.add(String.valueOf(selectedItemNumbs.toString()));
        jsonArray.add(userInst);
        jsonArray.add(userNum);
        jsonArray.add(rectChargeInstNum);
        jsonArray.add(rectChargePeopleNum);
        jsonArray.add(TimeUtils.getTodaynyrsfm());
        jsonArray.add(TimeUtils.getTomorrownyrsfm());
        mDisposables.add(NetFactory.getInstance()
                .postHidDangerJson(jsonArray)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        mView.cancelProgressDialog();
                        if (s.equals("1")) {
                            ToastUtil.getInstance().showToast("提交成功！");
                            mView.clearAllItems();
                            loadData();
                        } else {
                            mView.showErrorDialog();
                        }
                    }
                })
        );
    }

    @Override
    public void loadTodoDanger() {
        mDisposables.add(NetFactory.getInstance()
                .getTodoDangers()
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

}
