package com.whut.mine.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.whut.mine.R;
import com.whut.mine.danger.rectify.RectifyDoingFragment;
import com.whut.mine.data.RectifyListItem;
import com.whut.mine.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class RectifyListSolvingAdapter extends BaseQuickAdapter<RectifyListItem, BaseViewHolder> {

    private View mTempFocusView;
    private RectifyDoingFragment mView;

    public RectifyListSolvingAdapter(@LayoutRes int layoutResId, @Nullable RectifyDoingFragment fragment, @Nullable List<RectifyListItem> data) {
        super(layoutResId, data);
        mView = fragment;
        mContext = fragment.getContext();
        this.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                mView.hideKeyboard();
                requestScreenFocus(view);
                int viewId = view.getId();
                switch (viewId) {
                    case R.id.rectify_list_item_photo:
                        doWithPhoto(position);
                        break;
                    case R.id.rectify_list_item_photoTip:
                        doWithPhoto(position);
                        break;
                    case R.id.rectify_list_item_watch_photo:
                        mView.showPhotoInfo(getItem(position));
                        break;
                    case R.id.rectify_list_item_choose_circle:
                        changeSelectedStatus(position);
                        break;
                }
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder helper, final RectifyListItem item) {
        helper.setText(R.id.rectify_list_item_checkMemo, "隐患描述：" + item.getCheckmemo())
                .setText(R.id.rectify_list_item_rectifyInfo, "详情：" + item.getChecktime() + "/" + item.getCheckCatogoryNum() + "/" + item.getInstructionNum() + "/" + item.getConfirmPersonInstitution())
                .setText(R.id.rectify_list_item_rectifactionEndDate, "整改截止日期：" + item.getRectifactionEndDate())
                .setImageResource(R.id.rectify_list_item_choose_circle, item.getSelected() ? R.drawable.ic_choose : R.drawable.ic_circle)
                .addOnClickListener(R.id.rectify_list_item_photo)
                .addOnClickListener(R.id.rectify_list_item_watch_photo)
                .addOnClickListener(R.id.rectify_list_item_photoTip)
                .addOnClickListener(R.id.rectify_list_item_choose_circle);
        final EditText editText = helper.getView(R.id.rectify_list_item_rectifyDescription);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                item.setRectifyDescription(editable.toString().trim());
            }
        });


    }

    private void requestScreenFocus(View view) {
        if (mTempFocusView != null) {
            mTempFocusView.clearFocus();
        }
        mTempFocusView = view;
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    private void doWithPhoto(int position) {
        final RectifyListItem listItem = getItem(position);
        assert listItem != null;
        if (listItem.getPhotoUrl().size() >= 9) {
            ToastUtil.getInstance().showToast("同一整改项最多上传9张照片！");
        } else {
            new MaterialDialog.Builder(mContext)
                    .items(R.array.photo_choice)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                            if (which == 0) {
                                mView.takePhoto(listItem);
                            } else {
                                mView.takePhotoFromAlbum(listItem);
                            }
                        }
                    }).show();
        }
    }

    private void changeSelectedStatus(final int pos) {
        RectifyListItem item = getData().get(pos);
        item.setSelected(!item.getSelected());
        notifyItemChanged(pos);
    }

    public List<RectifyListItem> getSelectedItemList() {
        List<RectifyListItem> items = new ArrayList<>();
        List<RectifyListItem> itemList = getData();
        for (RectifyListItem item : itemList) {
            if (item.getSelected()) {
                items.add(item);
            }
        }
        return items;
    }

}
