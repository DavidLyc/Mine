package com.whut.mine.check.manage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.whut.mine.R;
import com.whut.mine.adapter.CheckManageAdapter;
import com.whut.mine.data.CheckManageItem;
import com.whut.mine.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CheckManageFragment extends Fragment implements CheckManageContract.View {

    @BindView(R.id.check_manage_recyclerview)
    RecyclerView mRecyclerview;
    Unbinder unbinder;
    @BindView(R.id.manage_check_nodataTip)
    TextView mNoDataTip;
    private CheckManageContract.Presenter mPresenter;
    private MaterialDialog mProgressDialog;
    private List<CheckManageItem> mListItems;
    private CheckManageAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater
            , @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.check_manage_fragment, container, false);
        unbinder = ButterKnife.bind(this, root);
        initRecyclerview();
        return root;
    }

    private void initRecyclerview() {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mListItems = new ArrayList<>();
        mAdapter = new CheckManageAdapter(R.layout.check_manage_item, mListItems);
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setNestedScrollingEnabled(false);
    }

    public static CheckManageFragment newInstance() {
        return new CheckManageFragment();
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

    @Override
    public void setPresenter(CheckManageContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showProgressDialog() {
        mProgressDialog = new MaterialDialog.Builder(getContext())
                .content("提交中...")
                .progress(true, 0)
                .canceledOnTouchOutside(false)
                .show();
    }

    @Override
    public void cancelProgressDialog() {
        mProgressDialog.cancel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void setRecyclerViewItems(List<CheckManageItem> items) {
        mListItems.clear();
        if (items.size() != 0) {
            mListItems.addAll(items);
        } else {
            mNoDataTip.setText("暂无已保存的检查记录!");
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void selectAllItems() {
        for (CheckManageItem item : mListItems) {
            item.setSelected(true);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void clearAllSelectedItems() {
        for (CheckManageItem item : mListItems) {
            item.setSelected(false);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteSelectedItems() {
        final List<CheckManageItem> checkManageItems = mAdapter.getSelectedItemList();
        if (checkManageItems.size() == 0) {
            ToastUtil.getInstance().showToast("请至少选中1条检查记录！");
            return;
        }
        new MaterialDialog.Builder(getContext())
                .content("确认要删除所选中的" + checkManageItems.size() + "条检查记录吗？")
                .positiveText("确认")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        mPresenter.deleteCheckRecord(checkManageItems);
                    }
                }).show();
    }

    @OnClick({R.id.check_manage_upload, R.id.check_manage_all_select, R.id.check_manage_cancel_all_select
            , R.id.check_manage_delete, R.id.check_manage_upload_text, R.id.check_manage_all_select_text
            , R.id.check_manage_cancel_all_select_text, R.id.check_manage_delete_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.check_manage_upload:
            case R.id.check_manage_upload_text:
                mPresenter.sendJson(mAdapter.getSelectedItemList());
                break;
            case R.id.check_manage_all_select:
            case R.id.check_manage_all_select_text:
                selectAllItems();
                break;
            case R.id.check_manage_cancel_all_select:
            case R.id.check_manage_cancel_all_select_text:
                clearAllSelectedItems();
                break;
            case R.id.check_manage_delete:
            case R.id.check_manage_delete_text:
                deleteSelectedItems();
                break;
        }
    }

}
