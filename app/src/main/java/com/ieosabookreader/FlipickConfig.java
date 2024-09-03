package com.ieosabookreader;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;

public class FlipickConfig {
    // public static String ServiceUrl
    // ="http://azmed01.cloudapp.net/store/index.php/webservices/Advancedapi";

    public static String DisplayShippingInfoForm = "Y";
    public static String UserJobFileName = "userjob.xml";
    //public static String UserJobZipurl = "https://ieosa.flipick.com/appcontent/userjob.zip";
    // Latest URL - 20 July 2024
    public static String UserJobZipurl = "https://ieosa.mediawide.com/appcontent/userjob.zip";

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ URL for Local
    // public static String ServiceUrl =
    // "http://192.168.200.23/bookshopDev/index.php/webservices/Advancedapi";
    // public static String DashBoardUrl =
    // "http://192.168.200.9/mcq/mcqclient1.1/postmcq.ashx";
    // public static String RandomPaperGeneratorUrl =
    // "http://192.168.200.9/MCQ/MCQAPI/API/Questions/GetRandQuestions";

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~arabic
    // public static String
    // ServiceUrl="http://192.168.200.14/flipickebooks/ieosa/index.php/webservices/Advancedapi/publishOrderList";
    // public static String
    // ServiceUrl="http://d3.mediawide.com/flipickebooks/ieosa/index.php/webservices/Advancedapi/publishOrderList";
    // public static String
    // Store_Url="http://d3.mediawide.com/flipickebooks/ieosa/";

    // username:ish.parekh@mediawide.com

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Esurjan
    // public static String
    // ServiceUrl="http://d3.mediawide.com/esrujan_new/index.php/webservices/Advancedapi/publishOrderList";
    // public static String
    // Store_Url="http://d3.mediawide.com/esrujan_new/index.php/";
    // public static String
    // ServiceUrl="http://180.92.165.155:9999/esrujan_new/index.php/webservices/Advancedapi/publishOrderList";

    // public static String ServiceUrl =
    // "http://www.bookhungama.com/esrujan_new/index.php/webservices/Advancedapi";
    // public static String Store_Url =
    // "http://www.bookhungama.com/esrujan_new/index.php/";

    // public static String ServiceUrl =
    // "http://www.bookhungama.com/index.php/webservices/Advancedapi";
    // public static String Store_Url = "http://www.bookhungama.com/index.php/";

    // public static String
    // Store_Url="http://180.92.165.155:9999/esrujan_new/index.php/";
    // Local
    // http://192.168.200.14/eSrujanDev/
    // public static String ServiceUrl
    // ="http://192.168.200.14/eSrujanDev/index.php/webservices/Advancedapi/publishOrderList";
    // public static String Store_Url =
    // "http://192.168.200.14/eSrujanDev/index.php/";

    // public static String ServiceUrl =
    // "http://192.168.200.14/bookshopDev/technicalpublications/webservices/Advancedapi/publishOrderList";
    // public static String Store_Url =
    // "http://192.168.200.14/bookshopDev/technicalpublications/";
    //

    // public static String ServiceUrl =
    // "http://192.168.200.14/mypgmeeDev/elsevier/index.php/webservices/Advancedapi";
    // public static String Store_Url =
    // "http://192.168.200.14/mypgmeeDev/elsevier/index.php/";

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~FlipickBook

    // public static String ServiceUrl =
    // "http://store.flipick.com/index.php/webservices/Advancedapi";
    // public static String Store_Url = "http://store.flipick.com/index.php/";

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ShethPublishers

    // public static String ServiceUrl =
    // "http://d3.mediawide.com/flipickebooks/shethpublishers/index.php/webservices/Advancedapi";
    // public static String Store_Url =
    // "http://d3.mediawide.com/flipickebooks/shethpublishers/index.php/";

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Elsevier
    // public static String ServiceUrl =
    // "http://d3.mediawide.com/elsevier/index.php/webservices/Advancedapi";
    // public static String Store_Url =
    // "http://d3.mediawide.com/elsevier/index.php/";

    // public static String ServiceUrl =
    // "http://elsevier.flipick.com/index.php/webservices/Advancedapi";
    // public static String Store_Url =
    // "http://elsevier.flipick.com/index.php/";
    // public static String OneClick_Url =
    // "http://elsevier.flipick.com/index.php/webservices/Advancedapi/oneclickCheckout/";

    // public static String ServiceUrl =
    // "http://elsevier.flipick.com/index.php/webservices/Advancedapi";
    // public static String Store_Url =
    // "http://elsevier.flipick.com/index.php/";

    // public static String
    // ServiceUrl="http://192.168.200.14/elsevier/index.php/webservices/Advancedapi";
    // public static String Store_Url =
    // "http://192.168.200.14/elsevier/index.php/";
    // public static String OneClick_Url=
    // "http://192.168.200.14/elsevier/index.php/webservices/Advancedapi/oneclickCheckout/";

    // public static String ServiceUrl =
    // "http://elsevier.flipick.com/index.php/webservices/Advancedapi";
    // public static String Store_Url =
    // "http://elsevier.flipick.com/index.php/";
    // public static String OneClick_Url =
    // "http://elsevier.flipick.com/index.php/webservices/Advancedapi/oneclickCheckout/";

    // public static String ServiceUrl =
    // "http://192.168.200.14/elsevier/index.php/webservices/Advancedapi";
    // public static String Store_Url =
    // "http://192.168.200.14/elsevier/index.php/";
    // public static String OneClick_Url =
    // "http://192.168.200.14/elsevier/index.php/webservices/Advancedapi/oneclickCheckout/";

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~FlipickBookReader
    // public static String ServiceUrl =
    // "http://d3.mediawide.com/flipickebooks/webservices/Advancedapi";
    // public static String Store_Url =
    // "http://d3.mediawide.com/flipickebooks/";
    // public static String OneClick_Url =
    // "http://d3.mediawide.com/flipickebooks/webservices/Advancedapi/oneclickCheckout/";

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~IEOSABookReader
    public static String ServiceUrl = "http://ieosa.flipick.com/index.php/webservices/Advancedapi";
    // public static String Store_Url = "http://ieosa.flipick.com/index.php/";
    // public static String OneClick_Url =
    // "http://ieosa.flipick.com/index.php/webservices/Advancedapi/oneclickCheckout/";
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // /~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Flipick Library – Rental
    // public static String
    // ServiceUrl="http://d3.mediawide.com/flipickebooks/index.php/webservices/Advancedapi/publishOrderList";
    // public static String
    // Store_Url="http://d3.mediawide.com/flipickebooks/index.php/";

    // public static String ServiceUrl
    // ="http://d3.mediawide.com/flipickebooks/webservices/Advancedapi";
    // public static String Store_Url="http://d3.mediawide.com/flipickebooks/";

    // public static String ServiceUrl
    // ="http://d3.mediawide.com/flipickebooks/webservices/Advancedapi";
    // public static String
    // ServiceUrl="http://d3.mediawide.com/flipickebooks/ieosa/webservices/Advancedapi";
    // public static String ServiceUrl
    // ="http://192.168.200.14/bookshopDev/webservices/Advancedapi";
    public static String ContentRootPath = "";
    public static String UserJobXmlPath = "";
    public static String Part1ContentRootPath = "";
    public static String Part2ContentRootPath = "";
    public static String StoreType = "Magento";

    public String LoadConfig()
    {
        // Read the config file from application directory and initialize the
        // service url
        return "";
    }

    public static float pxFromDp(final Context context, final float dp)
    {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static void SetFileConfigPath(Context context)
    {
        // FlipickConfig.ContentRootPath =
        // Environment.getExternalStorageDirectory().toString() +
        // "/FlipickBookReader_Content";
        // FlipickConfig.Part1ContentRootPath =
        // Environment.getExternalStorageDirectory().toString() +
        // "/FlipickBookReader_Content";

        // FlipickConfig.ContentRootPath = context.getFilesDir() +
        // "/FlipickBookReader_Content";
        // FlipickConfig.Part1ContentRootPath = context.getFilesDir() +
        // "/FlipickBookReader_Content";

        // FlipickConfig.ContentRootPath =
        // Environment.getExternalStorageDirectory().toString() +
        // "/IEOSABookReader";
        // FlipickConfig.Part1ContentRootPath =
        // Environment.getExternalStorageDirectory().toString() +
        // "/IEOSABookReader";
        // FlipickConfig.UserJobXmlPath =
        // Environment.getExternalStorageDirectory().toString() +
        // "/IEOSABookReader";


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            FlipickConfig.UserJobXmlPath = context.getFilesDir() + "/IEOSABookReader";
            FlipickConfig.ContentRootPath = context.getFilesDir() + "/IEOSABookReader";
            FlipickConfig.Part1ContentRootPath = context.getFilesDir() + "/IEOSABookReader";

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            FlipickConfig.UserJobXmlPath = context.getFilesDir() + "/IEOSABookReader";
            FlipickConfig.ContentRootPath = context.getFilesDir() + "/IEOSABookReader";
            FlipickConfig.Part1ContentRootPath = context.getFilesDir() + "/IEOSABookReader";

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            FlipickConfig.UserJobXmlPath = context.getFilesDir() + "/IEOSABookReader";
            FlipickConfig.ContentRootPath = context.getFilesDir() + "/IEOSABookReader";
            FlipickConfig.Part1ContentRootPath = context.getFilesDir() + "/IEOSABookReader";

        } else  {

            FlipickConfig.UserJobXmlPath = context.getFilesDir() + "/IEOSABookReader";
            FlipickConfig.ContentRootPath = context.getFilesDir() + "/IEOSABookReader";
            FlipickConfig.Part1ContentRootPath = context.getFilesDir() + "/IEOSABookReader";

        }

        // FlipickConfig.ContentRootPath =
        // Environment.getExternalStorageDirectory().toString() +
        // "/ElsevierBookReader";
        // FlipickConfig.Part1ContentRootPath =
        // Environment.getExternalStorageDirectory().toString() +
        // "/ElsevierBookReader";

        // FlipickConfig.ContentRootPath = context.getFilesDir() +
        // "/ElsevierBookReader";
        // FlipickConfig.Part1ContentRootPath = context.getFilesDir() +
        // "/ElsevierBookReader";

        // FlipickConfig.ContentRootPath =
        // Environment.getExternalStorageDirectory().toString() +
        // "/BookHungamaReader";
        // FlipickConfig.Part1ContentRootPath =
        // Environment.getExternalStorageDirectory().toString() +
        // "/BookHungamaReader";

        // FlipickConfig.ContentRootPath = context.getFilesDir() +
        // "/BookHungamaReader";
        // FlipickConfig.Part1ContentRootPath = context.getFilesDir() +
        // "/BookHungamaReader";

        FlipickConfig.Part2ContentRootPath = "";

        FlipickIO.CreateRootFolder();

        FlipickStaticData.DeviceId = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
        FlipickStaticData.OSVersion = android.os.Build.VERSION.RELEASE;

    }

    public static void saveAppVirsion(Context context)
    {
        try
        {
            SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

            editor.putInt("APPV", packageInfo.versionCode);
            editor.commit();
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    public static boolean isAppVirsionLess(Context context)
    {

        boolean is_same = true;
        try
        {
            SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), context.MODE_PRIVATE);
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (packageInfo.versionCode > preferences.getInt("APPV", -1))
            {
                is_same = false;
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return is_same;
    }

    public static void SaveDeviceWidth(Context context, float width)
    {
        SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("DeviceWidth", width);
        editor.commit();
    }

    public static void SaveDeviceheight(Context context, float Height)
    {
        SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("DeviceHeight", Height);
        editor.commit();
    }

    public static float getDeviceHeight(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), context.MODE_PRIVATE);
        return preferences.getFloat("DeviceHeight", 0.0f);
    }

    public static float getDeviceWidth(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), context.MODE_PRIVATE);
        return preferences.getFloat("DeviceWidth", 0.0f);
    }

    public static void SaveDeviceWidth_L(Context context, float width)
    {
        SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("DeviceWidth_L", width);
        editor.commit();
    }

    public static void SaveDeviceheight_L(Context context, float Height)
    {
        SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("DeviceHeight_L", Height);
        editor.commit();
    }

    public static float getDeviceHeight_L(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), context.MODE_PRIVATE);
        return preferences.getFloat("DeviceHeight_L", 0.0f);
    }

    public static float getDeviceWidth_L(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), context.MODE_PRIVATE);
        return preferences.getFloat("DeviceWidth_L", 0.0f);
    }

    public static String getPagerCurrentPage(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), context.MODE_PRIVATE);
        return preferences.getString("CurrentPage", "");
    }

    public static String getPagerCurrentPageJobId(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), context.MODE_PRIVATE);
        return preferences.getString("JobId", "");
    }

    public static void SavePagerCurrentPage(Context context, String currentPage, String JobId)
    {
        SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("CurrentPage", currentPage);
        editor.putString("JobId", JobId);
        editor.commit();
    }

    // public static String getAppVersion(Context context)
    // {
    // PackageInfo pinfo;
    // //int versionNumber;
    // String versionName = null;
    // try
    // {
    // pinfo =
    // context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
    // //versionNumber = pinfo.versionCode;
    // versionName = pinfo.versionName;
    // }
    // catch (NameNotFoundException e)
    // {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // return versionName;
    // }

    public static void clearSharedPreferences(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}
