package com.water.jxwz.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.water.jxwz.R;
import com.water.jxwz.base.BaseActivity;
import com.water.jxwz.db.Photos;
import com.water.jxwz.loader.ImageLoader;
import com.water.jxwz.ui.ZoomImageView;
import com.water.jxwz.utils.BitmapCacheHelper;
import com.water.jxwz.utils.ImgLoaderUtils;
import com.water.jxwz.utils.MyUtils;

import java.util.List;

/**
 * Created by ${xsy} on 2016/4/15.
 */
public class ImageAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Drawable mDefaultBitmapDrawable;
    private List<Photos> mPhotoses;
    private boolean mIsWifi;
    private boolean mCanGetBitmapFromNetWork;
    private boolean mIsGridViewIdle;
    private ImageLoader mImageLoader;
    private Context mContext;
    private ImgLoaderUtils imgLoader;

    public ImageAdapter(Context context,List<Photos> photoses,boolean mIsWifi,boolean mCanGetBitmapFromNetWork, boolean mIsGridViewIdle) {
        mInflater = LayoutInflater.from(context);
        mDefaultBitmapDrawable = context.getResources().getDrawable(R.drawable.image_default);
        this.mPhotoses=photoses;
        this.mIsWifi=mIsWifi;
        this.mCanGetBitmapFromNetWork=mCanGetBitmapFromNetWork;
        this.mIsGridViewIdle=mIsGridViewIdle;
        this.mContext=context;
        mImageLoader = ImageLoader.build(context);
        imgLoader=new ImgLoaderUtils(mContext);
    }

    @Override
    public int getCount() {
        return mPhotoses.size();
    }

    @Override
    public String getItem(int position) {
        return mPhotoses.get(position).getImgUrl();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int screenWidth = MyUtils.getScreenMetrics(mContext).widthPixels;
        int space = (int)MyUtils.dp2px(mContext, 20f);
        int mImageWidth = (screenWidth - space) / 4;

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.image_list_item,parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ImageView imageView = holder.imageView;
       /* final String tag = (String)imageView.getTag();
        final String uri = getItem(position);
        if (!uri.equals(tag)) {
            imageView.setImageDrawable(mDefaultBitmapDrawable);
        }
        if (mIsGridViewIdle && mCanGetBitmapFromNetWork) {
            imageView.setTag(uri);
            mImageLoader.bindBitmap(uri, imageView, mImageWidth, mImageWidth);
        }*/

       if (!TextUtils.isEmpty(mPhotoses.get(position).getImgUrl())){
           BitmapCacheHelper.getInstance().addPathToShowlist(mPhotoses.get(position).getImgUrl());
       }


        Bitmap bitmap = BitmapCacheHelper.getInstance().getBitmap(mPhotoses.get(position).getImgUrl(),0, 0, new BitmapCacheHelper.ILoadImageCallback() {
            @Override
            public void onLoadImageCallBack(Bitmap bitmap, String path, Object... objects) {

                if ( bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                }

            }
        }, position);

        if (bitmap != null){
            imageView.setImageBitmap(bitmap);
        }
       // imgLoader.imgLoader(mPhotoses.get(position).getImgUrl(), holder.imageView, R.drawable.image_default, false);
        return convertView;
    }

    private static class ViewHolder {
        public ImageView imageView;
    }
}
