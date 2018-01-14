package com.whut.mine.danger.manage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.whut.mine.R;
import com.whut.mine.adapter.ManageTodoAdapter;
import com.whut.mine.data.ManageTodoItem;
import com.whut.mine.entity.Institution;
import com.whut.mine.entity.User;
import com.whut.mine.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManageTodoFragment extends ManageBaseFragment implements ManageTodoContract.View {

    @BindView(R.id.manage_recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.manage_smartRefresh)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.bottom_choice_view)
    RelativeLayout mBottomChoiceView;
    private ManageTodoContract.Presenter mPresenter;
    private ManageTodoAdapter mAdapter;
    private List<ManageTodoItem> mListItems;
    private EditText mEditTextPeople;
    private RectifyMsg mRectifyMsg;

    @Override
    public View initView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manage_page_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        initRefreshLayout();
        initRecyclerview();
        return view;
    }

    @Override
    public void initData() {
        mPresenter.subscribe();
    }

    public static ManageTodoFragment newInstance() {
        return new ManageTodoFragment();
    }

    private void initRecyclerview() {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mListItems = new ArrayList<>();
        mAdapter = new ManageTodoAdapter(R.layout.manage_todo_item, mListItems);
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setNestedScrollingEnabled(false);
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
                refreshlayout.finishLoadmore();
                mPresenter.loadMoreCacheData();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void setPresenter(ManageTodoContract.Presenter presenter) {
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
    public void showDangerItems(List<ManageTodoItem> items) {
        mListItems.addAll(items);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void selectAllItems() {
        for (ManageTodoItem item : mListItems) {
            item.setSelected(true);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void clearAllSelectedItems() {
        for (ManageTodoItem item : mListItems) {
            item.setSelected(false);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void clearAllItems() {
        if (mListItems != null) {
            mListItems.clear();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showRectifyInstructionDialog() {
        if (mAdapter.getSelectedItemList().size() == 0) {
            ToastUtil.getInstance().showToast("请选择至少1条隐患！");
            return;
        }
        mRectifyMsg = new RectifyMsg();
        final TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mRectifyMsg.setInputPeopleName(charSequence.toString());
                mRectifyMsg.setRecPeopleNum("");
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
        MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                .title("发送整改命令")
                .customView(R.layout.manage_todo_customview, true)
                .positiveText("确认")
                .negativeText("取消")
                .canceledOnTouchOutside(true)
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        //防止内存泄露
                        mEditTextPeople.removeTextChangedListener(watcher);
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog1, @NonNull DialogAction action) {
                        //提交指令
                        if (!mRectifyMsg.getRecInstNum().isEmpty() && !mRectifyMsg.getRecPeopleNum().isEmpty()) {
                            mPresenter.sendRectInstruction(mAdapter.getSelectedItemList(), mRectifyMsg);
                        } else if (!mRectifyMsg.getInputPeopleName().trim().isEmpty()) {
                            List<String> searchUserList = User.getUserInfoBySearchName(mRectifyMsg.getInputPeopleName()
                                    , mRectifyMsg.getRecInstNum());
                            if (searchUserList == null) {
                                ToastUtil.getInstance().showToast("该部门下不存在该整改负责人！");
                            } else if (searchUserList.size() == 1) {
                                mRectifyMsg.setRecPeopleNum(User.getUserNumByName(mRectifyMsg.getInputPeopleName()));
                                mPresenter.sendRectInstruction(mAdapter.getSelectedItemList(), mRectifyMsg);
                            } else {
                                showPeopleTip(mPresenter.searchPeopleTip(mRectifyMsg.getInputPeopleName()
                                        , mRectifyMsg.getRecInstNum()), true);
                            }
                        } else {
                            ToastUtil.getInstance().showToast("整改信息填写不完整！");
                        }
                    }
                }).build();
        //spinner
        MaterialSpinner spinner = dialog.getCustomView().findViewById(R.id.manage_todo_customview_spinner);
        final List<String> institutionList = Institution.getAllInstitution();
        spinner.setItems(institutionList);
        //默认值
        mRectifyMsg.setRecInstNum(Institution.getInstitutionNumByName(institutionList.get(0)));
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                mRectifyMsg.setRecInstNum(Institution.getInstitutionNumByName(String.valueOf(item)));
                mRectifyMsg.setRecPeopleNum("");
            }
        });
        //editText
        mEditTextPeople = dialog.getCustomView().findViewById(R.id.manage_todo_customview_rectify_people_edit);
        mEditTextPeople.addTextChangedListener(watcher);
        dialog.getCustomView().findViewById(R.id.manage_todo_customview_rectify_people_search)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showPeopleTip(mPresenter.searchPeopleTip(mRectifyMsg.getInputPeopleName()
                                , mRectifyMsg.getRecInstNum()), false);
                    }
                });
        dialog.show();
    }

    @Override
    public void showPeopleTip(final CharSequence[] peopleList, final Boolean isSending) {
        if (peopleList == null || peopleList.length == 0) {
            ToastUtil.getInstance().showToast("没有符合条件的查询结果！");
        } else {
            new MaterialDialog.Builder(getContext())
                    .items(peopleList)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View v, int i, CharSequence sequence) {
                            mEditTextPeople.setText(peopleList[i].toString().split(" {2}")[0]);
                            mRectifyMsg.setRecPeopleNum(peopleList[i].toString().split(" {2}")[1]);
                            if (isSending) {
                                mPresenter.sendRectInstruction(mAdapter.getSelectedItemList(), mRectifyMsg);
                            }
                        }
                    }).show();
        }
    }

    @Override
    public void showErrorDialog() {
        new MaterialDialog.Builder(getContext())
                .content("网络异常，提交失败！")
                .positiveText("确定")
                .show();
    }

    @OnClick({R.id.bottom_choice_rectify, R.id.bottom_choice_all_select, R.id.bottom_choice_cancel_all_select
            , R.id.bottom_choice_rectify_text, R.id.bottom_choice_all_select_text
            , R.id.bottom_choice_cancel_all_select_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bottom_choice_rectify:
            case R.id.bottom_choice_rectify_text:
                showRectifyInstructionDialog();
                break;
            case R.id.bottom_choice_all_select:
            case R.id.bottom_choice_all_select_text:
                selectAllItems();
                break;
            case R.id.bottom_choice_cancel_all_select:
            case R.id.bottom_choice_cancel_all_select_text:
                clearAllSelectedItems();
                break;
        }
    }

}
