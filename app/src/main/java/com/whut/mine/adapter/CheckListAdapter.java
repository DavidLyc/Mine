package com.whut.mine.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.whut.mine.data.CheckListItem;
import com.whut.mine.R;

import java.util.List;

public class CheckListAdapter extends BaseQuickAdapter<CheckListItem, BaseViewHolder> {

    public CheckListAdapter(@LayoutRes int layoutResId, @Nullable List<CheckListItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CheckListItem item) {
        helper.setText(R.id.checklist_item_text, item.getTitle());
    }

}
