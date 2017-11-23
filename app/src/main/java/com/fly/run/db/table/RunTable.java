package com.fly.run.db.table;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by xinzhendi-031 on 2015/11/26.
 */
public class RunTable extends AbsBaseTable {
    public static final String TABLE_NAME = "table_run";

    /**
     * 表中所有的列名
     */

    public interface Columns extends BaseColumns {
        String RUN_ID = "run_id";//
        String RUN_LONGITUDE = "run_longitude";//经度
        String RUN_LATITUDE = "run_latitude";//纬度
        String RUN_ADDRESS = "run_address";//地点
        String RUN_NEARBY = "run_nearby";//附近
        String RUN_CONTINUE_DAYS = "run_continue_days";//持续天数
        String RUN_DATE = "run_date";//日期
        String RUN_DISTANCE = "run_distance";//路程
        String RUN_SPEED = "run_speed";//速度
        String RUN_USE_TIME = "run_use_time";//用时
        String RUN_HEAT = "run_heat";//热量
        String RUN_COORDINATE_LIST = "run_coordinate_list";//跑步坐标列表
        String RUN_COVER = "run_cover";//封面
        String RUN_CITY = "run_city";//城市
        String RUN_DISTRICT = "run_district";//城区信息
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String getCreateTableSql() {
        StringBuffer sqlStatement = new StringBuffer();
        sqlStatement.append("CREATE TABLE IF NOT EXISTS ").append(TABLE_NAME);
        sqlStatement.append(" (");
        sqlStatement.append(Columns.RUN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
        sqlStatement.append(Columns.RUN_LONGITUDE).append(" DOUBLE,");
        sqlStatement.append(Columns.RUN_LATITUDE).append(" DOUBLE,");
        sqlStatement.append(Columns.RUN_ADDRESS).append(" TEXT,");
        sqlStatement.append(Columns.RUN_NEARBY).append(" TEXT,");
        sqlStatement.append(Columns.RUN_CONTINUE_DAYS).append(" TEXT,");
        sqlStatement.append(Columns.RUN_DATE).append(" TEXT,");
        sqlStatement.append(Columns.RUN_DISTANCE).append(" TEXT,");
        sqlStatement.append(Columns.RUN_SPEED).append(" TEXT,");
        sqlStatement.append(Columns.RUN_USE_TIME).append(" TEXT,");
        sqlStatement.append(Columns.RUN_HEAT).append(" TEXT,");
        sqlStatement.append(Columns.RUN_COVER).append(" TEXT,");
        sqlStatement.append(Columns.RUN_CITY).append(" TEXT,");
        sqlStatement.append(Columns.RUN_DISTRICT).append(" TEXT,");
        sqlStatement.append(Columns.RUN_COORDINATE_LIST).append(" TEXT");
        sqlStatement.append(");");
        return sqlStatement.toString();
    }

    /**
     * 方法2：检查表中某列是否存在
     *
     * @param db
     * @param tableName  表名
     * @param columnName 列名
     * @return
     */
    private boolean checkColumnExists(SQLiteDatabase db, String tableName
            , String columnName) {
        boolean result = false;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from sqlite_master where name = ? and sql like ?"
                    , new String[]{tableName, "%" + columnName + "%"});
            result = null != cursor && cursor.moveToFirst();
        } catch (Exception e) {
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }
}
