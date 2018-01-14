package com.whut.mine.check.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.whut.mine.R;
import com.whut.mine.adapter.CheckListAdapter;
import com.whut.mine.check.detail.CheckDetailActivity;
import com.whut.mine.data.CheckListItem;
import com.whut.mine.ui.DividerDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CheckListFragment extends Fragment implements CheckListContract.View {

    @BindView(R.id.checklist_recyclerview)
    RecyclerView mRecyclerview;
    Unbinder unbinder;
    private List<CheckListItem> mListItems;
    private CheckListContract.Presenter mPresenter;
    private CheckListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater
            , @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.check_list_view, container, false);
        unbinder = ButterKnife.bind(this, root);
        initRecyclerview();
        return root;
    }

    public static CheckListFragment newInstance() {
        return new CheckListFragment();
    }

    @Override
    public void setPresenter(CheckListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void cancelProgressDialog() {

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    private void initRecyclerview() {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mListItems = new ArrayList<>();
        mAdapter = new CheckListAdapter(R.layout.check_list_item, mListItems);
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setNestedScrollingEnabled(false);
        mRecyclerview.addItemDecoration(new DividerDecoration(getContext()));
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), CheckDetailActivity.class);
                String title = mListItems.get(position).getTitle();
                Long checkTableID = mListItems.get(position).getCheckTableID();
                intent.putExtra("check_title", title);
                intent.putExtra("checktableid", checkTableID);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void setRecyclerViewItems(List<CheckListItem> items) {
        mListItems.clear();
        mListItems.addAll(items);
        mAdapter.notifyDataSetChanged();
    }

}
