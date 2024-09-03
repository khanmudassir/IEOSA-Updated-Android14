package com.ieosareader.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ieosabookreader.Bookmark;
import com.ieosabookreader.Highlights;
import com.ieosabookreader.JobInfo;
import com.ieosabookreader.Notes;

public class DatabaseInterface extends SQLiteOpenHelper {

    // All Static variables
    // Database Version

    // updated
    private static final int DATABASE_VERSION =1;
    // Database Name
    private static final String DATABASE_NAME = "IEOSABookReader_db";
    //private static final String DATABASE_NAME = "FlipickBookReader_db";
    //private static final String DATABASE_NAME ="ElsevierBookReader";
//	private static final String DATABASE_NAME = "BookHungamaReader";



    public DatabaseInterface(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(JobInfo.CREATE_JOB_INFO_TABLE);
        db.execSQL(Highlights.CREATE_TABLE_HIGHLIGHTS);
        db.execSQL(Notes.CREATE_TABLE_NOTES);
        db.execSQL(Bookmark.CREATE_TABLE_BOOKMARK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        System.out.println("db.getVersion() " + db.getVersion());
        if (oldVersion < newVersion)
        {
            updateDBforColumn(db);
//			db.execSQL(Notes.CREATE_TABLE_NOTES);
//			db.execSQL(Bookmark.CREATE_TABLE_BOOKMARK);
//			db.execSQL(Highlights.CREATE_TABLE_HIGHLIGHTS);
            db.setVersion(newVersion);

        }
    }

    public static boolean checkColumnExists(String Columname, SQLiteDatabase db, String TableName)
    {
        Boolean isColumnExistorNot = false;
        String selectQuery = "";
        selectQuery = "SELECT " + Columname + " FROM " + TableName;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.getColumnIndex(Columname);
        if (cursor.getColumnIndex(Columname) != -1)
        {
            isColumnExistorNot = true;
        }
        return isColumnExistorNot;

    }

    public static void updateDBforColumn(SQLiteDatabase db)
    {
        db.beginTransaction();
        try
        {
            Boolean iscolumnexist;
            try
            {
                iscolumnexist = checkColumnExists(JobInfo.KEY_PRODUCT_EXPIRYDATE, db, JobInfo.TABLE_NAME);
            }
            catch (SQLException e)
            {
                db.execSQL(JobInfo.ADD_KEY_PRODUCT_EXPIRYDATE);
            }
            try
            {
                iscolumnexist = checkColumnExists(JobInfo.KEY_CATEGORY_ID, db, JobInfo.TABLE_NAME);
            }
            catch (SQLException e)
            {
                db.execSQL(JobInfo.ADD_KEY_CATEGORY_ID);
            }

            try
            {
                iscolumnexist = checkColumnExists(JobInfo.KEY_CATEGORY_NAME, db, JobInfo.TABLE_NAME);
            }
            catch (SQLException e)
            {
                db.execSQL(JobInfo.ADD_KEY_CATEGORY_NAME);
            }

            try
            {
                iscolumnexist = checkColumnExists(JobInfo.KEY_PRODUCT_TYPE, db, JobInfo.TABLE_NAME);
            }
            catch (SQLException e)
            {
                db.execSQL(JobInfo.ADD_KEY_PRODUCT_TYPE);
            }

            try
            {
                iscolumnexist = checkColumnExists(JobInfo.KEY_LANGUAGE, db, JobInfo.TABLE_NAME);
            }
            catch (SQLException e)
            {
                db.execSQL(JobInfo.ADD_KEY_LANGUAGE);
            }

            try
            {
                iscolumnexist = checkColumnExists(JobInfo.KEY_PRODUCT_URL, db, JobInfo.TABLE_NAME);
            }
            catch (SQLException e)
            {
                db.execSQL(JobInfo.ADD_KEY_PRODUCT_URL);
            }

            try
            {
                iscolumnexist = checkColumnExists(JobInfo.KEY_IS_FREE_TRIAL, db, JobInfo.TABLE_NAME);
            }
            catch (SQLException e)
            {
                db.execSQL(JobInfo.ADD_KEY_IS_FREE_TRIAL);
            }
            try
            {
                iscolumnexist = checkColumnExists(JobInfo.KEY_UPDATE_NEWVERSION, db, JobInfo.TABLE_NAME);
            }
            catch (SQLException e)
            {
                db.execSQL(JobInfo.ADD_KEY_UPDATE_NEWVERSION);
            }

            db.setTransactionSuccessful();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            db.endTransaction();
        }
    }

}