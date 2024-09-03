package com.ieosabookreader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import org.xwalk.core.JavascriptInterface;
import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkView;

public class BookryWGT_Activity extends AppCompatActivity {

    Context context;
    XWalkView BookryXwalkwebview;
    WebView Chromewebview;
    ImageView img_close;
    String URL = "";
    // String Type = "";
    public BookryWGT_Activity oBookryWGT_Activity = null;

    float bookryHeight, bookryWidth;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (FlipickStaticData.currentapiVersion < 21)
        {
            setContentView(R.layout.bookrywgt);
        }
        else
        {
            setContentView(R.layout.bookrywgt_chromewebview);
        }

        oBookryWGT_Activity = this;
        context = this;

        if (FlipickStaticData.currentapiVersion < 21)
        {
            SettingUI();
        }
        else
        {
            SeetingChromiumWebview();
        }

    }

    public void OpenUrlinChrome(String urlopen)
    {
        // Intent browserIntent = new Intent(Intent.ACTION_VIEW,
        // Uri.parse(urlopen));

        try
        {
            File oFile = new File(urlopen);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromFile(oFile));
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            startActivity(intent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            // TODO: handle exception
        }
        // startActivity(browserIntent);

    }

    private void SeetingChromiumWebview()
    {
        Bundle oBundle = getIntent().getExtras();
        if (oBundle != null)
        {
            if (oBundle.getString("Path") != null)
            {
                URL = oBundle.getString("Path");
            }
            // if (oBundle.getString("Type") != null)
            // {
            // Type = oBundle.getString("Type");
            // }
        }

        Chromewebview = (WebView) findViewById(R.id.bookry_xwalkview);
        Chromewebview.getSettings().setAllowFileAccess(true);
        Chromewebview.getSettings().setJavaScriptEnabled(true);
        Chromewebview.getSettings().setAllowContentAccess(true);
        Chromewebview.getSettings().setAllowUniversalAccessFromFileURLs(true);
        Chromewebview.getSettings().setUseWideViewPort(true);
        Chromewebview.getSettings().setLoadWithOverviewMode(true);
        Chromewebview.setWebContentsDebuggingEnabled(true);
        // Chromewebview.setWebViewClient(new myWebClient());
        Chromewebview.getSettings().setDomStorageEnabled(true);
        Chromewebview.getSettings().setBuiltInZoomControls(true);
        Chromewebview.getSettings().setSupportZoom(true);
        Chromewebview.setInitialScale(1);
        // Chromewebview.setWebChromeClient(new WebChromeClient());
        final JavascriptInterface_Chromium bookryJavaScriptChromium = new JavascriptInterface_Chromium(this);
        Chromewebview.addJavascriptInterface(bookryJavaScriptChromium, "AndroidFunction");

        // BookryXwalkwebview.setResourceClient(new
        // XWalkResourceClientBase(BookryXwalkwebview));
        img_close = (ImageView) findViewById(R.id.img_close_bookry);

        img_close.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (Chromewebview != null)
                {
                    Chromewebview.destroy();
                }
                finish();

            }
        });

        File file = new File(URL);
        String Path = file.getAbsolutePath();
        String filePath = Path.substring(0, Path.lastIndexOf(File.separator));
        String Type = checkFileType(filePath);
        if (Type.equalsIgnoreCase("Bookry"))
        {
            // File file = new File(URL);
            // String Path = file.getAbsolutePath();
            // String filePath = Path.substring(0,
            // Path.lastIndexOf(File.separator));
            Chromewebview.loadUrl(FlipickStaticData.OEBPSPath + filePath + "/main.html");
            // String url = FlipickStaticData.OEBPSPath + filePath +
            // "/main1.html";
            // OpenUrlinChrome(FlipickStaticData.OEBPSPath + filePath +
            // "/main.html");
            // File isFileExists = new File(url);

            /*
             * if (!isFileExists.exists()) { // copyBookryOriginal();
             * setDimention(786.0f, 1024.0f); // loadBookryPage(); } else { //
             * copyBookryOriginal(); setDimention(786.0f, 1024.0f); //
             * loadBookryPage(); }
             */

            /*
             * if (isFileExists.exists()) { copyBookryOriginal();
             * loadBookryPage(); } else { setBookryWGT(); }
             */
        }
        else
        {
            setArticulate();
        }
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
        public void onPageFinished(WebView view, String url)
        {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        { // TODO Auto-generated method stub

            view.loadUrl(url);
            return false;

        }
    }

    private void SettingUI()
    {
        // context = this;

        Bundle oBundle = getIntent().getExtras();
        if (oBundle != null)
        {
            if (oBundle.getString("Path") != null)
            {
                URL = oBundle.getString("Path");
            }
            // if (oBundle.getString("Type") != null)
            // {
            // Type = oBundle.getString("Type");
            // }
        }

        BookryXwalkwebview = (XWalkView) findViewById(R.id.bookry_xwalkview);

        final JavascriptInterface_Bookry bookryJavaScript = new JavascriptInterface_Bookry(this);
        BookryXwalkwebview.addJavascriptInterface(bookryJavaScript, "AndroidFunction");

        BookryXwalkwebview.setResourceClient(new XWalkResourceClientBase(BookryXwalkwebview));
        img_close = (ImageView) findViewById(R.id.img_close_bookry);

        img_close.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (BookryXwalkwebview != null)
                {
                    BookryXwalkwebview.onDestroy();
                }
                finish();

            }
        });

        File file = new File(URL);
        String Path = file.getAbsolutePath();
        String filePath = Path.substring(0, Path.lastIndexOf(File.separator));
        String Type = checkFileType(filePath);
        if (Type.equalsIgnoreCase("Bookry"))
        {
            // File file = new File(URL);
            // String Path = file.getAbsolutePath();
            // String filePath = Path.substring(0,
            // Path.lastIndexOf(File.separator));
            String url = FlipickStaticData.OEBPSPath + filePath + "/main1.html";
            File isFileExists = new File(url);
            if (!isFileExists.exists())
            {
                copyBookryOriginal();
                loadBookryPage();
            }
            else
            {
                setBookryWGT();
            }
        }
        else
        {
            setArticulate();
        }

    }

    private String checkFileType(String filePath)
    {

        File folderPath = new File(FlipickStaticData.OEBPSPath + filePath + "/story_content");
        String type = "";
        if (folderPath.exists())
        {
            type = "Articulate";

        }
        else
        {
            type = "Bookry";
        }
        return type;

    }

    class XWalkResourceClientBase extends XWalkResourceClient
    {

        public XWalkResourceClientBase(XWalkView mXWalkView)
        {
            super(mXWalkView);
        }

        public void onLoadStarted(XWalkView view, String url)
        {
            // TODO Auto-generated method stub

            super.onLoadStarted(view, url);

        }

        public void onLoadFinished(XWalkView view, String url)
        {

            super.onLoadFinished(view, url);
            view.getNavigationHistory().clear();
        }

    }

    private void loadBookryPage()
    {
        File file = new File(URL);
        String Path = file.getAbsolutePath();
        String filePath = Path.substring(0, Path.lastIndexOf(File.separator));
        if (FlipickStaticData.currentapiVersion < 21)
        {
            BookryXwalkwebview.clearCache(true);
            BookryXwalkwebview.load("file:///" + FlipickStaticData.OEBPSPath + filePath + "/main1.html", null);
        }
        else
        {
            Chromewebview.clearCache(true);
            Chromewebview.loadUrl("file:///" + FlipickStaticData.OEBPSPath + filePath + "/main1.html", null);
        }

    }

    private void copyBookryOriginal()
    {

        File file = new File(URL);
        String Path = file.getAbsolutePath();
        String filename = file.getName();
        String filePath = Path.substring(0, Path.lastIndexOf(File.separator));
        String originalFile = FlipickStaticData.OEBPSPath + filePath + "/" + filename;// "/main.html";
        String copyFile = FlipickStaticData.OEBPSPath + filePath + "/main1.html";
        try
        {
            FlipickIO.CopyFile(originalFile, copyFile);

            File fileHtml = new File(copyFile);
            String HTMLData = FlipickIO.ReadHTMLFileText(fileHtml.getAbsolutePath());
            HTMLData = HTMLData.replace("<body>", "<body onload='getDimentions();'><div id='customdiv' style='position:absolute;left:0px;top:0px;width:" + (FlipickConfig.getDeviceWidth(context) - 20)
                    + ";height:" + (FlipickConfig.getDeviceHeight(context) - 20) + ";>'");

            HTMLData = HTMLData
                    .replace("</body>",
                            "<script language='javascript' type='text/javascript'> function getDimentions(){AndroidFunction.setDimention(document.body.clientWidth,document.body.clientHeight);} </script></div></body>");
            // HTMLData = HTMLData
            // .replace(
            // "</body>",
            // "<script language='javascript' type='text/javascript'> function GetDimentions(){setTimeout(function() {AndroidCall.setDimention(document.documentElement.clientWidth,document.documentElement.clientHeight);}, 5000);} </script></div></body>");
            FlipickIO.WriteHTMLFileText(fileHtml.getAbsolutePath(), HTMLData);

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void SetUrlToWebview(final String url)
    {
        if (FlipickStaticData.currentapiVersion < 21)
        {
            BookryXwalkwebview.clearCache(true);
            BookryXwalkwebview.load("file:///" + url, null);
        }
        else
        {
            // Chromewebview.clearCache(true);
            // OpenUrlinChrome("file:///" + url);
            Chromewebview.loadUrl("file:///" + url, null);
        }

    }

    public void setBookryWGT()
    {
        try
        {
            File file = new File(URL);
            String Path = file.getAbsolutePath();
            String FileName = file.getName();
            String filePath = Path.substring(0, Path.lastIndexOf(File.separator));

            // check if already exist bookrycontainer.html file.
            String url = FlipickStaticData.OEBPSPath + filePath + "/BookryContainer.html";
            File isFileExists = new File(url);
            createBookryContainerFile(filePath, FileName);

            /*
             * if (!isFileExists.exists()) { createBookryContainerFile(filePath,
             * FileName); } if (isFileExists.exists()) {
             * createBookryContainerFile(filePath, FileName); }
             */
            // set url to webview.
            String testurl = FlipickStaticData.OEBPSPath + filePath + "/BookryContainer.html";
            SetUrlToWebview(testurl);
            // OpenUrlinChrome("file:///" + testurl);
            // SetUrlToWebview(url);
        }
        catch (Exception e)
        {

            e.printStackTrace();
        }
    }

    public void setArticulate()
    {
        try
        {
            File file = new File(URL);
            String Path = file.getAbsolutePath();
            String FileName = file.getName();
            String filePath = Path.substring(0, Path.lastIndexOf(File.separator));
            // check if already exist bookrycontainer.html file.
            String url = FlipickStaticData.OEBPSPath + filePath + "/" + FileName;
            // set url to webview.
            SetUrlToWebview(url);
        }
        catch (Exception e)
        {

            e.printStackTrace();
        }
    }

    public void setDimention(float bookryWidth, float bookryHeight)
    {
        if (bookryWidth > 0 && bookryHeight > 0)
        {
            this.bookryWidth = bookryWidth;
            this.bookryHeight = bookryHeight;
        }
        else
        {
            this.bookryWidth = 400.0f;
            this.bookryHeight = 700.0f;
        }
        /*
         * if (FlipickStaticData.currentapiVersion < 21) {
         * BookryXwalkwebview.getNavigationHistory().clear(); } else { //
         * Chromewebview.clearHistory(); Chromewebview.loadUrl(""); }
         */
        runOnUiThread(new Runnable()
        {
            @JavascriptInterface
            public void run()
            {
                if (FlipickStaticData.currentapiVersion < 21)
                {
                    BookryXwalkwebview.getNavigationHistory().clear();
                }

                setBookryWGT();
            }
        });

    }

    private void createBookryContainerFile(String filePath, String filename) throws Exception
    {
        float jobhgt = bookryHeight;
        float jobwd = bookryWidth;

        float DeviceHeight = FlipickConfig.getDeviceHeight(context);
        float DeviceWidth = FlipickConfig.getDeviceWidth(context);

        float ARW = DeviceWidth / jobwd;
        float ARH = DeviceHeight / jobhgt;
        float ARFinal;// = DeviceHeight / jobhgt;

        if (ARW < ARH) ARFinal = ARW;
        else ARFinal = ARH;

        float Left = ((DeviceWidth - (jobwd * ARFinal)) / 2);
        float Top = ((DeviceHeight - (jobhgt * ARFinal)) / 2);

        String CssPortrait = "@media (orientation: portrait) {body > :first-child {position: absolute !important;left:" + Left + "px !important;top:" + Top + "px;width:" + jobwd + "px;height:"
                + jobhgt + "px !important;-webkit-transform-origin:0 0 !important;-webkit-transform:scale(" + ARFinal + ") !important;}}";

        String CssLandscape = GetJobReverseScaleCSS();

        String css = CssPortrait + CssLandscape;
        // String Html =
        // "<?xml version='1.0' encoding='UTF-8'?><html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:epub=\"http://www.idpf.org/2007/OEBPS\"><head><meta name='viewport' content='height=device-height, width=device-width,user-scalable=yes'></meta><meta http-equiv='default-style'content='text/html; charset=utf-8'></meta><style>"
        String Html = "<html><head><meta name='viewport' content='height=device-height, width=device-width,user-scalable=yes'></meta><meta http-equiv='default-style'content='text/html; charset=utf-8'></meta><style>"
                + css + "</style></head><body style='overflow-x:hidden;overflow-y:auto;'><iframe src='" + filename + "'/></body></html>";

        String content = Html;

        try
        {

            File file = new File(FlipickStaticData.OEBPSPath + filePath + "/BookryContainer.html");

            // If file does not exists, then create it
            file.createNewFile();
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
            /*
             * if (!file.exists()) { file.createNewFile(); FileWriter fw = new
             * FileWriter(file.getAbsoluteFile()); BufferedWriter bw = new
             * BufferedWriter(fw); bw.write(content); bw.close(); }
             */

        }
        catch (IOException e)
        {
            e.printStackTrace();

        }

    }

    private String GetJobReverseScaleCSS()
    {
        float jobWidth = bookryWidth;
        float jobHeight = bookryHeight;

        float DeviceWidth = FlipickConfig.getDeviceWidth_L(context);
        float DeviceHeight = FlipickConfig.getDeviceHeight_L(context);

        float ARW = DeviceWidth / jobWidth;
        float ARH = DeviceHeight / jobHeight;
        float ARFinal = DeviceHeight / jobHeight;

        if (ARW < ARH) ARFinal = ARW;
        else ARFinal = ARH;

        float Left = ((DeviceWidth - (jobWidth * ARFinal)) / 2);
        float Top = 0;

        String JobScaleCSS = "@media (orientation: landscape) {body > :first-child {position:absolute !important;left:" + Left + "px !important;top:" + Top + "px;width:" + jobWidth + "px;height:"
                + jobHeight + "px !important;-webkit-transform-origin:0 0 !important;-webkit-transform:scale(" + ARFinal + ") !important;  }}";

        return JobScaleCSS;
    }

    // @Override
    // public void onResume()
    // {
    // super.onResume();
    //
    // if (BookryXwalkwebview != null)
    // {
    // BookryXwalkwebview.resumeTimers();
    // BookryXwalkwebview.onShow();
    // }
    //
    // }

    // @Override
    // public void onDestroy()
    // {
    //
    // if (BookryXwalkwebview != null)
    // {
    //
    // BookryXwalkwebview.onDestroy();
    // }
    //
    // super.onDestroy();
    //
    // }

    // @Override
    // public void onPause()
    // {
    // super.onPause();
    // if (BookryXwalkwebview != null)
    // {
    // BookryXwalkwebview.pauseTimers();
    // BookryXwalkwebview.onHide();
    // }
    // }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (BookryXwalkwebview != null) {

            BookryXwalkwebview.onDestroy();
        }

        finish();

    }

}