package com.zhonghong.xqshijie.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.util.SharedPreferencesUtil;

/**
 * Created by zg on 2016/7/7.
 * 启动页
 */
public class LoadingActivity extends Activity {
    private static final long delayTime = 1000;
    private boolean isFirst;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFirst = SharedPreferencesUtil.getInstance(this).getBooleanValue("isFirst");
        ImageView view = new ImageView(this);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams
                .MATCH_PARENT));
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        view.setImageResource(R.drawable.loading_splash);
        setContentView(view);
        delayGoToPage();
        //统计启动
    }

    private void delayGoToPage() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFirst) {
                    Intent intent = new Intent(LoadingActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    SharedPreferencesUtil.getInstance(LoadingActivity.this).putBooleanValue("isFirst",true);
                    Intent intent = new Intent(LoadingActivity.this,GuidePageActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, delayTime);
    }
}