package com.whut.mine.data;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.whut.mine.adapter.CheckTableAdapter;

import java.util.ArrayList;
import java.util.List;

public class CheckTableSecondItem extends AbstractExpandableItem<CheckTableThirdItem> implements MultiItemEntity {

    private int firstIndexID;
    private int checkStatus;     //1--合格   0--不合格
    private ArrayList<String> photoUrl;
    private String hidDangerInfo;    //隐患信息
    private String hidDangerType;    //隐患类型
    private int secondIndexID;
    private String firstIndexName;
    private String secondIndexName;
    private String checkTime;

    public CheckTableSecondItem() {
        this.checkStatus = 1;
        this.photoUrl = new ArrayList<>();
        this.hidDangerInfo = "";
        this.hidDangerType = "设备设施类";
    }

    @Override
    public int getItemType() {
        return CheckTableAdapter.TYPE_SECONDINDEX;
    }

    @Override
    public int getLevel() {
        return 1;
    }

    public int getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(int checkStatus) {
        this.checkStatus = checkStatus;
    }


    public String getHidDangerType() {
        return hidDangerType;
    }

    public void setHidDangerType(String hidDangerType) {
        this.hidDangerType = hidDangerType;
    }

    public ArrayList<String> getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(ArrayList<String> photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void addPhotoUrl(String photoUrl) {
        this.photoUrl.add(photoUrl);
    }

    public String getHidDangerInfo() {
        return hidDangerInfo;
    }

    public void setHidDangerInfo(String hidDangerInfo) {
        this.hidDangerInfo = hidDangerInfo;
    }

    public int getSecondIndexID() {
        return secondIndexID;
    }

    public void setSecondIndexID(int secondIndexID) {
        this.secondIndexID = secondIndexID;
    }

    public int getFirstIndexID() {
        return firstIndexID;
    }

    public void setFirstIndexID(int firstIndexID) {
        this.firstIndexID = firstIndexID;
    }

    public String getFirstIndexName() {
        return firstIndexName;
    }

    public void setFirstIndexName(String firstIndexName) {
        this.firstIndexName = firstIndexName;
    }

    public String getSecondIndexName() {
        return secondIndexName;
    }

    public void setSecondIndexName(String secondIndexName) {
        this.secondIndexName = secondIndexName;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

}
