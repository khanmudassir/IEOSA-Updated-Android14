package com.ieosabookreader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import org.xwalk.core.XWalkView;

import java.io.File;
import java.io.IOException;


import org.xwalk.core.XWalkResourceClient;


public class BrowserFragment extends Fragment
{
    XWalkView xWalkWebView;
    String url;
    ViewPager pager;
    Activity pagerActivity;
    String Page_Link = "";
    int position = 0;
    RelativeLayout layout_progress;
    View view;
    WebView Webview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (FlipickStaticData.currentapiVersion < 21)
        {
            view = inflater.inflate(R.layout.browserfragment, container, false);

            xWalkWebView = (XWalkView) view.findViewById(R.id.xwalkview_sec);
            layout_progress = (RelativeLayout) view.findViewById(R.id.layout_progress);
            xWalkWebView.setResourceClient(new XWalkResourceClientBase(xWalkWebView));
            xWalkWebView.setTag(position);

            // XWalkSettings settings;
            xWalkWebView.getNavigationHistory().clear();
            // xWalkWebView.setDrawingCacheEnabled(true);
            //xWalkWebView.getSettings().setJavaScriptEnabled(true);
            final MyJavaScriptInterface myJavaScriptInterface = new MyJavaScriptInterface(pagerActivity, null);
            xWalkWebView.addJavascriptInterface(myJavaScriptInterface, "AndroidFunction");

            if (FlipickStaticData.BookType.equals("REFLOWABLE"))
            {
                try
                {
                    url = url.replace("file://", "");
                    File oFile = new File(FlipickStaticData.oJobInfo.ReflowNewFileNames.get(position));
                    // for Trail user functionaliy.(if url is dummy page then no
                    // need to add style)
                    if (!url.contains("android_asset"))
                    {
                        if (!oFile.exists())
                        {
                            String HTMLData = FlipickIO.ReadHTMLFileText(url);
                            HTMLData = HTMLData.replace("<head>", "<head><style type='text/css'>" + FlipickStaticData.oJobInfo.ReflowColumnShift.get(position) + "</style>");
                            HTMLData = HTMLData.replace("<title>", "<title>" + position);
                            FlipickIO.WriteTextFile(oFile, HTMLData);
                        }
                    }

                    if (FlipickStaticData.oJobInfo.ReflowNewFileNames.get(position).startsWith("file://"))
                    {
                        String NewUrl = FlipickStaticData.oJobInfo.ReflowNewFileNames.get(position);
                        xWalkWebView.load(NewUrl, null);
                    }
                    else
                    {
                        String NewUrl = "file://" + FlipickStaticData.oJobInfo.ReflowNewFileNames.get(position);
                        xWalkWebView.load(NewUrl, null);
                    }

                    // }// DeleteReflowOffScreenFiles(position);
                    // xWalkWebView.load(NewUrl, null);
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else
            {
                xWalkWebView.clearCache(true);
                xWalkWebView.load(url, null);
            }
            return view;
        }
        else
        {
            view = inflater.inflate(R.layout.browserfragmentviewpager_test, container, false);

            Webview = (WebView) view.findViewById(R.id.webView_browserfrg);
            layout_progress = (RelativeLayout) view.findViewById(R.id.layout_progress);
            // Webview.setResourceClient(new
            // XWalkResourceClientBase(xWalkWebView));
            Webview.setTag(position);

            // XWalkSettings settings;
            // Webview.getNavigationHistory().clear();
            // Webview.setDrawingCacheEnabled(true);
            Webview.getSettings().setJavaScriptEnabled(true);
            Webview.getSettings().setAllowFileAccess(true);
            // Webview.getSettings().setAllowFileAccessFromFileURLs(true);
            // Webview.getSettings().setAllowUniversalAccessFromFileURLs(true);
            // Webview.getSettings().setAllowContentAccess(true);
            // Webview.getSettings().setAllowUniversalAccessFromFileURLs(true);
            // Webview.getSettings().setUseWideViewPort(true);
            Webview.getSettings().setDomStorageEnabled(true);
            Webview.setWebChromeClient(new WebChromeClient());
            Webview.setWebViewClient(new WebViewClient());
            final MyJavaScriptInterfaceTest myJavaScriptInterface = new MyJavaScriptInterfaceTest(pagerActivity, null);
            Webview.addJavascriptInterface(myJavaScriptInterface, "AndroidFunction");

            if (FlipickStaticData.BookType.equals("REFLOWABLE"))
            {
                try
                {
                    url = url.replace("file://", "");
                    File oFile = new File(FlipickStaticData.oJobInfo.ReflowNewFileNames.get(position));
                    // for Trail user functionaliy.(if url is dummy page then no
                    // need to add style)
                    if (!url.contains("android_asset"))
                    {
                        if (!oFile.exists())
                        {
                            String HTMLData = FlipickIO.ReadHTMLFileText(url);
                            HTMLData = HTMLData.replace("<head>", "<head><style type='text/css'>" + FlipickStaticData.oJobInfo.ReflowColumnShift.get(position) + "</style>");
                            HTMLData = HTMLData.replace("<title>", "<title>" + position);
                            FlipickIO.WriteTextFile(oFile, HTMLData);
                        }
                    }

                    if (FlipickStaticData.oJobInfo.ReflowNewFileNames.get(position).startsWith("file://"))
                    {
                        String NewUrl = FlipickStaticData.oJobInfo.ReflowNewFileNames.get(position);
                        Webview.loadUrl(NewUrl, null);
                    }
                    else
                    {
                        String NewUrl = "file://" + FlipickStaticData.oJobInfo.ReflowNewFileNames.get(position);
                        Webview.loadUrl(NewUrl, null);
                    }

                    // }// DeleteReflowOffScreenFiles(position);
                    // xWalkWebView.load(NewUrl, null);
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else
            {
                Webview.clearCache(true);
                Webview.loadUrl(url, null);
            }
            return view;
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
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }
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
            if (!FlipickStaticData.BookType.equals("REFLOWABLE"))
            {
                layout_progress.setVisibility(View.VISIBLE);
            }
            super.onLoadStarted(view, url);
        }

        public void onLoadFinished(XWalkView view, String url)
        {
            super.onLoadFinished(view, url);

            if (((flipickpageviewer) pagerActivity).IsPageNavBarOn == true)
            {
                new Runnable()
                {
                    public void run()
                    {
                        try
                        {
                            if ((((flipickpageviewer) pagerActivity).currentNavPageIndex) == position) ((flipickpageviewer) pagerActivity).SetPageNavLink();
                            else return;
                        }
                        catch (Exception e)
                        {
                        }
                        catch (Throwable e)
                        {
                            e.printStackTrace();
                        }
                    }
                };
            }
            if (!FlipickStaticData.BookType.equals("REFLOWABLE"))
            {
                layout_progress.setVisibility(View.GONE);
            }

        }

        @Override
        public boolean shouldOverrideUrlLoading(XWalkView view, String url)
        {

            return super.shouldOverrideUrlLoading(xWalkWebView, url);
        }
    }

    // public void DeleteReflowOffScreenFiles(int position)
    // {
    // int BackIndex = position - 3;
    //
    // int ForwardIndex = position + 3;
    //
    // for (int i = ForwardIndex; i <
    // FlipickStaticData.oJobInfo.ReflowNewFileNames.size(); i++)
    // {
    // File oFile = new
    // File(FlipickStaticData.oJobInfo.ReflowNewFileNames.get(i));
    //
    // if (oFile.exists()) oFile.delete();
    // else break;
    // }
    //
    // for (int i = BackIndex; i >= 0; i--)
    // {
    // File oFile = new
    // File(FlipickStaticData.oJobInfo.ReflowNewFileNames.get(i));
    //
    // if (oFile.exists()) oFile.delete();
    // else break;
    // }
    // }

    // public static Fragment newInstanceReflow(String url, ViewPager MyPager,
    // Activity pagerActivity1, String PageLink, int position)
    // {
    // BrowserFragment f = new BrowserFragment();
    //
    // f.position = position;
    // f.url = url;
    // f.pager = MyPager;
    // f.pagerActivity = pagerActivity1;
    // f.Page_Link = PageLink;
    //
    // return f;
    // }

    public static Fragment newInstance(String url, ViewPager MyPager, Activity pagerActivity1, String PageLink, int position)
    {
        BrowserFragment f = new BrowserFragment();
        f.url = url;
        f.pager = MyPager;
        f.pagerActivity = pagerActivity1;
        f.Page_Link = PageLink;
        f.position = position;
        return f;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (FlipickStaticData.currentapiVersion < 21)
        {
            if (xWalkWebView != null)
            {
                xWalkWebView.pauseTimers();
                xWalkWebView.onHide();
            }
        }
        else
        {

        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (FlipickStaticData.currentapiVersion < 21)
        {
            if (xWalkWebView != null)
            {
                xWalkWebView.resumeTimers();
                xWalkWebView.onShow();
            }
        }
        else
        {

        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (FlipickStaticData.currentapiVersion < 21)
        {
            if (xWalkWebView != null)
            {
                xWalkWebView.onDestroy();
            }
        }
        else
        {

        }

    }
}