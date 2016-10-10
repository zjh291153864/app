package com.zhonghong.xqshijie.activity;

import android.view.LayoutInflater;
import android.view.View;
import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.base.BaseActivity;
import com.zhonghong.xqshijie.widget.TitleView;

/**
 * Created by zg on 2016/6/30.
 * 关于我们界面
 */
public class AboutUsActivity extends BaseActivity {
    //标题栏
    private TitleView mTitle;
    @Override
    public View initContentView() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_about_us, null);
        mTitle = (TitleView) contentView.findViewById(R.id.title);
        return contentView;
    }

    @Override
    public void handleCreate() {

    }

    @Override
    protected void customOnClick(View v) {
        switch (v.getId()) {
            case R.id.ll_common_title_TV_left:
                finish();
                break;
            default:
                break;
        }
    }
}
