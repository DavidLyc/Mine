package com.whut.mine.data;

public class ManageTodoItem {

    private String hiddenID;
    private String checkMemo;
    private String checkInfo;
    private Boolean isSelected;

    public ManageTodoItem(String hiddenID, String checkMemo, String checkInfo) {
        this.hiddenID = hiddenID;
        this.checkMemo = checkMemo;
        this.checkInfo = checkInfo;
        this.isSelected = false;
    }

    public String getHiddenID() {
        return hiddenID;
    }

    public String getCheckMemo() {
        return checkMemo;
    }

    public String getCheckInfo() {
        return checkInfo;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

}
