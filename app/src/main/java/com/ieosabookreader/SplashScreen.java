package com.ieosabookreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;

public class SplashScreen extends AppCompatActivity {

    Button btn_to_know_more;
    User oUser = null;
    Context context;
    public Boolean onPageScrollchange = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getBoolean(R.bool.isTablet)) {
            if (!FlipickStaticData.Orientation.equals(""))
            {
                if (FlipickStaticData.Orientation.equalsIgnoreCase("L"))
                {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                else
                {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        FlipickStaticData.Orientation = "";
        setContentView(R.layout.splash_screen_activity);

        oUser = new User(this);
        context = this;

        if (getIntent().getType() != null)
        {
            FlipickConfig.SetFileConfigPath(context);
            // sideload
            FlipickStaticData.TYPE = getIntent().getType();
            FlipickStaticData.DATA = getIntent().getData();
        }

        try
        {
            oUser.GetUserInfo();
            JobInfo oJobInfo = new JobInfo();
            oJobInfo.DeleteTempFolder(context, oUser.UserName.trim());
        }
        catch (Exception e)
        {
            FlipickError.DisplayErrorAsLongToast(getApplicationContext(), e.getMessage());
        }
        if (FlipickStaticData.TYPE != null)
        {

            if ((oUser.IsPreviousUserRegistered() && (!(oUser.Logout).equalsIgnoreCase("Y"))))
            {
                Intent oIntent = new Intent(context, SideloadActivity.class);
                oIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(oIntent);
                finish();
            }
            else
            {
                //Intent intent = new Intent(getApplicationContext(), Login.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                //startActivity(intent);
                //finish();

            }

        }
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, new int[]
                { R.layout.splashscreen });
        ViewPager myPager = (ViewPager) findViewById(R.id.myfivepanelpager);
        myPager.setAdapter(adapter);
        myPager.setCurrentItem(0);

        myPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            public void onPageSelected(int arg0)
            {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0)
            {
                if (onPageScrollchange) return;
                onPageScrollchange = true;
                //boolean isAppVersionLess = FlipickConfig.isAppVirsionLess(getApplicationContext());
                //if (!isAppVersionLess)


                //					SharedPreferences preferences = context.getSharedPreferences(context.getResources().getString(R.string.app_name), context.MODE_PRIVATE);
//					PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
//					if (preferences.getInt("APPV", -1) < 16)
//					{
//
//						Intent oIntent = new Intent(context, Blank_webview_landscape.class);
//						Bundle bundle = new Bundle();
//						bundle.putString("Activity", "SplashActivity");
//						oIntent.putExtras(bundle);
//						((Activity) context).startActivity(oIntent);
//						((Activity) context).finish();
//					}
//					else
//					{
                RedirectTolibrary();
//					}

            }
        });
    }

    public void RedirectTolibrary() {
        onPageScrollchange = true;
        FlipickConfig.Part2ContentRootPath = oUser.UserName.trim();
        FlipickConfig.ContentRootPath = FlipickConfig.Part1ContentRootPath + "/" + FlipickConfig.Part2ContentRootPath;
        finish();
        FlipickStaticData.ThreadMessage = "";
        FlipickStaticData.progressMessage = "";
//		Intent oIntent = new Intent(getApplicationContext(), MainActivity.class);
        Intent oIntent = new Intent(getApplicationContext(), LandingPage_Activity.class);
        startActivity(oIntent);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_left);
        finish();
    }
}