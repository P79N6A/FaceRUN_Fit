package com.fly.run.utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by xinzhendi-031 on 2016/11/10.
 */
public class MyOrientationListener implements SensorEventListener {

    private SensorManager mSensorManager;
    private Context mContext;
    private Sensor mSensor;
    private float lastX,lastY;


    private OnOrientationListener mOnOrientationListener;

    public void setmOnOrientationListener(OnOrientationListener mOnOrientationListener) {
        this.mOnOrientationListener = mOnOrientationListener;
    }

    public MyOrientationListener(Context context) {
        this.mContext = context;
    }


    public void star() {
        mSensorManager = (SensorManager) mContext
                .getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager != null) {
            //获得方向传感器
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        }

        if (mSensor != null) {
//            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
        }
    }

    public void stop() {
        //停止定位
        mSensorManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float x = event.values[SensorManager.DATA_X];
            float y = event.values[SensorManager.DATA_Y];
            if (Math.abs(x - lastX) > 0.1f || Math.abs(y - lastY) > 0.1f) {
                if (mOnOrientationListener != null) {
                    mOnOrientationListener.onOrientationChanged(x,y);
                }
            }
            lastX = x;
            lastY = y;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public interface OnOrientationListener {
        void onOrientationChanged(float x,float y);
    }
}