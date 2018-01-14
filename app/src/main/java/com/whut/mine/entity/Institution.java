package com.whut.mine.entity;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.whut.mine.greendao.InstitutionDao;
import com.whut.mine.util.ApplicationUtil;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

@Entity(nameInDb = "institution_t")
public class Institution {

    @Id
    @NotNull
    @Property(nameInDb = "InstitutionNum")
    private String institutionNum;  //机构编号

    @Property(nameInDb = "InstitutionName")
    private String institutionName;  //机构名称

    @Property(nameInDb = "ICategoryNum")
    private int iCategoryNum;   //部门类别编号  1-局级 2-矿级 3-科室 4-车间 5-班组

    @Property(nameInDb = "PeopleInCharge")
    private String peopleInCharge;   //部门负责人

    @Property(nameInDb = "Category")
    private String category;    //代表方

    @Property(nameInDb = "InstitutionPrefix")
    private String institutionPrefix;    //部门前缀

    @Generated(hash = 758278381)
    public Institution(@NotNull String institutionNum, String institutionName,
                       int iCategoryNum, String peopleInCharge, String category,
                       String institutionPrefix) {
        this.institutionNum = institutionNum;
        this.institutionName = institutionName;
        this.iCategoryNum = iCategoryNum;
        this.peopleInCharge = peopleInCharge;
        this.category = category;
        this.institutionPrefix = institutionPrefix;
    }

    @Generated(hash = 1565596376)
    public Institution() {
    }

    public static void saveInstitutionInDB(List<Institution> institutions) {
        InstitutionDao dao = ApplicationUtil.getInstance().getDBSession().getInstitutionDao();
        dao.deleteAll();
        dao.insertInTx(institutions);
    }

    public static String getInstitutionNameByNum(String institutionNum) {
        if (institutionNum.trim().isEmpty()) {
            return institutionNum;
        }
        InstitutionDao dao = ApplicationUtil.getInstance().getDBSession().getInstitutionDao();
        List<Institution> institutions = dao.queryBuilder()
                .where(InstitutionDao.Properties.InstitutionNum.eq(institutionNum))
                .limit(1)
                .list();
        if (institutions.isEmpty()) {
            return institutionNum;
        } else {
            return institutions.get(0).getInstitutionName();
        }
    }

    public static String getInstitutionNumByName(@NonNull String instName) {
        InstitutionDao dao = ApplicationUtil.getInstance().getDBSession().getInstitutionDao();
        List<Institution> institutions = dao.queryBuilder()
                .where(InstitutionDao.Properties.InstitutionName.eq(instName))
                .limit(1)
                .list();
        if (institutions.isEmpty()) {
            return null;
        } else {
            return institutions.get(0).getInstitutionNum();
        }
    }

    public static String getUserInstitution() {
        SharedPreferences pref = ApplicationUtil.getInstance().getApplicationContext()
                .getSharedPreferences("mine", MODE_PRIVATE);
        String userInst = pref.getString("user_inst", "user_inst");
        return getInstitutionNameByNum(userInst);
    }

    public static List<String> getAllInstitution() {
        InstitutionDao dao = ApplicationUtil.getInstance().getDBSession().getInstitutionDao();
        List<Institution> institutionList = dao.queryBuilder()
                .build()
                .list();
        List<String> instName = new ArrayList<>();
        for (Institution inst : institutionList) {
            instName.add(inst.getInstitutionName());
        }
        return instName;
    }

    public String getInstitutionNum() {
        return this.institutionNum;
    }

    public void setInstitutionNum(String institutionNum) {
        this.institutionNum = institutionNum;
    }

    public String getInstitutionName() {
        return this.institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public int getICategoryNum() {
        return this.iCategoryNum;
    }

    public void setICategoryNum(int iCategoryNum) {
        this.iCategoryNum = iCategoryNum;
    }

    public String getPeopleInCharge() {
        return this.peopleInCharge;
    }

    public void setPeopleInCharge(String peopleInCharge) {
        this.peopleInCharge = peopleInCharge;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInstitutionPrefix() {
        return this.institutionPrefix;
    }

    public void setInstitutionPrefix(String institutionPrefix) {
        this.institutionPrefix = institutionPrefix;
    }

}
