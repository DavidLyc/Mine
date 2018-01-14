package com.whut.mine.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.List;

public class FileUtils {

    public static String getStorePath(Context context) {
        return Environment.getExternalStorageDirectory() + "/" + context.getPackageName() + "/";
    }

    public static File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static void deleteFile(final String srcFilePath) {
        deleteFile(getFileByPath(srcFilePath));
    }

    public static void deleteImage(final List<String> imagePath) {
        for (String path : imagePath) {
            if (path.contains("file:")) {
                path = path.substring(5);
            }
            deleteFile(path);
        }
    }

    private static boolean deleteFile(final File file) {
        return file != null && (!file.exists() || file.isFile() && file.delete());
    }

    public static void deleteAllImages(Context context) {
        File files[] = getFileByPath(getStorePath(context) + "pic/").listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.exists()) {
                    try {
                        f.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
