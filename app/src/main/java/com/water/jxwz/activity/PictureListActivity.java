package com.water.jxwz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.water.jxwz.R;
import com.water.jxwz.adapter.PictureListAdapter;
import com.water.jxwz.base.BaseActivity;
import com.water.jxwz.base.Constant;
import com.water.jxwz.db.Photos;
import com.water.jxwz.stickygridheaders.StickyGridHeadersGridView;
import com.water.jxwz.utils.ToastCustom;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ${xsy} on 2016/4/18.
 */
public class PictureListActivity extends BaseActivity implements AdapterView.OnItemClickListener,View.OnClickListener{

    private StickyGridHeadersGridView mGridView;
    private List<Photos> photoses;
    private Map<String, Integer> sectionMap = new HashMap<String, Integer>();
    private static int section = 1;
    private List<String> mUrList = new ArrayList<String>();
    private TextView tv_delete;
    private TextView tv_upload;
    private PictureListAdapter adapter;
    private List<String> mImgUrls=new ArrayList<>();
    //参数类型
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    //创建OkHttpClient实例
    private final OkHttpClient client = new OkHttpClient();
    private ArrayList<Photos> photoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_list);
        initView();

        initData();
    }


    private void initView() {
        initTitle();
        title_center.setText(R.string.upload_manager);
        title_right.setText(R.string.preview);
        title_right.setOnClickListener(this);

        tv_delete = (TextView) findViewById(R.id.tv_delete);
        tv_upload = (TextView) findViewById(R.id.tv_upload);
        tv_delete.setOnClickListener(this);
        tv_upload.setOnClickListener(this);

        mGridView = (StickyGridHeadersGridView) findViewById(R.id.gv);
        mGridView.setOnItemClickListener(this);
    }

    private void initData() {
        photoses = DataSupport.order("takePicTime").find(Photos.class);
        Collections.reverse(photoses);
        for (int i = 0; i < photoses.size() ; i++) {
            if (!TextUtils.isEmpty(photoses.get(i).getImgUrl())){

                mUrList.add(photoses.get(i).getImgUrl());
            }
        }


        for(ListIterator<Photos> it = photoses.listIterator(); it.hasNext();){
            Photos mGridItem = it.next();
            String ym = mGridItem.getTakePicTime().substring(0,10);
            if(!sectionMap.containsKey(ym)){
                mGridItem.setSection(section);
                sectionMap.put(ym, section);
                section ++;
            }else{
                mGridItem.setSection(sectionMap.get(ym));
            }
        }

        adapter = new PictureListAdapter(PictureListActivity.this, photoses);
        mGridView.setAdapter(adapter);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

       /* Intent intent=new Intent(PictureListActivity.this, ViewPhotosActivity.class);
        intent.putStringArrayListExtra("mUrList", (ArrayList<String>) mUrList);
        intent.putExtra("index", position);
        intent.putExtra("type", Constant.FROM_PICTURE_LIST);
        startActivity(intent);*/

        adapter.changeSelection(view, position);

        tv_upload.setText("上传(" + adapter.getCurrentTotal() + "/" + adapter.getMaxTotal() + ")");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.tv_upload://上传
            {
                photoList = adapter.getSelected();
                for (int i = 0; i < photoList.size() ; i++) {
                    mImgUrls.add(photoList.get(i).getImgUrl());
                }
                uploadImg();
            }
            break;
            case R.id.title_right://预览
            {
                Intent intent=new Intent(PictureListActivity.this, ViewPhotosActivity.class);
                intent.putStringArrayListExtra("mUrList", adapter.getSelectedImgUrl());
//                intent.putExtra("index", 1);
                intent.putExtra("type", Constant.FROM_PICTURE_PREVIEW);
                startActivity(intent);
            }
            break;
            case R.id.tv_delete://删除
            {

                ArrayList<Photos> selectedImgs = adapter.getSelected();
                String[] imgs =new String[selectedImgs.size()];;
                for (int i = 0; i < selectedImgs.size(); i++) {

                    String imgUrl = selectedImgs.get(i).getImgUrl();
                    imgs[i]=imgUrl;
                    DataSupport.deleteAll(Photos.class, "imgUrl=?",imgUrl);
                }
                adapter.notifyDataSetChanged();
                initData();
                ToastCustom.makeText(this, "删除成功", Toast.LENGTH_LONG).show();
            }
            break;
            default:
                break;
        }
    }


    private void uploadImg() {

        // MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (int i = 0; i <mImgUrls.size() ; i++) {
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
            public void onResponse(Call call, Response response) throws IOException {
                //看清楚是response.body().string()不是response.body().toString()
                System.out.println("上传照片成功：response = " + response.body().string());
                ToastCustom.makeText(PictureListActivity.this, "上传成功", Toast.LENGTH_LONG).show();
               /* Photos photos = new Photos();
                photos.setImgStatus(imgType);
                photos.setIsUpload(1);
                for (int i = 0; i < mUrList.size(); i++) {
                    photos.updateAll("imgUrl=?",mUrList.get(i));
                }*/

            }
        });

    }

}
