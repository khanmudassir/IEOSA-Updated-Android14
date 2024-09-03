package com.ieosabookreader;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.ieosareader.database.DatabaseInterface;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunFlipickApp extends AppCompatActivity {


    Button btn_to_know_more;
    User oUser = new User(null);
    Boolean isDirectoryexist = false;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ExecutorService executorService  = Executors.newSingleThreadExecutor();
        executorService.execute(() -> System.out.println("Asynchronous task"));
        executorService.execute(() -> System.out.println("Asynchronous task"));
        FlipickStaticData.currentapiVersion = android.os.Build.VERSION.SDK_INT;

        File EPUBDirectory = new File(getApplicationContext().getFilesDir() + "/FlipickBookReader");
        if (EPUBDirectory.exists())
        {
            isDirectoryexist = true;
        }

        FlipickConfig.SetFileConfigPath(this);
        mContext = this;
        oUser = new User(this);
        FlipickStaticData.DATA = null;
        FlipickStaticData.TYPE = null;
        // CopyUserJobXml(FlipickConfig.UserJobFileName,
        // getApplicationContext());
        DatabaseInterface databaseInterface = new DatabaseInterface(getApplicationContext());
        SQLiteDatabase database = databaseInterface.getWritableDatabase();
        database.close();
        FlipickStaticData.OSVersion = android.os.Build.VERSION.RELEASE;

        try
        {
            oUser.GetUserInfo();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            FlipickError.DisplayErrorAsLongToast(getApplicationContext(), e.getMessage());
        }

        if (isDirectoryexist)  {
            Log.e("TAG", "If 02");
         /*   File oFile = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                //Android 11+
                Log.e("TAG", "A-11 Not created Directory Not exist 2");

            } else {
                //Old Android version
                Log.e("TAG", "SDK_INT < R");
                oFile = new File(Environment.getExternalStorageDirectory().toString() + "/FlipickBookReader");
            }*/
            File oFile = new File(Environment.getExternalStorageDirectory().toString() + "/FlipickBookReader");
            try
            {
                FlipickIO.DeleteDirectoryRecursive(oFile);
                if (oUser.IsPreviousUserRegistered())
                {
                    oUser.clearSharedPreferences();
                    oUser = new User(getApplicationContext());
                }
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            isDirectoryexist = false;
        } else {
            Log.e("TAG", "Directory Not exist");
        }

        if (oUser.IsPreviousUserRegistered() && (!(oUser.Logout).equalsIgnoreCase("Y"))) {
            Log.e("TAG", "If 01");
            // boolean isAppVersionLess =
            // FlipickConfig.isAppVirsionLess(getApplicationContext());
            // if (isAppVersionLess)
            // {
            if (!FlipickConfig.getPagerCurrentPage(getApplicationContext()).equals("") && !FlipickConfig.getPagerCurrentPageJobId(getApplicationContext()).equals(""))
            {
                FlipickConfig.Part2ContentRootPath = oUser.UserName.trim();
                FlipickConfig.ContentRootPath = FlipickConfig.Part1ContentRootPath + "/" + FlipickConfig.Part2ContentRootPath;

                Intent oIntent = new Intent(getApplicationContext(), flipickpageviewer.class);
                oIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();

                bundle.putString("JobId", FlipickConfig.getPagerCurrentPageJobId(getApplicationContext()));
                bundle.putString("Activity", "RunFlipickApp");
                bundle.putString("TransitionType", "Right_In_Left_Out");
                oIntent.putExtras(bundle);
                startActivity(oIntent);
                finish();
            }
            else
            {
                Log.e("TAG", "Else 9");

                Intent intent;
                if (FlipickConfig.getDeviceHeight(getApplicationContext()) != 0.0 && FlipickConfig.getDeviceWidth(getApplicationContext()) != 0.0)
                {
                    intent = new Intent(getApplicationContext(), SplashScreen.class);
                    // intent = new Intent(getApplicationContext(),
                    // Blank_webview.class);

                }
                else {
                    intent = new Intent(getApplicationContext(), Blank_webview.class);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
            // }
            // else
            // {
            // Intent intent = new Intent(getApplicationContext(),
            // SplashScreen.class);
            // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
            // Intent.FLAG_ACTIVITY_NEW_TASK);
            // startActivity(intent);
            // finish();
            // }
        } else {
            Log.e("TAG", "Else 10");
            // new LoadUserJobsLibraryAsync(getApplicationContext(), oUser,
            // edt_activation_code.getText().toString().trim()).execute();
            new LoadUserJobsLibraryAsync(mContext).execute();// ,"MainActivity","").execute();
            /*
             * Intent intent = new Intent(getApplicationContext(), Login.class);
             * intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
             * Intent.FLAG_ACTIVITY_NEW_TASK); startActivity(intent); finish();
             */
        }
    }

    /*
     * private void CopyUserJobXml(String filename, Context _context) {
     * InputStream in = null; OutputStream out = null; AssetManager assetManager
     * = _context.getAssets(); try { in = assetManager.open(filename); String
     * newFileName = FlipickConfig.ContentRootPath + "/" + filename; out = new
     * FileOutputStream(newFileName); FlipickIO.CopyFile(in, out); in.close();
     * in = null; out.flush(); out.close(); out = null; } catch (Exception e) {
     * FlipickError.DisplayErrorAsLongToast(_context, e.getMessage()); }
     *
     * }
     */

}