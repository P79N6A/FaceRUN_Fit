package com.fly.run.db.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.fly.run.bean.RunBean;
import com.fly.run.db.table.RunTable;
import com.fly.run.utils.Logger;
import com.fly.run.utils.TimeFormatUtils;

import java.util.ArrayList;

/**
 * Created by jian.cao on 2016/3/9.
 */
public class RunDBHelper {
    private static final String TAG = "RunDBHelper";

    public static boolean insert(RunBean bean) {
        if (bean == null)
            return false;
        ContentValues values = new ContentValues();
        values.put(RunTable.Columns.RUN_LATITUDE, bean.getmLat());
        values.put(RunTable.Columns.RUN_LONGITUDE, bean.getmLon());
        values.put(RunTable.Columns.RUN_ADDRESS, bean.getmAddress());
        values.put(RunTable.Columns.RUN_NEARBY, bean.getmNearBy());
        values.put(RunTable.Columns.RUN_CONTINUE_DAYS, bean.getmCity());
        values.put(RunTable.Columns.RUN_DATE, bean.getmRunDate());
        values.put(RunTable.Columns.RUN_DISTANCE, bean.getmRunDistance());
        values.put(RunTable.Columns.RUN_HEAT, bean.getmRunHeat());
        values.put(RunTable.Columns.RUN_SPEED, bean.getmRunSpeed());
        values.put(RunTable.Columns.RUN_USE_TIME, bean.getmUseTime());
        values.put(RunTable.Columns.RUN_COORDINATE_LIST, bean.getmRunCoordinateList());
        values.put(RunTable.Columns.RUN_COVER, bean.getmRunCover());
        values.put(RunTable.Columns.RUN_CITY, bean.getmCity());
        values.put(RunTable.Columns.RUN_DISTRICT, bean.getmDistrict());
        long mInsert = 0;
        mInsert = DatabaseHelper.openDataBase().insert(RunTable.TABLE_NAME, null, values);
        Logger.e("mInsert = " + mInsert);
        DatabaseHelper.closeDataBase();
        return mInsert > 0;
    }

    public static boolean update(RunBean bean) {
        if (bean == null && bean.getmID() == 0)
            return false;
        ContentValues values = new ContentValues();
        values.put(RunTable.Columns.RUN_LATITUDE, bean.getmLat());
        values.put(RunTable.Columns.RUN_LONGITUDE, bean.getmLon());
        values.put(RunTable.Columns.RUN_ADDRESS, bean.getmAddress());
        values.put(RunTable.Columns.RUN_NEARBY, bean.getmNearBy());
        values.put(RunTable.Columns.RUN_CONTINUE_DAYS, bean.getmCity());
        values.put(RunTable.Columns.RUN_DATE, bean.getmRunDate());
        values.put(RunTable.Columns.RUN_DISTANCE, bean.getmRunDistance());
        values.put(RunTable.Columns.RUN_HEAT, bean.getmRunHeat());
        values.put(RunTable.Columns.RUN_SPEED, bean.getmRunSpeed());
        values.put(RunTable.Columns.RUN_USE_TIME, bean.getmUseTime());
        values.put(RunTable.Columns.RUN_COORDINATE_LIST, bean.getmRunCoordinateList());
        values.put(RunTable.Columns.RUN_COVER, bean.getmRunCover());
        values.put(RunTable.Columns.RUN_CITY, bean.getmCity());
        values.put(RunTable.Columns.RUN_DISTRICT, bean.getmDistrict());
        int mUpdate = 0;
        mUpdate = DatabaseHelper.openDataBase().update(RunTable.TABLE_NAME, values, "run_id = ?", new String[]{String.valueOf(bean.getmID())});
        Logger.e("mUpdate = " + mUpdate);
        DatabaseHelper.closeDataBase();
        return mUpdate > 0;
    }

    public static ArrayList<RunBean> query() {
        String select = "";
        Cursor cursor = null;
        String projection[] = {RunTable.Columns.RUN_ID,//
                RunTable.Columns.RUN_ADDRESS,//
                RunTable.Columns.RUN_NEARBY,//
                RunTable.Columns.RUN_LATITUDE,//
                RunTable.Columns.RUN_LONGITUDE,//
                RunTable.Columns.RUN_CONTINUE_DAYS,//
                RunTable.Columns.RUN_DATE,//
                RunTable.Columns.RUN_DISTANCE,//
                RunTable.Columns.RUN_HEAT,//
                RunTable.Columns.RUN_SPEED,//
                RunTable.Columns.RUN_USE_TIME,//
                RunTable.Columns.RUN_COORDINATE_LIST,
                RunTable.Columns.RUN_COVER,
                RunTable.Columns.RUN_CITY,
                RunTable.Columns.RUN_DISTRICT
        };
        ArrayList<RunBean> runList = new ArrayList<>();
        /**
         * 排序
         * */
        StringBuffer order = new StringBuffer();
        order.append(RunTable.Columns.RUN_ID).append(" desc");//按时间排序
        try {
            cursor = DatabaseHelper.openDataBase().query(RunTable.TABLE_NAME, projection, null, null, null, null, order.toString(), null);
            if (cursor == null || cursor.getCount() == 0)
                return runList;
            String titleDate = "";
            while (cursor.moveToNext()) {
                String headDate = TimeFormatUtils.getFormatDate4(Long.parseLong(cursor.getString(6)));
                if (!titleDate.equals(headDate)) {
                    titleDate = headDate;
                    RunBean bean1 = new RunBean();
                    bean1.setType(1);
                    bean1.setmRunDate(cursor.getString(6));
                    bean1.setRunHeadDate(headDate);
                    bean1.setRunHeadDistance(queryAllDistance(headDate));
                    runList.add(bean1);
                }
                RunBean bean = new RunBean();
                bean.setType(0);
                bean.setmID(cursor.getInt(0));
                bean.setmAddress(cursor.getString(1));
                bean.setmNearBy(cursor.getString(2));
                bean.setmLat(cursor.getDouble(3));
                bean.setmLon(cursor.getDouble(4));
                bean.setmCity(cursor.getString(5));
                bean.setmRunDate(cursor.getString(6));
                bean.setRunHeadDate(headDate);
                bean.setmRunDistance(cursor.getString(7));
                bean.setmRunHeat(cursor.getString(8));
                bean.setmRunSpeed(cursor.getString(9));
                bean.setmUseTime(cursor.getString(10));
                bean.setmRunCoordinateList(cursor.getString(11));
                bean.setmRunCover(cursor.getString(12));
                bean.setmCity(cursor.getString(13));
                bean.setmDistrict(cursor.getString(14));
                runList.add(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //回收
            if (cursor != null)
                cursor.close();
            DatabaseHelper.closeDataBase();
        }

        return runList;
    }

    public static RunBean queryOne() {
        RunBean bean = null;
        String select = "";
        Cursor cursor = null;
        String projection[] = {RunTable.Columns.RUN_ID,//
                RunTable.Columns.RUN_ADDRESS,//
                RunTable.Columns.RUN_NEARBY,//
                RunTable.Columns.RUN_LATITUDE,//
                RunTable.Columns.RUN_LONGITUDE,//
                RunTable.Columns.RUN_CONTINUE_DAYS,//
                RunTable.Columns.RUN_DATE,//
                RunTable.Columns.RUN_DISTANCE,//
                RunTable.Columns.RUN_HEAT,//
                RunTable.Columns.RUN_SPEED,//
                RunTable.Columns.RUN_USE_TIME,//
                RunTable.Columns.RUN_COORDINATE_LIST,
                RunTable.Columns.RUN_COVER,
                RunTable.Columns.RUN_CITY,
                RunTable.Columns.RUN_DISTRICT
        };
        /**
         * 排序
         * */
        StringBuffer order = new StringBuffer();
        order.append(RunTable.Columns.RUN_ID).append(" desc");//按时间排序

        cursor = DatabaseHelper.openDataBase().query(RunTable.TABLE_NAME, projection, null, null, null, null, order.toString(), null);
        if (cursor == null || cursor.getCount() == 0)
            return bean;
        if (cursor.moveToNext()) {
            bean = new RunBean();
            bean.setmID(cursor.getInt(0));
            bean.setmAddress(cursor.getString(1));
            bean.setmNearBy(cursor.getString(2));
            bean.setmLat(cursor.getDouble(3));
            bean.setmLon(cursor.getDouble(4));
            bean.setmCity(cursor.getString(5));
            bean.setmRunDate(cursor.getString(6));
            bean.setmRunDistance(cursor.getString(7));
            bean.setmRunHeat(cursor.getString(8));
            bean.setmRunSpeed(cursor.getString(9));
            bean.setmUseTime(cursor.getString(10));
            bean.setmRunCoordinateList(cursor.getString(11));
            bean.setmRunCover(cursor.getString(12));
            bean.setmCity(cursor.getString(13));
            bean.setmDistrict(cursor.getString(14));
        }
        //回收
        cursor.close();
        DatabaseHelper.closeDataBase();
        return bean;
    }

    public static boolean delete(RunBean bean) {
        if (bean == null && bean.getmID() == 0)
            return false;
        int mDelete = 0;
        mDelete = DatabaseHelper.openDataBase().delete(RunTable.TABLE_NAME, "run_id = ?", new String[]{String.valueOf(bean.getmID())});
        DatabaseHelper.closeDataBase();
        return mDelete > 0;
    }

    public static double queryAllDistance(String monthDate) {
        double mAllDistance = 0;
        Cursor cursor = null;
        String projection[] = {RunTable.Columns.RUN_DISTANCE, RunTable.Columns.RUN_DATE};
        /**
         * 排序
         * */
        StringBuffer order = new StringBuffer();
        order.append(RunTable.Columns.RUN_ID).append(" desc");//按时间排序

        cursor = DatabaseHelper.openDataBase().query(RunTable.TABLE_NAME, projection, null, null, null, null, order.toString(), null);
        ArrayList<RunBean> runList = new ArrayList<>();
        if (cursor == null || cursor.getCount() == 0)
            return mAllDistance;
        while (cursor.moveToNext()) {
            String strDistance = cursor.getString(0);
            String headDate = TimeFormatUtils.getFormatDate4(Long.parseLong(cursor.getString(1)));
            if (TextUtils.isEmpty(monthDate))
                mAllDistance += Double.parseDouble(strDistance);
            else {
                if (monthDate.equals(headDate))
                    mAllDistance += Double.parseDouble(strDistance);
            }
        }
        //回收
        cursor.close();
        DatabaseHelper.closeDataBase();
        return mAllDistance;
    }

}
