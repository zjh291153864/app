package com.zhonghong.xqshijie.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.widget.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导页
 */
public class GuidePageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_page);
        initViews();
    }

    private int[] resIds = {R.drawable.guide_one, R.drawable.guide_two};


    protected void initViews() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        ArrayList<View> views = new ArrayList<View>();
        for (int i = 0; i < resIds.length; i++) {
            View view = getChildView(i, resIds[i]);
            views.add(view);
        }
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(views);
        viewPager.setAdapter(pagerAdapter);

        // 设置指示器
        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
    }

    /**
     * 添加View
     */
    private View getChildView(int index, int resId) {
        RelativeLayout view = new RelativeLayout(this);
        view.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT));
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageResource(resId);
        view.addView(imageView);
        // 最后一页添加Button
        if (index == resIds.length - 1) {
            addButton(view);
        }
        return view;
    }

    private void addButton(RelativeLayout view) {
        TextView btn = new TextView(this);
        btn.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
                .LayoutParams.WRAP_CONTENT));
        int leftPadding = (int) this.getResources().getDimension(R.dimen.guide_btn_padding_left);
        int topPadding = (int) this.getResources().getDimension(R.dimen.guide_btn_padding_top);
        btn.setPadding(leftPadding, topPadding, leftPadding, topPadding);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams
                .WRAP_CONTENT, RelativeLayout
                .LayoutParams.WRAP_CONTENT);
        params.bottomMargin = (int) this.getResources().getDimension(R.dimen.guide_btn_margin_bottom);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        btn.setBackgroundResource(R.drawable.common_btn_empty_tintblue);
        // 设置字体大小
        int textSize = getResources().getDimensionPixelSize(R.dimen.guide_btn_text_size);
        btn.setTextSize(textSize);
        // 设置字体颜色
        btn.setTextColor(getResources().getColor(R.color.ring_pie_chart_all));
        btn.setText("立即体验");

        view.addView(btn, params);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // SharedPrefUtil.putBoolean(SharedPrefUtil.isFirst, true);
                //TODO记录第一次应用
                startApp();
            }
        });
    }

    /**
     * 启动App
     */
    protected void startApp() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(intent);
        finish();
    }

    private class ViewPagerAdapter extends PagerAdapter {
        private List<View> views;

        public ViewPagerAdapter(List<View> views) {
            this.views = views;
        }

        // 销毁position位置的界面
        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(views.get(position));
        }

        @Override
        public int getCount() {
            if (views != null) {
                return views.size();
            }
            return 0;
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(views.get(position), 0);
            return views.get(position);
        }


        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return (arg0 == arg1);
        }
    }
}
