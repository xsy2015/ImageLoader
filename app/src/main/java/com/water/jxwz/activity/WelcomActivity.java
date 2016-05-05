package com.water.jxwz.activity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import com.water.jxwz.R;
import com.water.jxwz.base.BaseActivity;
import com.water.jxwz.utils.SharedInfoUtils;

/**
 * Created by ${xsy} on 2016/4/20.
 */
public class WelcomActivity extends BaseActivity implements Animation.AnimationListener{

    private LinearLayout ll_root;
    private static final long	DURATION		= 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcom);

        initView();
    }

    private void initView() {

        ll_root = (LinearLayout) findViewById(R.id.ll_root);
        //  透明度
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(DURATION);
        alpha.setFillAfter(true);

        ll_root.setAnimation(alpha);
        ll_root.setLayoutAnimationListener(this);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (SharedInfoUtils.getUserName()!=null&&SharedInfoUtils.getIsAutoLogin()){//已登录

        }else {//未登录

        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
