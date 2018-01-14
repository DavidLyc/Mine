package com.whut.mine.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.whut.mine.R;
import com.whut.mine.check.detail.CheckDetailActivity;
import com.whut.mine.data.CheckManageItem;

import java.util.ArrayList;
import java.util.List;

public class CheckManageAdapter extends BaseQuickAdapter<CheckManageItem, BaseViewHolder> {

    public CheckManageAdapter(int layoutResId, @Nullable List<CheckManageItem> data) {
        super(layoutResId, data);

        setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int pos) {
                int viewId = view.getId();
                switch (viewId) {
                    case R.id.check_manage_select:
                        changeSelectedStatus(pos);
                        break;
                }
            }
        });
    }

    @Override
    protected void convert(final BaseViewHolder helper, CheckManageItem item) {
        helper.setText(R.id.check_manage_tablename, item.getTableName())
                .setText(R.id.check_manage_check_time, item.getCheckTime())
                .setImageResource(R.id.check_manage_select, item.getSelected() ?
                        R.drawable.ic_choose : R.drawable.ic_circle)
                .addOnClickListener(R.id.check_manage_select)
                .itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCheckDetail(helper.getAdapterPosition());
            }
        });
    }

    private void changeSelectedStatus(final int pos) {
        CheckManageItem item = getData().get(pos);
        item.setSelected(!item.getSelected());
        notifyItemChanged(pos);
    }

    private void openCheckDetail(final int pos) {
        CheckManageItem item = getData().get(pos);
        Intent intent = new Intent(mContext, CheckDetailActivity.class);
        intent.putExtra("check_title", item.getTableName());
        intent.putExtra("checktableid", item.getCheckTableID());
        intent.putExtra("checktime", item.getCheckTime().substring(5));
        mContext.startActivity(intent);
    }

    public List<CheckManageItem> getSelectedItemList() {
        List<CheckManageItem> selectedList = new ArrayList<>();
        List<CheckManageItem> itemList = getData();
        for (CheckManageItem item : itemList) {
            if (item.getSelected()) {
                selectedList.add(item);
            }
        }
        return selectedList;
    }

}
