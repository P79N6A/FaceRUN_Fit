package com.fly.run.fragment;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.Projection;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceListener;
import com.amap.api.trace.TraceLocation;
import com.amap.api.trace.TraceOverlay;
import com.fly.run.R;
import com.fly.run.activity.MainRunActivity;
import com.fly.run.bean.NearByBean;
import com.fly.run.fragment.base.BaseFragment;
import com.fly.run.fragment.dialog.DialogNearByFragment;
import com.fly.run.utils.Logger;
import com.fly.run.utils.MapViewCenter;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link RunMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RunMapFragment extends BaseFragment implements TraceListener, LocationSource, AMapLocationListener, AMap.OnMarkerClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AMap aMap;
    private TextureMapView mapView;
    private UiSettings mUiSettings;

    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private ImageView ivLocationAnchor;
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);

    private int mCoordinateType = LBSTraceClient.TYPE_AMAP;
    private LBSTraceClient mTraceClient;
    private ConcurrentMap<Integer, TraceOverlay> mOverlayList = new ConcurrentHashMap<Integer, TraceOverlay>();
    private int mSequenceLineID = 1000;

    private float defaultZoom = 17.2f;
    private float anchorZoom = 18.0f;
    private float currentZoom = defaultZoom;
    private float currentBearing = 0f; //旋转角度
    private float currentTilt = 0f;  //倾斜角度
    private boolean isCameraChange = false;

    private boolean mapLoaded = false;

    public RunMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RunMapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RunMapFragment newInstance(String param1, String param2) {
        RunMapFragment fragment = new RunMapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_run_map, container, false);
//        mapView = (TextureMapView) rootView.findViewById(R.id.mapview);
        RelativeLayout layout_content_map = (RelativeLayout) rootView.findViewById(R.id.layout_content_map);
        // 定义成都市经纬度坐标
        LatLng centerCDPoint = new LatLng(30.657598, 104.065601);
        mapView = MapViewCenter.getCenterTextureMapView(getActivity(), savedInstanceState, centerCDPoint, 10);
        layout_content_map.addView(mapView);
        initMap();
        ivLocationAnchor = (ImageView) rootView.findViewById(R.id.iv_location_anchor);
        ivLocationAnchor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AMapLocation aMapLocation = ((MainRunActivity) getActivity()).getaMapLocation();
                if (aMapLocation != null
                        && aMapLocation.getErrorCode() == 0
                        && aMapLocation.getLatitude() != 0
                        && aMapLocation.getLongitude() != 0) {
                    changeCamera(
                            CameraUpdateFactory.newCameraPosition(new CameraPosition(
                                    new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), anchorZoom, 0, 0)), 800,
                            null);
                }
            }
        });
        return rootView;
    }

    public TextureMapView getMapView() {
        return mapView;
    }

    /**
     * 初始化AMap对象
     */
    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            mUiSettings = aMap.getUiSettings();
            aMap.setOnMarkerClickListener(this);
        }
        mUiSettings.setMyLocationButtonEnabled(false); // 是否显示默认的定位按钮
        mUiSettings.setZoomControlsEnabled(false);
        aMap.setLocationSource(this);
        aMap.setMyLocationEnabled(true);// 是否可触发定位并显示定位层
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
//        setupLocationStyle();
        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                mapLoaded = true;
            }
        });
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                isCameraChange = true;
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                currentZoom = cameraPosition.zoom;
                currentBearing = cameraPosition.bearing;
                currentTilt = cameraPosition.tilt;
                isCameraChange = false;
            }
        });
    }

    /**
     * 设置自定义定位蓝点
     */
    private void setupLocationStyle() {
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.drawable.gps_point));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(STROKE_COLOR);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(5);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(FILL_COLOR);
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap.setMyLocationStyle(myLocationStyle);
    }

    public boolean firstLoadMap = true;
    public AMapLocation aMapLocation;

    public void move2MyLocation() {
        if (!mapLoaded)
            return;
        aMapLocation = ((MainRunActivity) getActivity()).getaMapLocation();
        if (aMapLocation != null
                && aMapLocation.getLatitude() != 0
                && aMapLocation.getLongitude() != 0) {
            if (mListener != null)
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    LatLng mLatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    if (firstLoadMap) {
                        firstLoadMap = false;
                        changeCamera(
                                CameraUpdateFactory.newCameraPosition(new CameraPosition(mLatLng, 17.2f, 0, 0)), 800, null);
                    } else {
                        if (!isCameraChange)
                            changeCamera(
                                    CameraUpdateFactory.newCameraPosition(new CameraPosition(mLatLng, currentZoom, currentTilt, currentBearing)), 600, null);
                    }
                }
            });
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    public void setMapFocusable() {
        if (mapView != null){
            mapView.setFocusable(true);
            mapView.requestFocus();
            mapView.requestFocusFromTouch();
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
//        if (mlocationClient == null) {
//            mlocationClient = new AMapLocationClient(getActivity());
//            mLocationOption = new AMapLocationClientOption();
//            //设置定位监听
//            mlocationClient.setLocationListener(this);
//            //设置为高精度定位模式
//            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//            //设置定位参数
//            mlocationClient.setLocationOption(mLocationOption);
//            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
//            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
//            // 在定位结束后，在合适的生命周期调用onDestroy()方法
//            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//            mlocationClient.startLocation();
//        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null) {
            mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
        }
    }

    /**
     * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
     */
    private void changeCamera(CameraUpdate update, long duration, AMap.CancelableCallback callback) {
        boolean animated = true;
        if (animated) {
            aMap.animateCamera(update, (duration <= 0 ? 1000 : duration), callback);
        } else {
            aMap.moveCamera(update);
        }
    }

    /**
     * 轨迹纠偏失败回调
     */
    @Override
    public void onRequestFailed(int lineID, String errorInfo) {
        Log.d(TAG, "onRequestFailed");
        Toast.makeText(getActivity().getApplicationContext(), errorInfo,
                Toast.LENGTH_SHORT).show();
        if (mOverlayList.containsKey(lineID)) {
            TraceOverlay overlay = mOverlayList.get(lineID);
            overlay.setTraceStatus(TraceOverlay.TRACE_STATUS_FAILURE);
            setDistanceWaitInfo(overlay);
        }
    }

    /**
     * 轨迹纠偏过程回调
     */
    @Override
    public void onTraceProcessing(int lineID, int index, List<LatLng> segments) {
        Log.d(TAG, "onTraceProcessing");
        if (segments == null) {
            return;
        }
        if (mOverlayList.containsKey(lineID)) {
            TraceOverlay overlay = mOverlayList.get(lineID);
            overlay.setTraceStatus(TraceOverlay.TRACE_STATUS_PROCESSING);
            overlay.add(segments);
        }
    }

    /**
     * 轨迹纠偏结束回调
     */
    @Override
    public void onFinished(int lineID, List<LatLng> linepoints, int distance,
                           int watingtime) {
        Log.d(TAG, "onFinished");
        Toast.makeText(getActivity().getApplicationContext(), "onFinished",
                Toast.LENGTH_SHORT).show();
        if (mOverlayList.containsKey(lineID)) {
            TraceOverlay overlay = mOverlayList.get(lineID);
            overlay.setTraceStatus(TraceOverlay.TRACE_STATUS_FINISH);
            overlay.setDistance(distance);
            overlay.setWaitTime(watingtime);
            setDistanceWaitInfo(overlay);
        }
    }

    /**
     * 调起一次轨迹纠偏
     */
    public void traceGrasp() {
        if (mOverlayList.containsKey(mSequenceLineID)) {
            TraceOverlay overlay = mOverlayList.get(mSequenceLineID);
            overlay.zoopToSpan();
            int status = overlay.getTraceStatus();
            String tipString = "";
            if (status == TraceOverlay.TRACE_STATUS_PROCESSING) {
                tipString = "该线路轨迹纠偏进行中...";
                setDistanceWaitInfo(overlay);
            } else if (status == TraceOverlay.TRACE_STATUS_FINISH) {
                setDistanceWaitInfo(overlay);
                tipString = "该线路轨迹已完成";
            } else if (status == TraceOverlay.TRACE_STATUS_FAILURE) {
                tipString = "该线路轨迹失败";
            } else if (status == TraceOverlay.TRACE_STATUS_PREPARE) {
                tipString = "该线路轨迹纠偏已经开始";
            }
            Toast.makeText(getActivity().getApplicationContext(), tipString,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        TraceOverlay mTraceOverlay = new TraceOverlay(aMap);
        mOverlayList.put(mSequenceLineID, mTraceOverlay);
        List<LatLng> mapList = ((MainRunActivity) getActivity()).traceMapList;
        mTraceOverlay.setProperCamera(mapList);
        mTraceClient = new LBSTraceClient(getActivity().getApplicationContext());
        List<TraceLocation> mTraceList = ((MainRunActivity) getActivity()).mTraceList;
        mTraceClient.queryProcessedTrace(mSequenceLineID, mTraceList,
                mCoordinateType, this);
    }

    /**
     * 设置显示总里程和等待时间
     *
     * @param overlay
     */
    private void setDistanceWaitInfo(TraceOverlay overlay) {
        int distance = -1;
        int waittime = -1;
        if (overlay != null) {
            distance = overlay.getDistance();
            waittime = overlay.getWaitTime();
        }
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        Logger.e(TAG, "总里程 ： " + decimalFormat.format(distance / 1000d) + "KM");
        Logger.e(TAG, "等待时间 ： " + decimalFormat.format(waittime / 60d) + "Min");
    }

    public Map<String, NearByBean> runnerMap = new HashMap<>();
    public Map<String, Marker> runnerMarkerMap = new HashMap<>();

    public void addMarkers(List<NearByBean> list) {
        if (list == null || list.size() == 0) {
            //TODO Clear Markers
            aMap.clear();
            runnerMap.clear();
            runnerMarkerMap.clear();
        } else {
            for (NearByBean bean : list) {
                if (runnerMarkerMap.containsKey(bean.getUserID())) {
                    changeMarkerInfo(bean);
                } else {
                    addJumpMarker(bean);
                }
            }
        }
    }

    /**
     * 添加一个跳动的Marker
     */
    public synchronized void addJumpMarker(NearByBean bean) {
        if (!bean.isOnLine())
            return;
        LatLng mLatLng = new LatLng(bean.getLatitude(), bean.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.purple_pin))
                .position(mLatLng)
                .draggable(true);
        Marker marker = aMap.addMarker(markerOptions);
        marker.setTitle(bean.getUserName());
        runnerMap.put(bean.getUserID(), bean);
        runnerMarkerMap.put(bean.getUserID(), marker);
        jumpPoint(marker);
    }


    public synchronized void addJumpMarker(Marker marker, LatLng latLng, int drawable) {
        if (marker == null) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(drawable <= 0 ? R.drawable.purple_pin : drawable))
                    .position(latLng)
                    .draggable(true);
            marker = aMap.addMarker(markerOptions);
        }
//        startJumpAnimation(marker, latLng);
        jumpPoint(marker);
    }

    public synchronized void changeMarkerInfo(NearByBean bean) {
        Marker marker = runnerMarkerMap.get(bean.getUserID());
        if (marker != null) {
            if (!bean.isOnLine() && marker.isVisible()) {
                marker.setVisible(false);
//                runnerMap.remove(bean.getUserID());
//                runnerMarkerMap.remove(bean.getUserID());
                return;
            } else if (bean.isOnLine() && !marker.isVisible()) {
                marker.setVisible(true);
            }
            LatLng mLatLng = new LatLng(bean.getLatitude(), bean.getLongitude());
            marker.setPosition(mLatLng);
        } else {
            //TODO marker == null
            Logger.e(TAG, "改变marker信息，marker == null");
            runnerMap.remove(bean.getUserID());
            runnerMarkerMap.remove(bean.getUserID());
        }
    }

    private Handler changeUIHandler = new Handler();

    /**
     * marker点击时跳动一下
     */
    public void jumpPoint(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = aMap.getProjection();
        final LatLng markerLatlng = marker.getPosition();
        Point markerPoint = proj.toScreenLocation(markerLatlng);
        markerPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(markerPoint);
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * markerLatlng.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * markerLatlng.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker != null && !TextUtils.isEmpty(marker.getTitle())) {
//            ToastUtil.show(marker.getTitle());
            showNearByRunner(marker.getTitle(), marker.getPosition());
        }
        return true;
    }

    private void showNearByRunner(String title, LatLng latLng) {
        if (aMapLocation != null) {
            float distance = AMapUtils.calculateLineDistance(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), latLng);
            DialogNearByFragment fragment = DialogNearByFragment.newInstance(title, "" + distance);
            if (!fragment.isVisible() && !RunMapFragment.this.isDetached())
                fragment.show(getActivity().getSupportFragmentManager(), "DialogNearbyFragment");
        }
    }
}
