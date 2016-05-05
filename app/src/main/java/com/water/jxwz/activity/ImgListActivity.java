package com.water.jxwz.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.water.jxwz.R;
import com.water.jxwz.base.BaseActivity;
import com.water.jxwz.db.Photos;
import com.water.jxwz.utils.BitmapCacheHelper;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.recyclerview.DividerItemDecoration;
import com.zhy.base.adapter.recyclerview.OnItemClickListener;
import com.zhy.base.adapter.recyclerview.support.SectionAdapter;
import com.zhy.base.adapter.recyclerview.support.SectionSupport;

import org.litepal.crud.DataSupport;

import java.util.Collections;
import java.util.List;

/**
 * Created by ${xsy} on 2016/4/18.
 */
public class ImgListActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private List<Photos> photoses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_lsit);
        initView();

        initData();
    }

    private void initData() {

        photoses = DataSupport.order("takePicTime").find(Photos.class);
        Collections.reverse(photoses);

        SectionSupport<Photos> sectionSupport = new SectionSupport<Photos>()
        {
            @Override
            public int sectionHeaderLayoutId()
            {
                return R.layout.img_list_header;
            }

            @Override
            public int sectionTitleTextViewId()
            {
                return R.id.tv_title;
            }

            @Override
            public String getTitle(Photos photo)
            {
                return photo.getTakePicTime().substring(0,10);
            }
        };
        SectionAdapter<Photos> adapter = new SectionAdapter<Photos>(this, R.layout.image_list_item, photoses, sectionSupport)
        {

            @Override
            public void convert(final ViewHolder holder, Photos photos)
            {
                BitmapCacheHelper.getInstance().addPathToShowlist(photos.getImgUrl());
                Bitmap bitmap = BitmapCacheHelper.getInstance().getBitmap(photos.getImgUrl(),0, 0, new BitmapCacheHelper.ILoadImageCallback() {
                    @Override
                    public void onLoadImageCallBack(Bitmap bitmap, String path, Object... objects) {

                        if ( bitmap != null) {
                            holder.setImageBitmap(R.id.image,bitmap);
                        }

                    }
                });
               if (bitmap!=null){
                   holder.setImageBitmap(R.id.image,bitmap);
               }
            }
        };
        adapter.setOnItemClickListener(new OnItemClickListener<Photos>()
        {
            @Override
            public void onItemClick(ViewGroup parent, View view, Photos o, int position)
            {
                Toast.makeText(ImgListActivity.this, "Click:" + position + " => " + o, Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Photos o, int position)
            {
                return false;
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_imgs);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }
}
