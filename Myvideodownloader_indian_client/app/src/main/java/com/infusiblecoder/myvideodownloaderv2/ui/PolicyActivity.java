package com.infusiblecoder.myvideodownloaderv2.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.infusiblecoder.myvideodownloaderv2.R;

public class PolicyActivity extends AppCompatActivity {


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_policy);
        findViewById(R.id.img_back).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                PolicyActivity.this.onBackPressed();
            }
        });
        WebView webView = findViewById(R.id.web_view);
        webView.loadUrl(getResources().getString(R.string.policy_privacy_url));
        webView.setWebViewClient(new MyBrowser());
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(getResources().getString(R.string.policy_privacy_url));
    }

    private class MyBrowser extends WebViewClient {
        private MyBrowser() {
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            webView.loadUrl(str);
            return true;
        }
    }
}
