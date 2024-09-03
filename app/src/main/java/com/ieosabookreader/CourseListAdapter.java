package com.ieosabookreader;

import android.content.Context;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CourseListAdapter extends BaseAdapter
{
    List<JobInfo> jobdata;
    Context context;
    private LayoutInflater inflater = null;
    UIHolder holder;

    public CourseListAdapter(Context context)
    {
        this.context = context;
        jobdata = new ArrayList<JobInfo>();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount()
    {
        return jobdata.size();
    }

    public Object getItem(int position)
    {
        return jobdata.get(position);
    }

    public long getItemId(int position)
    {
        return 0;
    }

    public void updateDataSet(List<JobInfo> jobdata)
    {
        this.jobdata = jobdata;
        this.notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {

        final JobInfo oJobInfo = jobdata.get(position);
        if (convertView == null)
        {
            holder = new UIHolder();

            convertView = inflater.inflate(R.layout.library_list_item, null);

            holder.txt_title = (TextView) convertView.findViewById(R.id.txt_title);
            holder.txt_auther = (TextView) convertView.findViewById(R.id.txt_auther);
            holder.txt_view = (TextView) convertView.findViewById(R.id.up_View);
            holder.txt_download = (TextView) convertView.findViewById(R.id.item_download);
            holder.txt_update = (TextView) convertView.findViewById(R.id.Update);
            holder.txt_sm_view = (TextView) convertView.findViewById(R.id.small_view);

            holder.imageView = (ImageView) convertView.findViewById(R.id.item_view);
            holder.viewup_layout = (LinearLayout) convertView.findViewById(R.id.viewup_layout);

            convertView.setTag(holder);
        }
        else
        {
            holder = (UIHolder) convertView.getTag();
        }

        holder.imageView.setId(position);
        holder.txt_title.setId(position);
        holder.txt_auther.setId(position);
        holder.txt_view.setId(position);
        holder.txt_download.setId(position);
        holder.txt_update.setId(position);
        holder.txt_sm_view.setId(position);
        holder.viewup_layout.setId(position);

        holder.txt_title.setText(Html.fromHtml(oJobInfo.JobTitle).toString());
        holder.txt_auther.setText(Html.fromHtml(oJobInfo.JobAuthor).toString());
        if (!oJobInfo.JobId.equals(""))
        {
            if (oJobInfo.HasBookDownloaded && oJobInfo.HasProductUpdated)
            {
                // small images view and update
                holder.txt_view.setVisibility(View.GONE);
                holder.viewup_layout.setVisibility(View.VISIBLE);
                holder.txt_download.setVisibility(View.GONE);
                holder.txt_update.setText("Update");
                // holder.txt_update.setId(0);
                holder.txt_update.setTag(0);

                holder.imageView.setClickable(true);

//				holder.txt_sm_view.setTag(position);
//				holder.txt_update.setTag(position);
//				holder.imageView.setTag(position);
            }
            else if (oJobInfo.HasBookDownloaded)
            {
                // large Download Image
                holder.viewup_layout.setVisibility(View.GONE);
                holder.txt_download.setVisibility(View.GONE);

                if (oJobInfo.IsSideLoaded.equals("Yes"))
                {
                    System.out.println(position + " before init");
                    // small images view and update
                    holder.viewup_layout.setVisibility(View.VISIBLE);
                    holder.txt_view.setVisibility(View.GONE);
                    holder.txt_update.setText("Delete");

                    // large View Image
                    holder.imageView.setClickable(false);

//					holder.txt_update.setTag(position);
//					holder.txt_update.setId(1);
                    holder.txt_update.setTag(1);

//					holder.txt_sm_view.setTag(position);
                }
                else
                {
                    holder.txt_view.setVisibility(View.VISIBLE);
                    holder.imageView.setClickable(true);
//					holder.imageView.setTag(position);
                }
//				holder.txt_view.setTag(position);
//				holder.imageView.setTag(position);
            }
            else
            {
                // large Download Image
                if (oJobInfo.IsSideLoaded.equals("Yes"))
                {
                    System.out.println(position + " before init");
                    holder.txt_download.setVisibility(View.GONE);
                    // small images view and update
                    holder.viewup_layout.setVisibility(View.VISIBLE);
                    holder.txt_view.setVisibility(View.GONE);

                    holder.txt_update.setText("Delete");

                    // large View Image
                    holder.imageView.setClickable(false);

//					holder.txt_update.setTag(position);
//					holder.txt_update.setId(1);
                    holder.txt_update.setTag(1);

//					holder.txt_sm_view.setTag(position);
                }
                else
                {
                    holder.txt_view.setVisibility(View.GONE);
                    holder.viewup_layout.setVisibility(View.GONE);
                    holder.txt_download.setVisibility(View.VISIBLE);

                    // large View Image
                    holder.imageView.setClickable(false);
//					holder.txt_download.setTag(position);

//					holder.imageView.setTag(position);
                }
            }
        }
        else
        {
            // large Download Image
            holder.txt_view.setVisibility(View.GONE);
            holder.viewup_layout.setVisibility(View.GONE);
            holder.txt_download.setVisibility(View.GONE);

            // large View Image
            holder.imageView.setClickable(false);
        }

        holder.imageView.setOnClickListener(new OnClickListener()
        {
            // large View Image Onclick
            @Override
            public void onClick(View v)
            {
                FlipickStaticData.progressMessage = "";
//				if (v.getTag() == null)
//				{
//					FlipickError.DisplayErrorAsLongToast(((MainActivity) context).getApplicationContext(), "Reloading library");
//					((MainActivity) context).ReloadMe();
//					return;
//				}

                //int Pos = (Integer) v.getTag();
                int Pos = (Integer) v.getId();
                JobInfo oJobInfo = jobdata.get(Pos);
                ((MainActivity) context).LaunchPager(oJobInfo);
            }
        });
        holder.txt_view.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                FlipickStaticData.progressMessage = "";
//				if (v.getTag() == null)
//				{
//					FlipickError.DisplayErrorAsLongToast(((MainActivity) context).getApplicationContext(), "Reloading library");
//					((MainActivity) context).ReloadMe();
//					return;
//				}

                //int Pos = (Integer) v.getTag();
                int Pos = (Integer) v.getId();
                JobInfo oJobInfo = jobdata.get(Pos);
                if (oJobInfo.UpdateNewVersion.equals("Y") && oJobInfo.BookType.equals("REFLOWABLE"))// reflowable
                {
                    User oUser = new User((MainActivity) context);
                    String TempPath = FlipickConfig.ContentRootPath + "/" + oUser.UserName + oJobInfo.JobId;

                    // copy pages from Backup to Temp folder
                    TempPath = FlipickStaticData.oJobInfo.copyFilesInTheTempFolder(oJobInfo.JobId);

                    Intent intpagination = new Intent((MainActivity) context, PaginationActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("JobId", oJobInfo.JobId);
                    bundle.putString("Activity", "SynchUpdate");
                    bundle.putString("TempPath", TempPath);
                    intpagination.putExtras(bundle);
                    ((MainActivity) context).startActivity(intpagination);
                    ((MainActivity) context).finish();
                }
                else
                {
                    ((MainActivity) context).LaunchPager(oJobInfo);
                }

            }
        });

        holder.txt_sm_view.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                FlipickStaticData.progressMessage = "";
//				if (v.getTag() == null)
//				{
//					FlipickError.DisplayErrorAsLongToast(((MainActivity) context).getApplicationContext(), "Reloading library");
//					((MainActivity) context).ReloadMe();
//					return;
//				}

                //int Pos = (Integer) v.getTag();
                int Pos = (Integer) v.getId();
                JobInfo oJobInfo = jobdata.get(Pos);
                if (oJobInfo.IsSideLoaded.equalsIgnoreCase("Yes") && !oJobInfo.HasBookDownloaded)
                {
                    try
                    {
                        ((MainActivity) context).DownloadAndLaunchPager(Pos, "");
                    }
                    catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        FlipickError.DisplayErrorAsLongToast(context.getApplicationContext(), e.getMessage());
                    }
                }
                else
                {
                    ((MainActivity) context).LaunchPager(oJobInfo);
                }

            }
        });

        holder.txt_download.setOnClickListener(new OnClickListener()
        {
            // large Download Image Onclick
            @Override
            public void onClick(View v)
            {
                if (!ConnectionDetector.IsInternetAvailable(context.getApplicationContext()))
                {
                    ConnectionDetector.DisplayInternetErrorMessage(context.getApplicationContext());
                    return;
                }

                v.setVisibility(View.INVISIBLE);

                //int Pos = (Integer) v.getTag();
                int Pos = (Integer) v.getId();
                try
                {
                    ((MainActivity) context).DownloadAndLaunchPager(Pos, "");
                }
                catch (Exception e)
                {
                    FlipickError.DisplayErrorAsLongToast(context.getApplicationContext(), e.getMessage());
                }
            }
        });

        // onupdate event call update db
        holder.txt_update.setOnClickListener(new OnClickListener()
        {
            // small update Image Onclick
            @Override
            public void onClick(View v)
            {
                if (v.getTag().equals(0))
                {
                    if (!ConnectionDetector.IsInternetAvailable(context.getApplicationContext()))
                    {
                        ConnectionDetector.DisplayInternetErrorMessage(context.getApplicationContext());
                        return;
                    }

                    //	int Pos = (Integer) v.getTag();
                    int Pos = (Integer) v.getId();

                    try
                    {
                        ((MainActivity) context).DownloadAndLaunchPager(Pos, "Update");
                    }
                    catch (Exception e)
                    {
                        FlipickError.DisplayErrorAsLongToast(context.getApplicationContext(), e.getMessage());
                    }
                }
                else
                {
                    v.setClickable(false);
                    ((MainActivity) context).DeleteSlideJobEntryDialog(v);
                }
            }
        });
        holder.imageView.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (oJobInfo.HasBookDownloaded)
                {
                    //if (v.getTag() == null)
//					{
//						FlipickError.DisplayErrorAsLongToast(((MainActivity) context).getApplicationContext(), "Reloading library");
//						((MainActivity) context).ReloadMe();
//						return;
//					}

                    //int Pos = (Integer) v.getTag();
                    int Pos = (Integer) v.getId();
                    JobInfo oJobInfo = jobdata.get(Pos);
                    ((MainActivity) context).LaunchPager(oJobInfo);
                }

            }
        });
        if (jobdata.get(position).JobThumbnailPath.contains("http") && !oJobInfo.IsSideLoaded.equalsIgnoreCase("Yes"))
        {
            String job_Thumbnail = "";
            try
            {
                job_Thumbnail = "file:///" + FlipickConfig.ContentRootPath + "/Images/" + jobdata.get(position).JobId + "/" + jobdata.get(position).JobId + ".jpg";

                Bitmap bitmap = getThumbnail(Uri.parse(job_Thumbnail), context);

                holder.imageView.setImageBitmap(bitmap);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
                // FlipickError.DisplayErrorAsLongToast(((MainActivity)
                // activity).getApplicationContext(), e.getMessage());
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        else if (oJobInfo.IsSideLoaded.equalsIgnoreCase("Yes") && !jobdata.get(position).JobThumbnailPath.equals(""))
        {
            String job_Thumbnail = "";
            try
            {
                job_Thumbnail = "file:///" + jobdata.get(position).JobThumbnailPath;

                Bitmap bitmap = getThumbnail(Uri.parse(job_Thumbnail), context);

                holder.imageView.setImageBitmap(bitmap);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
                // FlipickError.DisplayErrorAsLongToast(((MainActivity)
                // activity).getApplicationContext(), e.getMessage());
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        else
        {
            AssetManager assetManager = context.getAssets();
            InputStream istr;
            try
            {
                istr = assetManager.open("green_course_vertical.png");

                Bitmap bitmap = BitmapFactory.decodeStream(istr);
                holder.imageView.setImageBitmap(bitmap);
                istr.close();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return convertView;
    }

    public static Bitmap getThumbnail(Uri uri, Context context) throws FileNotFoundException, IOException
    {
        InputStream input = context.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;// optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) return null;

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > 250) ? (originalSize / 250) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true;// optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// optional
        input = context.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio)
    {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else return k;
    }

    public void changeDataSet(List<JobInfo> jobdata)
    {
        this.jobdata = jobdata;
        notifyDataSetChanged();
    }

    class UIHolder
    {
        ImageView imageView;
        TextView txt_title, txt_auther, txt_view, txt_download, txt_update, txt_sm_view;
        LinearLayout viewup_layout;
    }

}