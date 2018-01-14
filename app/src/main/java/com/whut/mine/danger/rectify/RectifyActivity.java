package com.whut.mine.danger.rectify;

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

public class RectifyActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private RectifyFragment mFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger_rectify);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        setTitle("");
        mToolbarTitle.setText("隐患整改");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mFragment = (RectifyFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_rectify);
        if (mFragment == null) {
            mFragment = RectifyFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragment, R.id.fragment_rectify);
        }
    }

    @Override
    protected void onDestroy() {
        mFragment.destroy();
        mFragment = null;
        ApplicationUtil.getInstance().fixInputMethodManagerLeak(this);
        super.onDestroy();
    }

}
