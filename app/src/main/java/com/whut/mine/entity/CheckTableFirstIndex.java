package com.whut.mine.entity;

import com.whut.mine.data.CheckTableFirstItem;
import com.whut.mine.greendao.CheckTableFirstIndexDao;
import com.whut.mine.util.ApplicationUtil;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

import java.util.ArrayList;
import java.util.List;

@Entity(nameInDb = "CheckTableFirstIndex_T")
public class CheckTableFirstIndex {

    @Id
    @NotNull
    @Property(nameInDb = "FirstIndexID")
    private Long firstIndexID;   //ID

    @Property(nameInDb = "CheckTableID")
    private int checkTableID;   //所属检查表ID

    @Property(nameInDb = "SerialNum")
    private int serialNum;     //显示的编号顺序

    @Property(nameInDb = "FirstIndexName")
    private String firstIndexName;      //项目

    @Property(nameInDb = "Deleted")
    private String deleted;     //是否删除

    @Property(nameInDb = "AddTime")
    private String addTime;     //添加日期

    @Property(nameInDb = "DeleteTime")
    private String deleteTime;    //删除日期

    @Generated(hash = 639197657)
    public CheckTableFirstIndex(@NotNull Long firstIndexID, int checkTableID, int serialNum,
                                String firstIndexName, String deleted, String addTime, String deleteTime) {
        this.firstIndexID = firstIndexID;
        this.checkTableID = checkTableID;
        this.serialNum = serialNum;
        this.firstIndexName = firstIndexName;
        this.deleted = deleted;
        this.addTime = addTime;
        this.deleteTime = deleteTime;
    }

    @Generated(hash = 686907746)
    public CheckTableFirstIndex() {
    }

    public static void saveCheckTableFirstIndexInDB(List<CheckTableFirstIndex> indexList) {
        CheckTableFirstIndexDao dao = ApplicationUtil.getInstance().getDBSession().getCheckTableFirstIndexDao();
        dao.deleteAll();
        dao.insertInTx(indexList);
    }

    public static List<CheckTableFirstItem> getCheckTableFirstItemsByID(Long checkTableID) {
        CheckTableFirstIndexDao dao = ApplicationUtil.getInstance().getDBSession().getCheckTableFirstIndexDao();
        List<CheckTableFirstIndex> firstIndexList = dao.queryBuilder()
                .where(CheckTableFirstIndexDao.Properties.CheckTableID.eq(checkTableID))
                .orderAsc(CheckTableFirstIndexDao.Properties.SerialNum)
                .list();
        List<CheckTableFirstItem> firstItems = new ArrayList<>();
        for (CheckTableFirstIndex index : firstIndexList) {
            firstItems.add(new CheckTableFirstItem(index.getFirstIndexName(), index.getFirstIndexID()));
        }
        return firstItems;
    }

    public static String getFirstIndexNameByFirstIndexID(Long firstIndexID) {
        CheckTableFirstIndexDao dao = ApplicationUtil.getInstance().getDBSession().getCheckTableFirstIndexDao();
        return dao.queryBuilder()
                .where(CheckTableFirstIndexDao.Properties.FirstIndexID.eq(firstIndexID))
                .limit(1)
                .list()
                .get(0)
                .getFirstIndexName();
    }

    public int getCheckTableID() {
        return this.checkTableID;
    }

    public void setCheckTableID(int checkTableID) {
        this.checkTableID = checkTableID;
    }

    public int getSerialNum() {
        return this.serialNum;
    }

    public void setSerialNum(int serialNum) {
        this.serialNum = serialNum;
    }

    public String getFirstIndexName() {
        return this.firstIndexName;
    }

    public void setFirstIndexName(String firstIndexName) {
        this.firstIndexName = firstIndexName;
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

    public Long getFirstIndexID() {
        return this.firstIndexID;
    }

    public void setFirstIndexID(Long firstIndexID) {
        this.firstIndexID = firstIndexID;
    }

}
