package com.whut.mine.entity;

import com.whut.mine.data.CheckTableSecondItem;
import com.whut.mine.greendao.CheckTableSecondIndexDao;
import com.whut.mine.util.ApplicationUtil;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;

import java.util.ArrayList;
import java.util.List;

@Entity(nameInDb = "CheckTableSecondIndex_T")
public class CheckTableSecondIndex {

    @Id
    @NotNull
    @Property(nameInDb = "SecondIndexID")
    private Long secondIndexID;

    @Property(nameInDb = "FirstIndexID")
    private int firstIndexID;

    @Property(nameInDb = "SerialNum")
    private String serialNum;

    @Property(nameInDb = "SecondIndexName")
    private String secondIndexName;

    @Property(nameInDb = "SecondIndexDemo")
    private String secondIndexDemo;

    @Property(nameInDb = "HiddenDangerCategory")
    private String hiddenDangerCategory;

    @Property(nameInDb = "Deleted")
    private String deleted;     //是否删除

    @Property(nameInDb = "AddTime")
    private String addTime;     //添加日期

    @Property(nameInDb = "DeleteTime")
    private String deleteTime;    //删除日期

    @Generated(hash = 2114719331)
    public CheckTableSecondIndex(@NotNull Long secondIndexID, int firstIndexID, String serialNum,
                                 String secondIndexName, String secondIndexDemo, String hiddenDangerCategory, String deleted,
                                 String addTime, String deleteTime) {
        this.secondIndexID = secondIndexID;
        this.firstIndexID = firstIndexID;
        this.serialNum = serialNum;
        this.secondIndexName = secondIndexName;
        this.secondIndexDemo = secondIndexDemo;
        this.hiddenDangerCategory = hiddenDangerCategory;
        this.deleted = deleted;
        this.addTime = addTime;
        this.deleteTime = deleteTime;
    }

    @Generated(hash = 668935897)
    public CheckTableSecondIndex() {
    }

    public static void saveCheckTableSecondIndexInDB(List<CheckTableSecondIndex> indexList) {
        CheckTableSecondIndexDao dao = ApplicationUtil.getInstance().getDBSession().getCheckTableSecondIndexDao();
        dao.deleteAll();
        dao.insertInTx(indexList);
    }

    public static List<CheckTableSecondItem> getSecondItemsByFirstIndexID(Long firstIndexID) {
        CheckTableSecondIndexDao dao = ApplicationUtil.getInstance().getDBSession().getCheckTableSecondIndexDao();
        List<CheckTableSecondIndex> secondIndexList = dao.queryBuilder()
                .where(CheckTableSecondIndexDao.Properties.FirstIndexID.eq(firstIndexID))
                .list();
        List<CheckTableSecondItem> secondItems = new ArrayList<>();
        for (CheckTableSecondIndex index : secondIndexList) {
            CheckTableSecondItem item = new CheckTableSecondItem();
            item.setSecondIndexName(index.getSecondIndexName());
            item.setFirstIndexID(index.getFirstIndexID());
            item.setFirstIndexName(CheckTableFirstIndex.getFirstIndexNameByFirstIndexID(
                    (long) index.getFirstIndexID()));
            item.setSecondIndexID(Integer.parseInt(String.valueOf(index.getSecondIndexID())));
            secondItems.add(item);
        }
        return secondItems;
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

    public String getSecondIndexDemo() {
        return this.secondIndexDemo;
    }

    public void setSecondIndexDemo(String secondIndexDemo) {
        this.secondIndexDemo = secondIndexDemo;
    }

    public String getHiddenDangerCategory() {
        return this.hiddenDangerCategory;
    }

    public void setHiddenDangerCategory(String hiddenDangerCategory) {
        this.hiddenDangerCategory = hiddenDangerCategory;
    }

    public String getDeleted() {
        return this.deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getAddTime() {
        return this.addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getDeleteTime() {
        return this.deleteTime;
    }

    public void setDeleteTime(String deleteTime) {
        this.deleteTime = deleteTime;
    }

    public Long getSecondIndexID() {
        return this.secondIndexID;
    }

    public void setSecondIndexID(Long secondIndexID) {
        this.secondIndexID = secondIndexID;
    }

    public String getSerialNum() {
        return this.serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

}
