package com.zhonghong.xqshijie.widget.timelineview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zhonghong.xqshijie.R;

import java.util.List;

/**
 * Created by HP-HP on 05-12-2015.
 */
public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineViewHolder> {

    private List<TimeLineModel> mFeedList;
    private Context mContext;
    private Orientation mOrientation;
    public TimeLineAdapter(List<TimeLineModel> feedList, Orientation orientation) {
        mFeedList = feedList;
        mOrientation = orientation;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        View view = View.inflate(parent.getContext(), R.layout.adapter_item_timeline, null);
//        if(mOrientation == Orientation.horizontal) {
//            view = View.inflate(parent.getContext(), R.layout.item_timeline_horizontal, null);
//        } else {
//            view = View.inflate(parent.getContext(), R.layout.item_timeline, null);
//        }

        return new TimeLineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolder holder, int position) {

        TimeLineModel timeLineModel = mFeedList.get(position);

        holder.mTvName.setText(timeLineModel.name);
        holder.mTvTime.setText(timeLineModel.time);

    }

    @Override
    public int getItemCount() {
        return (mFeedList!=null? mFeedList.size():0);
    }

}
