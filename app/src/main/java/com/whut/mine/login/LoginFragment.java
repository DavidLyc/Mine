package com.whut.mine.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.whut.mine.R;
import com.whut.mine.home.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginFragment extends Fragment implements LoginContract.View {

    @BindView(R.id.login_fragment_account)
    EditText mAccount;
    @BindView(R.id.login_fragment_pwd)
    EditText mPassword;
    Unbinder unbinder;
    private LoginContract.Presenter mPresenter;
    private MaterialDialog mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container
            , Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.login_fragment, container, false);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showProgressDialog() {
        mProgressDialog = new MaterialDialog.Builder(getContext())
                .content("验证中...")
                .progress(true, 0)
                .canceledOnTouchOutside(false)
                .show();
    }

    @Override
    public void cancelProgressDialog() {
        mProgressDialog.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @OnClick(R.id.login_press)
    public void onViewClicked() {
        String account = String.valueOf(mAccount.getText());
        String password = String.valueOf(mPassword.getText());
        mPresenter.doLogin(account, password);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void enterHomePage() {
        Toast.makeText(getContext(), "登陆成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

}
