package com.ieosabookreader;

import android.app.Application;
import android.app.Activity;
import android.app.AlertDialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;

public class FlipickApplication extends Application {

    public static Context mContext;


    @Override public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return mContext;
    }
    public void setupPopupWindowOnSettingsAll(final View view, final Context context, final String currentPage, final String FromActivity,final String UserOrientation)
    {

        int[] location = new int[2];

        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        int popup_h = 0, popup_w = 0;
        int x = 0, y = 0;
        view.getLocationOnScreen(location);

        final View pView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.setting_all_popup, null, false);
        if (!FlipickStaticData.BookType.equals("REFLOWABLE") && !FlipickStaticData.BookType.equals("VERTICALSCROLL"))
        {
            ((TextView) pView.findViewById(R.id.txt_notes)).setVisibility(View.GONE);
            ((TextView) pView.findViewById(R.id.txt_highlights)).setVisibility(View.GONE);
            ((View) pView.findViewById(R.id.view_highlight)).setVisibility(View.GONE);
        }
        y = (((RelativeLayout) view.getParent()).getHeight() + getStatusBarHeight());

        if (width <= 480)
        {
            if (!FlipickStaticData.BookType.equals("REFLOWABLE") && !FlipickStaticData.BookType.equals("VERTICALSCROLL"))
            {
                popup_w = (int) (width * 0.35);
                // for single setting change height of seeting layout should be
                popup_h = (int) (height * 0.10);
            }
            else
            {
                popup_w = (int) (width * 0.35);
                // for single setting change height of seeting layout should be
                popup_h = (int) (height * 0.25);
            }
        }
        else
        {
            if (getResources().getBoolean(R.bool.isTablet))
            {
                if (width == 1280) y = (((RelativeLayout) view.getParent()).getHeight());
                if (!FlipickStaticData.BookType.equals("REFLOWABLE") && !FlipickStaticData.BookType.equals("VERTICALSCROLL"))
                {
                    popup_w = (int) (width * 0.12);
                    // for single setting change height of seeting layout should
                    // be
                    popup_h = (int) (height * 0.10);
                }
                else
                {
                    popup_w = (int) (width * 0.12);
                    // for single setting change height of seeting layout should
                    // be
                    popup_h = (int) (height * 0.30);
                }
                if (width == 720)
                {
                    y = (((RelativeLayout) view.getParent()).getHeight() + getStatusBarHeight());
                    popup_w = (int) (width * 0.35);
                    popup_h = (int) (height * 0.25);
                }
                else if (width == 1280)
                {
                    y = (((RelativeLayout) view.getParent()).getHeight() + getStatusBarHeight());
                    popup_w = (int) (width * 0.20);
                    popup_h = (int) (height * 0.30);
                }
                if (width == 800)
                {
                    y = (((RelativeLayout) view.getParent()).getHeight() + getStatusBarHeight());
                    popup_w = (int) (width * 0.35);
                    popup_h = (int) (height * 0.20);
                }
                if (width == 600)
                {
                    y = (((RelativeLayout) view.getParent()).getHeight() + getStatusBarHeight());
                    if (!FlipickStaticData.BookType.equals("REFLOWABLE") && !FlipickStaticData.BookType.equals("VERTICALSCROLL"))
                    {
                        popup_w = (int) (width * 0.22);
                        popup_h = (int) (height * 0.05);
                    }
                    else
                    {
                        popup_w = (int) (width * 0.22);
                        popup_h = (int) (height * 0.15);
                    }
                }
            }
            else
            {
                popup_w = (int) (width * 0.35);
                popup_h = (int) (height * 0.20);

                if (!FlipickStaticData.BookType.equals("REFLOWABLE") && !FlipickStaticData.BookType.equals("VERTICALSCROLL"))
                {
                    if (width == 1280)
                    {

                        popup_w = (int) (width * 0.18);
                        popup_h = (int) (height * 0.10);
                    }
                    if (width == 720)
                    {
                        popup_w = (int) (width * 0.30);
                        popup_h = (int) (height * 0.05);
                    }
                    if (width == 800)
                    {
                        popup_w = (int) (width * 0.20);
                        popup_h = (int) (height * 0.10);

                    }
                }
                else
                {
                    if (width == 1280)
                    {
                        popup_w = (int) (width * 0.20);
                        popup_h = (int) (height * 0.30);
                    }
                    if (width == 720)
                    {
                        popup_w = (int) (width * 0.30);
                        popup_h = (int) (height * 0.20);
                    }
                    if (width == 1440)
                    {
                        y = (((RelativeLayout) view.getParent()).getHeight() + getStatusBarHeight());
                        popup_w = (int) (width * 0.30);
                        popup_h = (int) (height * 0.20);
                    }

                }

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

        pw.setOnDismissListener(new OnDismissListener()
        {
            @Override
            public void onDismiss()
            {
            }
        });

        ((TextView) pView.findViewById(R.id.txt_notes)).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pw.dismiss();
                Intent oIntent = new Intent(context, NotesActivity.class);
                oIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                bundle.putString("PageLink", currentPage + "");
                oIntent.putExtras(bundle);
                ((Activity) context).startActivity(oIntent);
                ((Activity) context).overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_left);
                ((Activity) context).finish();

            }
        });
        ((TextView) pView.findViewById(R.id.txt_highlights)).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pw.dismiss();
                Intent oIntent = new Intent(context, HighlightsActivity.class);
                oIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                bundle.putString("PageLink", currentPage + "");
                oIntent.putExtras(bundle);
                ((Activity) context).startActivity(oIntent);
                ((Activity) context).overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_left);
                ((Activity) context).finish();
            }
        });

        ((TextView) pView.findViewById(R.id.txt_bookmark)).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pw.dismiss();
                Intent oIntent = new Intent(context, BookmarkActivity.class);
                oIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                bundle.putString("PageLink", currentPage + "");
                bundle.putString("UserOrientation", UserOrientation);
                oIntent.putExtras(bundle);
                ((Activity) context).startActivity(oIntent);
                ((Activity) context).overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_left);
                ((Activity) context).finish();
            }
        });
    }

    public void setupPopupWindowOnSettings(final View view, final Context context, final User oUser)
    {
        int[] location = new int[2];

        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        int popup_h = 0, popup_w = 0;
        int x = 0, y = 0;

        if (width <= 480)
        {
            popup_w = (int) (width * 0.35);
            // for single setting change height of seeting layout should be
            popup_h = (int) (height * 0.25);
        }
        else
        {
            if (getResources().getBoolean(R.bool.isTablet))
            {
                if (width == 1280) y = (((RelativeLayout) view.getParent()).getHeight());

                popup_w = (int) (width * 0.15);
                // for single setting change height of seeting layout should be

                popup_h = (int) (height * 0.30);

                if (width == 720)
                {
                    y = (((RelativeLayout) view.getParent()).getHeight() + getStatusBarHeight());
                    popup_w = (int) (width * 0.35);
                    popup_h = (int) (height * 0.25);
                }
                else if (width == 1280)
                {
                    y = (((RelativeLayout) view.getParent()).getHeight() + getStatusBarHeight());
                    popup_w = (int) (width * 0.20);
                    popup_h = (int) (height * 0.30);
                }
                if (width == 800)
                {
                    y = (((RelativeLayout) view.getParent()).getHeight() + getStatusBarHeight());
                    popup_w = (int) (width * 0.35);
                    popup_h = (int) (height * 0.20);
                }
                if (width == 600)
                {
                    y = (((RelativeLayout) view.getParent()).getHeight() + getStatusBarHeight());
                    popup_w = (int) (width * 0.25);
                    popup_h = (int) (height * 0.15);
                }
            }
            else
            {
                popup_w = (int) (width * 0.35);
                popup_h = (int) (height * 0.20);
                if (width == 1280)
                {
                    popup_w = (int) (width * 0.20);
                    popup_h = (int) (height * 0.30);
                }
                if (width == 720)
                {
                    popup_w = (int) (width * 0.35);
                    popup_h = (int) (height * 0.20);
                }
                if (width == 800)
                {
                    popup_w = (int) (width * 0.20);
                    popup_h = (int) (height * 0.35);
                }
            }
        }
        view.getLocationOnScreen(location);
        view.getLocationOnScreen(location);
        final View pView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.setting_popup, null, false);
        final PopupWindow pw = new PopupWindow(pView, popup_w, popup_h, false);
        pw.setTouchable(true);
        pw.setFocusable(true);
        pw.setOutsideTouchable(true);
        pw.setBackgroundDrawable(new BitmapDrawable());

        pw.showAtLocation(view, Gravity.NO_GRAVITY, location[0] - (popup_h / 4), (((RelativeLayout) view.getParent()).getHeight() + FlipickUtil.getStatusBarHeight(context)));

        ((TextView) pView.findViewById(R.id.txt_about)).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    pw.dismiss();
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.aboutus);
                    Button btn_closeabt = (Button) dialog.findViewById(R.id.btn_closedialog);
                    TextView txt_app_version = (TextView) dialog.findViewById(R.id.txt_app_version);
                    // txt_app_version.setText(getPackageManager().getPackageInfo(getPackageName(),
                    // 0).versionName);

                    btn_closeabt.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                catch (Throwable e)
                {
                    FlipickError.DisplayErrorAsToast(context, e.getMessage());
                }
            }
        });
        ((TextView) pView.findViewById(R.id.txt_de_register)).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pw.dismiss();
                try
                {
                    deRegisterDialog(context, oUser);
                }
                catch (Throwable e)
                {
                    FlipickError.DisplayErrorAsToast(context, e.getMessage());
                    // msbox("Error", e.getMessage());
                }
            }
        });
        ((TextView) pView.findViewById(R.id.txt_quit)).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pw.dismiss();
                backButtonHandler(context);
            }
        });

    }

    public void backButtonHandler(final Context context)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Quit");
        alertDialog.setMessage("Are you sure you want to quit?");
        alertDialog.setIcon(R.drawable.app_icon);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                ((Activity) context).finish();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void deRegisterDialog(final Context context, final User oUser)
    {
        AlertDialog.Builder dalertDialog = new AlertDialog.Builder(context);
        dalertDialog.setTitle("De-Register");
        dalertDialog.setMessage("Are you sure you want to de-register this device?");
        dalertDialog.setIcon(R.drawable.app_icon);
        dalertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                if (!ConnectionDetector.IsInternetAvailable(getApplicationContext()))
                {
                    ConnectionDetector.DisplayInternetErrorMessage(context);
                    return;
                }
                FlipickStaticData.progressMessage = "De-registering...";
                FlipickUtil.StartProgressBar(context);
                new DeRegisterDeviceAsynTask(context).execute(oUser);

            }
        });
        dalertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        dalertDialog.show();
    }

    public static class DeRegisterDeviceAsynTask extends AsyncTask<Object, Void, Object>
    {

        Context context;
        User oUser;

        public DeRegisterDeviceAsynTask(Context context)
        {
            this.context = context;
            oUser = new User(context);
        }

        @Override
        protected String doInBackground(Object... param)
        {
            try
            {

                //oUser = (User) param[0];
                FlipickStaticData.ThreadMessage = "";
                //oUser.DeRegisterDevice(oUser);

            }
            catch (Throwable e)
            {
                FlipickStaticData.ThreadMessage = e.getMessage();
            }
            return "";
        }

        @Override
        protected void onPostExecute(Object result)
        {
            if (!FlipickStaticData.ThreadMessage.equals(""))
            {
                FlipickStaticData.progressMessage = "";
                FlipickError.DisplayErrorAsLongToast(context, FlipickStaticData.ThreadMessage);
                ((Activity) context).finish();
                return;
            }
            else
            {

                FlipickError.DisplayErrorAsLongToast(context, "Your device has been successfully de-registered.");
                CookieSyncManager.createInstance(context);
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.removeAllCookie();
            }
            FlipickConfig.clearSharedPreferences(context);
            try
            {
                FlipickIO.DeleteDirectoryRecursive(new File(FlipickConfig.ContentRootPath));
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            JobInfo oJobinfo = new JobInfo(context);
            oJobinfo.deleteJobInfoTable(false);
            Notes notes = new Notes();
            notes.deleteTable(context);
            Highlights highlights = new Highlights();
            highlights.deleteTable(context);
            Bookmark bookmark = new Bookmark();
            bookmark.deleteTable(context);

            ((Activity) context).finish();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ((Activity) context).startActivity(intent);
            FlipickStaticData.progressMessage = "";
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

}