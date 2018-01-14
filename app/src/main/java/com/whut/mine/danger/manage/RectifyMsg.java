package com.whut.mine.danger.manage;

class RectifyMsg {

    private String recInstNum;
    private String recPeopleNum;
    private String inputPeopleName;

    RectifyMsg() {
        this.recInstNum = "";
        this.recPeopleNum = "";
        this.inputPeopleName = "";
    }

    String getRecInstNum() {
        return recInstNum;
    }

    void setRecInstNum(String recInstNum) {
        this.recInstNum = recInstNum;
    }

    String getRecPeopleNum() {
        return recPeopleNum;
    }

    void setRecPeopleNum(String recPeopleNum) {
        this.recPeopleNum = recPeopleNum;
    }

    String getInputPeopleName() {
        return inputPeopleName;
    }

    void setInputPeopleName(String inputPeopleName) {
        this.inputPeopleName = inputPeopleName;
    }

}
