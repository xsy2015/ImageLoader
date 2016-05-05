package com.water.jxwz.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.water.jxwz.R;
import com.water.jxwz.adapter.ImageAdapter;
import com.water.jxwz.base.BaseActivity;
import com.water.jxwz.db.Photos;
import com.water.jxwz.loader.ImageLoader;
import com.water.jxwz.utils.MyUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

public class PhotoListActivity extends BaseActivity implements OnScrollListener,AdapterView.OnItemClickListener,View.OnClickListener {
    private static final String TAG = "PhotoListActivity";

    private List<String> mUrList = new ArrayList<String>();
    ImageLoader mImageLoader;
    private GridView mImageGridView;
    private BaseAdapter mImageAdapter;

    private boolean mIsGridViewIdle = true;
    private int mImageWidth = 0;
    private boolean mIsWifi = false;
    private boolean mCanGetBitmapFromNetWork = false;
    private ImageView iv_go_back;
    private TextView tv_upload;
    private List<Photos> photoses;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        mImageLoader = ImageLoader.build(PhotoListActivity.this);
    }

    private void initData() {
        /*String[] imageUrls = {

                "http://img3.3lian.com/2013/c2/32/d/101.jpg",
                "http://pic25.nipic.com/20121210/7447430_172514301000_2.jpg",
                "http://img02.tooopen.com/images/20140320/sy_57121781945.jpg",
                "http://pic94.nipic.com/file/20160411/6842469_204233461001_2.jpg"
        };
        for (String url : imageUrls) {
            mUrList.add(url);
        }*/
        photoses = DataSupport.order("takePicTime").find(Photos.class);
        Collections.reverse(photoses);
        for (int i = 0; i < photoses.size() ; i++) {
            if (!TextUtils.isEmpty(photoses.get(i).getImgUrl())){

                mUrList.add(photoses.get(i).getImgUrl());
            }


        int screenWidth = MyUtils.getScreenMetrics(this).widthPixels;
        int space = (int)MyUtils.dp2px(this, 20f);
        mImageWidth = (screenWidth - space) / 3;
        mIsWifi = MyUtils.isWifi(this);
        if (mIsWifi) {
            mCanGetBitmapFromNetWork = true;
        }
    }}

    private void initView() {
        initTitle();
        title_center.setText(R.string.upload_manager);



        mImageGridView = (GridView) findViewById(R.id.gridView1);
       // mImageAdapter = new ImageAdapter(this);
        imageAdapter = new ImageAdapter(this, photoses, mIsWifi, mCanGetBitmapFromNetWork, mIsGridViewIdle);
        mImageGridView.setAdapter(imageAdapter);


        mImageGridView.setOnScrollListener(this);

        mImageGridView.setOnItemClickListener(this);

        if (!mIsWifi) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("初次使用会从网络下载大概5MB的图片，确认要下载吗？");
            builder.setTitle("注意");
            builder.setPositiveButton("是", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mCanGetBitmapFromNetWork = true;
                    mImageAdapter.notifyDataSetChanged();
                }
            });
            builder.setNegativeButton("否", null);
            builder.show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent(PhotoListActivity.this, ViewPhotosActivity.class);
        intent.putStringArrayListExtra("mUrList", (ArrayList<String>) mUrList);
        intent.putExtra("index",i);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
//            case R.id.iv_go_back://返回
//                break;
//            case R.id.tv_upload://上传照片
//                break;
            default:
                break;
        }
    }

    /*private class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Drawable mDefaultBitmapDrawable;

        private ImageAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
            mDefaultBitmapDrawable = context.getResources().getDrawable(R.drawable.image_default);
        }

        @Override
        public int getCount() {
            return mUrList.size();
        }

        @Override
        public String getItem(int position) {
            return mUrList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.image_list_item,parent, false);
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ImageView imageView = holder.imageView;
            final String tag = (String)imageView.getTag();
            final String uri = getItem(position);
            if (!uri.equals(tag)) {
                imageView.setImageDrawable(mDefaultBitmapDrawable);
            }
            if (mIsGridViewIdle && mCanGetBitmapFromNetWork) {
                imageView.setTag(uri);
                mImageLoader.bindBitmap(uri, imageView, mImageWidth, mImageWidth);
            }
            return convertView;
        }

    }

    private static class ViewHolder {
        public ImageView imageView;
    }*/

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            mIsGridViewIdle = true;
            mImageAdapter.notifyDataSetChanged();
        } else {
            mIsGridViewIdle = false;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
        // ignored
        
    }
}
