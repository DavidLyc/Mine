package com.whut.mine.data;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.whut.mine.adapter.CheckTableAdapter;

public class CheckTableThirdItem implements MultiItemEntity {

    @Override
    public int getItemType() {
        return CheckTableAdapter.TYPE_THIRDINDEX;
    }

}
