package com.fly.run.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.fly.run.utils.SDCardUtil.isSDCardEnable;

/**
 * Created by kongwei on 2017/2/24.
 */

public class FileUtil {

    /**
     * 统计指定目录下的文件
     */
    public static List<String> getFilesSDCardFilter(String path) {
        List<String> list = new ArrayList<>();
        if (isSDCardEnable()) {
            File file = new File(path);
            if (file.isDirectory()) {
                String[] files = file.list();
                for (String s : files) {
                    list.add(s);
                }
            }
        }
        return list;
    }

    /**
     * 过滤指定目录下的图片文件路径
     */
    public static List<String> getImgsSDCardFilter(String path) {
        List<String> list = new ArrayList<>();
        if (isSDCardEnable()) {
            File file = new File(path);
            if (file.isDirectory()) {
                String[] files = file.list();
                for (String s : files) {
                    if (checkFileIsImg(s))
                        list.add(s);
                }
            } else {
                if (checkFileIsImg(path))
                    list.add(path);
            }
        }
        return list;
    }

    /**
     * 过滤指定目录下的图片文件
     */
    public static List<File> getImgsFileSDCardFilter(String path) {
        List<File> list = new ArrayList<>();
        if (isSDCardEnable()) {
            File file = new File(path);
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                list = Arrays.asList(files);
            } else {
                if (checkFileIsImg(path))
                    list.add(file);
            }
        }
        return list;
    }

    public static boolean checkFileIsImg(String path) {
        path = path.toLowerCase();
        if (path.endsWith("jpg") || path.endsWith("jpeg") || path.endsWith("png") || path.endsWith("gif"))
            return true;
        return false;
    }

    public static boolean delFile(String path) {
        boolean del = false;
        try {
            File file = new File(path);
            if (file.exists())
                del = file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return del;
    }
}
