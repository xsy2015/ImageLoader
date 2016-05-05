package com.water.jxwz.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.water.jxwz.R;
import com.water.jxwz.adapter.ViewPhotoAdapter;
import com.water.jxwz.base.BaseActivity;
import com.water.jxwz.base.Constant;
import com.water.jxwz.db.Photos;
import com.water.jxwz.utils.SharedInfoUtils;
import com.water.jxwz.utils.ToastCustom;


import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ${xsy} on 2016/4/12.
 */
public class ViewPhotosActivity extends BaseActivity implements ViewPager.OnPageChangeListener,View.OnClickListener{

    private ViewPager vp_content;
    private TextView tv_index;
    private ArrayList<String> mUrList;
    private ArrayList<String> mImgUrls=new ArrayList<String>();
    private int index;
    private ImageView iv_clean;
    private ImageView iv_dirty;
    private ImageView iv_more;
    private ImageView iv_cancle;

    private PopupWindow pw;
    private ScaleAnimation sa;
    private View popupView;
    private LinearLayout rl_bottom;

    private boolean flag=false;

    //参数类型
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    //创建OkHttpClient实例
    private final OkHttpClient client = new OkHttpClient();
    private int currentPic;
    private String takePicTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);
        initView();
        initData();
        //初始化popupwindow
        initPopupWindow();
    }

    private void initData() {

      List<Photos> photoses = DataSupport.order("takePicTime").find(Photos.class);
      Collections.reverse(photoses);
        for (int i = 0; i <photoses.size() ; i++) {
            if (!TextUtils.isEmpty(photoses.get(i).getImgUrl())){

                mImgUrls.add(photoses.get(i).getImgUrl());
            }

        }

        Intent intent = getIntent();
//        mUrList = intent.getStringArrayListExtra("mUrList");
//        index = intent.getIntExtra("index", -1);

        String type = intent.getStringExtra("type");

        if (Constant.FROM_CAMERA.equals(type)){
            mUrList = intent.getStringArrayListExtra("mUrList");
            index = intent.getIntExtra("index", 0);
            takePicTime = intent.getStringExtra("takePicTime");

        }else if (Constant.FROM_PICTURE_LIST.equals(type)){
            index=intent.getIntExtra("index",0)+1;;
            mUrList=mImgUrls;
        }else if(Constant.FROM_PICTURE_PREVIEW.equals(type)){

            mUrList = intent.getStringArrayListExtra("mUrList");
            index=intent.getIntExtra("index",0)+1;;
        }

        currentPic = index;

        ViewPhotoAdapter adapter = new ViewPhotoAdapter(ViewPhotosActivity.this,mUrList,vp_content);
        vp_content.setAdapter(adapter);
        vp_content.setCurrentItem(index);

        title_center.setText((currentPic) + "/" + mUrList.size());

    }

    private void initView() {
        initTitle();
        title_left.setText("返回");
        vp_content = (ViewPager) findViewById(R.id.vp_content);
        tv_index = (TextView) findViewById(R.id.tv_index);
        vp_content.addOnPageChangeListener(this);

        iv_clean = (ImageView)findViewById(R.id.iv_clean);
        iv_dirty = (ImageView)findViewById(R.id.iv_dirty);
        iv_more = (ImageView)findViewById(R.id.iv_more);
        iv_cancle=(ImageView)findViewById(R.id.iv_cancel);
        iv_clean.setOnClickListener(this);
        iv_dirty.setOnClickListener(this);
        iv_more.setOnClickListener(this);
        iv_cancle.setOnClickListener(this);

        rl_bottom = (LinearLayout)findViewById(R.id.rl_bottom);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        title_center.setText((position) + "/" + mUrList.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_cancel:
            {
                DataSupport.deleteAll(Photos.class, "imgUrl=?",mUrList.get(0));
                ToastCustom.makeText(this, "放弃成功", Toast.LENGTH_LONG).show();
                finish();
            }
            break;
            case R.id.iv_clean:
                {
                    uploadImg(0);
                    finish();
                }
                break;
            case R.id.iv_dirty:
            {
                /*flag=!flag;
                if (flag){
                    iv_dirty.setSelected(true);
                    iv_dirty.setPressed(true);
                }else {
                    iv_dirty.setSelected(false);
                    iv_dirty.setPressed(false);
                }*/
                uploadImg(1);
                finish();
            }
            break;
            case R.id.iv_more:
            {
                showPopupWindow();
            }
            break;
            case R.id.tv_alarm:
            {
                pw.dismiss();
            }
            break;
            case R.id.tv_upload:
            {
                Intent intent = new Intent(ViewPhotosActivity.this, PictureListActivity.class);
                startActivity(intent);
                pw.dismiss();
            }
            break;
            case R.id.tv_notice:
            {
                Intent intent = new Intent(ViewPhotosActivity.this, SuperiorNoticeActivity.class);
                startActivity(intent);
                pw.dismiss();
            }
            break;
            case R.id.tv_set:
            {
                Intent intent = new Intent(ViewPhotosActivity.this, SettingActivity.class);
                startActivity(intent);
                pw.dismiss();
            }
            break;
            default:
                break;
        }
    }

    private void uploadImg(final int imgType) {

      // MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (int i = 0; i <mUrList.size() ; i++) {
            File f=new File(mImgUrls.get(i));
            if (f!=null) {
                builder.addFormDataPart("img", f.getName(), RequestBody.create(MEDIA_TYPE_PNG, f));
            }
        }
//        builder.addFormDataPart("time",takePicTime);
//        builder.addFormDataPart("mapX", SharedInfoUtils.getLongitude());
//        builder.addFormDataPart("mapY",SharedInfoUtils.getLatitude());
//        builder.addFormDataPart("name",SharedInfoUtils.getUserName());


        MultipartBody requestBody = builder.build();
        //构建请求
        Request request = new Request.Builder()
                .url(Constant.BASE_URL)//地址
                .post(requestBody)//添加请求体
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("request = " + call.request().url());
                System.out.println("上传失败:e.getLocalizedMessage() = " + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call,Response response) throws IOException {
                //看清楚是response.body().string()不是response.body().toString()
                System.out.println("上传照片成功：response = " + response.body().string());

                Photos photos = new Photos();
                photos.setImgStatus(imgType);
                photos.setIsUpload(1);
                for (int i = 0; i < mUrList.size(); i++) {
                    photos.updateAll("imgUrl=?",mUrList.get(i));
                }

            }
        });

    }

    private void initPopupWindow() {

        sa = new ScaleAnimation(1, 1,
                0, 1,
                Animation.RELATIVE_TO_SELF, 1,
                Animation.RELATIVE_TO_SELF, 0);
        sa.setDuration(200);
        popupView = View.inflate(ViewPhotosActivity.this,R.layout.popupwidow_add, null);

        TextView tv_alarm = (TextView) popupView.findViewById(R.id.tv_alarm);
        TextView tv_upload = (TextView) popupView.findViewById(R.id.tv_upload);
        TextView tv_notice = (TextView) popupView.findViewById(R.id.tv_notice);
        TextView tv_set = (TextView) popupView.findViewById(R.id.tv_set);
        tv_alarm.setOnClickListener(this);
        tv_upload.setOnClickListener(this);
        tv_notice.setOnClickListener(this);
        tv_set.setOnClickListener(this);


        pw = new PopupWindow(popupView, -2, 350);
        pw.setBackgroundDrawable(getDrawable(R.drawable.round_rectangle_pop));
        pw.setFocusable(true);
        pw.setOutsideTouchable(true);
    }

    private void showPopupWindow(){
        if ( pw.isShowing()) {//显示
            pw.dismiss();
        } else {
            int[] location = new int[2];
            rl_bottom.getLocationOnScreen(location);
           pw.showAtLocation(rl_bottom, Gravity.RIGHT|Gravity.TOP, location[0], location[1]-350);
            //pw.showAsDropDown(rl_bottom, 0, 0, Gravity.TOP);

            popupView.startAnimation(sa);
        }
    }
}
