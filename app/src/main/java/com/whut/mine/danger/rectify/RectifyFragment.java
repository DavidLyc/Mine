package com.whut.mine.danger.rectify;

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
import com.whut.mine.adapter.RectifyViewPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RectifyFragment extends Fragment {

    @BindView(R.id.rectify_tablayout)
    TabLayout mTablayout;
    @BindView(R.id.rectify_viewpager)
    ViewPager mViewpager;
    Unbinder unbinder;
    private RectifyDoingFragment mDoingFragment;
    private RectifyOverdueFragment mOverdueFragment;
    private RectifyOverduePresenter mOverduePresenter;
    private RectifyDoingPresenter mDoingPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.rectify_fragment, container, false);
        unbinder = ButterKnife.bind(this, root);
        mViewpager.setAdapter(new RectifyViewPagerAdapter(getContext(), getChildFragmentManager()
                , mDoingFragment, mOverdueFragment));
        mViewpager.setOffscreenPageLimit(RectifyViewPagerAdapter.getPageCount());
        mTablayout.setupWithViewPager(mViewpager);
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            FragmentManager manager = getChildFragmentManager();
            mDoingFragment = (RectifyDoingFragment) manager.getFragment(savedInstanceState
                    , RectifyDoingFragment.class.getSimpleName());
            mOverdueFragment = (RectifyOverdueFragment) manager.getFragment(savedInstanceState
                    , RectifyOverdueFragment.class.getSimpleName());
        } else {
            mDoingFragment = RectifyDoingFragment.newInstance();
            mOverdueFragment = RectifyOverdueFragment.newInstance();
        }

        mDoingPresenter = new RectifyDoingPresenter(mDoingFragment, getContext());
        mOverduePresenter = new RectifyOverduePresenter(mOverdueFragment, getContext());
    }

    public static RectifyFragment newInstance() {
        return new RectifyFragment();
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
        if (mDoingFragment.isAdded()) {
            manager.putFragment(outState, RectifyDoingFragment.class.getSimpleName(), mDoingFragment);
        }
        if (mOverdueFragment.isAdded()) {
            manager.putFragment(outState, RectifyOverdueFragment.class.getSimpleName(), mOverdueFragment);
        }
    }

    void destroy() {
        mDoingPresenter.destroy();
        mOverduePresenter.destroy();
        mOverduePresenter = null;
        mDoingPresenter = null;
    }

}
