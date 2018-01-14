package com.whut.mine.check.manage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.whut.mine.R;
import com.whut.mine.util.ActivityUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckManageActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    CheckManagePresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_manage);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        setTitle("");
        mToolbarTitle.setText("检查管理");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        CheckManageFragment checkManageFragment = (CheckManageFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_check_manage);
        if (checkManageFragment == null) {
            checkManageFragment = CheckManageFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    checkManageFragment, R.id.fragment_check_manage);
        }

        mPresenter = new CheckManagePresenter(checkManageFragment, this);

    }

    @Override
    protected void onDestroy() {
        mPresenter.destroy();
        mPresenter = null;
        super.onDestroy();
    }

}
