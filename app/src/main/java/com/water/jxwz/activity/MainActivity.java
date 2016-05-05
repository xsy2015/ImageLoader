package com.water.jxwz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.water.jxwz.base.BaseActivity;
import com.water.jxwz.base.Constant;
import com.water.jxwz.utils.PhotoUtil;
import com.water.jxwz.utils.StorageUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ${xsy} on 2016/4/13.
 */
public class MainActivity extends BaseActivity{
    private File[] files = new File[8];//拍照生成的文件
    private File tempFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        creatFile();
        tempFile = files[1];
        PhotoUtil.camera(MainActivity.this, tempFile);
    }

    private void creatFile() {
        if (files[0] != null) {
            return;
        }
        try {
            long curTime = System.currentTimeMillis();
            for (int i = 0; i < files.length; i++) {
                files[i] = new File(StorageUtils.getExternalCacheDir(this), curTime + i + ".png");
                files[i].createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.PHOTO_REQUEST_CAMERA:{ // 拍照
                    ArrayList<String> allPath = new ArrayList<String>();
                    allPath.add(tempFile.getAbsolutePath());
                    //compressPhoto(allPath);
                    break;
                }
                case Constant.PHOTO_REQUEST_GALLERY:{ // 从相册获取
                    ArrayList<String> allPath = data.getStringArrayListExtra("all_path");
                   // compressPhoto(allPath);
                    break;
                }
                default:
                    break;
            }
        }
    }
}
