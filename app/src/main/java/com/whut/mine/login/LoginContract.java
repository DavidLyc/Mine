package com.whut.mine.login;

import android.support.annotation.NonNull;

import com.whut.mine.base.BasePresenter;
import com.whut.mine.base.BaseView;

public interface LoginContract {

    interface View extends BaseView<Presenter> {

        void enterHomePage();

    }

    interface Presenter extends BasePresenter {

        void doLogin(@NonNull String account, @NonNull String password);

    }

}
