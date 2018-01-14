package com.whut.mine.util;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.JsonArray;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class DBUtils {

    private static final String ip = "xxx.xxx.xx.x";
    private int port;
    private Socket socket;

    public DBUtils(int port) {
        this.port = port;
    }

    public String connStr(String sql) {
        Context context = ApplicationUtil.getInstance().getApplicationContext();
        String result = "";
        try {
            socket = new Socket(ip, port);
            socket.setSoTimeout(10000);
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out.writeUTF(sql);
            out.flush();
            byte[] buffer = new byte[2 * 1024];
            int len;
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            while ((len = in.read(buffer)) != -1) {
                bout.write(buffer, 0, len);
            }
            result = bout.toString();
            bout.flush();
            bout.close();
            out.close();
            in.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            // 检查网络是否正常
            Toast.makeText(context, "无法连接服务器!", Toast.LENGTH_SHORT).show();
            return null;
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public String uploadJson(JsonArray json) {
        String result = "";
        try {
            socket = new Socket(ip, port);
            socket.setSoTimeout(10000);
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            String content = String.valueOf(json);
            out.writeUTF(content);
            out.flush();
            result = in.readUTF();
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getIp() {
        return ip;
    }

}
