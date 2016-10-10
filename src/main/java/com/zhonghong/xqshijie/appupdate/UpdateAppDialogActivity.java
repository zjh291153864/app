package com.zhonghong.xqshijie.appupdate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.zhonghong.xqshijie.R;


public class UpdateAppDialogActivity extends Activity implements View.OnClickListener {
    private Button btn_ok;
    private Button btn_cancel;
    private TextView tv_content;
    private View view_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.requestWindowFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        setContentView(R.layout.activity_update_app_dialog);
        setFinishOnTouchOutside(false);
        btn_cancel = (Button) findViewById(R.id.btn_update_app_cancel);
        btn_ok = (Button) findViewById(R.id.btn_update_app_ok);
        tv_content = (TextView) findViewById(R.id.tv_update_app_content);
        view_update = findViewById(R.id.view_update);
        btn_cancel.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        Intent intent = getIntent();
        boolean force = intent.getBooleanExtra(UpdateConstants.ACTIVITY_FLAG, false);
        String content = intent.getStringExtra(UpdateConstants.ACTIVITY_CONTENT);
        if (force) {
            btn_cancel.setVisibility(View.GONE);
            view_update.setVisibility(View.GONE);
        }
        if (content != null) {
            tv_content.setText(content);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update_app_cancel:
                UpdateAppController.stopUpdateService(UpdateAppDialogActivity.this);
                finish();
                break;
            case R.id.btn_update_app_ok:
                //让服务去更新
                UpdateAppController.startUpdateServiceForDown(this, UpdateConstants.ACTIVITY_FLAG, true);
                finish();
                break;
        }
    }
}
