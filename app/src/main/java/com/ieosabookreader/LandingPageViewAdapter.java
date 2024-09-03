package com.ieosabookreader;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
public class LandingPageViewAdapter extends BaseAdapter {
    String[] result;
    TypedArray imageId;

    Context context;
    private LayoutInflater inflater = null;
    //int[] color_array;
    User oUser;

    public LandingPageViewAdapter(Context context)
    {
        this.context = context;
        // courses = new ArrayList<Course>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Resources res = context.getResources();
        String[] DashboardList = res.getStringArray(R.array.dashboardArray);

        result = DashboardList;
        TypedArray DashboardImages = res.obtainTypedArray(R.array.dashboardImageArray);
        imageId = DashboardImages;

        //color_array = context.getResources().getIntArray(R.array.dashboardColorArray);
    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.landingpage_list_adapter, null);
        holder.tv = (TextView) rowView.findViewById(R.id.txt_title);
        holder.img = (ImageView) rowView.findViewById(R.id.img_thumb);
        //holder.row_holder = (RelativeLayout) rowView.findViewById(R.id.row_holder);
        holder.tv.setText(result[position]);
        // holder.img.setImageResource(imageId[position]);
        holder.img.setImageResource(imageId.getResourceId(position, -1));
        //holder.row_holder.setBackgroundColor(color_array[position]);

        rowView.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                if (result[position].equals("My Library"))
                {
                    // Toast.makeText(context, "My Library",
                    // Toast.LENGTH_LONG).show();
                    Intent oIntent = new Intent(context, MainActivity.class);
                    Bundle oBundle = new Bundle();
                    oBundle.putString("ACTION", "RefreshJobXml");
                    oIntent.putExtras(oBundle);
                    ((Activity) context).startActivity(oIntent);
                    ((Activity) context).finish();
                    ((Activity) context).overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_left);

                }
                else if (result[position].equals("Store"))
                {
                    // Toast.makeText(context, "Store",
                    // Toast.LENGTH_LONG).show();

                    // String Url = FlipickConfig.Store_Url;
                    // Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    // Uri.parse(Url));
                    // ((Activity) context).startActivity(browserIntent);

                    // if (!ConnectionDetector.IsInternetAvailable(context))
                    // {
                    // ConnectionDetector.DisplayInternetErrorMessage(context);
                    // return;
                    // }
                    // Intent intent = new Intent(context, StoreActivity.class);
                    // Bundle bundle = new Bundle();
                    // bundle.putString("Activity", "LandingPage");
                    // intent.putExtras(bundle);
                    // ((Activity) context).startActivity(intent);
                    // ((Activity) context).finish();
                    // ((Activity)
                    // context).overridePendingTransition(R.anim.enter_from_right,
                    // R.anim.exit_from_left);
                }

                else if (result[position].equals("Scan QR Code"))
                {
                    // Toast.makeText(context, "QR Codes",
                    // Toast.LENGTH_LONG).show();
					/*Intent intent = new Intent(context, QRCodeReaderActivity.class);
					((Activity) context).startActivity(intent);
					((Activity) context).finish();
					((Activity) context).overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_left);*/
                }
                else if (result[position].equals("Redeem Access Code"))
                {
                    // Toast.makeText(context, "Access Codes",
                    // Toast.LENGTH_LONG).show();

                   // Intent oIntent = new Intent(context, ActivationCode.class);
                    //((Activity) context).startActivity(oIntent);
                   // ((Activity) context).overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_left);
                   // ((Activity) context).finish();
                }
                else if (result[position].equals("My Profile"))
                {
                    //Intent oIntent = new Intent(context, MyProfile.class);
                    //((Activity) context).startActivity(oIntent);
                    //((Activity) context).overridePendingTransition(R.anim.enter_from_right, R.anim.exit_from_left);
                    //((Activity) context).finish();
                    // Toast.makeText(context, "My Profile",
                    // Toast.LENGTH_LONG).show();
                }

            }
        });
        return rowView;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
        RelativeLayout row_holder;
    }

}
