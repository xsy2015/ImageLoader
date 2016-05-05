package com.water.jxwz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.water.jxwz.R;
import com.water.jxwz.adapter.OfficerManagerAdapter;
import com.water.jxwz.base.BaseActivity;
import com.water.jxwz.bean.OfficerBean;
import com.water.jxwz.customview.RefreshLayout;
import com.water.jxwz.customview.XListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by ${xsy} on 2016/4/19.
 */
public class OfficerManagerActivity extends BaseActivity  implements XListView.IXListViewListener,AdapterView.OnItemClickListener{

    private XListView mListView;
    private List<OfficerBean> list = new ArrayList<>();;
    private OfficerManagerAdapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officer_manager);

        initView();

        getData();

    }


    private void initView() {

        initTitle();
        title_center.setText(R.string.officer_manager);

        mListView = (XListView) findViewById(R.id.lv);
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        mListView.setAutoLoadEnable(true);
        mListView.setXListViewListener(this);
        mListView.setRefreshTime(getTime());

        mListView.setOnItemClickListener(this);

    }

    private void getData() {


        list.clear();

        OfficerBean bean = null;
        for (int i = 0; i < 5; i++) {
            bean = new OfficerBean(("王英五" + i), "2016041900" + i);
            list.add(bean);
        }

        adapter = new OfficerManagerAdapter(OfficerManagerActivity.this, list);
        mListView.setAdapter(adapter);

    }
   /* @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            mListView.autoRefresh();
        }
    }*/


    @Override
    public void onRefresh() {
            getData();
            onLoad();
    }

    @Override
    public void onLoadMore() {

        getData();
        adapter.notifyDataSetChanged();
        onLoad();
    }

    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime(getTime());
    }

    private String getTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Intent intent = new Intent(OfficerManagerActivity.this, OfficerDetailsActivity.class);
        startActivity(intent);
    }
}
