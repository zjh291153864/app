package com.zhonghong.xqshijie.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.data.bean.ProductPhotoBean;
import com.zhonghong.xqshijie.data.bean.ProductPhotoMappingBean;
import com.zhonghong.xqshijie.util.PublicUtils;
import com.zhonghong.xqshijie.widget.FullyGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 相册右边页面外部列表的控制器
 */
public class PhotoTotalFragmentListAdapter extends BaseAdapter {
    //去详情页的回调
    private PublicUtils.photoOutsideTabCallBack mActivityCallBack = null;

    /**
     * 设置数据
     */
    private List<ProductPhotoMappingBean> datas = null;
    private Context context = null;
    public ViewHolder holder = null;
    private RecyclerView mRcView = null;
    private mStandardRecyclerViewAdapter recyclerViewAdapter = null;

    public PhotoTotalFragmentListAdapter(Context context, PublicUtils.photoOutsideTabCallBack mActivityCallBack) {
        this.context = context;
        this.mActivityCallBack = mActivityCallBack;
    }

    public List<ProductPhotoMappingBean> getDatas() {
        return datas;
    }

    public void setDatas(List<ProductPhotoMappingBean> datas) {
        this.datas = datas;
    }

    @Override
    public int getCount() {
        if (datas != null && datas.size() > 0) {
            return datas.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ProductPhotoMappingBean currentPhotoMapBean = datas.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(this.context, R.layout.adapter_photofragment_rightpage, null);
            holder.mTvTopleftType = (TextView) convertView.findViewById(R.id.tv_photo_typeword);
            holder.mTvToprightSize = (TextView) convertView.findViewById(R.id.tv_photo_pagesize);
            //recycler
            holder.subRecycley = (RecyclerView) convertView.findViewById(R.id.rc_photo_rightpage);
            holder.subRecycley.setTag(currentPhotoMapBean.getType());
//            holder.subRecycley.setLayoutManager(new GridLayoutManager(context, 3));
            holder.subRecycley.setLayoutManager(new FullyGridLayoutManager(context, 3));
            MyStandardItemDecoration decoration = new MyStandardItemDecoration(10);
            holder.subRecycley.addItemDecoration(decoration);
            holder.subRecycley.setItemAnimator(new DefaultItemAnimator());//设置动画
            recyclerViewAdapter = new mStandardRecyclerViewAdapter();
            recyclerViewAdapter.setDatassources(null);
            holder.mViewRoot = convertView;
            holder.subRecycley.setAdapter(recyclerViewAdapter);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTvTopleftType.setText(currentPhotoMapBean.getName());
        holder.mTvToprightSize.setText(currentPhotoMapBean.getData().size() + "页");

//        内部的recyclerview
        mStandardRecyclerViewAdapter adapter = (mStandardRecyclerViewAdapter) holder.subRecycley.getAdapter();
        ArrayList<ProductPhotoBean> recyclerdata = (ArrayList<ProductPhotoBean>) currentPhotoMapBean.getData();
        adapter.setDatassources(recyclerdata);
        adapter.notifyDataSetChanged();

        //顶部条目检测，联动刷新都比按钮条目

        return convertView;
    }


    public class ViewHolder {
        public RecyclerView subRecycley = null;
        public TextView mTvTopleftType = null;
        public TextView mTvToprightSize = null;
        public View mViewRoot = null;
    }

    /**
     * ****************************** recycler  ***************************
     */
    class MyStandardItemDecoration extends RecyclerView.ItemDecoration {

        int space = 0;

        public MyStandardItemDecoration(int space) {
            super();
            this.space = space;
        }

        /**
         * 设置分隔大小
         */
        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.set(space, space, 0, 0);
        }

        /**
         * 重绘分割线风格
         */
        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDrawOver(c, parent, state);
        }
    }


    /**
     * viewholder  :item的findviewbyid
     */
    class mStandardViewHolder extends RecyclerView.ViewHolder {

        ImageView mIvSubitem = null;

        public mStandardViewHolder(final View itemview) {
            super(itemview);
            mIvSubitem = (ImageView) itemview.findViewById(R.id.iv_photo_subimg);
        }
    }


    /**
     * adapter
     */
    class mStandardRecyclerViewAdapter extends RecyclerView.Adapter<mStandardViewHolder> {

        private ArrayList<ProductPhotoBean> datasources = null;

        private void setDatassources(ArrayList<ProductPhotoBean> datasources) {
            this.datasources = datasources;
        }

        @Override
        public int getItemCount() {
            if (datasources != null) {
                return datasources.size();
            }
            return 0;
        }

        /**
         * 这里holder.textview.settext("XXXX");
         */
        @Override
        public void onBindViewHolder(mStandardViewHolder holder, final int position) {
            if (datasources != null && datasources.size() > 0) {//.override(500, 500)
//                holder.mIvSubitem.setTag(position);
                Glide.with(context).load(datasources.get(position).getmPictruUrl())/*.thumbnail(0.5f)*/.error(R.drawable.image_onloading_homebig).crossFade().placeholder(R.drawable.image_onloading_homebig).into(holder.mIvSubitem);//加载图片
            }
            holder.mIvSubitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivityCallBack.switchActivityPage(Integer.parseInt(datasources.get(position).getIndexInTotalList())-1);//直接去详情页
                }
            });
        }

        /**
         * 这里边初始化 view ... layout
         */
        @Override
        public mStandardViewHolder onCreateViewHolder(ViewGroup viewgroup, int position) {
            View subview = LayoutInflater.from(context).inflate(R.layout.adapter_photo_rightsubitem, viewgroup, false);
            subview.setTag(position);
            return new mStandardViewHolder(subview);
        }

    }

}
