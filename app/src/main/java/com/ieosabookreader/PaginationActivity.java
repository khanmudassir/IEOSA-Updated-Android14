package com.ieosabookreader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import java.io.File;
import android.annotation.SuppressLint;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xwalk.core.XWalkResourceClient;
import org.xwalk.core.XWalkView;


public class PaginationActivity extends AppCompatActivity {

    String Current_PageLink = "";
    int PageIndex = 0;
    String JobId = "";
    String PageUrl;
    String FromActivity = null;
    String TempPath = null;

    XWalkView xWalkWebView;
    Context context;

    // Progress bar var deifined
    private ProgressBar progress_bar;
    RelativeLayout layout_progressbar;
    private int mProgressStatus = 0;

    @SuppressLint("SetJavaScriptEnabled")
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webviewpaginationpopup);
        context = this;
        try
        {
            xWalkWebView = (XWalkView)findViewById(R.id.webviewpagination_popup);
            xWalkWebView.addJavascriptInterface(new JavascriptInterface_Pagination(this), "AndroidFunction");
            xWalkWebView.setResourceClient(new XWalkResourceClientBase(xWalkWebView));

            progress_bar = (ProgressBar) findViewById(R.id.progressbar_activity);
            layout_progressbar = (RelativeLayout) findViewById(R.id.layout_progress);

            Bundle oBundle = getIntent().getExtras();
            if (oBundle != null && oBundle.getString("JobId") != null)
            {
                JobId = oBundle.getString("JobId");
            }
            if (oBundle != null && oBundle.getString("Activity") != null)
            {
                FromActivity = oBundle.getString("Activity");
            }

            if (oBundle != null && oBundle.getString("TempPath") != null)
            {
                TempPath = oBundle.getString("TempPath");
            }
            FlipickStaticData.oJobInfo.JobId = JobId;

            PopulateArrayListAsynTask oPopulateArrayListAsynTask = new PopulateArrayListAsynTask();
            oPopulateArrayListAsynTask.execute();
        }
        catch (Exception e)
        {
            FlipickError.DisplayErrorAsLongToast(getApplicationContext(), e.getMessage());
        }
    }

    public void updateProgress(final String stmessage)
    {

        new Thread(new Runnable()
        {
            public void run()
            {
                while (mProgressStatus < 100)
                {
                    if (!FlipickStaticData.progressMessage.equals(""))
                    {
                        updateProgress();
                        try
                        {
                            Thread.sleep(1000);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        FlipickStaticData.progressMessage = "";
                        break;
                    }
                }
            }

            public void updateProgress()
            {
                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        if (FlipickStaticData.progressMessage.equals("STOP"))
                        {
                            FlipickStaticData.progressMessage = "";
                            return;
                        }
                        TextView txt_progress = (TextView) findViewById(R.id.progress_status);
                        txt_progress.setText(FlipickStaticData.progressMessage);
                    }
                });
            }
        }).start();

    }

    class XWalkResourceClientBase extends XWalkResourceClient
    {

        public XWalkResourceClientBase(XWalkView mXWalkView)
        {
            super(mXWalkView);
        }

        @Override
        public boolean shouldOverrideUrlLoading(XWalkView view, String url)
        {
            return false;
        }
    }

    public void SetUrlToWebview()
    {
        PageUrl = FlipickStaticData.oJobInfo.PaginationXHTMLList.get(PageIndex);
        xWalkWebView.load(PageUrl, null);
    }

    public String GetPageUrl()
    {
        try
        {
            if (FlipickStaticData.oJobInfo.PaginationXHTMLList.size() == PageIndex || PageIndex > FlipickStaticData.oJobInfo.PaginationXHTMLList.size())
            {
                if (FromActivity.equals("flipickpageviewer") || FromActivity.equals("SynchUpdate"))
                {
                    FlipickStaticData.progressMessage = "Please wait..100%";
                    updateProgress(FlipickStaticData.progressMessage);
                }
                else
                {
                    FlipickStaticData.progressMessage = "Pagination......  100%";
                    updateProgress(FlipickStaticData.progressMessage);
                }

                if (!getResources().getBoolean(R.bool.isTablet))// mobile
                {
                    FlipickStaticData.oJobInfo.CreateCssForReflowableBook(getApplicationContext(), TempPath, FlipickStaticData.DefaultFontSizeMobile + "em", FromActivity);
                }
                else
                {
                    FlipickStaticData.oJobInfo.CreateCssForReflowableBook(getApplicationContext(), TempPath, FlipickStaticData.DefaultFontSizeTablet + "em", FromActivity);

                }

                // after download and pagination successfully completed update
                // info
                if (FromActivity.equals("MainActivity"))
                {
                    FlipickStaticData.oJobInfo.HasBookDownloaded = true;

                    JobInfo info = new JobInfo(PaginationActivity.this);
                    info.updateJobInDB(FlipickStaticData.oJobInfo);
                    FlipickStaticData.oJobInfo.RenameJobFolder(TempPath);
                    FlipickStaticData.oJobInfo.DeleteZipFile();
                    if (!(FlipickStaticData.oJobInfo.IsSideLoaded).equals("Yes"))
                    {
                        // FlipickStaticData.oJobInfo.DonwloadConfirmation();
                        try
                        {
                            //info.DonwloadConfirmation();
                        }
                        catch (Throwable e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                else if (FromActivity.equals("flipickpageviewer") || FromActivity.equals("SynchUpdate"))
                {
                    FlipickIO.DeleteDirectoryRecursive(new File(FlipickConfig.ContentRootPath + "/" + JobId));
                    FlipickStaticData.oJobInfo.RenameJobFolder(TempPath);
                }
                if (FromActivity.equals("SynchUpdate"))
                {
                    FlipickStaticData.oJobInfo.updateUpdateNewVersionInJobDB("D", JobId, context);
                }

                // remove all highlighted data from table
                Highlights highlight = new Highlights();
                highlight.deleteHighlightsfromDBByJobId(context, JobId);

                // remove all notes data from table

                Notes notes = new Notes();
                notes.deleteNotesfromDBByJobId(context, JobId);

                // remove all bookmark data from table

                Bookmark bookmark = new Bookmark();
                bookmark.deleteBookmarkfromDBByJobId(context, JobId);

                // clear timer
                runOnUiThread(new Runnable()
                {
                    @JavascriptInterface
                    public void run()
                    {
                        xWalkWebView.load("javascript:clearTimer()", null);
                    }
                });

                // clear all array of jobinfo
                FlipickStaticData.oJobInfo.PageXHTMLList.clear();
                FlipickStaticData.oJobInfo.PaginationXHTMLList.clear();
                FlipickStaticData.oJobInfo.ReflowColumnShift.clear();
                FlipickStaticData.oJobInfo.ReflowNewFileNames.clear();
                FlipickStaticData.oJobInfo.XHTMLMeasuredWidth.clear();
                FlipickStaticData.oJobInfo.PageXHTML_LinkIds.clear();
                FlipickStaticData.oJobInfo.LinkedIDsOnPageIndex.clear();
                FlipickStaticData.oJobInfo.LinkedIDs.clear();

                FlipickStaticData.progressMessage = "STOP";
                Intent oIntent = new Intent(getApplicationContext(), flipickpageviewer.class);
                oIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                bundle.putString("JobId", JobId);
                bundle.putString("Activity", "PaginationActivity");
                bundle.putString("TransitionType", "Right_In_Left_Out");
                oIntent.putExtras(bundle);
                startActivity(oIntent);
                finish();

                FlipickStaticData.progressMessage = "";

//				runOnUiThread(new Runnable()
//				{
//					@JavascriptInterface
//					public void run()
//					{
                progress_bar.setVisibility(View.GONE);
                layout_progressbar.setVisibility(View.GONE);
//					}
//				});

                return "";
            }
            else
            {
                String A = FlipickStaticData.oJobInfo.PaginationXHTMLList.get(PageIndex);
                return A;
            }
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            Log.v("e.getMessage()", e.getMessage());
            // FlipickError.DisplayErrorAsLongToast(getApplicationContext(),
            // e.getMessage());
            return "";
        }

    }

    public String GetLinkedIds()
    {
        return FlipickStaticData.oJobInfo.PageXHTML_LinkIds.get(PageIndex);
    }

    public void SetPageLinkedIds(String LinkedPageIndex)
    {
        FlipickStaticData.oJobInfo.LinkedIDsOnPageIndex.add(LinkedPageIndex);
    }

    public void SetPageWidth(String Measuredwidth)
    {
        try
        {
            FlipickStaticData.oJobInfo.XHTMLMeasuredWidth.add(Integer.parseInt(Measuredwidth));

            if (FromActivity.equals("flipickpageviewer") || FromActivity.equals("SynchUpdate"))
            {
                FlipickStaticData.progressMessage = "Please wait.." + (int) ((PageIndex * 100) / FlipickStaticData.oJobInfo.PaginationXHTMLList.size()) + "%";
                updateProgress(FlipickStaticData.progressMessage);
            }
            else
            {
                FlipickStaticData.progressMessage = "Pagination... " + (int) ((PageIndex * 100) / FlipickStaticData.oJobInfo.PaginationXHTMLList.size()) + "%";
                updateProgress(FlipickStaticData.progressMessage);
            }

            PageIndex++;

        }
        catch (Exception e)
        {
            // TODO: handle exception
            // FlipickError.DisplayErrorAsLongToast(getApplicationContext(),
            // e.getMessage());
        }
    }

    private class PopulateArrayListAsynTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected void onPreExecute()
        {
            // TODO Auto-generated method stub
            super.onPreExecute();

            layout_progressbar.setVisibility(View.VISIBLE);
            progress_bar.setVisibility(View.VISIBLE);

            if (FromActivity.equals("flipickpageviewer") || FromActivity.equals("SynchUpdate"))
            {
                FlipickStaticData.progressMessage = "Please wait..";
                updateProgress(FlipickStaticData.progressMessage);
            }
            else
            {
                FlipickStaticData.progressMessage = "Please wait..";
                updateProgress(FlipickStaticData.progressMessage);

            }
        }

        @Override
        protected String doInBackground(String... urls)
        {
            try
            {

                FlipickStaticData.oJobInfo.LinkedIDsOnPageIndex.clear();
                FlipickStaticData.oJobInfo.PopulatePageXHTMLArray2(TempPath, FromActivity);

                FlipickStaticData.oJobInfo.CreatePagesForPagination(getApplicationContext(), FromActivity);

            }
            catch (Exception e)
            {
                FlipickError.DisplayErrorAsLongToast(getApplicationContext(), e.getMessage());
            }
            catch (Throwable e)
            {
                FlipickError.DisplayErrorAsLongToast(getApplicationContext(), e.getMessage());
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result)
        {
            SetUrlToWebview();

        }
    }

}