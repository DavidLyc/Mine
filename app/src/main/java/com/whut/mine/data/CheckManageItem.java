package com.whut.mine.data;

public class CheckManageItem {

    private Long checkTableID;
    private String tableName;
    private String checkTime;
    private Boolean isSelected;

    public CheckManageItem(Long checkTableID, String tableName, String checkTime) {
        this.checkTableID = checkTableID;
        this.tableName = tableName;
        this.checkTime = checkTime;
        this.isSelected = false;
    }

    public String getTableName() {
        return tableName;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public Long getCheckTableID() {
        return checkTableID;
    }

}
