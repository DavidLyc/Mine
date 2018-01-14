package com.whut.mine.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.whut.mine.entity.User;
import com.whut.mine.network.NetFactory;
import com.whut.mine.network.UserInstBean;
import com.whut.mine.util.MD5Utils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mView;
    private Context mContext;
    private CompositeDisposable mDisposables;

    LoginPresenter(@NonNull LoginContract.View view, @NonNull Context context) {
        mView = view;
        mContext = context;
        mView.setPresenter(this);
        mDisposables = new CompositeDisposable();
    }

    @Override
    public void subscribe() {

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
    public void doLogin(@NonNull final String account, @NonNull final String password) {
        if (account.equals("") || password.equals("")) {
            Toast.makeText(mContext, "员工号或密码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        mView.showProgressDialog();
        mDisposables.add(NetFactory.getInstance()
                .isAvailableByPing()
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean result) throws Exception {
                        if (result) {
                            loadUserAndInstTable(account, password);
                        } else {
                            mView.cancelProgressDialog();
                            Toast.makeText(mContext, "无法连接到服务器！", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
        );
    }

    private void loadUserAndInstTable(final String account, final String password) {
        mDisposables.add(NetFactory.getInstance()
                .getUsersAndInst()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<UserInstBean>() {
                            @Override
                            public void accept(UserInstBean bean) throws Exception {
                                mView.cancelProgressDialog();
                                if (User.isLoginValid(account, MD5Utils.encryptMD5ToString(password))) {
                                    mView.enterHomePage();
                                } else {
                                    Toast.makeText(mContext, "员工号或密码错误！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                )
        );
    }

}
