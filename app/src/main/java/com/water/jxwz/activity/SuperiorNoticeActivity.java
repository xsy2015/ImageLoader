package com.water.jxwz.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.water.jxwz.R;
import com.water.jxwz.adapter.SuperiorNoticeAdapter;
import com.water.jxwz.base.BaseActivity;
import com.water.jxwz.bean.SuperiorNoticeBean;
import com.zhy.base.adapter.recyclerview.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${xsy} on 2016/4/18.
 */
public class SuperiorNoticeActivity extends BaseActivity{

    private RecyclerView mRecycleView;
    private List<SuperiorNoticeBean> list=new ArrayList<SuperiorNoticeBean>();
    private SuperiorNoticeAdapter adapter;
    private int staffType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_superior_notice);
        staffType = getIntent().getIntExtra("staffType", 0);

        initView();

       getData();
    }

    private void getData() {
        for (int i = 0; i < 3; i++) {

            list.add(new SuperiorNoticeBean("上级通知","2016-4-18","枣庄河段水质较差"));
        }

        adapter = new SuperiorNoticeAdapter(this, R.layout.item_superior_notice,list,staffType);
        mRecycleView.setAdapter(adapter);
    }

    private void initView() {
        initTitle();
        if (staffType==2){//巡防员
            title_center.setText(R.string.emergency_notice);
        }else {
            title_center.setText(R.string.superior_notice);
        }


        mRecycleView = (RecyclerView)findViewById(R.id.rv);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

    }
}
