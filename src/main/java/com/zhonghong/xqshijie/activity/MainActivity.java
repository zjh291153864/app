package com.zhonghong.xqshijie.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.app.SharedPreferencesTag;
import com.zhonghong.xqshijie.appupdate.MessageBean;
import com.zhonghong.xqshijie.appupdate.NetStateChangeReceiver;
import com.zhonghong.xqshijie.appupdate.UpdateAppController;
import com.zhonghong.xqshijie.base.BaseActivity;
import com.zhonghong.xqshijie.controller.ChagePwdController;
import com.zhonghong.xqshijie.data.bean.MenuleftItemBean;
import com.zhonghong.xqshijie.data.response.LoginResponse;
import com.zhonghong.xqshijie.fragment.FragmentFactory;
import com.zhonghong.xqshijie.fragment.WebFragment;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.util.AppUtils;
import com.zhonghong.xqshijie.util.PermissionsChecker;
import com.zhonghong.xqshijie.util.PublicUtils;
import com.zhonghong.xqshijie.util.SharedPreferencesUtil;
import com.zhonghong.xqshijie.util.StringUtils;
import com.zhonghong.xqshijie.widget.dialog.NormalDialog;
import com.zhonghong.xqshijie.widget.dialog.OnBtnClickL;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private ListView lv;
    private QuickAdapter<MenuleftItemBean> quickAdapter;
    public FragmentManager mFragmentManager;
    private Fragment mCurrentFragment;
    private long exitTime = 0;
    public SlidingMenu mMenu;
    private NetStateChangeReceiver mNetStateChangeReceiver;
    private ChagePwdController mChagePwdController;
    private String mMumberId = "";
    private String mOriginalPwd = "";
    private static final int REQUEST_CODE = 3; // 权限请求码
    // 所需的权限
    private String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
   //是的我是
    @Override
    public View initContentView() {
        View inflate = View.inflate(MainActivity.this, R.layout.activity_main, null);
        return inflate;
    }

    @Override
    public void handleCreate() {
        mMumberId = SharedPreferencesUtil.getInstance(this).getStringValue(SharedPreferencesTag.MEMBER_ID);
        mOriginalPwd = SharedPreferencesUtil.getInstance(this).getStringValue(SharedPreferencesTag.MEMBER_PASSWORD);
        initSlidingmenu();
        mFragmentManager = getSupportFragmentManager();
        lv = (ListView) findViewById(R.id.lv);

        String tag = FragmentFactory.transTag(R.id.menu_home_page_v);
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            fragment = FragmentFactory.getInstanceByIndex(R.id.menu_home_page_v, getResources().getString(R.string.menu_home_page), MainActivity.this);
        }
        switchFragment(fragment, tag);

        lv.setAdapter(quickAdapter = new QuickAdapter<MenuleftItemBean>(this, R.layout.menu_frame_left_item, getItemBeans()) {
            @Override
            protected void convert(BaseAdapterHelper helper, MenuleftItemBean item) {
                helper.setImageResource(R.id.tv_menu_image, item.getImg()).setText(R.id.tv_menu_textview, item.getTitle());
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                MenuleftItemBean menuleftItemBean = (MenuleftItemBean) lv.getAdapter().getItem(position);
                if (menuleftItemBean != null) {
                    String tag = FragmentFactory.transTag(menuleftItemBean.getResourceId());
                    if (tag.equals("conn")) {
                        AppUtils.showCallDialog(mContext, "10086", mContext.getString(R.string.telaction_msg), mContext.getResources().getString(R.string.btn_ok_sting), mContext.getResources().getString(R.string.btn_cancel_sting));
                    } else {
                        mMenu.toggle();
                        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
                        if (fragment == null) {
                            fragment = FragmentFactory.getInstanceByIndex(menuleftItemBean.getResourceId(), menuleftItemBean.getTitle(), MainActivity.this);
                        }
                        switchFragment(fragment, tag);
                    }
                }

            }
        });
        //注册监听网络改变的广播
        mNetStateChangeReceiver = new NetStateChangeReceiver();
        UpdateAppController.registerNetStateReceiver(this, mNetStateChangeReceiver);
        //开启服务去检查软件更新
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UpdateAppController.startUpdateService(MainActivity.this);
            }
        }, 10000);
        //判断用户原密码是否正确
        if (!StringUtils.isNull(mMumberId) && !StringUtils.isNull(mOriginalPwd)) {
            verificationOriginalPwd();
        }
        // 缺少权限时, 进入权限配置页面
        if (PermissionsChecker.lacksPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_CODE);
        }
    }

    private void initSlidingmenu() {
        mMenu = new SlidingMenu(this);
        mMenu.setMode(SlidingMenu.LEFT);
        mMenu.setTouchModeAbove(SlidingMenu.LEFT);
        mMenu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
        mMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        mMenu.setFadeDegree(0.35f);
        mMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        mMenu.setMenu(R.layout.menu_frame_left);
        initSlidingAnim(mMenu);
    }

    @Override
    protected void customOnClick(View v) {
        switch (v.getId()) {
            case R.id.common_title_BigIV_left:
                mMenu.toggle();
                break;
            case R.id.ll_common_title_TV_left:
                if (mCurrentFragment instanceof WebFragment) {
                    WebFragment webFragment = (WebFragment) mCurrentFragment;
                    if (webFragment != null) {
                        webFragment.onBackPressed();
                    }
                } else {
                    mMenu.toggle();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void processMessage(Message msg) {
        super.processMessage(msg);
        switch (msg.what) {
            case EXIT_OK:
                finish();
                break;
        }
    }


    private void initSlidingAnim(SlidingMenu menu) {

        SlidingMenu.CanvasTransformer sldanm = new SlidingMenu.CanvasTransformer() {
            @Override
            public void transformCanvas(Canvas canvas, float percentOpen) {
                float scale = (float) (percentOpen * 0.25 + 0.75);
//                canvas.drawColor(MainActivity.this.getResources().getColor(R.color.green));
                canvas.scale(scale, scale, canvas.getWidth() / 2, canvas.getHeight() / 2);
            }
        };
        menu.setBehindCanvasTransformer(sldanm);
    }

    public void switchFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (fragment != null) {
            if (mCurrentFragment != null && fragment != mCurrentFragment) {
                if (!fragment.isAdded()) {
                    transaction.hide(mCurrentFragment).add(R.id.fragment_content, fragment, tag).commit();
                } else {
                    transaction.hide(mCurrentFragment).show(fragment).commit();
                }
            } else {
                if (!fragment.isAdded()) {
                    transaction.add(R.id.fragment_content, fragment, tag).commit();
                }
            }

            if (null != mCurrentFragment) {
                mCurrentFragment.setUserVisibleHint(false);
            }

            fragment.setUserVisibleHint(true);
            mCurrentFragment = fragment;
        }
    }


    private List<MenuleftItemBean> getItemBeans() {
        List<MenuleftItemBean> itemBeans = new ArrayList<>();
        itemBeans.add(new MenuleftItemBean(R.id.menu_home_page_v, R.drawable.ic_menu_homepage, getResources().getString(R.string.menu_home_page), false));
        itemBeans.add(new MenuleftItemBean(R.id.menu_scenic_v, R.drawable.ic_menu_scenic, getResources().getString(R.string.menu_scenic), false));
        itemBeans.add(new MenuleftItemBean(R.id.menu_ylt_v, R.drawable.ic_menu_ylt, getResources().getString(R.string.menu_ylt), false));
        itemBeans.add(new MenuleftItemBean(R.id.menu_member_center_v, R.drawable.ic_menu_membercenter, getResources().getString(R.string.menu_member_center), false));
        itemBeans.add(new MenuleftItemBean(R.id.menu_newcomer_v, R.drawable.ic_menu_newcomer, getResources().getString(R.string.menu_newcomer), false));
        itemBeans.add(new MenuleftItemBean(R.id.menu_conn_v, R.drawable.ic_menu_tel, getResources().getString(R.string.menu_conn), false));
        return itemBeans;
    }

    //    验证旧密码
    private void verificationOriginalPwd() {
        if (mChagePwdController == null) {
            mChagePwdController = new ChagePwdController(this);
        }
        mChagePwdController.handleOriginalPwdByNet(new NetInterface() {
            @Override
            public void onNetResult(String interfaceAction, Object result) {
                LoginResponse loginResponse = (LoginResponse) result;
                if (loginResponse.mResult.equals("02")) {//原密码错误
                    final NormalDialog normalDialog = new NormalDialog(MainActivity.this);
                    normalDialog.isTitleShow(false)
                            .bgColor(Color.parseColor("#FFFFFF"))
                            .cornerRadius(5)
                            .contentGravity(Gravity.CENTER)
                            .contentTextColor(Color.parseColor("#2A2A2A"))
                            .content("密码已修改，是否重新登陆？")
                            .btnTextColor(Color.parseColor("#2A2A2A"), Color.parseColor("#2A2A2A"))
                            .show();
                    normalDialog.setOnBtnClickL(new OnBtnClickL() {
                        @Override
                        public void onBtnClick() {
                            normalDialog.dismiss();
                            PublicUtils.removeUserInformation(MainActivity.this);//删除用户信息
                        }
                    }, new OnBtnClickL() {
                        @Override
                        public void onBtnClick() {
                            normalDialog.dismiss();
                            PublicUtils.removeUserInformation(MainActivity.this);
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);//删除用户信息
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onNetFinished(String interfaceAction) {
            }
        }, mMumberId, mOriginalPwd);
    }

    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (PermissionsChecker.lacksPermissions(this, PERMISSIONS)) {
                final NormalDialog normalDialog = new NormalDialog(MainActivity.this);
                normalDialog.isTitleShow(false).bgColor(Color.parseColor("#FFFFFF"))
                        .cornerRadius(5)
                        .contentGravity(Gravity.CENTER)
                        .contentTextColor(Color.parseColor("#2A2A2A"))
                        .content("当前应用缺少必要的权限。\n" + "请点击'确定'-'权限'打开所需的权限。")
                        .btnTextColor(Color.parseColor("#2A2A2A"), Color.parseColor("#2A2A2A"))
                        .show();
                normalDialog.setOnBtnClickL(new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        normalDialog.dismiss();
                    }
                }, new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        normalDialog.dismiss();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                    }
                });
            }
        }
    }

    @Subscribe
    public void onEventMainThread(MessageBean event) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解注册网络状态监听广播
        UpdateAppController.unRegistrNetStateReceiver(this, mNetStateChangeReceiver);
    }


    @Override
    public void onBackPressed() {
        if (mCurrentFragment instanceof WebFragment) {
            WebFragment webFragment = (WebFragment) mCurrentFragment;
            if (webFragment != null) {
                webFragment.onBackPressed();
            }
        } else {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), R.string.on_down_to_exit,
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                basicHandler.sendEmptyMessage(EXIT_OK);
            }
        }
    }

}
