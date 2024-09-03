package com.ieosabookreader;

import android.app.Activity;
import android.webkit.JavascriptInterface;

public class JavaScriptInterface_blank_webview_L
{
    Activity oBlankWebview_L;

    JavaScriptInterface_blank_webview_L(Activity temp)
    {
        oBlankWebview_L = temp;
    }

    @JavascriptInterface
    public void setDimention(float DeviceWidth, float DeviceHeight)
    {
        ((Blank_webview_landscape) oBlankWebview_L).setDimention(DeviceWidth, DeviceHeight);

    }

    @JavascriptInterface
    public void DisplayErrorAsUIThread(final String Message)
    {
        try
        {
            ((Blank_webview_landscape) oBlankWebview_L).runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    {
                        FlipickError.DisplayErrorAsLongToast(oBlankWebview_L.getApplicationContext(), Message);
                        return;
                    }
                }
            });
        }
        finally
        {

        }
    }

}
