package com.ieosabookreader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ArrayList<JobInfo> jobArray = new ArrayList<JobInfo>();
    ArrayList<JobInfo> jobEpubBookArray = new ArrayList<JobInfo>();
    final Context context = this;
    public CourseListAdapter courseListAdapter;
    User oUser = null;
    JobInfo oEpubJob = null;
    ProgressBar progressBar;
    RelativeLayout Layoutgridview;
    DownloadEpubAsynTask oDownloadEpubAsynTask = null;

    public String Action = "";

    private GridView book_list_view;

    public MainActivity oMainActivity = null;
    TextView txt_title_main;
    Boolean tabletSize;

    Dialog dialog;
    int orientation;
    int width = 0;
    int height = 0;
    Spinner spinner1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try
        {
            // clear static used in login
            FlipickStaticData.CallFrom = "";
            FlipickStaticData.AsynRunningActivity = "";
            super.onCreate(savedInstanceState);
            if (FlipickConfig.ContentRootPath == null || FlipickConfig.ContentRootPath.equals(""))
            {
                ReloadApp();
                return;
            }

            oMainActivity = this;
            oUser = new User(this);
            tabletSize = getResources().getBoolean(R.bool.isTablet);

            setContentView(R.layout.activity_main);
            if (!getResources().getBoolean(R.bool.isTablet)) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

            CreateMenu();

            oEpubJob = new JobInfo(this);
            Layoutgridview = (RelativeLayout) findViewById(R.id.layoutGridview);
            txt_title_main = (TextView) findViewById(R.id.txt_title_main);
            book_list_view = (GridView) findViewById(R.id.grid_view);

            orientation = getScreenOrientation();
            Bundle oBundle = getIntent().getExtras();

            if (oBundle != null) {
                Action = oBundle.getString("ACTION");
            }

            if (orientation == 0 || orientation == 8)
            {
                if (tabletSize) book_list_view.setColumnWidth(280);
            }

            if (tabletSize)
            {
                book_list_view.setNumColumns(3);
            }

            oUser.GetUserInfo();
            FlipickConfig.Part2ContentRootPath = oUser.UserName.trim();
            FlipickConfig.ContentRootPath = FlipickConfig.Part1ContentRootPath + "/" + FlipickConfig.Part2ContentRootPath;
            jobArray = null;
            jobArray = oEpubJob.getJobInfoFromDB();

            if (jobArray.size() == 0) {
                AlertDialog.Builder b = new AlertDialog.Builder(this);
                AlertDialog dialog = b.create();
                dialog.setMessage("You don't have a book to read." + "\n" + "To purchase a book, please click on the Store button." + "\n"
                        + "If you have already purchased a book then click on Refresh button." + "\n" + "If you have redeem access code then click on the Redeem Access Code button.");
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);
                dialog.show();
            }

            if (!Action.equals("") && Action.equals("RefreshLibrary")) {
                BindeBookJobGridView();
                // UpdateCategoryFilter();
            }

            else if (Action.equals("RefreshJobXml")) {
                if (!ConnectionDetector.IsInternetAvailable(getApplicationContext()))
                {
                    ConnectionDetector.DisplayInternetErrorMessage(getApplicationContext());
                    return;
                }

                jobArray = jobEpubBookArray;
                RefreshJobList();
            }

            else if (Action.equals("")) {
                BindeBookJobGridView();
                // UpdateCategoryFilter();
            }

            FlipickStaticData.CallFrom = "";

        }
        catch (Throwable E) {
            FlipickError.DisplayErrorAsLongToast(getApplicationContext(), E.getMessage());
            return;
        }

        progressBar = (ProgressBar) findViewById(R.id.progressbar_activity);

    }

    public void UpdateCategoryFilter()
    {
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        oEpubJob.listCategoryNames.clear();
        oEpubJob.listCategoryIds.clear();
        ArrayList<String> categories = new ArrayList<String>();
        ArrayList<String> categoryIds = new ArrayList<String>();
        categories = oEpubJob.getDistinctCategory(context);
        categories.add(0, "All");
        // oEpubJob.listCategoryNames.add(0, "All");
        String categoryName = categories.get(0).toString();
        categoryIds = oEpubJob.getCategoryID(context, categoryName);
        categoryIds.add(0, "0");
        // oEpubJob.listCategoryIds.add(0, "0");
        // FlipickIO.CopyDataBaseToSdCard(context);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(spinnerArrayAdapter);
        spinner1.setOnItemSelectedListener(new OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                Object item = adapterView.getItemAtPosition(position);
                if (item != null)
                {
                    if (!item.toString().equalsIgnoreCase("All"))
                    {
                        ArrayList<String> categoryIds = new ArrayList<String>();
                        categoryIds = oEpubJob.getCategoryID(context, item.toString());
                        String categoryID = "";
                        for (int i = 0; i < categoryIds.size(); i++)
                        {
                            categoryID = categoryIds.get(i);
                            break;
                        }
                        jobArray = oEpubJob.getCategoryJobInfoFromDB(categoryID);
                        BindeBookJobGridView();
                        Toast.makeText(MainActivity.this, item.toString(), Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if (item.toString().equalsIgnoreCase("All"))
                        {
                            jobArray = oEpubJob.getJobInfoFromDB();
                            BindeBookJobGridView();
                        }
                    }

                }
                // Toast.makeText(MainActivity.this, "Selected",
                // Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                // TODO Auto-generated method stub

            }
        });
    }

    public int getScreenOrientation()
    {
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;

        // if the device's natural orientation is portrait:
        if ((rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) && height > width || (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) && width > height)
        {
            switch (rotation)
            {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_180:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                case Surface.ROTATION_270:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                default:

                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
            }
        }
        // if the device's natural orientation is landscape or if the device
        // is square:
        else
        {
            switch (rotation)
            {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_180:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                case Surface.ROTATION_270:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                default:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
            }
        }

        return orientation;
    }

    private void ReloadApp()
    {
        Thread background = new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    Thread.sleep(500);
                    finish();
                    Intent homepage = new Intent(getApplicationContext(), RunFlipickApp.class);
                    homepage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(homepage);

                    Log.e("TAG", "RunFlipickApp clled");
                }
                catch (Throwable t)
                {
                    t.printStackTrace();
                }
            }
        });

        background.start();
    }

    public void BacktoLibrary()
    {
        Layoutgridview.setVisibility(RelativeLayout.VISIBLE);
        ImageView btn_refresh = (ImageView) findViewById(R.id.btn_refresh);
        btn_refresh.setVisibility(View.VISIBLE);
    }

    private void CreateMenu()
    {
        ImageView btn_landingPage = (ImageView) findViewById(R.id.btn_landingpage);
        btn_landingPage.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                Intent oIntent = new Intent(context, LandingPage_Activity.class);
                startActivity(oIntent);
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_from_right);
                finish();
            }
        });

        ImageView btn_Refresh = (ImageView) findViewById(R.id.btn_refresh);

        btn_Refresh.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!ConnectionDetector.IsInternetAvailable(getApplicationContext()))
                {
                    ConnectionDetector.DisplayInternetErrorMessage(getApplicationContext());
                    return;
                }

                jobArray = jobEpubBookArray;
                RefreshJobList();

            }
        });
    }

    @Override
    public void onBackPressed()
    {
        Intent oIntent = new Intent(this, LandingPage_Activity.class);
        startActivity(oIntent);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_from_right);
        finish();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~DownloadEpubAsynTask
    // Starts~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @SuppressLint("DefaultLocale")
    private class DownloadEpubAsynTask extends AsyncTask<String, Void, String>
    {
        boolean is_update = false;

        @Override
        protected void onPreExecute()
        {
            // TODO Auto-generated method stub
            super.onPreExecute();
            FlipickStaticData.progressMessage = FlipickStaticData.MsgDownload + "0%";
            FlipickUtil.StartProgressBar(context);
        }

        @SuppressLint("DefaultLocale")
        @Override
        protected String doInBackground(String... urls)
        {
            try
            {
                if (urls.length > 0 && urls[0].equalsIgnoreCase("Update")) is_update = true;
                FlipickStaticData.ThreadMessage = "";
                FlipickStaticData.AsynRunningActivity = "DOWNLOAD";
                if (!(oEpubJob.IsSideLoaded).equals("Yes"))
                {
                    if (!ConnectionDetector.IsInternetAvailable(getApplicationContext()))
                    {
                        FlipickStaticData.ThreadMessage = ConnectionDetector.GetInternetErrorMessage();
                        return "";
                    }
                }

                oEpubJob.DownloadEPUBFile(context);

                if (is_update)
                {
                    oEpubJob.HasProductUpdated = false;
                }
                if (!(oEpubJob.BookType).equals("REFLOWABLE"))
                {
                    oEpubJob.HasBookDownloaded = true;

                    JobInfo info = new JobInfo(MainActivity.this);
                    info.updateJobInDB(oEpubJob);
                    oEpubJob.RenameJobFolder(oEpubJob.TempPath);
                    oEpubJob.DeleteZipFile();
                    // if (!(oEpubJob.IsSideLoaded).equals("Yes"))
                    // {
                    // oEpubJob.DonwloadConfirmation();
                    // }
                    if (oEpubJob.IsSideLoaded.equals("No"))
                    {
                        FlipickIO.DeleteDirectoryRecursive(new File(oEpubJob.TempPath));
                    }
                }
                if ((oEpubJob.BookType).equals("REFLOWABLE"))
                {
                    // backup of original files
                    oEpubJob.BackupOriginalFiles(oEpubJob.JobId, oEpubJob.TempPath);
                }
                FlipickStaticData.ThreadMessage = "FINISH";
            }
            catch (Exception e)
            {
                FlipickStaticData.ThreadMessage = e.getMessage() + ":" + e.getCause();
            }
            catch (Throwable e)
            {
                FlipickStaticData.ThreadMessage = e.getMessage() + ":" + e.getCause();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result)
        {
            FlipickStaticData.AsynRunningActivity = "";

            if (FlipickStaticData.ThreadMessage.equals("FINISH") && (oEpubJob.BookType).equals("REFLOWABLE"))
            {
                FlipickStaticData.progressMessage = "STOP";

                Intent intpagination = new Intent(getApplicationContext(), PaginationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("JobId", oEpubJob.JobId);
                bundle.putString("Activity", "MainActivity");
                bundle.putString("TempPath", oEpubJob.TempPath);
                intpagination.putExtras(bundle);
                startActivity(intpagination);
                finish();
            }

            else if (FlipickStaticData.ThreadMessage.equals("FINISH"))
            {
                FlipickStaticData.progressMessage = "STOP";
                Intent oIntent = new Intent(getApplicationContext(), flipickpageviewer.class);
                oIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                bundle.putString("JobId", oEpubJob.JobId);
                bundle.putString("ACTION", "");
                bundle.putString("PageLink", "");
                oIntent.putExtras(bundle);
                startActivity(oIntent);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_left);
                finish();

            }
            else if (FlipickStaticData.ThreadMessage.indexOf("ETIMEDOUT") != -1)
            {
                if (is_update)
                {
                    oEpubJob.HasProductUpdated = false;
                    oEpubJob.HasBookDownloaded = false;
                    JobInfo info = new JobInfo(MainActivity.this);
                    info.updateJobInDB(oEpubJob);
                }
                FlipickError.DisplayErrorAsLongToast(getBaseContext(), "Connection lost while downloading book.");
                finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else
            {
                oEpubJob.HasProductUpdated = false;
                oEpubJob.HasBookDownloaded = false;
                JobInfo info = new JobInfo(MainActivity.this);
                info.updateJobInDB(oEpubJob);
                FlipickError.DisplayErrorAsLongToast(getApplicationContext(), FlipickStaticData.ThreadMessage);
                finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
            FlipickStaticData.progressMessage = "";
            FlipickStaticData.ThreadMessage = "";
        }
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~DownloadEpubAsynTask
    // Ends~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public void RefreshJobList()
    {
        if (!ConnectionDetector.IsInternetAvailable(getApplicationContext()))
        {
            ConnectionDetector.DisplayInternetErrorMessage(getApplicationContext());
            return;
        }
        new RefreshAsyncTask(MainActivity.this, oUser).execute();// ,"MainActivity","").execute();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ IMAGE BINDING
    // GRIDVIEW~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private void BindeBookJobGridView()
    {
        courseListAdapter = new CourseListAdapter(context);
        courseListAdapter.changeDataSet(jobArray);
        book_list_view.setAdapter(courseListAdapter);
    }

    public void DownloadAndLaunchPager(int position, String Action) throws Exception
    {
        try
        {
            FlipickStaticData.oJobInfo.PageXHTMLList.clear();
            FlipickStaticData.oJobInfo.PaginationXHTMLList.clear();
            FlipickStaticData.oJobInfo.ReflowColumnShift.clear();
            FlipickStaticData.oJobInfo.ReflowNewFileNames.clear();
            FlipickStaticData.oJobInfo.XHTMLMeasuredWidth.clear();
            FlipickStaticData.oJobInfo.PageXHTML_LinkIds.clear();
            FlipickStaticData.oJobInfo.LinkedIDsOnPageIndex.clear();
            FlipickStaticData.oJobInfo.LinkedIDs.clear();

            oEpubJob = jobArray.get(position);
            if (oEpubJob.JobId.equals("")) return;

            if (Action.equals("Update")) oEpubJob.DeletejobFolder();

            FlipickStaticData.DeviceWidth = FlipickConfig.getDeviceWidth(context);
            FlipickStaticData.DeviceHeight = FlipickConfig.getDeviceHeight(context);
            FlipickStaticData.DecoFinal = FlipickUtil.getNavigationBarHeight(context);
            FlipickStaticData.ThreadMessage = "";

            oEpubJob.oUserinfo = oUser;
            // FlipickStaticData.oJobInfo.oUserinfo = oEpubJob.oUserinfo;

            oDownloadEpubAsynTask = new DownloadEpubAsynTask();
            oDownloadEpubAsynTask.execute(new String[]
                    { Action });

        }
        catch (Throwable e)
        {
            FlipickError.DisplayErrorAsLongToast(getApplicationContext(), e.getMessage());
        }
    }

    public void LaunchPager(JobInfo oEpubJob)
    {
        try
        {
            FlipickStaticData.progressMessage = "";
            oEpubJob.HasBookDownloaded = true;
            Intent oIntent = new Intent(getApplicationContext(), flipickpageviewer.class);
            oIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle bundle = new Bundle();
            bundle.putString("JobId", oEpubJob.JobId);
            bundle.putString("Activity", "MainActivity");
            bundle.putString("TransitionType", "Right_In_Left_Out");
            oIntent.putExtras(bundle);
            startActivity(oIntent);
            finish();

        }
        catch (Exception e)
        {
            FlipickError.DisplayErrorAsLongToast(getApplicationContext(), e.getMessage());
        }
    }

    public void DeleteSlideJobEntryDialog(final View view)
    {
        AlertDialog.Builder dalertDialog = new AlertDialog.Builder(MainActivity.this);
        dalertDialog.setMessage("Are you sure you want to delete ?");
        dalertDialog.setIcon(R.drawable.app_icon);
        dalertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                // JobInfo jobInfo = jobArray.get((Integer) view.getTag());
                JobInfo jobInfo = jobArray.get((Integer) view.getId());
                new DeleteSlideJobEntry().execute(jobInfo);

            }
        });
        dalertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
                view.setClickable(true);
            }
        });
        dalertDialog.show();
    }

    public class DeleteSlideJobEntry extends AsyncTask<Object, Void, Object>
    {
        JobInfo jobInfo = new JobInfo();

        @Override
        protected void onPreExecute()
        {
            FlipickStaticData.progressMessage = "Please wait...";
            FlipickProgressBar.StartProgressBar((Activity) context);
            super.onPreExecute();
        }

        protected Object doInBackground(Object... params)
        {
            jobInfo = (JobInfo) params[0];
            try
            {
                FlipickIO.DeleteDirectoryRecursive(new File(FlipickConfig.ContentRootPath + "/" + jobInfo.JobId));
                oEpubJob.deleteJobInforFromDB(jobInfo);
                jobArray = oEpubJob.getJobInfoFromDB();
            }
            catch (Throwable e)
            {
                // e.printStackTrace();
                if (e.getMessage() == null)
                {
                    FlipickStaticData.ThreadMessage = "an error occurred.";
                }
                else
                {
                    FlipickStaticData.ThreadMessage = e.getMessage();
                }
            }
            return "";
        }

        protected void onPostExecute(Object result)
        {
            super.onPostExecute(result);
            FlipickStaticData.progressMessage = "";

            if (FlipickStaticData.ThreadMessage.trim().length() > 0)
            {
                FlipickError.DisplayErrorAsToast(context, FlipickStaticData.ThreadMessage);
                FlipickStaticData.ThreadMessage = "";
            }
            else
            {
                BindeBookJobGridView();
            }
        }
    }

}