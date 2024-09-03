package com.ieosabookreader;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ViewPagerAdapter extends PagerAdapter {
    @Override
    public int getCount() {
        return layoutArray.length;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1)
    {
        return arg0 == ((View) arg1);
    }
    Activity activity;
    int layoutArray[];

    public ViewPagerAdapter(Activity act, int[] imgArra)
    {
        layoutArray = imgArra;
        activity = act;
    }
    public Object  instantiateItem (ViewGroup collection, int position)
    {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layoutArray[position], null);

        ((ViewPager) collection).addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2)
    {
        ((ViewPager) arg0).removeView((View) arg2);
    }



    @Override
    public Parcelable saveState()
    {
        return null;
    }
}
