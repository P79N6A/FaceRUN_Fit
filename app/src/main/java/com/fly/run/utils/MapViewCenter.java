package com.fly.run.utils;

import android.content.Context;
import android.os.Bundle;

import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.MapView;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;

/**
 * Created by kongwei on 2017/2/24.
 */

public class MapViewCenter {

    // 定义成都市经纬度坐标 new LatLng(30.657598, 104.065601)
    public static LatLng centerCDPoint = new LatLng(30.657598, 104.065601);

    public static TextureMapView getCenterTextureMapView(Context context, Bundle b, LatLng centerLatLng, float zoom) {
        // 定义了一个配置 AMap 对象的参数类
        AMapOptions aMapOptions = new AMapOptions();
        // 设置了一个可视范围的初始化位置
        // CameraPosition 第一个参数： 目标位置的屏幕中心点经纬度坐标。
        // CameraPosition 第二个参数： 目标可视区域的缩放级别
        // CameraPosition 第三个参数： 目标可视区域的倾斜度，以角度为单位。
        // CameraPosition 第四个参数： 可视区域指向的方向，以角度为单位，从正北向顺时针方向计算，从0度到360度
        aMapOptions.camera(new CameraPosition(centerLatLng == null ? centerCDPoint : centerLatLng, zoom < 3 ? 10f : zoom, 0, 0));
        TextureMapView mapView = new TextureMapView(context, aMapOptions);
        mapView.onCreate(b);
        return mapView;
    }

    public static MapView getCenterMapView(Context context, Bundle b, LatLng centerLatLng, float zoom) {
        // 定义了一个配置 AMap 对象的参数类
        AMapOptions aMapOptions = new AMapOptions();
        // 设置了一个可视范围的初始化位置
        // CameraPosition 第一个参数： 目标位置的屏幕中心点经纬度坐标。
        // CameraPosition 第二个参数： 目标可视区域的缩放级别
        // CameraPosition 第三个参数： 目标可视区域的倾斜度，以角度为单位。
        // CameraPosition 第四个参数： 可视区域指向的方向，以角度为单位，从正北向顺时针方向计算，从0度到360度
        aMapOptions.camera(new CameraPosition(centerLatLng == null ? centerCDPoint : centerLatLng, zoom < 3 ? 10f : zoom, 0, 0));
        MapView mapView = new MapView(context, aMapOptions);
        mapView.onCreate(b);
        return mapView;
    }
}
