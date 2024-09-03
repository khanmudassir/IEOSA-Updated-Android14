package com.ieosabookreader;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class LandingPage_Activity extends AppCompatActivity implements View.OnClickListener {
    Context context;
    private ImageView layout_btn_menu;
    LandingPageViewAdapter LandingPageViewAdapter;
    User oUser = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landingpage_activity);
        if (getResources().getBoolean(R.bool.isTablet))
        {
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
        }
        else
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        FlipickStaticData.Orientation = "";



        context = this;
        oUser = new User(this);
        oUser.GetUserInfo();

        layout_btn_menu = (ImageView) findViewById(R.id.btn_setting);
        layout_btn_menu.setOnClickListener(this);
        //UpdatedeviceTextViews();
        GridView dashboard_grid_view = (GridView) findViewById(R.id.dashboard_grid_view);
        LandingPageViewAdapter = new LandingPageViewAdapter(context);
        dashboard_grid_view.setAdapter(LandingPageViewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_main_menus,menu);
        return true;
    }
    public void UpdatedeviceTextViews()
    {
        TextView txt_widthp = (TextView)findViewById(R.id.txt_widthp);
        TextView txt_heightp = (TextView)findViewById(R.id.txt_heightp);
        TextView txt_widthl = (TextView)findViewById(R.id.txt_widthl);
        TextView txt_heightl = (TextView)findViewById(R.id.txt_heightl);

        float deviceWidth = FlipickConfig.getDeviceWidth(context);
        float deviceHeight = FlipickConfig.getDeviceHeight(context);
        float deviceWidthL = FlipickConfig.getDeviceWidth_L(context);
        float deviceHeightL = FlipickConfig.getDeviceHeight_L(context);

        txt_widthp.setText(deviceWidth + " WP" );
        txt_heightp.setText(deviceHeight + " HP" );

        txt_widthl.setText(deviceWidthL + " WL" );
        txt_heightl.setText(deviceHeightL + " HL" );



    }


    @Override
    public void onBackPressed()
    {
        backButtonHandler();
    }

    public void backButtonHandler()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LandingPage_Activity.this);
        alertDialog.setTitle("Quit");
        alertDialog.setMessage("Are you sure you want to quit?");
        alertDialog.setIcon(R.drawable.app_icon);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                finish();
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

    public void showAboutUsDialog(){
        try
        {
            //pw.dismiss();
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.aboutus);
            Button btn_closeabt = (Button) dialog.findViewById(R.id.btn_closedialog);
            TextView txt_app_version = (TextView) dialog.findViewById(R.id.txt_app_version);
            // txt_app_version.setText(getPackageManager().getPackageInfo(getPackageName(),
            // 0).versionName);

            btn_closeabt.setOnClickListener(new View.OnClickListener()
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

    public void deRegisterDialog()
    {
        android.app.AlertDialog.Builder dalertDialog = new android.app.AlertDialog.Builder(context);
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
                new FlipickApplication.DeRegisterDeviceAsynTask(context).execute(oUser);

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
    @Override
    public void onClick(View view)
    {
        // TODO Auto-generated method stub
        if (view == layout_btn_menu)
        {

            PopupMenu popup = new PopupMenu(getApplicationContext(), view);
            //inflate menu with layout mainmenu

            popup.getMenuInflater().inflate(R.menu.toolbar_main_menus,popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId()== R.id.ABOUT){
                        Toast.makeText(context, "ABOUT", Toast.LENGTH_LONG).show();
                        showAboutUsDialog();
                    }
                    if (item.getItemId() == R.id.DEREGISTER){
                        deRegisterDialog();
                    }
                    if (item.getItemId() == R.id.QUIT){
                        backButtonHandler(context);
                    }
                    return false;
                }
            });
            popup.show();

            //this.openOptionsMenu();
           // ((FlipickApplication) getApplicationContext()).setupPopupWindowOnSettings(view, context, oUser);
        }
    }
    public void backButtonHandler(Context context)
    {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context);
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
}