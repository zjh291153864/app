package com.zhonghong.xqshijie.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.data.bean.ProductPhotoBean;
import com.zhonghong.xqshijie.data.bean.ProductPhotoMappingBean;
import com.zhonghong.xqshijie.util.PublicUtils;
import com.zhonghong.xqshijie.util.ZoomOutPagerTransformer;
import com.zhonghong.xqshijie.widget.TitleView;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by xiezl on 16/6/14.
 */
public class PhotoDetailFragment extends Fragment {

    private PublicUtils.photoCallBack mCallBack = null;
    private ViewPager mVpPhotoViewpager;
    private ArrayList<ProductPhotoMappingBean> photoMappingList = null;//细分数据
    private TextView mTvBottomWord;
    private ArrayList<ProductPhotoBean> mTotalSortList = null;//总数据
    private TitleView mTitle;
    private List<RadioButton> mOutisdeButtons = null;
    private String mLasttype = null;
    private ImageView mIvNetError;//错误图片
    private RelativeLayout mRlDownPage;
    private View mContentView;
    private DetailPagerAdapter mAdapter;

    public PublicUtils.photoCallBack getmCallBack() {
        return mCallBack;
    }

    public void setmCallBack(PublicUtils.photoCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    /**
     * 切换到某一页 用于和外部tab联动
     */
    public void swichPages(int pageno, boolean anim) {
        mVpPhotoViewpager.setCurrentItem(pageno, anim);
        mAdapter.notifyDataSetChanged();
    }

    //为相册设置数据
    public void bindData(ArrayList<ProductPhotoBean> mTotalSortList, ArrayList<ProductPhotoMappingBean> photoMappingList, List<RadioButton> outisdeButtons) {
        this.mTotalSortList = mTotalSortList;
        this.photoMappingList = photoMappingList;
        this.mOutisdeButtons = outisdeButtons;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_photodetail, null);
        mIvNetError = (ImageView) mContentView.findViewById(R.id.iv_photo_neterror);
        mRlDownPage = (RelativeLayout) mContentView.findViewById(R.id.rl_photo_downpage);
        mTvBottomWord = (TextView) mContentView.findViewById(R.id.photo_any_infos);
        mTitle = (TitleView) mContentView.findViewById(R.id.detail_title);
        mVpPhotoViewpager = (ViewPager) mContentView.findViewById(R.id.vp_photocontain);
        mTitle.setTitleLineColor(getResources().getColor(R.color.black));
        mTitle.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
        mTitle.setTitle(1 + "/" + (mTotalSortList.size()));//初始化顶部文字
        if (mTotalSortList == null && mTotalSortList.size() > 0) {
            mTvBottomWord.setText(mTotalSortList.get(0).getmDecreption() + mTotalSortList.get(0).getmPictruTilte() + PublicUtils.getPhotoNameByIndex(Integer.parseInt(mTotalSortList.get(0).getmPictureType())));//初始化底部文字
        }

        //外部界面和内部界面跳转的回调
        mTitle.setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.switchToFinish();
            }
        });
        mTitle.setRightImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.switchPageTwo();
            }
        });
        mTitle.setTvCenterTextColor(getResources().getColor(R.color.white));

        mAdapter = new DetailPagerAdapter();

        if (mTotalSortList != null && mTotalSortList.size() > 0) {
            mAdapter.setDatas(mTotalSortList);
        }

        mVpPhotoViewpager.setAdapter(mAdapter);
        mVpPhotoViewpager.setPageTransformer(true, new ZoomOutPagerTransformer());
        mVpPhotoViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTitle.setTitle((position + 1) + "/" + (mTotalSortList.size()));
                ProductPhotoBean currBean = mTotalSortList.get(position);
                String description = currBean.getmDecreption();
                String pcTitle = currBean.getmPictruTilte();
                String picType = PublicUtils.getPhotoNameByIndex(Integer.parseInt(currBean.getmPictureType()));
                mTvBottomWord.setText(description + " " + pcTitle + " " + picType);

                String currItemType = mTotalSortList.get(position).getmPictureType();
                if (mLasttype != null && !mLasttype.equals(currItemType)) {//只要上次的类型记录和当前的不一样就意味着切换，切换就需要联动
                    mOutisdeButtons.get(Integer.parseInt(currItemType) - 1).setChecked(true);
                }
                mLasttype = currItemType;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return mContentView;
    }

    class DetailPagerAdapter extends PagerAdapter {
        private List<ProductPhotoBean> mDatasorces = null;

        public void setDatas(List<ProductPhotoBean> datasorces) {
            mDatasorces = datasorces;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewGroup) container).removeView((View) object);
            object = null;
        }

        @Override
        public int getCount() {
            if (mTotalSortList != null) {
                return mTotalSortList.size();
            }
            return 0;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView pView = new PhotoView(getActivity());
            pView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            ProductPhotoBean currProductPhotoBean = mDatasorces.get(position);
            String picUrls = currProductPhotoBean.getmPictruUrl();//图片网址
            String picTypes = currProductPhotoBean.getmPictureType();//图片类型
            picTypes = PublicUtils.getPhotoNameByIndex(Integer.parseInt(picTypes));
            int picssize = -1, picindex = -1;

            for (int i = 0; i < photoMappingList.size(); i++) {
                if (photoMappingList.get(i).getType().equals(currProductPhotoBean.getmPictureType())) {
                    picssize = photoMappingList.get(i).getData().size();
                    picindex = photoMappingList.get(i).getData().indexOf(currProductPhotoBean);
                }
            }

            Glide.with(getActivity()).load(picUrls).placeholder(R.drawable.image_onloading_homebig).error(R.drawable.image_onloading_homebig)./*crossFade().*/placeholder(R.drawable.image_onloading_homebig).into(pView);//加载图片
            container.addView(pView);
            return pView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
