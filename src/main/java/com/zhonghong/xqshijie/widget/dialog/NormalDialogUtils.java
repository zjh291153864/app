package com.zhonghong.xqshijie.widget.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by ZG on 16/7/9.
 */
public class NormalDialogUtils {

    private NormalDialog dialog;
    private Context mContext;
    private BaseAnimatorSet bas_in;
    private BaseAnimatorSet bas_out;
    private String mContent;//提示内容
    private String mbtnLeftContent;//左边按钮显示内容

    public void setmBtnCount(int mBtnCount) {
        this.mBtnCount = mBtnCount;
    }

    public void setMbtnLeftContent(String mbtnLeftContent) {
        this.mbtnLeftContent = mbtnLeftContent;
    }

    public void setMbtnRightContent(String mbtnRightContent) {
        this.mbtnRightContent = mbtnRightContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    private String mbtnRightContent;//右边按钮显示内容
    private int mBtnCount;


    public NormalDialogUtils(Context mContext) {
        dialog = new NormalDialog(mContext);
        this.mContext = mContext;
        bas_in = new BounceTopEnter();
        bas_out = new SlideBottomExit();
    }

    public NormalDialogUtils() {
    }

    public void NormalDialogCustomAttr() {

        dialog.isTitleShow(false)//
                .bgColor(Color.parseColor("#383838"))//
                .cornerRadius(5)//
                .content(mContent)//
                .contentGravity(Gravity.CENTER)//
                .contentTextColor(Color.parseColor("#ffffff"))//
                .dividerColor(Color.parseColor("#222222"))//
                .btnTextSize(15.5f, 15.5f)//
                .btnTextColor(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"))//
                .btnPressColor(Color.parseColor("#2B2B2B"))//
                .btnText(mbtnLeftContent, mbtnRightContent)
                .widthScale(0.85f)//
//                .showAnim(bas_in)//
//                .dismissAnim(bas_out)//
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        Toast.makeText(mContext, "left", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        Toast.makeText(mContext, "right", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
    }

}
