package com.ieosabookreader;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HighlightListAdapter extends BaseAdapter {


    List<Highlights> highlightList;
    Context context;
    private LayoutInflater inflater = null;
    UIHolder holder;

    public HighlightListAdapter(Context context)
    {
        this.context = context;
        highlightList = new ArrayList<Highlights>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return highlightList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return highlightList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    public void updateDataSet(List<Highlights> filtervalues)
    {
        this.highlightList = filtervalues;
        this.notifyDataSetChanged();
    }

    class UIHolder
    {

        TextView hltxt_section_name;
        TextView hltxt_highlight_data;
        TextView highlight_pageno;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub

        if (convertView == null)
        {
            holder = new UIHolder();

            convertView = inflater.inflate(R.layout.thumbnails_highlights, null);

            holder.hltxt_section_name = (TextView) convertView.findViewById(R.id.hltxt_section_name);
            holder.hltxt_highlight_data = (TextView) convertView.findViewById(R.id.hltxt_highlight_data);
            holder.highlight_pageno = (TextView) convertView.findViewById(R.id.highlight_pageno);
            convertView.setTag(holder);

        }
        else
        {

            holder = (UIHolder) convertView.getTag();
        }

        Spannable wordtoSpan = new SpannableString(highlightList.get(position).getHighlightsData().replaceAll("\\n|\\t|\\r|\\s+", " "));
        wordtoSpan.setSpan(new BackgroundColorSpan(Color.parseColor(highlightList.get(position).getHighlightColor())), 0, wordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.hltxt_highlight_data.setText(wordtoSpan);
        int pageNo = Integer.parseInt(highlightList.get(position).getHighlightsPageNo()) + 1;
        holder.highlight_pageno.setText("Page No. " + pageNo);
        return convertView;

    }

    public void changeDataSet(List<Highlights> filtervalues)
    {
        this.highlightList = filtervalues;
        notifyDataSetChanged();
    }

}
