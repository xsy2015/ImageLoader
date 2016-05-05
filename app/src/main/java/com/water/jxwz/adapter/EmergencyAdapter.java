package com.water.jxwz.adapter;

import android.content.Context;

import com.water.jxwz.R;
import com.water.jxwz.bean.SuperiorNoticeBean;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.recyclerview.CommonAdapter;

import java.util.List;

/**
 * Created by ${xsy} on 2016/4/18.
 */
public class EmergencyAdapter extends CommonAdapter<SuperiorNoticeBean> {



    public EmergencyAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);

    }

    @Override
    public void convert(ViewHolder holder, SuperiorNoticeBean bean) {
            holder.setText(R.id.tv_title,bean.getTitle());
            holder.setText(R.id.tv_time,bean.getTime());
            holder.setText(R.id.tv_content,bean.getContent());


    }


}
