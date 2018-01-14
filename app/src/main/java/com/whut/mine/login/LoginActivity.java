package com.whut.mine.login;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.whut.mine.R;
import com.whut.mine.home.HomeActivity;
import com.whut.mine.util.ActivityUtils;
import com.whut.mine.util.ApplicationUtil;

import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

public class LoginActivity extends AppCompatActivity {

    LoginPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        judgeLoginStatus();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        requestPermission();

        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager()
                .findFragmentById(R.id.login_fragment);
        if (loginFragment == null) {
            loginFragment = LoginFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    loginFragment, R.id.login_fragment);
        }

        mPresenter = new LoginPresenter(loginFragment, this);
    }

    private void requestPermission() {
        RxPermissions mRxPermissions = new RxPermissions(this);
        mRxPermissions.request(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            finish();
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        mPresenter.destroy();
        mPresenter = null;
        ApplicationUtil.getInstance().fixInputMethodManagerLeak(this);
        super.onDestroy();
    }

    private void judgeLoginStatus() {
        SharedPreferences pref = getSharedPreferences("mine", MODE_PRIVATE);
        if (pref.getBoolean("login_status", false)) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

}
