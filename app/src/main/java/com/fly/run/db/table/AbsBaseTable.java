package com.fly.run.db.table;

import android.database.sqlite.SQLiteDatabase;

/**
 * BaseTable.java - 数据库表基类
 *
 * @author Kevin.Zhang
 *         <p/>
 *         2014-7-7 下午1:58:53
 */
public abstract class AbsBaseTable {

    /**
     * 获得数据库表名（抽象方法）
     *
     * @return
     */
    protected abstract String getTableName();

    /**
     * 获得数据库建表语句（抽象方法）
     *
     * @return
     */
    protected abstract String getCreateTableSql();

    public void createTable(SQLiteDatabase db) {
        db.execSQL(getCreateTableSql());
    }

    public void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + getTableName());
    }

    public void clearAllData(SQLiteDatabase db) {
        db.delete(getTableName(), null, null);
    }

}
