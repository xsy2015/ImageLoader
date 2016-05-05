package com.water.jxwz.adapter;

import android.app.Activity;
import android.content.Context;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.water.jxwz.R;
import com.water.jxwz.loader.ImageLoader;
import com.water.jxwz.ui.ZoomImageView;
import com.water.jxwz.utils.BitmapCacheHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${xsy} on 2016/4/12.
 */
public class ViewPhotoAdapter extends PagerAdapter {

    private List<String> mData;
    private Context mContext;
    private ViewPager mViewPager;
    private Bitmap bitmap;

    public ViewPhotoAdapter(Context context,ArrayList<String> mUrList,ViewPager viewPager){
        this.mData=mUrList;
        this.mContext=context;
        this.mViewPager=viewPager;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_view_photo, null);
       // ZoomImageView zoomImageView = (ZoomImageView) view.findViewById(R.id.zoom_image_view);
        ZoomImageView zoomImageView = (ZoomImageView) view.findViewById(R.id.zoom_image_view);
        BitmapCacheHelper.getInstance().addPathToShowlist(mData.get(position));
        zoomImageView.setTag(mData.get(position));
        Bitmap bitmap = BitmapCacheHelper.getInstance().getBitmap(mData.get(position), 0, 0, new BitmapCacheHelper.ILoadImageCallback() {
            @Override
            public void onLoadImageCallBack(Bitmap bitmap, String path, Object... objects) {
                ZoomImageView view = ((ZoomImageView)mViewPager.findViewWithTag(path));
                if (view != null && bitmap != null)
                    ((ZoomImageView)mViewPager.findViewWithTag(path)).setSourceImageBitmap(bitmap);
            }
        }, position);
//        ImageLoader.build(mContext).bindBitmap(mData.get(position),zoomImageView);
//        bitmap = ImageLoader.build(mContext).loadBitmap(mData.get(position),200,200);
        /*new Thread(){
            @Override
            public void run() {
                super.run();
            }
        }.start();*/

        if (bitmap != null){
            zoomImageView.setSourceImageBitmap(bitmap);
        }
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        View view = (View) object;
        container.removeView(view);
       BitmapCacheHelper.getInstance().removePathFromShowlist(mData.get(position));

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view==object;
    }
}
