package com.whut.mine.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import static com.chad.library.adapter.base.listener.SimpleClickListener.TAG;

public class ImageUtils {

    public static String getLabelImageStorePath(Context context, Long checkTableID) {
        int i = (int) (Math.random() * 900) + 100;
        File file = new File(FileUtils.getStorePath(context) + "pic/" + checkTableID + "_S_"
                + TimeUtils.getTodaynyrsfmNoDiv() + i + "0.jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return String.valueOf(Uri.fromFile(file));
    }

    public static String getHiddenImageStorePath(Context context, Long checkTableID, int sign) {
        int i = (int) (Math.random() * 900) + 100;
        File file = new File(FileUtils.getStorePath(context) + "pic/" + checkTableID + "_H_"
                + TimeUtils.getTodaynyrsfmNoDiv() + i + "" + sign + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return String.valueOf(Uri.fromFile(file));
    }

    public static String getRectifyImageStorePath(Context context, Long hiddenID, int sign) {
        int i = (int) (Math.random() * 900) + 100;
        File file = new File(FileUtils.getStorePath(context) + "pic/" + hiddenID + "_R_"
                + TimeUtils.getTodaynyrsfmNoDiv() + i + "" + sign + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return String.valueOf(Uri.fromFile(file));
    }

    public static void saveAsJPEG(Bitmap bitmap, String filePath) throws IOException {
        FileOutputStream fos = null;
        try {
            //去掉文件名的file:前缀，否则会有无法创建图片的问题
            if (filePath.contains("file:")) {
                filePath = filePath.substring(5);
            }
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.flush();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    public static String getPicJson(List<String> picUrls) {
        StringBuilder json = new StringBuilder();
        for (String url : picUrls) {
            json.append(new File(url).getName()).append("?");
        }
        return json.toString();
    }

    public static void uploadImage(String picPath, int checkSign) {
        try {
            if (picPath.contains("file:")) {
                picPath = picPath.substring(5);
            }
            Socket socket = new Socket(DBUtils.getIp(), 8000);
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            // 将图片封装成文件进行传输
            int size = 2 * 1024;
            byte[] buffer = new byte[size];
            File file = new File(picPath);
            // 对图片文件进行传输
            DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(picPath)));
            dos.writeInt(checkSign);//传输一个标识位，告诉服务器照片是检查时拍摄还是整改时拍摄 0为检查，1为整改
            dos.writeUTF(file.getName()); // 传输图片名称
            dos.writeLong(file.length()); // 传输图片长度
            int len;
            while ((len = dis.read(buffer)) != -1) {
                dos.write(buffer, 0, len);
            }
            dis.close();
            dos.flush();
            dos.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            // 检查网络是否正常
            Log.e(TAG, e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
