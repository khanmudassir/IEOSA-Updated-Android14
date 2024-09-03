package com.ieosabookreader;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.animation.Animation;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import org.xwalk.core.XWalkView;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xwalk.core.JavascriptInterface;
import org.xwalk.core.XWalkView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;


import android.text.Html;
import android.view.ActionMode;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ieosabookreader.customactionbar.CustomActionBar;


public class flipickpageviewer extends FragmentActivity implements Animation.AnimationListener {

    public ViewPager pager;
    FragmentStatePagerAdapter adapter;
    public int currentpage;
    public String JobId = "";
    public String RequestAction = "";
    public String PageLink = "";
    public String TOCPageLink = "";
    // public String Orientation = "";

    String FromActivity = "";
    CustomActionBar actionBar;
    String Fontsize = "";
    String BackgroundColor = "";
    String FontColor = "";

    String LastColumnNumber = "";
    String LastColumnName = "";
    String Last_font_size = "";
    String UserOrientation = "";
    String Last_BackgroundColor = "";
    boolean HasUserChangeFont = false;

    // Seek bar
    LinearLayout layout_navigation;
    SeekBar navigation_bar;
    boolean IsPageNavBarOn = false;// used in browserfragment
    List<Integer> PageNavigationProgress = new ArrayList<Integer>();;
    int currentNavPageIndex;
    int progress = 0;
    boolean KeepPageNavigationOn = false;
    boolean IsMenuVisible = false;

    // ///////////////////////mobile
    float Mcal_1em;
    float Mcal_11em;
    float Mcal_12em;
    float Mcal_13em;
    float Mcal_14em;
    // //////////////////////tablet
    float Tcal_1em;
    float Tcal_11em;
    float Tcal_12em;
    float Tcal_13em;
    float Tcal_14em;

    // ///////////highlight

    LinearLayout notehighlightholder;
    LinearLayout note_hightlight_layout;
    LinearLayout color_pallet_layout;
    LinearLayout notes_layout;

    EditText edt_notes;
    Boolean ishighlightViewVisible = false;

    String HighlightColor;
    final Context context = this;

    // notes pallet
    LinearLayout notesholder;
    EditText showedit_notes;

    // jobHeight width for pagetofit
    String JobHeight, JobWidth;
    int LastIndexProductBuy = 0;

    String SelectedText = "";

    // Android xwalkwebview declaration
    XWalkView CurrentXwalkWebview = null;

    // Android webview declaration
    WebView CurrentAndroidWebview = null;

    // WebView PreviousAndroidWebview = null;
    // WebView NextAndroidWebview = null;
    public static final int BEHAVIOR_SET_USER_VISIBLE_HINT = 0;

    /**
     * Indicates that only the current fragment will be
     * in the Lifecycle.State#RESUMED state. All other Fragments
     * are capped at Lifecycle.State#STARTED.
     */
    public static final int BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        final Object temp = this;
        super.onCreate(savedInstanceState);

        try
        {
            FlipickStaticData.AsynRunningActivity = "";

            setContentView(R.layout.flipickpageviewer);
            getAllBundleValue();
            GetUserOrientation();

            setupUI();

            if (FlipickStaticData.oJobInfo.Language.equals("ar"))
            {
                FlipickStaticData.oJobInfo.PopulateReversePageXHTMLArray(null, null);
            }
            else
            {
                FlipickStaticData.oJobInfo.PopulatePageXHTMLArray(null, null);
            }

            FlipickStaticData.oJobInfo.populateBookmark(context);

            if (!FlipickStaticData.BookType.equals("REFLOWABLE"))
            {
                if (!UserOrientation.equals(""))
                {
                    if (UserOrientation.equals("2"))
                    {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    }
                    if (UserOrientation.equals("1"))
                    {
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    }
                }
            }
            // if it is reflowable need to change orientation coz default for
            // both (Portrait,landscape)
            if (FlipickStaticData.BookType.equals("REFLOWABLE"))
            {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

            setActionBar();

            // get last index of product buy page
            if (FlipickStaticData.oJobInfo.Is_Free_Trial.equals("Y"))
            {
                LastIndexProductBuy = FlipickStaticData.oJobInfo.PageXHTMLList.size() - 1;
            }

            FlipickStaticData.JobId = FlipickStaticData.oJobInfo.JobId;

            // ///////////////Seek bar
            navigation_bar.setMax(FlipickStaticData.oJobInfo.PageXHTMLList.size());
            setProgressValue(progress);

            navigation_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser)
                {
                    progress = progresValue;
                    setProgressValue(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar)
                {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar)
                {
                    KeepPageNavigationOn = true;
                    if (PageNavigationProgress.size() == 0)
                    {
                        currentNavPageIndex = progress;
                        pager.setCurrentItem(progress);
                        PageNavigationProgress.add(progress);
                    }
                    else
                    {
                        PageNavigationProgress.add(progress);
                    }

                    IsPageNavBarOn = true;
                    PageNavigationProgress.clear();
                }
            });
            // ///////End Seek bar

            pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
            {
                @Override
                public void onPageSelected(final int position)
                {
                    if (TOCPageLink != null && !TOCPageLink.equals(""))
                    {
                        if (Integer.parseInt(TOCPageLink) != position)
                        {
                            FlipickStaticData.IsLastPageToc = false;
                        }
                        else
                        {
                            FlipickStaticData.IsLastPageToc = true;
                        }
                    }
                    else
                    {
                        FlipickStaticData.IsLastPageToc = false;
                    }

                    currentpage = position;
                    FlipickConfig.SavePagerCurrentPage(context, currentpage + "", JobId + "");
                    setVisibility();

                    // ////seekbar
                    progress = currentpage;
                    if (navigation_bar.getVisibility() == View.VISIBLE)
                    {
                        navigation_bar.setProgress(progress);
                        setProgressValue(progress);
                    }
                    // //////end seekbar
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2)
                {
                }

                @Override
                public void onPageScrollStateChanged(int arg0)
                {
                    // TODO Auto-generated method stub
                }
            });

            adapter = new FragmentStatePagerAdapter(getSupportFragmentManager(),BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
            {

                @Override
                public int getCount()
                {
                    // TODO Auto-generated method stub
                    return FlipickStaticData.oJobInfo.PageXHTMLList.size();
                }

                @Override
                public Fragment getItem(int position)
                {
                    // TODO Auto-generated method stub

                    return BrowserFragment.newInstance(FlipickStaticData.oJobInfo.PageXHTMLList.get(position), pager, (flipickpageviewer) temp, PageLink, position);
                }
            };
            pager.setAdapter(adapter);
            setCurrentItemFromDifferentActivity();

        }
        catch (Exception E)
        {
            FlipickError.DisplayErrorAsLongToast(this, E.getMessage());
            super.onBackPressed();
        }

    }

    public void setCurrentItemFromDifferentActivity() throws Exception
    {

        // for arabic language
        if (FlipickStaticData.oJobInfo.Language.equals("ar") && (RequestAction == null || RequestAction.equals("")) && !FromActivity.equals("MainActivity")
                && !FromActivity.equals("Flipickpageviewer") && !FromActivity.equals("RunFlipickApp") && !FromActivity.equalsIgnoreCase("BookmarkActivity"))
        {
            pager.setCurrentItem(FlipickStaticData.oJobInfo.PageXHTMLList.size() - 1, false);
            progress = FlipickStaticData.oJobInfo.PageXHTMLList.size() - 1;
        }
        // RequestAction pass from TOCBrowserFragment.java called from
        // javascript
        else if (RequestAction != null && RequestAction.equals("DISPLAYPAGE"))
        {
            if (FlipickStaticData.BookType.equals("VERTICALSCROLL"))
            {
                SetVerticalPage(PageLink);
            }
            else
            {
                pager.setCurrentItem(Integer.parseInt(PageLink), false);
                progress = Integer.parseInt(PageLink);
            }
            // end seekbar
        }
        else if (FromActivity.equals("RunFlipickApp"))
        {
            int pageno = Integer.parseInt(FlipickConfig.getPagerCurrentPage(context));
            pager.setCurrentItem(pageno, false);
        }
        // get lastpagename user pressback and back from mainActivity
        else if (FromActivity.equals("MainActivity"))
        {
            FlipickStaticData.oJobInfo.GetLastPageName();
            if (!FlipickStaticData.oJobInfo.Last_page_link.equals("null") && !FlipickStaticData.oJobInfo.Last_page_link.equals(""))
            {
                PageLink = FlipickStaticData.oJobInfo.Last_page_link;
            }
            else
            {
                PageLink = "";
            }
            if (!PageLink.equals(""))
            {
                for (int i = 0; i < FlipickStaticData.oJobInfo.PageXHTMLList.size(); i++)
                {
                    if (i == Integer.parseInt(PageLink))
                    {
                        FlipickStaticData.oJobInfo.PageXHTMLList.set(i, FlipickStaticData.oJobInfo.PageXHTMLList.get(i).toString());
                        pager.setCurrentItem(i, false);
                        progress = i;
                        break;
                    }
                }
            }
            else
            {
                if (FlipickStaticData.oJobInfo.Language.equals("ar"))
                {
                    pager.setCurrentItem(FlipickStaticData.oJobInfo.PageXHTMLList.size() - 1, false);
                    // for seekbar
                    progress = FlipickStaticData.oJobInfo.PageXHTMLList.size() - 1;
                    // end seek bar
                }
            }

        }
        // user change font and back from pagination
        else if (FromActivity.equals("PaginationActivity"))
        {
            FlipickStaticData.oJobInfo.getPageIndexAfterFontSize();
            if (!FlipickStaticData.oJobInfo.LastPageFontColumnNo.equals("") && !FlipickStaticData.oJobInfo.LastPageFontPageName.equals(""))
            {
                int currentPageIndex = -1;

                for (int i = 0; i < FlipickStaticData.oJobInfo.PageXHTMLList.size(); i++)
                {
                    File currentFileName = new File(FlipickStaticData.oJobInfo.PageXHTMLList.get(i));
                    if (FlipickStaticData.oJobInfo.LastPageFontPageName.equalsIgnoreCase(currentFileName.getName()))
                    {
                        if (currentPageIndex == -1)
                        {
                            currentPageIndex = Integer.parseInt(FlipickStaticData.oJobInfo.LastPageFontColumnNo) + i;
                            FlipickStaticData.oJobInfo.PageXHTMLList.set(currentPageIndex, FlipickStaticData.oJobInfo.PageXHTMLList.get(currentPageIndex).toString());
                            pager.setCurrentItem(currentPageIndex, false);
                            progress = currentPageIndex;
                            break;
                        }
                    }
                }
            }
        }
        else if (FromActivity.equalsIgnoreCase("HighlightActivity") || FromActivity.equalsIgnoreCase("NotesActivity") || FromActivity.equalsIgnoreCase("BookmarkActivity")
                || FromActivity.equalsIgnoreCase("TOCActivity"))
        {
            pager.setCurrentItem(Integer.parseInt(PageLink));
        }
        else if (FromActivity.equalsIgnoreCase("Flipickpageviewer"))
        {
            if (!PageLink.equals(""))
            {
                pager.setCurrentItem(Integer.parseInt(PageLink));
            }

        }
        else
        {
            pager.setAdapter(adapter);
        }

    }

    public void setVisibility()
    {
        if (LastIndexProductBuy != 0 && FlipickStaticData.oJobInfo.Is_Free_Trial.equals("Y"))
        {
            if (currentpage == LastIndexProductBuy)
            {
                IsMenuVisible = true;
                hideMenu();
            }
            else
            {
                if (IsMenuVisible == true)
                {
                    IsMenuVisible = false;
                    showMenu();
                }
            }
        }
        if (notesholder.getVisibility() == View.VISIBLE)
        {
            notesholder.setVisibility(View.GONE);
        }
        if (notehighlightholder.getVisibility() == View.VISIBLE)
        {
            notehighlightholder.setVisibility(View.GONE);
        }
        if (note_hightlight_layout.getVisibility() == View.VISIBLE)
        {
            note_hightlight_layout.setVisibility(View.GONE);
        }
        if (color_pallet_layout.getVisibility() == View.VISIBLE)
        {
            color_pallet_layout.setVisibility(View.GONE);
        }
        if (actionBar.isVisible())
        {
            actionBar.hideActionBar();
        }
        if (layout_navigation.getVisibility() == View.VISIBLE)
        {
            if (KeepPageNavigationOn == true)
            {
                KeepPageNavigationOn = false;
            }
            else
            {
                hideNavigation();
            }
        }
    }

    public void showMenu()
    {
        if (!IsMenuVisible)
        {
            if (!FlipickStaticData.BookType.equals("REFLOWABLE") && !FlipickStaticData.BookType.equals("VERTICALSCROLL"))
            {
                actionBar.showBookmark();
                actionBar.setLayoutButtonAll(View.VISIBLE);
                actionBar.showfittopage();
                actionBar.showfittowidth();
            }
            else
            {
                actionBar.showBookmark();
                actionBar.setLayoutButtonAll(View.VISIBLE);
                actionBar.showlayoutfontsetting();
            }
        }
    }

    public void hideMenu()
    {
        if (!FlipickStaticData.BookType.equals("REFLOWABLE") && !FlipickStaticData.BookType.equals("VERTICALSCROLL"))
        {
            actionBar.hideBookmark();
            actionBar.invisiblebtnAll();
            actionBar.hidelayoutfittopage();
            actionBar.hidelayoutfittowidth();
        }
        else
        {
            actionBar.hideBookmark();
            actionBar.invisiblebtnAll();
            actionBar.hidelayoutfontsetting();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        if (!FlipickStaticData.BookType.equals("REFLOWABLE"))
        {
            if (UserOrientation.equals("0"))
            {
                if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
                {
                    // ReloadActivity("");
                    ReloadActivity();
                }
                else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
                {
                    // ReloadActivity("");
                    ReloadActivity();
                }
            }
        }
    }

    public void ReloadActivity()
    {

        Intent oIntent = new Intent(context, flipickpageviewer.class);
        oIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString("PageLink", String.valueOf(currentpage));
        bundle.putString("Activity", "Flipickpageviewer");
        // bundle.putString("Orientation", Orientation);
        bundle.putString("JobId", FlipickStaticData.oJobInfo.JobId);
        oIntent.putExtras(bundle);
        startActivity(oIntent);
        this.overridePendingTransition(0, 0);

    }

    // for seekbar
    public void setProgressValue(int progress)
    {
        progress = progress + 1;
        if (progress <= FlipickStaticData.oJobInfo.PageXHTMLList.size())
        {
            if (FlipickStaticData.oJobInfo.Language.equals("ar"))
            {
                ((TextView) findViewById(R.id.txt_PageNo)).setText("Page No. " + ((FlipickStaticData.oJobInfo.PageXHTMLList.size() - progress) + 1) + "" + "/" + navigation_bar.getMax());
            }
            else
            {
                ((TextView) findViewById(R.id.txt_PageNo)).setText("Page No. " + progress + "/" + navigation_bar.getMax());
            }
        }

    }

    public void SetPageNavLink()
    {
        KeepPageNavigationOn = true;
        if (PageNavigationProgress.size() > 0)
        {
            int lastIndex = PageNavigationProgress.get(PageNavigationProgress.size() - 1);
            if (currentpage == lastIndex)
            {
                return;
            }
            else
            {
                currentNavPageIndex = lastIndex;
                pager.setCurrentItem(lastIndex);
            }
        }
    }

    // end seek bar

    /**
     * This method will setup custom action bar for this particular view
     */
    private void setActionBar()
    {
        actionBar = (CustomActionBar) findViewById(R.id.action_bar);

        if (!FlipickStaticData.BookType.equals("REFLOWABLE") && !FlipickStaticData.BookType.equals("VERTICALSCROLL"))
        {
            actionBar.hidelayoutfontsetting();
            actionBar.showfittopage();
            actionBar.showfittowidth();
        }
        ((ImageView) actionBar.getImageButtonBookMark()).setTag(false);

        if (FlipickStaticData.BookType.equals("VERTICALSCROLL"))
        {
            actionBar.hidelayoutfontsetting();
        }
        actionBar.setTitleText(Html.fromHtml(FlipickStaticData.BookTitle).toString());
        ((RelativeLayout) actionBar.getLibraryLayout()).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Back();
            }
        });

        ((RelativeLayout) actionBar.getTocLayout()).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    LastPageOpenLink();
                }
                catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                GoToTOC();
            }
        });

        ((RelativeLayout) actionBar.layout_gettxt_fontsetting()).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                View v1 = pager.findViewWithTag(currentpage);
                if (FlipickStaticData.currentapiVersion < 21)
                {
                    CurrentXwalkWebview = (XWalkView) v1;
                }
                else
                {
                    CurrentAndroidWebview = (WebView) v1;
                }
                HasUserChangeFont = false;
                getFontSetting(v);

            }
        });

        ((RelativeLayout) actionBar.getLayoutButtonAll()).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                ((FlipickApplication) getApplicationContext()).setupPopupWindowOnSettingsAll(v, context, currentpage + "", "", UserOrientation);

            }
        });
        // for fitting option functionality
        ((RelativeLayout) actionBar.layout_get_fittopage()).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    GetJobHeightWidth();

                    float DeviceHeight = FlipickConfig.getDeviceHeight(context);
                    float DeviceWidth = FlipickConfig.getDeviceWidth(context);
                    float ARW = DeviceWidth / Float.parseFloat(JobWidth);
                    float ARH = DeviceHeight / Float.parseFloat(JobHeight);
                    float ARFinal = DeviceHeight / Float.parseFloat(JobHeight);

                    if (ARW < ARH) ARFinal = ARW;
                    else ARFinal = ARH;

                    float Left = ((DeviceWidth - (Float.parseFloat(JobWidth) * ARFinal)) / 2);
                    float Top = ((DeviceHeight - (Float.parseFloat(JobHeight) * ARFinal)) / 2);

                    String defaultCssPortrait = "@media (orientation: portrait) {body > :first-child {position: absolute !important;left:" + Left + "px !important;top:" + Top
                            + "px !important;-webkit-transform-origin:0 0 !important;-webkit-transform:scale(" + ARFinal + ") !important;}}";

                    String defaultCssLandscape = GetJobReverseScaleCSS();
                    String content = defaultCssPortrait + defaultCssLandscape;
                    savecssFile(content);
                    updateUserOrientation("1");
                    // ReloadActivity("");
                    ReloadActivity();
                }
                catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        ((RelativeLayout) actionBar.layout_get_fittowidth()).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    GetJobHeightWidth();
                    float DeviceWidth = FlipickConfig.getDeviceWidth(context);
                    float ARFinal = DeviceWidth / Float.parseFloat(JobWidth);
                    String defaultCssPortrait = "@media (orientation: portrait) {body > :first-child {position: absolute !important;left: 0px !important;top: 0px !important;-webkit-transform-origin:0 0 !important;-webkit-transform:scale("
                            + ARFinal + ") !important;}}";
                    DeviceWidth = FlipickConfig.getDeviceWidth_L(context);
                    ARFinal = DeviceWidth / Float.parseFloat(JobWidth);
                    String defaultCssLandscape = "@media (orientation: landscape) {body > :first-child {position: absolute !important;left: 0px !important;top: 0px !important;-webkit-transform-origin:0 0 !important;-webkit-transform:scale("
                            + ARFinal + ") !important;  }}";

                    String content = defaultCssLandscape + defaultCssPortrait;
                    savecssFile(content);
                    updateUserOrientation("2");
                    // ReloadActivity("");
                    ReloadActivity();

                }
                catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        /* Book mark */
        ((ImageView) actionBar.getImageButtonBookMark()).setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if ((Boolean) ((ImageView) v).getTag())
                {
                    FlipickStaticData.oJobInfo.BookmarkList.get(currentpage).is_Bookmark = "N";
                    removeBMFromDB("N", currentpage + "");

                    DisplayErrorMessage("Bookmark of this page has been removed");
                    actionBar.setBackgroundForBookMark(R.drawable.bookmark);
                    ((ImageView) actionBar.getImageButtonBookMark()).setTag(false);
                }
                else
                {
                    FlipickStaticData.oJobInfo.BookmarkList.get(currentpage).is_Bookmark = "Y";
                    addBMFromDB("Y", currentpage + "");

                    DisplayErrorMessage("This page has been bookmarked");
                    actionBar.setBackgroundForBookMark(R.drawable.bookmark_active);
                    ((ImageView) actionBar.getImageButtonBookMark()).setTag(true);

                }
            }
        });
        /* End Book mark */
    }

    public void getAllBundleValue()
    {
        Bundle oBundle = getIntent().getExtras();
        if (!oBundle.getString("JobId").equals(""))
        {
            JobId = oBundle.getString("JobId");
        }
        FlipickStaticData.oJobInfo = FlipickStaticData.oJobInfo.getJobInfoFromDBByJobId(JobId, context);

        // For TocBrowserFragment

        if (oBundle.getString("ACTION") != null) RequestAction = oBundle.getString("ACTION");

        if (oBundle.getString("PageLink") != null) PageLink = oBundle.getString("PageLink");
        else PageLink = "";

        if (oBundle.getString("Activity") != null) FromActivity = oBundle.getString("Activity");
        if (FromActivity.equalsIgnoreCase("TOCActivity") || RequestAction.equalsIgnoreCase("DISPLAYPAGE"))
        {
            TOCPageLink = PageLink;
        }
        // End

        // if (oBundle.getString("Orientation") != null)
        // {
        // Orientation = oBundle.getString("Orientation");
        // }

    }

    public void setupUI() throws Exception
    {
        pager = (ViewPager) findViewById(R.id.my_pager);
        // /////Seekbar
        navigation_bar = (SeekBar) findViewById(R.id.navigation_bar);
        layout_navigation = (LinearLayout) findViewById(R.id.layout_navigation);
        // //////End Seekbar

        // /highlight
        notehighlightholder = (LinearLayout) findViewById(R.id.notehighlightholder);
        note_hightlight_layout = (LinearLayout) findViewById(R.id.note_hightlight_layout);
        color_pallet_layout = (LinearLayout) findViewById(R.id.color_pallet_layout);
        notes_layout = (LinearLayout) findViewById(R.id.notes_layout);

        notehighlightholder.setVisibility(View.GONE);
        note_hightlight_layout.setVisibility(View.GONE);
        color_pallet_layout.setVisibility(View.GONE);
        notes_layout.setVisibility(View.GONE);

        edt_notes = (EditText) findViewById(R.id.edt_notes);
        notesholder = (LinearLayout) findViewById(R.id.notesholder);
        notesholder.setVisibility(View.GONE);
        showedit_notes = (EditText) findViewById(R.id.showedit_notes);
        pager.setOffscreenPageLimit(1);

        final EditText edit_goto = (EditText) findViewById(R.id.edit_goto);
        Button btngoto = (Button) findViewById(R.id.btngoto);
        btngoto.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                if (!edit_goto.getText().toString().equals(""))
                {
                    int pageno = Integer.parseInt(edit_goto.getText().toString());
                    if (pageno <= FlipickStaticData.oJobInfo.PageXHTMLList.size())
                    {
                        if (FlipickStaticData.oJobInfo.Language.equals("ar"))
                        {
                            pager.setCurrentItem(FlipickStaticData.oJobInfo.PageXHTMLList.size()-pageno);
                            edit_goto.setText("");
                        }
                        else
                        {
                            pager.setCurrentItem(pageno-1);
                            edit_goto.setText("");
                        }
                    }
                    else
                    {
                        Toast.makeText(context, "Please enter page no between 1 to " + FlipickStaticData.oJobInfo.PageXHTMLList.size() + ".", Toast.LENGTH_SHORT).show();
                        edit_goto.setText("");
                    }
                }
                else
                {
                    Toast.makeText(context, "Please enter page no.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private String GetJobReverseScaleCSS()
    {
        float jobWidth = Float.parseFloat(JobWidth);
        float jobHeight = Float.parseFloat(JobHeight);

        float DeviceWidth = FlipickConfig.getDeviceWidth_L(context);
        float DeviceHeight = FlipickConfig.getDeviceHeight_L(context);
        float StatusBarHeight = getStatusBarHeight();
        // DeviceHeight = DeviceHeight - StatusBarHeight;

        float ARW = DeviceWidth / jobWidth;
        float ARH = DeviceHeight / jobHeight;
        float ARFinal = DeviceHeight / jobHeight;

        if (ARW < ARH) ARFinal = ARW;
        else ARFinal = ARH;

        float Left = ((DeviceWidth - (jobWidth * ARFinal)) / 2);
        float Top = 0;

        String JobScaleCSS = "@media (orientation: landscape) {body > :first-child {position:absolute !important;left:" + Left + "px !important;top:" + Top
                + "px !important;-webkit-transform-origin:0 0 !important;-webkit-transform:scale(" + ARFinal + ") !important;  }}";

        return JobScaleCSS;
    }

    public int getNavigationBarHeight()
    {
        int result = 0;
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;

    }

    public void addBMFromDB(String Is_BookMark, String currentpage)
    {

        Bookmark bookmark = new Bookmark();
        bookmark.PageNo = currentpage;
        bookmark.Is_Bookmark = Is_BookMark;
        bookmark.JobId = FlipickStaticData.oJobInfo.JobId;
        bookmark.insertBookmarkInDB(this);
    }

    public void removeBMFromDB(String is_BookMark, String currentpage)
    {
        Bookmark bookmark = new Bookmark();
        bookmark.deleteBookmarkfromDB(this, FlipickStaticData.oJobInfo.JobId, currentpage);
    }

    public void savecssFile(String content)
    {
        try
        {
            File file = new File(FlipickStaticData.OEBPSPath + "/fitcontent.css");
            if (file.exists())
            {
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(content);
                bw.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void GetParentViewURL()
    {

        if (!ConnectionDetector.IsInternetAvailable(getApplicationContext()))
        {
            ConnectionDetector.DisplayInternetErrorMessage(getApplicationContext());
            return;

        }
        else
        {

            // Intent intent = new Intent(getApplicationContext(),
            // StoreActivity.class);
            // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
            // Intent.FLAG_ACTIVITY_NEW_TASK);
            // Bundle bundle = new Bundle();
            // bundle.putString("PageLink", String.valueOf(currentpage));
            // bundle.putString("Activity", "Flipickpageviewer");
            // bundle.putString("JobId", FlipickStaticData.oJobInfo.JobId);
            // intent.putExtras(bundle);
            // startActivity(intent);
            // finish();
        }
    }

    public int getStatusBarHeight()
    {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public String RemoveHighlightText(String ParagraphID, String OuterHeadHtml, String OuterBodyHtml)
    {
        Highlights oHighlights = new Highlights();
        oHighlights.deleteHighlightsfromDB(getApplicationContext(), ParagraphID, Integer.toString(currentpage));
        SaveRemovedHightedText(OuterHeadHtml, OuterBodyHtml);
        return null;
    }

    public String SaveRemovedHightedText(String highlightedOuterHeadHTML, String highlightedOuterBodyHTML)
    {
        String XhtmlName = "";
        highlightedOuterBodyHTML = highlightedOuterBodyHTML.replace("xmlns=\"http://www.w3.org/1999/xhtml\"", "");
        highlightedOuterBodyHTML = highlightedOuterBodyHTML.replace("xmlns:epub=\"http://www.idpf.org/2007/ops\"", "");
        String DocPath = "";

        String HtmlPath = "";
        if (FlipickStaticData.BookType.equals("VERTICALSCROLL"))
        {
            HtmlPath = FlipickStaticData.oJobInfo.PageXHTMLList.get(currentpage);
        }
        else
        {
            HtmlPath = FlipickStaticData.oJobInfo.ReflowNewFileNames.get(currentpage);
        }
        XhtmlName = HtmlPath.split("/")[(HtmlPath.split("/").length) - 1];
        try
        {
            DocPath = FlipickStaticData.OEBPSPath + "/" + XhtmlName;

            String HTML = FlipickIO.ReadHTMLFileText(DocPath);
            int indexHead = HTML.indexOf("<head");
            int indexBody = HTML.indexOf("</body>");
            String FindHeadBody = HTML.substring(indexHead, indexBody + 7);
            HTML = HTML.replace(FindHeadBody, highlightedOuterHeadHTML + highlightedOuterBodyHTML);

            FlipickIO.WriteHTMLFileText(DocPath, HTML);
            pager.setOnTouchListener(null);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public String SavePagewithHightedText(String highlightedHeadOuterHTML, String highlightedBodyOuterHtml, final String hightedtext, final String ParegraphId)
    {
        String XhtmlName = "";
        highlightedBodyOuterHtml = highlightedBodyOuterHtml.replace("xmlns=\"http://www.w3.org/1999/xhtml\"", "");
        highlightedBodyOuterHtml = highlightedBodyOuterHtml.replace("xmlns:epub=\"http://www.idpf.org/2007/ops\"", "");
        String DocPath = "";
        String HtmlPath = "";
        if (FlipickStaticData.BookType.equals("VERTICALSCROLL"))
        {
            HtmlPath = FlipickStaticData.oJobInfo.PageXHTMLList.get(currentpage);
        }
        else
        {
            HtmlPath = FlipickStaticData.oJobInfo.ReflowNewFileNames.get(currentpage);
        }

        XhtmlName = HtmlPath.split("/")[(HtmlPath.split("/").length) - 1];

        try
        {
            DocPath = FlipickStaticData.OEBPSPath + "/" + XhtmlName;

            String HTML = FlipickIO.ReadHTMLFileText(DocPath);
            int indexHead = HTML.indexOf("<head");
            int indexBody = HTML.indexOf("</body>");
            String FindHeadBody = HTML.substring(indexHead, indexBody + 7);
            HTML = HTML.replace(FindHeadBody, highlightedHeadOuterHTML + highlightedBodyOuterHtml);

            FlipickIO.WriteHTMLFileText(DocPath, HTML);

            if (hightedtext != "" && !hightedtext.equals(""))
            {
                addHLToDB(hightedtext, ParegraphId);
            }

            pager.setOnTouchListener(null);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public void addHLToDB(String hightedtext, String ParegraphId)
    {
        Highlights highlight = new Highlights();
        highlight.PageNo = Integer.toString(currentpage);
        highlight.HighlightsData = hightedtext;
        highlight.ParagraphID = ParegraphId;
        highlight.JobID = FlipickStaticData.oJobInfo.JobId;
        highlight.TopicName = FlipickStaticData.oJobInfo.JobTitle;
        highlight.HighlightColor = HighlightColor;
        highlight.insertHighlightsInDB(this);
    }

    public void getUserSelection(String selectedText)
    {
        SelectedText = selectedText;
    }

    public void shownoteshighlight()
    {
        ishighlightViewVisible = true;
        actionBar.hideActionBar();
        layout_navigation.setVisibility(View.GONE);
        notehighlightholder.setVisibility(View.VISIBLE);
        note_hightlight_layout.setVisibility(View.VISIBLE);
        // for lock page
        pager.setOnTouchListener(new OnTouchListener()
        {

            public boolean onTouch(View arg0, MotionEvent arg1)
            {
                return true;
            }
        });

        findViewById(R.id.txt_dictionary).setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                notehighlightholder.setVisibility(View.GONE);
                note_hightlight_layout.setVisibility(View.GONE);
                color_pallet_layout.setVisibility(View.GONE);
                notes_layout.setVisibility(View.GONE);
                if (ConnectionDetector.IsInternetAvailable(context))
                {
                    Intent intent = new Intent(context, DictionaryActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString("SelectedText", SelectedText);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else
                {
                    FlipickError.DisplayErrorAsToast(context, "No internet connection available.");
                }

            }

        });

        findViewById(R.id.txt_note).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                notehighlightholder.setVisibility(View.VISIBLE);
                note_hightlight_layout.setVisibility(View.VISIBLE);
                color_pallet_layout.setVisibility(View.GONE);

                ishighlightViewVisible = false;
                edt_notes.setText("");
                if (FlipickStaticData.currentapiVersion < 21)
                {
                    if (FlipickStaticData.BookType.equals("VERTICALSCROLL"))
                    {
                        String AssetsImagePath = "file:///android_asset/sticky-note-pin.png" + "," + "25";
                        CurrentXwalkWebview.load("javascript:highlightNotesText(\"" + AssetsImagePath + "\")", null);
                    }
                    else
                    {
                        String AssetsImagePath = "file:///android_asset/sticky-note-pin.png" + "," + "50";
                        CurrentXwalkWebview.load("javascript:highlightNotesText(\"" + AssetsImagePath + "\")", null);
                    }
                }
                else
                {
                    if (FlipickStaticData.BookType.equals("VERTICALSCROLL"))
                    {
                        String AssetsImagePath = "file:///android_asset/sticky-note-pin.png" + "," + "25";
                        CurrentAndroidWebview.loadUrl("javascript:highlightNotesText(\"" + AssetsImagePath + "\")", null);
                    }
                    else
                    {
                        String AssetsImagePath = "file:///android_asset/sticky-note-pin.png" + "," + "50";
                        CurrentAndroidWebview.loadUrl("javascript:highlightNotesText(\"" + AssetsImagePath + "\")", null);
                    }
                }

                showNotes();
            }
        });

        findViewById(R.id.txt_highlight).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ishighlightViewVisible = false;
                showHighlight();
            }
        });
        findViewById(R.id.txt_Close).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ishighlightViewVisible = false;
                ShowHideHighlight();
            }
        });
    }

    public void addNOTESToDB(String notestext, String ParegraphId)
    {
        Notes notes = new Notes();
        notes.PageNo = Integer.toString(currentpage);
        notes.NotesData = notestext;
        notes.Paragraph_id = ParegraphId;
        notes.JobId = FlipickStaticData.oJobInfo.JobId;
        notes.TopicName = FlipickStaticData.oJobInfo.JobTitle;
        notes.insertNotesInDB(this);
    }

    public void EditDelNotes(final String ImageId)
    {

        findViewById(R.id.text_notes_edit).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!showedit_notes.getText().toString().equals(""))
                {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(showedit_notes.getWindowToken(), 0);
                    Notes notes = new Notes();
                    notes.updateNotesDataByParagraphId(getApplicationContext(), showedit_notes.getText().toString(), FlipickStaticData.oJobInfo.JobId, ImageId);
                }
                notesholder.setVisibility(View.GONE);
                ishighlightViewVisible = false;
            }
        });
        findViewById(R.id.text_notes_delete).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                View view = pager.findViewWithTag(currentpage);
                if (FlipickStaticData.currentapiVersion < 21)
                {
                    CurrentXwalkWebview = (XWalkView) view;
                    CurrentXwalkWebview.load("javascript:RemoveNotes('" + ImageId + "')", null);
                }
                else
                {
                    CurrentAndroidWebview = (WebView) view;
                    CurrentAndroidWebview.loadUrl("javascript:RemoveNotes('" + ImageId + "')", null);
                }

                notesholder.setVisibility(View.GONE);
                ishighlightViewVisible = false;
            }
        });

    }

    public void showNotes()
    {
        notes_layout.setVisibility(View.VISIBLE);

        findViewById(R.id.text_notes_save).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!edt_notes.getText().toString().equals(""))
                {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edt_notes.getWindowToken(), 0);
                    if (FlipickStaticData.currentapiVersion < 21)
                    {
                        CurrentXwalkWebview.load("javascript:SavePagewithNotesText()", null);
                    }
                    else
                    {
                        CurrentAndroidWebview.loadUrl("javascript:SavePagewithNotesText()", null);

                    }


                }
                notehighlightholder.setVisibility(View.GONE);
                note_hightlight_layout.setVisibility(View.GONE);
                color_pallet_layout.setVisibility(View.GONE);
                notes_layout.setVisibility(View.GONE);
                ishighlightViewVisible = false;
            }
        });

    }

    public void showDeleteNotes(final String ImageId)
    {
        ishighlightViewVisible = true;
        actionBar.hideActionBar();

        notesholder.setVisibility(View.VISIBLE);

        Notes notes = new Notes();
        notes.GetNotesDataByParagraphID(getApplicationContext(), FlipickStaticData.oJobInfo.JobId, ImageId);
        showedit_notes.setText(notes.getNotesData());
        EditDelNotes(ImageId);

    }

    public void showNotesPallet(final String ImageId)
    {
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                try
                {
                    showDeleteNotes(ImageId);
                }
                catch (Exception e)
                {
                    // Log.w("XXX", e.getMessage());
                }
            }
        });
    }

    public void SavePagewithNotesText(String OutHeadHTML, String OutBodyHTML, String ImgId)
    {
        String XhtmlName = "";
        OutBodyHTML = OutBodyHTML.replace("xmlns=\"http://www.w3.org/1999/xhtml\"", "");
        OutBodyHTML = OutBodyHTML.replace("xmlns:epub=\"http://www.idpf.org/2007/ops\"", "");
        String DocPath = "";

        String HtmlPath = "";
        if (FlipickStaticData.BookType.equals("VERTICALSCROLL"))
        {
            HtmlPath = FlipickStaticData.oJobInfo.PageXHTMLList.get(currentpage);
        }
        else
        {
            HtmlPath = FlipickStaticData.oJobInfo.ReflowNewFileNames.get(currentpage);
        }
        XhtmlName = HtmlPath.split("/")[(HtmlPath.split("/").length) - 1];

        try
        {
            DocPath = FlipickStaticData.OEBPSPath + "/" + XhtmlName;

            String HTML = FlipickIO.ReadHTMLFileText(DocPath);
            int indexHead = HTML.indexOf("<head");
            int indexBody = HTML.indexOf("</body>");
            String FindHeadBody = HTML.substring(indexHead, indexBody + 7);
            HTML = HTML.replace(FindHeadBody, OutHeadHTML + OutBodyHTML);

            FlipickIO.WriteHTMLFileText(DocPath, HTML);
            addNOTESToDB(edt_notes.getText().toString(), ImgId);
            pager.setOnTouchListener(null);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void RemoveNotesText(String ImageId, String OuterHeadHtml, String OuterBodyHtml)
    {
        Notes notes = new Notes();
        notes.deleteNotesfromDB(getApplicationContext(), FlipickStaticData.oJobInfo.JobId, ImageId);
        SaveRemovedNotesText(OuterHeadHtml, OuterBodyHtml);
    }

    public String SaveRemovedNotesText(String highlightedOuterHeadHTML, String highlightedOuterBodyHTML)
    {
        String XhtmlName = "";
        highlightedOuterBodyHTML = highlightedOuterBodyHTML.replace("xmlns=\"http://www.w3.org/1999/xhtml\"", "");
        highlightedOuterBodyHTML = highlightedOuterBodyHTML.replace("xmlns:epub=\"http://www.idpf.org/2007/ops\"", "");
        String DocPath = "";

        String HtmlPath = "";
        if (FlipickStaticData.BookType.equals("VERTICALSCROLL"))
        {
            HtmlPath = FlipickStaticData.oJobInfo.PageXHTMLList.get(currentpage);
        }
        else
        {
            HtmlPath = FlipickStaticData.oJobInfo.ReflowNewFileNames.get(currentpage);
        }
        XhtmlName = HtmlPath.split("/")[(HtmlPath.split("/").length) - 1];
        try
        {
            DocPath = FlipickStaticData.OEBPSPath + "/" + XhtmlName;

            String HTML = FlipickIO.ReadHTMLFileText(DocPath);
            int indexHead = HTML.indexOf("<head");
            int indexBody = HTML.indexOf("</body>");
            String FindHeadBody = HTML.substring(indexHead, indexBody + 7);
            HTML = HTML.replace(FindHeadBody, highlightedOuterHeadHTML + highlightedOuterBodyHTML);

            FlipickIO.WriteHTMLFileText(DocPath, HTML);
            pager.setOnTouchListener(null);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public void showHighlight()
    {
        actionBar.hideActionBar();
        notehighlightholder.setVisibility(View.VISIBLE);
        note_hightlight_layout.setVisibility(View.VISIBLE);
        color_pallet_layout.setVisibility(View.VISIBLE);
        notes_layout.setVisibility(View.GONE);

        findViewById(R.id.text_green).setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                HighlightColor = "#3dd62f";
                if (FlipickStaticData.currentapiVersion < 21)
                {
                    CurrentXwalkWebview.load("javascript:highlightText(\"" + HighlightColor + "\")", null);
                    CurrentXwalkWebview.load("javascript:SavePagewithHightedText()", null);
                }
                else
                {
                    CurrentAndroidWebview.loadUrl("javascript:highlightText(\"" + HighlightColor + "\")", null);
                    CurrentAndroidWebview.loadUrl("javascript:SavePagewithHightedText()", null);
                }


                notehighlightholder.setVisibility(View.GONE);
                note_hightlight_layout.setVisibility(View.GONE);
                color_pallet_layout.setVisibility(View.GONE);
                notes_layout.setVisibility(View.GONE);
                ishighlightViewVisible = false;
            }
        });

        findViewById(R.id.text_aqua).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                HighlightColor = "#1ab3c4";
                if (FlipickStaticData.currentapiVersion < 21)
                {
                    CurrentXwalkWebview.load("javascript:highlightText(\"" + HighlightColor + "\")", null);
                    CurrentXwalkWebview.load("javascript:SavePagewithHightedText()", null);
                }
                else
                {
                    CurrentAndroidWebview.loadUrl("javascript:highlightText(\"" + HighlightColor + "\")", null);
                    CurrentAndroidWebview.loadUrl("javascript:SavePagewithHightedText()", null);
                }

                notehighlightholder.setVisibility(View.GONE);
                note_hightlight_layout.setVisibility(View.GONE);
                color_pallet_layout.setVisibility(View.GONE);
                notes_layout.setVisibility(View.GONE);
                ishighlightViewVisible = false;
            }
        });

        findViewById(R.id.text_voilet).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                HighlightColor = "#3476fa";


                if (FlipickStaticData.currentapiVersion < 21)
                {
                    CurrentXwalkWebview.load("javascript:highlightText(\"" + HighlightColor + "\")", null);
                    CurrentXwalkWebview.load("javascript:SavePagewithHightedText()", null);
                }
                else
                {
                    CurrentAndroidWebview.loadUrl("javascript:highlightText(\"" + HighlightColor + "\")", null);
                    CurrentAndroidWebview.loadUrl("javascript:SavePagewithHightedText()", null);

                }
                notehighlightholder.setVisibility(View.GONE);
                note_hightlight_layout.setVisibility(View.GONE);
                color_pallet_layout.setVisibility(View.GONE);
                notes_layout.setVisibility(View.GONE);
                ishighlightViewVisible = false;
            }
        });

        findViewById(R.id.text_yellow).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                HighlightColor = "#ffeb0a";


                if (FlipickStaticData.currentapiVersion < 21)
                {
                    CurrentXwalkWebview.load("javascript:highlightText(\"" + HighlightColor + "\")", null);
                    CurrentXwalkWebview.load("javascript:SavePagewithHightedText()", null);
                }
                else
                {
                    CurrentAndroidWebview.loadUrl("javascript:highlightText(\"" + HighlightColor + "\")", null);
                    CurrentAndroidWebview.loadUrl("javascript:SavePagewithHightedText()", null);
                }
                notehighlightholder.setVisibility(View.GONE);
                note_hightlight_layout.setVisibility(View.GONE);
                color_pallet_layout.setVisibility(View.GONE);
                notes_layout.setVisibility(View.GONE);
                ishighlightViewVisible = false;
            }
        });
        findViewById(R.id.text_orange).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                HighlightColor = "#eb8810";


                if (FlipickStaticData.currentapiVersion < 21)
                {
                    CurrentXwalkWebview.load("javascript:highlightText(\"" + HighlightColor + "\")", null);
                    CurrentXwalkWebview.load("javascript:SavePagewithHightedText()", null);
                }
                else
                {
                    CurrentAndroidWebview.loadUrl("javascript:highlightText(\"" + HighlightColor + "\")", null);
                    CurrentAndroidWebview.loadUrl("javascript:SavePagewithHightedText()", null);
                }
                notehighlightholder.setVisibility(View.GONE);
                note_hightlight_layout.setVisibility(View.GONE);
                color_pallet_layout.setVisibility(View.GONE);
                notes_layout.setVisibility(View.GONE);
                ishighlightViewVisible = false;
            }
        });
        findViewById(R.id.text_red).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                HighlightColor = "#e85454";


                if (FlipickStaticData.currentapiVersion < 21)
                {
                    CurrentXwalkWebview.load("javascript:highlightText(\"" + HighlightColor + "\")", null);
                    CurrentXwalkWebview.load("javascript:SavePagewithHightedText()", null);
                }
                else
                {
                    CurrentAndroidWebview.loadUrl("javascript:highlightText(\"" + HighlightColor + "\")", null);
                    CurrentAndroidWebview.loadUrl("javascript:SavePagewithHightedText()", null);
                }
                notehighlightholder.setVisibility(View.GONE);
                note_hightlight_layout.setVisibility(View.GONE);
                color_pallet_layout.setVisibility(View.GONE);
                notes_layout.setVisibility(View.GONE);
                ishighlightViewVisible = false;
            }
        });

        findViewById(R.id.text_delete).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (FlipickStaticData.currentapiVersion < 21)
                {
                    CurrentXwalkWebview.load("javascript:removehighlightedText()", null);
                }
                else
                {
                    CurrentAndroidWebview.loadUrl("javascript:removehighlightedText()", null);
                }
                notehighlightholder.setVisibility(View.GONE);
                note_hightlight_layout.setVisibility(View.GONE);
                color_pallet_layout.setVisibility(View.GONE);
                notes_layout.setVisibility(View.GONE);
                ishighlightViewVisible = false;
            }
        });
    }

    public void ShowHideHighlight()
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(showedit_notes.getWindowToken(), 0);
        pager.setOnTouchListener(null);

        if (!ishighlightViewVisible)
        {
            notesholder.setVisibility(View.GONE);
        }

        ishighlightViewVisible = false;
        notehighlightholder.setVisibility(View.GONE);
        note_hightlight_layout.setVisibility(View.GONE);
        color_pallet_layout.setVisibility(View.GONE);
        notes_layout.setVisibility(View.GONE);
    }

    private ActionMode mActionMode = null;

    @Override
    public void onActionModeStarted(ActionMode mode)
    {

        if (mActionMode == null)
        {

            mActionMode = mode;
            Menu menu = mode.getMenu();
            // Remove the default menu items (select all, copy, paste,
            // search)

            menu.clear();

            View v = pager.findViewWithTag(currentpage);
            if (FlipickStaticData.currentapiVersion < 21)
            {
                CurrentXwalkWebview = (XWalkView) v;
                CurrentXwalkWebview.load("javascript:getUserSelection()", null);
            }
            else
            {
                CurrentAndroidWebview = (WebView) v;
                CurrentAndroidWebview.loadUrl("javascript:getUserSelection()", null);
            }

            // showHighlight();
            if (FlipickStaticData.BookType.equals("REFLOWABLE") || FlipickStaticData.BookType.equals("VERTICALSCROLL"))
            {
                if (!FlipickStaticData.oJobInfo.PageXHTMLList.get(currentpage).contains("file:///android_asset"))
                {
                    shownoteshighlight();
                }
            }
        }

        super.onActionModeStarted(mode);

    }

    @Override
    public void onActionModeFinished(ActionMode mode)
    {

        mActionMode = null;

        super.onActionModeFinished(mode);

    }

    public void getFontSetting(View view)
    {
        try
        {

            int[] location = new int[2];

            Display display = ((Activity) this).getWindowManager().getDefaultDisplay();
            int width = display.getWidth();
            int height = display.getHeight();
            int popup_h = 0, popup_w = 0;
            int x = 0, y = 0;
            view.getLocationOnScreen(location);

            final ImageView img_popup_arrow = (ImageView) view.findViewById(R.id.img_popup_arrow);
            img_popup_arrow.setVisibility(View.VISIBLE);
            final View pView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.fontpopup, null, false);

            y = (((RelativeLayout) view.getParent()).getHeight() + getStatusBarHeight());
            if (width <= 480)
            {
                popup_w = width;
                popup_h = (int) (height * 0.20);
            }
            else
            {
                if (getResources().getBoolean(R.bool.isTablet))
                {
                    if (width == 1280) y = (((RelativeLayout) view.getParent()).getHeight());
                    popup_w = (int) (width * 0.20);
                    popup_h = (int) (height * 0.30);
                    if (width == 720)
                    {
                        y = (((RelativeLayout) view.getParent()).getHeight() + getStatusBarHeight());
                        popup_w = width;
                        popup_h = (int) (height * 0.25);
                    }
                    if (width == 800)
                    {
                        y = (((RelativeLayout) view.getParent()).getHeight() + getStatusBarHeight());
                        popup_w = width;
                        popup_h = (int) (height * 0.20);
                    }
                    if (width == 600)
                    {
                        y = (((RelativeLayout) view.getParent()).getHeight() + getStatusBarHeight());
                        popup_w = width;
                        popup_h = (int) (height * 0.15);
                    }
                }
                else
                {
                    popup_w = width;
                    popup_h = (int) (height * 0.25);
                }
            }
            x = location[0] - (popup_h / 4);

            int result = 0;
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0)
            {
                result = getResources().getDimensionPixelSize(resourceId);
                result = (int) FlipickConfig.pxFromDp(getApplicationContext(), result);

                if (width == 1440)
                {
                    result = result - 35; // Height of the Custom Action Bar
                }
                else if (width == 480)
                {
                    result = result + 55;
                }
                else
                {
                    result = result + 50; // Height of the Custom Action Bar
                }
            }

            final PopupWindow pw = new PopupWindow(pView, popup_w, popup_h, false);
            pw.setTouchable(true);
            pw.setFocusable(true);
            pw.setOutsideTouchable(true);
            pw.setBackgroundDrawable(new BitmapDrawable());
            pw.showAtLocation(view, Gravity.TOP, x, result);

            GetLastFontSize();
            GetLastBackgroundColor();

            // calculate fontsize based on defaultfontsize(Mobile/tablet) set on
            // FlipickStaticData
            CalculateFontsForTablet();
            CalculateFontsForMobile();

            pw.setOnDismissListener(new OnDismissListener()
            {
                @Override
                public void onDismiss()
                {

                    img_popup_arrow.setVisibility(View.INVISIBLE);
                    if (HasUserChangeFont == false) return;

                    if (Last_font_size.equalsIgnoreCase(Fontsize))
                    {

                        pw.dismiss();
                        return;
                    }

                }
            });

            if (!getResources().getBoolean(R.bool.isTablet))// mobile
            {

                if (Last_font_size.equals(""))
                {
                    Last_font_size = FlipickStaticData.DefaultFontSizeMobile + "em";
                }
            }
            else
            {
                if (Last_font_size.equals(""))
                {
                    Last_font_size = FlipickStaticData.DefaultFontSizeTablet + "em";
                }
            }

            if (getResources().getBoolean(R.bool.isTablet))// tablet
            {

                if (Last_font_size.equalsIgnoreCase((Tcal_1em + "em")))
                {
                    ((View) pView.findViewById(R.id.view_1em)).setVisibility(View.VISIBLE);
                    ((View) pView.findViewById(R.id.view_12em)).setVisibility(View.INVISIBLE);
                }
                else if (Last_font_size.equalsIgnoreCase((Tcal_11em + "em")))
                {
                    ((View) pView.findViewById(R.id.view_11em)).setVisibility(View.VISIBLE);
                    ((View) pView.findViewById(R.id.view_1em)).setVisibility(View.INVISIBLE);
                    ((View) pView.findViewById(R.id.view_12em)).setVisibility(View.INVISIBLE);
                }
                else if (Last_font_size.equalsIgnoreCase((Tcal_12em + "em")))
                {
                    ((View) pView.findViewById(R.id.view_12em)).setVisibility(View.VISIBLE);
                    ((View) pView.findViewById(R.id.view_1em)).setVisibility(View.INVISIBLE);
                }
                else if (Last_font_size.equalsIgnoreCase((Tcal_13em + "em")))
                {
                    ((View) pView.findViewById(R.id.view_13em)).setVisibility(View.VISIBLE);
                    ((View) pView.findViewById(R.id.view_12em)).setVisibility(View.INVISIBLE);
                    ((View) pView.findViewById(R.id.view_1em)).setVisibility(View.INVISIBLE);
                }
                else if (Last_font_size.equalsIgnoreCase((Tcal_14em + "em")))
                {
                    ((View) pView.findViewById(R.id.view_14em)).setVisibility(View.VISIBLE);
                    ((View) pView.findViewById(R.id.view_12em)).setVisibility(View.INVISIBLE);
                    ((View) pView.findViewById(R.id.view_1em)).setVisibility(View.INVISIBLE);
                }
            }
            else
            {
                if (Last_font_size.equalsIgnoreCase((Mcal_1em + "em")))
                {
                    ((View) pView.findViewById(R.id.view_1em)).setVisibility(View.VISIBLE);
                    ((View) pView.findViewById(R.id.view_12em)).setVisibility(View.INVISIBLE);
                }
                else if (Last_font_size.equalsIgnoreCase((Mcal_11em + "em")))
                {
                    ((View) pView.findViewById(R.id.view_11em)).setVisibility(View.VISIBLE);
                    ((View) pView.findViewById(R.id.view_1em)).setVisibility(View.INVISIBLE);
                    ((View) pView.findViewById(R.id.view_12em)).setVisibility(View.INVISIBLE);
                }
                else if (Last_font_size.equalsIgnoreCase((Mcal_12em + "em")))
                {
                    ((View) pView.findViewById(R.id.view_12em)).setVisibility(View.VISIBLE);
                    ((View) pView.findViewById(R.id.view_1em)).setVisibility(View.INVISIBLE);
                }
                else if (Last_font_size.equalsIgnoreCase((Mcal_13em + "em")))
                {
                    ((View) pView.findViewById(R.id.view_13em)).setVisibility(View.VISIBLE);
                    ((View) pView.findViewById(R.id.view_12em)).setVisibility(View.INVISIBLE);
                    ((View) pView.findViewById(R.id.view_1em)).setVisibility(View.INVISIBLE);
                }
                else if (Last_font_size.equalsIgnoreCase((Mcal_14em + "em")))
                {
                    ((View) pView.findViewById(R.id.view_14em)).setVisibility(View.VISIBLE);
                    ((View) pView.findViewById(R.id.view_12em)).setVisibility(View.INVISIBLE);
                    ((View) pView.findViewById(R.id.view_1em)).setVisibility(View.INVISIBLE);
                }
            }

            ((TextView) pView.findViewById(R.id.txt_1em)).setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    HasUserChangeFont = true;
                    if (getResources().getBoolean(R.bool.isTablet))// tablet
                    {
                        Fontsize = Tcal_1em + "em";
                    }
                    else
                    {
                        Fontsize = Mcal_1em + "em";
                    }
                    ((View) pView.findViewById(R.id.view_1em)).setVisibility(View.VISIBLE);
                    ((View) pView.findViewById(R.id.view_11em)).setVisibility(View.INVISIBLE);
                    ((View) pView.findViewById(R.id.view_12em)).setVisibility(View.INVISIBLE);
                    ((View) pView.findViewById(R.id.view_13em)).setVisibility(View.INVISIBLE);
                    ((View) pView.findViewById(R.id.view_14em)).setVisibility(View.INVISIBLE);

                    if (FlipickStaticData.currentapiVersion < 21)
                    {
                        CurrentXwalkWebview.load("javascript:SetFontSize(\"" + Fontsize + "\")", null);
                    }
                    else
                    {
                        CurrentAndroidWebview.loadUrl("javascript:SetFontSize(\"" + Fontsize + "\")", null);
                    }

                }
            });

            ((TextView) pView.findViewById(R.id.txt_11em)).setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    HasUserChangeFont = true;

                    if (getResources().getBoolean(R.bool.isTablet))// tablet
                    {
                        Fontsize = Tcal_11em + "em";
                    }
                    else
                    {
                        Fontsize = Mcal_11em + "em";
                    }
                    ((View) pView.findViewById(R.id.view_11em)).setVisibility(View.VISIBLE);
                    ((View) pView.findViewById(R.id.view_1em)).setVisibility(View.INVISIBLE);
                    ((View) pView.findViewById(R.id.view_12em)).setVisibility(View.INVISIBLE);
                    ((View) pView.findViewById(R.id.view_13em)).setVisibility(View.INVISIBLE);
                    ((View) pView.findViewById(R.id.view_14em)).setVisibility(View.INVISIBLE);
                    if (FlipickStaticData.currentapiVersion < 21)
                    {
                        CurrentXwalkWebview.load("javascript:SetFontSize(\"" + Fontsize + "\")", null);
                    }
                    else
                    {
                        CurrentAndroidWebview.loadUrl("javascript:SetFontSize(\"" + Fontsize + "\")", null);
                    }

                }

            });

            ((TextView) pView.findViewById(R.id.txt_12em)).setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    HasUserChangeFont = true;
                    if (getResources().getBoolean(R.bool.isTablet))// tablet
                    {
                        Fontsize = Tcal_12em + "em";
                    }
                    else
                    {
                        Fontsize = Mcal_12em + "em";
                    }

                    ((View) pView.findViewById(R.id.view_12em)).setVisibility(View.VISIBLE);
                    ((View) pView.findViewById(R.id.view_11em)).setVisibility(View.INVISIBLE);
                    ((View) pView.findViewById(R.id.view_1em)).setVisibility(View.INVISIBLE);
                    ((View) pView.findViewById(R.id.view_13em)).setVisibility(View.INVISIBLE);
                    ((View) pView.findViewById(R.id.view_14em)).setVisibility(View.INVISIBLE);

                    if (FlipickStaticData.currentapiVersion < 21)
                    {
                        CurrentXwalkWebview.load("javascript:SetFontSize(\"" + Fontsize + "\")", null);
                    }
                    else
                    {
                        CurrentAndroidWebview.loadUrl("javascript:SetFontSize(\"" + Fontsize + "\")", null);
                    }
                }

            });

            ((TextView) pView.findViewById(R.id.txt_13em)).setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    HasUserChangeFont = true;
                    if (getResources().getBoolean(R.bool.isTablet))// tablet
                    {
                        Fontsize = Tcal_13em + "em";
                    }
                    else
                    {
                        Fontsize = Mcal_13em + "em";
                    }
                    ((View) pView.findViewById(R.id.view_13em)).setVisibility(View.VISIBLE);
                    ((View) pView.findViewById(R.id.view_11em)).setVisibility(View.INVISIBLE);
                    ((View) pView.findViewById(R.id.view_12em)).setVisibility(View.INVISIBLE);
                    ((View) pView.findViewById(R.id.view_1em)).setVisibility(View.INVISIBLE);
                    ((View) pView.findViewById(R.id.view_14em)).setVisibility(View.INVISIBLE);
                    if (FlipickStaticData.currentapiVersion < 21)
                    {
                        CurrentXwalkWebview.load("javascript:SetFontSize(\"" + Fontsize + "\")", null);
                    }
                    else
                    {
                        CurrentAndroidWebview.loadUrl("javascript:SetFontSize(\"" + Fontsize + "\")", null);
                    }
                }

            });

            ((TextView) pView.findViewById(R.id.txt_14em)).setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    HasUserChangeFont = true;
                    if (getResources().getBoolean(R.bool.isTablet))// tablet
                    {
                        Fontsize = Tcal_14em + "em";
                    }
                    else
                    {
                        Fontsize = Mcal_14em + "em";
                    }
                    ((View) pView.findViewById(R.id.view_14em)).setVisibility(View.VISIBLE);
                    ((View) pView.findViewById(R.id.view_11em)).setVisibility(View.INVISIBLE);
                    ((View) pView.findViewById(R.id.view_12em)).setVisibility(View.INVISIBLE);
                    ((View) pView.findViewById(R.id.view_13em)).setVisibility(View.INVISIBLE);
                    ((View) pView.findViewById(R.id.view_1em)).setVisibility(View.INVISIBLE);
                    if (FlipickStaticData.currentapiVersion < 21)
                    {
                        CurrentXwalkWebview.load("javascript:SetFontSize(\"" + Fontsize + "\")", null);
                    }
                    else
                    {
                        CurrentAndroidWebview.loadUrl("javascript:SetFontSize(\"" + Fontsize + "\")", null);
                    }

                }
            });

            // /background color in all pages.

            ((ImageView) pView.findViewById(R.id.img_bg_white)).setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    HasUserChangeFont = true;
                    BackgroundColor = "white,black";
                    // FontColor="black";
                    if (FlipickStaticData.currentapiVersion < 21)
                    {
                        CurrentXwalkWebview.load("javascript:SetBackgroundColor(\"" + BackgroundColor + "\")", null);
                    }
                    else
                    {
                        CurrentAndroidWebview.loadUrl("javascript:SetBackgroundColor(\"" + BackgroundColor + "\")", null);
                    }

                }
            });

            ((ImageView) pView.findViewById(R.id.img_bg_sepia)).setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    HasUserChangeFont = true;
                    BackgroundColor = "#DAC6A2,black";
                    if (FlipickStaticData.currentapiVersion < 21)
                    {
                        CurrentXwalkWebview.load("javascript:SetBackgroundColor(\"" + BackgroundColor + "\")", null);
                    }
                    else
                    {
                        CurrentAndroidWebview.loadUrl("javascript:SetBackgroundColor(\"" + BackgroundColor + "\")", null);
                    }
                    // FontColor="black";

                }
            });

            ((ImageView) pView.findViewById(R.id.img_bg_black)).setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    HasUserChangeFont = true;
                    BackgroundColor = "#7e7e7e,black";
                    if (FlipickStaticData.currentapiVersion < 21)
                    {
                        CurrentXwalkWebview.load("javascript:SetBackgroundColor(\"" + BackgroundColor + "\")", null);
                    }
                    else
                    {
                        CurrentAndroidWebview.loadUrl("javascript:SetBackgroundColor(\"" + BackgroundColor + "\")", null);
                    }

                    // FontColor="white";

                }
            });

        }
        catch (Exception e)
        {
            FlipickStaticData.ThreadMessage = e.getMessage() + ":" + e.getCause();
        }
    }

    private void CalculateFontsForMobile()
    {
        // 3.35-0.5=2.85 //1em
        Mcal_1em = (float) (FlipickStaticData.DefaultFontSizeMobile - 0.5);
        // 3.35-0.25=3.1
        Mcal_11em = (float) (FlipickStaticData.DefaultFontSizeMobile - 0.25);
        // Default
        Mcal_12em = (float) FlipickStaticData.DefaultFontSizeMobile;
        // 3.35+0.25=3.6
        Mcal_13em = (float) (FlipickStaticData.DefaultFontSizeMobile + 0.25);
        // 3.35+0.5=3.85
        Mcal_14em = (float) (FlipickStaticData.DefaultFontSizeMobile + 0.5);

    }

    private void CalculateFontsForTablet()
    {

        // 2-0.5=1.5
        Tcal_1em = (float) (FlipickStaticData.DefaultFontSizeTablet - 0.5);
        // 2-0.25=1.75
        Tcal_11em = (float) (FlipickStaticData.DefaultFontSizeTablet - 0.25);
        // default
        Tcal_12em = (float) FlipickStaticData.DefaultFontSizeTablet;
        // 2+0.25=2.25
        Tcal_13em = (float) (FlipickStaticData.DefaultFontSizeTablet + 0.25);
        // 2+0.5=2.5
        Tcal_14em = (float) (FlipickStaticData.DefaultFontSizeTablet + 0.5);
    }

    public void setColumnNumber(String ColNumber)
    {
        File LastPageName = new File(FlipickStaticData.oJobInfo.PageXHTMLList.get(currentpage));
        LastColumnName = LastPageName.getName();
        LastColumnNumber = ColNumber + "";

        // update fonts in all pages
        AlertDialog.Builder dalertDialog = new AlertDialog.Builder(flipickpageviewer.this);
        dalertDialog.setTitle("Confirmation");
        dalertDialog.setMessage("Once you change the font size or background colours, all the highlights, notes & bookmarks of this book will get removed. Do you still want to continue?");
        dalertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                // isUpdateNo = false;
                UpdateFontAsynTask oUpdateFontAsynTask = new UpdateFontAsynTask();
                oUpdateFontAsynTask.execute();

            }
        });
        dalertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener()
        {
            public void onClick(final DialogInterface dialog, int which)
            {
                runOnUiThread(new Runnable()
                {
                    @JavascriptInterface
                    public void run()
                    {
                        try
                        {
                            if (BackgroundColor == "")
                            {
                                String js = "document.getElementsByTagName('body')[0].style.fontSize= '" + Last_font_size + "';";
                                if (FlipickStaticData.currentapiVersion < 21)
                                {
                                    CurrentXwalkWebview.load("javascript:(function() { " + js + " })()", null);
                                }
                                else
                                {
                                    CurrentAndroidWebview.loadUrl("javascript:(function() { " + js + " })()", null);
                                }


                                Fontsize = "";
                            }
                            else
                            {
                                if (!Last_BackgroundColor.equals(""))
                                {
                                    String values[] = Last_BackgroundColor.split(",");
                                    if (values.length > 1)
                                    {
                                        String bgColor = values[0];
                                        String fontColor = values[1];
                                        String js = "document.getElementsByTagName('body')[0].style.backgroundColor= '" + bgColor + "';" + "document.getElementsByTagName('body')[0].style.color= '"
                                                + fontColor + "';";
                                        if (FlipickStaticData.currentapiVersion < 21)
                                        {
                                            CurrentXwalkWebview.load("javascript:(function() { " + js + " })()", null);
                                        }
                                        else
                                        {
                                            CurrentAndroidWebview.loadUrl("javascript:(function() { " + js + " })()", null);
                                        }

                                    }
                                }
                                else
                                {
                                    String js = "document.getElementsByTagName('body')[0].style.backgroundColor= 'white';" + "document.getElementsByTagName('body')[0].style.color= 'black';";
                                    if (FlipickStaticData.currentapiVersion < 21)
                                    {
                                        CurrentXwalkWebview.load("javascript:(function() { " + js + " })()", null);
                                    }
                                    else
                                    {
                                        CurrentAndroidWebview.loadUrl("javascript:(function() { " + js + " })()", null);
                                    }

                                    BackgroundColor = "";
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            // Log.w("XXX", e.getMessage());
                        }
                    }
                });
            }
        });
        dalertDialog.show();

    }

    private class UpdateFontAsynTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected void onPreExecute()
        {
            // TODO Auto-generated method stub
            super.onPreExecute();
            FlipickStaticData.progressMessage = "Please wait..";
            FlipickUtil.StartProgressBar(context);

        }

        @Override
        protected String doInBackground(String... urls)
        {
            try
            {
                // copy pages from Backup to Temp folder
                FlipickStaticData.oJobInfo.copyFilesInTheTempFolder(FlipickStaticData.oJobInfo.JobId);

                try
                {
                    // save last page open by user
                    LastPageOpenLinkBeforePagination(LastColumnNumber, LastColumnName, FlipickStaticData.oJobInfo.TempPath);
                }
                catch (Exception e)
                {
                    FlipickError.DisplayErrorAsLongToast(getApplicationContext(), e.getMessage());
                }

                if (Fontsize == "")
                {
                    Fontsize = Last_font_size;
                }
                if (BackgroundColor != "")
                {
                    // save last font selected by user
                    LastBackgroundColor(BackgroundColor, FlipickStaticData.oJobInfo.TempPath);
                }
                else
                {
                    LastBackgroundColor(Last_BackgroundColor, FlipickStaticData.oJobInfo.TempPath);
                }

                // save last font selected by user
                lastFontSize(Fontsize, FlipickStaticData.oJobInfo.TempPath);
                // update font size in all pages
                if (BackgroundColor != "")
                {
                    FlipickStaticData.oJobInfo.UpdateFontSizeInHTML(Fontsize, FlipickStaticData.oJobInfo.TempPath, BackgroundColor);

                }
                else
                {
                    FlipickStaticData.oJobInfo.UpdateFontSizeInHTML(Fontsize, FlipickStaticData.oJobInfo.TempPath, Last_BackgroundColor);
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
            Intent intpagination = new Intent(getApplicationContext(), PaginationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("JobId", FlipickStaticData.oJobInfo.JobId);
            bundle.putString("Activity", "flipickpageviewer");
            bundle.putString("TempPath", FlipickStaticData.oJobInfo.TempPath);
            intpagination.putExtras(bundle);
            startActivity(intpagination);
            finish();

        }
    }

    // get user orientation from jobId.xml
    public void GetUserOrientation() throws Exception
    {
        String Job_xml_path = FlipickConfig.ContentRootPath + "/" + JobId + "/" + JobId + ".xml";
        Document oDocJobInfo = FlipickIO.GetDocumentFromFile(Job_xml_path);
        if (oDocJobInfo.getElementsByTagName("UserOrientation").item(0) != null)
        {
            NodeList NL_Orientation = oDocJobInfo.getElementsByTagName("UserOrientation");
            UserOrientation = NL_Orientation.item(0).getFirstChild().getNodeValue();
        }
        else
        {
            NodeList NL_Heads = oDocJobInfo.getElementsByTagName("JobInfo");
            Element E_Head = (Element) NL_Heads.item(0);
            Element EL_UserOrientation = (Element) oDocJobInfo.createElement("UserOrientation");
            EL_UserOrientation.appendChild(oDocJobInfo.createTextNode("0"));
            E_Head.appendChild(EL_UserOrientation);
            FlipickIO.SaveXMLFileFromDocument(Job_xml_path, oDocJobInfo);

            if (oDocJobInfo.getElementsByTagName("UserOrientation").item(0) != null)
            {
                NodeList NL_Orientation = oDocJobInfo.getElementsByTagName("UserOrientation");
                UserOrientation = NL_Orientation.item(0).getFirstChild().getNodeValue();
            }
        }

    }

    public void updateUserOrientation(String Orientation) throws Exception
    {
        String Job_xml_path = FlipickConfig.ContentRootPath + "/" + JobId + "/" + JobId + ".xml";

        Document oDocJobInfo = FlipickIO.GetDocumentFromFile(Job_xml_path);
        Element EL_userOrientation = (Element) oDocJobInfo.getElementsByTagName("UserOrientation").item(0);
        EL_userOrientation.setTextContent(Orientation);
        FlipickIO.SaveXMLFileFromDocument(Job_xml_path, oDocJobInfo);
    }

    // get last font from jobId.xml
    public void GetLastFontSize() throws Exception
    {
        String Job_xml_path = FlipickConfig.ContentRootPath + "/" + JobId + "/" + JobId + ".xml";
        Document oDocJobInfo = FlipickIO.GetDocumentFromFile(Job_xml_path);
        if (oDocJobInfo.getElementsByTagName("LastFontSize").item(0).getFirstChild() != null)
        {
            NodeList NL_Orientation = oDocJobInfo.getElementsByTagName("LastFontSize");
            Last_font_size = NL_Orientation.item(0).getFirstChild().getNodeValue();
        }

    }

    public void GetJobHeightWidth() throws Exception
    {
        String Job_xml_path = FlipickConfig.ContentRootPath + "/" + JobId + "/" + JobId + ".xml";
        Document oDocJobInfo = FlipickIO.GetDocumentFromFile(Job_xml_path);
        if (oDocJobInfo.getElementsByTagName("Height").item(0).getFirstChild() != null)
        {
            NodeList NL_Orientation = oDocJobInfo.getElementsByTagName("Height");
            JobHeight = NL_Orientation.item(0).getFirstChild().getNodeValue();
        }
        if (oDocJobInfo.getElementsByTagName("Width").item(0).getFirstChild() != null)
        {
            NodeList NL_Orientation = oDocJobInfo.getElementsByTagName("Width");
            JobWidth = NL_Orientation.item(0).getFirstChild().getNodeValue();
        }

    }

    // get last backgroudcolor from jobId.xml
    public void GetLastBackgroundColor() throws Exception
    {
        String Job_xml_path = FlipickConfig.ContentRootPath + "/" + JobId + "/" + JobId + ".xml";
        Document oDocJobInfo = FlipickIO.GetDocumentFromFile(Job_xml_path);
        if (oDocJobInfo.getElementsByTagName("LastBackgroundColor").item(0).getFirstChild() != null)
        {
            NodeList NL_Orientation = oDocJobInfo.getElementsByTagName("LastBackgroundColor");
            Last_BackgroundColor = NL_Orientation.item(0).getFirstChild().getNodeValue();
        }
    }

    // save last font in jobId.xml
    public void lastFontSize(String FontSize, String TempPath) throws Exception
    {
        String Page_Name = FlipickStaticData.SelectedPageLink.substring(FlipickStaticData.SelectedPageLink.lastIndexOf("/") + 1);
        Page_Name = Page_Name.toString();
        String Job_xml_path = TempPath + "/" + JobId + ".xml";

        Document oDocJobInfo = FlipickIO.GetDocumentFromFile(Job_xml_path);
        Element EL_lastpageopened = (Element) oDocJobInfo.getElementsByTagName("LastFontSize").item(0);
        EL_lastpageopened.setTextContent(FontSize + "");
        FlipickIO.SaveXMLFileFromDocument(Job_xml_path, oDocJobInfo);
    }

    // save last background color in jobid.xml
    public void LastBackgroundColor(String backgroundcolor, String TempPath) throws Exception
    {
        String Job_xml_path = TempPath + "/" + JobId + ".xml";
        Document oDocJobInfo = FlipickIO.GetDocumentFromFile(Job_xml_path);
        Element EL_lastpageopened = (Element) oDocJobInfo.getElementsByTagName("LastBackgroundColor").item(0);
        EL_lastpageopened.setTextContent(backgroundcolor + "");
        FlipickIO.SaveXMLFileFromDocument(Job_xml_path, oDocJobInfo);
    }

    // also use in MyJavaScriptInterface
    public void SetPage(int PageNo)
    {
        pager.setCurrentItem(PageNo, false);
    }

    public void SetVerticalPage(String PageName)
    {
        for (int i = 0; i < FlipickStaticData.oJobInfo.PageXHTMLList.size(); i++)
        {
            String TempPageName = PageName.split("#")[0];

            if (((String) FlipickStaticData.oJobInfo.PageXHTMLList.get(i).toLowerCase()).equals(TempPageName.toLowerCase()))
            {
                FlipickStaticData.oJobInfo.PageXHTMLList.set(i, FlipickStaticData.oJobInfo.PageXHTMLList.get(i).replace(TempPageName, PageName));
                SetPage(i);
                progress = i;
                break;
            }
        }
    }

    public void GoToTOC()
    {

        String Toc_path = FlipickStaticData.OEBPSPath + "/FlipickToc.xhtml";

        Intent mIntent = new Intent(getApplicationContext(), TOCBrowserFragment.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putString("PageLink", currentpage + "");
        bundle.putString("TOCLink", Toc_path);
        bundle.putString("BookTitle", FlipickStaticData.BookTitle);
        bundle.putString("UserOrientation", UserOrientation);
        mIntent.putExtras(bundle);
        startActivity(mIntent);
        this.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_from_right);
        finish();
    }

    public void DisplayImageInPopup(String ImagePath) throws Throwable
    {
        try
        {
            Intent intent = new Intent(getApplicationContext(), BrowserFragmentImagePopUp.class);
            Bundle bundle = new Bundle();
            bundle.putString("ImagePath", ImagePath);
            intent.putExtras(bundle);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_right_in, 0);
        }
        catch (Throwable e)
        {
            throw e;
        }
    }

    public void DisplayErrorMessage(String message)
    {
        notes_layout.setVisibility(View.GONE);
        FlipickError.DisplayErrorAsLongToast(context, message);
    }

    public void OpenBookryWGT(String Value)
    {
        Intent intent = new Intent(getApplicationContext(), BookryWGT_Activity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Path", Value);
//		bundle.putString("Type", "Bookry");
        intent.putExtras(bundle);
        startActivity(intent);
    }

//	public void OpenArticulate(String Value)
//	{
//		Intent intent = new Intent(getApplicationContext(), BookryWGT_Activity.class);
//		Bundle bundle = new Bundle();
//		bundle.putString("Path", Value);
//		bundle.putString("Type", "Articulate");
//		intent.putExtras(bundle);
//		startActivity(intent);
//	}

    public void ShowHideActionBar()
    {
        // unlocking viewpager
        pager.setOnTouchListener(null);

        if (!ishighlightViewVisible)
        {
            if (actionBar.isVisible())
            {
                actionBar.hideActionBar();
            }
            else
            {
                actionBar.showActionBar();
                /* Book mark */
                if (FlipickStaticData.oJobInfo.BookmarkList.get(currentpage).is_Bookmark.equalsIgnoreCase("Y"))
                {
                    actionBar.setBackgroundForBookMark(R.drawable.bookmark_active);
                    ((ImageView) actionBar.getImageButtonBookMark()).setTag(true);
                }
                else
                {
                    actionBar.setBackgroundForBookMark(R.drawable.bookmark);
                    ((ImageView) actionBar.getImageButtonBookMark()).setTag(false);
                }
            }
        }
        else
        {
            actionBar.hideActionBar();
        }
    }

    public void ShowPageNavigationBar()
    {
        if (!ishighlightViewVisible)
        {
            if (layout_navigation.getVisibility() == View.VISIBLE)
            {
                if (actionBar.isVisible())
                {
                    layout_navigation.setVisibility(View.GONE);
                }
                else
                {
                    layout_navigation.setVisibility(View.VISIBLE);
                }
            }
            else
            {
                if (!actionBar.isVisible())
                {
                    layout_navigation.setVisibility(View.VISIBLE);
                }
                else
                {
                    layout_navigation.setVisibility(View.GONE);
                }
            }
        }
        else
        {
            layout_navigation.setVisibility(View.GONE);
        }
    }

    @SuppressLint("NewApi")
    public void hideActionBar()
    {
        actionBar.hideActionBar();
    }

    @SuppressLint("NewApi")
    public void hideNavigation()
    {
        layout_navigation.setVisibility(View.GONE);
    }

    public void DisplayURL(String Url)
    {
        Intent itnURL = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
        startActivity(itnURL);
    }

    public void Back()
    {
        try
        {
            LastPageOpenLink();
        }
        catch (Exception e)
        {
            FlipickError.DisplayErrorAsLongToast(getApplicationContext(), e.getMessage());
        }
        //finish();
        FlipickStaticData.TYPE = null;
        FlipickStaticData.DATA = null;
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        Bundle oBundle = new Bundle();
        oBundle.putString("ACTION", "RefreshJobXml");
        intent.putExtras(oBundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_from_right);
        finish();
    }

    public void LastPageOpenLink() throws Exception
    {
        String Page_Name = FlipickStaticData.SelectedPageLink.substring(FlipickStaticData.SelectedPageLink.lastIndexOf("/") + 1);
        Page_Name = Page_Name.toString();
        String Job_xml_path = FlipickConfig.ContentRootPath + "/" + JobId + "/" + JobId + ".xml";

        Document oDocJobInfo = FlipickIO.GetDocumentFromFile(Job_xml_path);
        Element EL_lastpageopened = (Element) oDocJobInfo.getElementsByTagName("LastPagelink").item(0);
        EL_lastpageopened.setTextContent(currentpage + "");
        FlipickIO.SaveXMLFileFromDocument(Job_xml_path, oDocJobInfo);
    }

    public void LastPageOpenLinkBeforePagination(String LastColumnNo, String LastPageName, String TempPath) throws Exception
    {
        String Page_Name = FlipickStaticData.SelectedPageLink.substring(FlipickStaticData.SelectedPageLink.lastIndexOf("/") + 1);
        Page_Name = Page_Name.toString();
        String Job_xml_path = TempPath + "/" + JobId + ".xml";

        Document oDocJobInfo = FlipickIO.GetDocumentFromFile(Job_xml_path);
        Element EL_LastPageFontPageName = (Element) oDocJobInfo.getElementsByTagName("LastPageFontPageName").item(0);
        EL_LastPageFontPageName.setTextContent(LastPageName + "");

        Element EL_LastPageFontColumnNo = (Element) oDocJobInfo.getElementsByTagName("LastPageFontColumnNo").item(0);
        EL_LastPageFontColumnNo.setTextContent(LastColumnNo + "");

        FlipickIO.SaveXMLFileFromDocument(Job_xml_path, oDocJobInfo);
    }

    @Override
    public void onAnimationEnd(Animation animation)
    {

    }

    @Override
    public void onAnimationRepeat(Animation animation)
    {
    }

    @Override
    public void onAnimationStart(Animation animation)
    {

    }

    @Override
    public void onBackPressed()
    {
        try
        {
            if (FlipickStaticData.IsLastPageToc.equals(false))
            {
                LastPageOpenLink();
                FlipickConfig.SavePagerCurrentPage(context, "", "");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Bundle oBundle = new Bundle();
                oBundle.putString("ACTION", "RefreshJobXml");
                intent.putExtras(oBundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_left);
                finish();
            }
            else
            {
                GoToTOC();
            }
        }
        catch (Exception e)
        {
            FlipickError.DisplayErrorAsLongToast(getApplicationContext(), e.getMessage());
        }
        // super.onBackPressed();
    }

    public void StartSubmitProgressBar()
    {

    }

    public void StopSubmitProgressBar()
    {

    }

    public void StartProgressBar()
    {
        Intent intent = new Intent(getApplicationContext(), FlipickProgressBar.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        startActivity(intent);
    }
}
