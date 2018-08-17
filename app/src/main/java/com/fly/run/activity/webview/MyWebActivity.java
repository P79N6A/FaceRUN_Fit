package com.fly.run.activity.webview;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fly.run.R;
import com.fly.run.activity.base.BaseUIActivity;
import com.fly.run.view.actionbar.CommonActionBar;

/**
 * MyWebActivity
 */
public class MyWebActivity extends BaseUIActivity {
    private CommonActionBar actionBar;
    private WebView webView;
    public static final String KEY_DATA = "data";
    private String data;
    private String mStrTitle;

    private void initActionBar() {
        actionBar = (CommonActionBar) findViewById(R.id.common_action_bar);
        actionBar.setLayout_actionbar_content_visiable(View.GONE);
        actionBar.setActionLeftIconListenr(-1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myweb);
        initActionBar();
        initView();
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(true);
        // 设置setWebChromeClient对象
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mStrTitle = title;
                actionBar.setActionTitle(title);
            }

        });

        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if (!TextUtils.isEmpty(url)) {
                    if (url.startsWith("http://") || url.startsWith("https://"))
                        view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dismissProgressDialog();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showProgreessDialog();
            }

//            @Override
//            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
//                final AlertDialog.Builder builder = new AlertDialog.Builder(QRCodeWebActivity.this);
//                builder.setMessage("error ssl cert invalid");
//                builder.setPositiveButton(getResources().getString(R.string.txt_edit_continue), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        handler.proceed();
//                    }
//                });
//                builder.setNegativeButton(getResources().getString(R.string.txt_cancel), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        handler.cancel();
//                    }
//                });
//                final AlertDialog dialog = builder.create();
//                dialog.show();
//            }
        });

//        data = getIntent().getStringExtra(KEY_DATA);
        data = "http://120.78.81.9/test/food_main";
        if (TextUtils.isEmpty(data)) {
            data = "";
        }
        if (data.startsWith("http://") || data.startsWith("https://")) {
            webView.loadUrl(data);
        } else {
            webView.loadDataWithBaseURL("fake://not/needed", data, "text/html", "utf-8", "");
        }
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
