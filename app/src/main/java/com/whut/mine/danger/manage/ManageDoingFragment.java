package com.whut.mine.danger.manage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.whut.mine.R;
import com.whut.mine.adapter.ManageDoingOverAdapter;
import com.whut.mine.data.ManageDoingOverItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManageDoingFragment extends ManageBaseFragment implements ManageDoingContract.View {

    @BindView(R.id.manage_recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.manage_smartRefresh)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.bottom_choice_view)
    RelativeLayout mBottomChoiceView;
    private ManageDoingContract.Presenter mPresenter;

    @Override
    public View initView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manage_page_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        mBottomChoiceView.setVisibility(View.GONE);
        initRefreshLayout();
        initRecyclerview();
        return view;
    }

    @Override
    public void initData() {
        mPresenter.subscribe();
    }

    public static ManageDoingFragment newInstance() {
        return new ManageDoingFragment();
    }

    private void initRefreshLayout() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                clearAllItems();
                mPresenter.loadData();
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mPresenter.loadMoreCacheData();
                refreshlayout.finishLoadmore();
            }
        });
    }

    private void initRecyclerview() {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mListItems = new ArrayList<>();
        mAdapter = new ManageDoingOverAdapter(R.layout.manage_doing_over_item, mListItems);
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setNestedScrollingEnabled(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void setPresenter(ManageDoingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showProgressDialog() {
        mProgressDialog = new MaterialDialog.Builder(getContext())
                .content("通信中...")
                .progress(true, 0)
                .canceledOnTouchOutside(false)
                .show();
    }

    @Override
    public void cancelProgressDialog() {
        mProgressDialog.cancel();
        mRefreshLayout.finishRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showDangerItems(List<ManageDoingOverItem> items) {
        mListItems.addAll(items);
        mAdapter.notifyDataSetChanged();
    }

}
