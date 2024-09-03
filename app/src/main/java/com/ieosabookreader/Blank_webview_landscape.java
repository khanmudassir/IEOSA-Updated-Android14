package com.ieosabookreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Blank_webview_landscape extends AppCompatActivity {

    //private XWalkView xWalkWebView_l;
    private WebView xWalkWebView_l;

    public Blank_webview_landscape oBlankwebview_l = null;
    JavaScriptInterface_blank_webview_L oJavaScriptInterfaceblankwebview_L;
    String FromActivity = "";

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        oBlankwebview_l = this;
        setContentView(R.layout.blank_webview_landscape);
        //oJavaScriptInterfaceblankwebview_L = new JavaScriptInterface_blank_webview_L(oBlankwebview_l);
        Bundle oBundle = getIntent().getExtras();
        if (oBundle != null && oBundle.getString("Activity") != null)
        {
            FromActivity = oBundle.getString("Activity");
        }
        //xWalkWebView_l = (XWalkView) findViewById(R.id.xwalkview_l);
        // xWalkWebView_l = (WebView) findViewById(R.id.xwalkview_l);
        // xWalkWebView_l.getSettings().setJavaScriptEnabled(true);
        // xWalkWebView_l.setBackgroundColor(Color.TRANSPARENT);
        // xWalkWebView_l.clearCache(true);
        // xWalkWebView_l.addJavascriptInterface(oJavaScriptInterfaceblankwebview_L,
        // "AndroidCall");
        // String blank_htmlpath = "file:///android_asset/Viewport_L.html";
        // xWalkWebView_l.loadUrl(blank_htmlpath, null);

        xWalkWebView_l = (WebView) findViewById(R.id.xwalkview_l);
        xWalkWebView_l.getSettings().setAllowFileAccess(true);
        //xWalkWebView_l.getSettings().setAllowContentAccess(true);
        //xWalkWebView_l.getSettings().setAllowUniversalAccessFromFileURLs(true);
        //xWalkWebView_l.getSettings().setUseWideViewPort(true);
        //xWalkWebView_l.getSettings().setDomStorageEnabled(true);
        xWalkWebView_l.setWebChromeClient(new WebChromeClient());
        xWalkWebView_l.getSettings().setJavaScriptEnabled(true);
        //xWalkWebView_l.setWebContentsDebuggingEnabled(true);
        oJavaScriptInterfaceblankwebview_L = new JavaScriptInterface_blank_webview_L(oBlankwebview_l);
        xWalkWebView_l.addJavascriptInterface(oJavaScriptInterfaceblankwebview_L, "AndroidCall");
        xWalkWebView_l.setWebViewClient(new WebViewClient());
        //xWalkWebView.setResourceClient(new XWalkResourceClientBase(xWalkWebView));
        //xWalkWebView.getSettings().setJavaScriptEnabled(true);
        xWalkWebView_l.setBackgroundColor(Color.TRANSPARENT);
        xWalkWebView_l.clearCache(true);

        String blank_htmlpath = "file:///android_asset/Viewport_L.html";
        xWalkWebView_l.loadUrl(blank_htmlpath, null);

    }

    public void Redirect_SplashScreen()
    {
        if (FromActivity.equalsIgnoreCase("Registration"))
        {
            if (!ConnectionDetector.IsInternetAvailable(getApplicationContext()))
            {
                Intent oIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(oIntent);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_left);
                finish();
            }
            else
            {
//				Intent intent = new Intent(getApplicationContext(), StoreActivity.class);
//				startActivity(intent);
//				overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_left);
//				finish();
                Intent intent = new Intent(getApplicationContext(), LandingPage_Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_left);
                finish();
            }
        }
        else
        {
            if (!FromActivity.equalsIgnoreCase("SplashActivity"))
            {
                Intent oIntent = new Intent(getApplicationContext(), SplashScreen.class);
                startActivity(oIntent);
                finish();
            }
            else
            {
                Intent oIntent = new Intent(getApplicationContext(), SplashScreen.class);
                startActivity(oIntent);
                finish();
            }
        }

    }

    public void setDimention(float DeviceWidth, float DeviceHeight)
    {
        FlipickConfig.SaveDeviceWidth_L(getApplicationContext(), DeviceWidth);
        FlipickConfig.SaveDeviceheight_L(getApplicationContext(), DeviceHeight);
        Redirect_SplashScreen();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (xWalkWebView_l != null)
        {

            xWalkWebView_l.pauseTimers();
            //xWalkWebView_l.onHide();

            xWalkWebView_l.onPause();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (xWalkWebView_l != null)
        {
            xWalkWebView_l.resumeTimers();
            //xWalkWebView_l.onShow();

            xWalkWebView_l.onResume();
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (xWalkWebView_l != null)
        {
            xWalkWebView_l.onPause();
            //xWalkWebView_l.onDestroy();
        }
    }
}