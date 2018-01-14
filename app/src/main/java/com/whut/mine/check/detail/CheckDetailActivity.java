package com.whut.mine.check.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.whut.mine.R;
import com.whut.mine.base.BaseActivity;
import com.whut.mine.util.ActivityUtils;
import com.whut.mine.util.ApplicationUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckDetailActivity extends AppCompatActivity implements BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    CheckDetailPresenter mPresenter;
    private Boolean mIsSaved = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_detail);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        ButterKnife.bind(this);
        initToolbar();

        CheckDetailFragment checkDetailFragment = (CheckDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.check_detail_fragment);
        if (checkDetailFragment == null) {
            checkDetailFragment = CheckDetailFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    checkDetailFragment, R.id.check_detail_fragment);
        }

        mPresenter = new CheckDetailPresenter(checkDetailFragment, this);
    }

    @Override
    public void initToolbar() {
        setSupportActionBar(mToolbar);
        setTitle("");
        String title = getIntent().getStringExtra("check_title") + "检查表";
        mToolbarTitle.setText(title);
        mToolbar.setNavigationIcon(R.drawable.back_arrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!mIsSaved) {
            new MaterialDialog.Builder(this)
                    .content("建议退出前进行保存操作！")
                    .positiveText("继续退出")
                    .negativeText("取消")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                            finish();
                        }
                    }).show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        mPresenter.destroy();
        mPresenter = null;
        ApplicationUtil.getInstance().fixInputMethodManagerLeak(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.checktable_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_checktable:
                mPresenter.saveCheckTableInDao(true);
                mIsSaved = true;
                break;
            case R.id.push_checktable:
                mPresenter.postCheckTable();
                break;
        }
        return true;
    }

}
