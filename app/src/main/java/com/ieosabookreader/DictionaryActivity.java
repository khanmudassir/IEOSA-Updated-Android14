package com.ieosabookreader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class DictionaryActivity extends AppCompatActivity {
    WebView browser_dictionary;
    ImageView btn_close;
    String selectedText = "";
    ProgressBar dictprogress_bar;

    @SuppressLint(
            { "SetJavaScriptEnabled", "NewApi" })
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dictionaryactivity);


        Bundle oBundle = getIntent().getExtras();
        if (oBundle != null && oBundle.getString("SelectedText") != null)
        {
            selectedText = oBundle.getString("SelectedText");
        }
        dictprogress_bar = (ProgressBar) findViewById(R.id.dictprogress_bar);
        browser_dictionary = (WebView) findViewById(R.id.browser_dictionary);
        btn_close = (ImageView) findViewById(R.id.btn_close);

        browser_dictionary.getSettings().setJavaScriptEnabled(true);
        browser_dictionary.getSettings().setLoadWithOverviewMode(true);
        browser_dictionary.getSettings().setUseWideViewPort(true);
        browser_dictionary.getSettings().setAllowUniversalAccessFromFileURLs(true);
        browser_dictionary.setWebViewClient(new WebClient());
        String wikiurl = "https://en.wikipedia.org/wiki/" + selectedText;
        //String wikiurl ="http://pocket.dict.cc/?s=" +selectedText;//dictionary
        //String wikiurl ="https://translate.google.com/?hl=en#auto/en/"+ selectedText;// for translate
        //String wikiurl ="https://hi.m.wikipedia.org/wiki/" +selectedText; //marathi
        browser_dictionary.loadUrl(wikiurl);

        btn_close.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    public class WebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
        {

            dictprogress_bar.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url)
        {

            dictprogress_bar.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        browser_dictionary.onPause();//if it's not webview in your case then add the method name you want pause when user device is pause
    }


    @Override
    protected void onResume() {
        super.onResume();
        browser_dictionary.onResume();//same as here if it's not webview then add the method name you want to resume when user resume their device
    }
}