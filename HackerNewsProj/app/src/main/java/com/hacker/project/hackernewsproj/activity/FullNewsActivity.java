package com.hacker.project.hackernewsproj.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.hacker.project.hackernewsproj.R;

/**
 * Created by Navin on 17/05/16.
 */
public class FullNewsActivity extends BaseActivity {
    WebView webview;
    final Activity activity = this;
    String webURL;
    ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.full_news_activity);
        webURL = getIntent().getStringExtra("URL");
        webview = (WebView) findViewById(R.id.web_view);

        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                activity.setProgress(progress * 1000);
            }
        });
        webview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            }
        });

        setupWebView();
    }

    private void setupWebView() {
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webview.setWebViewClient(
                new SSLTolerentWebViewClient()
        );
        webview.loadUrl(webURL);
    }


    private class SSLTolerentWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            dialog.dismiss();
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            dialog = new ProgressDialog(FullNewsActivity.this);
            dialog.show();
            super.onPageStarted(view, url, favicon);

        }

    }
}
