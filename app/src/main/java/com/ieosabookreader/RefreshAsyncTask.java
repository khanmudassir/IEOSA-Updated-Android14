package com.ieosabookreader;

import android.os.AsyncTask;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class RefreshAsyncTask extends AsyncTask<String, String, String> {

    Context context;
    User oUser;
    // String FromActivity;
    // String ActivationCode;

    ArrayList<JobInfo> old_jobArray;

    public RefreshAsyncTask(Context context, User oUser) // String
    // FromActivity,String
    // Code)
    {
        this.context = context;
        this.oUser = oUser;
        // this.FromActivity = FromActivity;
        // this.ActivationCode=Code;

    }

    @Override
    protected void onPreExecute() {
        FlipickStaticData.progressMessage = "Synchronizing...";
        FlipickUtil.StartProgressBar(context);

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            JobInfo oEpubJob = new JobInfo(context);
            old_jobArray = oEpubJob.getJobInfoFromDB();
            String pathtoCheck = FlipickConfig.ContentRootPath;
            ArrayList<JobInfo> jobArray = new ArrayList<JobInfo>();
            String ZipPath = FlipickConfig.UserJobXmlPath + "/" + "userjob.zip";
            FlipickIO.DonwloadAndSaveFileWithoutProgress(FlipickConfig.UserJobZipurl, ZipPath);
            FlipickIO.extractFolder(ZipPath, FlipickConfig.UserJobXmlPath + "/");
            File ofile = new File(FlipickConfig.ContentRootPath + "/" + "userjob.xml");
            FlipickStaticData.progressMessage = "Synchronizing...";
            if (ofile.exists()) {

            } else {

            }
            // if (FlipickConfig.StoreType.equals("Magento")) jobArray =
            // oEpubJob.MagentoGetJobThumbnail(oUser);
            if (FlipickConfig.StoreType.equals("Magento"))
                jobArray = oEpubJob.MagentoHardcodedGetJobThumbnail(oUser);

            oEpubJob.deleteJobInfoTableFromDB(true);

            oEpubJob.addJobInfosInDB(jobArray, old_jobArray);
            oEpubJob.downloadThumbnails(jobArray);
            oEpubJob.DeleteCacheJobThumbnails();
        } catch (Exception e) {
            if (e.getMessage() == null) FlipickStaticData.ThreadMessage = "Null pointer exception";
            else FlipickStaticData.ThreadMessage = e.getMessage();
        } catch (Throwable e) {
            FlipickStaticData.ThreadMessage = e.getMessage() + ":" + e.getCause();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        FlipickStaticData.progressMessage = "";

        if (!FlipickStaticData.ThreadMessage.equals("")) {
            FlipickStaticData.ThreadMessage = "";
            FlipickStaticData.progressMessage = "";
            ((Activity) context).finish();
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle oBundle = new Bundle();
            oBundle.putString("ACTION", "RefreshLibrary");
            intent.putExtras(oBundle);
            ((Activity) context).startActivity(intent);
            FlipickStaticData.progressMessage = "";
        }

        if (FlipickStaticData.ThreadMessage.equals("")) {
            FlipickStaticData.ThreadMessage = "";
            FlipickStaticData.progressMessage = "";
            // if (FromActivity.equalsIgnoreCase("ActivationActivity"))
            // {
            // getConfirmationOnPrintCopy();
            // }
            // else
            // {

            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            ((Activity) context).startActivity(intent);
            ((Activity) context).finish();
            // }
        }

    }

}
