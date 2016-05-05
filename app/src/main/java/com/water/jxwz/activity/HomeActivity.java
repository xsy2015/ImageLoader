package com.water.jxwz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.water.jxwz.R;
import com.water.jxwz.base.BaseActivity;

/**
 * Created by ${xsy} on 2016/4/19.
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener{

    private ImageView iv_officer_manager;
    private ImageView iv_notice_manager;
    private ImageView iv_reshoot;
    private TextView tv_emergency;
    private TextView tv_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
    }

    private void initView() {
        iv_officer_manager = (ImageView) findViewById(R.id.iv_officer_manager);
        iv_notice_manager = (ImageView) findViewById(R.id.iv_notice_manager);
        iv_reshoot = (ImageView) findViewById(R.id.iv_reshoot);
        tv_emergency = (TextView) findViewById(R.id.tv_emergency);
        tv_setting = (TextView) findViewById(R.id.tv_setting);

        iv_officer_manager.setOnClickListener(this);
        iv_notice_manager.setOnClickListener(this);
        iv_reshoot.setOnClickListener(this);
        tv_emergency.setOnClickListener(this);
        tv_setting.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_officer_manager ://巡防员管理
            {
                Intent intent = new Intent(HomeActivity.this, OfficerManagerActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.iv_notice_manager ://通知管理
            {
                Intent intent = new Intent(HomeActivity.this, SuperiorNoticeActivity.class);
                intent.putExtra("staffType",2);
                startActivity(intent);
            }
            break;
            case R.id.iv_reshoot ://补拍
            {
                Intent intent = new Intent(HomeActivity.this, CustomCameraActivity.class);
                intent.putExtra("staffType",2);
                startActivity(intent);
            }
            break;
            case R.id.tv_emergency ://紧急事件
            {
                Intent intent = new Intent(HomeActivity.this, EmergencyActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.tv_setting ://设置
            {
                Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                intent.putExtra("staffType",2);
                startActivity(intent);
            }
            break;
            default:
                break;
        }
    }
}
