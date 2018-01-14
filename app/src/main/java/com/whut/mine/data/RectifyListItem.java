package com.whut.mine.data;

import com.whut.mine.danger.rectify.RectifyActivity;

import java.util.ArrayList;
import java.util.List;

public class RectifyListItem {

    private Boolean selected;
    private String hiddenId;
    private String checktime;
    private String checkCatogoryNum;
    private String instructionNum;
    private String checkmemo;
    private String ConfirmPersonInstitution;
    private String RectifactionForAlarmCompletedTime;
    private String rectifactionEndDate;
    private String rectifyDescription;
    private List<String> photoUrl;
    public RectifyListItem(){
        selected = false;
        hiddenId = " ";
        checktime = " ";
        checkCatogoryNum = " ";
        instructionNum = " ";
        checkmemo = " ";
        ConfirmPersonInstitution = " ";
        rectifactionEndDate = " ";
        rectifyDescription = " ";
        RectifactionForAlarmCompletedTime="";
        photoUrl = new ArrayList<>();
    }


    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getHiddenId() {
        return hiddenId;
    }

    public void setHiddenId(String hiddenId) {
        this.hiddenId = hiddenId;
    }

    public String getChecktime() {
        return checktime;
    }

    public void setChecktime(String checktime) {
        this.checktime = checktime;
    }

    public String getCheckCatogoryNum() {
        return checkCatogoryNum;
    }

    public void setCheckCatogoryNum(String checkCatogoryNum) {
        this.checkCatogoryNum = checkCatogoryNum;
    }

    public String getInstructionNum() {
        return instructionNum;
    }

    public void setInstructionNum(String instructionNum) {
        this.instructionNum = instructionNum;
    }

    public String getCheckmemo() {
        return checkmemo;
    }

    public void setCheckmemo(String checkmemo) {
        this.checkmemo = checkmemo;
    }

    public String getConfirmPersonInstitution() {
        return ConfirmPersonInstitution;
    }

    public void setConfirmPersonInstitution(String confirmPersonInstitution) {
        ConfirmPersonInstitution = confirmPersonInstitution;
    }

    public String getRectifactionEndDate() {
        return rectifactionEndDate;
    }

    public void setRectifactionEndDate(String rectifactionEndDate) {
        this.rectifactionEndDate = rectifactionEndDate;
    }

    public String getRectifyDescription() {
        return rectifyDescription;
    }

    public void setRectifyDescription(String rectifyDescription) {
        this.rectifyDescription = rectifyDescription;
    }

    public List<String> getPhotoUrl() {
        return photoUrl;
    }
    public void addPhoto(String photoUrl){
        this.photoUrl.add(photoUrl);
    }

    public void setPhotoUrl(List<String> photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getRectifactionForAlarmCompletedTime() {
        return RectifactionForAlarmCompletedTime;
    }

    public void setRectifactionForAlarmCompletedTime(String rectifactionForAlarmCompletedTime) {
        RectifactionForAlarmCompletedTime = rectifactionForAlarmCompletedTime;
    }
}
