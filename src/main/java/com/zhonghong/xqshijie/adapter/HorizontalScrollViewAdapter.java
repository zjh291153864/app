package com.zhonghong.xqshijie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.base.BaseActivity;
import com.zhonghong.xqshijie.data.response.HomePageResponse;
import com.zhonghong.xqshijie.util.DensityUtil;
import com.zhonghong.xqshijie.util.ImageLoaderUtil;

import java.util.List;

public class HorizontalScrollViewAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<HomePageResponse.ObjectBean.AddresssBean> mDatas;
    public void setmDatas(List<HomePageResponse.ObjectBean.AddresssBean> mDatas) {
        this.mDatas = mDatas;
    }

    public HorizontalScrollViewAdapter(Context context, List<HomePageResponse.ObjectBean.AddresssBean> mDatas) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    public int getCount() {
        return mDatas.size();
    }

    public Object getItem(int position) {
        return mDatas.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(
                    R.layout.adapter_addrhorizontalscroll_item, parent, false);
            RelativeLayout relativeLayout = (RelativeLayout) convertView.findViewById(R.id.rl_address_item);
            ViewGroup.LayoutParams params=new ViewGroup.LayoutParams((((BaseActivity)mContext).screenWidth-DensityUtil.dip2px(mContext,24))/3,ViewGroup.LayoutParams.WRAP_CONTENT);
            relativeLayout.setLayoutParams(params);
            viewHolder.mImg = (ImageView) convertView
                    .findViewById(R.id.id_index_gallery_item_image);
            viewHolder.mText = (TextView) convertView
                    .findViewById(R.id.id_index_gallery_item_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position < mDatas.size()) {
            HomePageResponse.ObjectBean.AddresssBean item = mDatas.get(position);
            if (item != null) {
                ImageLoaderUtil.getInstance().loadImageByRoundedImage(item.mImageUrl, viewHolder.mImg, R.drawable.ic_ylt_productdefault);
                viewHolder.mText.setText(item.mTitle);
            }
        }

        return convertView;
    }

    private class ViewHolder {
        ImageView mImg;
        TextView mText;
    }

}
