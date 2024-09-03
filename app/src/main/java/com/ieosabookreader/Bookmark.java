package com.ieosabookreader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ieosareader.database.DatabaseInterface;

import java.util.ArrayList;
import java.util.List;

public class Bookmark {
    public static String TABLE_NAME = "tbl_bookmark";
    public static String KEY_BOOKMARK_ID = "bookmark_id";
    public static String KEY_PAGE_NO = "page_no";
    public static String KEY_JOB_ID = "job_id";
    public static final String KEY_IS_BOOKMARK = "is_bookmark";

    public static String CREATE_TABLE_BOOKMARK = "CREATE TABLE " + Bookmark.TABLE_NAME + "(" + Bookmark.KEY_BOOKMARK_ID + " integer primary key autoincrement," + Bookmark.KEY_PAGE_NO + " INTEGER,"
            + Bookmark.KEY_IS_BOOKMARK + " VARCHAR DEFAULT 'N', " + Bookmark.KEY_JOB_ID + " INTEGER" + ")";

    public String BookmarkID;
    public String PageNo;
    public String JobId;
    public String Is_Bookmark;

    public String getBookmarkID()
    {
        return BookmarkID;
    }

    public void setBookmarkID(String BookmarkID)
    {
        this.BookmarkID = BookmarkID;
    }

    public String getPageNo()
    {
        return PageNo;
    }

    public void setPageNo(String pageNo)
    {
        this.PageNo = pageNo;
    }

    public String getJobId()
    {
        return JobId;
    }

    public void setJobId(String jobId)
    {
        this.JobId = jobId;
    }

    public String getIs_Bookmark()
    {
        return Is_Bookmark;
    }

    public void setIs_Bookmark(String is_bookmark)
    {
        this.Is_Bookmark = is_bookmark;
    }

    public void insertBookmarkInDB(Context context)
    {
        DatabaseInterface databaseInterface = new DatabaseInterface(context);
        SQLiteDatabase db = databaseInterface.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Bookmark.KEY_PAGE_NO, PageNo);
        values.put(Bookmark.KEY_JOB_ID, JobId);
        values.put(Bookmark.KEY_IS_BOOKMARK, Is_Bookmark);

        db.insert(Bookmark.TABLE_NAME, null, values);

    }

    public static List<Bookmark> GetobjBookmarkFromDB(Context context, String JobId)
    {

        List<Bookmark> Bookmark_array = new ArrayList<Bookmark>();
        DatabaseInterface databaseInterface = new DatabaseInterface(context);
        SQLiteDatabase db = databaseInterface.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + Bookmark.TABLE_NAME + " WHERE " + Bookmark.KEY_JOB_ID + " = " + JobId + " ORDER BY " + Bookmark.KEY_BOOKMARK_ID + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            do
            {
                Bookmark bookmark = new Bookmark();
                bookmark.setBookmarkID(cursor.getString(cursor.getColumnIndex(Bookmark.KEY_BOOKMARK_ID)));
                bookmark.setIs_Bookmark(cursor.getString(cursor.getColumnIndex(Bookmark.KEY_IS_BOOKMARK)));
                bookmark.setPageNo(cursor.getString(cursor.getColumnIndex(Bookmark.KEY_PAGE_NO)));
                bookmark.setJobId(cursor.getString(cursor.getColumnIndex(Bookmark.KEY_JOB_ID)));

                Bookmark_array.add(bookmark);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return Bookmark_array;
    }

    public void deleteBookmarkfromDB(Context context, String Job_Id, String PageNo)
    {

        DatabaseInterface databaseInterface = new DatabaseInterface(context);
        SQLiteDatabase db = databaseInterface.getReadableDatabase();
        db.delete(TABLE_NAME, Bookmark.KEY_JOB_ID + " = " + Job_Id + " AND " + Bookmark.KEY_PAGE_NO + " = ?", new String[]
                { String.valueOf(PageNo) });
        db.close();

    }

    public void deleteBookmarkfromDBByJobId(Context context, String Job_Id)
    {

        DatabaseInterface databaseInterface = new DatabaseInterface(context);
        SQLiteDatabase db = databaseInterface.getReadableDatabase();
        db.delete(TABLE_NAME, Bookmark.KEY_JOB_ID + " = " + Job_Id, null);
        db.close();

    }

    public static void deleteTable(Context context)
    {
        DatabaseInterface databaseInterface = new DatabaseInterface(context);
        SQLiteDatabase db = databaseInterface.getWritableDatabase();
        db.delete(Bookmark.TABLE_NAME, null, null);
        db.close();
    }
}