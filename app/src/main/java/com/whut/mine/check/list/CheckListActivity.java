package com.whut.mine.check.list;

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

public class CheckListActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    CheckListPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        setTitle("");
        mToolbarTitle.setText("安全检查表列表");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        CheckListFragment checkListFragment = (CheckListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.check_list_fragment);
        if (checkListFragment == null) {
            checkListFragment = CheckListFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    checkListFragment, R.id.check_list_fragment);
        }

        mPresenter = new CheckListPresenter(checkListFragment, this);
    }

    @Override
    protected void onDestroy() {
        mPresenter.destroy();
        mPresenter = null;
        super.onDestroy();
    }

}
