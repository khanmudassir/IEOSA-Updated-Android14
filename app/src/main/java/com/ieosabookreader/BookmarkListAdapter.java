package com.ieosabookreader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class BookmarkListAdapter extends BaseAdapter {

    List<Bookmark> bookmarkList;
    Context context;
    private LayoutInflater inflater = null;
    UIHolder holder;

    public BookmarkListAdapter(Context context)
    {
        this.context = context;
        bookmarkList = new ArrayList<Bookmark>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return bookmarkList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return bookmarkList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    public void updateDataSet(List<Bookmark> filtervalues)
    {
        this.bookmarkList = filtervalues;
        this.notifyDataSetChanged();
    }

    class UIHolder
    {
        TextView bookmark_pageno;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub

        if (convertView == null)
        {
            holder = new UIHolder();
            convertView = inflater.inflate(R.layout.thumbnails_bookmark, null);

            holder.bookmark_pageno = (TextView) convertView.findViewById(R.id.bookmark_pageNo);
            convertView.setTag(holder);
        }
        else
        {
            holder = (UIHolder) convertView.getTag();
        }

        if (FlipickStaticData.oJobInfo.Language.equals("ar"))
        {
            int pageNo = (FlipickStaticData.oJobInfo.PageXHTMLList.size() - Integer.parseInt(bookmarkList.get(position).getPageNo()));
            holder.bookmark_pageno.setText("Page No. " + pageNo);
        }
        else
        {
            int pageNo = Integer.parseInt(bookmarkList.get(position).getPageNo()) + 1;
            holder.bookmark_pageno.setText("Page No. " + pageNo);
        }
        return convertView;

    }

    public void changeDataSet(List<Bookmark> filtervalues)
    {
        this.bookmarkList = filtervalues;
        notifyDataSetChanged();
    }

}
