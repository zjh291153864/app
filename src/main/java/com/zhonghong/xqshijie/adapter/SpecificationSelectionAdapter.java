package com.zhonghong.xqshijie.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.data.response.SpecificationSelectionResponse.HousesBean;

import java.util.List;

/**
 * Created by jh on 2016/7/7.
 */
public class SpecificationSelectionAdapter extends BaseAdapter {
    private Context mContext;
    List<HousesBean> mHousesList;
    private int clickTemp = -1;

    public void setSeclection(int position) {
        clickTemp = position;
    }

    public SpecificationSelectionAdapter(Context mContext, List<HousesBean> mHousesList) {
        this.mContext = mContext;
        this.mHousesList = mHousesList;
    }

    @Override
    public int getCount() {
        return mHousesList.size();
    }

    @Override
    public Object getItem(int position) {
        return mHousesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HousesBean mHouse = mHousesList.get(position);
        ViewHolder viewHolder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.adapter_specification_selection_item,null);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.mAdapterSpecificationSelectionItemApartment.setText(mHouse.mHouseName);
        viewHolder.mAdapterSpecificationSelectionItemArea.setText(mHouse.mHouseArea+mContext.getResources().getString(R.string.square));
        if(clickTemp==position){
            viewHolder.mAdapterSpecificationSelectionLayout.setBackgroundResource(R.drawable.specification_selection_item_bg);
            viewHolder.mAdapterSpecificationSelectionItemApartment.setTextColor(ContextCompat.getColor(mContext,R.color.mc_next_btn_color));
            viewHolder.mAdapterSpecificationSelectionItemArea.setTextColor(ContextCompat.getColor(mContext,R.color.mc_next_btn_color));
        }else{
            viewHolder.mAdapterSpecificationSelectionLayout.setBackgroundResource(R.drawable.specification_selection_btn_bg);
            viewHolder.mAdapterSpecificationSelectionItemApartment.setTextColor(ContextCompat.getColor(mContext,R.color.text_color));
            viewHolder.mAdapterSpecificationSelectionItemArea.setTextColor(ContextCompat.getColor(mContext,R.color.text_color));
        }
        return convertView;
    }

    public class ViewHolder {
        public final TextView mAdapterSpecificationSelectionItemApartment;
        public final TextView mAdapterSpecificationSelectionItemArea;
        public final LinearLayout mAdapterSpecificationSelectionLayout;
        public final View root;

        public ViewHolder(View root) {
            mAdapterSpecificationSelectionItemApartment = (TextView) root.findViewById(R.id.adapter_specification_selection_item_apartment);
            mAdapterSpecificationSelectionItemArea = (TextView) root.findViewById(R.id.adapter_specification_selection_item_area);
            mAdapterSpecificationSelectionLayout = (LinearLayout) root.findViewById(R.id.adapter_specification_selection_layout);
            this.root = root;
        }
    }
}
