package com.whut.mine.danger.manage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.whut.mine.R;
import com.whut.mine.util.ActivityUtils;
import com.whut.mine.util.ApplicationUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManageActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private ManageFragment mManageFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger_manage);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        setTitle("");
        mToolbarTitle.setText("隐患管理");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mManageFragment = (ManageFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_manage);
        if (mManageFragment == null) {
            mManageFragment = ManageFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mManageFragment, R.id.fragment_manage);
        }
    }

    @Override
    protected void onDestroy() {
        mManageFragment.destroy();
        mManageFragment = null;
        ApplicationUtil.getInstance().fixInputMethodManagerLeak(this);
        super.onDestroy();
    }

}
