package com.wyy.myhealth.ui.setting;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.wyy.myhealth.R;
import com.wyy.myhealth.ui.baseactivity.BaseActivity;
import com.wyy.myhealth.ui.baseactivity.interfacs.ActivityInterface;

public class FunctionIntroActivity extends BaseActivity implements
		ActivityInterface {

	private static final String URL = "http://gn.weidata.com.cn/";

	private WebView webView;

	private ProgressBar loadBar;

	@Override
	protected void onInitActionBar() {
		// TODO Auto-generated method stub
		super.onInitActionBar();
		getSupportActionBar().setTitle(R.string.function_intro);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_func_intro);
		initView();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		webView = (WebView) findViewById(R.id.web_page);
		loadBar = (ProgressBar) findViewById(R.id.web_progressBar);
		initData();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		webView.loadUrl(URL);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.getSettings().setJavaScriptEnabled(true);

		webView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				super.onProgressChanged(view, newProgress);
				loadBar.setProgress(newProgress);
				if (newProgress == 100) {
					loadBar.setVisibility(View.GONE);
				} else {
					loadBar.setVisibility(View.VISIBLE);
				}
			}

		});

		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				return super.shouldOverrideUrlLoading(view, url);

			}

		});
	}

}
