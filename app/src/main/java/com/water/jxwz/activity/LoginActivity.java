package com.water.jxwz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.water.jxwz.R;
import com.water.jxwz.base.BaseActivity;

/**
 * Created by ${xsy} on 2016/4/14.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private EditText et_name;
    private EditText et_pwd;
    private TextView tv_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        initTitle();
        title_left.setVisibility(View.GONE);
        title_center.setText("登录");

        et_name = (EditText) findViewById(R.id.et_name);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        tv_login = (TextView) findViewById(R.id.tv_login);

        tv_login.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_login://登录
            {
                Intent intent = new Intent(LoginActivity.this, CustomCameraActivity.class);
                startActivity(intent);
            }
                break;
            default:
                break;
        }
    }
}
