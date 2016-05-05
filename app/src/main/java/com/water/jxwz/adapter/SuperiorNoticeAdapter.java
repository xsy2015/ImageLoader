package com.water.jxwz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.water.jxwz.R;
import com.water.jxwz.bean.SuperiorNoticeBean;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.recyclerview.CommonAdapter;

import java.util.List;

/**
 * Created by ${xsy} on 2016/4/18.
 */
public class SuperiorNoticeAdapter extends CommonAdapter<SuperiorNoticeBean> {

    private int type;

    public SuperiorNoticeAdapter(Context context, int layoutId, List datas,int type) {
        super(context, layoutId, datas);
        this.type=type;
    }

    @Override
    public void convert(ViewHolder holder, SuperiorNoticeBean bean) {
            holder.setText(R.id.tv_title,bean.getTitle());
            holder.setText(R.id.tv_time,bean.getTime());
            holder.setText(R.id.tv_content,bean.getContent());

        if (type==2){//巡防员
            holder.setVisible(R.id.iv_emergency,true);
        }
    }


}
