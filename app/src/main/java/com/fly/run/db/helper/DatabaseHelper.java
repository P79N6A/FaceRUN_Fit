/*
 * Copyright 2014-2024 setNone. All rights reserved. 
 */
package com.fly.run.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fly.run.app.App;
import com.fly.run.db.table.RunTable;
import com.fly.run.utils.Logger;

/**
 * DatabaseHelper.java - 数据库操作帮助类
 *
 * @author Kevin.Zhang
 *         <p/>
 *         2014-7-17 下午5:18:56
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * 调试TAG
     */
    private static final String TAG = "DatabaseHelper";

    /**
     * 数据库名
     */
    private static final String DATABASE_NAME = "KingRun";

    private static final long mUserId = 20170222L;

    /**
     * 数据库版本号(必须大于等于1)
     */
    private static final int DATABASE_VERSION = 1;

    public static DatabaseHelper databaseHelper = null;

    public static DatabaseHelper getInstace() {
        initHelper();
        return databaseHelper;
    }

    public static void initHelper() {
        if (databaseHelper == null)
            databaseHelper = new DatabaseHelper(App.getInstance());
    }

    public DatabaseHelper(Context context) {
        super(context, buildUserDB(mUserId), null, DATABASE_VERSION);
    }

    public DatabaseHelper(Context context, long userId) {
        super(context, buildUserDB(userId), null, DATABASE_VERSION);
    }

    // 得到根据用户名而变化的db文件（默认ChatRecord_0.db）
    private static String buildUserDB(long userId) {
        Logger.i("--->DB DatabaseHelper init!");
        return new StringBuffer(DATABASE_NAME).append("_").append(userId).append(".db").toString();

    }

    public static SQLiteDatabase mSQLiteDataBase;

    public static SQLiteDatabase openDataBase() {
        if (mSQLiteDataBase == null) {
            mSQLiteDataBase = DatabaseHelper.getInstace().getWritableDatabase();
        }
        return mSQLiteDataBase;
    }

    public static void closeDataBase() {
        if (mSQLiteDataBase != null) {
            mSQLiteDataBase.close();
            mSQLiteDataBase = null;
        }
    }

    // 首次创建数据库
    @Override
    public void onCreate(SQLiteDatabase db) {
        Logger.i(TAG, "onCreate SQLiteDatabase = " + db);
        createRunTable(db);
    }

    // 当前版本大于系统版本
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Logger.i(TAG, "onUpgrade newVersion = " + newVersion + " oldVersion = " + oldVersion);
        for (int currentVersion = oldVersion + 1; currentVersion <= newVersion; currentVersion++) {
            switch (currentVersion) {
                case 2:
                    break;
            }
        }
    }

    /**
     * 创建RunTable
     */
    private void createRunTable(SQLiteDatabase db) {
        Logger.i(TAG, "RunTable SQLiteDatabase = " + db);
        RunTable table = new RunTable();
        table.createTable(db);
    }
}