package com.whut.mine.base;

public interface BaseView<T> {

    void setPresenter(T presenter);

    void showProgressDialog();

    void cancelProgressDialog();

}