package com.ieosabookreader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import java.io.File;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.view.Display;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
public class BrowserFragmentImagePopUp extends AppCompatActivity {

    WebView imgpopup_browser;

    @SuppressLint("SetJavaScriptEnabled")
    @JavascriptInterface
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browserfragmentimagepopup);

        Bundle oBundle = getIntent().getExtras();
        String ImagePath = oBundle.getString("ImagePath");

        imgpopup_browser = (WebView) findViewById(R.id.fragment_browser_imagepopup);
        imgpopup_browser.setWebChromeClient(new WebChromeClient());

        imgpopup_browser.getSettings().setJavaScriptEnabled(true);
        imgpopup_browser.getSettings().setBuiltInZoomControls(true);
        imgpopup_browser.setBackgroundColor(0);

        imgpopup_browser.getSettings().setUseWideViewPort(true);

        File oFile = new File(ImagePath);
        String path = oFile.getParent();
        path = path.replace("file:/", "file:///");
        String Name = oFile.getName();
        // ///////////////////////////////

        Display display = getWindowManager().getDefaultDisplay();
        float DeviceWidth = display.getWidth();
        float DeviceHeight = display.getHeight();
        DeviceWidth = DeviceWidth - DeviceWidth * 1 / 100;
        DeviceHeight = DeviceHeight - DeviceHeight * 1 / 100;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(ImagePath.replace("file://", ""), options);
        float ImageHeight = options.outHeight;
        float ImageWidth = options.outWidth;

        float ARH = DeviceHeight / ImageHeight;
        float ARW = DeviceWidth / ImageWidth;
        float ARFinal = 1;

        if (ARH < ARW) ARFinal = ARH;
        else ARFinal = ARW;

        ImageHeight = (ImageHeight * ARFinal);
        ImageWidth = (ImageWidth * ARFinal);

        float Left = (DeviceWidth - ImageWidth) / 2;
        float Top = (DeviceHeight - ImageHeight) / 2;

        String HTMLContent = "";
        HTMLContent = "<img style='position:absolute;left:" + Left + "px;top:" + Top + "px'  width='" + ImageWidth + "px' height='" + ImageHeight + "px'  src='" + Name + "'>";

        imgpopup_browser.loadDataWithBaseURL(path + "/", "<html><head><meta name='viewport' content='width=device-width, target-densitydpi=device-dpi, user-scalable=yes'> </meta></head><body>"
                + HTMLContent + "</body></html>", "text/html", "utf-8", "");

        ImageView imgViewPopupClose = (ImageView) findViewById(R.id.btn_imgpopuclosebrowserfrag);

        imgViewPopupClose.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                finish();
                overridePendingTransition(0, R.anim.slide_right_out);
            }
        });
    }


}