package com.whut.mine.danger.manage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whut.mine.R;
import com.whut.mine.adapter.ManageViewPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ManageFragment extends Fragment {

    @BindView(R.id.manage_tablayout)
    TabLayout mTablayout;
    @BindView(R.id.manage_viewpager)
    ViewPager mViewpager;
    Unbinder unbinder;
    private ManageOverduePresenter mOverduePresenter;
    private ManageTodoPresenter mTodoPresenter;
    private ManageDoingPresenter mDoingPresenter;
    private ManageTodoFragment mTodoFragment;
    private ManageDoingFragment mDoingFragment;
    private ManageOverdueFragment mOverdueFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater
            , @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.manage_fragment, container, false);
        unbinder = ButterKnife.bind(this, root);
        mViewpager.setAdapter(new ManageViewPagerAdapter(getContext(), getChildFragmentManager()
                , mTodoFragment, mDoingFragment, mOverdueFragment));
        mViewpager.setOffscreenPageLimit(ManageViewPagerAdapter.getPageCount());
        mTablayout.setupWithViewPager(mViewpager);
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            FragmentManager manager = getChildFragmentManager();
            mTodoFragment = (ManageTodoFragment) manager.getFragment(savedInstanceState
                    , ManageTodoFragment.class.getSimpleName());
            mDoingFragment = (ManageDoingFragment) manager.getFragment(savedInstanceState
                    , ManageDoingFragment.class.getSimpleName());
            mOverdueFragment = (ManageOverdueFragment) manager.getFragment(savedInstanceState
                    , ManageOverdueFragment.class.getSimpleName());
        } else {
            mTodoFragment = ManageTodoFragment.newInstance();
            mDoingFragment = ManageDoingFragment.newInstance();
            mOverdueFragment = ManageOverdueFragment.newInstance();
        }

        mTodoPresenter = new ManageTodoPresenter(mTodoFragment, getContext());
        mDoingPresenter = new ManageDoingPresenter(mDoingFragment, getContext());
        mOverduePresenter = new ManageOverduePresenter(mOverdueFragment, getContext());
    }

    public static ManageFragment newInstance() {
        return new ManageFragment();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        FragmentManager manager = getChildFragmentManager();
        if (mTodoFragment.isAdded()) {
            manager.putFragment(outState, ManageTodoFragment.class.getSimpleName(), mTodoFragment);
        }
        if (mDoingFragment.isAdded()) {
            manager.putFragment(outState, ManageDoingFragment.class.getSimpleName(), mDoingFragment);
        }
        if (mOverdueFragment.isAdded()) {
            manager.putFragment(outState, ManageOverdueFragment.class.getSimpleName(), mOverdueFragment);
        }
    }

    void destroy() {
        mTodoPresenter.destroy();
        mDoingPresenter.destroy();
        mOverduePresenter.destroy();
        mTodoPresenter = null;
        mDoingPresenter = null;
        mOverduePresenter = null;
    }

}
