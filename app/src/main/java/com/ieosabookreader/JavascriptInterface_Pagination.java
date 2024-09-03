package com.ieosabookreader;

import android.app.Activity;
import org.xwalk.core.JavascriptInterface;


public class JavascriptInterface_Pagination
{
    Activity oPaginationActivity;

    JavascriptInterface_Pagination(Activity temp)
    {
        oPaginationActivity = temp;
    }

    @JavascriptInterface
    public void SetPageWidth(String Measuredwidth)
    {
        ((PaginationActivity) oPaginationActivity).SetPageWidth(Measuredwidth);

    }

    @JavascriptInterface
    public String GetPageUrl() throws Throwable
    {
        return ((PaginationActivity) oPaginationActivity).GetPageUrl();

    }

    @JavascriptInterface
    public String GetLinkedIds()
    {
        return ((PaginationActivity) oPaginationActivity).GetLinkedIds();

    }

    @JavascriptInterface
    public void SetPageLinkedIds(String LinkedPageIndex)
    {
        ((PaginationActivity) oPaginationActivity).SetPageLinkedIds(LinkedPageIndex);
    }

}