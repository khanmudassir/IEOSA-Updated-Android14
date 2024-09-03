package com.ieosabookreader;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ieosareader.database.DatabaseInterface;


public class Notes {
    public static String TABLE_NAME = "tbl_notes";
    public static String KEY_NOTES_ID = "notes_id";
    public static String KEY_NOTES_DATA = "notes_data";
    public static String KEY_PAGE_NO = "page_no";
    public static String KEY_PARAGRAPH_ID = "paragraph_id";
    public static String KEY_SELECTEDTEXT_POS_ID = "selectedtext_Pos_id";
    public static String KEY_JOB_ID = "job_id";
    public static String KEY_TOPIC_NAME = "topic_name";

    public static String CREATE_TABLE_NOTES = "CREATE TABLE " + Notes.TABLE_NAME + "(" + Notes.KEY_NOTES_ID + " integer primary key autoincrement," + Notes.KEY_NOTES_DATA + " VHARCHAR,"
            + Notes.KEY_TOPIC_NAME + " VARCHAR," + Notes.KEY_PAGE_NO + " INTEGER," + Notes.KEY_PARAGRAPH_ID + " VARCHAR," + Notes.KEY_SELECTEDTEXT_POS_ID + " VARCHAR," + Notes.KEY_JOB_ID + " INTEGER"
            + ")";

    public String NotesData;
    public String NotesID;
    public String PageNo;
    public String Paragraph_id;
    public String Selectedtext_Pos_id;
    public String JobId;
    public String TopicName;

    public String getNotesData()
    {
        return NotesData;
    }

    public void setNotesData(String notesData)
    {
        NotesData = notesData;
    }

    public String getNotesID()
    {
        return NotesID;
    }

    public void setNotesID(String notesID)
    {
        this.NotesID = notesID;
    }

    public String getPageNo()
    {
        return PageNo;
    }

    public void setPageNo(String pageNo)
    {
        this.PageNo = pageNo;
    }

    public String getParagraphId()
    {
        return Paragraph_id;
    }

    public void setParagraphId(String paregraphId)
    {
        this.Paragraph_id = paregraphId;
    }

    public String getSelectedtextPosid()
    {
        return Selectedtext_Pos_id;
    }

    public void setSelectedtextPosId(String selectedtext_posid)
    {
        this.Selectedtext_Pos_id = selectedtext_posid;
    }

    public String getJobId()
    {
        return JobId;
    }

    public void setJobId(String jobId)
    {
        this.JobId = jobId;
    }

    public String getTopicName()
    {
        return TopicName;
    }

    public void setTopicName(String topicName)
    {
        this.TopicName = topicName;
    }

    public void insertNotesInDB(Context context)
    {
        DatabaseInterface databaseInterface = new DatabaseInterface(context);
        SQLiteDatabase db = databaseInterface.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Notes.KEY_NOTES_DATA, NotesData);
        values.put(Notes.KEY_PAGE_NO, PageNo);
        values.put(Notes.KEY_PARAGRAPH_ID, Paragraph_id);
        values.put(Notes.KEY_SELECTEDTEXT_POS_ID, Selectedtext_Pos_id);
        values.put(Notes.KEY_JOB_ID, JobId);
        values.put(Notes.KEY_TOPIC_NAME, TopicName);

        db.insert(Notes.TABLE_NAME, null, values);

    }

    public void GetNotesDataByParagraphID(Context context, String JobID, String ParagraphId)
    {
        DatabaseInterface databaseInterface = new DatabaseInterface(context);
        SQLiteDatabase db = databaseInterface.getWritableDatabase();

        String selectQuery = "SELECT  * FROM " + Notes.TABLE_NAME + " WHERE " + Notes.KEY_JOB_ID + " = " + JobID + " AND " + Notes.KEY_PARAGRAPH_ID + " ='" + ParagraphId + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToLast())
        {
            do
            {
                this.setNotesData(cursor.getString(cursor.getColumnIndex(Notes.KEY_NOTES_DATA)));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void updateNotesDataByParagraphId(Context context, String noteData, String JobId, String ParagraphId)
    {
        DatabaseInterface databaseInterface = new DatabaseInterface(context);
        SQLiteDatabase db = databaseInterface.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NOTES_DATA, noteData);

        db.update(TABLE_NAME, values, KEY_JOB_ID + " = ?" + " AND " + Notes.KEY_PARAGRAPH_ID + " = ? ", new String[]
                { String.valueOf(JobId), String.valueOf(ParagraphId) });
        db.close();
    }

    public static List<Notes> GetobjNotesFromDB(Context context, String JobId)
    {

        List<Notes> Notes_array = new ArrayList<Notes>();
        DatabaseInterface databaseInterface = new DatabaseInterface(context);
        SQLiteDatabase db = databaseInterface.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + Notes.TABLE_NAME + " WHERE " + Notes.KEY_JOB_ID + " = " + JobId + " ORDER BY " + Notes.KEY_NOTES_ID + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst())
        {
            do
            {
                Notes notes = new Notes();
                notes.setNotesID(cursor.getString(cursor.getColumnIndex(Notes.KEY_NOTES_ID)));
                notes.setNotesData(cursor.getString(cursor.getColumnIndex(Notes.KEY_NOTES_DATA)));
                notes.setPageNo(cursor.getString(cursor.getColumnIndex(Notes.KEY_PAGE_NO)));
                notes.setJobId(cursor.getString(cursor.getColumnIndex(Notes.KEY_JOB_ID)));
                notes.setParagraphId(cursor.getString(cursor.getColumnIndex(Highlights.KEY_PARAGRAPH_ID)));
                notes.setTopicName(cursor.getString(cursor.getColumnIndex(Highlights.KEY_TOPIC_NAME)));
                notes.setSelectedtextPosId(cursor.getString(cursor.getColumnIndex(Highlights.KEY_SELECTEDTEXT_POS_ID)));

                Notes_array.add(notes);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return Notes_array;
    }

    public void deleteNotesfromDB(Context context, String Job_Id, String ParagraphId)
    {

        DatabaseInterface databaseInterface = new DatabaseInterface(context);
        SQLiteDatabase db = databaseInterface.getReadableDatabase();
        db.delete(TABLE_NAME, Notes.KEY_JOB_ID + " = " + Job_Id + " AND " + Notes.KEY_PARAGRAPH_ID + " = ?", new String[]
                { String.valueOf(ParagraphId) });
        db.close();

    }

    public void deleteNotesfromDBByJobId(Context context, String JobId)
    {
        DatabaseInterface databaseInterface = new DatabaseInterface(context);
        SQLiteDatabase db = databaseInterface.getReadableDatabase();
        db.delete(Notes.TABLE_NAME, Notes.KEY_JOB_ID + " = " + JobId, null);
        db.close();
    }

    public static void deleteTable(Context context)
    {
        DatabaseInterface databaseInterface = new DatabaseInterface(context);
        SQLiteDatabase db = databaseInterface.getWritableDatabase();
        db.delete(Notes.TABLE_NAME, null, null);
        db.close();
    }
}