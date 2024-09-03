package com.ieosabookreader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.widget.Toast;

public class ConnectionDetector
{

    public static boolean IsInternetAvailable(Context _context)
    {
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {

            NetworkInfo info = connectivity.getActiveNetworkInfo();// getAllNetworkInfo();
            boolean isConnected = info != null &&
                    info.isConnectedOrConnecting();

            return  isConnected;

        }
        return false;
    }

    public static void DisplayInternetErrorMessage(Context context)
    {
        // TODO Auto-generated method stub
        Toast toast_msg = Toast.makeText(context, "No internet connection available.", Toast.LENGTH_LONG);
        toast_msg.setGravity(Gravity.CENTER, 0, 0);
        toast_msg.show();
    }

    public static String GetInternetErrorMessage()
    {
        return "No internet connection available.";
    }

}
