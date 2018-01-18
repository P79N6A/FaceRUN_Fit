package com.fly.run.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.fly.run.bean.FileItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinzhendi-031 on 2017/12/13.
 */
public class MediaQueryUtil {

    /**
     * 查询图片
     */
    public static List<FileItem> getAllPhoto(Context context) {
        List<FileItem> photos = new ArrayList<>();
        String[] projection = new String[]{MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.DISPLAY_NAME};
        //asc 按升序排列
        //desc 按降序排列
        //projection 是定义返回的数据，selection 通常的sql 语句，例如  selection=MediaStore.Images.ImageColumns.MIME_TYPE+"=? " 那么 selectionArgs=new String[]{"jpg"};
//        String selection = MediaStore.Images.ImageColumns.MIME_TYPE + "=? ";
//        String[] selectionArgs=new String[]{"jpg"};
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, MediaStore.Images.ImageColumns.DATE_MODIFIED + "  desc");
        String imageId = null;
        String fileName;
        String filePath;
        while (cursor.moveToNext()) {
            imageId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID));
            fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME));
            filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
            FileItem fileItem = new FileItem(imageId, filePath, fileName);
            photos.add(fileItem);
        }
        cursor.close();
        cursor = null;
        return photos;
    }

    /**
     * 查询文本文件
     */
    public static List<FileItem> getAllText(Context context) {
        List<FileItem> texts = new ArrayList<>();
        String[] projection = new String[]{MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.TITLE, MediaStore.Files.FileColumns.MIME_TYPE};
        //相当于我们常用sql where 后面的写法
        String selection = MediaStore.Files.FileColumns.MIME_TYPE + "= ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? "
                + " or " + MediaStore.Files.FileColumns.MIME_TYPE + " = ? ";
        String[] selectionArgs = new String[]{"text/plain", "application/msword", "application/pdf", "application/vnd.ms-powerpoint", "application/vnd.ms-excel"};
        Cursor cursor = context.getContentResolver().query(MediaStore.Files.getContentUri("external"), projection, selection, selectionArgs, MediaStore.Files.FileColumns.DATE_MODIFIED + " desc");
        String fileId;
        String fileName;
        String filePath;
        while (cursor.moveToNext()) {
            fileId = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
            fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE));
            filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
            Log.e("AllText", fileId + " -- " + fileName + " -- " + "--" + cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE)) + filePath);
            FileItem fileItem = new FileItem(fileId, filePath, fileName);
            texts.add(fileItem);
        }
        cursor.close();
        cursor = null;
        return texts;
    }

    /**
     * 查询视频文件
     */
    public static List<FileItem> getAllVideo(Context context) {
        List<FileItem> texts = new ArrayList<>();
        String[] projection = new String[]{MediaStore.Files.FileColumns._ID, MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.TITLE, MediaStore.Files.FileColumns.MIME_TYPE};
        //相当于我们常用sql where 后面的写法
        String selection = MediaStore.Files.FileColumns.MIME_TYPE + "= ? ";
        String[] selectionArgs = new String[]{"video/mp4"};
        Cursor cursor = context.getContentResolver().query(MediaStore.Files.getContentUri("external"), projection, selection, selectionArgs, MediaStore.Files.FileColumns.DATE_MODIFIED + " desc");
        String fileId;
        String fileName;
        String filePath;
        while (cursor.moveToNext()) {
            fileId = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
            fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE));
            filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
            Log.e("video", fileId + " -- " + fileName + " -- " + cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE)) + "--" + filePath);
            FileItem fileItem = new FileItem(fileId, filePath, fileName);
            texts.add(fileItem);
        }
        cursor.close();
        cursor = null;
        return texts;
    }

    public static List<FileItem> getAllVideoImages(Context context) {
        List<FileItem> files = new ArrayList<>();
        Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        // 只查询JPEG和PNG扩展名的图片，并按照最近修改时间排序
        StringBuffer selection = new StringBuffer();
        selection.append(MediaStore.Images.Media.MIME_TYPE).append("= ? or ").append(MediaStore.Images.Media.MIME_TYPE);
        selection.append(" = ? or ").append(MediaStore.Files.FileColumns.MIME_TYPE).append("= ?");
        String[] args = new String[]{"image/jpeg", "image/png", "video/mp4"};
        Cursor cursor = context.getContentResolver().query(MediaStore.Files.getContentUri("external"), null, selection.toString(), args, MediaStore.Images.Media.DATE_MODIFIED);
        List<String> photoUrlList = new ArrayList<String>();
        String fileId;
        String fileName;
        String filePath;
        while (cursor.moveToNext()) {
            // 获取图片的路径
            fileId = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID));
            fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.TITLE));
            filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
            Log.e("AllVideoImages", cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE)) + "--" + filePath);
            FileItem fileItem = new FileItem(fileId, filePath, fileName);
            files.add(fileItem);
        }
        cursor.close();
        cursor = null;
        return files;
    }

}
