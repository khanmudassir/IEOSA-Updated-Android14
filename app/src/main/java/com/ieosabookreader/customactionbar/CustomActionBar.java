package com.ieosabookreader.customactionbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ieosabookreader.R;

public class CustomActionBar extends LinearLayout
{
    LayoutInflater inflater;
    View view;
    TextView txt_title;
    Animation slidDownFromTop;
    Animation slidUpFromTop;
    RelativeLayout layout_txt_fontSetting, layout_btn_toc, layout_btn_library;
    private RelativeLayout layout_btn_all;
    private LinearLayout layout_navigation_button;
    private RelativeLayout layout_btn_fittopage, layout_btn_fittowidth, layout_btn_bookmark;
    ImageView img_btn_bookmark;

    public CustomActionBar(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.custom_actionbar, this, true);
        txt_title = (TextView) view.findViewById(R.id.txt_booktitle);
        layout_txt_fontSetting = (RelativeLayout) view.findViewById(R.id.layout_txt_fontSetting);
        layout_btn_library = (RelativeLayout) view.findViewById(R.id.layout_btn_library);
        layout_btn_toc = (RelativeLayout) view.findViewById(R.id.layout_btn_toc);
        layout_btn_all = (RelativeLayout) view.findViewById(R.id.layout_btn_all);
        layout_navigation_button = (LinearLayout) view.findViewById(R.id.layout_back_navigation);
        layout_btn_fittopage = (RelativeLayout) view.findViewById(R.id.layout_btn_fittopage);
        layout_btn_fittowidth = (RelativeLayout) view.findViewById(R.id.layout_btn_fittowidth);
        layout_btn_bookmark = (RelativeLayout) view.findViewById(R.id.layout_btn_bookmark);
        img_btn_bookmark = (ImageView) view.findViewById(R.id.img_btn_bookmark);
        view.setVisibility(View.INVISIBLE);
        initAnimation(context);
    }

    private void initAnimation(Context context)
    {
        slidDownFromTop = AnimationUtils.loadAnimation(context, R.anim.slide_top_down);
        slidUpFromTop = AnimationUtils.loadAnimation(context, R.anim.slide_top_up);

        slidDownFromTop.setAnimationListener(new AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {
            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                view.setVisibility(View.VISIBLE);
            }
        });

        slidUpFromTop.setAnimationListener(new AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                view.setVisibility(View.INVISIBLE);
            }
        });
    }

    public ImageView getImageButtonBookMark()
    {
        return img_btn_bookmark;
    }

    public void setImageButtonBookMark(int is_visible)
    {
        img_btn_bookmark.setVisibility(is_visible);
    }

    public void setBackgroundForBookMark(int image_resource)
    {
        img_btn_bookmark.setBackgroundResource(image_resource);
    }

    public RelativeLayout getLayoutBookmark()
    {
        return layout_btn_bookmark;
    }

    public void setLayoutBookmark(int is_visible)
    {
        layout_btn_bookmark.setVisibility(is_visible);
    }

    public RelativeLayout getLayoutButtonAll()
    {
        return layout_btn_all;
    }

    public void setLayoutButtonAll(int is_visible)
    {
        layout_btn_all.setVisibility(is_visible);
    }

    public void setTitleText(String title_text)
    {
        txt_title.setText(title_text);
    }

    public RelativeLayout layout_gettxt_fontsetting()
    {
        return layout_txt_fontSetting;
    }

    public RelativeLayout layout_get_fittopage()
    {
        return layout_btn_fittopage;
    }

    public RelativeLayout layout_get_fittowidth()
    {
        return layout_btn_fittowidth;
    }

    public RelativeLayout getLibraryLayout()
    {
        return layout_btn_library;
    }

    public RelativeLayout getTocLayout()
    {
        return layout_btn_toc;
    }

    public boolean isVisible()
    {
        if (view.getVisibility() == View.VISIBLE) return true;
        else return false;
    }

    /**
     * Call this method to show the action bar
     */
    public void showActionBar()
    {
        if (view.getVisibility() == View.INVISIBLE) view.startAnimation(slidDownFromTop);
    }

    public void hideActionBar()
    {
        if (view.getVisibility() == View.VISIBLE) view.startAnimation(slidUpFromTop);
    }

    /**
     * call this method to hide first button from ActionBar
     */
    public void hideLibraryButton()
    {
        layout_btn_library.setVisibility(View.INVISIBLE);
    }

    public void hideBookmark()
    {
        layout_btn_bookmark.setVisibility(View.INVISIBLE);
    }

    public void showBookmark()
    {
        layout_btn_bookmark.setVisibility(View.VISIBLE);
    }

    public void hideBtnAll()
    {
        layout_btn_all.setVisibility(View.GONE);
    }

    public void invisiblebtnAll()
    {
        layout_btn_all.setVisibility(View.INVISIBLE);
    }

    /**
     * call this method to show first button from ActionBar
     */
    public void showLibraryButton()
    {
        layout_btn_library.setVisibility(View.VISIBLE);
    }

    /**
     * call this method to hide second button from ActionBar
     */
    public void hideTOCButton()
    {
        layout_btn_toc.setVisibility(View.GONE);
    }

    public void hidelayoutfontsetting()
    {
        layout_txt_fontSetting.setVisibility(View.GONE);
    }

    public void showlayoutfontsetting()
    {
        layout_txt_fontSetting.setVisibility(View.VISIBLE);
    }

    public void hidelayoutfittopage()
    {
        layout_btn_fittopage.setVisibility(View.INVISIBLE);
    }

    public void hidelayoutfittowidth()
    {
        layout_btn_fittowidth.setVisibility(View.INVISIBLE);
    }

    /**
     * call this method to show second button from ActionBar
     */

    public void showfittowidth()
    {
        layout_btn_fittowidth.setVisibility(View.VISIBLE);
    }

    public void showfittopage()
    {
        layout_btn_fittopage.setVisibility(View.VISIBLE);
    }

    public void showTOCButton()
    {
        layout_btn_toc.setVisibility(View.VISIBLE);
    }
}