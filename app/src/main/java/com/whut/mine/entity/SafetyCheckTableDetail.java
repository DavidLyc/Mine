package com.whut.mine.entity;

import com.whut.mine.data.CheckTableSecondItem;
import com.whut.mine.greendao.SafetyCheckTableDetailDao;
import com.whut.mine.util.ApplicationUtil;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity(nameInDb = "SafetyCheckTableDetail_T")
public class SafetyCheckTableDetail {

    @Id(autoincrement = true)
    @Property(nameInDb = "SafetyCheckTableDetailID")
    private Long safetyCheckTableDetailID;     //ID

    @NotNull
    @Property(nameInDb = "CheckTableID")
    private int checkTableID;       //检查表ID

    @NotNull
    @Property(nameInDb = "CheckTime")
    private String checkTime;

    @NotNull
    @Property(nameInDb = "FirstIndexID")
    private int firstIndexID;

    @Property(nameInDb = "SecondIndexID")
    private int secondIndexID;      //二级指标ID

    @Property(nameInDb = "SecondIndexName")
    private String secondIndexName;

    @Property(nameInDb = "Result")
    private String result;      //是否合格

    @Property(nameInDb = "CheckMemo")
    private String checkMemo;       //不合格说明

    @Property(nameInDb = "HiddenDangerCategory")
    private String hiddenDangerCategory;

    @Property(nameInDb = "Picture")
    private String picture;     //隐患图片存放地址

    @Generated(hash = 32097514)
    public SafetyCheckTableDetail(Long safetyCheckTableDetailID, int checkTableID, @NotNull String checkTime
            , int firstIndexID, int secondIndexID, String secondIndexName, String result, String checkMemo
            , String hiddenDangerCategory, String picture) {
        this.safetyCheckTableDetailID = safetyCheckTableDetailID;
        this.checkTableID = checkTableID;
        this.checkTime = checkTime;
        this.firstIndexID = firstIndexID;
        this.secondIndexID = secondIndexID;
        this.secondIndexName = secondIndexName;
        this.result = result;
        this.checkMemo = checkMemo;
        this.hiddenDangerCategory = hiddenDangerCategory;
        this.picture = picture;
    }

    @Generated(hash = 250699704)
    public SafetyCheckTableDetail() {
    }

    public static SafetyCheckTableDetail getCheckTableDetailBySecondItem(CheckTableSecondItem item, int checkTableID) {
        SafetyCheckTableDetail detail = new SafetyCheckTableDetail();
        detail.setCheckTableID(checkTableID);
        detail.setCheckTime(item.getCheckTime());
        detail.setFirstIndexID(item.getFirstIndexID());
        detail.setSecondIndexID(item.getSecondIndexID());
        detail.setSecondIndexName(item.getSecondIndexName());
        detail.setResult(String.valueOf(item.getCheckStatus()));
        detail.setCheckMemo(item.getHidDangerInfo());
        detail.setHiddenDangerCategory(item.getHidDangerType());
        detail.setPicture(String.valueOf(item.getPhotoUrl()));
        return detail;
    }

    public static void saveCheckTableDetailsInDB(List<SafetyCheckTableDetail> details) {
        SafetyCheckTableDetailDao dao = ApplicationUtil.getInstance().getDBSession().getSafetyCheckTableDetailDao();
        deleteDetailsByTime(details.get(0).getCheckTime());
        dao.insertInTx(details);
    }

    public Long getSafetyCheckTableDetailID() {
        return this.safetyCheckTableDetailID;
    }

    public void setSafetyCheckTableDetailID(Long safetyCheckTableDetailID) {
        this.safetyCheckTableDetailID = safetyCheckTableDetailID;
    }

    public static List<CheckTableSecondItem> getSecondItemsByCheckTime(String checkTime) {
        SafetyCheckTableDetailDao dao = ApplicationUtil.getInstance().getDBSession().getSafetyCheckTableDetailDao();
        List<SafetyCheckTableDetail> details = dao.queryBuilder()
                .where(SafetyCheckTableDetailDao.Properties.CheckTime.eq(checkTime))
                .list();
        List<CheckTableSecondItem> secondItems = new ArrayList<>();
        for (SafetyCheckTableDetail detail : details) {
            CheckTableSecondItem item = new CheckTableSecondItem();
            item.setFirstIndexID(detail.getFirstIndexID());
            item.setCheckStatus(Integer.parseInt(detail.getResult()));
            item.setPhotoUrl(new ArrayList<>(Arrays.asList(detail.getPicture().substring(
                    1, detail.getPicture().length() - 1).split(", "))));
            item.setHidDangerInfo(detail.getCheckMemo());
            item.setHidDangerType(detail.getHiddenDangerCategory());
            item.setSecondIndexID(detail.getSecondIndexID());
            item.setFirstIndexName(CheckTableFirstIndex.getFirstIndexNameByFirstIndexID((long) detail.getFirstIndexID()));
            item.setSecondIndexName(detail.getSecondIndexName());
            secondItems.add(item);
        }
        return secondItems;
    }

    public static void deleteDetailsByTime(String checkTime) {
        SafetyCheckTableDetailDao dao = ApplicationUtil.getInstance().getDBSession().getSafetyCheckTableDetailDao();
        dao.deleteInTx(dao.queryBuilder()
                .where(SafetyCheckTableDetailDao.Properties.CheckTime.eq(checkTime))
                .list());
    }

    public int getCheckTableID() {
        return this.checkTableID;
    }

    public void setCheckTableID(int checkTableID) {
        this.checkTableID = checkTableID;
    }

    public int getSecondIndexID() {
        return this.secondIndexID;
    }

    public void setSecondIndexID(int secondIndexID) {
        this.secondIndexID = secondIndexID;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCheckMemo() {
        return this.checkMemo;
    }

    public void setCheckMemo(String checkMemo) {
        this.checkMemo = checkMemo;
    }

    public String getPicture() {
        return this.picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getFirstIndexID() {
        return this.firstIndexID;
    }

    public void setFirstIndexID(int firstIndexID) {
        this.firstIndexID = firstIndexID;
    }

    public String getSecondIndexName() {
        return this.secondIndexName;
    }

    public void setSecondIndexName(String secondIndexName) {
        this.secondIndexName = secondIndexName;
    }

    public String getCheckTime() {
        return this.checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getHiddenDangerCategory() {
        return this.hiddenDangerCategory;
    }

    public void setHiddenDangerCategory(String hiddenDangerCategory) {
        this.hiddenDangerCategory = hiddenDangerCategory;
    }

}
