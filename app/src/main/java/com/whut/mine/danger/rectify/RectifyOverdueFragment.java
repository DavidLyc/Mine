package com.whut.mine.danger.rectify;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.whut.mine.R;
import com.whut.mine.adapter.RectifyListOverdueAdapter;
import com.whut.mine.album.AlbumActivity;
import com.whut.mine.base.BaseFragment;
import com.whut.mine.data.RectifyListItem;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class RectifyOverdueFragment extends BaseFragment implements RectifyOverdueContract.View {

    @BindView(R.id.rectify_recyclerview)
    RecyclerView mRecyclerview;
    Unbinder unbinder;
    @BindView(R.id.rectify_smartRefresh)
    SmartRefreshLayout mRefreshLayout;
    private RectifyOverdueContract.Presenter mPresenter;
    private MaterialDialog mDialog;
    private List<RectifyListItem> mListItems;
    private RectifyListOverdueAdapter mAdapter;
    private RectifyListItem saveItem;
    private RectifyListItem photoItem;

    @Override
    public View initView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rectify_content_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        initRefreshLayout();
        initRecyclerview();
        return view;
    }

    @Override
    public void initData() {
        mPresenter.subscribe();
    }

    public static RectifyOverdueFragment newInstance() {
        return new RectifyOverdueFragment();
    }


    public void initRefreshLayout() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mListItems.clear();
                mPresenter.loadData();
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (mPresenter.loadMoreCacheData()) {
                    refreshlayout.finishLoadmore();
                } else {
                    refreshlayout.finishLoadmoreWithNoMoreData();
                }
            }
        });
    }

    @Override
    public void setPresenter(@NonNull RectifyOverdueContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private void initRecyclerview() {
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mListItems = new ArrayList<>();
        mAdapter = new RectifyListOverdueAdapter(R.layout.rectify_list_item, this, mListItems);
        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setNestedScrollingEnabled(false);
    }

    @Override
    public void showProgressDialog() {
        mDialog = new MaterialDialog.Builder(getContext())
                .content("通信中...")
                .progress(true, 0)
                .canceledOnTouchOutside(false)
                .show();
    }

    @Override
    public void showOverdueAlarms(List<RectifyListItem> items) {
        mListItems.addAll(items);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showNoData() {
        mRefreshLayout.resetNoMoreData();
    }

    @Override
    public void selectAllItems() {
        for (RectifyListItem item : mListItems) {
            item.setSelected(true);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void clearAllSelectedItems() {
        for (RectifyListItem item : mListItems) {
            item.setSelected(false);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void takePhoto(final RectifyListItem item) {
        saveItem = item;
        Album.camera(this)
                .image()
                .onResult(new Action<String>() {
                    @Override
                    public void onAction(int requestCode, @NonNull String result) {
                        mPresenter.saveShotPhoto(result, item);
                    }
                }).start();
    }

    @Override
    public void takePhotoFromAlbum(final RectifyListItem item) {
        saveItem = item;
        Album.image(this)
                .multipleChoice()
                .camera(false)
                .columnCount(3)
                .selectCount(9 - item.getPhotoUrl().size())
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(int requestCode, @NonNull ArrayList<AlbumFile> result) {
                        mPresenter.saveAlbumPhoto(result, item);
                    }
                })
                .start();
    }

    @Override
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    @Override
    public void showPhotoInfo(RectifyListItem item) {
        if (item.getPhotoUrl().size() == 0) {
            Toast.makeText(getContext(), "还没有可查看的图片", Toast.LENGTH_SHORT).show();
        } else {
            photoItem = item;
            Intent intent = new Intent(getActivity(), AlbumActivity.class);
            intent.putStringArrayListExtra("photo_url", (ArrayList<String>) item.getPhotoUrl());
            startActivityForResult(intent, 1);
        }
    }

    @Override
    public void cancelProgressDialog() {
        mDialog.cancel();
        mRefreshLayout.finishRefresh();
    }

    @Override
    public void addPhotoToRectifyListItem(List<String> labelPhotoUrls) {
        for (String url : labelPhotoUrls) {
            saveItem.addPhoto(url);
        }
    }

    @Override
    public void showRectifyTip() {
        final List<RectifyListItem> itemList = mAdapter.getSelectedItemList();
        if (itemList.size() == 0) {
            Toast.makeText(getContext(), "请至少选择一条整改记录", Toast.LENGTH_SHORT).show();
            return;
        }
        for (RectifyListItem item : itemList) {
            if (item.getRectifyDescription().trim().isEmpty()) {
                Toast.makeText(getContext(), "有未填写的整改说明，无法提交！", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        new MaterialDialog.Builder(getContext())
                .title("确定提交隐患的整改信息吗？")
                .positiveText("确认")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mPresenter.saveRectifyInfo(itemList);
                    }
                }).show();
    }

    @Override
    public void clearAllItems() {
        mListItems.clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    List<String> urls = data.getStringArrayListExtra("photo_url_return");
                    photoItem.setPhotoUrl(urls);
                }
                break;
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
                showRectifyTip();
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
