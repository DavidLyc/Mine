package com.whut.mine.data;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.whut.mine.adapter.CheckTableAdapter;

public class CheckTableFirstItem extends AbstractExpandableItem<CheckTableSecondItem> implements MultiItemEntity {

    private String firstIndexTitle;
    private Long firstIndexID;

    public CheckTableFirstItem(String firstIndexTitle, Long firstIndexID) {
        this.firstIndexTitle = firstIndexTitle;
        this.firstIndexID = firstIndexID;
    }

    public String getFirstIndexTitle() {
        return firstIndexTitle;
    }

    @Override
    public int getItemType() {
        return CheckTableAdapter.TYPE_FIRSTINDEX;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    public Long getFirstIndexID() {
        return firstIndexID;
    }

    public void setFirstIndexID(Long firstIndexID) {
        this.firstIndexID = firstIndexID;
    }

}
