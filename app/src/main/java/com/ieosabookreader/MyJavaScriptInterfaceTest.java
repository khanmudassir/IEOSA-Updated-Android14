package com.ieosabookreader;

import java.net.URLDecoder;
import java.util.ArrayList;

import android.webkit.JavascriptInterface;

import android.app.Activity;
import android.util.Log;

public class MyJavaScriptInterfaceTest extends Activity
{
    Activity objFlipickPageViewer;
    Activity CurrentActivity;
    Activity objtocBrowserfragment;

    ConnectionDetector oCDetector;

    MyJavaScriptInterfaceTest(Activity objFlipickPageViewer1, Activity TocbrowserActivity)
    {
        objFlipickPageViewer = objFlipickPageViewer1;
        objtocBrowserfragment = TocbrowserActivity;
        if (objFlipickPageViewer != null) CurrentActivity = objFlipickPageViewer;
        if (objtocBrowserfragment != null) CurrentActivity = objtocBrowserfragment;
    }

    @JavascriptInterface
    public String getImagePath()
    {
        return "file://" + FlipickConfig.ContentRootPath + "/" + FlipickStaticData.JobId + "/OPS/images/";
    }

    @JavascriptInterface
    public void ShowActionBar()
    {
        runOnUiThread(new Runnable()
        {
            @JavascriptInterface
            public void run()
            {
                try
                {
                    ((flipickpageviewer) objFlipickPageViewer).ShowHideActionBar();
                }
                catch (Exception e)
                {
                    // Log.w("XXX", e.getMessage());
                }
            }
        });
    }

    @JavascriptInterface
    public void setColumnNumber(String ColNumber)
    {
        ((flipickpageviewer) objFlipickPageViewer).setColumnNumber(ColNumber);
    }

    @JavascriptInterface
    public void ShowPageNavigationBar()
    {
        runOnUiThread(new Runnable()
        {
            @JavascriptInterface
            public void run()
            {
                try
                {
                    ((flipickpageviewer) objFlipickPageViewer).ShowPageNavigationBar();
                }
                catch (Exception e)
                {
                    // Log.w("XXX", e.getMessage());
                }
            }
        });
    }

    @JavascriptInterface
    public void GetParentViewURL()
    {
        ((flipickpageviewer) objFlipickPageViewer).GetParentViewURL();
    }

    @JavascriptInterface
    public void getUserSelection(String SelectedText)
    {
        ((flipickpageviewer) objFlipickPageViewer).getUserSelection(SelectedText);
    }

    @JavascriptInterface
    public void DisplayErrorMessage(String Message) throws Exception
    {

        ((flipickpageviewer) objFlipickPageViewer).DisplayErrorMessage(Message);
        return;
    }

    @JavascriptInterface
    public void OpenBookryWGT(String Value)
    {
        ((flipickpageviewer) objFlipickPageViewer).OpenBookryWGT(Value);

    }

//	@JavascriptInterface
//	public void OpenArticulate(String Value)
//	{
//		((flipickpageviewer) objFlipickPageViewer).OpenArticulate(Value);
//
//	}

    @JavascriptInterface
    public void DisplayImageInPopup(final String ImagePath)
    {
        runOnUiThread(new Runnable()
        {
            @JavascriptInterface
            public void run()
            {
                try
                {
                    ((flipickpageviewer) objFlipickPageViewer).DisplayImageInPopup(ImagePath);
                }
                catch (Exception e)
                {
                    DisplayErrorAsUIThread(e.getMessage());
                }
                catch (Throwable e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private void DisplayErrorAsUIThread(final String Message)
    {
        try
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    {
                        FlipickError.DisplayErrorAsLongToast(objFlipickPageViewer.getApplicationContext(), Message);
                        return;
                    }
                }
            });
        }
        finally
        {

        }
    }

    @JavascriptInterface
    public void DisplayToastFromJSCall(final String oMessage) throws Exception
    {
        try
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    {
                        FlipickError.DisplayErrorAsLongToast(CurrentActivity.getApplicationContext(), oMessage);
                        return;
                    }
                }
            });
        }
        finally
        {

        }
    }

    @JavascriptInterface
    public void DisplayUrlInDefaultBrowser(String url)
    {
        ((flipickpageviewer) objFlipickPageViewer).DisplayURL(url);
    }

    @JavascriptInterface
    public void PageNavigate(final String PageLink)
    {
        runOnUiThread(new Runnable()
        {
            @JavascriptInterface
            public void run()
            {
                try
                {
                    ((flipickpageviewer) objFlipickPageViewer).hideActionBar();
                    ((flipickpageviewer) objFlipickPageViewer).hideNavigation();
                    String[] PageAry = PageLink.split("#");

                    // for space and 's
                    String PageName = URLDecoder.decode((PageAry[0]));
                    PageName = PageName.replace("file:////", "file:///");

                    String LinkedIds = "";
                    if (PageAry.length > 1)
                    {
                        LinkedIds = PageAry[1];
                    }
                    if (FlipickStaticData.BookType.equals("VERTICALSCROLL"))
                    {
                        if (!FlipickStaticData.oJobInfo.Is_Free_Trial.equalsIgnoreCase("Y"))
                        {
                            for (int i = 0; i < FlipickStaticData.oJobInfo.PageXHTMLList.size(); i++)
                            {
                                String currentPage = FlipickStaticData.oJobInfo.PageXHTMLList.get(i).split("#")[0];
                                if (((String) currentPage.toLowerCase()).equals(PageName.toLowerCase()))
                                {
                                    String PageTOCLink = "";
                                    if (PageAry.length > 1) PageTOCLink = currentPage + "#" + PageAry[1];
                                    else PageTOCLink = currentPage;

                                    FlipickStaticData.oJobInfo.PageXHTMLList.set(i, PageTOCLink);
                                    ((flipickpageviewer) objFlipickPageViewer).SetPage(i);
                                    break;
                                }
                            }
                        }
                        else
                        {
                            for (int i = 0; i < FlipickStaticData.oJobInfo.PageXHTMLList.size(); i++)
                            {
                                if (FlipickStaticData.MaxAllowedFixLayoutPages > i)
                                {
                                    if (((String) FlipickStaticData.oJobInfo.PageXHTMLList.get(i).toLowerCase()).equals(PageName.toLowerCase()))
                                    {
                                        String PageTOCLink = "";
                                        if (PageAry.length > 1) PageTOCLink = FlipickStaticData.oJobInfo.PageXHTMLList.get(i) + "#" + PageAry[1];
                                        else PageTOCLink = FlipickStaticData.oJobInfo.PageXHTMLList.get(i);

                                        FlipickStaticData.oJobInfo.PageXHTMLList.set(i, PageTOCLink);
                                        ((flipickpageviewer) objFlipickPageViewer).SetPage(i);
                                        break;
                                    }
                                }
                                else
                                {

                                    int lastindex = FlipickStaticData.oJobInfo.PageXHTMLList.size() - 1;
                                    ((flipickpageviewer) objFlipickPageViewer).SetPage(lastindex);

                                    break;
                                }
                            }
                        }

                    }
                    else
                    {

                        if (PageName.indexOf("/__fp") != -1)
                        {
                            if (FlipickStaticData.oJobInfo.Is_Free_Trial.equalsIgnoreCase("Y"))
                            {
                                int count = 0;
                                ArrayList<String> otempList = new ArrayList<String>();
                                for (int i = 0; i < FlipickStaticData.oJobInfo.ReflowNewFileNames.size(); i++)
                                {
                                    if (FlipickStaticData.BookType.equals("REFLOWABLE"))
                                    {
                                        // for free trial user. counter for get
                                        // actual 5
                                        // pages.
                                        String tempPageName = (String) FlipickStaticData.oJobInfo.ReflowNewFileNames.get(i);
                                        int index1 = tempPageName.indexOf("page");
                                        tempPageName = tempPageName.substring(index1);

                                        if (!otempList.contains(tempPageName))
                                        {
                                            otempList.add(tempPageName);
                                            count++;
                                        }

                                        if (FlipickStaticData.MaxAllowedAdaptivePages >= count)
                                        {

                                            String NewReflowableNames = FlipickStaticData.oJobInfo.ReflowNewFileNames.get(i);
                                            if (PageName.startsWith("file://"))
                                            {
                                                NewReflowableNames = "file://" + NewReflowableNames;

                                            }

                                            int index = NewReflowableNames.indexOf("page");
                                            NewReflowableNames = NewReflowableNames.substring(index);

                                            int indexPage = PageName.indexOf("page");
                                            PageName = PageName.substring(indexPage);

                                            if ((NewReflowableNames.toLowerCase()).equals(PageName.toLowerCase()))
                                            {
                                                if (!LinkedIds.equals(""))
                                                {
                                                    if (FlipickStaticData.oJobInfo.LinkedIDs.get(i).contains(LinkedIds))
                                                    {
                                                        ((flipickpageviewer) objFlipickPageViewer).SetPage(i);
                                                        break;
                                                    }
                                                }
                                                else
                                                {

                                                    ((flipickpageviewer) objFlipickPageViewer).SetPage(i);
                                                    break;
                                                }
                                            }
                                        }
                                        else
                                        {
                                            int lastindex = FlipickStaticData.oJobInfo.ReflowNewFileNames.size() - 1;
                                            ((flipickpageviewer) objFlipickPageViewer).SetPage(lastindex);
                                            break;
                                        }
                                    }
                                    else
                                    {
                                        if (FlipickStaticData.MaxAllowedFixLayoutPages > i)
                                        {
                                            String NewReflowableNames = FlipickStaticData.oJobInfo.ReflowNewFileNames.get(i);
                                            if (PageName.startsWith("file://"))
                                            {
                                                NewReflowableNames = "file://" + NewReflowableNames;

                                            }

                                            int index = NewReflowableNames.indexOf("page");
                                            NewReflowableNames = NewReflowableNames.substring(index);

                                            int indexPage = PageName.indexOf("page");
                                            PageName = PageName.substring(indexPage);

                                            if ((NewReflowableNames.toLowerCase()).equals(PageName.toLowerCase()))
                                            {
                                                if (!LinkedIds.equals(""))
                                                {
                                                    if (FlipickStaticData.oJobInfo.LinkedIDs.get(i).contains(LinkedIds))
                                                    {
                                                        ((flipickpageviewer) objFlipickPageViewer).SetPage(i);
                                                        break;
                                                    }
                                                }
                                                else
                                                {

                                                    ((flipickpageviewer) objFlipickPageViewer).SetPage(i);
                                                    break;
                                                }
                                            }
                                        }
                                        else
                                        {
                                            int lastindex = FlipickStaticData.oJobInfo.ReflowNewFileNames.size() - 1;
                                            ((flipickpageviewer) objFlipickPageViewer).SetPage(lastindex);
                                            break;
                                        }
                                    }
                                }
                            }
                            else
                            {
                                for (int i = 0; i < FlipickStaticData.oJobInfo.ReflowNewFileNames.size(); i++)
                                {
                                    String NewReflowableNames = FlipickStaticData.oJobInfo.ReflowNewFileNames.get(i);
                                    if (PageName.startsWith("file://"))
                                    {
                                        NewReflowableNames = "file://" + NewReflowableNames;

                                    }

                                    int index = NewReflowableNames.indexOf("page");
                                    NewReflowableNames = NewReflowableNames.substring(index);

                                    int indexPage = PageName.indexOf("page");
                                    PageName = PageName.substring(indexPage);

                                    if ((NewReflowableNames.toLowerCase()).equals(PageName.toLowerCase()))
                                    {
                                        if (!LinkedIds.equals(""))
                                        {
                                            if (FlipickStaticData.oJobInfo.LinkedIDs.get(i).contains(LinkedIds))
                                            {
                                                ((flipickpageviewer) objFlipickPageViewer).SetPage(i);
                                                break;
                                            }
                                        }
                                        else
                                        {

                                            ((flipickpageviewer) objFlipickPageViewer).SetPage(i);
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                        else
                        {
                            if (FlipickStaticData.oJobInfo.Is_Free_Trial.equalsIgnoreCase("Y"))
                            {
                                int count = 0;
                                ArrayList<String> otempList = new ArrayList<String>();
                                for (int i = 0; i < FlipickStaticData.oJobInfo.PageXHTMLList.size(); i++)
                                {
                                    if (FlipickStaticData.BookType.equals("REFLOWABLE"))
                                    {
                                        // for free trial user. counter for get
                                        // actual 5
                                        // pages.
                                        String tempPageName = (String) FlipickStaticData.oJobInfo.PageXHTMLList.get(i);

                                        if (!otempList.contains(tempPageName))
                                        {
                                            otempList.add(FlipickStaticData.oJobInfo.PageXHTMLList.get(i));
                                            count++;
                                        }

                                        if (FlipickStaticData.MaxAllowedAdaptivePages >= count)
                                        {
                                            if (((String) FlipickStaticData.oJobInfo.PageXHTMLList.get(i).toLowerCase()).equals(PageName.toLowerCase()))
                                            {
                                                if (!LinkedIds.equals(""))
                                                {
                                                    if (FlipickStaticData.oJobInfo.LinkedIDs.get(i).contains(LinkedIds))
                                                    {
                                                        String PageTOCLink = "";
                                                        if (PageAry.length > 1) PageTOCLink = FlipickStaticData.oJobInfo.PageXHTMLList.get(i) + "#" + PageAry[1];
                                                        else PageTOCLink = FlipickStaticData.oJobInfo.PageXHTMLList.get(i);

                                                        ((flipickpageviewer) objFlipickPageViewer).SetPage(i);
                                                        break;
                                                    }
                                                }
                                                else
                                                {

                                                    String PageTOCLink = "";
                                                    if (PageAry.length > 1) PageTOCLink = FlipickStaticData.oJobInfo.PageXHTMLList.get(i) + "#" + PageAry[1];
                                                    else PageTOCLink = FlipickStaticData.oJobInfo.PageXHTMLList.get(i);

                                                    ((flipickpageviewer) objFlipickPageViewer).SetPage(i);
                                                    break;
                                                }
                                            }
                                        }
                                        else
                                        {
                                            int lastindex = FlipickStaticData.oJobInfo.PageXHTMLList.size() - 1;
                                            ((flipickpageviewer) objFlipickPageViewer).SetPage(lastindex);
                                            break;
                                        }
                                    }
                                    else
                                    {
                                        if (FlipickStaticData.MaxAllowedFixLayoutPages > i)
                                        {
                                            if (((String) FlipickStaticData.oJobInfo.PageXHTMLList.get(i).toLowerCase()).equals(PageName.toLowerCase()))
                                            {
                                                if (!LinkedIds.equals(""))
                                                {
                                                    if (FlipickStaticData.oJobInfo.LinkedIDs.get(i).contains(LinkedIds))
                                                    {
                                                        String PageTOCLink = "";
                                                        if (PageAry.length > 1) PageTOCLink = FlipickStaticData.oJobInfo.PageXHTMLList.get(i) + "#" + PageAry[1];
                                                        else PageTOCLink = FlipickStaticData.oJobInfo.PageXHTMLList.get(i);

                                                        ((flipickpageviewer) objFlipickPageViewer).SetPage(i);
                                                        break;
                                                    }
                                                }
                                                else
                                                {

                                                    String PageTOCLink = "";
                                                    if (PageAry.length > 1) PageTOCLink = FlipickStaticData.oJobInfo.PageXHTMLList.get(i) + "#" + PageAry[1];
                                                    else PageTOCLink = FlipickStaticData.oJobInfo.PageXHTMLList.get(i);

                                                    ((flipickpageviewer) objFlipickPageViewer).SetPage(i);
                                                    break;
                                                }
                                            }
                                        }
                                        else
                                        {

                                            int lastindex = FlipickStaticData.oJobInfo.PageXHTMLList.size() - 1;
                                            ((flipickpageviewer) objFlipickPageViewer).SetPage(lastindex);
                                            break;

                                        }
                                    }
                                }
                            }
                            else
                            {
                                for (int i = 0; i < FlipickStaticData.oJobInfo.PageXHTMLList.size(); i++)
                                {
                                    if (((String) FlipickStaticData.oJobInfo.PageXHTMLList.get(i).toLowerCase()).equals(PageName.toLowerCase()))
                                    {
                                        if (!LinkedIds.equals(""))
                                        {
                                            if (FlipickStaticData.oJobInfo.LinkedIDs.get(i).contains(LinkedIds))
                                            {
                                                String PageTOCLink = "";
                                                if (PageAry.length > 1) PageTOCLink = FlipickStaticData.oJobInfo.PageXHTMLList.get(i) + "#" + PageAry[1];
                                                else PageTOCLink = FlipickStaticData.oJobInfo.PageXHTMLList.get(i);

                                                ((flipickpageviewer) objFlipickPageViewer).SetPage(i);
                                                break;
                                            }
                                        }
                                        else
                                        {

                                            String PageTOCLink = "";
                                            if (PageAry.length > 1) PageTOCLink = FlipickStaticData.oJobInfo.PageXHTMLList.get(i) + "#" + PageAry[1];
                                            else PageTOCLink = FlipickStaticData.oJobInfo.PageXHTMLList.get(i);

                                            ((flipickpageviewer) objFlipickPageViewer).SetPage(i);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
                catch (Exception e)
                {
                    Log.w("XXX", e.getMessage());
                }

            }
        });
    }

    @JavascriptInterface
    public void SavePagewithHightedText(String OutHeadHTML, String OutBodyHTML, String SelectedText, String SelectParaId)
    {
        ((flipickpageviewer) objFlipickPageViewer).SavePagewithHightedText(OutHeadHTML, OutBodyHTML, SelectedText, SelectParaId);
    }

    @JavascriptInterface
    public void SavePagewithNotesText(String OutHeadHTML, String OutBodyHTML, String ImgId)
    {
        ((flipickpageviewer) objFlipickPageViewer).SavePagewithNotesText(OutHeadHTML, OutBodyHTML, ImgId);
    }

    @JavascriptInterface
    public void showNotesPallet(String ImageId)
    {
        ((flipickpageviewer) objFlipickPageViewer).showNotesPallet(ImageId);
    }

    @JavascriptInterface
    public void RemoveNotes(String SelectParaId, String OuterHeadHtml, String OuterBodyHtml)
    {
        ((flipickpageviewer) objFlipickPageViewer).RemoveNotesText(SelectParaId, OuterHeadHtml, OuterBodyHtml);
    }

    @JavascriptInterface
    public void RemoveHighLight(String SelectParaId, String OuterHeadHtml, String OuterBodyHtml)
    {
        ((flipickpageviewer) objFlipickPageViewer).RemoveHighlightText(SelectParaId, OuterHeadHtml, OuterBodyHtml);
    }

    @JavascriptInterface
    public void HideHighlight()
    {
        runOnUiThread(new Runnable()
        {
            @JavascriptInterface
            public void run()
            {
                try
                {
                    ((flipickpageviewer) objFlipickPageViewer).ShowHideHighlight();
                }
                catch (Exception e)
                {
                    // Log.w("XXX", e.getMessage());
                }
            }
        });
    }
}