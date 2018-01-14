package com.whut.mine.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.whut.mine.R;
import com.whut.mine.data.ManageTodoItem;

import java.util.ArrayList;
import java.util.List;

public class ManageTodoAdapter extends BaseQuickAdapter<ManageTodoItem, BaseViewHolder> {

    public ManageTodoAdapter(@LayoutRes int layoutResId, @Nullable List<ManageTodoItem> data) {
        super(layoutResId, data);

        setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int pos) {
                int viewId = view.getId();
                switch (viewId) {
                    case R.id.manage_todo_select:
                        changeSelectedStatus(pos);
                        break;
                }
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder helper, ManageTodoItem item) {
        helper.setText(R.id.manage_todo_checkMemo, item.getCheckMemo())
                .setText(R.id.manage_todo_checkInfo, item.getCheckInfo())
                .setImageResource(R.id.manage_todo_select, item.getSelected() ?
                        R.drawable.ic_choose : R.drawable.ic_circle)
                .addOnClickListener(R.id.manage_todo_select);
    }

    private void changeSelectedStatus(final int pos) {
        ManageTodoItem item = getData().get(pos);
        item.setSelected(!item.getSelected());
        notifyItemChanged(pos);
    }

    public List<ManageTodoItem> getSelectedItemList() {
        List<ManageTodoItem> selectedList = new ArrayList<>();
        List<ManageTodoItem> itemList = getData();
        for (ManageTodoItem item : itemList) {
            if (item.getSelected()) {
                selectedList.add(item);
            }
        }
        return selectedList;
    }

}
