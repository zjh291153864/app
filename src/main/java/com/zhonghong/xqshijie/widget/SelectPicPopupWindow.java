package com.zhonghong.xqshijie.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhonghong.xqshijie.R;

/**
 * 上传头像弹窗
 * Created by jh on 2016/6/29.
 */
public class SelectPicPopupWindow extends PopupWindow implements PopupWindow.OnDismissListener{
    private Activity context=null;
    private TextView mTvPpwdTakePhoto;
    private TextView mTvPpwdPickPhoto;
    private TextView mTvPpwdCancel;
    private View mPopupWindow;

    public SelectPicPopupWindow(Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPopupWindow = inflater.inflate(R.layout.popupwindow_replace_avatar, null);
        mTvPpwdTakePhoto = (TextView) mPopupWindow.findViewById(R.id.tv_ppwd_take_photo);
        mTvPpwdPickPhoto = (TextView) mPopupWindow.findViewById(R.id.tv_ppwd_pick_photo);
        mTvPpwdCancel = (TextView) mPopupWindow.findViewById(R.id.tv_ppwd_cancel);
        //取消按钮
        mTvPpwdCancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });
        //设置按钮监听
        mTvPpwdPickPhoto.setOnClickListener(itemsOnClick);
        mTvPpwdTakePhoto.setOnClickListener(itemsOnClick);
        //设置SelectPicPopupWindow的View
        this.setContentView(mPopupWindow);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.take_photo_anim);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00ffffff);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mPopupWindow.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = mPopupWindow.findViewById(R.id.ll_ppwd_layout).getTop();
                int y=(int) event.getY();
                if(event.getAction()== MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });
        setOnDismissListener(this);
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
