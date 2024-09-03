package com.ieosabookreader;

import android.app.Activity;
import android.webkit.JavascriptInterface;
public class JavascriptInterface_Chromium
{
    Activity activity;
    boolean isPageCalled = false;

    JavascriptInterface_Chromium(Activity temp)
    {
        activity = temp;
    }

    @JavascriptInterface
    public void setDimention(float DeviceWidth, float DeviceHeight)
    {
        if (!isPageCalled)
        {
            isPageCalled = true;
            ((BookryWGT_Activity) activity).setDimention(DeviceWidth, DeviceHeight);
        }

    }

}
