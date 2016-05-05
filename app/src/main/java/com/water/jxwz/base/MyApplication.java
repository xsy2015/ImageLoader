package com.water.jxwz.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationService;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.water.jxwz.utils.SharedInfoUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import org.litepal.LitePalApplication;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


/**
 * Created by ${xsy} on 2016/4/12.
 */
public class MyApplication extends LitePalApplication {
    private static MyApplication instance;
    public LocationService locationService;
    public Vibrator mVibrator;

    private LocationClientOption option;
    private GeoCoder mSearch;

    // 获取到主线程的handler
    private static Handler mMainThreadHandler = null;
    // 获取到主线程的looper
    private static Looper mMainThreadLooper = null;
    // 获取到主线程
    private static Thread mMainThead = null;
    // 获取到主线程的id
    private static int mMainTheadId;

    public List<Activity> activities = new LinkedList<Activity>();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        MyApplication.mMainThreadHandler = new Handler();
        MyApplication.mMainThreadLooper = getMainLooper();
        MyApplication.mMainThead = Thread.currentThread();
        MyApplication.mMainTheadId = android.os.Process.myTid();// 主线程id


        LitePalApplication.initialize(this);//初始化数据库

        //配置okhttp

        OkHttpUtils.getInstance().debug("water").setConnectTimeout(10, TimeUnit.SECONDS);


        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
        initMap();

    }

    public static MyApplication getInstance(){
        return instance;
    }

    private void initMap() {
        locationService = new LocationService(instance);
        locationService.registerListener(mListener);
        LocationClientOption option = new LocationClientOption();
        //设置是否需要地址信息，默认为无地址
        option.setIsNeedAddress(true);
        //设置是否需要返回位置语义化信息，可以在BDLocation.getLocationDescribe()中得到数据，ex:"在天安门附近"， 可以用作地址信息的补充
        option.setIsNeedLocationDescribe(true);
        //设置是否需要返回位置POI信息，可以在BDLocation.getPoiList()中得到数据
        option.setIsNeedLocationPoiList(true);
        /**
         * 设置定位模式
         * Battery_Saving
         * 低功耗模式
         * Device_Sensors
         * 仅设备(Gps)模式
         * Hight_Accuracy
         * 高精度模式
         */
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        //注意：注释该行可以显示反地理编码位置
        //	option.setScanSpan(1000);//设置扫描间隔，单位是毫秒 当<1000(1s)时，定时定位无效
        locationService.setLocationOption(option);
        locationService.start();
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
    }

    private BDLocationListener mListener = new BDLocationListener(){

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {

                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                                // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100).latitude(location.getLatitude())
                        .longitude(location.getLongitude()).build();
                LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                // 反Geo搜索
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ll ));

                System.out.println("--------位置信息：经度" + ll.longitude + " 维度=" + ll.latitude);
                SharedInfoUtils.setLongitude(String.valueOf(ll.longitude));
                SharedInfoUtils.setLatitude(String.valueOf(ll.latitude));

               // System.out.println("+++++++++++"+SharedInfoUtils.getLongitude()+"+++++++"+SharedInfoUtils.getLatitude());
            }

        }
    };

    public static int getmMainTheadId() {
        return mMainTheadId;
    }

    public static Thread getmMainThead() {
        return mMainThead;
    }

    public static Looper getmMainThreadLooper() {
        return mMainThreadLooper;
    }

    public static Handler getmMainThreadHandler() {
        return mMainThreadHandler;
    }


    /**
     * @Description 添加activity
     * @param activity
     * @return void
     */
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * @Description 移除activity
     * @param activity
     * @return void
     */
    public void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    /**
     * @Description 退出所有activity
     * @return void
     */
    public void quitApp() {
        for (int i = 0; i < activities.size(); i++) {
            if (activities.get(i) != null) {
                activities.get(i).finish();
            }
        }

        try {
            ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.killBackgroundProcesses(getPackageManager()
                    .getPackageInfo(this.getPackageName(), 0).packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        int currentVersion = android.os.Build.VERSION.SDK_INT;
        if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            System.exit(0);
        } else {// android2.1
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            am.restartPackage(getPackageName());
        }
    }

}
