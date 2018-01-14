package com.whut.mine.entity;

import com.whut.mine.data.CheckManageItem;
import com.whut.mine.greendao.SafetyCheckTableInfoDao;
import com.whut.mine.util.ApplicationUtil;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;

import java.util.ArrayList;
import java.util.List;

@Entity(nameInDb = "SafetyCheckTableInfo_T")
public class SafetyCheckTableInfo {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    @Property(nameInDb = "CheckTableID")
    private int checkTableID;

    @NotNull
    @Property(nameInDb = "CheckTime")
    private String checkTime;

    @Property(nameInDb = "DataTime")
    private String dataTime;

    @Property(nameInDb = "PersonInChargeNum")
    private String personInChargeNum;

    @Property(nameInDb = "PersonInChargeName")
    private String personInChargeName;

    @Property(nameInDb = "PeopleForCheck")
    private String peopleForCheck;

    @Property(nameInDb = "Suggestion")
    private String suggestion;

    @Property(nameInDb = "ValidatePic")
    private String validatePic;

    @Property(nameInDb = "CheckCatogoryNum")
    private int checkCatogoryNum;

    @Property(nameInDb = "InstitutionChecked")
    private String institutionChecked;   //被检单位

    @Generated(hash = 459320406)
    public SafetyCheckTableInfo(Long id, int checkTableID, @NotNull String checkTime, String dataTime,
                                String personInChargeNum, String personInChargeName, String peopleForCheck, String suggestion,
                                String validatePic, int checkCatogoryNum, String institutionChecked) {
        this.id = id;
        this.checkTableID = checkTableID;
        this.checkTime = checkTime;
        this.dataTime = dataTime;
        this.personInChargeNum = personInChargeNum;
        this.personInChargeName = personInChargeName;
        this.peopleForCheck = peopleForCheck;
        this.suggestion = suggestion;
        this.validatePic = validatePic;
        this.checkCatogoryNum = checkCatogoryNum;
        this.institutionChecked = institutionChecked;
    }

    @Generated(hash = 678066496)
    public SafetyCheckTableInfo() {
    }

    public static void deleteDataByTime(String checkTime) {
        SafetyCheckTableInfoDao dao = ApplicationUtil.getInstance().getDBSession().getSafetyCheckTableInfoDao();
        dao.deleteInTx(dao.queryBuilder()
                .where(SafetyCheckTableInfoDao.Properties.CheckTime.eq(checkTime))
                .list());
    }

    public static void saveCheckTableInfoInDatabase(SafetyCheckTableInfo info) {
        SafetyCheckTableInfoDao infoDao = ApplicationUtil.getInstance().getDBSession().getSafetyCheckTableInfoDao();
        deleteDataByTime(info.checkTime);
        infoDao.insert(info);
    }

    private static List<SafetyCheckTableInfo> getAllCheckTableInfo() {
        SafetyCheckTableInfoDao infoDao = ApplicationUtil.getInstance().getDBSession().getSafetyCheckTableInfoDao();
        return infoDao.queryBuilder().list();
    }

    public static SafetyCheckTableInfo getCheckInfoByTime(String checkTime) {
        SafetyCheckTableInfoDao dao = ApplicationUtil.getInstance().getDBSession().getSafetyCheckTableInfoDao();
        return dao.queryBuilder()
                .where(SafetyCheckTableInfoDao.Properties.CheckTime.eq(checkTime))
                .limit(1)
                .list()
                .get(0);
    }

    public static List<CheckManageItem> getAllCheckManageItem() {
        List<SafetyCheckTableInfo> infoList = getAllCheckTableInfo();
        List<CheckManageItem> items = new ArrayList<>();
        for (SafetyCheckTableInfo info : infoList) {
            items.add(new CheckManageItem((long) info.getCheckTableID()
                    , SafetyCheckTable.getCheckTableNameByID(info.getCheckTableID())
                    , "检查时间：" + info.getCheckTime()));
        }
        return items;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCheckTableID() {
        return this.checkTableID;
    }

    public void setCheckTableID(int checkTableID) {
        this.checkTableID = checkTableID;
    }

    public String getCheckTime() {
        return this.checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getDataTime() {
        return this.dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public String getPersonInChargeNum() {
        return this.personInChargeNum;
    }

    public void setPersonInChargeNum(String personInChargeNum) {
        this.personInChargeNum = personInChargeNum;
    }

    public String getPersonInChargeName() {
        return this.personInChargeName;
    }

    public void setPersonInChargeName(String personInChargeName) {
        this.personInChargeName = personInChargeName;
    }

    public String getPeopleForCheck() {
        return this.peopleForCheck;
    }

    public void setPeopleForCheck(String peopleForCheck) {
        this.peopleForCheck = peopleForCheck;
    }

    public String getSuggestion() {
        return this.suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getValidatePic() {
        return this.validatePic;
    }

    public void setValidatePic(String validatePic) {
        this.validatePic = validatePic;
    }

    public int getCheckCatogoryNum() {
        return this.checkCatogoryNum;
    }

    public void setCheckCatogoryNum(int checkCatogoryNum) {
        this.checkCatogoryNum = checkCatogoryNum;
    }

    public String getInstitutionChecked() {
        return this.institutionChecked;
    }

    public void setInstitutionChecked(String institutionChecked) {
        this.institutionChecked = institutionChecked;
    }

}
