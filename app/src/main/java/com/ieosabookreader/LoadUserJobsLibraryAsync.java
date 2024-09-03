package com.ieosabookreader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoadUserJobsLibraryAsync extends AsyncTask<String, String, String> {

    Context context;
    User oUser;
    // String FromActivity;
    // String ActivationCode;

    ArrayList<JobInfo> old_jobArray;

    public LoadUserJobsLibraryAsync(Context context) // String
    // FromActivity,String
    // Code)
    {
        this.context = context;
        // this.oUser = oUser;
        this.oUser = new User(context);
        // this.FromActivity = FromActivity;
        // this.ActivationCode=Code;

    }
    @Override
    protected void onPreExecute()
    {
       // FlipickStaticData.progressMessage = "Synchronizing please wait...";
        FlipickStaticData.progressMessage = "Downloading please wait...";
        FlipickUtil.StartProgressBar(context);

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... urls)
    {
        try
        {
            // userjob
            String ZipPath = FlipickConfig.UserJobXmlPath + "/" + "userjob.zip";
            FlipickIO.DonwloadAndSaveFileWithoutProgress(FlipickConfig.UserJobZipurl, ZipPath);
            FlipickIO.extractFolder(ZipPath, FlipickConfig.UserJobXmlPath + "/");
            File ofile = new File(FlipickConfig.ContentRootPath + "/" + "userjob.xml");
            //FlipickStaticData.progressMessage = "Synchronizing please wait...";
            FlipickStaticData.progressMessage = "Downloading please wait...";
            if (ofile.exists())
            {
                Log.e("XXX", "Called If");
            }
            else
            {
                Log.e("XXX", "Called Else");

            }
            JobInfo oEpubJob = new JobInfo(context);
            ArrayList<JobInfo> jobArray = new ArrayList<JobInfo>();
            String Err = "";
            oUser.UserName = "demouser@ieosa.com";
            oUser.Password = "password";
            oUser.UserId = "54321";
            oUser.SessionId = "111";
            oUser.WebsiteId = "1";
            oUser.StoreID = "2";
            // editor.putString("WebsiteId", this.WebsiteId);
            // editor.putString("StoreId", this.StoreID);
            if (FlipickConfig.StoreType.equals("Magento"))
            {
                jobArray = oEpubJob.MagentoHardcodedGetJobThumbnail(oUser);
            }
            else
            {
                //Err = oUser.AuthenticateUser();
            }
            if (!Err.equals(""))
            {
                FlipickStaticData.ThreadMessage = Err;
                return "";
            }
            // oUser_old.GetUserInfo();

            /*
             * if ((oUser.UserName.trim()).equals(oUser_old.UserName.trim()) &&
             * (oUser.Password).equals(oUser_old.Password)) {
             * oUser.SetUserInfo(); FlipickStaticData.ThreadMessage = ""; return
             * ""; }
             */

            oUser.clearSharedPreferences();
            /*
             * File oFile = new File(FlipickConfig.Part1ContentRootPath);
             * FlipickIO.DeleteDirectoryRecursive(oFile);
             */

            FlipickConfig.Part2ContentRootPath = oUser.UserName.trim();
            FlipickConfig.ContentRootPath = FlipickConfig.Part1ContentRootPath + "/" + FlipickConfig.Part2ContentRootPath;
            FlipickIO.CreateUserRootFolder();
            oUser.SetUserInfo();

            // save appversion in shared pref
            FlipickConfig.saveAppVirsion(context);

            oEpubJob.deleteJobInfoTableFromDB(false);
            if (jobArray != null && jobArray.size() > 0)
            {
                oEpubJob.addJobInfosInDB(jobArray, null);// add into
                oEpubJob.downloadThumbnails(jobArray);
            }
            oEpubJob.DeleteCacheJobThumbnails();
        }
        catch (Exception e)
        {
            if (e.getMessage() == null) FlipickStaticData.ThreadMessage = "Null pointer exception";
            else FlipickStaticData.ThreadMessage = e.getMessage();
        }
        catch (Throwable e)
        {
            FlipickStaticData.ThreadMessage = e.getMessage();
        }
        finally
        {

        }
        return "";
    }

    @Override
    protected void onPostExecute(String result)
    {
        // FlipickStaticData.progressMessage = "";
        FlipickStaticData.AsynRunningActivity = "";
        if (FlipickStaticData.ThreadMessage.equals(""))
        {
            // finish();
            FlipickStaticData.ThreadMessage = "";
            FlipickStaticData.progressMessage = "";
            Intent oIntent = new Intent(context, Blank_webview.class);
            ((Activity) context).startActivity(oIntent);
            ((Activity) context).finish();
        }
        else
        {
            FlipickError.DisplayErrorAsToast(context, FlipickStaticData.ThreadMessage);
            FlipickStaticData.ThreadMessage = "";
            FlipickStaticData.ThreadMessage = "";
            FlipickStaticData.progressMessage = "";
        }

    }

}