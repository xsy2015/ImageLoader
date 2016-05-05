package com.water.jxwz.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.water.jxwz.R;
import com.water.jxwz.utils.ToastCustom;

/**
 * Created by ${xsy} on 2016/4/13.
 */
public class BaseActivity extends FragmentActivity {

    protected TextView title_left;
    protected TextView title_center;
    protected TextView title_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showToast(Context context, String content){

        ToastCustom.makeText(context, content, 1).show();
    }

    public void initTitle(){
        title_left = (TextView) findViewById(R.id.title_left);
        title_center = (TextView) findViewById(R.id.title_center);
        title_right = (TextView) findViewById(R.id.title_right);

        title_left.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.R.anim.slide_in_left,
                        android.R.anim.slide_out_right);
            }
        });
    }
}
