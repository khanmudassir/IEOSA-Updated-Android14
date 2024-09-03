package com.ieosabookreader;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ieosareader.database.DatabaseInterface;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

public class JobInfo
{
    public static String TABLE_NAME = "JobInfo";

    public static String KEY_ID = "Id";
    public static String KEY_THUMBNAIL_URL = "ThumbnailUrl";
    public static String KEY_TITLE = "Title";
    public static String KEY_AUTHOR = "Author";
    public static String KEY_EPUB_URL = "EPUBUrl";
    public static String KEY_BOOK_TYPE = "BookType";
    public static String KEY_PRODUCT_UPDATE = "ProductUpdate";
    public static String KEY_HAS_DOWNLOADED = "HasDownloaded";
    public static String KEY_HAS_PRODUCT_UPDATED = "HasProductUpdated";
    public static String KEY_IS_SIDE_LOADED = "IsSideLoaded";

    // new added column
    public static String KEY_PRODUCT_TYPE = "ProductType";
    public static String KEY_PRODUCT_EXPIRYDATE = "ProductExpiryDate";
    public static String KEY_LANGUAGE = "Language";

    // new added column 16-Nov-2015
    public static String KEY_PRODUCT_URL = "ProductUrl";
    public static String KEY_IS_FREE_TRIAL = "Is_free_trial";
    public static String KEY_UPDATE_NEWVERSION = "UpdateNewVersion";

    // new added column for category showing 17-feb-2019
    public static String KEY_CATEGORY_ID = "CategoryId";
    public static String KEY_CATEGORY_NAME = "CategoryName";

    /*
     * public static String CREATE_JOB_INFO_TABLE = "CREATE TABLE " + TABLE_NAME
     * + "(" + JobInfo.KEY_ID + " VARCHAR PRIMARY KEY," + JobInfo.KEY_TITLE +
     * " TEXT," + JobInfo.KEY_AUTHOR + " TEXT," + JobInfo.KEY_THUMBNAIL_URL +
     * " VARCHAR," + JobInfo.KEY_EPUB_URL + " VARCHAR," +
     * JobInfo.KEY_PRODUCT_EXPIRYDATE + " VARCHAR," + JobInfo.KEY_LANGUAGE +
     * " VARCHAR," + JobInfo.KEY_PRODUCT_TYPE + " VARCHAR," +
     * JobInfo.KEY_BOOK_TYPE + " TEXT," + JobInfo.KEY_PRODUCT_UPDATE +
     * " VARCHAR," + JobInfo.KEY_IS_SIDE_LOADED + " TEXT," +
     * JobInfo.KEY_HAS_DOWNLOADED + " INTEGER," +
     * JobInfo.KEY_HAS_PRODUCT_UPDATED + " INTEGER," + JobInfo.KEY_PRODUCT_URL +
     * " VARCHAR," + JobInfo.KEY_IS_FREE_TRIAL + " TEXT," +
     * JobInfo.KEY_UPDATE_NEWVERSION + " TEXT" + ")";
     */

    public static String CREATE_JOB_INFO_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + JobInfo.KEY_ID + " VARCHAR PRIMARY KEY," + JobInfo.KEY_TITLE + " TEXT," + JobInfo.KEY_AUTHOR + " TEXT,"
            + JobInfo.KEY_THUMBNAIL_URL + " VARCHAR," + JobInfo.KEY_EPUB_URL + " VARCHAR," + JobInfo.KEY_PRODUCT_EXPIRYDATE + " VARCHAR," + JobInfo.KEY_LANGUAGE + " VARCHAR,"
            + JobInfo.KEY_PRODUCT_TYPE + " VARCHAR," + JobInfo.KEY_CATEGORY_ID + " VARCHAR," + JobInfo.KEY_CATEGORY_NAME + " VARCHAR," + JobInfo.KEY_BOOK_TYPE + " TEXT," + JobInfo.KEY_PRODUCT_UPDATE
            + " VARCHAR," + JobInfo.KEY_IS_SIDE_LOADED + " TEXT," + JobInfo.KEY_HAS_DOWNLOADED + " INTEGER," + JobInfo.KEY_HAS_PRODUCT_UPDATED + " INTEGER," + JobInfo.KEY_PRODUCT_URL + " VARCHAR,"
            + JobInfo.KEY_IS_FREE_TRIAL + " TEXT," + JobInfo.KEY_UPDATE_NEWVERSION + " TEXT" + ")";

    public static final String ADD_KEY_PRODUCT_TYPE = "ALTER TABLE " + JobInfo.TABLE_NAME + " ADD COLUMN " + JobInfo.KEY_PRODUCT_TYPE + " VARCHAR DEFAULT ''";
    public static final String ADD_KEY_PRODUCT_EXPIRYDATE = "ALTER TABLE " + JobInfo.TABLE_NAME + " ADD COLUMN " + JobInfo.KEY_PRODUCT_EXPIRYDATE + " VARCHAR DEFAULT ''";
    public static final String ADD_KEY_LANGUAGE = "ALTER TABLE " + JobInfo.TABLE_NAME + " ADD COLUMN " + JobInfo.KEY_LANGUAGE + " VARCHAR DEFAULT 'en'";
    public static final String ADD_KEY_PRODUCT_URL = "ALTER TABLE " + JobInfo.TABLE_NAME + " ADD COLUMN " + JobInfo.KEY_PRODUCT_URL + " VARCHAR DEFAULT ''";
    public static final String ADD_KEY_IS_FREE_TRIAL = "ALTER TABLE " + JobInfo.TABLE_NAME + " ADD COLUMN " + JobInfo.KEY_IS_FREE_TRIAL + " TEXT DEFAULT 'N'";

    public static final String ADD_KEY_UPDATE_NEWVERSION = "ALTER TABLE " + JobInfo.TABLE_NAME + " ADD COLUMN " + JobInfo.KEY_UPDATE_NEWVERSION + " TEXT DEFAULT 'N'";

    public static final String ADD_KEY_CATEGORY_ID = "ALTER TABLE " + JobInfo.TABLE_NAME + " ADD COLUMN " + JobInfo.KEY_CATEGORY_ID + " VARCHAR DEFAULT ''";
    public static final String ADD_KEY_CATEGORY_NAME = "ALTER TABLE " + JobInfo.TABLE_NAME + " ADD COLUMN " + JobInfo.KEY_CATEGORY_NAME + " VARCHAR DEFAULT ''";

    public String Last_page_link = "";

    // after fontchange
    public String LastPageFontPageName = "";
    public String LastPageFontColumnNo = "";

    public String JobTitle = "";
    public String JobAuthor = "";
    public String JobUrl = "";
    public String JobId = "";
    public String ProductUpdated = "";
    public String EPUBUrl = "";
    public String JobThumbnailPath = "";
    public String BookType = "EPUB"; // EPUB2/EPUB3
    public String IsExamHall = "No";
    public String IsSideLoaded = "No";
    public User oUserinfo = null;
    public float JobWidth;
    public float JobHeight;
    public String PathOPS = "";
    private boolean IsEpubEncrypted = false;
    // private String PathPackage = "";
    public Boolean HasBookDownloaded = false;
    public Boolean HasProductUpdated = false;
    public String SideLoadCoverImage = "";

    // add new column
    public String ProductType = "";
    public String ProductExpiryDate = "";
    public String Language = "en";
    public String ProductUrl = "";
    public String Is_Free_Trial = "N";
    public String UpdateNewVersion = "N";

    public String CategoryId = "";
    public String CategoryName = "";

    MagentoServiceInterface oMagentoServiceInterface = new MagentoServiceInterface();

    public List<String> PageXHTMLList = new ArrayList<String>();
    public List<String> PaginationXHTMLList = new ArrayList<String>();
    public List<String> ReflowColumnShift = new ArrayList<String>();
    public List<String> ReflowNewFileNames = new ArrayList<String>();
    public List<Integer> XHTMLMeasuredWidth = new ArrayList<Integer>();
    public List<PageInfo> BookmarkList = new ArrayList<PageInfo>();

    // for Hyperlinks
    public List<String> PageXHTML_LinkIds = new ArrayList<String>();
    public List<String> LinkedIDsOnPageIndex = new ArrayList<String>();
    public List<String> LinkedIDs = new ArrayList<String>();
    ArrayList<String> originalPages = new ArrayList<String>();

    String JobTemp = "";
    String TempPath = "";

    public List<String> listCategoryNames = new ArrayList<String>();
    public List<String> listCategoryIds = new ArrayList<String>();

    private Activity oMainActivity;
    RowTransposition oRowTransposition = new RowTransposition();

    DatabaseInterface databaseInterface;
    PageInfo oPageInfo;

    public JobInfo(Object _context)
    {
        oMainActivity = (Activity) _context;
        oUserinfo = new User((Activity) _context);
        databaseInterface = new DatabaseInterface((Context) _context);
    }

    public JobInfo()
    {
        // TODO Auto-generated constructor stub
    }

    public void addJobInfosInDB(ArrayList<JobInfo> jobArray, ArrayList<JobInfo> old_job_array)
    {
        SQLiteDatabase db = databaseInterface.getWritableDatabase();
        for (JobInfo jobInfo : jobArray)
        {
            if (old_job_array != null)
            {
                for (JobInfo oldjobInfo : old_job_array)
                {
                    if (oldjobInfo.HasBookDownloaded && oldjobInfo.JobId.equals(jobInfo.JobId))
                    {
                        if (!oldjobInfo.ProductUpdated.equalsIgnoreCase(jobInfo.ProductUpdated))
                        {
                            jobInfo.HasProductUpdated = true;
                        }
                        else
                        {
                            if (oldjobInfo.HasProductUpdated)
                            {
                                jobInfo.HasProductUpdated = oldjobInfo.HasProductUpdated;
                            }

                        }
                        jobInfo.HasBookDownloaded = oldjobInfo.HasBookDownloaded;
                        jobInfo.Language = oldjobInfo.Language;
                    }

                }
            }

            addJobInfoInDB(jobInfo, db);
        }
        db.close();
    }

    public void downloadThumbnails(ArrayList<JobInfo> jobArray) throws Exception
    {
        for (JobInfo jobInfo : jobArray)
        {

            String path = FlipickConfig.ContentRootPath + "/Images";// +
            // jobInfo.JobId;
            File ImgDirectory = new File(path);
            if (!ImgDirectory.exists())
            {
                ImgDirectory.mkdir();
                // return;
            }
            File JobImgDir = new File(path + "/" + jobInfo.JobId);
            if (!JobImgDir.exists())
            {
                JobImgDir.mkdir();
            }
            File ImagePath = new File(FlipickConfig.ContentRootPath + "/Images/" + jobInfo.JobId + "/" + jobInfo.JobId + ".jpg");
            if (ImagePath.exists())
            {

            }
            else
            {
                if (jobInfo.JobThumbnailPath != null && jobInfo.JobThumbnailPath.trim().length() > 0)
                {

                    FlipickIO.DonwloadAndSaveFileWithoutProgress(jobInfo.JobUrl, FlipickConfig.ContentRootPath + "/Images/" + jobInfo.JobId + "/" + jobInfo.JobId + ".jpg");

                }
            }

        }
    }

    public void addJobInfoInDB(JobInfo jobInfo, SQLiteDatabase database)
    {
        SQLiteDatabase db = database;
        if (database == null) db = databaseInterface.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, jobInfo.JobId);
        values.put(KEY_TITLE, jobInfo.JobTitle);
        values.put(KEY_AUTHOR, jobInfo.JobAuthor);
        values.put(KEY_THUMBNAIL_URL, jobInfo.JobThumbnailPath);
        values.put(KEY_EPUB_URL, jobInfo.EPUBUrl);
        values.put(KEY_BOOK_TYPE, jobInfo.BookType);
        values.put(KEY_PRODUCT_UPDATE, jobInfo.ProductUpdated);
        values.put(KEY_IS_SIDE_LOADED, jobInfo.IsSideLoaded);

        // new added
        values.put(KEY_PRODUCT_EXPIRYDATE, jobInfo.ProductExpiryDate);
        values.put(KEY_PRODUCT_TYPE, jobInfo.ProductType);
        values.put(KEY_LANGUAGE, jobInfo.Language);
        values.put(KEY_PRODUCT_URL, jobInfo.ProductUrl);
        values.put(KEY_IS_FREE_TRIAL, jobInfo.Is_Free_Trial);
        // end

        values.put(KEY_UPDATE_NEWVERSION, jobInfo.UpdateNewVersion);
        values.put(KEY_CATEGORY_ID, jobInfo.CategoryId);
        values.put(KEY_CATEGORY_NAME, jobInfo.CategoryName);
        if (jobInfo.HasBookDownloaded) values.put(KEY_HAS_DOWNLOADED, 1);
        else values.put(KEY_HAS_DOWNLOADED, 0);

        if (jobInfo.HasProductUpdated) values.put(KEY_HAS_PRODUCT_UPDATED, 1);
        else values.put(KEY_HAS_PRODUCT_UPDATED, 0);

        db.insert(TABLE_NAME, null, values);
        if (database == null) db.close();
    }

    public void updateUpdateNewVersionInJobDB(String UpdateNewVersion, String JobId, Context context)
    {
        DatabaseInterface databaseInterface = new DatabaseInterface(context);
        SQLiteDatabase db = databaseInterface.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_UPDATE_NEWVERSION, UpdateNewVersion);

        db.update(TABLE_NAME, values, KEY_ID + " = ?", new String[]
                { String.valueOf(JobId) });
        db.close();
    }

    public void updateJobInDB(JobInfo jobInfo)
    {
        SQLiteDatabase db = databaseInterface.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (jobInfo.HasBookDownloaded) values.put(KEY_HAS_DOWNLOADED, 1);
        else values.put(KEY_HAS_DOWNLOADED, 0);

        if (jobInfo.HasProductUpdated) values.put(KEY_HAS_PRODUCT_UPDATED, 1);
        else values.put(KEY_HAS_PRODUCT_UPDATED, 0);

        db.update(TABLE_NAME, values, KEY_ID + " = ?", new String[]
                { String.valueOf(jobInfo.JobId) });
        db.close();
    }

    public void updateLanguageInJobDB(String Language, String JobId)
    {
        SQLiteDatabase db = databaseInterface.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_LANGUAGE, Language);

        db.update(TABLE_NAME, values, KEY_ID + " = ?", new String[]
                { String.valueOf(JobId) });
        db.close();
    }

    public void updateBookTypeInJobDB(String BookType, String JobId)
    {
        SQLiteDatabase db = databaseInterface.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_BOOK_TYPE, BookType);

        db.update(TABLE_NAME, values, KEY_ID + " = ?", new String[]
                { String.valueOf(JobId) });
        db.close();
    }

    public ArrayList<JobInfo> getJobInfoFromDB()
    {
        ArrayList<JobInfo> jobInfos = new ArrayList<JobInfo>();
        SQLiteDatabase db = databaseInterface.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            do
            {
                JobInfo info = new JobInfo();
                info.JobId = cursor.getString(cursor.getColumnIndex(KEY_ID)) + "";
                info.JobTitle = cursor.getString(cursor.getColumnIndex(KEY_TITLE));
                info.JobAuthor = cursor.getString(cursor.getColumnIndex(KEY_AUTHOR));
                info.JobThumbnailPath = cursor.getString(cursor.getColumnIndex(KEY_THUMBNAIL_URL));
                info.EPUBUrl = cursor.getString(cursor.getColumnIndex(KEY_EPUB_URL));
                info.BookType = cursor.getString(cursor.getColumnIndex(KEY_BOOK_TYPE));
                info.ProductUpdated = cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_UPDATE));
                info.IsSideLoaded = cursor.getString(cursor.getColumnIndex(KEY_IS_SIDE_LOADED));
                // new added
                info.ProductExpiryDate = cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_EXPIRYDATE));
                info.ProductType = cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_TYPE));
                info.Language = cursor.getString(cursor.getColumnIndex(KEY_LANGUAGE));
                info.ProductUrl = cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_URL));
                info.Is_Free_Trial = cursor.getString(cursor.getColumnIndex(KEY_IS_FREE_TRIAL));
                // end

                // new
                info.UpdateNewVersion = cursor.getString(cursor.getColumnIndex(KEY_UPDATE_NEWVERSION));

                // end

                info.HasBookDownloaded = (cursor.getInt(cursor.getColumnIndex(KEY_HAS_DOWNLOADED)) == 0 ? false : true);
                info.HasProductUpdated = (cursor.getInt(cursor.getColumnIndex(KEY_HAS_PRODUCT_UPDATED)) == 0 ? false : true);

                info.CategoryId = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_ID));
                info.CategoryName = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_NAME));

                info.JobUrl = info.JobThumbnailPath;

                // if Product type is Rent/Package then check ExpiryDate and do
                // not add in list
                if (info.ProductType.equalsIgnoreCase("rent") || info.ProductType.equalsIgnoreCase("package"))
                {
                    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                    if (info.ProductExpiryDate.split(" ")[0].compareTo(date) < 0)
                    {
                    }
                    else
                    {
                        jobInfos.add(info);
                    }
                }
                else
                {
                    jobInfos.add(info);
                }
            }
            while (cursor.moveToNext());
        }
        db.close();
        return jobInfos;
    }

    public ArrayList<JobInfo> getCategoryJobInfoFromDB(String categoryID)
    {
        ArrayList<JobInfo> jobInfos = new ArrayList<JobInfo>();
        SQLiteDatabase db = databaseInterface.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE CategoryId='" + categoryID + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            do
            {
                JobInfo info = new JobInfo();
                info.JobId = cursor.getString(cursor.getColumnIndex(KEY_ID)) + "";
                info.JobTitle = cursor.getString(cursor.getColumnIndex(KEY_TITLE));
                info.JobAuthor = cursor.getString(cursor.getColumnIndex(KEY_AUTHOR));
                info.JobThumbnailPath = cursor.getString(cursor.getColumnIndex(KEY_THUMBNAIL_URL));
                info.EPUBUrl = cursor.getString(cursor.getColumnIndex(KEY_EPUB_URL));
                info.BookType = cursor.getString(cursor.getColumnIndex(KEY_BOOK_TYPE));
                info.ProductUpdated = cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_UPDATE));
                info.IsSideLoaded = cursor.getString(cursor.getColumnIndex(KEY_IS_SIDE_LOADED));
                // new added
                info.ProductExpiryDate = cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_EXPIRYDATE));
                info.ProductType = cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_TYPE));
                info.Language = cursor.getString(cursor.getColumnIndex(KEY_LANGUAGE));
                info.ProductUrl = cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_URL));
                info.Is_Free_Trial = cursor.getString(cursor.getColumnIndex(KEY_IS_FREE_TRIAL));
                // end

                // new
                info.UpdateNewVersion = cursor.getString(cursor.getColumnIndex(KEY_UPDATE_NEWVERSION));

                // end

                info.HasBookDownloaded = (cursor.getInt(cursor.getColumnIndex(KEY_HAS_DOWNLOADED)) == 0 ? false : true);
                info.HasProductUpdated = (cursor.getInt(cursor.getColumnIndex(KEY_HAS_PRODUCT_UPDATED)) == 0 ? false : true);

                info.CategoryId = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_ID));
                info.CategoryName = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_NAME));

                info.JobUrl = info.JobThumbnailPath;

                // if Product type is Rent/Package then check ExpiryDate and do
                // not add in list
                if (info.ProductType.equalsIgnoreCase("rent") || info.ProductType.equalsIgnoreCase("package"))
                {
                    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                    if (info.ProductExpiryDate.split(" ")[0].compareTo(date) < 0)
                    {
                    }
                    else
                    {
                        jobInfos.add(info);
                    }
                }
                else
                {
                    jobInfos.add(info);
                }
            }
            while (cursor.moveToNext());
        }
        db.close();
        return jobInfos;
    }

    public ArrayList<String> getDistinctCategory(Context context)
    {

        // JobInfo info = new JobInfo();
        // listCategoryNames
        ArrayList<String> listcategories = new ArrayList<String>();
        DatabaseInterface databaseInterface = new DatabaseInterface(context);
        SQLiteDatabase db = databaseInterface.getWritableDatabase();
        String selectQuery = "SELECT DISTINCT CategoryName FROM " + TABLE_NAME + " ORDER BY CategoryName ASC";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            do
            {

                String CategoryName = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_NAME));
                listcategories.add(CategoryName);

            }
            while (cursor.moveToNext());
        }
        db.close();
        return listcategories;
    }

    public ArrayList<String> getCategoryID(Context context, String CategoryName)
    {

        // JobInfo info = new JobInfo();
        // listCategoryNames
        ArrayList<String> CategoryIds = new ArrayList<String>();
        DatabaseInterface databaseInterface = new DatabaseInterface(context);
        SQLiteDatabase db = databaseInterface.getWritableDatabase();
        // String selectQuery = "SELECT DISTINCT CategoryName FROM " +
        // TABLE_NAME + " ORDER BY CategoryName ASC";
        String selectQuery = "SELECT CategoryId FROM " + TABLE_NAME + " WHERE CategoryName='" + CategoryName + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            do
            {

                String CategoryId = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_ID));
                CategoryIds.add(CategoryId);

            }
            while (cursor.moveToNext());
        }
        db.close();
        return CategoryIds;
    }

    public JobInfo getJobInfoFromDBByJobId(String JobId, Context context)
    {

        JobInfo info = new JobInfo();
        DatabaseInterface databaseInterface = new DatabaseInterface(context);
        SQLiteDatabase db = databaseInterface.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_ID + " = " + JobId;

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            do
            {

                info.JobId = cursor.getString(cursor.getColumnIndex(KEY_ID)) + "";
                info.JobTitle = cursor.getString(cursor.getColumnIndex(KEY_TITLE));
                info.JobAuthor = cursor.getString(cursor.getColumnIndex(KEY_AUTHOR));
                info.JobThumbnailPath = cursor.getString(cursor.getColumnIndex(KEY_THUMBNAIL_URL));
                info.EPUBUrl = cursor.getString(cursor.getColumnIndex(KEY_EPUB_URL));
                info.BookType = cursor.getString(cursor.getColumnIndex(KEY_BOOK_TYPE));
                info.ProductUpdated = cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_UPDATE));
                info.IsSideLoaded = cursor.getString(cursor.getColumnIndex(KEY_IS_SIDE_LOADED));
                // new added
                info.ProductExpiryDate = cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_EXPIRYDATE));
                info.ProductType = cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_TYPE));
                info.Language = cursor.getString(cursor.getColumnIndex(KEY_LANGUAGE));
                info.ProductUrl = cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_URL));
                info.Is_Free_Trial = cursor.getString(cursor.getColumnIndex(KEY_IS_FREE_TRIAL));
                // end

                // new
                info.UpdateNewVersion = cursor.getString(cursor.getColumnIndex(KEY_UPDATE_NEWVERSION));
                // end

                info.HasBookDownloaded = (cursor.getInt(cursor.getColumnIndex(KEY_HAS_DOWNLOADED)) == 0 ? false : true);
                info.HasProductUpdated = (cursor.getInt(cursor.getColumnIndex(KEY_HAS_PRODUCT_UPDATED)) == 0 ? false : true);

                info.CategoryId = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_ID));
                info.CategoryName = cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_NAME));

                info.JobUrl = info.JobThumbnailPath;

            }
            while (cursor.moveToNext());
        }
        db.close();
        return info;
    }

    public void deleteJobInfoTableFromDB(boolean is_refresh)
    {
        SQLiteDatabase db = databaseInterface.getWritableDatabase();
        if (is_refresh) db.delete(TABLE_NAME, KEY_IS_SIDE_LOADED + " = 'No'", null);
        else db.delete(TABLE_NAME, null, null);

        db.close();
    }

    public void deleteJobInfoTable(boolean is_refresh)
    {
        SQLiteDatabase db = databaseInterface.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);

        db.close();
    }

    public void deleteJobInforFromDB(JobInfo jobInfo)
    {
        SQLiteDatabase db = databaseInterface.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?", new String[]
                { String.valueOf(jobInfo.JobId) });
        db.close();
    }

    public void DeletejobFolder() throws Exception
    {
        try
        {
            String path = FlipickConfig.ContentRootPath + "/" + JobId;
            File EPUBFolder = new File(path);
            if (EPUBFolder.exists())
            {
                FlipickIO.DeleteDirectoryRecursive(EPUBFolder);
            }
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    private void savefitdefaultCss(String filePath, Context context)
    {
        float Left = 0, Top = 0;
        float DeviceWidth = FlipickConfig.getDeviceWidth(context);
        float DeviceHeight = FlipickConfig.getDeviceHeight(context);

        float ARW = DeviceWidth / JobWidth;
        float ARH = DeviceHeight / JobHeight;
        float ARFinal = DeviceHeight / JobHeight;

        if (ARW < ARH) ARFinal = ARW;
        else ARFinal = ARH;

        Left = ((DeviceWidth - (JobWidth * ARFinal)) / 2);
        Top = ((DeviceHeight - (JobHeight * ARFinal)) / 2);

        String defaultCssPortrait = "@media (orientation: portrait) {body > :first-child {position: absolute !important;left:" + Left + "px !important;top:" + Top
                + "px !important;-webkit-transform-origin:0 0 !important;-webkit-transform:scale(" + ARFinal + ") !important;}}";

        DeviceWidth = FlipickConfig.getDeviceWidth_L(context);
        ARFinal = DeviceWidth / JobWidth;
        String defaultCssLandscape = "@media (orientation: landscape) {body > :first-child {position: absolute !important;left: 0px !important;top: 0px !important;-webkit-transform-origin:0 0 !important;-webkit-transform:scale("
                + ARFinal + ") !important;  }}";

        String content = defaultCssLandscape + defaultCssPortrait;

        try
        {

            File file = new File(filePath + "/fitcontent.css");

            // If file does not exists, then create it
            if (!file.exists())
            {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();

        }
        catch (IOException e)
        {
            e.printStackTrace();

        }

    }

    private void savedefaultCssForVerticalBook(String filePath, Context context)
    {
        String content;
        if (context.getResources().getBoolean(R.bool.isTablet))
        {

            float DeviceWidth = FlipickStaticData.DeviceWidth;

            // portrait mode
            float scalePortrait = (float) 2.0;// 2.0;
            float calWidth = (DeviceWidth / scalePortrait);
            float calMargin = ((DeviceWidth * 3) / 100);
            float ActualWidth = (calWidth - calMargin);
            String div = "<div style=\"position:absolute;left:" + calMargin + "px;top:" + calMargin + "px;width:" + ActualWidth + "px;height:100px;transform:scale(" + scalePortrait
                    + ");transform-origin:0 0;\">";
            // landscape mode
            float scaleLandscape = (float) 1.3;// 1.5;
            float calWidthL = (DeviceWidth / scaleLandscape);
            float calMarginL = ((DeviceWidth * 3) / 100);
            float ActualWidthL = (calWidthL - calMarginL);
            String divL = "<div style=\"position:absolute;left:" + calMarginL + "px;top:" + calMarginL + "px;width:" + ActualWidthL + "px;height:100px;transform:scale(" + scaleLandscape
                    + ");transform-origin:0 0;\">";

            String defaultCssPortrait = "@media (orientation: portrait) {body > :first-child {position:absolute;left:" + calMargin + "px;top:" + calMargin + "px;width:" + ActualWidth
                    + "px;height:100px;transform:scale(" + scalePortrait + ");transform-origin:0 0;}}";

            String defaultCssLandscape = "@media (orientation: landscape) {body > :first-child {position:absolute;left:" + calMarginL + "px;top:" + calMarginL + "px;width:" + ActualWidthL
                    + "px;height:100px;transform:scale(" + scaleLandscape + ");transform-origin:0 0;}}";

            content = defaultCssLandscape + defaultCssPortrait;
        }
        else
        {
            float DeviceWidth = FlipickStaticData.DeviceWidth;

            // portrait mode
            float scalePortrait = (float) 3.0;// 2.0;
            float calWidth = (DeviceWidth / scalePortrait);
            float calMargin = ((DeviceWidth * 3) / 100);
            float ActualWidth = (calWidth - calMargin);
            String div = "<div style=\"position:absolute;left:" + calMargin + "px;top:" + calMargin + "px;width:" + ActualWidth + "px;height:100px;transform:scale(" + scalePortrait
                    + ");transform-origin:0 0;\">";
            // landscape mode
            float scaleLandscape = (float) 1.8;// 1.5;
            float calWidthL = (DeviceWidth / scaleLandscape);
            float calMarginL = ((DeviceWidth * 3) / 100);
            float ActualWidthL = (calWidthL - calMarginL);
            String divL = "<div style=\"position:absolute;left:" + calMarginL + "px;top:" + calMarginL + "px;width:" + ActualWidthL + "px;height:100px;transform:scale(" + scaleLandscape
                    + ");transform-origin:0 0;\">";

            String defaultCssPortrait = "@media (orientation: portrait) {body > :first-child {position:absolute;left:" + calMargin + "px;top:" + calMargin + "px;width:" + ActualWidth
                    + "px;height:100px;transform:scale(" + scalePortrait + ");transform-origin:0 0;}}";

            String defaultCssLandscape = "@media (orientation: landscape) {body > :first-child {position:absolute;left:" + calMarginL + "px;top:" + calMarginL + "px;width:" + ActualWidthL
                    + "px;height:100px;transform:scale(" + scaleLandscape + ");transform-origin:0 0;}}";

            content = defaultCssLandscape + defaultCssPortrait;
        }
        try
        {

            File file = new File(filePath + "/fitVerticalcontent.css");

            // If file does not exists, then create it
            if (!file.exists())
            {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();

        }
        catch (IOException e)
        {
            e.printStackTrace();

        }

    }

    public void UpdateScaleAndViewPort(String JobScaleCSS, String FilePath, String JobId) throws Exception
    {
        Document oXMLDoc = FlipickIO.GetDocumentFromFile(FilePath);

        NodeList NL_Metas = oXMLDoc.getElementsByTagName("meta");
        for (int n = 0; n < NL_Metas.getLength(); n++)
        {
            Element EL_Meta = (Element) NL_Metas.item(n);
            if (EL_Meta.getAttribute("name").equals("viewport"))
            {

                EL_Meta.setAttribute("content", "height=device-height, width=device-width,user-scalable=yes");

                EL_Meta.appendChild(oXMLDoc.createTextNode(" "));
            }
            else
            {
                EL_Meta.appendChild(oXMLDoc.createTextNode(" "));
            }
        }
        if (!FilePath.contains("toc.xhtml"))
        {
            NodeList NL_Links = oXMLDoc.getElementsByTagName("link");
            for (int n = 0; n < NL_Links.getLength(); n++)
            {
                Element EL_Link = (Element) NL_Links.item(n);
                EL_Link.appendChild(oXMLDoc.createTextNode(" "));
            }

            NodeList NL_Body = oXMLDoc.getElementsByTagName("body");
            for (int n = 0; n < NL_Body.getLength(); n++)
            {
                Element EL_Body = (Element) NL_Body.item(n);

                EL_Body.setAttribute("style", "overflow-x:hidden;overflow-y:auto;");

            }
        }

        NodeList NLHead = oXMLDoc.getElementsByTagName("head");
        Element E_Head = (Element) NLHead.item(0);
        Date oDate = Calendar.getInstance().getTime();
        NodeList NL_CssLink = E_Head.getElementsByTagName("link");
        for (int n = 0; n < NL_CssLink.getLength(); n++)
        {
            Element element = (Element) NL_CssLink.item(n);
            if (element.hasAttribute("type") && element.getAttribute("type").equals("text/css"))
            {
                String Value = element.getAttribute("href");
                Value = Value + "?Random=L" + oDate.toString().replace(":", "").replace(" ", "");
                element.setAttribute("href", Value);
            }
        }

        Element E_PageScript = oXMLDoc.createElement("script");
        E_PageScript.setAttribute("type", "text/javascript");
        E_PageScript.setAttribute("src", "file:///" + "android_asset" + "/PageScript/PageScript.js");
        E_Head.appendChild(E_PageScript);

        if (!FilePath.contains("toc.xhtml"))
        {
            // Highlight
            Element E_Script_highlight = oXMLDoc.createElement("script");
            E_Script_highlight.setAttribute("type", "text/javascript");
            E_Script_highlight.setAttribute("src", "file:///" + "android_asset" + "/highlight/highlight.js");
            E_Head.appendChild(E_Script_highlight);
            E_Script_highlight.getAttribute("src");

            Element E_Link_highlight_css = oXMLDoc.createElement("link");
            E_Link_highlight_css.setAttribute("type", "text/css");
            E_Link_highlight_css.setAttribute("rel", "stylesheet");
            E_Link_highlight_css.setAttribute("href", "file:///" + "android_asset" + "/highlight/highlight.css");
            E_Head.appendChild(E_Link_highlight_css);
            E_Link_highlight_css.getAttribute("src");
            // End Highlight

            // for fitting option
            Element E_Fitting_css = oXMLDoc.createElement("link");
            E_Fitting_css.setAttribute("type", "text/css");
            E_Fitting_css.setAttribute("rel", "stylesheet");
            String path = "";
            if (!PathOPS.equals(""))
            {
                path = "file:///" + FlipickConfig.ContentRootPath + "/" + JobId + "/" + PathOPS + "/fitcontent.css";
            }
            else
            {
                path = "file:///" + FlipickConfig.ContentRootPath + "/" + JobId + "/fitcontent.css";
            }
            path = path.replace("file:////", "file:///");
            E_Fitting_css.setAttribute("href", path);
            E_Head.appendChild(E_Fitting_css);

            // for fitting option
            Element E_FlipickBookReader_css = oXMLDoc.createElement("link");
            E_FlipickBookReader_css.setAttribute("type", "text/css");
            E_FlipickBookReader_css.setAttribute("rel", "stylesheet");
            E_FlipickBookReader_css.setAttribute("href", "file:///" + "android_asset" + "/FlipickBookReader.css");
            E_Head.appendChild(E_FlipickBookReader_css);
        }
        FlipickIO.SaveXMLFileFromDocument(FilePath, oXMLDoc);
    }

    public void DownloadEPUBFile(Context context) throws Throwable
    {

        try
        {
            if (!IsSideLoaded.equalsIgnoreCase("Yes"))
            {
                String path = FlipickConfig.ContentRootPath + "/" + JobId;
                File EPUBDirectory = new File(path);
                if (EPUBDirectory.exists())
                {
                    return;
                }

                final int MAX_VALUE = 9999;
                Random rand = new Random();
                int randValue = rand.nextInt(MAX_VALUE);
                JobTemp = randValue + "";
                TempPath = FlipickConfig.ContentRootPath + "/" + JobId + "__" + JobTemp;
                EPUBDirectory = new File(TempPath);

                if (EPUBDirectory.exists())
                {
                    FlipickIO.DeleteFolder(EPUBDirectory);
                }

                File EPUBTempDirectory = new File(TempPath);
                if (!EPUBTempDirectory.exists()) EPUBTempDirectory.mkdirs();

                if (!EPUBUrl.equals(""))
                {
                    EPUBUrl = EPUBUrl.replaceAll("\\s+", "%20");
                }
                else
                {
                    Exception oException = new Exception("Empty download url.");
                    throw oException;

                }
                // EPUBUrl = "http://192.168.200.9/WebFiles/560.epub";
                int Attempt = 0;
                FlipickIO.DonwloadAndSaveFile(Attempt, EPUBUrl, TempPath + "/" + JobId + ".zip");
                FlipickIO.extractFolder(TempPath + "/" + JobId + ".zip", TempPath);
            }
            else
            {
                TempPath = FlipickConfig.ContentRootPath + "/" + JobId;
            }

            File file = new File(TempPath + "/OPS/encrypt.txt");
            if (file.exists()) IsEpubEncrypted = true;

            Document oXMLJobInfoDoc = CreateJobInfoXML(TempPath, context, JobId);
            originalPages = new ArrayList<String>();
            if (BookType.equals("EPUB3")) ApplyScalingToXHTMLPages(context, TempPath, JobId);
            else
            {
                RemoveJSScriptFile(context, TempPath);
            }

            if (!BookType.equals("REFLOWABLE") && !BookType.equals("VERTICALSCROLL"))
            {
                if (IsEpubEncrypted)
                {
                    DecryptSvg(context, TempPath);
                }
            }
            Toc oToc = new Toc();
            oToc.UpdateTOC(BookType, PathOPS, originalPages, TempPath, context, oXMLJobInfoDoc, JobId);

            UpdateJobInfoXmlWithOrientation(oXMLJobInfoDoc, TempPath, JobId);
        }
        catch (Throwable e)
        {

            throw e;

        }
    }

    public String copyFilesInTheTempFolder(String JobId)
    {
        try
        {
            final int MAX_VALUE = 9999;
            Random rand = new Random();
            int randValue = rand.nextInt(MAX_VALUE);
            String JobTemp = randValue + "";
            TempPath = FlipickConfig.ContentRootPath + "/" + JobId + "__" + JobTemp;

            File JobTemp_FOLDER = new File(TempPath);
            if (!JobTemp_FOLDER.exists())
            {
                JobTemp_FOLDER.mkdirs();
            }

            File sourceLocation = new File(FlipickConfig.ContentRootPath + "/" + JobId);
            File targetLocation = new File(TempPath);
            FlipickIO.copyfolderDirectoryRecursively(sourceLocation, targetLocation);

            String XML_SourceLocation = TempPath + "/" + JobId + ".xml";
            Document oDocJobInfo = FlipickIO.GetDocumentFromFile(XML_SourceLocation);

            NodeList NL_PathOPS = oDocJobInfo.getElementsByTagName("PathOPS");
            String PathOPS = "";
            if (NL_PathOPS.item(0).getFirstChild() != null)
            {
                PathOPS = NL_PathOPS.item(0).getFirstChild().getNodeValue();
            }
            String Delete_TempJobXml = TempPath + "/" + JobId + ".xml";
            if (new File(Delete_TempJobXml).exists())
            {
                new File(Delete_TempJobXml).delete();
            }
            if (!new File(Delete_TempJobXml).exists())
            {
                String Original_Job_XML = "";
                if (!PathOPS.equals(""))
                {
                    Original_Job_XML = TempPath + "/" + PathOPS + "/backupXML/" + JobId + ".xml";
                }
                else
                {
                    Original_Job_XML = TempPath + "/backupXML/" + JobId + ".xml";
                }
                String Temp_Folder = TempPath + "/" + JobId + ".xml";
                FlipickIO.CopyFile(Original_Job_XML, Temp_Folder);
            }

            File Xhtml_Old = null;
            if (!PathOPS.equals(""))
            {
                Xhtml_Old = new File(TempPath + "/" + PathOPS);
            }
            else
            {
                Xhtml_Old = new File(TempPath);
            }
            File list[] = Xhtml_Old.listFiles();
            for (int i = 0; i < list.length; i++)
            {
                String FileName = list[i].getName();
                if (FileName.toLowerCase().indexOf(".xhtml") != -1)
                {
                    new File(list[i].getAbsolutePath()).delete();
                }
            }
            // // copy all xhtml from backup to OPS
            File Original_Xhtmls = null;
            if (!PathOPS.equals(""))
            {
                Original_Xhtmls = new File(TempPath + "/" + PathOPS + "/backup/");
            }
            else
            {
                Original_Xhtmls = new File(TempPath + "/backup/");
            }
            File list_xhtmls[] = Original_Xhtmls.listFiles();
            for (int j = 0; j < list_xhtmls.length; j++)
            {
                String FilePath = list_xhtmls[j].getAbsolutePath();
                String XhtmlFileName = list_xhtmls[j].getName();
                String SourcePath = "";
                if (!PathOPS.equals(""))
                {
                    SourcePath = TempPath + "/" + PathOPS + "/" + XhtmlFileName;
                }
                else
                {
                    SourcePath = TempPath + "/" + XhtmlFileName;
                }
                FlipickIO.CopyFile(FilePath, SourcePath);
            }

        }
        catch (Exception e)
        {
            FlipickStaticData.ThreadMessage = e.getMessage() + ":" + e.getCause();
        }
        return TempPath;
    }

    // backup original files for reflowable book
    public void BackupOriginalFiles(String JobId, String TempPath)
    {
        try
        {
            String JobFolderPath = TempPath;

            Document oDocJobInfo = FlipickIO.GetDocumentFromFile(TempPath + "/" + JobId + ".xml");

            NodeList NL_PathOPS = oDocJobInfo.getElementsByTagName("PathOPS");
            String PathOPS = "";
            if (NL_PathOPS.item(0).getFirstChild() != null)
            {
                PathOPS = NL_PathOPS.item(0).getFirstChild().getNodeValue();
            }

            // backup job xml
            String JobXml_SourcePath = TempPath + "/" + JobId + ".xml";
            String JobXml_TargetLocation = "";
            if (!PathOPS.equals(""))
            {
                JobXml_TargetLocation = JobFolderPath + "/" + PathOPS + "/backupXML";
            }
            else
            {
                JobXml_TargetLocation = JobFolderPath + "/backupXML";
            }
            File JobXml_FOLDER = new File(JobXml_TargetLocation);
            if (!JobXml_FOLDER.exists())
            {
                JobXml_FOLDER.mkdirs();
            }
            FlipickIO.CopyFile(JobXml_SourcePath, JobXml_TargetLocation + "/" + JobId + ".xml");

            // backup all xhtml files
            String targetLocation = "";
            if (!PathOPS.equals(""))
            {
                targetLocation = JobFolderPath + "/" + PathOPS + "/backup";
            }
            else
            {
                targetLocation = JobFolderPath + "/backup";
            }
            File Backup_FOLDER = new File(targetLocation);
            if (!Backup_FOLDER.exists())
            {
                Backup_FOLDER.mkdirs();
            }
            File Original_Xhtmls = null;
            if (!PathOPS.equals(""))
            {
                Original_Xhtmls = new File(TempPath + "/" + PathOPS + "/");
            }
            else
            {
                Original_Xhtmls = new File(TempPath);
            }
            File list_xhtmls[] = Original_Xhtmls.listFiles();
            for (int j = 0; j < list_xhtmls.length; j++)
            {
                String XhtmlFileName = list_xhtmls[j].getName();
                if (XhtmlFileName.toLowerCase().indexOf(".xhtml") != -1)
                {
                    String FilePath = list_xhtmls[j].getAbsolutePath();
                    String TargetLOcation = targetLocation + "/" + XhtmlFileName;
                    FlipickIO.CopyFile(FilePath, TargetLOcation);
                }
            }
        }
        catch (Exception e)
        {
            FlipickStaticData.ThreadMessage = e.getMessage() + ":" + e.getCause();
        }
    }

    public void DecryptSvg(Context context, String FilePath) throws Exception
    {

        File svg_files = new File(FilePath + "/OPS/images/");
        if (svg_files.exists())
        {
            File list_svg[] = svg_files.listFiles();
            if (IsEpubEncrypted)
            {
                for (int count = 0; count < list_svg.length; count++)
                {
                    String SVG_FileName = list_svg[count].getName();
                    String SVG_FilePath = list_svg[count].getAbsolutePath();
                    if (SVG_FileName.toLowerCase().indexOf(".svg") != -1)
                    {
                        EncryptHTML(SVG_FilePath);
                    }
                }
            }
        }
    }

    private void EncryptHTML(String Filepath) throws Exception
    {
        String EncryptedText = FlipickIO.ReadHTMLFileText(Filepath);
        String DecryptedText = oRowTransposition.Decrypt(EncryptedText);
        FlipickIO.WriteHTMLFileText(Filepath, DecryptedText);
    }

    public void CopyLocalZipToAppLocation(File file, String jobID, Context context) throws Throwable
    {
        try
        {
            JobId = jobID;
            final int MAX_VALUE = 9999;
            Random rand = new Random();
            int randValue = rand.nextInt(MAX_VALUE);
            JobTemp = randValue + "";
            TempPath = FlipickConfig.ContentRootPath + "/" + JobId + "_" + JobTemp;
            File EPUBDirectory = new File(TempPath);

            if (EPUBDirectory.exists())
            {
                FlipickIO.DeleteFolder(EPUBDirectory);
            }

            File EPUBTempDirectory = new File(TempPath);
            if (!EPUBTempDirectory.exists()) EPUBTempDirectory.mkdirs();

            FlipickIO.CopyFile(file.getAbsolutePath(), new File(TempPath + "/" + file.getName()).getAbsolutePath());
            new File(TempPath + "/" + file.getName()).renameTo(new File(TempPath + "/" + JobId + ".zip"));
            FlipickIO.extractFolder(TempPath + "/" + JobId + ".zip", TempPath);

            GetSideBookJobCoverPage(TempPath, context);
        }
        catch (Throwable e)
        {
            throw e;
        }
    }

    public void RenameJobFolder(String TempPath) throws Exception
    {
        String path = FlipickConfig.ContentRootPath + "/" + JobId;
        File EPUBDirectory = new File(path);
        EPUBDirectory = new File(TempPath);
        final File F = new File(TempPath);
        File newDrectory = new File(F.getParent(), JobId);
        F.renameTo(newDrectory);
    }

    public void DeleteZipFile() throws Exception
    {
        String JobZipPath = FlipickConfig.ContentRootPath + "/" + JobId + "/" + JobId + ".zip";
        File JobZipFile = new File(JobZipPath);
        if (JobZipFile.exists())
        {
            JobZipFile.delete();
        }
    }

    public void UpdateJobInfoXmlWithOrientation(Document oXMLJobInfoDoc, String TempPath, String JobId) throws Exception
    {
        String JobFolderPath = TempPath;

        NodeList E_JobInfos = oXMLJobInfoDoc.getElementsByTagName("JobInfo");
        Element E_JobInfo = (Element) E_JobInfos.item(0);

        Element E_Height = oXMLJobInfoDoc.createElement("Height");
        E_Height.appendChild(oXMLJobInfoDoc.createTextNode(Float.toString(JobHeight)));
        E_JobInfo.appendChild(E_Height);

        Element E_Width = oXMLJobInfoDoc.createElement("Width");
        E_Width.appendChild(oXMLJobInfoDoc.createTextNode(Float.toString(JobWidth)));
        E_JobInfo.appendChild(E_Width);

        Element E_Orientation = oXMLJobInfoDoc.createElement("Orientation");
        if (JobWidth > JobHeight) E_Orientation.appendChild(oXMLJobInfoDoc.createTextNode("L"));
        else E_Orientation.appendChild(oXMLJobInfoDoc.createTextNode("P"));

        E_JobInfo.appendChild(E_Orientation);

        FlipickIO.SaveXMLFileFromDocument(JobFolderPath + "/" + JobId + ".xml", oXMLJobInfoDoc);
    }

    public Document CreateJobInfoXML(String TempPath, Context context, String JobId) throws Exception
    {
        String JobFolderPath = TempPath;

        Document oXMLContainerDoc = FlipickIO.GetDocumentFromFile(JobFolderPath + "/META-INF/container.xml");

        Element E_RootFile = (Element) oXMLContainerDoc.getElementsByTagName("rootfile").item(0);
        String OEBPSPath = E_RootFile.getAttribute("full-path");

        String ContentPath = "";
        FlipickStaticData.OEBPSPath = "";
        PathOPS = "";
        if (OEBPSPath.contains("/"))
        {
            PathOPS = OEBPSPath.split("/")[0];
            FlipickStaticData.OEBPSPath = PathOPS;
        }

        ContentPath = OEBPSPath;
        oXMLContainerDoc = null;

        Document oPackageDoc = FlipickIO.GetDocumentFromFile(JobFolderPath + "/" + ContentPath);// +

        NodeList E_MetaList = oPackageDoc.getElementsByTagName("meta");

        String Job_Title = "";
        NodeList E_DC_Title = oPackageDoc.getElementsByTagName("dc:title");
        if (E_DC_Title.getLength() > 0 && oPackageDoc.getElementsByTagName("dc:title").item(0).getFirstChild() != null)
        {

            Job_Title = ((Node) E_DC_Title.item(0)).getFirstChild().getNodeValue();
        }
        NodeList E_DC_Language = oPackageDoc.getElementsByTagName("dc:language");
        if (E_DC_Language.getLength() > 0 && oPackageDoc.getElementsByTagName("dc:language").item(0).getFirstChild() != null)
        {

            Language = ((Node) E_DC_Language.item(0)).getFirstChild().getNodeValue();
            JobInfo info = new JobInfo(context);
            info.updateLanguageInJobDB(Language, JobId);
        }

        BookType = "REFLOWABLE";

        for (int i = 0; i < E_MetaList.getLength(); i++)
        {
            Element E_Meta = (Element) E_MetaList.item(i);
            if (E_Meta.hasAttribute("property"))
            {
                if (E_Meta.getAttribute("property").equals("rendition:layout"))
                {
                    if (E_Meta.getTextContent().equals("pre-paginated"))
                    {
                        BookType = "EPUB3";
                        break;
                    }
                    else if (E_Meta.getTextContent().equals("reflowable"))
                    {
                        BookType = "REFLOWABLE";
                        break;
                    }
                    else if (E_Meta.getTextContent().equals(""))
                    {
                        BookType = "VERTICALSCROLL";
                    }
                    else
                    {
                        BookType = "REFLOWABLE";
                        break;
                    }
                }
                else continue;
            }

        }

        // spine
        ArrayList<String> otempItemref = new ArrayList<String>();
        // itemref
        NodeList E_Itemref = oPackageDoc.getElementsByTagName("itemref");

        for (int j = 0; j < E_Itemref.getLength(); j++)
        {
            Element E_Item = (Element) E_Itemref.item(j);
            E_Item.getAttribute("idref");
            otempItemref.add(E_Item.getAttribute("idref"));
        }

        // update BookType in JobInfo table

        JobInfo info = new JobInfo(context);
        info.updateBookTypeInJobDB(BookType, JobId);

        NodeList E_Items = oPackageDoc.getElementsByTagName("item");
        Document oXMLJobInfoDoc = FlipickIO.GetXMLDocument();
        Element E_JobInfo = oXMLJobInfoDoc.createElement("JobInfo");

        Element E_BookType = oXMLJobInfoDoc.createElement("BookType");
        E_BookType.appendChild(oXMLJobInfoDoc.createTextNode(BookType));
        E_JobInfo.appendChild(E_BookType);

        Element E_BookTitle = oXMLJobInfoDoc.createElement("Title");
        if (!JobTitle.equals(""))
        {
            E_BookTitle.appendChild(oXMLJobInfoDoc.createTextNode(JobTitle));
        }
        else
        {
            E_BookTitle.appendChild(oXMLJobInfoDoc.createTextNode(Job_Title));
        }
        E_JobInfo.appendChild(E_BookTitle);

        Element E_Date = oXMLJobInfoDoc.createElement("Date");
        E_Date.appendChild(oXMLJobInfoDoc.createTextNode(ProductUpdated));
        E_JobInfo.appendChild(E_Date);

        Element E_UserOrientation = oXMLJobInfoDoc.createElement("UserOrientation");
        E_UserOrientation.appendChild(oXMLJobInfoDoc.createTextNode("0"));
        E_JobInfo.appendChild(E_UserOrientation);

        Element E_LastPagelink = oXMLJobInfoDoc.createElement("LastPagelink");
        E_LastPagelink.appendChild(oXMLJobInfoDoc.createTextNode(""));
        E_JobInfo.appendChild(E_LastPagelink);

        Element E_LastFontSize = oXMLJobInfoDoc.createElement("LastFontSize");
        E_LastFontSize.appendChild(oXMLJobInfoDoc.createTextNode(""));
        E_JobInfo.appendChild(E_LastFontSize);

        Element E_LastPageFontPageName = oXMLJobInfoDoc.createElement("LastPageFontPageName");
        E_LastPageFontPageName.appendChild(oXMLJobInfoDoc.createTextNode(""));
        E_JobInfo.appendChild(E_LastPageFontPageName);

        Element E_LastPageFontColumnNo = oXMLJobInfoDoc.createElement("LastPageFontColumnNo");
        E_LastPageFontColumnNo.appendChild(oXMLJobInfoDoc.createTextNode(""));
        E_JobInfo.appendChild(E_LastPageFontColumnNo);

        Element E_LastBackgroundColor = oXMLJobInfoDoc.createElement("LastBackgroundColor");
        E_LastBackgroundColor.appendChild(oXMLJobInfoDoc.createTextNode(""));
        E_JobInfo.appendChild(E_LastBackgroundColor);
        String Path = "";

        HashMap<String, String> itemRef = new LinkedHashMap<String, String>();

        for (String item : otempItemref)
        {

            for (int j = 0; j < E_Items.getLength(); j++)
            {
                Element E_Item = (Element) E_Items.item(j);
                if (E_Item.getAttribute("media-type") != null)
                {
                    if (E_Item.getAttribute("media-type").equals("application/xhtml+xml"))
                    {
                        if (E_Item.getAttribute("id").equals("cover")) continue;
                        if (E_Item.getAttribute("id").equals("toc")) continue;
                        if (E_Item.getAttribute("id").equals("nav")) continue;

                        if (item.equals(E_Item.getAttribute("id")))
                        {
                            itemRef.put(item, URLDecoder.decode(E_Item.getAttribute("href"), "UTF-8"));
                            break;
                        }

                    }
                    else continue;
                }
                else continue;
            }

        }
        Element E_Pages = oXMLJobInfoDoc.createElement("Pages");
        for (String ele : itemRef.keySet())
        {
            if (URLDecoder.decode(itemRef.get(ele), "UTF-8").contains("/"))
            {
                Path = URLDecoder.decode(itemRef.get(ele), "UTF-8").substring(0, URLDecoder.decode(itemRef.get(ele), "UTF-8").lastIndexOf("/"));
            }
            Element E_Page = oXMLJobInfoDoc.createElement("Page");
            E_Page.setAttribute("LinkedIds", "");
            if (URLDecoder.decode(itemRef.get(ele), "UTF-8").contains("/"))
            {
                E_Page.appendChild(oXMLJobInfoDoc.createTextNode(URLDecoder.decode(itemRef.get(ele), "UTF-8").substring(URLDecoder.decode(itemRef.get(ele), "UTF-8").lastIndexOf("/") + 1)));
            }
            else
            {
                E_Page.appendChild(oXMLJobInfoDoc.createTextNode(URLDecoder.decode(itemRef.get(ele), "UTF-8")));
            }
            E_Pages.appendChild(E_Page);
        }
        Element E_PathOPS = oXMLJobInfoDoc.createElement("PathOPS");
        if (!PathOPS.equals(""))
        {
            if (!Path.equals(""))
            {
                PathOPS = PathOPS + "/" + Path;
            }
            else
            {
                PathOPS = PathOPS;
            }
        }
        else
        {
            if (!Path.equals(""))
            {
                PathOPS = Path;
            }
        }
        E_PathOPS.appendChild(oXMLJobInfoDoc.createTextNode(PathOPS));
        E_JobInfo.appendChild(E_PathOPS);
        E_JobInfo.appendChild(E_Pages);
        oXMLJobInfoDoc.appendChild(E_JobInfo);
        return oXMLJobInfoDoc;
    }

    public void GetSideBookJobCoverPage(String TempPath, Context context) throws Exception
    {
        String JobFolderPath = TempPath;

        Document oXMLContainerDoc = FlipickIO.GetDocumentFromFile(JobFolderPath + "/META-INF/container.xml");

        Element E_RootFile = (Element) oXMLContainerDoc.getElementsByTagName("rootfile").item(0);
        String OEBPSPath = E_RootFile.getAttribute("full-path");

        String ContentPath = "";
        if (OEBPSPath.contains("/"))
        {
            PathOPS = OEBPSPath.split("/")[0];

        }
        FlipickStaticData.OEBPSPath = PathOPS;
        ContentPath = OEBPSPath;

        Document oPackageDoc = FlipickIO.GetDocumentFromFile(JobFolderPath + "/" + ContentPath);

        NodeList E_DC_Title = oPackageDoc.getElementsByTagName("dc:title");
        if (E_DC_Title.getLength() > 0 && oPackageDoc.getElementsByTagName("dc:title").item(0).getFirstChild() != null)
        {
            JobTitle = ((Node) E_DC_Title.item(0)).getFirstChild().getNodeValue();
        }

        NodeList E_Items = oPackageDoc.getElementsByTagName("item");

        for (int j = 0; j < E_Items.getLength(); j++)
        {
            Element E_Item = (Element) E_Items.item(j);
            if (E_Item.hasAttribute("id"))
            {
                if (E_Item.getAttribute("properties").equals("cover-image") && E_Item.getAttribute("media-type").startsWith("image/"))
                {
                    SideLoadCoverImage = E_Item.getAttribute("href");
                }
            }
        }
    }

    public void RemoveJSScriptFile(Context context, String TempPath) throws Exception
    {

        String JobFolderPath = TempPath;
        File file = null;
        if (!PathOPS.equals(""))
        {
            file = new File(JobFolderPath + "/" + PathOPS);
        }
        else
        {
            file = new File(JobFolderPath);
        }
        File list[] = file.listFiles();

        for (int i = 0; i < list.length; i++)
        {
            int scale_percentage = ((int) (((float) i / (float) list.length) * 100)) / 2 + 50;
            FlipickStaticData.progressMessage = FlipickStaticData.MsgOpeningBook + scale_percentage + "%";
            String FileName = URLDecoder.decode(list[i].getName(), "UTF-8");
            String FilePath = list[i].getAbsolutePath();

            if (IsEpubEncrypted)
            {
                if (FileName.toLowerCase().indexOf(".xhtml") != -1)
                {
                    EncryptHTML(FilePath);
                }

            }

            if (FileName.toLowerCase().indexOf(".xhtml") == -1) continue;
            // if (FileName.toLowerCase().equals("nav.xhtml")) continue;
            // if (FileName.toLowerCase().equals("toc.xhtml")) continue;
            String XMLFilePath = "";
            if (!PathOPS.equals(""))
            {
                XMLFilePath = JobFolderPath + "/" + PathOPS + "/" + FileName;
            }
            else
            {
                XMLFilePath = JobFolderPath + "/" + FileName;
            }
            Document oXMLDoc = FlipickIO.GetDocumentFromFile(XMLFilePath);

            Boolean IsViewPortMetaFound = false;
            NodeList NL_Metas = oXMLDoc.getElementsByTagName("meta");
            for (int n = 0; n < NL_Metas.getLength(); n++)
            {
                Element EL_Meta = (Element) NL_Metas.item(n);
                if (EL_Meta.getAttribute("name").equals("viewport"))
                {
                    EL_Meta.setAttribute("content", "user-scalable=no");
                    EL_Meta.appendChild(oXMLDoc.createTextNode(" "));
                    IsViewPortMetaFound = true;
                }
                else
                {
                    EL_Meta.appendChild(oXMLDoc.createTextNode(" "));
                }
            }
            if (!IsViewPortMetaFound)
            {
                NodeList NL_Heads = oXMLDoc.getElementsByTagName("head");
                Element EL_Meta = oXMLDoc.createElement("meta");
                EL_Meta.setAttribute("name", "viewport");

                EL_Meta.setAttribute("content", "user-scalable=no");
                NL_Heads.item(0).appendChild(EL_Meta);
            }

            // get LinkedIds from internal <a href
            NodeList NL_HyperLinks = oXMLDoc.getElementsByTagName("a");
            for (int n = 0; n < NL_HyperLinks.getLength(); n++)
            {
                Element element = (Element) NL_HyperLinks.item(n);
                if (element.hasAttribute("href"))
                {
                    String Value = element.getAttribute("href");
                    if (Value.startsWith("#"))
                    {
                        originalPages.add(FileName + Value);
                    }
                    else
                    {
                        originalPages.add(Value);
                    }
                }
            }
            // end

            NodeList NL_Images = oXMLDoc.getElementsByTagName("img");
            for (int n = 0; n < NL_Images.getLength(); n++)
            {
                Element element = (Element) NL_Images.item(n);
                if (element.hasAttribute("onclick")) continue;

                element.setAttribute("onclick", "DisplayImageInPopup(this.src);");
            }

            NodeList NLHead = oXMLDoc.getElementsByTagName("head");
            Element E_Head = (Element) NLHead.item(0);
            // /
            Date oDate = Calendar.getInstance().getTime();
            NodeList NL_CssLink = E_Head.getElementsByTagName("link");
            for (int n = 0; n < NL_CssLink.getLength(); n++)
            {
                Element element = (Element) NL_CssLink.item(n);
                if (element.hasAttribute("type") && element.getAttribute("type").equals("text/css"))
                {
                    String Value = element.getAttribute("href");
                    Value = Value + "?Random=L" + oDate.toString().replace(":", "").replace(" ", "") + n + "Page" + i;
                    element.setAttribute("href", Value);
                }
            }

            Element E_Script = oXMLDoc.createElement("script");
            E_Script.setAttribute("type", "text/javascript");
            E_Script.setAttribute("src", "file:///" + "android_asset" + "/PageScript/PageScript.js");
            E_Head.appendChild(E_Script);
            E_Script.getAttribute("src");

            // Highlight
            Element E_Script_highlight = oXMLDoc.createElement("script");
            E_Script_highlight.setAttribute("type", "text/javascript");
            E_Script_highlight.setAttribute("src", "file:///" + "android_asset" + "/highlight/highlight.js");
            E_Head.appendChild(E_Script_highlight);
            E_Script_highlight.getAttribute("src");

            Element E_Link_highlight_css = oXMLDoc.createElement("link");
            E_Link_highlight_css.setAttribute("type", "text/css");
            E_Link_highlight_css.setAttribute("rel", "stylesheet");
            E_Link_highlight_css.setAttribute("href", "file:///" + "android_asset" + "/highlight/highlight.css");
            E_Head.appendChild(E_Link_highlight_css);
            E_Link_highlight_css.getAttribute("src");

            // End Highlight

            if (BookType.equals("VERTICALSCROLL"))
            {
                // for fitting option
                if (FileName.toLowerCase().equals("toc.xhtml")) continue;
                Element E_Fitting_css = oXMLDoc.createElement("link");
                E_Fitting_css.setAttribute("type", "text/css");
                E_Fitting_css.setAttribute("rel", "stylesheet");
                String path = "";
                if (!PathOPS.equals(""))
                {
                    path = "file:///" + FlipickConfig.ContentRootPath + "/" + JobId + "/" + PathOPS + "/fitVerticalcontent.css";
                }
                else
                {
                    path = "file:///" + FlipickConfig.ContentRootPath + "/" + JobId + "/fitVerticalcontent.css";
                }
                path = path.replace("file:////", "file:///");
                E_Fitting_css.setAttribute("href", path);
                E_Head.appendChild(E_Fitting_css);
            }

            FlipickIO.SaveXMLFileFromDocument(URLDecoder.decode(XMLFilePath, "UTF-8"), oXMLDoc);

            if (BookType.equals("VERTICALSCROLL"))
            {

                if (FileName.toLowerCase().equals("toc.xhtml")) continue;
                String HTML = FlipickIO.ReadHTMLFileText(XMLFilePath);
                String divTag = "<div>";// addDivTag(HTML);
                int indexBody = HTML.indexOf("<body");

                String Startbody = HTML.substring(HTML.indexOf("<body"));
                String FindBody = HTML.substring(indexBody, Startbody.indexOf(">") + indexBody + 1);
                HTML = HTML.replace(FindBody, FindBody + divTag);
                HTML = HTML.replace("</body>", "</div></body>");
                FlipickIO.WriteHTMLFileText(XMLFilePath, HTML);

            }

        }

        if (BookType.equals("VERTICALSCROLL"))
        {
            if (!PathOPS.equals(""))
            {
                savedefaultCssForVerticalBook(JobFolderPath + "/" + PathOPS + "/", context);
            }
            else
            {
                savedefaultCssForVerticalBook(JobFolderPath + "/", context);
            }
        }
    }

    public void ApplyScalingToXHTMLPages(Context context, String TempPath, String JobId) throws Exception
    {
        String JobFolderPath = TempPath;
        File file = null;
        if (!PathOPS.equals(""))
        {
            file = new File(JobFolderPath + "/" + PathOPS + "/cover.xhtml");
        }
        else
        {
            file = new File(JobFolderPath + "/cover.xhtml");
        }
        if (file.exists())
        {
            if (!PathOPS.equals(""))
            {
                FlipickIO.CopyFile(JobFolderPath + "/" + PathOPS + "/cover.xhtml", JobFolderPath + "/" + PathOPS + "/page001.xhtml");
            }
            else
            {
                FlipickIO.CopyFile(JobFolderPath + "/cover.xhtml", JobFolderPath + "/page001.xhtml");
            }
        }
        if (!PathOPS.equals(""))
        {
            file = new File(JobFolderPath + "/" + PathOPS);
        }
        else
        {
            file = new File(JobFolderPath);
        }
        File list[] = file.listFiles();
        String JobScaleCSS = "";

        String JobScaleAssigned = "N";

        for (int i = 0; i < list.length; i++)
        {
            int scale_percentage = ((int) (((float) i / (float) list.length) * 100)) / 2 + 50;
            FlipickStaticData.progressMessage = FlipickStaticData.MsgOpeningBook + scale_percentage + "%";
            String FileName = list[i].getName();
            String FilePath = list[i].getAbsolutePath();

            if (IsEpubEncrypted)
            {
                if (FileName.toLowerCase().indexOf(".xhtml") != -1)
                {
                    EncryptHTML(FilePath);
                }

            }
            String XMLFilePath = "";
            if (!PathOPS.equals(""))
            {
                XMLFilePath = JobFolderPath + "/" + PathOPS + "/" + FileName;
            }
            else
            {
                XMLFilePath = JobFolderPath + "/" + FileName;
            }
            if (FileName.toLowerCase().indexOf(".xhtml") == -1) continue;
            if (JobScaleAssigned.equals("N"))
            {
                Document oXMLDoc = FlipickIO.GetDocumentFromFile(XMLFilePath);
                NodeList NL_Metas = oXMLDoc.getElementsByTagName("meta");
                for (int n = 0; n < NL_Metas.getLength(); n++)
                {
                    Element EL_Meta = (Element) NL_Metas.item(n);
                    if (EL_Meta.getAttribute("name").equals("viewport"))
                    {
                        String Content = EL_Meta.getAttribute("content");

                        if (Content.contains("px") || Content.contains("PX"))
                        {
                            Content = Content.replace("px", "");
                        }
                        String[] AryWH = Content.split(",");
                        JobWidth = Float.parseFloat(AryWH[0].split("=")[1]);
                        JobHeight = Float.parseFloat(AryWH[1].split("=")[1]);
                        if (!PathOPS.equals(""))
                        {
                            savefitdefaultCss(JobFolderPath + "/" + PathOPS + "/", context);
                        }
                        else
                        {
                            savefitdefaultCss(JobFolderPath + "/", context);
                        }
                        JobScaleAssigned = "Y";
                    }
                }

                NL_Metas = null;
                oXMLDoc = null;
            }

            try
            {
                file = null;
                UpdateScaleAndViewPort(JobScaleCSS, XMLFilePath, JobId);
            }
            catch (Exception e)
            {
                throw e;
            }
        }
    }

    /*
     * public void DonwloadConfirmation() throws Throwable { try { String
     * RequestXML = "<?xml version='1.0' encoding='UTF-8'?>"; RequestXML =
     * RequestXML +
     * "<ns1:AddDownloadHistory><UserEmail xsi:type='xsd:string'><![CDATA[" +
     * oUserinfo.UserName.trim() + "]]></UserEmail>"; RequestXML = RequestXML +
     * "<Password xsi:type='xsd:string'><![CDATA[" + oUserinfo.Password +
     * "]]></Password>"; RequestXML = RequestXML +
     * "<DeviceId xsi:type='xsd:string'><![CDATA[" + FlipickStaticData.DeviceId
     * + "]]></DeviceId>"; RequestXML = RequestXML +
     * "<ProductType xsi:type='xsd:string'><![CDATA[ALL]]></ProductType>";
     * RequestXML = RequestXML + "<WebsiteId xsi:type='xsd:string'><![CDATA[" +
     * oUserinfo.WebsiteId + "]]></WebsiteId>"; RequestXML = RequestXML +
     * "<OS xsi:type='xsd:string'><![CDATA[Android]]></OS>"; RequestXML =
     * RequestXML + "<OSVersion xsi:type='xsd:string'><![CDATA[" +
     * FlipickStaticData.OSVersion + "]]></OSVersion>"; RequestXML = RequestXML
     * + "<DeviceType xsi:type='xsd:string'><![CDATA[Mobile]]></DeviceType>";
     * RequestXML = RequestXML + "<ProductId xsi:type='xsd:string'><![CDATA[" +
     * JobId + "]]></ProductId></ns1:AddDownloadHistory>";
     *
     * oMagentoServiceInterface.ServiceDonwloadConfirmation("AddDownloadHistory",
     * RequestXML); } catch (Throwable e) { throw (e); } }
     */

    public void DeleteCacheJobThumbnails() throws Throwable
    {
        try
        {
            File oDir = new File(FlipickConfig.ContentRootPath + "/LazyList");
            Boolean IsDirDeleted = FlipickIO.DeleteDirectoryRecursive(oDir);
        }
        catch (Throwable e)
        {
            throw e;
        }
    }

    /*public ArrayList<JobInfo> MagentoGetJobThumbnail(User oUser) throws Throwable
    {
        ArrayList<JobInfo> jobArray = new ArrayList<JobInfo>();
        MagentoServiceInterface oMagentoServiceInterface = new MagentoServiceInterface();
        try
        {
            String RequestXML = "<?xml version='1.0' encoding='UTF-8'?>";
            RequestXML = RequestXML + "<publishOrderList><UserEmail xsi:type='xsd:string' ><![CDATA[" + oUser.UserName.trim() + "]]></UserEmail> ";
            RequestXML = RequestXML + "<Password  xsi:type='xsd:string'><![CDATA[" + oUser.Password + "]]></Password>";
            RequestXML = RequestXML + "<DeviceId  xsi:type='xsd:string'><![CDATA[" + FlipickStaticData.DeviceId + "]]></DeviceId>";
            RequestXML = RequestXML + "<ProductType xsi:type='xsd:string'><![CDATA[ALL]]></ProductType>";
            RequestXML = RequestXML + "<WebsiteId  xsi:type='xsd:string'><![CDATA[" + oUser.WebsiteId + "]]></WebsiteId>";
            RequestXML = RequestXML + "<OS  xsi:type='xsd:string'><![CDATA[Android]]></OS>";
            RequestXML = RequestXML + "<OSVersion xsi:type='xsd:string'><![CDATA[" + FlipickStaticData.OSVersion + "]]></OSVersion>";
            RequestXML = RequestXML + "<DeviceType xsi:type='xsd:string'><![CDATA[Mobile]]></DeviceType>";
            RequestXML = RequestXML + "<DeRegisterDevice xsi:type='xsd:string'><![CDATA[No]]></DeRegisterDevice></publishOrderList>";

            String ResponseXml = oMagentoServiceInterface.GetMagentoOrderList("publishOrderList", RequestXML);

            XMLParser parser = new XMLParser();
            Document MagentoxmlJobThumbsResponse = parser.getDomElement(ResponseXml);

            Element EL_Orders = (Element) MagentoxmlJobThumbsResponse.getElementsByTagName("orders").item(0);

            String cStatus = EL_Orders.getElementsByTagName("ErrorCode").item(0).getFirstChild().getNodeValue();

            cStatus = cStatus.replace("<![CDATA[", "");
            String Status = cStatus.replace("]]>", "");
            String cErrMsg = EL_Orders.getElementsByTagName("ErrorMessage").item(0).getFirstChild().getNodeValue();
            cErrMsg = cErrMsg.replace("<![CDATA[", "");
            String ErrMsg = cErrMsg.replace("]]>", "");
            if (Status.equals("100") != true)
            {
                if (ErrMsg.contains("don't have orders"))
                {
                    Element EL_Result = (Element) EL_Orders.getElementsByTagName("result").item(0);
                    Element EL_CustomerInfo = (Element) EL_Result.getElementsByTagName("CustomerInfo").item(0);
                    if (EL_CustomerInfo.getElementsByTagName("CustomerId").item(0) != null)
                    {
                        oUser.UserId = EL_CustomerInfo.getElementsByTagName("CustomerId").item(0).getFirstChild().getNodeValue(); // parser.getValue(elem,"ProductId");
                        oUser.UserId = oUser.UserId.replace("<![CDATA[", "");
                        oUser.UserId = oUser.UserId.replace("]]>", "");
                        return null;
                    }
                }
                else
                {
                    Exception myException = new Exception(ErrMsg);
                    throw myException;
                }
            }
            else
            {
                Element EL_Result = (Element) EL_Orders.getElementsByTagName("result").item(0);
                Element EL_CustomerInfo = (Element) EL_Result.getElementsByTagName("CustomerInfo").item(0);
                if (EL_CustomerInfo.getElementsByTagName("CustomerId").item(0).getFirstChild() != null)
                {
                    oUser.UserId = EL_CustomerInfo.getElementsByTagName("CustomerId").item(0).getFirstChild().getNodeValue(); // parser.getValue(elem,"ProductId");
                    oUser.UserId = oUser.UserId.replace("<![CDATA[", "");
                    oUser.UserId = oUser.UserId.replace("]]>", "");

                }

                NodeList MNDList = EL_Result.getChildNodes();

                for (int i = 0; i < MNDList.getLength(); i++)
                {
                    Element elem = (Element) MNDList.item(i);
                    if (!elem.getNodeName().equals("items")) continue;

                    String ProductId = elem.getElementsByTagName("ProductId").item(0).getFirstChild().getNodeValue(); // parser.getValue(elem,"ProductId");
                    ProductId = ProductId.replace("<![CDATA[", "");
                    ProductId = ProductId.replace("]]>", "");

                    JobInfo moJobInfo = new JobInfo(null);
                    moJobInfo.JobId = ProductId;

                    if (elem.getElementsByTagName("ProductName").item(0).getFirstChild() != null)
                    {
                        moJobInfo.JobTitle = elem.getElementsByTagName("ProductName").item(0).getFirstChild().getNodeValue();
                        moJobInfo.JobTitle = (moJobInfo.JobTitle).replace("<![CDATA[", "");
                        moJobInfo.JobTitle = (moJobInfo.JobTitle).replace("]]>", "");
                    }
                    if (elem.getElementsByTagName("ProductAuthor").item(0).getFirstChild() != null)
                    {
                        moJobInfo.JobAuthor = elem.getElementsByTagName("ProductAuthor").item(0).getFirstChild().getNodeValue();
                        moJobInfo.JobAuthor = (moJobInfo.JobAuthor).replace("<![CDATA[", "");
                        moJobInfo.JobAuthor = (moJobInfo.JobAuthor).replace("]]>", "");
                    }
                    if (elem.getElementsByTagName("ProductImageUrl").item(0).getFirstChild() != null)
                    {

                        moJobInfo.JobUrl = elem.getElementsByTagName("ProductImageUrl").item(0).getFirstChild().getNodeValue();
                        moJobInfo.JobUrl = (moJobInfo.JobUrl).replace("<![CDATA[", "");
                        moJobInfo.JobUrl = (moJobInfo.JobUrl).replace("]]>", "");
                    }

                    if (elem.getElementsByTagName("ProductUpdatedAt").item(0).getFirstChild() != null)
                    {
                        moJobInfo.ProductUpdated = elem.getElementsByTagName("ProductUpdatedAt").item(0).getFirstChild().getNodeValue();
                        // moJobInfo.ProductUpdated="<![CDATA[2016-06-23 05:24:29]]>";
                        moJobInfo.ProductUpdated = (moJobInfo.ProductUpdated).replace("<![CDATA[", "");
                        moJobInfo.ProductUpdated = (moJobInfo.ProductUpdated).replace("]]>", "");
                    }

                    if (elem.getElementsByTagName("FreeTrial").item(0) != null)
                    {
                        if (elem.getElementsByTagName("FreeTrial").item(0).getFirstChild() != null)
                        {
                            moJobInfo.Is_Free_Trial = elem.getElementsByTagName("FreeTrial").item(0).getFirstChild().getNodeValue();
                            moJobInfo.Is_Free_Trial = (moJobInfo.Is_Free_Trial).replace("<![CDATA[", "");
                            moJobInfo.Is_Free_Trial = (moJobInfo.Is_Free_Trial).replace("]]>", "");
                        }
                    }

                    // new added
                    if (elem.getElementsByTagName("Type").item(0) != null)
                    {
                        if (elem.getElementsByTagName("Type").item(0).getFirstChild() != null)
                        {
                            moJobInfo.ProductType = elem.getElementsByTagName("Type").item(0).getFirstChild().getNodeValue();
                            moJobInfo.ProductType = (moJobInfo.ProductType).replace("<![CDATA[", "");
                            moJobInfo.ProductType = (moJobInfo.ProductType).replace("]]>", "");
                        }
                    }

                    if (elem.getElementsByTagName("ExpiryDate").item(0) != null)
                    {
                        if (elem.getElementsByTagName("ExpiryDate").item(0).getFirstChild() != null)
                        {
                            moJobInfo.ProductExpiryDate = elem.getElementsByTagName("ExpiryDate").item(0).getFirstChild().getNodeValue();
                            moJobInfo.ProductExpiryDate = (moJobInfo.ProductExpiryDate).replace("<![CDATA[", "");
                            moJobInfo.ProductExpiryDate = (moJobInfo.ProductExpiryDate).replace("]]>", "");
                        }
                    }

                    if (elem.getElementsByTagName("ProductUrl").item(0) != null)
                    {
                        if (elem.getElementsByTagName("ProductUrl").item(0).getFirstChild() != null)
                        {
                            moJobInfo.ProductUrl = elem.getElementsByTagName("ProductUrl").item(0).getFirstChild().getNodeValue();
                            moJobInfo.ProductUrl = (moJobInfo.ProductUrl).replace("<![CDATA[", "");
                            moJobInfo.ProductUrl = (moJobInfo.ProductUrl).replace("]]>", "");

                        }
                    }

                    // end
                    // <![CDATA[2013-12-28 09:15:52]]>
                    Element XEResponse = (Element) elem.getElementsByTagName("ProductLinks").item(0);

                    if (XEResponse == null) continue;

                    NodeList eNDList = (XEResponse).getElementsByTagName("item");

                    for (int j = 0; j < eNDList.getLength(); j++)
                    {
                        Node iNode = eNDList.item(j);
                        Element mElement = (Element) iNode;
                        Node iNDListw = mElement.getElementsByTagName("key").item(0).getFirstChild();
                        if (iNDListw == null) continue;
                        String ciNDList = mElement.getElementsByTagName("key").item(0).getFirstChild().getNodeValue();
                        ciNDList = ciNDList.replace("<![CDATA[", "");
                        String iNDList = ciNDList.replace("]]>", "");
                        iNDList = iNDList.toString();

                        String cEPUBUrl = mElement.getElementsByTagName("value").item(0).getFirstChild().getNodeValue();
                        cEPUBUrl = cEPUBUrl.replace("<![CDATA[", "");
                        EPUBUrl = cEPUBUrl.replace("]]>", "");

                        if (EPUBUrl.toLowerCase().indexOf(".epub") != -1 || EPUBUrl.toLowerCase().indexOf(".fl1") != -1)
                        {
                            moJobInfo.EPUBUrl = EPUBUrl;
                            if (EPUBUrl.toLowerCase().indexOf(".epub") != -1) moJobInfo.BookType = "EPUB";
                            if (EPUBUrl.toLowerCase().indexOf(".fl1") != -1) moJobInfo.BookType = "FLASH";

                            Element EL_ApiExport = (Element) elem.getElementsByTagName("API_Export").item(0);
                            NodeList NL_ApiExport_items = EL_ApiExport.getElementsByTagName("items");
                            for (int count = 0; count < NL_ApiExport_items.getLength(); count++)
                            {
                                Element ApiExport_items = (Element) NL_ApiExport_items.item(count);
                                if (ApiExport_items.getElementsByTagName("label").item(0).getFirstChild() != null)
                                {
                                    String label_examhall = ApiExport_items.getElementsByTagName("label").item(0).getFirstChild().getNodeValue();

                                    if (label_examhall.toLowerCase().equals("exam hall"))
                                    {
                                        if (ApiExport_items.getElementsByTagName("value").item(0).getFirstChild() != null)
                                        {
                                            String value_examhall = ApiExport_items.getElementsByTagName("value").item(0).getFirstChild().getNodeValue();
                                            ApiExport_items.getElementsByTagName("value").item(0).getFirstChild().getNodeValue();
                                            moJobInfo.IsExamHall = value_examhall;
                                        }
                                    }
                                }
                            }
                            moJobInfo.IsSideLoaded = "No";
                            moJobInfo.HasBookDownloaded = false;
                            moJobInfo.HasProductUpdated = false;
                            moJobInfo.UpdateNewVersion = "N";
                            moJobInfo.JobThumbnailPath = moJobInfo.JobUrl;

                            jobArray.add(moJobInfo);
                            break;
                        }
                    }
                }
            }
        }
        catch (Throwable e)
        {
            throw e;
        }
        return jobArray;
    }*/

    public ArrayList<JobInfo> MagentoHardcodedGetJobThumbnail(User oUser) throws Throwable
    {
        ArrayList<JobInfo> jobArray = new ArrayList<JobInfo>();
        MagentoServiceInterface oMagentoServiceInterface = new MagentoServiceInterface();
        try
        {
            String RequestXML = "<?xml version='1.0' encoding='UTF-8'?>";
            RequestXML = RequestXML + "<publishOrderList><UserEmail xsi:type='xsd:string' ><![CDATA[" + oUser.UserName.trim() + "]]></UserEmail> ";
            RequestXML = RequestXML + "<Password  xsi:type='xsd:string'><![CDATA[" + oUser.Password + "]]></Password>";
            RequestXML = RequestXML + "<DeviceId  xsi:type='xsd:string'><![CDATA[" + FlipickStaticData.DeviceId + "]]></DeviceId>";
            RequestXML = RequestXML + "<ProductType xsi:type='xsd:string'><![CDATA[ALL]]></ProductType>";
            RequestXML = RequestXML + "<WebsiteId  xsi:type='xsd:string'><![CDATA[" + oUser.WebsiteId + "]]></WebsiteId>";
            RequestXML = RequestXML + "<OS  xsi:type='xsd:string'><![CDATA[Android]]></OS>";
            RequestXML = RequestXML + "<OSVersion xsi:type='xsd:string'><![CDATA[" + FlipickStaticData.OSVersion + "]]></OSVersion>";
            RequestXML = RequestXML + "<DeviceType xsi:type='xsd:string'><![CDATA[Mobile]]></DeviceType>";
            RequestXML = RequestXML + "<DeRegisterDevice xsi:type='xsd:string'><![CDATA[No]]></DeRegisterDevice></publishOrderList>";
            String newFileName = FlipickConfig.UserJobXmlPath + "/" + FlipickConfig.UserJobFileName;
            Document MagentoxmlJobThumbsResponse = FlipickIO.GetDocumentFromFile(newFileName);
            String ResponseXml = FlipickIO.ReadHTMLFileText(newFileName);
            // String ResponseXml =
            // oMagentoServiceInterface.GetMagentoOrderList("publishOrderList",
            // RequestXML);

            // XMLParser parser = new XMLParser();
            // Document MagentoxmlJobThumbsResponse =
            // parser.getDomElement(ResponseXml);

            Element EL_Orders = (Element) MagentoxmlJobThumbsResponse.getElementsByTagName("orders").item(0);

            String cStatus = EL_Orders.getElementsByTagName("ErrorCode").item(0).getFirstChild().getNodeValue();

            cStatus = cStatus.replace("<![CDATA[", "");
            String Status = cStatus.replace("]]>", "");
            String cErrMsg = EL_Orders.getElementsByTagName("ErrorMessage").item(0).getFirstChild().getNodeValue();
            cErrMsg = cErrMsg.replace("<![CDATA[", "");
            String ErrMsg = cErrMsg.replace("]]>", "");
            if (Status.equals("100") != true)
            {
                if (ErrMsg.contains("don't have orders"))
                {
                    Element EL_Result = (Element) EL_Orders.getElementsByTagName("result").item(0);
                    Element EL_CustomerInfo = (Element) EL_Result.getElementsByTagName("CustomerInfo").item(0);
                    if (EL_CustomerInfo.getElementsByTagName("CustomerId").item(0) != null)
                    {
                        oUser.UserId = EL_CustomerInfo.getElementsByTagName("CustomerId").item(0).getFirstChild().getNodeValue(); // parser.getValue(elem,"ProductId");
                        oUser.UserId = oUser.UserId.replace("<![CDATA[", "");
                        oUser.UserId = oUser.UserId.replace("]]>", "");
                        return null;
                    }
                }
                else
                {
                    Exception myException = new Exception(ErrMsg);
                    throw myException;
                }
            }
            else
            {
                Element EL_Result = (Element) EL_Orders.getElementsByTagName("result").item(0);
                Element EL_CustomerInfo = (Element) EL_Result.getElementsByTagName("CustomerInfo").item(0);
                if (EL_CustomerInfo.getElementsByTagName("CustomerId").item(0).getFirstChild() != null)
                {
                    oUser.UserId = EL_CustomerInfo.getElementsByTagName("CustomerId").item(0).getFirstChild().getNodeValue(); // parser.getValue(elem,"ProductId");
                    oUser.UserId = oUser.UserId.replace("<![CDATA[", "");
                    oUser.UserId = oUser.UserId.replace("]]>", "");

                }

                NodeList MNDList = EL_Result.getChildNodes();

                for (int i = 0; i < MNDList.getLength(); i++) {
                    if (MNDList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        Element elem = (Element) MNDList.item(i);
                        if (!elem.getNodeName().equals("items")) continue;

                        String ProductId = elem.getElementsByTagName("ProductId").item(0).getFirstChild().getNodeValue(); // parser.getValue(elem,"ProductId");
                        ProductId = ProductId.replace("<![CDATA[", "");
                        ProductId = ProductId.replace("]]>", "");

                        JobInfo moJobInfo = new JobInfo(null);
                        moJobInfo.JobId = ProductId;

                        if (elem.getElementsByTagName("ProductName").item(0).getFirstChild() != null) {
                            moJobInfo.JobTitle = elem.getElementsByTagName("ProductName").item(0).getFirstChild().getNodeValue();
                            moJobInfo.JobTitle = (moJobInfo.JobTitle).replace("<![CDATA[", "");
                            moJobInfo.JobTitle = (moJobInfo.JobTitle).replace("]]>", "");
                        }

                        // Added changes for category filtering 17 Feb 2021
					/*if (elem.getElementsByTagName("CategoryId").item(0).getFirstChild() != null)
					{
						moJobInfo.CategoryId = elem.getElementsByTagName("CategoryId").item(0).getFirstChild().getNodeValue();
						moJobInfo.CategoryId = (moJobInfo.CategoryId).replace("<![CDATA[", "");
						moJobInfo.CategoryId = (moJobInfo.CategoryId).replace("]]>", "");
					}
					if (elem.getElementsByTagName("CategoryName").item(0).getFirstChild() != null)
					{
						moJobInfo.CategoryName = elem.getElementsByTagName("CategoryName").item(0).getFirstChild().getNodeValue();
						moJobInfo.CategoryName = (moJobInfo.CategoryName).replace("<![CDATA[", "");
						moJobInfo.CategoryName = (moJobInfo.CategoryName).replace("]]>", "");
					}*/

                        if (elem.getElementsByTagName("ProductAuthor").item(0).getFirstChild() != null) {
                            moJobInfo.JobAuthor = elem.getElementsByTagName("ProductAuthor").item(0).getFirstChild().getNodeValue();
                            moJobInfo.JobAuthor = (moJobInfo.JobAuthor).replace("<![CDATA[", "");
                            moJobInfo.JobAuthor = (moJobInfo.JobAuthor).replace("]]>", "");
                        }
                        if (elem.getElementsByTagName("ProductImageUrl").item(0).getFirstChild() != null) {

                            moJobInfo.JobUrl = elem.getElementsByTagName("ProductImageUrl").item(0).getFirstChild().getNodeValue();
                            moJobInfo.JobUrl = (moJobInfo.JobUrl).replace("<![CDATA[", "");
                            moJobInfo.JobUrl = (moJobInfo.JobUrl).replace("]]>", "");
                        }

                        if (elem.getElementsByTagName("ProductUpdatedAt").item(0).getFirstChild() != null) {
                            moJobInfo.ProductUpdated = elem.getElementsByTagName("ProductUpdatedAt").item(0).getFirstChild().getNodeValue();
                            // moJobInfo.ProductUpdated="<![CDATA[2016-06-23 05:24:29]]>";
                            moJobInfo.ProductUpdated = (moJobInfo.ProductUpdated).replace("<![CDATA[", "");
                            moJobInfo.ProductUpdated = (moJobInfo.ProductUpdated).replace("]]>", "");
                        }

                        if (elem.getElementsByTagName("FreeTrial").item(0) != null) {
                            if (elem.getElementsByTagName("FreeTrial").item(0).getFirstChild() != null) {
                                moJobInfo.Is_Free_Trial = elem.getElementsByTagName("FreeTrial").item(0).getFirstChild().getNodeValue();
                                moJobInfo.Is_Free_Trial = (moJobInfo.Is_Free_Trial).replace("<![CDATA[", "");
                                moJobInfo.Is_Free_Trial = (moJobInfo.Is_Free_Trial).replace("]]>", "");
                            }
                        }

                        // new added
                        if (elem.getElementsByTagName("Type").item(0) != null) {
                            if (elem.getElementsByTagName("Type").item(0).getFirstChild() != null) {
                                moJobInfo.ProductType = elem.getElementsByTagName("Type").item(0).getFirstChild().getNodeValue();
                                moJobInfo.ProductType = (moJobInfo.ProductType).replace("<![CDATA[", "");
                                moJobInfo.ProductType = (moJobInfo.ProductType).replace("]]>", "");
                            }
                        }

                        if (elem.getElementsByTagName("ExpiryDate").item(0) != null) {
                            if (elem.getElementsByTagName("ExpiryDate").item(0).getFirstChild() != null) {
                                moJobInfo.ProductExpiryDate = elem.getElementsByTagName("ExpiryDate").item(0).getFirstChild().getNodeValue();
                                moJobInfo.ProductExpiryDate = (moJobInfo.ProductExpiryDate).replace("<![CDATA[", "");
                                moJobInfo.ProductExpiryDate = (moJobInfo.ProductExpiryDate).replace("]]>", "");
                            }
                        }

                        if (elem.getElementsByTagName("ProductUrl").item(0) != null) {
                            if (elem.getElementsByTagName("ProductUrl").item(0).getFirstChild() != null) {
                                moJobInfo.ProductUrl = elem.getElementsByTagName("ProductUrl").item(0).getFirstChild().getNodeValue();
                                moJobInfo.ProductUrl = (moJobInfo.ProductUrl).replace("<![CDATA[", "");
                                moJobInfo.ProductUrl = (moJobInfo.ProductUrl).replace("]]>", "");

                            }
                        }

                        // end
                        // <![CDATA[2013-12-28 09:15:52]]>
                        Element XEResponse = (Element) elem.getElementsByTagName("ProductLinks").item(0);

                        if (XEResponse == null) continue;

                        NodeList eNDList = (XEResponse).getElementsByTagName("item");

                        for (int j = 0; j < eNDList.getLength(); j++) {
                            Node iNode = eNDList.item(j);
                            Element mElement = (Element) iNode;
                            Node iNDListw = mElement.getElementsByTagName("key").item(0).getFirstChild();
                            if (iNDListw == null) continue;
                            String ciNDList = mElement.getElementsByTagName("key").item(0).getFirstChild().getNodeValue();
                            ciNDList = ciNDList.replace("<![CDATA[", "");
                            String iNDList = ciNDList.replace("]]>", "");
                            iNDList = iNDList.toString();

                            String cEPUBUrl = mElement.getElementsByTagName("value").item(0).getFirstChild().getNodeValue();
                            cEPUBUrl = cEPUBUrl.replace("<![CDATA[", "");
                            EPUBUrl = cEPUBUrl.replace("]]>", "");

                            if (EPUBUrl.toLowerCase().indexOf(".epub") != -1 || EPUBUrl.toLowerCase().indexOf(".fl1") != -1) {
                                moJobInfo.EPUBUrl = EPUBUrl;
                                if (EPUBUrl.toLowerCase().indexOf(".epub") != -1)
                                    moJobInfo.BookType = "EPUB";
                                if (EPUBUrl.toLowerCase().indexOf(".fl1") != -1)
                                    moJobInfo.BookType = "FLASH";

                                Element EL_ApiExport = (Element) elem.getElementsByTagName("API_Export").item(0);
                                NodeList NL_ApiExport_items = EL_ApiExport.getElementsByTagName("items");
                                for (int count = 0; count < NL_ApiExport_items.getLength(); count++) {
                                    Element ApiExport_items = (Element) NL_ApiExport_items.item(count);
                                    if (ApiExport_items.getElementsByTagName("label").item(0).getFirstChild() != null) {
                                        String label_examhall = ApiExport_items.getElementsByTagName("label").item(0).getFirstChild().getNodeValue();

                                        if (label_examhall.toLowerCase().equals("exam hall")) {
                                            if (ApiExport_items.getElementsByTagName("value").item(0).getFirstChild() != null) {
                                                String value_examhall = ApiExport_items.getElementsByTagName("value").item(0).getFirstChild().getNodeValue();
                                                ApiExport_items.getElementsByTagName("value").item(0).getFirstChild().getNodeValue();
                                                moJobInfo.IsExamHall = value_examhall;
                                            }
                                        }
                                    }
                                }
                                moJobInfo.IsSideLoaded = "No";
                                moJobInfo.HasBookDownloaded = false;
                                moJobInfo.HasProductUpdated = false;
                                moJobInfo.UpdateNewVersion = "N";
                                moJobInfo.JobThumbnailPath = moJobInfo.JobUrl;

                                jobArray.add(moJobInfo);
                                break;
                            }
                        }
                    }
                }
            }
        }
        catch (Throwable e)
        {
            throw e;
        }
        return jobArray;
    }

    public void RenameFolder(String TempPath, String Path, String JobId) throws Exception
    {
        String path = Path;

        File EPUBDirectory = new File(path);
        EPUBDirectory = new File(TempPath);
        final File F = new File(TempPath);
        File newDrectory = new File(F.getParent(), JobId);
        F.renameTo(newDrectory);
    }

    public void GetLastPageName() throws Exception
    {
        String Job_xml_path = FlipickConfig.ContentRootPath + "/" + JobId + "/" + JobId + ".xml";
        Document oDocJobInfo = FlipickIO.GetDocumentFromFile(Job_xml_path);
        if (oDocJobInfo.getElementsByTagName("LastPagelink").item(0).getFirstChild() != null)
        {
            NodeList NL_Orientation = oDocJobInfo.getElementsByTagName("LastPagelink");
            Last_page_link = NL_Orientation.item(0).getFirstChild().getNodeValue();
        }
    }

    public void getPageIndexAfterFontSize() throws Exception
    {
        String Job_xml_path = FlipickConfig.ContentRootPath + "/" + JobId + "/" + JobId + ".xml";
        Document oDocJobInfo = FlipickIO.GetDocumentFromFile(Job_xml_path);
        if (oDocJobInfo.getElementsByTagName("LastPageFontPageName").item(0).getFirstChild() != null)
        {
            NodeList NL_Orientation = oDocJobInfo.getElementsByTagName("LastPageFontPageName");
            LastPageFontPageName = NL_Orientation.item(0).getFirstChild().getNodeValue();
        }
        if (oDocJobInfo.getElementsByTagName("LastPageFontColumnNo").item(0).getFirstChild() != null)
        {
            NodeList NL_Orientation = oDocJobInfo.getElementsByTagName("LastPageFontColumnNo");
            LastPageFontColumnNo = NL_Orientation.item(0).getFirstChild().getNodeValue();
        }
    }

    public void PopulatePageXHTMLArray(String tempPath, String FromActivity) throws Exception
    {
        String JobFolderPath;
        Document oDocJobInfo;
        if (tempPath != null && FromActivity != null)
        {
            JobFolderPath = tempPath;
            oDocJobInfo = FlipickIO.GetDocumentFromFile(tempPath + "/" + JobId + ".xml");

        }
        else
        {
            JobFolderPath = FlipickConfig.ContentRootPath + "/" + JobId;
            oDocJobInfo = FlipickIO.GetDocumentFromFile(FlipickConfig.ContentRootPath + "/" + JobId + "/" + JobId + ".xml");
        }
        NodeList NL_Orientation = oDocJobInfo.getElementsByTagName("Orientation");
        FlipickStaticData.Orientation = NL_Orientation.item(0).getFirstChild().getNodeValue();

        NodeList NL_BookType = oDocJobInfo.getElementsByTagName("BookType");
        String BookType = NL_BookType.item(0).getFirstChild().getNodeValue();
        FlipickStaticData.BookType = BookType;

        if (oDocJobInfo.getElementsByTagName("Title").item(0).getFirstChild() != null)
        {
            NodeList NL_BookTitle = oDocJobInfo.getElementsByTagName("Title");
            FlipickStaticData.BookTitle = NL_BookTitle.item(0).getFirstChild().getNodeValue();
        }
        NodeList NL_PathOPS = oDocJobInfo.getElementsByTagName("PathOPS");
        String PathOPS = "";
        if (NL_PathOPS.item(0).getFirstChild() != null)
        {
            PathOPS = NL_PathOPS.item(0).getFirstChild().getNodeValue();
        }

        NodeList NL_Pages = oDocJobInfo.getElementsByTagName("Page");
        PageXHTMLList = new ArrayList<String>();

        if (!PathOPS.equals(""))
        {
            if (tempPath != null && FromActivity != null)
            {
                FlipickStaticData.OEBPSPath = tempPath + "/" + PathOPS;
            }
            else
            {
                FlipickStaticData.OEBPSPath = FlipickConfig.ContentRootPath + "/" + JobId + "/" + PathOPS;
            }
        }
        else
        {
            if (tempPath != null && FromActivity != null)
            {
                FlipickStaticData.OEBPSPath = tempPath;
            }
            else
            {
                FlipickStaticData.OEBPSPath = FlipickConfig.ContentRootPath + "/" + JobId;
            }
        }
        ReflowColumnShift = new ArrayList<String>();
        LinkedIDs = new ArrayList<String>();
        ReflowNewFileNames = new ArrayList<String>();

        if (FlipickStaticData.oJobInfo.Is_Free_Trial.equalsIgnoreCase("Y"))
        {
            int count = 0;

            for (int i = 0; i < NL_Pages.getLength(); i++)
            {

                if (FlipickStaticData.BookType.equals("REFLOWABLE"))
                {
                    if (count < FlipickStaticData.MaxAllowedAdaptivePages)
                    {
                        String PageName = (String) NL_Pages.item(i).getTextContent();
                        if (FlipickStaticData.BookType.equals("REFLOWABLE"))
                        {
                            String ColShift = ((Element) NL_Pages.item(i)).getAttribute("ColumShift");
                            ReflowColumnShift.add(ColShift);
                            if (!PathOPS.equals(""))
                            {
                                ReflowNewFileNames.add(JobFolderPath + "/" + PathOPS + "/__fp" + i + PageName);
                            }
                            else
                            {
                                ReflowNewFileNames.add(JobFolderPath + "/__fp" + i + PageName);
                            }

                            // For HyperLink TOC
                            String LinkedId = ((Element) NL_Pages.item(i)).getAttribute("LinkedIds");
                            LinkedIDs.add(LinkedId);
                            // End
                        }
                        String XHTMLFilePath = "";
                        if (!PathOPS.equals(""))
                        {
                            XHTMLFilePath = "file://" + JobFolderPath + "/" + PathOPS + "/" + PageName;
                        }
                        else
                        {
                            XHTMLFilePath = "file://" + JobFolderPath + "/" + PageName;
                        }
                        if (!PageXHTMLList.contains(XHTMLFilePath))
                        {
                            count++;
                        }
                        PageXHTMLList.add(XHTMLFilePath);
                    }
                    else
                    {
                        if (FlipickStaticData.BookType.equals("REFLOWABLE"))
                        {
                            String ColShift = ((Element) NL_Pages.item(i)).getAttribute("ColumShift");
                            ReflowColumnShift.add(ColShift);
                            ReflowNewFileNames.add("file:///" + "android_asset" + "/ProductBuy.html");
                        }
                        // for all reflowable and fix layout book.
                        PageXHTMLList.add("file:///" + "android_asset" + "/ProductBuy.html");
                        break;
                    }
                }
                else
                {
                    if (count < FlipickStaticData.MaxAllowedFixLayoutPages)
                    {
                        String PageName = (String) NL_Pages.item(i).getTextContent();
                        String XHTMLFilePath = "";
                        if (!PathOPS.equals(""))
                        {
                            XHTMLFilePath = "file://" + JobFolderPath + "/" + PathOPS + "/" + PageName;
                        }
                        else
                        {
                            XHTMLFilePath = "file://" + JobFolderPath + "/" + PageName;
                        }
                        if (!PageXHTMLList.contains(XHTMLFilePath))
                        {
                            count++;
                        }
                        PageXHTMLList.add(XHTMLFilePath);
                    }
                    else
                    {
                        PageXHTMLList.add("file:///" + "android_asset" + "/ProductBuy.html");
                        break;
                    }
                }
            }
        }
        else
        {

            for (int i = 0; i < NL_Pages.getLength(); i++)
            {

                String PageName = (String) NL_Pages.item(i).getTextContent();
                if (FlipickStaticData.BookType.equals("REFLOWABLE"))
                {
                    String ColShift = ((Element) NL_Pages.item(i)).getAttribute("ColumShift");
                    ReflowColumnShift.add(ColShift);

                    if (!PathOPS.equals(""))
                    {
                        ReflowNewFileNames.add(JobFolderPath + "/" + PathOPS + "/__fp" + i + PageName);
                    }
                    else
                    {
                        ReflowNewFileNames.add(JobFolderPath + "/__fp" + i + PageName);
                    }
                    // For HyperLink TOC
                    String LinkedId = ((Element) NL_Pages.item(i)).getAttribute("LinkedIds");
                    LinkedIDs.add(LinkedId);
                    // End
                }
                String XHTMLFilePath = "";
                if (!PathOPS.equals(""))
                {
                    XHTMLFilePath = "file://" + JobFolderPath + "/" + PathOPS + "/" + PageName;
                }
                else
                {
                    XHTMLFilePath = "file://" + JobFolderPath + "/" + PageName;
                }
                PageXHTMLList.add(XHTMLFilePath);
            }
        }
    }

    public void populateBookmark(Context context)
    {
        List<Bookmark> bookmark;
        new Bookmark();
        bookmark = Bookmark.GetobjBookmarkFromDB(context, JobId);

        BookmarkList = new ArrayList<PageInfo>();

        for (int i = 0; i < PageXHTMLList.size(); i++)
        {
            oPageInfo = new PageInfo();

            oPageInfo.Job_ID = JobId;
            oPageInfo.Xhtml_Path_PageLink = PageXHTMLList.get(i);
            oPageInfo.Bookmark_Pageno = i + "";
            oPageInfo.is_Bookmark = "N";
            BookmarkList.add(oPageInfo);
        }

        if (bookmark.size() > 0)
        {
            for (int j = 0; j < bookmark.size(); j++)
            {
                for (int i = 0; i < PageXHTMLList.size(); i++)
                {
                    if (Integer.parseInt(bookmark.get(j).PageNo) == i)
                    {
                        BookmarkList.get(i).is_Bookmark = bookmark.get(j).Is_Bookmark;
                    }

                }
            }
        }
    }

    public void PopulateReversePageXHTMLArray(String tempPath, String FromActivity) throws Exception
    {
        String JobFolderPath;
        Document oDocJobInfo;
        if (tempPath != null && FromActivity != null)
        {
            JobFolderPath = tempPath;
            oDocJobInfo = FlipickIO.GetDocumentFromFile(tempPath + "/" + JobId + ".xml");

        }
        else
        {
            JobFolderPath = FlipickConfig.ContentRootPath + "/" + JobId;
            oDocJobInfo = FlipickIO.GetDocumentFromFile(FlipickConfig.ContentRootPath + "/" + JobId + "/" + JobId + ".xml");
        }
        NodeList NL_Orientation = oDocJobInfo.getElementsByTagName("Orientation");
        FlipickStaticData.Orientation = NL_Orientation.item(0).getFirstChild().getNodeValue();

        NodeList NL_BookType = oDocJobInfo.getElementsByTagName("BookType");
        String BookType = NL_BookType.item(0).getFirstChild().getNodeValue();
        FlipickStaticData.BookType = BookType;

        if (oDocJobInfo.getElementsByTagName("Title").item(0).getFirstChild() != null)
        {
            NodeList NL_BookTitle = oDocJobInfo.getElementsByTagName("Title");
            FlipickStaticData.BookTitle = NL_BookTitle.item(0).getFirstChild().getNodeValue();
        }
        NodeList NL_PathOPS = oDocJobInfo.getElementsByTagName("PathOPS");
        String PathOPS = "";
        if (NL_PathOPS.item(0).getFirstChild() != null)
        {
            PathOPS = NL_PathOPS.item(0).getFirstChild().getNodeValue();
        }
        NodeList NL_Pages = oDocJobInfo.getElementsByTagName("Page");
        PageXHTMLList = new ArrayList<String>();
        if (!PathOPS.equals(""))
        {
            if (tempPath != null && FromActivity != null)
            {
                FlipickStaticData.OEBPSPath = tempPath + "/" + PathOPS;
            }
            else
            {
                FlipickStaticData.OEBPSPath = FlipickConfig.ContentRootPath + "/" + JobId + "/" + PathOPS;
            }
        }
        else
        {
            if (tempPath != null && FromActivity != null)
            {
                FlipickStaticData.OEBPSPath = tempPath;
            }
            else
            {
                FlipickStaticData.OEBPSPath = FlipickConfig.ContentRootPath + "/" + JobId;
                ;
            }
        }
        ReflowColumnShift = new ArrayList<String>();
        LinkedIDs = new ArrayList<String>();
        for (int i = NL_Pages.getLength(); i > 0; i--)
        {
            String PageName = (String) NL_Pages.item(i - 1).getTextContent();
            if (FlipickStaticData.BookType.equals("REFLOWABLE"))
            {
                String ColShift = ((Element) NL_Pages.item(i)).getAttribute("ColumShift");
                ReflowColumnShift.add(ColShift);
                if (!PathOPS.equals(""))
                {
                    ReflowNewFileNames.add(JobFolderPath + "/" + PathOPS + "/__fp" + i + PageName);
                }
                else
                {
                    ReflowNewFileNames.add(JobFolderPath + "/__fp" + i + PageName);
                }
                String LinkedId = ((Element) NL_Pages.item(i)).getAttribute("LinkedIds");
                LinkedIDs.add(LinkedId);
            }
            String XHTMLFilePath = "";
            if (!PathOPS.equals(""))
            {
                XHTMLFilePath = "file://" + JobFolderPath + "/" + PathOPS + "/" + PageName;
            }
            else
            {
                XHTMLFilePath = "file://" + JobFolderPath + "/" + PageName;
            }
            PageXHTMLList.add(XHTMLFilePath);
        }

    }

    public void PopulatePageXHTMLArray2(String tempPath, String FromActivity) throws Exception
    {
        String JobFolderPath;
        Document oDocJobInfo;
        if (tempPath != null && FromActivity != null)
        {
            JobFolderPath = tempPath;
            oDocJobInfo = FlipickIO.GetDocumentFromFile(tempPath + "/" + JobId + ".xml");

        }
        else
        {
            JobFolderPath = FlipickConfig.ContentRootPath + "/" + JobId;
            oDocJobInfo = FlipickIO.GetDocumentFromFile(FlipickConfig.ContentRootPath + "/" + JobId + "/" + JobId + ".xml");
        }

        NodeList NL_PathOPS = oDocJobInfo.getElementsByTagName("PathOPS");
        String PathOPS = "";
        if (NL_PathOPS.item(0).getFirstChild() != null)
        {
            PathOPS = NL_PathOPS.item(0).getFirstChild().getNodeValue();
        }
        NodeList NL_Pages = oDocJobInfo.getElementsByTagName("Page");
        PageXHTMLList = new ArrayList<String>();
        PageXHTML_LinkIds = new ArrayList<String>();

        for (int i = 0; i < NL_Pages.getLength(); i++)
        {
            String PageName = (String) NL_Pages.item(i).getTextContent();
            File FileName = new File(PageName);
            String stName = FileName.getName();
            String XHTMLFilePath = "";
            if (!PathOPS.equals(""))
            {
                XHTMLFilePath = "file://" + JobFolderPath + "/" + PathOPS + "/" + stName;// +
                // PageName;
            }
            else
            {
                XHTMLFilePath = "file://" + JobFolderPath + "/" + stName;// +
                // PageName;
            }
            PageXHTMLList.add(XHTMLFilePath);

            if (!PathOPS.equals(""))
            {
                if (tempPath != null && FromActivity != null)
                {
                    FlipickStaticData.OEBPSPath = tempPath + "/" + PathOPS;
                }
                else
                {
                    FlipickStaticData.OEBPSPath = FlipickConfig.ContentRootPath + "/" + JobId + "/" + PathOPS;
                }
            }
            else
            {
                if (tempPath != null && FromActivity != null)
                {
                    FlipickStaticData.OEBPSPath = tempPath;
                }
                else
                {
                    FlipickStaticData.OEBPSPath = FlipickConfig.ContentRootPath + "/" + JobId;
                }
            }

            // // For hypeLink Toc
            Element element = (Element) NL_Pages.item(i);
            if (element.hasAttribute("LinkedIds"))
            {
                String Value = element.getAttribute("LinkedIds");
                PageXHTML_LinkIds.add(Value);
            }
            // end
        }

    }

    public void CreatePagesForPagination(Context context, String FromActivity) throws Exception
    {

        for (int j = 0; j < PageXHTMLList.size(); j++)
        {
            String defaultFont = "";

            String Path = PageXHTMLList.get(j);
            Path = Path.replace("file://", "");
            File fileInfo = new File(Path);

            String Filename = fileInfo.getName();

            Filename = Filename.replaceAll(" ", "_");

            String FilePath = FlipickStaticData.OEBPSPath + "/" + Filename;

            float deviceWidth = FlipickConfig.getDeviceWidth(context);
            float deviceHeight = FlipickConfig.getDeviceHeight(context);

            int Margin = (int) (deviceWidth * 4 / 100);

            if ((Margin % 2) != 0) Margin = Margin + 1;

            int ColumGap = Margin * 2;

            deviceWidth = deviceWidth - (Margin * 2);
            deviceHeight = deviceHeight - (Margin * 2);

            String HTMLData = FlipickIO.ReadHTMLFileText(fileInfo.getAbsolutePath());

            HTMLData = HTMLData.replace("<head>", "<head><script type='text/javascript' src='file:///android_asset/pagination/pagination.js'></script>");
            String BodyStyle = "";
            if (!FromActivity.equals("flipickpageviewer"))
            {
                if (!context.getResources().getBoolean(R.bool.isTablet))// mobile
                {
                    // defaultFont = "3.35em";
                    defaultFont = FlipickStaticData.DefaultFontSizeMobile + "em" + " !important";
                }
                else
                {
                    // defaultFont = "2em";
                    defaultFont = FlipickStaticData.DefaultFontSizeTablet + "em" + " !important";
                }
                BodyStyle = "padding:0px !important;margin:0px !important;font-size:" + defaultFont + "; ";
            }
            else
            {

                BodyStyle = "padding:0px !important;margin:0px !important;";
            }

            String BodyContainerStart = "<div id='f_p_container' style='margin:" + Margin + "px;width:" + deviceWidth + "px;-webkit-column-width:" + deviceWidth + "px;-webkit-column-gap:" + ColumGap
                    + "px;height:" + deviceHeight + "px;position:absolute;left:0px;top:0px;'>";
            String BodyContainerEnd = "</div>";

            HTMLData = AddStyleIntoBodyTag(HTMLData, BodyStyle, BodyContainerStart, BodyContainerEnd);

            Filename = "__" + Filename;

            String NewFilePath = FlipickStaticData.OEBPSPath + "/" + Filename;

            FlipickIO.WriteHTMLFileText(NewFilePath, HTMLData);

            // ////////////////
            // fill Static Array
            PaginationXHTMLList.add("file://" + FlipickStaticData.OEBPSPath + "/" + Filename);

        }

    }

    public void CreateCssForReflowableBook(Context context, String TempPath, String FontSize, String Activity) throws Exception
    {
        float deviceWidth = FlipickConfig.getDeviceWidth(context);
        float deviceHeight = FlipickConfig.getDeviceHeight(context);
        int PageCount = 0;
        int MeasureWidth = 0;
        List<String> arrayOriginalPageName = new ArrayList<String>();
        List<String> arrayColumShift = new ArrayList<String>();

        List<String> LinkedColumnId = new ArrayList<String>();
        ArrayList<String> LinkedName = new ArrayList<String>();
        ArrayList<String> arrayIDsInColumn = new ArrayList<String>();

        for (int i = 0; i < PageXHTMLList.size(); i++)
        {
            String LinkedIds[] = LinkedIDsOnPageIndex.get(i).replaceAll("\\s+", "").split("#");
            if (LinkedIds.length > 0)
            {
                LinkedColumnId = Arrays.asList(LinkedIds[0].split(","));
                LinkedName = (ArrayList<String>) Arrays.asList(LinkedIds[1].split(","));
            }
            MeasureWidth = XHTMLMeasuredWidth.get(i);

            PageCount = (int) Math.ceil(MeasureWidth / deviceWidth);

            for (int j = 0; j < PageCount; j++)
            {

                StringBuilder commaSepValueBuilder = new StringBuilder();
                if (LinkedColumnId.size() > 0)
                {
                    for (int k = 0; k < LinkedColumnId.size(); k++)
                    {
                        if (j == Integer.parseInt(LinkedColumnId.get(k)))
                        {
                            commaSepValueBuilder.append(LinkedName.get(k));
                            commaSepValueBuilder.append(",");
                        }

                    }
                }
                int Shift = ((int) deviceWidth * j);
                arrayColumShift.add("left:-" + Shift + "px");
                File filename = new File(PageXHTMLList.get(i));
                String SourceFilename = filename.getName();
                arrayOriginalPageName.add(SourceFilename);
                if (commaSepValueBuilder.length() > 0)
                {
                    commaSepValueBuilder.insert(0, ",");
                }
                arrayIDsInColumn.add(commaSepValueBuilder.toString());
            }
        }
        // Building Common CSS for all the pages
        int Margin = (int) (deviceWidth * 4 / 100);

        if ((Margin % 2) != 0) Margin = Margin + 1;

        int ColumGap = Margin * 2;

        float deviceWidth_1 = deviceWidth - (Margin * 2);
        float deviceHeight_1 = deviceHeight - (Margin * 2);

        String ParentDivCSS = " .f_p_parent_div {position:absolute;width:" + deviceWidth + "px;height:" + deviceHeight + "px;left:0px;top:0px;overflow:hidden} ";
        String ChidDivCSS = " .f_p_child_div {margin:" + Margin + "px;width:" + deviceWidth_1 + "px;-webkit-column-width:" + deviceWidth_1 + "px;-webkit-column-gap:" + ColumGap + "px;height:"
                + deviceHeight_1 + "px;position: absolute;top: 0px;} ";
        FlipickIO.WriteTextFile(new File(FlipickStaticData.OEBPSPath + "/f_p_common.css"), ParentDivCSS + ChidDivCSS);

        // Add CSS Link to all the pages
        for (int i = 0; i < PageXHTMLList.size(); i++)
        {

            String SourcePath = PageXHTMLList.get(i);
            SourcePath = SourcePath.replace("file://", "");

            String HTMLData = FlipickIO.ReadHTMLFileText(SourcePath);
            String FindText = "<head>";

            String ReplaceText = "<head><link href='f_p_common.css' rel='stylesheet' type='text/css' /><style type='text/css'>body{font-size:" + FontSize + " !important}</style>";
            HTMLData = HTMLData.replace(FindText, ReplaceText);

            String BodyStyle = "padding:0px !important;margin:0px !important";
            String BodyContainerStart = "<div class='f_p_parent_div'><div id='f_p_child_div_c' class='f_p_child_div'>";
            String BodyContainerEnd = "</div></div>";
            HTMLData = AddStyleIntoBodyTag(HTMLData, BodyStyle, BodyContainerStart, BodyContainerEnd);

            FlipickIO.WriteTextFile(new File(SourcePath), HTMLData);
        }

        // Update Job Xml
        String XmlPath = TempPath + "/" + JobId + ".xml";
        Document oXMLDoc = FlipickIO.GetDocumentFromFile(XmlPath);

        NodeList NLPages = oXMLDoc.getElementsByTagName("Pages");

        NLPages.item(0).getParentNode().removeChild(NLPages.item(0));

        Element EL_Pages = oXMLDoc.createElement("Pages");

        for (int b = 0; b < arrayOriginalPageName.size(); b++)
        {
            Element EL_Page = oXMLDoc.createElement("Page");
            EL_Page.setAttribute("ColumShift", "#f_p_child_div_c {" + arrayColumShift.get(b) + "}");
            EL_Page.setAttribute("LinkedIds", arrayIDsInColumn.get(b));
            EL_Page.appendChild(oXMLDoc.createTextNode(arrayOriginalPageName.get(b)));
            EL_Pages.appendChild(EL_Page);

        }
        ((Element) oXMLDoc.getElementsByTagName("JobInfo").item(0)).appendChild(EL_Pages);
        FlipickIO.SaveXMLFileFromDocument(XmlPath, oXMLDoc);

        // Delete temporary pages
        for (int i = 0; i < PaginationXHTMLList.size(); i++)
        {
            String SourcePath = PaginationXHTMLList.get(i);
            SourcePath = SourcePath.replace("file://", "");
            File filename = new File(SourcePath);
            filename.delete();
        }
    }

    public String AddStyleIntoBodyTag(String HTML, String BodyStyle, String BodyContainerStart, String BodyContainerEnd)
    {

        if (HTML.indexOf("<body>") == -1)
        {
            int b_startindex = HTML.indexOf("<body");
            if (b_startindex > 0)
            {
                String BeforeBody = HTML.substring(0, b_startindex);

                String WithBody = HTML.substring(b_startindex, HTML.length());

                int bodyEndIntex = WithBody.indexOf(">");
                String BodyTag = WithBody.substring(0, bodyEndIntex + 1);

                if (BodyTag.indexOf("style") > 0)
                {
                    BodyTag = BodyTag.replace("style='", "style='" + BodyStyle);
                    BodyTag = BodyTag.replace("style=\"", "style=\"" + BodyStyle);
                }
                else
                {
                    BodyTag = BodyTag.replace("<body", "<body style='" + BodyStyle + "'");
                }

                String AfterBody = WithBody.substring(WithBody.indexOf(">") + 1);
                if (BodyContainerStart != "")
                {
                    AfterBody = BodyContainerStart + AfterBody;
                    AfterBody = AfterBody.replace("</body>", BodyContainerEnd + "</body>");
                }
                HTML = BeforeBody + BodyTag + AfterBody;
            }
            // else
            // {
            //
            // }
        }
        else
        {
            HTML = HTML.replace("<body>", "<body>" + BodyContainerStart);
            HTML = HTML.replace("</body>", BodyContainerEnd + "</body>");
            HTML = HTML.replace("<body>", "<body style='" + BodyStyle + "'>");
        }

        return HTML;
    }

    public void DeleteTempFolder(Context context, String UserName) throws Exception
    {
        String XML_TargetLocation = FlipickConfig.ContentRootPath + "/" + UserName;

        File file = new File(XML_TargetLocation);

        File list[] = file.listFiles();
        if (list != null)
        {
            for (int j = 0; j < list.length; j++)
            {
                String Path = list[j].getAbsolutePath();
                if (Path.contains("__"))
                {
                    FlipickIO.DeleteDirectoryRecursive(new File(Path));
                }
            }
        }
    }

    public void UpdateFontSizeInHTML(String FontSize, String TempPath, String BackgroundColor)
    {
        try
        {
            String bgColor = "";
            String fontcolor = "";
            String values[] = BackgroundColor.split(",");
            if (values.length > 1)
            {
                bgColor = values[0];
                fontcolor = values[1];
            }
            String XML_TargetLocation = TempPath + "/" + JobId + ".xml";

            Document oDocJobInfo = FlipickIO.GetDocumentFromFile(XML_TargetLocation);

            NodeList NL_PathOPS = oDocJobInfo.getElementsByTagName("PathOPS");
            String PathOPS = "";
            if (NL_PathOPS.item(0).getFirstChild() != null)
            {
                PathOPS = NL_PathOPS.item(0).getFirstChild().getNodeValue();
            }

            NodeList NL_Pages = oDocJobInfo.getElementsByTagName("Page");
            for (int i = 0; i < NL_Pages.getLength(); i++)
            {
                String PageName = (String) NL_Pages.item(i).getTextContent();
                String SourcePath = "";
                if (!PathOPS.equals(""))
                {
                    SourcePath = TempPath + "/" + PathOPS + "/" + PageName;
                }
                else
                {
                    SourcePath = TempPath + "/" + PageName;
                }
                String HTMLData = FlipickIO.ReadHTMLFileText(SourcePath);
                String FindText = "<head>";
                String ReplaceText;
                if (bgColor == "" && fontcolor == "")
                {
                    ReplaceText = "<head><style type='text/css'>body{font-size:" + FontSize + " !important}</style>";
                }
                else
                {
                    ReplaceText = "<head><style type='text/css'>body{font-size:" + FontSize + " !important;background-color:" + bgColor + ";color:" + fontcolor + "}</style>";
                }

                HTMLData = HTMLData.replace(FindText, ReplaceText);
                FlipickIO.WriteTextFile(new File(SourcePath), HTMLData);

            }

        }
        catch (Exception e)
        {

        }
    }
}
