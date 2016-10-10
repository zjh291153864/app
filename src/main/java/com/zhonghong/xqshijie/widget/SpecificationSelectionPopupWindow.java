package com.zhonghong.xqshijie.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.activity.ConfirmOrderActivity;
import com.zhonghong.xqshijie.adapter.SpecificationSelectionAdapter;
import com.zhonghong.xqshijie.controller.SpecificationSelectionController;
import com.zhonghong.xqshijie.data.response.SpecificationSelectionResponse;
import com.zhonghong.xqshijie.data.response.SpecificationSelectionResponse.HousesBean;
import com.zhonghong.xqshijie.interfaces.NetInterface;
import com.zhonghong.xqshijie.util.ImageLoaderUtil;

/**
 * 立即购买弹窗
 * Created by jh on 2016/6/29.
 */
public class SpecificationSelectionPopupWindow extends PopupWindow implements View.OnClickListener,AdapterView.OnItemClickListener,PopupWindow.OnDismissListener{
    private Activity mContext;
    private View mPopupWindow;
    private ImageView mIvClose;//关闭按钮
    private ImageView mIvProjectCover;//项目图片
    private TextView mTvProjectName;//项目名称
    private TextView mTvHousePrice;//产品价格
    private TextView mTvHouseName;//产品名称
    private TextView mTvHouseArea;//产品面积
    private TextView mTvRemind;//定金提醒
    private GridView mGvHouseList;//产品列表
    private Button mBtnDetermine;//确定
    private String mId;//项目ID
    private SpecificationSelectionAdapter specificationSelectionAdapter;//适配器
    private SpecificationSelectionResponse specificationSelectionResponse;//获取回来的数据
    private HousesBean mHouse;//点击的产品

    public SpecificationSelectionPopupWindow(Activity context,String id) {
        super(context);
        this.mContext=context;
        this.mId=id;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPopupWindow = inflater.inflate(R.layout.popupwindow_specification_selection, null);

        mIvClose = (ImageView) mPopupWindow.findViewById(R.id.iv_specification_selection_close);
        mIvProjectCover = (ImageView) mPopupWindow.findViewById(R.id.iv_specification_selection_project_cover);
        mTvProjectName = (TextView) mPopupWindow.findViewById(R.id.tv_specification_selection_project_name);
        mTvHousePrice = (TextView) mPopupWindow.findViewById(R.id.tv_specification_selection＿house_price);
        mTvHouseName = (TextView) mPopupWindow.findViewById(R.id.tv_specification_selection_house_name);
        mTvRemind = (TextView) mPopupWindow.findViewById(R.id.tv_specification_selection_remind);
        mTvHouseArea = (TextView) mPopupWindow.findViewById(R.id.tv_specification_selection_house_area);
        mGvHouseList = (GridView) mPopupWindow.findViewById(R.id.gv_specification_selection_houselist);
        mBtnDetermine = (Button) mPopupWindow.findViewById(R.id.btn_specification_selection_determine);
        setPopupWindows();
        getByProjectIdData();

        mBtnDetermine.setOnClickListener(this);
        mIvClose.setOnClickListener(this);
        mGvHouseList.setOnItemClickListener(this);
        setOnDismissListener(this);
    }

    //    PopupWindow相关设置
    private void setPopupWindows(){
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
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    private void getByProjectIdData(){
        SpecificationSelectionController specificationSelectionController=new SpecificationSelectionController(mContext);
        specificationSelectionController.handleGetByProjectIdByNet(new NetInterface() {
            @Override
            public void onNetResult(String interfaceAction, Object result) {
                specificationSelectionResponse= (SpecificationSelectionResponse) result;
                specificationSelectionAdapter=new SpecificationSelectionAdapter(mContext,specificationSelectionResponse.mHouses);
                mGvHouseList.setAdapter(specificationSelectionAdapter);
                setView(0);
                ImageLoaderUtil.getInstance().loadImage(specificationSelectionResponse.mProjectCover,mIvProjectCover,R.drawable.ic_ylt_productdefault);
                mTvProjectName.setText(specificationSelectionResponse.mProjectName);
            }
            @Override
            public void onNetError(String interfaceAction, Throwable ex, boolean isOnCallback) {
            }
            @Override
            public void onNetFinished(String interfaceAction) {
            }
        },mId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        		case R.id.iv_specification_selection_close:
        			dismiss();
        			break;
        		case R.id.btn_specification_selection_determine:
                    if(specificationSelectionResponse!=null){
                        Intent intent=new Intent(mContext, ConfirmOrderActivity.class);
                        intent.putExtra("house",mHouse);
                        intent.putExtra("projectCover",specificationSelectionResponse.mProjectCover);
                        intent.putExtra("projectName",specificationSelectionResponse.mProjectName);
                        mContext.startActivity(intent);
                    }
                    dismiss();
        			break;
        		default:
        			break;
        		}
    }

//    GridView的Item监听
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        setView(position);
    }

    public void setView(int i){
        specificationSelectionAdapter.setSeclection(i);
        specificationSelectionAdapter.notifyDataSetChanged();
        mHouse=specificationSelectionResponse.mHouses.get(i);
        if(mHouse.mHasCert.equals("1")){//有预售证
            mTvRemind.setVisibility(View.GONE);
            SpannableString spanString = new SpannableString(mContext.getResources().getString(R.string.money)+mHouse.mHousePrice);
            AbsoluteSizeSpan span = new AbsoluteSizeSpan(33);
            spanString.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mTvHousePrice.setText(spanString);
        }else if(mHouse.mHasCert.equals("0")){//无预售证只可付定金
            mTvRemind.setVisibility(View.VISIBLE);
            SpannableString spanString = new SpannableString(mContext.getResources().getString(R.string.earnest)+mHouse.mHousePrice);
            AbsoluteSizeSpan span = new AbsoluteSizeSpan(33);
            spanString.setSpan(span, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mTvHousePrice.setText(spanString);
        }
        mTvHouseName.setText(mHouse.mHouseName);
        mTvHouseArea.setText(mHouse.mHouseArea+mContext.getResources().getString(R.string.square));
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        mContext.getWindow().setAttributes(lp);
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
