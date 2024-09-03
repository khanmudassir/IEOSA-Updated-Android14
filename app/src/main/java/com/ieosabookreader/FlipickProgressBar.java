package com.ieosabookreader;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FlipickProgressBar extends AppCompatActivity {

    private ProgressBar mProgress;
    private int mProgressStatus = 0;
    private TextView txtprogress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progressbaractivity);
        mProgress = (ProgressBar) findViewById(R.id.progressbar_activity);
        txtprogress = (TextView) findViewById(R.id.progress_status);
        mProgress.setMax(100);

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
                        StopProgressBar();
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
                            StopProgressBar();
                            return;
                        }
                        TextView txt_progress = (TextView) findViewById(R.id.progress_status);
                        txt_progress.setText(FlipickStaticData.progressMessage);
                    }
                });
            }
        }).start();

    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    public void StopProgressBar()
    {
        FlipickProgressBar.this.finish();
        FlipickStaticData.progressMessage = "";
    }

    public static void StartProgressBar(Activity activity)
    {
        Intent intent = new Intent(activity, FlipickProgressBar.class);
        activity.startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        return;
    }

}