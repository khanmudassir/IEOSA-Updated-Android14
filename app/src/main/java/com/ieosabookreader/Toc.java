package com.ieosabookreader;

import android.content.Context;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Toc {
    public void UpdateTOC(String BookType, String PathOPS, ArrayList<String> originalPages, String TempPath, Context context, Document oXMLJobInfoDoc, String JobId) throws Exception
    {
        String OriginalPath = TempPath;
        if (!PathOPS.equals(""))
        {
            TempPath = TempPath + "/" + PathOPS;
        }
        String TOCPath = getTocPathFromManifest(PathOPS, OriginalPath);

        File TOCPathExist = new File(TOCPath);// for copy toc file with
        // different name
        if (TOCPathExist.exists())
        {
            FlipickIO.CopyFile(TOCPath, TempPath + "/FlipickToc.xhtml");
            TOCPath = TempPath + "/FlipickToc.xhtml";

            updateTocFile(TOCPath);
        }

        // ~~~~~~~~~~~~~~~~~~~~~~For Toc file remove <nav> tag comes two times
        Document doc = FlipickIO.GetDocumentFromFile(TOCPath);
        NodeList NL_TOCLinks = doc.getElementsByTagName("nav");

        if (NL_TOCLinks.getLength() > 1)
        {
            Element eNav = (Element) NL_TOCLinks.item(1);
            eNav.getParentNode().removeChild(eNav);
        }
        FlipickIO.SaveXMLFileFromDocument(TOCPath, doc);

        // ~~~~~~~~~~~~~~~~~~~~~~~End~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~for HyperLink Toc
        // Navigation~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        NodeList NL_HyperLinks = doc.getElementsByTagName("a");
        for (int n = 0; n < NL_HyperLinks.getLength(); n++)
        {
            Element element = (Element) NL_HyperLinks.item(n);
            String Value = element.getAttribute("href");
            originalPages.add(Value);
        }

        ArrayList<String> keySet = new ArrayList<String>();
        LinkedHashMap<String, ArrayList<String>> mergedMap = new LinkedHashMap<String, ArrayList<String>>();

        for (int i = 0; i < originalPages.size(); i++)
        {
            String currentSting = originalPages.get(i);
            if (currentSting.contains("#"))
            {
                String[] arrayOfIds = currentSting.split("#");
                keySet.add(arrayOfIds[0]);
            }
            else
            {
                keySet.add(currentSting);
            }
        }

        for (String curStr : keySet)
        {
            ArrayList<String> valueList = new ArrayList<String>();
            try
            {
                for (int i = 0; i < originalPages.size(); i++)
                {
                    String currentSting = originalPages.get(i);
                    if (currentSting.startsWith(curStr))
                    {
                        if (currentSting.contains("#"))
                        {
                            String[] arrayOfIds = currentSting.split("#");
                            valueList.add(arrayOfIds[1]);
                        }
                        else
                        {
                            valueList.add("");
                        }
                    }
                }

                mergedMap.put(curStr, valueList);
            }
            catch (Exception e)
            {

                Log.e("error", e.getMessage());
            }
        }

        NodeList NL_Page = oXMLJobInfoDoc.getElementsByTagName("Page");
        for (int n = 0; n < NL_Page.getLength(); n++)
        {
            Element element = (Element) NL_Page.item(n);

            String Value = ((Node) NL_Page.item(n)).getFirstChild().getNodeValue();
            for (String key : mergedMap.keySet())
            {
                String keyValue = mergedMap.get(key) + "";
                int start = keyValue.indexOf("[");
                int end = keyValue.indexOf("]");
                keyValue = keyValue.substring(start + 1, end);

                if (key.equals(Value))
                {
                    element.setAttribute("LinkedIds", keyValue);
                    if (element.hasAttribute("LinkedIds")) element.getAttributes();
                }
            }
        }

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~End HyperLink Toc
        // Navigation~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~Add style in Toc
        // Page~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        if (BookType.equals("REFLOWABLE") && BookType.equals("VERTICALSCROLL"))
        {
            float deviceWidth = FlipickConfig.getDeviceWidth(context);
            float deviceHeight = FlipickConfig.getDeviceHeight(context);
            File list = new File(TOCPath);// "\"ROM\""
            int Margin = (int) (deviceWidth * 4 / 100);
            if ((Margin % 2) != 0) Margin = Margin + 1;
            int ColumGap = Margin * 2;
            deviceWidth = deviceWidth - (Margin * 2);
            deviceHeight = deviceHeight - (Margin * 2);
            String HTMLData = FlipickIO.ReadHTMLFileText(list.getAbsolutePath());
            String BodyStyle = "padding:0px !important;margin:" + Margin + "px" + " !important";
            HTMLData = AddStyleIntoBodyTag(HTMLData, BodyStyle, "", "");
            FlipickIO.WriteHTMLFileText(list.getAbsolutePath(), HTMLData);
        }
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~End Add style in Toc
        // Page~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    }

    public void updateTocFile(String TOCPath) throws IOException
    {
        String HTMLData = FlipickIO.ReadHTMLFileText(TOCPath);
        int indexHead = HTMLData.indexOf("<head");
        int indexEndHead = HTMLData.indexOf("</head>");
        int FindTitle = HTMLData.indexOf("<title>");
        int FindEndTitle = HTMLData.indexOf("</title>");
        String TitleContent = HTMLData.substring(FindTitle + 7, FindEndTitle);
        String FindHeadHead = HTMLData.substring(indexHead, indexEndHead + 7);
        HTMLData = HTMLData
                .replace(
                        FindHeadHead,
                        "<head><title>"
                                + TitleContent
                                + "</title><link type=\"text/css\" rel=\"stylesheet\" href=\"file:///android_asset/toc.css\"/> <style type='text/css'>body{font-size:1.0em}</style><script type=\"text/javascript\" src=\"file:///android_asset/PageScript/PageScript.js\"/> </head>");
        int indexBody = HTMLData.indexOf("<body");
        String Startbody = HTMLData.substring(HTMLData.indexOf("<body"));

        String FindBody = HTMLData.substring(indexBody, Startbody.indexOf(">") + indexBody + 1);
        int indexEndBody = HTMLData.indexOf("</body>");
        String BodyContent = HTMLData.substring(indexBody, indexEndBody);
        if (!BodyContent.contains("<nav"))
        {
            HTMLData = HTMLData.replace(FindBody, "<body><nav xmlns:epub=\"http://www.idpf.org/2007/ops\" epub:type=\"toc\" id=\"toc\">");
            HTMLData = HTMLData.replace("</body>", "</nav></body>");
        }
        FlipickIO.WriteHTMLFileText(TOCPath, HTMLData);
    }

    public String getTocPathFromManifest(String PathOPS, String TempPath) throws Exception
    {
        Document oXMLContainerDoc = FlipickIO.GetDocumentFromFile(TempPath + "/META-INF/container.xml");

        Element E_RootFile = (Element) oXMLContainerDoc.getElementsByTagName("rootfile").item(0);
        String OEBPSPath = E_RootFile.getAttribute("full-path");

        String ContentPath = OEBPSPath;
        oXMLContainerDoc = null;

        Document oPackageDoc = FlipickIO.GetDocumentFromFile(TempPath + "/" + ContentPath);// +

        NodeList E_Items = oPackageDoc.getElementsByTagName("item");
        String Toc_href = "";
        for (int j = 0; j < E_Items.getLength(); j++)
        {
            Element E_Item = (Element) E_Items.item(j);
            if (E_Item.getAttribute("media-type") != null)
            {

                if (E_Item.getAttribute("media-type").equals("application/xhtml+xml") || E_Item.getAttribute("media-type").equals("application/x-dtbncx+xml"))
                {
                    if (E_Item.getAttribute("properties").equals("nav"))
                    {
                        Toc_href = URLDecoder.decode(E_Item.getAttribute("href"), "UTF-8");
                        break;
                    }
                    else if (E_Item.getAttribute("id").equals("ncx"))
                    {
                        Toc_href = URLDecoder.decode(E_Item.getAttribute("href"), "UTF-8");
                        break;
                    }
                }
                else continue;
            }
            else continue;
        }

        String TOCPath = "";
        String OriginalPath = TempPath;
        //
        if (!PathOPS.equals(""))
        {
            TempPath = TempPath + "/" + PathOPS;
        }

        if (Toc_href.contains(".ncx"))
        {
            createDummyToc(PathOPS, OriginalPath);
            TOCPath = TempPath + "/toc.xhtml";
        }
        else
        {
            TOCPath = TempPath + "/" + Toc_href;
        }
        return TOCPath;
    }

    public String AddStyleIntoBodyTag(String HTML, String BodyStyle, String BodyContainerStart, String BodyContainerEnd)
    {

        if (HTML.indexOf("<body>") == -1)
        {
            int b_startindex = HTML.indexOf("<body");
            if (b_startindex > 0)
            {
                String BeforeBody = HTML.substring(0, b_startindex);

                String WithBody = HTML.substring(b_startindex, HTML.length());

                int bodyEndIntex = WithBody.indexOf(">");
                String BodyTag = WithBody.substring(0, bodyEndIntex + 1);

                if (BodyTag.indexOf("style") > 0)
                {
                    BodyTag = BodyTag.replace("style='", "style='" + BodyStyle);
                    BodyTag = BodyTag.replace("style=\"", "style=\"" + BodyStyle);
                }
                else
                {

                    BodyTag = BodyTag.replace("<body", "<body style='" + BodyStyle + "'");
                }

                String AfterBody = WithBody.substring(WithBody.indexOf(">") + 1);

                if (BodyContainerStart != "")
                {
                    AfterBody = BodyContainerStart + AfterBody;
                    AfterBody = AfterBody.replace("</body>", BodyContainerEnd + "</body>");

                }
                HTML = BeforeBody + BodyTag + AfterBody;

            }
            // else
            // {
            //
            // }
        }
        else
        {
            HTML = HTML.replace("<body>", "<body>" + BodyContainerStart);
            HTML = HTML.replace("</body>", BodyContainerEnd + "</body>");
            HTML = HTML.replace("<body>", "<body style='" + BodyStyle + "'>");
        }

        return HTML;
    }

    public void createDummyToc(String PathOPS, String TempPath) throws Exception
    {
        ArrayList<String> OtempNavLabelText = new ArrayList<String>();
        ArrayList<String> OtempContent = new ArrayList<String>();
        String TOCPath = "";
        if (FlipickStaticData.OEBPSPath.equals(""))
        {
            TOCPath = TempPath + "/toc.ncx";
        }
        else
        {
            TOCPath = TempPath + "/" + FlipickStaticData.OEBPSPath + "/toc.ncx";
        }
        Document doc = FlipickIO.GetDocumentFromFile(TOCPath);
        NodeList NL_DocTitle = doc.getElementsByTagName("docTitle");
        Element element = (Element) NL_DocTitle.item(0);
        String DocTitle = element.getElementsByTagName("text").item(0).getFirstChild().getNodeValue();
        NodeList NL_NavPoint = doc.getElementsByTagName("navPoint");

        for (int i = 0; i < NL_NavPoint.getLength(); i++)
        {
            // get all content and text from navpoint and store into temp array
            NodeList NL_Content = doc.getElementsByTagName("content");
            Element elementContent = (Element) NL_Content.item(i);
            String content = elementContent.getAttribute("src");
            OtempContent.add(URLDecoder.decode(content, "UTF-8").substring(URLDecoder.decode(content, "UTF-8").lastIndexOf("/") + 1));

            NodeList NL_navLabel = doc.getElementsByTagName("navLabel");
            String nodevalue = ((Element) NL_navLabel.item(i)).getElementsByTagName("text").item(0).getFirstChild().getNodeValue();
            OtempNavLabelText.add(URLDecoder.decode(nodevalue, "UTF-8"));
        }
        String HtmlContent = "";
        String strHtml = "<?xml version='1.0' encoding='utf-8'?><html xmlns=\"http://www.w3.org/1999/xhtml\"><head>";
        String strTitleHead = "<title>" + DocTitle + "</title><script type=\"text/javascript\" src=\"file:///android_asset/PageScript/PageScript.js\"/></head>";
        String strBody = "<body><nav xmlns:epub=\"http://www.idpf.org/2007/ops\" epub:type=\"toc\" id=\"toc\"><ol>";

        String strLI = "";
        for (int i = 0; i < NL_NavPoint.getLength(); i++)
        {
            strLI += "<li><a href=\"" + OtempContent.get(i) + "\">" + OtempNavLabelText.get(i) + "</a></li>";
        }
        HtmlContent = strHtml + strTitleHead + strBody + strLI + "</ol></nav></body></html>";
        try
        {
            File file = null;
            if (!PathOPS.equals(""))
            {
                file = new File(TempPath + "/" + PathOPS + "/toc.xhtml");
            }
            else
            {
                file = new File(TempPath + "/toc.xhtml");
            }
            // If file does not exists, then create it
            if (!file.exists())
            {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(HtmlContent);
            bw.close();

        }
        catch (IOException e)
        {
            e.printStackTrace();

        }

    }
}
