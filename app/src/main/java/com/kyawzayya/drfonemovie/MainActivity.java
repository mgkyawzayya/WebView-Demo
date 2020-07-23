package com.kyawzayya.drfonemovie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String ShowOrHideWebViewInitialUse = "show";

    private WebView webview;
    private ProgressBar spinner;
    SwipeRefreshLayout mySwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!DetectConnection.checkInternetConnection(this)) {
            Toast.makeText(getApplicationContext(), "No Internet!", Toast.LENGTH_LONG).show();
        } else {

            webview =(WebView)findViewById(R.id.webView);

        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        webview.setWebViewClient(new CustomWebViewClient());

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        webview.loadUrl("http://159.65.6.116");


        }


    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webview.canGoBack()) {
                        webview.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    private class CustomWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView webview, String url, Bitmap favicon) {
            if (ShowOrHideWebViewInitialUse.equals("show")) {
                webview.setVisibility(webview.INVISIBLE);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                startActivity(intent);
                return true;
            }
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            ShowOrHideWebViewInitialUse = "hide";
            spinner.setVisibility(View.GONE);

            view.setVisibility(webview.VISIBLE);
            super.onPageFinished(view, url);

        }

        public void onProgressChanged(WebView view, int newProgress){
            // Update the progress bar with page loading progress
            spinner.setProgress(newProgress);
            if(newProgress == 100){
                // Hide the progressbar
                spinner.setVisibility(View.GONE);
            }
        }

    }
}