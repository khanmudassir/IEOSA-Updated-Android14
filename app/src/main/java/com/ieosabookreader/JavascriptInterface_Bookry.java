package com.ieosabookreader;

import org.xwalk.core.JavascriptInterface;

import android.app.Activity;
public class JavascriptInterface_Bookry
{
    Activity activity;

    JavascriptInterface_Bookry(Activity temp)
    {
        activity = temp;
    }

    @JavascriptInterface
    public void setDimention(float DeviceWidth, float DeviceHeight)
    {

        ((BookryWGT_Activity) activity).setDimention(DeviceWidth, DeviceHeight);

    }

}

