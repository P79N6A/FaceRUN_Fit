package com.fly.run.activity.map.smooth;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.TranslateAnimation;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.activity.offlinemap.ToastUtil;
import com.fly.run.bean.RunBean;
import com.fly.run.db.helper.RunDBHelper;
import com.fly.run.utils.LocationUtils;
import com.fly.run.utils.Logger;
import com.fly.run.utils.MapViewCenter;
import com.fly.run.utils.NetWorkUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 包名： com.amap.map3d.demo.smooth
 * <p>
 * 创建时间：2016/12/5
 * 项目名称：AMap3DDemo
 *
 * @author guibao.ggb
 * @email guibao.ggb@alibaba-inc.com
 * <p>
 * 类说明：应用于出行应用的小车平滑移动
 */
public class SmoothMoveActivity extends BaseUIActivity implements AMap.OnMarkerClickListener {

    private MapView mMapView;
    private AMap mAMap;
    private Polyline mPolyline;

    private List<LatLng> pointsLine;

    //    private String strPointsData;
    private RunBean runBean;
    private LocationUtils locationUtils;
    private boolean isUpdate = false;
    private double mAllDistance = 0;
    private boolean isTrain = false;

    private LatLng startLatLng, endLatLng;
    private Marker startMarker, endMarker;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smooth_move);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.containsKey("Bean"))
                runBean = (RunBean) b.getSerializable("Bean");
            if (b.containsKey("IsTrain"))
                isTrain = b.getBoolean("IsTrain", false);
        }

        if (pointsLine == null)
            pointsLine = readLatLngs();
        if (NetWorkUtil.haveNetWork(this) && runBean != null
                && (TextUtils.isEmpty(runBean.getmNearBy())
                || TextUtils.isEmpty(runBean.getmAddress()))) {
            locationUtils = new LocationUtils();
            locationUtils.startGeocodeSearch(this, new LatLonPoint(runBean.getmLat(), runBean.getmLon()), new GeocodeSearch.OnGeocodeSearchListener() {
                @Override
                public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                    if (i == AMapException.CODE_AMAP_SUCCESS) {
                        if (regeocodeResult != null && regeocodeResult.getRegeocodeAddress() != null
                                && regeocodeResult.getRegeocodeAddress().getFormatAddress() != null) {
                            String addressName = regeocodeResult.getRegeocodeAddress().getFormatAddress();
                            String poiName = addressName;
                            List<PoiItem> poiItems = regeocodeResult.getRegeocodeAddress().getPois();
                            if (poiItems != null && poiItems.size() > 0) {
                                String name = poiItems.get(0).getTitle();
                                if (!TextUtils.isEmpty(name))
                                    poiName = name;
                            }
                            if (TextUtils.isEmpty(runBean.getmAddress()))
                                runBean.setmAddress(addressName);
                            if (TextUtils.isEmpty(runBean.getmNearBy()))
                                runBean.setmNearBy(poiName);
                            isUpdate = RunDBHelper.update(runBean);
                            Logger.e(TAG, "update = " + isUpdate);
                        }
                    }
                }

                @Override
                public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

                }
            });
        }
        RelativeLayout layout_content_map = (RelativeLayout) findViewById(R.id.layout_content_map);
//        LatLng centerLatLng = new LatLng(30.540925, 104.075236);
        mMapView = MapViewCenter.getCenterMapView(SmoothMoveActivity.this, savedInstanceState, centerPoint, 17);
        layout_content_map.addView(mMapView);
        init();
    }

    @Override
    public void onBackPressed() {
        if (isUpdate) {
            setResult(RESULT_OK);
        }
        super.onBackPressed();
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (mAMap == null) {
            mAMap = mMapView.getMap();
        }
        mAMap.setOnMarkerClickListener(this);
        mAMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                addPolylineInPlayGround();
            }
        });
        UiSettings uiSettings = mAMap.getUiSettings();
        uiSettings.setScaleControlsEnabled(true);
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        jumpPoint(marker);
        return true;
    }

    /**
     * 添加一个跳动的Marker
     */
    public synchronized void addJumpMarker(Marker marker, LatLng latLng, int drawable) {
        if (marker == null) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(drawable <= 0 ? R.drawable.purple_pin : drawable))
                    .position(latLng)
                    .draggable(true);
            marker = mAMap.addMarker(markerOptions);
        }
//        startJumpAnimation(marker, latLng);
        jumpPoint(marker);
    }

    /**
     * 跳动的Marker
     */
    private synchronized void startJumpAnimation(Marker marker, LatLng target) {
        if (marker != null) {
            //使用TranslateAnimation,填写一个需要移动的目标点
            Animation animation = new TranslateAnimation(target);
            animation.setInterpolator(new Interpolator() {
                @Override
                public float getInterpolation(float input) {
                    // 模拟重加速度的interpolator
                    Logger.e(TAG, "input = " + input);
                    if (input <= 0.5) {
                        return (float) (0.5f - 2 * (0.5 - input) * (0.5 - input));
                    } else {
                        return (float) (0.5f - Math.sqrt((input - 0.5f) * (1.5f - input)));
                    }
                }
            });
            //整个移动所需要的时间
            animation.setDuration(600);
            //设置动画
            marker.setAnimation(animation);
            //开始动画
            marker.startAnimation();
        }
    }

    /**
     * marker点击时跳动一下
     */
    public void jumpPoint(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mAMap.getProjection();
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


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();

    }

    public void setLine(View view) {
//        addPolylineInPlayGround();
    }

    /**
     * 开始移动
     */
    public void startMove(View view) {
        if (mPolyline == null) {
            ToastUtil.showShortToast(this, "请先设置路线");
            return;
        }
        // 读取轨迹点
        if (pointsLine == null)
            pointsLine = readLatLngs();
        if (pointsLine == null && pointsLine.size() < 2)
            return;
        // 构建 轨迹的显示区域
        LatLngBounds bounds = new LatLngBounds(pointsLine.get(0), pointsLine.get(pointsLine.size() - 2));
        mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));

        // 实例 SmoothMoveMarker 对象
        SmoothMoveMarker smoothMarker = new SmoothMoveMarker(mAMap);
        // 设置 平滑移动的 图标
        smoothMarker.setDescriptor(BitmapDescriptorFactory.fromResource(R.drawable.icon_car));

        // 取轨迹点的第一个点 作为 平滑移动的启动
        LatLng drivePoint = pointsLine.get(0);
        Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(pointsLine, drivePoint);
        pointsLine.set(pair.first, drivePoint);
        List<LatLng> subList = pointsLine.subList(pair.first, pointsLine.size());

        // 设置轨迹点
        smoothMarker.setPoints(subList);
        // 设置平滑移动的总时间  单位  秒
        smoothMarker.setTotalDuration(40);

        // 设置  自定义的InfoWindow 适配器
        mAMap.setInfoWindowAdapter(infoWindowAdapter);
        // 显示 infowindow
        smoothMarker.getMarker().showInfoWindow();

        // 设置移动的监听事件  返回 距终点的距离  单位 米
        smoothMarker.setMoveListener(new SmoothMoveMarker.MoveListener() {
            @Override
            public void move(final double distance) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (infoWindowLayout != null && title != null) {
                            title.setText("距离终点还有： " + (int) distance + "米");
                        }
                    }
                });
            }
        });
        // 开始移动
        smoothMarker.startSmoothMove();
    }

    /**
     * 个性化定制的信息窗口视图的类
     * 如果要定制化渲染这个信息窗口，需要重载getInfoWindow(Marker)方法。
     * 如果只是需要替换信息窗口的内容，则需要重载getInfoContents(Marker)方法。
     */
    AMap.InfoWindowAdapter infoWindowAdapter = new AMap.InfoWindowAdapter() {

        // 个性化Marker的InfoWindow 视图
        // 如果这个方法返回null，则将会使用默认的信息窗口风格，内容将会调用getInfoContents(Marker)方法获取
        @Override
        public View getInfoWindow(Marker marker) {

            return getInfoWindowView(marker);
        }

        // 这个方法只有在getInfoWindow(Marker)返回null 时才会被调用
        // 定制化的view 做这个信息窗口的内容，如果返回null 将以默认内容渲染
        @Override
        public View getInfoContents(Marker marker) {

            return getInfoWindowView(marker);
        }
    };

    LinearLayout infoWindowLayout;
    TextView title;
    TextView snippet;

    /**
     * 自定义View并且绑定数据方法
     *
     * @param marker 点击的Marker对象
     * @return 返回自定义窗口的视图
     */
    private View getInfoWindowView(Marker marker) {
        if (infoWindowLayout == null) {
            infoWindowLayout = new LinearLayout(this);
            infoWindowLayout.setOrientation(LinearLayout.VERTICAL);
            title = new TextView(this);
            snippet = new TextView(this);
            title.setTextColor(Color.BLACK);
            snippet.setTextColor(Color.BLACK);
            infoWindowLayout.setBackgroundResource(R.drawable.infowindow_bg);
            infoWindowLayout.addView(title);
            infoWindowLayout.addView(snippet);
        }
        return infoWindowLayout;
    }

    /**
     * 添加轨迹线
     */
    private void addPolylineInPlayGround() {
//        List<LatLng> list = null;
        if (pointsLine == null)
            pointsLine = readLatLngs();
        if (pointsLine == null && pointsLine.size() < 2)
            return;
        List<Integer> colorList = new ArrayList<Integer>();
        List<BitmapDescriptor> bitmapDescriptors = new ArrayList<BitmapDescriptor>();

        int[] colors = new int[]{Color.argb(255, 0, 255, 0), Color.argb(255, 255, 255, 0), Color.argb(255, 255, 0, 0)};

        //用一个数组来存放纹理
        List<BitmapDescriptor> textureList = new ArrayList<BitmapDescriptor>();
        textureList.add(BitmapDescriptorFactory.fromResource(R.drawable.custtexture));

        List<Integer> texIndexList = new ArrayList<Integer>();
        texIndexList.add(0);//对应上面的第0个纹理
        texIndexList.add(1);
        texIndexList.add(2);

        Random random = new Random();
        for (int i = 0; i < pointsLine.size(); i++) {
            colorList.add(colors[random.nextInt(3)]);
            bitmapDescriptors.add(textureList.get(0));
        }

        mPolyline = mAMap.addPolyline(new PolylineOptions().setCustomTexture(BitmapDescriptorFactory.fromResource(R.drawable.custtexture)) //setCustomTextureList(bitmapDescriptors)
//				.setCustomTextureIndex(texIndexList)
                .addAll(pointsLine)
                .useGradient(true)
                .width(18));

        if (centerPoint != null) {
            Logger.e("11111111", "1111111");
            mAMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(centerPoint, mZoom, 0, 0)), 800, new AMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    if (startLatLng != null && endLatLng != null) {
                        Logger.e("11111111", "1111111 onFinish");
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                addJumpMarker(startMarker, startLatLng, R.drawable.amap_start);
                                addJumpMarker(endMarker, endLatLng, R.drawable.amap_end);
                            }
                        }, 810);
                    }
                }

                @Override
                public void onCancel() {
                    Logger.e("11111111", "1111111 onCancel");
                }
            });
        } else {
            LatLngBounds bounds = new LatLngBounds(pointsLine.get(0), pointsLine.get(pointsLine.size() - 2));
            mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
        }

    }

    /**
     * 读取坐标点
     * @return
     */
//    private List<LatLng> readLatLngs() {
//        List<LatLng> points = new ArrayList<LatLng>();
//        for (int i = 0; i < coords.length; i += 2) {
//            points.add(new LatLng(coords[i+1], coords[i]));
//        }
//        return points;
//    }

    /**
     * 读取坐标点
     *
     * @return
     */
    private List<LatLng> readLatLngs() {
//        List<LatLng> points = parseLocationLatLng(SDCardUtil.getLogDir() + File.separator + getIntent().getStringExtra("FileName"));
        List<LatLng> points = parseLocationLatLng();
        startLatLng = points.get(0);
        endLatLng = points.get(points.size() - 1);
        return points;
    }

    private float mZoom = 17.0f;
    private float mLatDistance = 0;
    private float mLonDistance = 0;
    private LatLng centerPoint;
    //四个极端点（左上右下）
    private double mLatLeft = 0;
    private double mLonLeft = 0;
    private double mLatTop = 0;
    private double mLonTop = 0;
    private double mLatRight = 0;
    private double mLonRight = 0;
    private double mLatBottom = 0;
    private double mLonBottom = 0;

    private void filter4Point(int index, double lat, double lon) {
        if (index == 0) {
            mLatLeft = lat;
            mLonLeft = lon;
            mLatTop = lat;
            mLonTop = lon;
            mLatRight = lat;
            mLonRight = lon;
            mLatBottom = lat;
            mLonBottom = lon;
        }
        //左边的点
        if (lon < mLonLeft) {
            mLonLeft = lon;
            mLatLeft = lat;
        }
        //上边的点
        if (lat > mLatTop) {
            mLonTop = lon;
            mLatTop = lat;
        }
        //右边的点
        if (lon > mLonRight) {
            mLonRight = lon;
            mLatRight = lat;
        }
        //下边的点
        if (lat < mLatBottom) {
            mLonBottom = lon;
            mLatBottom = lat;
        }
    }

    public List<LatLng> parseLocationLatLng() {
        List<LatLng> locLists = new ArrayList<LatLng>();
        JSONArray array = null;
//            if (TextUtils.isEmpty(strPointsData)) {
//                strPointsData = IOTools.readFileSdcardFile(path);
//            }
        if (runBean == null)
            return locLists;
        String strPointsData = runBean.getmRunCoordinateList();
        if (!TextUtils.isEmpty(strPointsData))
            array = JSON.parseArray(strPointsData);
        if (array == null || array.size() < 2)
            return locLists;
        String strSpeed = runBean.getmRunSpeed();
        long speed = 0;
        try {
            if (!TextUtils.isEmpty(strSpeed))
                speed = Long.parseLong(strSpeed);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int arraySize = array.size();
        for (int i = 0; i < arraySize; i++) {
            JSONObject traceItem = array.getJSONObject(i);
            double lat = traceItem.getDoubleValue("mLat");
            double lon = traceItem.getDoubleValue("mLon");
            filter4Point(i, lat, lon);
            LatLng mLatLng = new LatLng(lat, lon);
            if (speed >= 10 && speed < 15) {
                if (i % 5 == 0)
                    locLists.add(mLatLng);
            } else if (speed >= 15 && speed < 20) {
                if (i % 10 == 0)
                    locLists.add(mLatLng);
            } else if (speed >= 20 && speed < 25) {
                if (i % 15 == 0)
                    locLists.add(mLatLng);
            } else if (speed >= 25) {
                if (i % 20 == 0)
                    locLists.add(mLatLng);
            } else {
                locLists.add(mLatLng);
            }
            if (i > 0) {
                LatLng preLatlon = locLists.get(i - 1);
                double oneDistance = AMapUtils.calculateLineDistance(mLatLng, preLatlon);
                mAllDistance += oneDistance;
            }
        }
//        double beanDistance = Double.parseDouble(runBean.getmRunDistance());
//        if ((int) beanDistance != (int) mAllDistance) {
//            runBean.setmRunDistance(String.valueOf(mAllDistance));
//            isUpdate = RunDBHelper.update(runBean);
//        }
        double latCenter = (mLatTop + mLatBottom) / 2;
        double lonCenter = (mLonRight + mLonLeft) / 2;
        if (latCenter != 0 || lonCenter != 0) {
            centerPoint = new LatLng(latCenter, lonCenter);
            mLatDistance = AMapUtils.calculateLineDistance(new LatLng(mLatTop, mLonTop), new LatLng(mLatBottom, mLonTop));
            mLonDistance = AMapUtils.calculateLineDistance(new LatLng(mLatLeft, mLonLeft), new LatLng(mLatLeft, mLonRight));
            mZoom = LocationUtils.setMapZoom(mLatDistance, mLonDistance);
        }
        return locLists;
    }
}
