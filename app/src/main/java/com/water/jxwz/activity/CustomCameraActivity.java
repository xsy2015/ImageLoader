package com.water.jxwz.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.water.jxwz.R;
import com.water.jxwz.base.Constant;
import com.water.jxwz.base.MyApplication;
import com.water.jxwz.db.Photos;
import com.water.jxwz.utils.CameraUtil;
import com.water.jxwz.utils.DateUtil;
import com.water.jxwz.utils.SharedInfoUtils;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by KingJA on 2016/1/24.
 */
public class CustomCameraActivity extends Activity implements SurfaceHolder.Callback,View.OnClickListener{
    private Camera mCamera;
    private SurfaceView mPreview;
    private SurfaceHolder mHolder;
    private ImageButton shutter;
    private ImageView iv_preview;
    private TextView tv_date;
    private File imageFile;
    private Bitmap newBitmap;
    private List<String> imgList=new ArrayList<String>();

    private PopupWindow pw;
    private ScaleAnimation sa;
    private View popupView;
    private RelativeLayout rl_bottom;
    private TextView tv_more;
    private String imgUrl;

    private Camera.PictureCallback pictureCallback=new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            imageFile = CameraUtil.createImageFile();
            try {

                FileOutputStream fis = new FileOutputStream(imageFile);
                fis.write(data);
                fis.close();
                Bitmap oldBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                iv_preview.setImageBitmap(oldBitmap);
//                CameraUtil.bitmap2Location(newBitmap,imageFile.getAbsolutePath());

            } catch (IOException e) {
                e.printStackTrace();
            }

            takePicTime = DateUtil.getSecond(System.currentTimeMillis());

            saveImgInfo();//保存相片信息
            handleImg();//处理照片
        }
    };
    private String takePicTime;

    private void handleImg() {
        if (imgList.size()>0){
            imgList.clear();
        }
        imgUrl = imageFile.getAbsolutePath();
        imgList.add(imgUrl);
        Intent intent = new Intent(CustomCameraActivity.this, ViewPhotosActivity.class);
        intent.putStringArrayListExtra("mUrList", (ArrayList<String>) imgList);
        intent.putExtra("index", 1);
        intent.putExtra("type", Constant.FROM_CAMERA);
        intent.putExtra("takePicTime", takePicTime);
        startActivity(intent);
    }


    private void saveImgInfo() {//保存相片信息到数据库

        Photos mPhoto=new Photos();
        mPhoto.setTakePicTime(takePicTime);
        mPhoto.setImgUrl(imageFile.getAbsolutePath());
        mPhoto.setMapX(SharedInfoUtils.getLongitude());
        mPhoto.setMapY(SharedInfoUtils.getLatitude());
        mPhoto.setImgName(imageFile.getAbsolutePath().substring(26));
        mPhoto.save();

        List<Photos> photoses = DataSupport.order("takePicTime").find(Photos.class);

        System.out.println("查询size="+ photoses.size()+" imgURL="+photoses.get(0).getImgUrl()+"imgName="+photoses.get(0).getImgName()+"拍照时间="+photoses.get(0).getTakePicTime());

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_camera);

        initView();
        //初始化popupwindow
        initPopupWindow();

        //SQLiteDatabase db = Connector.getDatabase();


    }

   Runnable runnable=new Runnable() {
       @Override
       public void run() {
           MyApplication.getmMainThreadHandler().postDelayed(this,3000);
           mCamera.autoFocus(null);//自动对焦
       }
   };

    private void initView() {
        tv_more = (TextView) findViewById(R.id.tv_more);
        tv_more.setOnClickListener(this);
        rl_bottom= (RelativeLayout) findViewById(R.id.rl_bottom);

        mPreview = (SurfaceView) findViewById(R.id.preview);
        shutter = (ImageButton) findViewById(R.id.shutter);
        tv_date = (TextView) findViewById(R.id.tv_date);
        iv_preview = (ImageView) findViewById(R.id.iv_preview);
        iv_preview.setOnClickListener(this);
        tv_date.setText(CameraUtil.getDateString());
        mHolder = mPreview.getHolder();
        mHolder.addCallback(this);
        mPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.autoFocus(null);
            }
        });


        mHolder.setKeepScreenOn(true);//设置屏幕敞亮

    }

    /**
     * 快门
     * @param view
     */
    public void shutter(View view) {
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPictureFormat(ImageFormat.JPEG);

//        parameters.setPreviewSize(1280,720);
        List<Camera.Size> pictureSizes = parameters.getSupportedPictureSizes();

       // parameters.setPictureSize(1280,720);
        parameters.setPictureSize(pictureSizes.get(pictureSizes.size()-4).width,pictureSizes.get(pictureSizes.size()-4).height);
        parameters.setRotation(90);
        parameters.setFlashMode(Camera.Parameters.FOCUS_MODE_AUTO);

//        mCamera.autoFocus(new Camera.AutoFocusCallback() {
//            @Override
//            public void onAutoFocus(boolean success, Camera camera) {
//                if (success) {
//                    //mCamera.takePicture(null,null,pictureCallback);
//                    mCamera.cancelAutoFocus();
//                }
//            }
//        });
        mCamera.setParameters(parameters);
        mCamera.takePicture(null, null, pictureCallback);

       // System.out.println("===相机参数=="+mCamera.getParameters().flatten().toString());
//        for (int i = 0; i < pictureSizes.size(); i++) {
//            System.out.println("相机参数"+ pictureSizes.get(i).width + "x" + pictureSizes.get(i).height);
//        }
    }

    /**
     * 保存
     * @param v
     */
    public void save(View v) {
        Intent intent = new Intent(this, DisplayActivity.class);
        imageFile.getAbsoluteFile();
        intent.putExtra("PHOTO", imageFile.getAbsolutePath());
        startActivity(intent);
    }

    public Camera getCamera(){
        Camera camera =  Camera.open();

        return camera;
    }
    public void releaseCameraAndPreview(){
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera=null;
        }

    }

    public void setStartPreview(Camera camera, SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera==null){
            mCamera=getCamera();
            if (mHolder != null) {
                setStartPreview(mCamera,mHolder);
            }
        }

        int staffType = getIntent().getIntExtra("staffType", 1);
        if (staffType==2){//乡镇负责人进入补拍
            tv_more.setVisibility(View.GONE);
        }

        MyApplication.getmMainThreadHandler().post(runnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCameraAndPreview();

        MyApplication.getmMainThreadHandler().removeCallbacks(runnable);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setStartPreview(mCamera, mHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mCamera!=null){
            mCamera.stopPreview();
        }
        setStartPreview(mCamera, mHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCameraAndPreview();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_more://更多
                {
                    showPopupWindow();
                }
                break;
            case R.id.iv_preview://相册
                {
                    Intent intent = new Intent(CustomCameraActivity.this, PictureListActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.tv_alarm://紧急事件报警
            {
                Intent intent = new Intent(CustomCameraActivity.this, HomeActivity.class);
                startActivity(intent);
                pw.dismiss();
            }
            break;
            case R.id.tv_upload:
            {
               // Intent intent = new Intent(CustomCameraActivity.this, PhotoListActivity.class);
                Intent intent = new Intent(CustomCameraActivity.this, PictureListActivity.class);
                startActivity(intent);
                pw.dismiss();
            }
            break;
            case R.id.tv_notice:
            {
                Intent intent = new Intent(CustomCameraActivity.this, SuperiorNoticeActivity.class);
                startActivity(intent);
                pw.dismiss();
            }
            break;
            case R.id.tv_set:
            {
                Intent intent = new Intent(CustomCameraActivity.this, SettingActivity.class);
                startActivity(intent);
                pw.dismiss();
            }
            break;
            default:
                break;
        }
    }

    private void initPopupWindow() {

        sa = new ScaleAnimation(1, 1,
                0, 1,
                Animation.RELATIVE_TO_SELF, 1,
                Animation.RELATIVE_TO_SELF, 0);
        sa.setDuration(200);
        popupView = View.inflate(CustomCameraActivity.this,R.layout.popupwidow_add, null);

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
