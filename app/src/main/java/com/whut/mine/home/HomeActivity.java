package com.whut.mine.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.whut.mine.R;
import com.whut.mine.adapter.HomeAdapter;
import com.whut.mine.check.list.CheckListActivity;
import com.whut.mine.check.manage.CheckManageActivity;
import com.whut.mine.danger.manage.ManageActivity;
import com.whut.mine.danger.rectify.RectifyActivity;
import com.whut.mine.data.HomeItem;
import com.whut.mine.home.setting.SettingActivity;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.home_recyclerView)
    RecyclerView mRecyclerView;
    private Boolean[] mUserRight = new Boolean[4];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().getDecorView().setSystemUiVisibility(
                          View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        ButterKnife.bind(this);
        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initUserRight();
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        List<HomeItem> homeItems = Arrays.asList(
                new HomeItem(R.drawable.icon_homelist_item1),
                new HomeItem(R.drawable.icon_homelist_item2),
                new HomeItem(R.drawable.icon_homelist_item3),
                new HomeItem(R.drawable.icon_homelist_item4)
        );
        HomeAdapter adapter = new HomeAdapter(R.layout.homeitem_view, homeItems);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!mUserRight[position]) {
                    Toast.makeText(HomeActivity.this, "抱歉，您没有权限使用该功能！", Toast.LENGTH_SHORT).show();
                    return;
                }
                switch (position) {
                    case 0:
                        startActivity(new Intent(HomeActivity.this, CheckListActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(HomeActivity.this, RectifyActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(HomeActivity.this, ManageActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(HomeActivity.this, CheckManageActivity.class));
                        break;
                }
            }
        });
    }

    @OnClick(R.id.home_bottombar_button)
    public void onViewClicked() {
        startActivity(new Intent(HomeActivity.this, SettingActivity.class));
    }

    private void initUserRight() {
        SharedPreferences pref = getSharedPreferences("mine", MODE_PRIVATE);
        String prefRight = pref.getString("user_right", "user_right");
        mUserRight[0] = prefRight.charAt(0) == '1';
        mUserRight[1] = prefRight.charAt(4) == '1';
        mUserRight[2] = prefRight.charAt(2) == '1';
        mUserRight[3] = prefRight.charAt(6) == '1';
    }

}
