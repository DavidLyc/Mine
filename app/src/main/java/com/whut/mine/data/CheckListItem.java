package com.whut.mine.data;

public class CheckListItem {

    private String title;
    private Long checkTableID;

    public CheckListItem(String title, Long checkTableID) {
        this.title = title;
        this.checkTableID = checkTableID;
    }

    public String getTitle() {
        return title;
    }

    public Long getCheckTableID() {
        return checkTableID;
    }

    public void setCheckTableID(Long checkTableID) {
        this.checkTableID = checkTableID;
    }
}
