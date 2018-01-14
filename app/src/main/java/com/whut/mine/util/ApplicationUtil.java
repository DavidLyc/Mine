package com.whut.mine.util;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.whut.mine.greendao.DaoMaster;
import com.whut.mine.greendao.DaoSession;

import java.lang.reflect.Field;

public class ApplicationUtil extends Application {

    private static ApplicationUtil instance;
    private static DaoSession dbSession;
    private PushAgent mPushAgent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(false);
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String s) {
//                Log.d("ApplicationUtil", s);
            }

            @Override
            public void onFailure(String s, String s1) {
            }
        });
        mPushAgent.setDisplayNotificationNumber(6);
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SERVER); //声音
        mPushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SERVER);//呼吸灯
        mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SERVER);//振动
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static synchronized ApplicationUtil getInstance() {
        return instance;
    }

    public PushAgent getPushAgent() {
        return mPushAgent;
    }

    public DaoSession getDBSession() {
        if (dbSession == null) {
            DaoMaster.OpenHelper dbHelper = new DaoMaster.DevOpenHelper(this, "mine.db");
            DaoMaster daoMaster = new DaoMaster(dbHelper.getWritableDatabase());
            dbSession = daoMaster.newSession();
        }
        return dbSession;
    }

    public void dropAllTables() {
        DaoMaster.OpenHelper dbHelper = new DaoMaster.DevOpenHelper(this, "mine.db");
        DaoMaster daoMaster = new DaoMaster(dbHelper.getWritableDatabase());
        DaoMaster.dropAllTables(daoMaster.getDatabase(), true);
        DaoMaster.createAllTables(daoMaster.getDatabase(), true);
    }

    public void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }
        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field f;
        Object obj_get;
        for (String param : arr) {
            try {
                f = imm.getClass().getDeclaredField(param);
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                obj_get = f.get(imm);
                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    if (v_get.getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null); // 置空，破坏掉path to gc节点
                    } else {
                        break;
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

}
