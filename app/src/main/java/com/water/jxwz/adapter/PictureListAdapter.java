package com.water.jxwz.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.water.jxwz.R;
import com.water.jxwz.db.Photos;
import com.water.jxwz.stickygridheaders.StickyGridHeadersSimpleAdapter;
import com.water.jxwz.utils.BitmapCacheHelper;
import com.water.jxwz.utils.DateUtil;
import com.water.jxwz.utils.MyConstants;
import com.water.jxwz.utils.ToastCustom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${xsy} on 2016/4/18.
 */
public class PictureListAdapter extends BaseAdapter implements StickyGridHeadersSimpleAdapter {

    private Context mContext;
    private List<Photos> mData;
    private LayoutInflater mInflater;
    private int maxTotal = 8;// 最多上传照片数
    private int currentTotal = 0;// 当前选择照片数

    public PictureListAdapter(Context mContext, List<Photos> mData) {
        this.mContext = mContext;
        this.mData = mData;
        mInflater = LayoutInflater.from(mContext);
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public int getCurrentTotal() {
        return currentTotal;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.image_list_item, parent, false);
            holder.iv = (ImageView) convertView.findViewById(R.id.image);
            holder.iv_select = (ImageView) convertView.findViewById(R.id.iv_select);
            holder.iv_type = (ImageView) convertView.findViewById(R.id.iv_type);
            holder.iv_upload = (ImageView) convertView.findViewById(R.id.iv_upload);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //设置数据
        holder.iv_select .setImageResource(mData.get(position).isSeleted()?R.drawable.selected : R.drawable.select_normal);
        holder.iv_type.setImageResource(mData.get(position).getImgStatus() == 0 ? R.drawable.clean : R.drawable.dirty);
        holder.iv_upload.setVisibility(mData.get(position).getIsUpload() == 1 ? View.VISIBLE : View.GONE);

        if (!TextUtils.isEmpty(mData.get(position).getImgUrl())){
            BitmapCacheHelper.getInstance().addPathToShowlist(mData.get(position).getImgUrl());
        }


        Bitmap bitmap = BitmapCacheHelper.getInstance().getBitmap(mData.get(position).getImgUrl(),0, 0, new BitmapCacheHelper.ILoadImageCallback() {
            @Override
            public void onLoadImageCallBack(Bitmap bitmap, String path, Object... objects) {

                if ( bitmap != null) {
                    holder.iv.setImageBitmap(bitmap);
                }

            }
        }, position);

        if (bitmap != null){
            holder.iv.setImageBitmap(bitmap);
        }
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return mData.get(position).getSection();
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {

        final HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.img_list_header, parent, false);
            holder.tv_header = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(holder);

        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        String time = mData.get(position).getTakePicTime();
        if (time!=null){
            holder.tv_header.setText(time.substring(0,4)+"年"+time.substring(5,7)+"月"+time.substring(8,10)+"日");
        }

        return convertView;
    }

    class ViewHolder{
        private ImageView iv;
        private ImageView iv_select;
        private ImageView iv_type;
        private ImageView iv_upload;

    }
    class HeaderViewHolder{
        private TextView tv_header;
    }

    public void changeSelection(View v, int position) {

        if (mData.get(position).getIsUpload()==1){
            return;
        }

        if (mData.get(position).isSeleted()) {
            mData.get(position).setIsSeleted(false);
            currentTotal--;
        } else {
            if (currentTotal == maxTotal) {
               ToastCustom.makeText(mContext, "一次最多只能上传" + maxTotal + "张照片", Toast.LENGTH_LONG).show();
                return;
            }
            mData.get(position).setIsSeleted(true);
            currentTotal++;
        }

        ((ViewHolder) v.getTag()).iv_select .setImageResource(mData.get(position).isSeleted() ?
                R.drawable.selected : R.drawable.select_normal);

    }

    public ArrayList<Photos> getSelected() {
        ArrayList<Photos> imgSelected = new ArrayList<Photos>();

        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).isSeleted()) {
                imgSelected.add(mData.get(i));
            }
        }

        return imgSelected;
    }

    public ArrayList<String> getSelectedImgUrl() {
        ArrayList<String> imgSelectedUrl = new ArrayList<String>();

        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).isSeleted()) {
                imgSelectedUrl.add(mData.get(i).getImgUrl());
            }
        }

        return imgSelectedUrl;
    }
}
