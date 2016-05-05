package com.water.jxwz.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.water.jxwz.R;
import com.water.jxwz.loader.ImageLoader;
import com.water.jxwz.ui.ZoomImageView;
import com.water.jxwz.utils.BitmapCacheHelper;
import com.water.jxwz.utils.ImgLoaderUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${xsy} on 2016/4/12.
 */
public class ViewImgAdapter extends PagerAdapter {

    private List<String> mData;
    private Context mContext;
    private ViewPager mViewPager;
    private Bitmap bitmap;
    private final ImgLoaderUtils imgLoader;

    public ViewImgAdapter(Context context, List<String> mUrList, ViewPager viewPager){
        this.mData=mUrList;
        this.mContext=context;
        this.mViewPager=viewPager;
        imgLoader = new ImgLoaderUtils(mContext);
    }
    @Override
    public int getCount() {
        if (mData!=null){
            return mData.size();
        }
        return 0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_view_img, null);
        ImageView zoomImageView = (ImageView) view.findViewById(R.id.zoom_image_view);
       // ZoomImageView zoomImageView = (ZoomImageView) view.findViewById(R.id.zoom_image_view);
        /*BitmapCacheHelper.getInstance().addPathToShowlist(mData.get(position));
        zoomImageView.setTag(mData.get(position));
        Bitmap bitmap = BitmapCacheHelper.getInstance().getBitmap(mData.get(position), 0, 0, new BitmapCacheHelper.ILoadImageCallback() {
            @Override
            public void onLoadImageCallBack(Bitmap bitmap, String path, Object... objects) {
                ZoomImageView view = ((ZoomImageView)mViewPager.findViewWithTag(path));
                if (view != null && bitmap != null)
                    ((ZoomImageView)mViewPager.findViewWithTag(path)).setSourceImageBitmap(bitmap);
            }
        }, position);*/
        //ImageLoader.build(mContext).bindBitmap(mData.get(position),zoomImageView,340,420);
        imgLoader.imgLoader(mData.get(position),zoomImageView,R.drawable.default_img,false);
        // bitmap = ImageLoader.build(mContext).loadBitmap(mData.get(position),200,200);


        /*if (bitmap != null){
            zoomImageView.setSourceImageBitmap(bitmap);
        }*/
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
