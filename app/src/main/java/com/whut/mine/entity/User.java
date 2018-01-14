package com.whut.mine.entity;

import android.content.SharedPreferences;

import com.umeng.message.UTrack;
import com.whut.mine.greendao.UserDao;
import com.whut.mine.util.ApplicationUtil;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

@Entity(nameInDb = "userinfo_t")
public class User {

    @Id
    @NotNull
    @Property(nameInDb = "EmployeeNum")
    private String employeeNum;     //员工编号 PK

    @Property(nameInDb = "EmployeeName")
    private String employeeName;        //员工姓名

    @Property(nameInDb = "InstitutionNum")
    private String institutionNum;      //机构编号

    @Property(nameInDb = "Password")
    private String password;        //登录密码

    @Property(nameInDb = "UserRight")
    private String userRight;       //用户权限

    @Generated(hash = 12704005)
    public User(@NotNull String employeeNum, String employeeName,
                String institutionNum, String password, String userRight) {
        this.employeeNum = employeeNum;
        this.employeeName = employeeName;
        this.institutionNum = institutionNum;
        this.password = password;
        this.userRight = userRight;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public static void saveUserInDB(List<User> users) {
        UserDao dao = ApplicationUtil.getInstance().getDBSession().getUserDao();
        dao.deleteAll();
        dao.insertInTx(users);
    }

    public static boolean isLoginValid(String employeeNum, String password) {
        UserDao dao = ApplicationUtil.getInstance().getDBSession().getUserDao();
        final List<User> userList = dao.queryBuilder()
                .limit(1)
                .where(dao.queryBuilder().and(UserDao.Properties.EmployeeNum.eq(employeeNum)
                        , UserDao.Properties.Password.eq(password)))
                .build()
                .list();
        if (!userList.isEmpty()) {
            //sp存入员工号,姓名,机构名,app登陆状态
            SharedPreferences pref = ApplicationUtil.getInstance().getApplicationContext()
                    .getSharedPreferences("mine", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("user_num", userList.get(0).getEmployeeNum());
            editor.putString("user_name", userList.get(0).getEmployeeName());
            editor.putString("user_inst", userList.get(0).getInstitutionNum());
            editor.putString("user_right", userList.get(0).getUserRight());
            editor.putBoolean("login_status", true);
            editor.apply();
            //向umeng发送alias
            ApplicationUtil.getInstance()
                    .getPushAgent()
                    .addAlias(userList.get(0).getEmployeeNum()
                            , userList.get(0).getInstitutionNum()
                            , new UTrack.ICallBack() {
                                @Override
                                public void onMessage(boolean isSuccess, String message) {
//                                    Log.d("User", "isSuccess:" + isSuccess);
//                                    Log.d("User", message);
                                }
                            });
            return true;
        }
        return false;
    }

    public static void updateUserRight() {
        SharedPreferences pref = ApplicationUtil.getInstance().getApplicationContext()
                .getSharedPreferences("mine", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String userName = pref.getString("user_name", "user_name");
        UserDao dao = ApplicationUtil.getInstance().getDBSession().getUserDao();
        List<User> userList = dao.queryBuilder()
                .where(UserDao.Properties.EmployeeName.eq(userName))
                .limit(1)
                .build()
                .list();
        if (!userList.isEmpty()) {
            editor.putString("user_right", userList.get(0).getUserRight());
            editor.apply();
        }
    }

    public static List<String> getUserInfoBySearchName(String employeeName, String instNum) {
        UserDao dao = ApplicationUtil.getInstance().getDBSession().getUserDao();
        List<User> userList = dao.queryBuilder()
                .where(dao.queryBuilder().and(UserDao.Properties.EmployeeName.like("%" + employeeName + "%")
                        , UserDao.Properties.InstitutionNum.eq(instNum)))
                .list();
        if (userList != null) {
            List<String> userInfoList = new ArrayList<>();
            for (User user : userList) {
                userInfoList.add(user.getEmployeeName() + "  " + user.getEmployeeNum());
            }
            return userInfoList;
        } else {
            return null;
        }
    }

    public static String getUserNumByName(String name) {
        UserDao dao = ApplicationUtil.getInstance().getDBSession().getUserDao();
        List<User> userList = dao.queryBuilder()
                .where(UserDao.Properties.EmployeeName.eq(name))
                .limit(1)
                .build()
                .list();
        if (!userList.isEmpty()) {
            return userList.get(0).getEmployeeNum();
        } else {
            return null;
        }
    }

    public String getEmployeeNum() {
        return this.employeeNum;
    }

    public void setEmployeeNum(String employeeNum) {
        this.employeeNum = employeeNum;
    }

    public String getEmployeeName() {
        return this.employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getInstitutionNum() {
        return this.institutionNum;
    }

    public void setInstitutionNum(String institutionNum) {
        this.institutionNum = institutionNum;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserRight() {
        return this.userRight;
    }

    public void setUserRight(String userRight) {
        this.userRight = userRight;
    }

}
