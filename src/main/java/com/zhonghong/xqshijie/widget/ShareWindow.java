package com.zhonghong.xqshijie.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.util.PublicUtils;

import share.com.libshare.ShareHelper;

public class ShareWindow extends PopupWindow implements PopupWindow.OnDismissListener {

    private Button btn_take_photo, btn_pick_photo, btn_cancel;
    private View mMenuView;
    private Activity context = null;

    public ShareWindow(Activity context) {
        super(context);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popupwindow_share, null);
        initTabs();

        // 设置按钮监听
        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.FILL_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        ColorDrawable dw = new ColorDrawable(Color.parseColor("#00ffffff"));
        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(R.id.pop_father_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

        setOnDismissListener(this);
        mMenuView.findViewById(R.id.btn_share_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    /**
     * 分享按钮
     */
    private void initTabs() {
        ImageTextTab imbQqShare = (ImageTextTab) mMenuView.findViewById(R.id.sh_qq);
        ImageTextTab imbWxShare = (ImageTextTab) mMenuView.findViewById(R.id.sh_wx);
        ImageTextTab imbPyqShare = (ImageTextTab) mMenuView.findViewById(R.id.sh_pyq);
//        ImageTextTab imbSinaShare = (ImageTextTab) mMenuView.findViewById(R.id.sh_sina);
        ImageTextTab imbQqzoneShare = (ImageTextTab) mMenuView.findViewById(R.id.sh_qqzone);
        imbQqShare.setImageResId(R.drawable.share_qq, R.drawable.share_qq);
        imbWxShare.setImageResId(R.drawable.share_wx, R.drawable.share_wx);
        imbPyqShare.setImageResId(R.drawable.share_pyq, R.drawable.share_pyq);
//        imbSinaShare.setImageResId(R.drawable.share_sina, R.drawable.share_sina);
        imbQqzoneShare.setImageResId(R.drawable.share_qzon, R.drawable.share_qzon);
        imbQqShare.setTexts("QQ", 12, R.color.text_color);
        imbWxShare.setTexts("微信", 10, R.color.text_color);
        imbPyqShare.setTexts("朋友圈", 10, R.color.text_color);
//        imbSinaShare.setTexts("新浪微博", 10, R.color.text_color);
        imbQqzoneShare.setTexts("QQ空间", 10, R.color.text_color);

        imbQqShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PublicUtils.isFastDoubleClick()) {
                    ShareHelper.sonMethod_share(context, ShareHelper.QQtype, "qq", "http://www.umeng.com/images/pic/social/integrated_3.png", "https://www.baidu.com/", "qq title");
                }
            }
        });

        imbWxShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PublicUtils.isFastDoubleClick()) {
                    ShareHelper.sonMethod_share(context, ShareHelper.WEIXINtype, "wx", "http://www.umeng.com/images/pic/social/integrated_3.png", "https://www.baidu.com", "wx title");
                }
            }
        });

        imbPyqShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PublicUtils.isFastDoubleClick()) {
                    ShareHelper.sonMethod_share(context, ShareHelper.WEIXINCIRCLEtype, "朋友圈", "http://www.umeng.com/images/pic/social/integrated_3.png", "https://www.baidu.com/", "qq title");
                }
            }
        });

//        imbSinaShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ShareHelper.sonMethod_share(context, ShareHelper.SINAtype, "wb", "http://www.umeng.com/images/pic/social/integrated_3.png", "https://www.baidu.com/", "sina title");
//            }
//        });

        imbQqzoneShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PublicUtils.isFastDoubleClick()) {
                    ShareHelper.sonMethod_share(context, ShareHelper.QZoneType, "qzone", "http://www.umeng.com/images/pic/social/integrated_3.png", "https://www.baidu.com", "qzone title");
                }
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        context.getWindow().setAttributes(lp);
    }

    public void alertPopupwindow(View outsiderootview) {
        backgroundAlpha(0.5f);

        showAtLocation(
                outsiderootview,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置

    }

    @Override
    public void onDismiss() {
        backgroundAlpha(1f);
    }
}
