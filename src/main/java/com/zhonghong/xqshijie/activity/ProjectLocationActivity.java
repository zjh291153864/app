package com.zhonghong.xqshijie.activity;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.base.BaseActivity;
import com.zhonghong.xqshijie.util.BaiduSearchLocation;
import com.zhonghong.xqshijie.widget.TitleView;

/**
 * Created by zg on 2016/7/12.
 * 项目位置
 */
public class ProjectLocationActivity extends BaseActivity {
    //标题栏
    private TitleView mTitle;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private String mAddress;
    private String mCity;
    private BaiduSearchLocation mBaiduSearchLocation;
    Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mBaiduSearchLocation.getStringLocation(mCity, mAddress);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        basicHandler.sendEmptyMessage(0);
    }

    @Override
    public View initContentView() {

        mAddress = getIntent().getStringExtra("ADDRESS");
        mCity = getIntent().getStringExtra("CITY");
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_project_location, null);
        myHandler.sendEmptyMessage(0);
        mTitle = (TitleView) contentView.findViewById(R.id.title);
        mMapView = (MapView) contentView.findViewById(R.id.bmapView);
        mTitle.setLeftImageOnClickListener(this);
        mBaiduMap = mMapView.getMap();
        //定义Maker坐标点
        //LatLng point = new LatLng(39.963175, 116.400244);
        mBaiduSearchLocation = new BaiduSearchLocation(mBaiduMap, this);
        return contentView;
    }

    @Override
    public void handleCreate() {

    }

    @Override
    protected void customOnClick(View v) {
        switch (v.getId()) {
            case R.id.common_title_TV_left:
                finish();
                break;
            default:
                break;
        }
    }
}
