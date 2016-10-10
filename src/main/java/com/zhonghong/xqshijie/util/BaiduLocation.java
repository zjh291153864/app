package com.zhonghong.xqshijie.util;

import android.location.LocationManager;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.zhonghong.xqshijie.app.Constants;
import com.zhonghong.xqshijie.app.MyApplication;

public class BaiduLocation implements BDLocationListener {

    private LocationClient mLocBaiduClient;
    private static BaiduLocation baiduLocation;
    private String latitude = "";
    private String longitude = "";
    private String address = "";
    private String coorTypr = "";

    public String getCoorTypr() {
        return coorTypr;
    }

    public void setCoorTypr(String coorTypr) {
        this.coorTypr = coorTypr;
    }

    public static BaiduLocation getInstance() {
        if (baiduLocation == null) {
            baiduLocation = new BaiduLocation();
        }
        return baiduLocation;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BaiduLocation() {
        mLocBaiduClient = MyApplication.getAppContext().mLocationClient;
    }

    /**
     * 启动定位
     *
     * @param coorTypr 坐标系
     *                 定位SDK可以返回bd09、bd09ll、gcj02三种类型坐标
     */
    public void startLocationListener(String coorTypr, BaiduLocationLister mBaiduLocationListener) {
        this.mBaiduLocationListener = mBaiduLocationListener;
        if (coorTypr == null) {
            coorTypr = "bd09ll";
        }
        this.coorTypr = coorTypr;
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps((boolean) (MyApplication.getAppContext().locaMgr.isProviderEnabled(LocationManager.GPS_PROVIDER))); // 打开gps
        option.setAddrType("all");//返回的定位结果包含地址信息
        option.setCoorType(coorTypr); // 设置坐标类型为bd09ll
        option.setPriority(LocationClientOption.GpsFirst); // 设置GPS优先
        option.setProdName("oncon"); // 设置产品线名称
        option.setScanSpan(5000); // 定时定位
        option.disableCache(true);// 1.//true表示禁用缓存定位，false表示启用缓存定位。
        mLocBaiduClient.setLocOption(option);
        mLocBaiduClient.registerLocationListener(this);
        mLocBaiduClient.start();
    }

    public void stopLocationListener() {
        if (mLocBaiduClient != null) {
            mLocBaiduClient.unRegisterLocationListener(this);
            if (mLocBaiduClient.isStarted()) {
                mLocBaiduClient.stop();
            }
        }
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        latitude = "";
        longitude = "";
        address = "";
        if (location == null) {
            Log.d(Constants.LOG_TAG, "定位失败原因location = null");
        } else if (location.getLocType() == BDLocation.TypeGpsLocation
                || location.getLocType() == BDLocation.TypeNetWorkLocation) {
            latitude = location.getLatitude() + "";
            longitude = location.getLongitude() + "";
            address = location.getAddrStr();
        } else {
            Log.d(Constants.LOG_TAG, "定位失败原因=" + location.getLocType());
        }
        if (mBaiduLocationListener != null) {
            mBaiduLocationListener.baiduLocFinish(latitude, longitude, address, coorTypr);
        }
        stopLocationListener();

    }

    private BaiduLocationLister mBaiduLocationListener;

    public interface BaiduLocationLister {
        public void baiduLocFinish(String latitude, String longitude, String address, String coorTypr);
    }

}
