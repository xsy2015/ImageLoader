package com.water.jxwz.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.water.jxwz.R;
import com.water.jxwz.bean.OfficerBean;
import com.water.jxwz.utils.UIUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${xsy} on 2016/4/19.
 */
public class OfficerManagerAdapter extends BaseAdapter {

    private Context mContext;
    private List<OfficerBean> mData;
    private TextView tv_index;

    public OfficerManagerAdapter(Context mContext, List<OfficerBean> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        if (mData!=null){
            return mData.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (mData!=null){
            return mData.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view==null){
            view= UIUtils.inflate(R.layout.item_officer_manager);
            holder=new ViewHolder();
            //设置标记
            view.setTag(holder);

            holder.iv_msg= (ImageView) view.findViewById(R.id.iv_msg);
            holder.tv_name=(TextView)view.findViewById(R.id.tv_name);
            holder.tv_number=(TextView)view.findViewById(R.id.tv_number);
            holder.iv1=(ImageView) view.findViewById(R.id.iv1);
            holder.iv2=(ImageView) view.findViewById(R.id.iv2);
            holder.iv3=(ImageView) view.findViewById(R.id.iv3);
            holder.view_img=(ImageView) view.findViewById(R.id.view_img);

        }else {
            holder=(ViewHolder)view.getTag();
        }
        //设置数据
        holder.tv_name.setText(mData.get(i).getName());
        holder.tv_number.setText(mData.get(i).getRiverNumber());

        holder.view_img.setTag(i);
        holder.view_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              showImgDialog();
            }
        });

        return view;
    }

    private void showImgDialog() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        final AlertDialog ab = new AlertDialog.Builder(mContext).create();
        ab.show();
        Window window = ab.getWindow();
        window.setContentView(R.layout.dialog_show_img);
        WindowManager.LayoutParams lp = window.getAttributes();
        //lp.y=800;
        window.setLayout((int) (width*0.9),(int) (height*0.7));
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);

        //View view = View.inflate(mContext,R.layout.dialog_select_time_period, null);
        //view.getBackground().setAlpha(0);
        ViewPager vp_content = (ViewPager) window.findViewById(R.id.vp_content);
        ImageView iv_close = (ImageView) window.findViewById(R.id.iv_close);
        tv_index = (TextView) window.findViewById(R.id.index);


       List<String>mUrList=new ArrayList<String>();
        mUrList.add("http://b.hiphotos.baidu.com/zhidao/pic/item/a6efce1b9d16fdfafee0cfb5b68f8c5495ee7bd8.jpg");
        mUrList.add("http://pic47.nipic.com/20140830/7487939_180041822000_2.jpg");
        mUrList.add("http://pic41.nipic.com/20140518/4135003_102912523000_2.jpg");
        mUrList.add("");
        mUrList.add("");
        mUrList.add("");


        ViewImgAdapter adapter=new ViewImgAdapter(mContext,mUrList,vp_content);
        vp_content.setAdapter(adapter);
        vp_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv_index.setText(position + 1+"");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                ab.dismiss();

            }
        });

    }

    class ViewHolder{
        ImageView iv_msg;
        TextView tv_name;
        TextView tv_number;
        ImageView iv1;
        ImageView iv2;
        ImageView iv3;
        ImageView view_img;
    }
}
