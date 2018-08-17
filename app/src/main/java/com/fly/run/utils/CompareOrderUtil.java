package com.fly.run.utils;

import com.fly.run.bean.FileItem;

import java.util.Comparator;

/**
 * Created by maesinfo on 2018/8/1.
 */

public class CompareOrderUtil implements Comparator<FileItem> {
    @Override
    public int compare(FileItem fileItem, FileItem t1) {
        return (int) (t1.getFileDate() - fileItem.getFileDate());
    }
}
