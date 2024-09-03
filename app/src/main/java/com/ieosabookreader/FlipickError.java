package com.ieosabookreader;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class FlipickError {

    public static void DisplayErrorAsToast(Context c, String Message)
    {
        Toast.makeText(c, Message, Toast.LENGTH_SHORT).show();
    }

    public static void DisplayErrorAsLongToast(Context c,String Message)
    {
        Toast toast_msg =Toast.makeText(c, Message, Toast.LENGTH_LONG);
        toast_msg.setGravity(Gravity.CENTER, 0, 0);
        toast_msg.show();
    }
}