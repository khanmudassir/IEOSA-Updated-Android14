package com.ieosabookreader;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.util.Random;

public class SideloadActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        User oUser = new User(this);
        oUser.GetUserInfo();
        copyEpub(this,oUser);
    }

    // for sideload
    public void copyEpub(Context context, User oUser)
    {
        if (FlipickStaticData.TYPE != null)
        {
            if (FlipickStaticData.TYPE.equalsIgnoreCase("application/epub+zip"))
            {
                JobInfo oEpubJob = new JobInfo(context);
                File file = new File(FlipickStaticData.DATA.getPath());
                FlipickStaticData.progressMessage = FlipickStaticData.MsgOpeningBook;
                FlipickUtil.StartProgressBar(context);

                FlipickStaticData.DeviceWidth = FlipickConfig.getDeviceWidth(context);
                FlipickStaticData.DeviceHeight = FlipickConfig.getDeviceHeight(context);
                FlipickStaticData.DecoFinal = FlipickUtil.getNavigationBarHeight(context);
                FlipickStaticData.ThreadMessage = "";
                new CopyAndShowLocalEpub(oEpubJob, context, oUser).execute(file);
            }
        }
    }

    public class CopyAndShowLocalEpub extends AsyncTask<File, String, String>
    {
        JobInfo oEpubJob;
        Context context;
        User oUser;

        public CopyAndShowLocalEpub(JobInfo oEpubJob, Context context, User oUser)
        {
            this.oEpubJob = oEpubJob;
            this.context = context;
            this.oUser = oUser;
        }

        @Override
        protected String doInBackground(File... params)
        {

            Log.d(getClass().getName(), "selected file " + params[0].toString());
            try
            {

                oEpubJob.oUserinfo = oUser;

                final int MAX_VALUE = 9999;
                Random rand = new Random();
                int randValue = rand.nextInt(MAX_VALUE);
                String Title = params[0].getName().split("\\.")[0];
                oEpubJob.JobId = Integer.toString(randValue);
                oEpubJob.IsSideLoaded = "Yes";
                oEpubJob.IsExamHall = "No";
                oEpubJob.BookType = "EPUB";
                oEpubJob.HasBookDownloaded = false;
                oEpubJob.HasProductUpdated = false;

                FlipickConfig.Part2ContentRootPath = oUser.UserName.trim();
                FlipickConfig.ContentRootPath = FlipickConfig.Part1ContentRootPath + "/" + FlipickConfig.Part2ContentRootPath;

                String path = FlipickConfig.ContentRootPath + "/" + oEpubJob.JobId;
                File EPUBDirectory = new File(path);
                if (EPUBDirectory.exists()) FlipickIO.DeleteDirectoryRecursive(EPUBDirectory);
                oEpubJob.CopyLocalZipToAppLocation(params[0].getAbsoluteFile(), oEpubJob.JobId, context);
                oEpubJob.RenameJobFolder(oEpubJob.TempPath);
                oEpubJob.DeleteZipFile();

                if (!oEpubJob.SideLoadCoverImage.equals(""))
                {
                    oEpubJob.JobUrl = path + "/" + oEpubJob.PathOPS + "/" + oEpubJob.SideLoadCoverImage;
                    oEpubJob.JobThumbnailPath = path + "/" + oEpubJob.PathOPS + "/" + oEpubJob.SideLoadCoverImage;
                }
                try
                {
                    oEpubJob.deleteJobInforFromDB(oEpubJob);
                    oEpubJob.addJobInfoInDB(oEpubJob, null);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                FlipickStaticData.progressMessage = FlipickStaticData.MsgOpeningBook;
                FlipickStaticData.ThreadMessage = "FINISH";

            }
            catch (Exception e)
            {
                FlipickStaticData.ThreadMessage = e.getMessage();
                e.printStackTrace();
            }
            catch (Throwable e)
            {
                FlipickStaticData.ThreadMessage = e.getMessage();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            if (FlipickStaticData.ThreadMessage.equals("FINISH"))
            {
                ((Activity) context).finish();
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
            else if (FlipickStaticData.ThreadMessage.indexOf("ETIMEDOUT") != -1)
            {
                FlipickError.DisplayErrorAsLongToast(context, "Connection lost while downloading book.");
                ((Activity) context).finish();
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
            else
            {
                FlipickError.DisplayErrorAsLongToast(context, FlipickStaticData.ThreadMessage);
            }
            FlipickStaticData.progressMessage = "";
            FlipickStaticData.ThreadMessage = "";
        }
    }
}
