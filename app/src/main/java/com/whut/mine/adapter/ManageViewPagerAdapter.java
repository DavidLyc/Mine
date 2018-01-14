package com.whut.mine.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.whut.mine.danger.manage.ManageDoingFragment;
import com.whut.mine.danger.manage.ManageOverdueFragment;
import com.whut.mine.danger.manage.ManageTodoFragment;

public class ManageViewPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private static final int PAGE_COUNT = 3;
    private ManageTodoFragment mTodoFragment;
    private ManageDoingFragment mDoingFragment;
    private ManageOverdueFragment mOverdueFragment;

    public ManageViewPagerAdapter(@NonNull Context context
            , @NonNull FragmentManager manager
            , @NonNull ManageTodoFragment todoFragment
            , @NonNull ManageDoingFragment doingFragment
            , @NonNull ManageOverdueFragment overdueFragment) {
        super(manager);
        mContext = context;
        mTodoFragment = todoFragment;
        mDoingFragment = doingFragment;
        mOverdueFragment = overdueFragment;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;
        switch (i) {
            case 0:
                fragment = mTodoFragment;
                break;
            case 1:
                fragment = mDoingFragment;
                break;
            case 2:
                fragment = mOverdueFragment;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "未处理";
            case 1:
                return "整改中";
            case 2:
                return "已逾期";
        }
        return null;
    }

    public static int getPageCount() {
        return PAGE_COUNT;
    }

}
