package com.github.alenastan.clientvkontakte;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.github.alenastan.clientvkontakte.auth.VkOAuthHelper;

/**
 * Created by lena on 25.01.2015.
 */
public class VkLoginActivity extends ActionBarActivity implements VkOAuthHelper.Callbacks {

    private static final String TAG = VkLoginActivity.class.getSimpleName();

    private WebView mWebView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vk_login);
        getSupportActionBar().hide();
        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.setWebViewClient(new VkWebViewClient());
        mWebView.loadUrl(Api.AUTORIZATION_URL);

    }

    @Override
    public void onError(Exception e) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(e.getMessage())
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onSuccess(String accessToken) {
        Intent intent = getIntent();
        intent.putExtra(Session.ACCESS_TOKEN,accessToken);
        setResult(RESULT_OK,intent);
        finish();
    }

    private class VkWebViewClient extends WebViewClient {

        public VkWebViewClient() {
            super();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            showProgress();
            view.setVisibility(View.INVISIBLE);
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
           if (VkOAuthHelper.parseUrl(VkLoginActivity.this, url, VkLoginActivity.this)) {
                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                view.loadUrl(url);
                return false;
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            showProgress();
            view.setVisibility(View.VISIBLE);
            dismissProgress();

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.setVisibility(View.VISIBLE);
            dismissProgress();
            }

    }

    private void dismissProgress() {
        findViewById(android.R.id.progress).setVisibility(View.GONE);
    }

    private void showProgress() {
        findViewById(android.R.id.progress).setVisibility(View.VISIBLE);
    }

}