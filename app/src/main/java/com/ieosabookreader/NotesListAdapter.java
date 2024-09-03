package com.ieosabookreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NotesListAdapter extends BaseAdapter {

    List<Notes> notesList;
    Context context;
    private LayoutInflater inflater = null;
    UIHolder holder;

    public NotesListAdapter(Context context)
    {
        this.context = context;
        notesList = new ArrayList<Notes>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return notesList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return notesList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    public void updateDataSet(List<Notes> filtervalues)
    {
        this.notesList = filtervalues;
        this.notifyDataSetChanged();
    }

    class UIHolder
    {

        TextView nttxt_topic_name;
        TextView nttxt_note_data;
        TextView notes_pageno;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub

        if (convertView == null)
        {
            holder = new UIHolder();

            convertView = inflater.inflate(R.layout.thumbnails_notes, null);

            holder.nttxt_topic_name = (TextView) convertView.findViewById(R.id.nttxt_topic_name);
            holder.nttxt_note_data = (TextView) convertView.findViewById(R.id.nttxt_notes_data);
            holder.notes_pageno = (TextView) convertView.findViewById(R.id.notes_pageno);
            convertView.setTag(holder);

        }
        else
        {

            holder = (UIHolder) convertView.getTag();
        }

        holder.nttxt_topic_name.setText(notesList.get(position).getTopicName());

        holder.nttxt_note_data.setText(notesList.get(position).getNotesData());
        int pageNo = Integer.parseInt(notesList.get(position).getPageNo()) + 1;
        holder.notes_pageno.setText("Page No. " + pageNo);
        return convertView;

    }

    public void changeDataSet(List<Notes> filtervalues)
    {
        this.notesList = filtervalues;
        notifyDataSetChanged();
    }

}
