package com.whut.mine.home.setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.whut.mine.R;
import com.whut.mine.entity.User;
import com.whut.mine.login.LoginActivity;
import com.whut.mine.network.NetFactory;
import com.whut.mine.network.SafetyCheckBean;
import com.whut.mine.network.UserInstBean;
import com.whut.mine.util.ApplicationUtil;
import com.whut.mine.util.FileUtils;
import com.whut.mine.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private CompositeDisposable mDisposables;
    private MaterialDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        setTitle("");
        mToolbarTitle.setText("设置");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mDisposables = new CompositeDisposable();
    }

    @OnClick({R.id.setting_user_inst, R.id.setting_check_table, R.id.setting_sign_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.setting_user_inst:
                updateUserAndInst();
                break;
            case R.id.setting_check_table:
                updateCheckTable();
                break;
            case R.id.setting_sign_out:
                signOut();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDisposables.clear();
    }

    public void showProgressDialog() {
        mProgressDialog = new MaterialDialog.Builder(this)
                .content("加载中...")
                .progress(true, 0)
                .canceledOnTouchOutside(false)
                .show();
    }

    public void cancelProgressDialog() {
        mProgressDialog.cancel();
    }

    private void updateUserAndInst() {
        showProgressDialog();
        mDisposables.add(NetFactory.getInstance()
                .isAvailableByPing()
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean result) throws Exception {
                        if (!result) {
                            cancelProgressDialog();
                            ToastUtil.getInstance().showToast("无法连接到服务器！");
                        } else {
                            mDisposables.add(NetFactory.getInstance()
                                    .getUsersAndInst()
                                    .observeOn(Schedulers.io())
                                    .doOnNext(new Consumer<UserInstBean>() {
                                        @Override
                                        public void accept(UserInstBean bean) throws Exception {
                                            User.updateUserRight();
                                        }
                                    })
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<UserInstBean>() {
                                        @Override
                                        public void accept(UserInstBean bean) throws Exception {
                                            cancelProgressDialog();
                                            ToastUtil.getInstance().showToast("更新成功！");
                                        }
                                    })
                            );
                        }
                    }
                })
        );
    }

    private void updateCheckTable() {
        showProgressDialog();
        mDisposables.add(NetFactory.getInstance()
                .isAvailableByPing()
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean result) throws Exception {
                        if (!result) {
                            cancelProgressDialog();
                            ToastUtil.getInstance().showToast("无法连接到服务器！");
                        } else {
                            mDisposables.add(NetFactory.getInstance()
                                    .getSafetyCheckTables()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<SafetyCheckBean>() {
                                        @Override
                                        public void accept(SafetyCheckBean bean) throws Exception {
                                            cancelProgressDialog();
                                            ToastUtil.getInstance().showToast("更新成功！");
                                        }
                                    })
                            );
                        }
                    }
                })
        );
    }

    private void signOut() {
        new MaterialDialog.Builder(this)
                .content("确认退出当前账号吗？")
                .positiveText("确认")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        SharedPreferences pref = ApplicationUtil.getInstance().getApplicationContext()
                                .getSharedPreferences("mine", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean("login_status", false);
                        editor.apply();
                        //清空所有数据
                        ApplicationUtil.getInstance().dropAllTables();
                        FileUtils.deleteAllImages(getApplicationContext());
                        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).show();
    }

}
