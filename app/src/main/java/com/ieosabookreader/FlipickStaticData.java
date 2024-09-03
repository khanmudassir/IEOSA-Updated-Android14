package com.ieosabookreader;

import android.net.Uri;

public class FlipickStaticData {
    public static int currentapiVersion;
    public static float DeviceWidth;
    public static float DeviceHeight;
    public static float DecoFinal;
    public static String CallFrom = "";
    public static String ThreadMessage = "";
    public static String BookType = "";

    public static String BookTitle = "";

    public static JobInfo oJobInfo = new JobInfo();
    public static String Orientation = "";

    public static String progressMessage = "";
    public static String MsgDownload = "Downloading... ";
    public static String MsgUnzip = "Unziping... ";
    public static String MsgOpeningBook = "Processing... ";

    public static String OEBPSPath = "";

    public static String DeviceId = "";
    public static String OSVersion = "";

    public static String JobId = "";
    public static String SelectedPageLink = "";

    public static String AsynRunningActivity = "";
    public static Boolean isInternetPresent = false;
    public static String TYPE = null;
    public static Uri DATA = null;

    // Default font size for tablet and mobile
    public static float DefaultFontSizeMobile = (float) 2.75;//3.25
    public static float DefaultFontSizeTablet = (float) 2;

    // Max allowed pages for free user.
    public static int MaxAllowedFixLayoutPages = 5;
    public static int MaxAllowedAdaptivePages = 5;

    //Toc Page trac
    public static Boolean IsLastPageToc=false;

    //public static Boolean IsConfigurationStateChanged=false;


}
