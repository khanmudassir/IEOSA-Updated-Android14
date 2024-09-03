package com.ieosabookreader;

import android.app.Activity;
import android.webkit.JavascriptInterface;

public class JavaScriptInterface_blank_webview extends Activity
{
    Activity oBlankWebview;

    JavaScriptInterface_blank_webview(Activity temp)
    {
        oBlankWebview = temp;
    }

    @JavascriptInterface
    public void setDimention(float DeviceWidth, float DeviceHeight)
    {
        ((Blank_webview) oBlankWebview).setDimention(DeviceWidth, DeviceHeight);

    }

    @JavascriptInterface
    public void DisplayErrorAsUIThread(final String Message)
    {
        try
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    {
                        FlipickError.DisplayErrorAsLongToast(oBlankWebview.getApplicationContext(), Message);
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
