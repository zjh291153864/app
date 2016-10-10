package com.zhonghong.xqshijie.widget.timelineview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zhonghong.xqshijie.R;

/**
 * Created by HP-HP on 05-12-2015.
 */
public class TimeLineViewHolder extends RecyclerView.ViewHolder {
    public TextView mTvName;
    public TimelineView mTimelineView;
    public TextView mTvTime;

    public TimeLineViewHolder(View itemView, int viewType) {
        super(itemView);
        mTvName = (TextView) itemView.findViewById(R.id.tv_name);
        mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
        mTimelineView = (TimelineView) itemView.findViewById(R.id.tlv_time_marker);
        mTimelineView.initLine(viewType);
    }
}
