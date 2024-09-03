package com.ieosabookreader;

import androidx.fragment.app.FragmentActivity;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import com.ieosabookreader.customactionbar.CustomActionBar;

public class TOCBrowserFragment extends FragmentActivity
{
    WebView toc_browser;
    String toc_Path;
    String Title = "";
    String currentPage = "";
    boolean isTap = false;
    CustomActionBar actionBar;
    String UserOrientation;

    @SuppressLint(
            { "SetJavaScriptEnabled", "JavascriptInterface" })
    @JavascriptInterface
    @SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tocbrowserfragment);

        getBundleValue();
        if (!FlipickStaticData.BookType.equals("REFLOWABLE"))
        {
            if (UserOrientation.equals("1"))
            {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            if (UserOrientation.equals("2"))
            {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }
        else
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setActionBar();
        toc_browser = (WebView) findViewById(R.id.tocfragment_browser);
        toc_browser.getSettings().setJavaScriptEnabled(true);
        toc_browser.getSettings().setLoadWithOverviewMode(true);
        toc_browser.getSettings().setAllowFileAccess(true);
        //toc_browser.getSettings().setLightTouchEnabled(true);
        toc_browser.getSettings().setSupportZoom(true);
        //toc_browser.getSettings().setRenderPriority(RenderPriority.HIGH);

        TocJavascriptInterface jsInterface = new TocJavascriptInterface(this);
        toc_browser.addJavascriptInterface(jsInterface, "AndroidFunction");

        FlipickStaticData.IsLastPageToc = true;

        if (FlipickStaticData.BookType.equals("EPUB3"))
        {
        }
        toc_browser.setWebChromeClient(new WebChromeClient());

        Map<String, String> noCacheHeaders = new HashMap<String, String>(2);
        noCacheHeaders.put("Pragma", "no-cache");
        noCacheHeaders.put("Cache-Control", "no-cache");
        toc_browser.loadUrl("file:///" + toc_Path);
    }

    @Override
    public void onBackPressed()
    {

        try
        {

            Intent oIntent = new Intent(getApplicationContext(), flipickpageviewer.class);
            oIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle bundle = new Bundle();
            bundle.putString("PageLink", currentPage);
            bundle.putString("Activity", "TOCActivity");
            bundle.putString("JobId", FlipickStaticData.oJobInfo.JobId);
            oIntent.putExtras(bundle);
            startActivity(oIntent);
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_from_right);
            finish();

        }
        catch (Exception e)
        {
            FlipickError.DisplayErrorAsLongToast(getApplicationContext(), e.getMessage());
        }
    }

    private void getBundleValue()
    {
        Bundle oToclink = getIntent().getExtras();
        toc_Path = oToclink.getString("TOCLink");
        Title = oToclink.getString("BookTitle");
        currentPage = oToclink.getString("PageLink");

        UserOrientation = oToclink.getString("UserOrientation");
    }

    private void setActionBar()
    {

        actionBar = (CustomActionBar) findViewById(R.id.action_bar);

        actionBar.showActionBar();
        actionBar.hidelayoutfontsetting();
        actionBar.invisiblebtnAll();
        actionBar.hideBookmark();
        actionBar.setTitleText(Html.fromHtml(Title).toString());
        actionBar.hideTOCButton();

        ((RelativeLayout) actionBar.getLibraryLayout()).setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                BackToLibrary();
            }
        });
    }

    public void BackToLibrary()
    {
        //finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_from_right);
        finish();
    }

    public void GoToLink(String pageLink, String JobId)
    {
        finish();
        Intent oIntent = new Intent(getApplicationContext(), flipickpageviewer.class);
        oIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString("ACTION", "DISPLAYPAGE");
        bundle.putString("PageLink", pageLink);
        bundle.putString("JobId", JobId);
        oIntent.putExtras(bundle);
        startActivity(oIntent);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_left);
    }

    @Override
    protected void onPause() {
        super.onPause();
        toc_browser.onPause();//if it's not webview in your case then add the method name you want pause when user device is pause
    }


    @Override
    protected void onResume() {
        super.onResume();
        toc_browser.onResume();//same as here if it's not webview then add the method name you want to resume when user resume their device
    }
}