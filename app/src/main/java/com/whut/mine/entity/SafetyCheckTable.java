package com.whut.mine.entity;

import com.whut.mine.greendao.SafetyCheckTableDao;
import com.whut.mine.util.ApplicationUtil;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;

import java.util.List;

@Entity(nameInDb = "SafetyCheckTable_T")
public class SafetyCheckTable {

    @Id
    @NotNull
    @Property(nameInDb = "CheckTableID")
    private Long checkTableID;   //ID

    @Property(nameInDb = "CheckTableNum")
    private String checkTableNum;    //检查表编号

    @Property(nameInDb = "CheckTableName")
    private String checkTableName;    //检查表名

    @Property(nameInDb = "Category")
    private String category;    //类别

    @Property(nameInDb = "InstitutionNum")
    private String institutionNum;      //所属部门编号

    @Property(nameInDb = "Deleted")
    private String deleted;       //是否删除  Y—删除  N—没有删除

    @Property(nameInDb = "AddTime")
    private String addTime;   //添加时间

    @Property(nameInDb = "DeleteTime")
    private String deleteTime;    //删除时间

    @Property(nameInDb = "VisibleToWho")
    private String visibleToWho;      //是否对安环、高管可见
    //0：两个均不可见；1-仅安环部可见 2、仅高管可见  3、两者均可见

    @Generated(hash = 450324580)
    public SafetyCheckTable(@NotNull Long checkTableID, String checkTableNum, String checkTableName,
                            String category, String institutionNum, String deleted, String addTime, String deleteTime,
                            String visibleToWho) {
        this.checkTableID = checkTableID;
        this.checkTableNum = checkTableNum;
        this.checkTableName = checkTableName;
        this.category = category;
        this.institutionNum = institutionNum;
        this.deleted = deleted;
        this.addTime = addTime;
        this.deleteTime = deleteTime;
        this.visibleToWho = visibleToWho;
    }

    @Generated(hash = 1126334929)
    public SafetyCheckTable() {
    }

    public static void saveSafetyCheckTableInDB(List<SafetyCheckTable> tables) {
        SafetyCheckTableDao dao = ApplicationUtil.getInstance().getDBSession().getSafetyCheckTableDao();
        //清空数据库后再添加数据
        dao.deleteAll();
        dao.insertInTx(tables);
    }

    public static String getCheckTableNameByID(int checkTableID) {
        SafetyCheckTableDao dao = ApplicationUtil.getInstance().getDBSession().getSafetyCheckTableDao();
        List<SafetyCheckTable> checkTable = dao.queryBuilder()
                .where(SafetyCheckTableDao.Properties.CheckTableID.eq(checkTableID))
                .limit(1)
                .list();
        return checkTable.get(0).getCheckTableName();
    }

    public static List<SafetyCheckTable> getSafetyCheckTableInDB() {
        SafetyCheckTableDao dao = ApplicationUtil.getInstance().getDBSession().getSafetyCheckTableDao();
        return dao.loadAll();
    }

    public static Boolean isSafetyCheckTableEmpty() {
        SafetyCheckTableDao dao = ApplicationUtil.getInstance().getDBSession().getSafetyCheckTableDao();
        return dao.count() == 0;
    }

    public String getCheckTableNum() {
        return this.checkTableNum;
    }

    public void setCheckTableNum(String checkTableNum) {
        this.checkTableNum = checkTableNum;
    }

    public String getCheckTableName() {
        return this.checkTableName;
    }

    public void setCheckTableName(String checkTableName) {
        this.checkTableName = checkTableName;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInstitutionNum() {
        return this.institutionNum;
    }

    public void setInstitutionNum(String institutionNum) {
        this.institutionNum = institutionNum;
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

    public String getVisibleToWho() {
        return this.visibleToWho;
    }

    public void setVisibleToWho(String visibleToWho) {
        this.visibleToWho = visibleToWho;
    }


    public Long getCheckTableID() {
        return this.checkTableID;
    }


    public void setCheckTableID(Long checkTableID) {
        this.checkTableID = checkTableID;
    }

}
