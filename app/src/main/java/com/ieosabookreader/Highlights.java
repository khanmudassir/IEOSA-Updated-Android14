package com.ieosabookreader;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ieosareader.database.DatabaseInterface;


public class Highlights
{
    public static String TABLE_NAME = "tbl_highlights";
    public static String KEY_HIGHLIGHTS_ID = "highlights_id";
    public static String KEY_HIGHLIGHTS_DATA = "highlights_data";
    public static String KEY_PAGE_NO = "page_no";
    public static String KEY_PARAGRAPH_ID = "paragraph_id";
    public static String KEY_SELECTEDTEXT_POS_ID = "selectedtext_Pos_id";
    public static String KEY_JOB_ID = "job_id";
    public static String KEY_TOPIC_NAME = "topic_name";
    public static String KEY_HIGHLIGHT_COLOR = "highlight_color";

    public static String CREATE_TABLE_HIGHLIGHTS = "CREATE TABLE " + Highlights.TABLE_NAME + "(" + Highlights.KEY_HIGHLIGHTS_ID + " integer primary key autoincrement,"
            + Highlights.KEY_HIGHLIGHTS_DATA + " VARCHAR," + Highlights.KEY_TOPIC_NAME + " VARCHAR," + Highlights.KEY_PAGE_NO + " INTEGER," + Highlights.KEY_PARAGRAPH_ID + " VARCHAR,"
            + Highlights.KEY_SELECTEDTEXT_POS_ID + " VARCHAR," + Highlights.KEY_HIGHLIGHT_COLOR + " VARCHAR," + Highlights.KEY_JOB_ID + " INTEGER" + ")";

    // Alter Table for Highlight adding column selectedtext position column
    public static final String ADD_KEY_SELECTEDTEXT_POS_ID = "ALTER TABLE " + Highlights.TABLE_NAME + " ADD COLUMN " + Highlights.KEY_SELECTEDTEXT_POS_ID + " VARCHAR DEFAULT '0'";

    // Alter Table for Highlight adding column topic name column
    public static final String ADD_KEY_TOPIC_NAME = "ALTER TABLE " + Highlights.TABLE_NAME + " ADD COLUMN " + Highlights.KEY_TOPIC_NAME + " VARCHAR DEFAULT ' '";

    // Alter Table for Highlight adding column page_url column
    public static final String ADD_KEY_HIGHLIGHT_COLOR = "ALTER TABLE " + Highlights.TABLE_NAME + " ADD COLUMN " + Highlights.KEY_HIGHLIGHT_COLOR + " VARCHAR DEFAULT ' '";

    public String HighlightsData;
    public String HighlightsID;
    public String PageNo;
    public String ParagraphID;
    public String JobID;
    public String TopicName;
    public String SelectedTextScrollPos;
    public String HighlightColor;

    public String getHighlightColor()
    {
        return HighlightColor;
    }

    public void setHighlightColor(String highlightColor)
    {
        this.HighlightColor = highlightColor;
    }

    public String getSelectedTextScrollPos()
    {
        return SelectedTextScrollPos;
    }

    public void setSelectedTextScrollPos(String selectedTextScrollPos)
    {
        SelectedTextScrollPos = selectedTextScrollPos;
    }

    public String getHighlightsData()
    {
        return HighlightsData;
    }

    public void setHighlightsData(String highlightsData)
    {
        HighlightsData = highlightsData;
    }

    public String getTopicName()
    {
        return TopicName;
    }

    public void setTopicName(String topicName)
    {
        TopicName = topicName;
    }

    public String getHighlightsID()
    {
        return HighlightsID;
    }

    public void setHighlightsID(String highlightsID)
    {
        this.HighlightsID = highlightsID;
    }

    public String getHighlightsParagraphId()
    {
        return ParagraphID;
    }

    public void setHighlightsParagraphId(String paragraphId)
    {
        this.ParagraphID = paragraphId;
    }

    public String getHighlightsPageNo()
    {
        return PageNo;
    }

    public void setHighlightsPageNo(String pageNo)
    {
        this.PageNo = pageNo;
    }

    public void setHighlightsJobId(String jobId)
    {
        this.JobID = jobId;
    }

    public String getHighlightsCourseId()
    {
        return JobID;
    }

    public void insertHighlightsInDB(Context context)
    {

        DatabaseInterface databaseInterface = new DatabaseInterface(context);
        SQLiteDatabase db = databaseInterface.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(Highlights.KEY_HIGHLIGHTS_DATA, HighlightsData);
        values.put(Highlights.KEY_PAGE_NO, PageNo);
        values.put(Highlights.KEY_PARAGRAPH_ID, ParagraphID);
        values.put(Highlights.KEY_JOB_ID, JobID);
        values.put(Highlights.KEY_TOPIC_NAME, TopicName);
        values.put(Highlights.KEY_SELECTEDTEXT_POS_ID, SelectedTextScrollPos);
        values.put(Highlights.KEY_HIGHLIGHT_COLOR, HighlightColor);
        db.insert(Highlights.TABLE_NAME, null, values);
        // FlipickIO.CopyDataBaseToSdCard(context);

    }

    public static List<Highlights> GetobjHighlightsFromDB(Context context, String JobId)
    {

        List<Highlights> Highlight_array = new ArrayList<Highlights>();
        DatabaseInterface databaseInterface = new DatabaseInterface(context);
        SQLiteDatabase db = databaseInterface.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + Highlights.TABLE_NAME + " WHERE " + Highlights.KEY_JOB_ID + " = " + JobId + " ORDER BY " + Highlights.KEY_HIGHLIGHTS_ID + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            do
            {
                Highlights highlights = new Highlights();
                highlights.setHighlightsID(cursor.getString(cursor.getColumnIndex(Highlights.KEY_HIGHLIGHTS_ID)));
                highlights.setHighlightsData(cursor.getString(cursor.getColumnIndex(Highlights.KEY_HIGHLIGHTS_DATA)));
                highlights.setHighlightsPageNo(cursor.getString(cursor.getColumnIndex(Highlights.KEY_PAGE_NO)));
                highlights.setHighlightsParagraphId(cursor.getString(cursor.getColumnIndex(Highlights.KEY_PARAGRAPH_ID)));

                highlights.setHighlightsJobId(cursor.getString(cursor.getColumnIndex(Highlights.KEY_JOB_ID)));
                highlights.setTopicName(cursor.getString(cursor.getColumnIndex(Highlights.KEY_TOPIC_NAME)));

                highlights.setSelectedTextScrollPos(cursor.getString(cursor.getColumnIndex(Highlights.KEY_SELECTEDTEXT_POS_ID)));

                highlights.setHighlightColor(cursor.getString(cursor.getColumnIndex(Highlights.KEY_HIGHLIGHT_COLOR)));

                Highlight_array.add(highlights);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return Highlight_array;
    }

    public void deleteHighlightsfromDB(Context context, String Paragraph_Id, String PageNo)
    {
        DatabaseInterface databaseInterface = new DatabaseInterface(context);
        SQLiteDatabase db = databaseInterface.getReadableDatabase();
        String table_name = TABLE_NAME;
        String where = "paragraph_id ='" + Paragraph_Id + "' AND page_no ='" + PageNo + "'";
        String[] whereArgs = null;
        db.delete(table_name, where, whereArgs);
        db.close();
        // FlipickIO.CopyDataBaseToSdCard(context);

    }

    public void deleteHighlightsfromDBByJobId(Context context, String JobId)
    {
        DatabaseInterface databaseInterface = new DatabaseInterface(context);
        SQLiteDatabase db = databaseInterface.getReadableDatabase();
        db.delete(Highlights.TABLE_NAME, Highlights.KEY_JOB_ID + " = " + JobId, null);
        db.close();
    }

    public static void deleteTable(Context context)
    {
        DatabaseInterface databaseInterface = new DatabaseInterface(context);
        SQLiteDatabase db = databaseInterface.getWritableDatabase();
        db.delete(Highlights.TABLE_NAME, null, null);

        db.close();
    }
}