package com.water.jxwz.activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.water.jxwz.R;
import com.water.jxwz.base.BaseActivity;


/**
 * @项目名: 	YouJia
 * @包名:	com.hhxh.youjia.activity
 * @类名:	DiscountEventDetailActivity
 * @创建者:	xsy
 * @创建时间:	2015-12-12	上午9:47:46 
 * @描述:	通知详情
 */
public class ShowNoticeDetailActivity extends BaseActivity
{
    private WebView	mWv;
	private ProgressBar	mPb;
	private String	url;
	private String	titleName;

	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	
    	super.onCreate(savedInstanceState);
    	
    	setContentView(R.layout.activity_web);
    	initView();
    	initWebView();
    }

	private void initView()
	{
		Intent intent = getIntent();
		url = intent.getStringExtra("url");
		titleName=intent.getStringExtra("title");
		
		initTitle();
		title_center.setText(titleName);
		
		mWv = (WebView) findViewById(R.id.wv);
		mPb = (ProgressBar) findViewById(R.id.pb);
		
	}

	private void initWebView()
	{
		

		// webView的设置
				WebSettings settings = mWv.getSettings();// 获取设置
				settings.setJavaScriptEnabled(true);// 设置js可用
				settings.setBuiltInZoomControls(true);// 放大缩小的控件
				settings.setUseWideViewPort(true);// 双击放大或缩小

				// 高级api
				mWv.setWebViewClient(new WebViewClient() {
					@Override
					public void onPageStarted(WebView view, String url, Bitmap favicon) {
						mPb.setVisibility(View.VISIBLE);
					}

					@Override
					public void onPageFinished(WebView view, String url) {

						mPb.setVisibility(View.GONE);
					}
				});

				mWv.setWebChromeClient(new WebChromeClient() {
					@Override
					public void onProgressChanged(WebView view, int newProgress) {

					}
				});

				mWv.loadUrl(url);

		
	}
}
