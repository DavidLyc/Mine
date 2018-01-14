package com.whut.mine.danger.manage;

import android.content.Context;

import com.google.gson.JsonArray;
import com.whut.mine.data.ManageDoingOverItem;
import com.whut.mine.entity.Institution;

import io.reactivex.disposables.CompositeDisposable;

abstract class ManageBasePresenter<T> {

    T mView;
    Context mContext;
    CompositeDisposable mDisposables;
    Boolean isFirstLoad = true;
    int mNowItemNum;
    int mTotalItemNum;
    JsonArray mJsonArray = null;
    static final int CONVERTNUM = 50;
    static final String[] Categories = new String[]{"公司级", "车间级", "班组级", "专业级"};

    ManageBasePresenter(T mView, Context mContext) {
        this.mView = mView;
        this.mContext = mContext;
        mDisposables = new CompositeDisposable();
    }

    String getSubString(String s) {
        return s.substring(1, s.length() - 1);
    }

    ManageDoingOverItem getDoingOverItemFromJson() {
        JsonArray json = mJsonArray.get(mNowItemNum).getAsJsonArray();
        String instructionCode = getSubString(String.valueOf(json.get(4)));
        if (instructionCode.equals("-9999")) {
            instructionCode = "无指令号";
        } else {
            String[] temp = json.get(4).getAsString().trim().split(";");
            instructionCode = "安令字[" + temp[0] + "]第(" + temp[1] + ")号";
        }
        return new ManageDoingOverItem(
                "隐患描述：" + getSubString(String.valueOf(json.get(1)))
                , "详情：" + getSubString(String.valueOf(json.get(2))).substring(0, 19) + " / "
                + Categories[Integer.parseInt(getSubString(String.valueOf(json.get(3)))) - 1] + " / "
                + instructionCode + " / "
                + Institution.getInstitutionNameByNum(getSubString(String.valueOf(json.get(5))))
                , "截止日期：" + getSubString(String.valueOf(json.get(8))).substring(0, 19));
    }

    void destroy() {
        mView = null;
        mContext = null;
    }

}
