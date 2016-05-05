package com.water.jxwz.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.water.jxwz.R;
import com.water.jxwz.base.BaseActivity;
import com.water.jxwz.customview.OpenDialog;

/**
 * Created by ${xsy} on 2016/4/20.
 */
public class OfficerDetailsActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_name;
    private TextView tv_address;
    private TextView tv_phone;
    private ImageView iv_phone;
    private TextView tv_kaoqing;
    private TextView tv_river_num;
    private TextView tv_start;
    private TextView tv_end;

    public static final int CODE_FOR_CALL_PHONE_PERMISSION = 100;
    private String servicePhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officer_details);
        initView();
    }

    private void initView() {
        initTitle();
        title_center.setText(R.string.officer_details);

        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        iv_phone = (ImageView) findViewById(R.id.iv_phone);
        tv_kaoqing = (TextView) findViewById(R.id.tv_kaoqing);
        tv_river_num = (TextView) findViewById(R.id.tv_river_num);
        tv_start = (TextView) findViewById(R.id.tv_start);
        tv_end = (TextView) findViewById(R.id.tv_end);

        iv_phone.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_phone:

                callPhone();

                break;
            default:
                break;
        }
    }

    private void callPhone() {

        servicePhone = "15811808186";

        OpenDialog.getInstance().showTwoBtnListenerDialog(
                OfficerDetailsActivity.this,
                servicePhone,
                getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                }, getResources().getString(R.string.call),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        //使用兼容库就无需判断系统版本
                        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.CALL_PHONE);
                        if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {

                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + servicePhone));
                            startActivity(intent);
                        }
                        //需要弹出dialog让用户手动赋予权限
                        else {
                            ActivityCompat.requestPermissions(OfficerDetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, CODE_FOR_CALL_PHONE_PERMISSION);
                        }

                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_FOR_CALL_PHONE_PERMISSION){
            if (permissions[0].equals(Manifest.permission.CALL_PHONE)&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //用户同意拨打电话
                Intent intent= new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + servicePhone));
                startActivity(intent);

            }else{
                //用户不同意，向用户展示该权限作用
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setMessage("拨打电话需要赋予权限，不开启将无法正常工作！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).create();
                    dialog.show();
                    return;
                }
                finish();
            }
        }
    }
}
