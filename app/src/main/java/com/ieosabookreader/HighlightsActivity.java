package com.ieosabookreader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class HighlightsActivity extends AppCompatActivity implements View.OnClickListener {

    private List<Highlights> highlights;
    TextView txt_screen_title;
    private LinearLayout layout_navigation_button;
    Boolean tabletSize;
    Context context;
    private ListView highlights_listview;
    int orientation;
    String currentPage;
    private RelativeLayout layout_btn_all;
    String FromActivity;
    HighlightListAdapter cHighlightListadapter;
    RelativeLayout layout_txt_fontSetting, layout_btn_toc, layout_btn_library, layout_btn_bookmark;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highlights_grid_activity);
        context = HighlightsActivity.this;
        if (!getResources().getBoolean(R.bool.isTablet) || FlipickStaticData.BookType.equals("REFLOWABLE"))
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setupUI();
        getHighlights();
        Bundle oBundle = getIntent().getExtras();
        if (oBundle != null && oBundle.getString("PageLink") != null)
        {
            currentPage = oBundle.getString("PageLink");
        }
    }

    private void setupUI()
    {
        orientation = getScreenOrientation();
        tabletSize = getResources().getBoolean(R.bool.isTablet);
        txt_screen_title = (TextView) findViewById(R.id.txt_booktitle);
        layout_btn_all = (RelativeLayout) findViewById(R.id.layout_btn_all);
        layout_navigation_button = (LinearLayout) findViewById(R.id.layout_back_navigation);
        highlights_listview = (ListView) findViewById(R.id.highlights_listview);

        layout_txt_fontSetting = (RelativeLayout) findViewById(R.id.layout_txt_fontSetting);
        layout_btn_library = (RelativeLayout) findViewById(R.id.layout_btn_library);
        layout_btn_toc = (RelativeLayout) findViewById(R.id.layout_btn_toc);

        layout_btn_all = (RelativeLayout) findViewById(R.id.layout_btn_all);

        txt_screen_title.setText("Highlights");
        layout_btn_all.setVisibility(View.VISIBLE);
        layout_btn_all.setOnClickListener(this);
        layout_txt_fontSetting.setVisibility(View.GONE);
        layout_btn_library.setVisibility(View.INVISIBLE);
        layout_btn_toc.setVisibility(View.GONE);
        layout_btn_bookmark = (RelativeLayout) findViewById(R.id.layout_btn_bookmark);
        layout_btn_bookmark.setVisibility(View.GONE);
        layout_navigation_button.setVisibility(View.VISIBLE);
        layout_navigation_button.setOnClickListener(this);
    }

    private int getScreenOrientation()
    {
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

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

    private void getHighlights()
    {
        new Highlights();
        highlights = Highlights.GetobjHighlightsFromDB(getApplicationContext(), FlipickStaticData.JobId);

        cHighlightListadapter = new HighlightListAdapter(context);
        cHighlightListadapter.changeDataSet(highlights);
        highlights_listview.setAdapter(cHighlightListadapter);

        highlights_listview.setOnItemClickListener(new OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                String PageNo = highlights.get(position).getHighlightsPageNo();// PageNoList.get(position);
                Intent oIntent = new Intent(getApplicationContext(), flipickpageviewer.class);
                oIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                bundle.putString("PageLink", PageNo);
                bundle.putString("Activity", "HighlightActivity");
                bundle.putString("JobId", FlipickStaticData.oJobInfo.JobId);

                oIntent.putExtras(bundle);
                startActivity(oIntent);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_left);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        Intent oIntent = new Intent(getApplicationContext(), flipickpageviewer.class);
        oIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString("PageLink", currentPage);
        bundle.putString("Activity", "HighlightActivity");
        bundle.putString("JobId", FlipickStaticData.oJobInfo.JobId);
        oIntent.putExtras(bundle);
        startActivity(oIntent);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_from_right);
        finish();
    }

    @Override
    public void onClick(View view)
    {
        // TODO Auto-generated method stub
        if (view == layout_navigation_button)
        {
            Intent oIntent = new Intent(getApplicationContext(), flipickpageviewer.class);
            oIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle bundle = new Bundle();
            bundle.putString("PageLink", currentPage);
            bundle.putString("Activity", "HighlightActivity");
            bundle.putString("JobId", FlipickStaticData.oJobInfo.JobId);
            oIntent.putExtras(bundle);
            startActivity(oIntent);
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_from_right);
            finish();

        }
        if (view == layout_btn_all)
        {
            ((FlipickApplication) getApplicationContext()).setupPopupWindowOnSettingsAll(view, context, currentPage + "", "","");
        }
    }
}