package com.ieosabookreader;

import java.net.URLDecoder;
import java.util.ArrayList;
import android.app.Activity;
import android.webkit.JavascriptInterface;

public class TocJavascriptInterface extends Activity // extend activity for runonUiThread
{

    Activity objtocBrowserfragment;

    TocJavascriptInterface(Activity temp)
    {
        objtocBrowserfragment = temp;
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

                    String[] PageAry = PageLink.split("#");

                    String PageName = URLDecoder.decode((PageAry[0]));//for space and 's
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
                                if (((String) FlipickStaticData.oJobInfo.PageXHTMLList.get(i).toLowerCase()).equals(PageName.toLowerCase()))
                                {
                                    String PageTOCLink = "";
                                    if (PageAry.length > 1) PageTOCLink = FlipickStaticData.oJobInfo.PageXHTMLList.get(i) + "#" + PageAry[1];
                                    else PageTOCLink = FlipickStaticData.oJobInfo.PageXHTMLList.get(i);
                                    ((TOCBrowserFragment) objtocBrowserfragment).GoToLink(PageTOCLink, FlipickStaticData.oJobInfo.JobId);
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
                                        if (PageAry.length > 1)
                                        {

                                            PageTOCLink = FlipickStaticData.oJobInfo.PageXHTMLList.get(i) + "#" + PageAry[1];
                                        }

                                        else
                                        {
                                            PageTOCLink = FlipickStaticData.oJobInfo.PageXHTMLList.get(i);
                                        }
                                        ((TOCBrowserFragment) objtocBrowserfragment).GoToLink(PageTOCLink, FlipickStaticData.oJobInfo.JobId);
                                        break;
                                    }
                                }
                                else
                                {
                                    String PageTOCLink = "";
                                    int lastindex = FlipickStaticData.oJobInfo.PageXHTMLList.size() - 1;
                                    PageTOCLink = FlipickStaticData.oJobInfo.PageXHTMLList.get(lastindex);
                                    ((TOCBrowserFragment) objtocBrowserfragment).GoToLink(PageTOCLink, FlipickStaticData.oJobInfo.JobId);

                                    break;
                                }
                            }
                        }
                    }
                    else
                    {
                        if (!FlipickStaticData.oJobInfo.Is_Free_Trial.equalsIgnoreCase("Y"))
                        {
                            for (int i = 0; i < FlipickStaticData.oJobInfo.PageXHTMLList.size(); i++)
                            {
                                if (((String) FlipickStaticData.oJobInfo.PageXHTMLList.get(i).toLowerCase()).equals(PageName.toLowerCase()))
                                {
                                    if (!LinkedIds.equals(""))
                                    {
                                        if (FlipickStaticData.oJobInfo.LinkedIDs.get(i).contains(LinkedIds))
                                        {
                                            ((TOCBrowserFragment) objtocBrowserfragment).GoToLink(i + "", FlipickStaticData.oJobInfo.JobId);
                                            break;
                                        }
                                    }
                                    else
                                    {
                                        ((TOCBrowserFragment) objtocBrowserfragment).GoToLink(i + "", FlipickStaticData.oJobInfo.JobId);
                                        break;
                                    }
                                }

                            }
                        }
                        else
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
                                                    ((TOCBrowserFragment) objtocBrowserfragment).GoToLink(i + "", FlipickStaticData.oJobInfo.JobId);
                                                    break;
                                                }
                                            }
                                            else
                                            {
                                                ((TOCBrowserFragment) objtocBrowserfragment).GoToLink(i + "", FlipickStaticData.oJobInfo.JobId);
                                                break;
                                            }
                                        }
                                    }
                                    else
                                    {
                                        int lastindex = FlipickStaticData.oJobInfo.PageXHTMLList.size() - 1;
                                        ((TOCBrowserFragment) objtocBrowserfragment).GoToLink(lastindex + "", FlipickStaticData.oJobInfo.JobId);
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
                                                    ((TOCBrowserFragment) objtocBrowserfragment).GoToLink(i + "", FlipickStaticData.oJobInfo.JobId);
                                                    break;
                                                }
                                            }
                                            else
                                            {
                                                ((TOCBrowserFragment) objtocBrowserfragment).GoToLink(i + "", FlipickStaticData.oJobInfo.JobId);
                                                break;
                                            }
                                        }
                                    }
                                    else
                                    {
                                        int lastindex = FlipickStaticData.oJobInfo.PageXHTMLList.size() - 1;
                                        ((TOCBrowserFragment) objtocBrowserfragment).GoToLink(lastindex + "", FlipickStaticData.oJobInfo.JobId);

                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    // Log.w("XXX", e.getMessage());
                }

            }
        });
    }

}