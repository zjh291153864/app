package com.zhonghong.xqshijie.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.activity.AddaNewAddressActivity;
import com.zhonghong.xqshijie.activity.AddressListActivity;
import com.zhonghong.xqshijie.data.response.AddressResponse.AddressListBean;
import com.zhonghong.xqshijie.dialog.InfoToast;
import com.zhonghong.xqshijie.widget.dialog.NormalDialog;
import com.zhonghong.xqshijie.widget.dialog.OnBtnClickL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jh on 2016/7/3.
 */
public class AddressListAdapter extends BaseAdapter {
    private AddressListActivity context;
    private List<AddressListBean> mAddressList;

    private AddressListBean mAddressIs;

    public AddressListAdapter(AddressListActivity context, List<AddressListBean> mAddressList) {
        this.context = context;
        this.mAddressList = mAddressList;
    }

    @Override
    public int getCount() {
        return mAddressList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAddressList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final AddressListBean address = mAddressList.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_addresslist_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mTvAddressListItemName.setText(address.mConsigneeName);
        viewHolder.mTvAddressListItemPhone.setText(address.mConsigneeMobile);
        viewHolder.mTvAddressListItemAddress.setText(address.mProvinceName + address.mCityName + address.mCountyName + address.mAddress);
        viewHolder.mRbAddressListItemTrue.setClickable(false);

        if(address.mIsDefault){
            viewHolder.mRbAddressListItemTrue.setChecked(true);
            mAddressIs=address;
        }else{
            viewHolder.mRbAddressListItemTrue.setChecked(false);
        }
//        删除按钮监听
        viewHolder.mRbAddressListItemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NormalDialog normalDialog=new NormalDialog(context);
                normalDialog.isTitleShow(false)
                        .bgColor(Color.parseColor("#FFFFFF"))
                        .cornerRadius(5)
                        .contentGravity(Gravity.CENTER)
                        .contentTextColor(Color.parseColor("#2A2A2A"))
                        .content("确定删除地址吗？")
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
                        context.deleteAddress(address.mAddressId);
                        normalDialog.dismiss();
                    }
                });
            }
        });

//        编辑按钮监听
        viewHolder.mRbAddressListItemEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddaNewAddressActivity.class);
                intent.putExtra("address", mAddressList.get(position));
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        public final TextView mTvAddressListItemName;
        public final TextView mTvAddressListItemPhone;
        public final TextView mTvAddressListItemAddress;
        public final RadioButton mRbAddressListItemTrue;
        public final TextView mRbAddressListItemDelete;
        public final TextView mRbAddressListItemEdit;
        public final View root;

        public ViewHolder(View root) {
            mTvAddressListItemName = (TextView) root.findViewById(R.id.tv_addresslist_item_name);
            mTvAddressListItemPhone = (TextView) root.findViewById(R.id.tv_addresslist_item_phone);
            mTvAddressListItemAddress = (TextView) root.findViewById(R.id.tv_addresslist_item_address);
            mRbAddressListItemTrue = (RadioButton) root.findViewById(R.id.rb_addresslist_item_true);
            mRbAddressListItemDelete = (TextView) root.findViewById(R.id.tv_addresslist_item_delete);
            mRbAddressListItemEdit = (TextView) root.findViewById(R.id.tv_addresslist_item_edit);
            this.root = root;
        }
    }

    public AddressListBean getAddressIs() {
        return mAddressIs;
    }
}
