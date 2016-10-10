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
import com.google.gson.Gson;
import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.data.response.AddressJsonResponse;
import com.zhonghong.xqshijie.data.response.AddressJsonResponse.RegionsBean.ProvincesBean;
import com.zhonghong.xqshijie.data.response.AddressJsonResponse.RegionsBean.ProvincesBean.CitysBean;
import com.zhonghong.xqshijie.data.response.AddressJsonResponse.RegionsBean.ProvincesBean.CitysBean.DistrictsBean;
import com.zhonghong.xqshijie.util.AssetsUtils;
import com.zhonghong.xqshijie.widget.wheel.OnWheelChangedListener;
import com.zhonghong.xqshijie.widget.wheel.WheelView;
import com.zhonghong.xqshijie.widget.wheel.adapters.ArrayWheelAdapter;

import java.util.List;

/**
 * 选择收货地址弹窗
 * Created by jh on 2016/7/4.
 */
public class MailingAddressPopupWindow extends PopupWindow implements OnWheelChangedListener,View.OnClickListener,PopupWindow.OnDismissListener {
    private final TextView mTvCancel;
    private final TextView mTvDetermine;
    private final WheelView mIdProvince;
    private final WheelView mIdCity;
    private final WheelView mIdDistrict;
    private final Activity mContext;
    private View mPopupWindow;
    private final popupCallback mCallback;

    private String mAddressJson;
    private AddressJsonResponse mAddressJsonResponse;

    private List<ProvincesBean> mProvincesList;
    private List<CitysBean> mCitysList;
    private List<DistrictsBean> mDistrictsList;

    private ProvincesBean mCurrentProvice;
    private CitysBean mCurrentCity;
    private DistrictsBean mCurrentDistrict;
    private String[] mDatas={""};
    //     所有省
    protected String[] mProvinceDatas;

    public MailingAddressPopupWindow(Activity context, final popupCallback mCallback) {
        super(context);
        this.mContext = context;
        this.mCallback = mCallback;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPopupWindow = inflater.inflate(R.layout.popupwindow_mailing_address, null);
        mTvCancel = (TextView) mPopupWindow.findViewById(R.id.tv_cancel);
        mTvDetermine = (TextView) mPopupWindow.findViewById(R.id.tv_determine);
        mIdProvince = (WheelView) mPopupWindow.findViewById(R.id.id_province);
        mIdCity = (WheelView) mPopupWindow.findViewById(R.id.id_city);
        mIdDistrict = (WheelView) mPopupWindow.findViewById(R.id.id_district);
        setPopupWindows();
        initProvinceDatas();

        //按钮监听
        mTvDetermine.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);
        //WheelView选择监听
        mIdProvince.addChangingListener(this);
        mIdCity.addChangingListener(this);
        mIdDistrict.addChangingListener(this);
        setUpData();
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
        setOnDismissListener(this);
    }

    private void setUpData() {
        mIdProvince.setViewAdapter(new ArrayWheelAdapter<String>(mContext, mProvinceDatas));
        // 设置可见条目数量
        mIdProvince.setVisibleItems(5);
        mIdCity.setVisibleItems(5);
        mIdDistrict.setVisibleItems(5);
        updateCitys();
        updateDistricts();
    }


    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateDistricts() {
            int pCurrent = mIdCity.getCurrentItem();
            mCurrentCity=mCitysList.get(pCurrent);
            mDistrictsList =mCurrentCity.mDistricts;
            if(mDistrictsList!=null&&!mDistrictsList.isEmpty()){
                String[] mDistrictDatas = new String[mDistrictsList.size()];
                for (int i = 0; i < mDistrictDatas.length; i++) {
                    mDistrictDatas[i]= mDistrictsList.get(i).mName;
                }
                mIdDistrict.setViewAdapter(new ArrayWheelAdapter<String>(mContext, mDistrictDatas));
                mIdDistrict.setCurrentItem(0);
                mCurrentDistrict=mDistrictsList.get(0);
            }else{
                mIdDistrict.setViewAdapter(new ArrayWheelAdapter<String>(mContext, mDatas));
                mCurrentDistrict=null;
            }
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCitys() {
        int pCurrent = mIdProvince.getCurrentItem();
        mCurrentProvice= mProvincesList.get(pCurrent);
        mCitysList =mCurrentProvice.mCitys;
        if(mCitysList!=null&& !mCitysList.isEmpty()){
            String[] mCityDatas = new String[mCitysList.size()];
            for (int i = 0; i < mCitysList.size(); i++) {
                mCityDatas[i]= mCitysList.get(i).mName;
            }
            mIdCity.setViewAdapter(new ArrayWheelAdapter<String>(mContext, mCityDatas));
            mIdCity.setCurrentItem(0);
            updateDistricts();
        }else{
            mIdCity.setViewAdapter(new ArrayWheelAdapter<String>(mContext, mDatas));
            mIdDistrict.setViewAdapter(new ArrayWheelAdapter<String>(mContext, mDatas));
            mCurrentCity=null;
            mCurrentDistrict=null;
        }
    }

//    按钮监听
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        		case R.id.tv_cancel:
                    //销毁弹出框
                    dismiss();
        			break;
        		case R.id.tv_determine:
                    mCallback.dosomething(mCurrentProvice,mCurrentCity,mCurrentDistrict);
                    dismiss();
        			break;
        		default:
        			break;
        		}
    }

//    WheelView滑动监听
    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        switch (wheel.getId()) {
            case R.id.id_province:
                updateCitys();
                break;
            case R.id.id_city:
                updateDistricts();
                break;
            case R.id.id_district:
                if(mDistrictsList!=null&&!mDistrictsList.isEmpty()){
                    mCurrentDistrict= mDistrictsList.get(newValue);
                }else{
                    mCurrentDistrict=null;
                }
                break;
            default:
                break;
        }
    }
//    解析Json文件
    public void initProvinceDatas(){
        mAddressJson= AssetsUtils.getFromAssets(mContext,"sys_region.json");
        Gson gson=new Gson();
        mAddressJsonResponse =gson.fromJson(mAddressJson, AddressJsonResponse.class);
        mProvincesList = mAddressJsonResponse.mRegions.mProvinces;
        //*/ 初始化默认选中的省、市、区
        if (mProvincesList != null && !mProvincesList.isEmpty()) {
            mCurrentProvice = mProvincesList.get(0);
            mCitysList = mProvincesList.get(0).mCitys;
            if (mCitysList != null && !mCitysList.isEmpty()) {
                mCurrentCity = mCitysList.get(0);
                mDistrictsList = mCitysList.get(0).mDistricts;
                mCurrentDistrict = mDistrictsList.get(0);
            }
        }
        mProvinceDatas = new String[mProvincesList.size()];
        for (int i = 0; i < mProvincesList.size(); i++) {
            // 遍历所有省的数据
            mProvinceDatas[i] = mProvincesList.get(i).mName;
        }
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

    public interface popupCallback {
        void dosomething(ProvincesBean province, CitysBean city, DistrictsBean district);
    }
}
