package com.water.jxwz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.water.jxwz.R;
import com.water.jxwz.base.BaseActivity;

/**
 * Created by ${xsy} on 2016/4/14.
 */
public class SettingActivity extends BaseActivity {

    private LinearLayout ll_modify_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {

        initTitle();
        title_center.setText(R.string.setting);

        int staffType = getIntent().getIntExtra("staffType", 0);

        ll_modify_pwd = (LinearLayout) findViewById(R.id.ll_modify_pwd);

        if (staffType==2){//巡防员
            ll_modify_pwd.setVisibility(View.VISIBLE);
        }


    }
}
