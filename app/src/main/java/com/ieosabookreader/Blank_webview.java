package com.ieosabookreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Blank_webview extends AppCompatActivity {
    WebView xWalkWebView;
    int orientation;
    int currentapiVersion;
    public Blank_webview oBlankwebview = null;
    JavaScriptInterface_blank_webview oJavaScriptInterfaceblankwebview;
    String FromActivity = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        oBlankwebview = this;
        setContentView(R.layout.blank_webview);
        Bundle oBundle = getIntent().getExtras();
        if (oBundle != null && oBundle.getString("Activity") != null)
        {
            FromActivity = oBundle.getString("Activity");
        }
        xWalkWebView = (WebView) findViewById(R.id.xwalkview);
        xWalkWebView.getSettings().setAllowFileAccess(true);
        xWalkWebView.getSettings().setJavaScriptEnabled(true);
        oJavaScriptInterfaceblankwebview = new JavaScriptInterface_blank_webview(oBlankwebview);
        xWalkWebView.addJavascriptInterface(oJavaScriptInterfaceblankwebview, "AndroidCall");
        xWalkWebView.setWebViewClient(new WebViewClient());
        xWalkWebView.setBackgroundColor(Color.TRANSPARENT);
        xWalkWebView.clearCache(true);

        String blank_htmlpath = "file:///android_asset/Viewport.html";
        xWalkWebView.loadUrl("file:///android_asset/Viewport.html");// (blank_htmlpath,
        // null);
    }

    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
        {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }
    }

    /*
     * class XWalkResourceClientBase extends XWalkResourceClient {
     *
     * public XWalkResourceClientBase(XWalkView mXWalkView) { super(mXWalkView);
     * }
     *
     * public void onLoadStarted(XWalkView view, String url) { // TODO //
     * Auto-generated method stub
     *
     * super.onLoadStarted(view, url); }
     *
     * public void onLoadFinished(XWalkView view, String url) {
     * super.onLoadFinished(view, url);
     *
     * }
     *
     * @Override public boolean shouldOverrideUrlLoading(XWalkView view, String
     * url) {
     *
     * return super.shouldOverrideUrlLoading(xWalkWebView, url); } }
     */

    public void Redirect_LandscapeWebview()
    {
        FlipickError.DisplayErrorAsToast(this, "Checking Orientation");
        Intent oIntent = new Intent(getApplicationContext(), Blank_webview_landscape.class);
        Bundle bundle = new Bundle();
        bundle.putString("Activity", FromActivity);
        oIntent.putExtras(bundle);
        startActivity(oIntent);
        finish();
    }

    public void setDimention(float DeviceWidth, float DeviceHeight)
    {
        FlipickConfig.SaveDeviceWidth(getApplicationContext(), DeviceWidth);
        FlipickConfig.SaveDeviceheight(getApplicationContext(), DeviceHeight);
        String TAG = " DISPALY INFO : ";
        Display display = getWindowManager().getDefaultDisplay();
        String displayName = display.getName(); // minSdkVersion=17+
        Log.i(TAG, "displayName  = " + displayName);

        // display size in pixels
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.i(TAG, "width        = " + width);
        Log.i(TAG, "height       = " + height);

        // pixels
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int heightPixels = metrics.heightPixels;
        int widthPixels = metrics.widthPixels;
        int densityDpi = metrics.densityDpi;
        float xdpi = metrics.xdpi;
        float ydpi = metrics.ydpi;
        Log.i(TAG, "widthPixels  = " + widthPixels);
        Log.i(TAG, "heightPixels = " + heightPixels);
        Log.i(TAG, "densityDpi   = " + densityDpi);
        Log.i(TAG, "xdpi         = " + xdpi);
        Log.i(TAG, "ydpi         = " + ydpi);
        int screenHeight = metrics.heightPixels;
        int screenWidth = metrics.widthPixels;
        double y = Math.pow(screenHeight / metrics.xdpi, 2);
        double x = Math.pow(screenWidth / metrics.xdpi, 2);
        // deprecated

        Log.i(TAG, "screenHeight = " + screenHeight);
        Log.i(TAG, "screenWidth  = " + screenWidth);

        // orientation (either ORIENTATION_LANDSCAPE, ORIENTATION_PORTRAIT)
        int orientation = getResources().getConfiguration().orientation;
        Log.i(TAG, "orientation  = " + orientation);
        Redirect_LandscapeWebview();
    }

    @Override
    public void onPause()
    {
        super.onPause();

        // if (xWalkWebView != null)
        // {
        // xWalkWebView.pauseTimers();
        // xWalkWebView.onHide();
        // }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        // if (xWalkWebView != null)
        // {
        // xWalkWebView.resumeTimers();
        // xWalkWebView.onShow();
        // }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        // if (xWalkWebView != null)
        // {
        // xWalkWebView.onDestroy();
        // }
    }
}