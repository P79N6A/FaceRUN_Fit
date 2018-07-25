/**
 *
 */
package com.fly.run.utils;

import android.content.Context;
import android.text.TextUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 辅助工具类
 *
 * @author hongming.wang
 * @创建时间： 2015年11月24日 上午11:46:50
 * @项目名称： AMapLocationDemo2.x
 * @文件名称: Utils.java
 * @类型名称: Utils
 */
public class LocationUtils {
    /**
     * 开始定位
     */
    public final static int MSG_LOCATION_START = 0;
    /**
     * 定位完成
     */
    public final static int MSG_LOCATION_FINISH = 1;
    /**
     * 停止定位
     */
    public final static int MSG_LOCATION_STOP = 2;

    public final static String KEY_URL = "URL";
    public final static String URL_H5LOCATION = "file:///android_asset/location.html";


    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = new AMapLocationClientOption();

    private GeocodeSearch geocoderSearch;

    /**
     * 初始化定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    public void startLocation(Context context, boolean OnceLocation) {
        //初始化client
        locationClient = new AMapLocationClient(context);
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption(OnceLocation));
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
        // 启动定位
        locationClient.startLocation();
    }


    /**
     * 停止定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    public void stopLocation() {
        // 停止定位
        locationClient.stopLocation();
    }

    /**
     * 销毁定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    public void destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption(boolean OnceLocation) {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(OnceLocation);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            if (listener != null)
                listener.onLocationChanged(loc);
            if (null != loc && loc.getErrorCode() == 0) {
                
            } else {
//                ToastUtil.show("定位失败");
            }
        }
    };

    public void startGeocodeSearch(Context context, LatLonPoint latLonPoint, GeocodeSearch.OnGeocodeSearchListener listener) {
        geocoderSearch = new GeocodeSearch(context);
        geocoderSearch.setOnGeocodeSearchListener(listener == null ? regeocodeSearchListener : listener);
        getAddress(latLonPoint);
    }

    private GeocodeSearch.OnGeocodeSearchListener regeocodeSearchListener = new GeocodeSearch.OnGeocodeSearchListener() {
        @Override
        public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
            if (i == AMapException.CODE_AMAP_SUCCESS) {
                if (regeocodeResult != null && regeocodeResult.getRegeocodeAddress() != null
                        && regeocodeResult.getRegeocodeAddress().getFormatAddress() != null) {
                    String addressName = regeocodeResult.getRegeocodeAddress().getFormatAddress()
                            + "附近";
                    ToastUtil.show(addressName);
                } else {
                    ToastUtil.show("没有搜索到相关数据");
                }
            } else {
                ToastUtil.show(i);
            }
        }

        @Override
        public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

        }
    };

    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
    }

    private LocationChangedListener listener = null;

    public LocationUtils setListener(LocationChangedListener listener) {
        this.listener = listener;
        return this;
    }

    public interface LocationChangedListener {
        public void onLocationChanged(AMapLocation loc);
    }

    /**
     * 根据定位结果返回定位信息的字符串
     *
     * @param location
     * @return
     */
    public synchronized static String getLocationStr(AMapLocation location) {
        if (null == location) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
        if (location.getErrorCode() == 0) {
            sb.append("定位成功" + "\n");
            sb.append("定位类型: " + location.getLocationType() + "\n");
            sb.append("经    度    : " + location.getLongitude() + "\n");
            sb.append("纬    度    : " + location.getLatitude() + "\n");
            sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
            sb.append("提供者    : " + location.getProvider() + "\n");

            sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
            sb.append("角    度    : " + location.getBearing() + "\n");
            // 获取当前提供定位服务的卫星个数
            sb.append("星    数    : " + location.getSatellites() + "\n");
            sb.append("国    家    : " + location.getCountry() + "\n");
            sb.append("省            : " + location.getProvince() + "\n");
            sb.append("市            : " + location.getCity() + "\n");
            sb.append("城市编码 : " + location.getCityCode() + "\n");
            sb.append("区            : " + location.getDistrict() + "\n");
            sb.append("区域 码   : " + location.getAdCode() + "\n");
            sb.append("地    址    : " + location.getAddress() + "\n");
            sb.append("兴趣点    : " + location.getPoiName() + "\n");
            //定位完成的时间
            sb.append("定位时间: " + formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
        } else {
            //定位失败
            sb.append("定位失败" + "\n");
            sb.append("错误码:" + location.getErrorCode() + "\n");
            sb.append("错误信息:" + location.getErrorInfo() + "\n");
            sb.append("错误描述:" + location.getLocationDetail() + "\n");
        }
        //定位之后的回调时间
        sb.append("回调时间: " + formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");
        return sb.toString();
    }

    private static SimpleDateFormat sdf = null;

    public synchronized static String formatUTC(long l, String strPattern) {
        if (TextUtils.isEmpty(strPattern)) {
            strPattern = "yyyy-MM-dd HH:mm:ss";
        }
        if (sdf == null) {
            try {
                sdf = new SimpleDateFormat(strPattern, Locale.CHINA);
            } catch (Throwable e) {
            }
        } else {
            sdf.applyPattern(strPattern);
        }
        return sdf == null ? "NULL" : sdf.format(l);
    }


    /**
     * 1 GPS定位结果
     * 2 前次定位结果
     * 4 缓存定位结果
     * 5 Wifi定位结果
     * 6 基站定位结果
     * 8 离线定位结果
     */
    public static String checkLocType(int type) {
        String strType = "定位失败";
        switch (type) {
            case 1:
                strType = "GPS";
                break;
            case 2:
                strType = "前次";
                break;
            case 4:
                strType = "缓存";
                break;
            case 5:
                strType = "Wifi";
                break;
            case 6:
                strType = "基站";
                break;
            case 8:
                strType = "离线";
                break;
        }
        return strType;
    }

    public static String minkm = "分/公里";
    public static String hourkm = "时/公里";

    /**
     * 速度计算  分钟／公里
     * distance单位：米
     * useTime单位：毫秒
     */
    public static String caculateSpeed(double distance, long useTime) {
        String strSpeed = "";
        String danwei = minkm;
        if (distance > 0 && useTime > 0) {
            double sec = useTime / 1000f;
            double min = sec / 60f;
            double h = 0;
            if (min >= 60) {
                h = min / 60;
            }
            double speed = 0;
//            if (h >= 1) {
//                speed = h / (distance / 1000f);
//                danwei = hourkm;
//            } else {
                speed = min / (distance / 1000f);
//                if (speed > 60) {
//                    speed = speed / 60f;
//                    danwei = hourkm;
//                }
//            }

            if (speed > 0) {
                strSpeed = TimeFormatUtils.retainOne(speed) + danwei;
            }
        }
        return strSpeed;
    }

    /**
     * 速度计算  分钟／公里
     * distance单位：米
     * useTime单位：毫秒
     */
    public static String caculateSpeedMin(double distance, long useTime) {
        String strSpeed = "";
        if (distance > 0 && useTime > 0) {
            double sec = useTime / 1000f;
            double min = sec / 60f;
            double h = 0;
            if (min >= 60) {
                h = min / 60;
            }
            double speed = min / (distance / 1000f);
            if (speed > 0) {
                strSpeed = TimeFormatUtils.retainOne(speed);
            }
        }
        return strSpeed;
    }


    public static float setMapZoom(float mLatDistance, float mLonDistance) {
        float zoom = 10.0f;
        if (mLatDistance != 0 && mLonDistance != 0) {
            if (mLatDistance / mLonDistance >= 2) {
                //以经度为准
                zoom = setmZoom(mLatDistance);
            } else {
                //以纬度为准
                zoom = setmZoom(mLonDistance);
            }
        }
        return zoom;
    }

    public static float setmZoom(double distance) {
        float mZoom = 10.0f;
        float min = 0.0f;
        if (distance < 100) {
            mZoom = 19.0f;
        } else if (distance >= 100 && distance < 250) {
            min = (float) ((distance - 100f) / 150f);
            mZoom = 19.0f;
        } else if (distance >= 250 && distance < 500) {
            min = (float) ((distance - 250f) / 250f);
            mZoom = 18.0f;
        } else if (distance >= 500 && distance < 1000) {
            min = (float) ((distance - 500f) / 500f);
            mZoom = 17.0f;
        } else if (distance >= 1000 && distance < 2000) {
            min = (float) ((distance - 1000f) / 1000f);
            mZoom = 16.0f;
        } else if (distance >= 2000 && distance < 4000) {
            min = (float) ((distance - 2000f) / 2000f);
            mZoom = 15.0f;
        } else if (distance >= 4000 && distance < 8000) {
            min = (float) ((distance - 4000f) / 4000f);
            mZoom = 14.0f;
        } else if (distance >= 8000 && distance < 16000) {
            min = (float) ((distance - 8000f) / 8000f);
            mZoom = 13.0f;
        } else if (distance >= 16000 && distance < 32000) {
            min = (float) ((distance - 16000f) / 16000f);
            mZoom = 12.0f;
        } else if (distance >= 32000 && distance < 64000) {
            min = (float) ((distance - 32000f) / 32000f);
            mZoom = 11.0f;
        }
        mZoom -= min;
        return mZoom;
    }
}
