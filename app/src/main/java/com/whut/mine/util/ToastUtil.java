package com.whut.mine.util;

import android.widget.Toast;

public class ToastUtil {

    private static class SingletonHolder {
        private static ToastUtil instance = new ToastUtil();
    }

    private ToastUtil() {
    }

    public static ToastUtil getInstance() {
        return SingletonHolder.instance;
    }

    private Toast mToast = null;

    public void showToast(String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(ApplicationUtil.getInstance().getApplicationContext()
                    , msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

}
