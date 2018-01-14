package com.whut.mine.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.whut.mine.R;
import com.whut.mine.data.ManageDoingOverItem;

import java.util.List;

public class ManageDoingOverAdapter extends BaseQuickAdapter<ManageDoingOverItem, BaseViewHolder> {

    public ManageDoingOverAdapter(int layoutResId, @Nullable List<ManageDoingOverItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ManageDoingOverItem item) {
        helper.setText(R.id.manage_doing_over_checkMemo, item.getCheckMemo())
                .setText(R.id.manage_doing_over_checkInfo, item.getCheckInfo())
                .setText(R.id.manage_doing_over_checkEndDate, item.getEndDate());
    }

}
