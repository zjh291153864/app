package com.zhonghong.xqshijie.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zhonghong.xqshijie.R;
import com.zhonghong.xqshijie.widget.timelineview.Orientation;
import com.zhonghong.xqshijie.widget.timelineview.TimeLineAdapter;
import com.zhonghong.xqshijie.widget.timelineview.TimeLineModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP-HP on 05-12-2015.
 */
public class TimeLineActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private TimeLineAdapter mTimeLineAdapter;

    private List<TimeLineModel> mDataList = new ArrayList<>();

    private Orientation mOrientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_timeline);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(getLinearLayoutManager());
        mRecyclerView.setHasFixedSize(true);
        initView();
    }

    private LinearLayoutManager getLinearLayoutManager() {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            return linearLayoutManager;
    }

    private void initView() {
        //测试数据
        for(int i = 0;i <20;i++) {
            TimeLineModel model = new TimeLineModel();
            model.name="合同已经派发";
            model.time="2016-13-"+i;
            mDataList.add(model);
        }

        mTimeLineAdapter = new TimeLineAdapter(mDataList, mOrientation);
        mRecyclerView.setAdapter(mTimeLineAdapter);
    }

}
