package com.whut.mine.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.whut.mine.danger.rectify.RectifyDoingFragment;
import com.whut.mine.danger.rectify.RectifyOverdueFragment;

public class RectifyViewPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private static final int PAGE_COUNT = 2;
    private RectifyDoingFragment mDoingFragment;
    private RectifyOverdueFragment mOverdueFragment;

    public RectifyViewPagerAdapter(@NonNull Context context
            , @NonNull FragmentManager manager
            , @NonNull RectifyDoingFragment doingFragment
            , @NonNull RectifyOverdueFragment overdueFragment) {
        super(manager);
        mContext = context;
        mDoingFragment = doingFragment;
        mOverdueFragment = overdueFragment;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return mDoingFragment;
        } else {
            return mOverdueFragment;
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "整改中";
        } else {
            return "已逾期";
        }
    }

    public static int getPageCount() {
        return PAGE_COUNT;
    }

}
