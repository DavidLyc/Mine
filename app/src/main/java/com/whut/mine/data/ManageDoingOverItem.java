package com.whut.mine.data;

public class ManageDoingOverItem {

    private String checkMemo;
    private String checkInfo;
    private String endDate;

    public ManageDoingOverItem(String checkMemo, String checkInfo, String endDate) {
        this.checkMemo = checkMemo;
        this.checkInfo = checkInfo;
        this.endDate = endDate;
    }

    public String getCheckMemo() {
        return checkMemo;
    }

    public void setCheckMemo(String checkMemo) {
        this.checkMemo = checkMemo;
    }

    public String getCheckInfo() {
        return checkInfo;
    }

    public void setCheckInfo(String checkInfo) {
        this.checkInfo = checkInfo;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

}
