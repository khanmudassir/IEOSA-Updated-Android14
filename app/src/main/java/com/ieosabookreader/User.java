package com.ieosabookreader;

import android.content.Context;
import android.content.SharedPreferences;
import org.w3c.dom.Document;
import java.io.File;
import java.io.IOException;

public class User {
    public String UserName = "demouser@ieosa.com";
    public String Password = "password";
    public String UserId = "54321";
    public String old_UserName = "";
    public String old_Password = "";

    public String SessionId = "XX";
    public String WebsiteId = "";
    public String StoreID = "";
    public String FirstName = "";
    public String LastName = "";
    public String MobileNo = "1234567890";
    public String Logout = "";
    Context _context;

    public User(Context context)
    {
        this._context = context;
    }

    public boolean IsUserRegistered()
    {
        File file = new File(FlipickConfig.Part1ContentRootPath + "/UserInfo.xml");

        if (file.exists()) return true;
        else return false;
    }

    public boolean IsPreviousUserRegistered()
    {

        if ((UserName.trim()) != "" && (Password) != "") return true;
        else return false;
    }

    public void BackUpUserInfoFile() throws IOException
    {
        File file = new File(FlipickConfig.Part1ContentRootPath + "/UserInfo.xml");
        FlipickIO.CopyFile(FlipickConfig.Part1ContentRootPath + "/UserInfo.xml", FlipickConfig.Part1ContentRootPath + "/UserInfo_old.xml");
        Boolean deleted = file.delete();
    }

    /*public String DeRegisterDevice(User oUser) throws Throwable
    {
        MagentoServiceInterface oMagentoServiceInterface = new MagentoServiceInterface();
        try
        {
            String RequestXML = "<?xml version='1.0' encoding='UTF-8'?>";
            RequestXML = RequestXML + "<publishOrderList><UserEmail xsi:type='xsd:string' ><![CDATA[" + oUser.UserName.trim() + "]]></UserEmail>";
            RequestXML = RequestXML + "<Password  xsi:type='xsd:string'><![CDATA[" + oUser.Password + "]]></Password>";
            RequestXML = RequestXML + "<DeviceId  xsi:type='xsd:string'><![CDATA[" + FlipickStaticData.DeviceId + "]]></DeviceId>";
            RequestXML = RequestXML + "<ProductType xsi:type='xsd:string'><![CDATA[ALL]]></ProductType>";
            RequestXML = RequestXML + "<WebsiteId  xsi:type='xsd:string'><![CDATA[" + oUser.WebsiteId + "]]></WebsiteId>";
            RequestXML = RequestXML + "<OS  xsi:type='xsd:string'><![CDATA[Android]]></OS>";
            RequestXML = RequestXML + "<OSVersion xsi:type='xsd:string'><![CDATA[" + FlipickStaticData.OSVersion + "]]></OSVersion>";
            RequestXML = RequestXML + "<DeviceType xsi:type='xsd:string'><![CDATA[Mobile]]></DeviceType>";
            RequestXML = RequestXML + "<DeRegisterDevice xsi:type='xsd:string'><![CDATA[Yes]]></DeRegisterDevice></publishOrderList>";

            String ResponseXml = oMagentoServiceInterface.GetMagentoOrderList("publishOrderList", RequestXML);
            XMLParser parser = new XMLParser();

            Document DeRegisterDoc = parser.getDomElement(ResponseXml);
            String cdevice_status = DeRegisterDoc.getElementsByTagName("ErrorCode").item(0).getFirstChild().getNodeValue();
            cdevice_status = cdevice_status.replace("<![CDATA[", "");
            String device_status = cdevice_status.replace("]]>", "");

            String cErrMsg = DeRegisterDoc.getElementsByTagName("ErrorMessage").item(0).getFirstChild().getNodeValue();
            cErrMsg = cErrMsg.replace("<![CDATA[", "");
            String ErrMsg = cErrMsg.replace("]]>", "");

            if (!device_status.equals("109") && !device_status.equals("111"))
            {
                Exception myException = new Exception(ErrMsg);
                throw myException;
            }
        }
        catch (Throwable e)
        {
            throw e;
        }
        return "";
    }*/

    public void SetUserInfo()
    {
        SharedPreferences preferences = _context.getSharedPreferences(_context.getResources().getString(R.string.app_name), _context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putString("UserName", UserName.trim());
        editor.putString("Password", Password);
        editor.putString("UserId", UserId);
        editor.putString("SessionId", SessionId);
        editor.putString("WebsiteId", this.WebsiteId);
        editor.putString("StoreId", this.StoreID);
        editor.putString("Logout", "N");
        editor.commit();
    }

    public void GetUserInfo()
    {
        SharedPreferences preferences = _context.getSharedPreferences(_context.getResources().getString(R.string.app_name), _context.MODE_PRIVATE);

        UserName = preferences.getString("UserName", "");
        Password = preferences.getString("Password", "");
        UserId=preferences.getString("UserId", "");
        Logout = preferences.getString("Logout", "");
    }

    public void clearSharedPreferences()
    {
        SharedPreferences preferences = _context.getSharedPreferences(_context.getResources().getString(R.string.app_name), _context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    /*public String AuthenticateUser() throws Exception
    {
        HttpPost httpPost = null;
        try
        {
            httpPost = new HttpPost(FlipickConfig.ServiceUrl);
            httpPost.addHeader("Content-Type", " text/xml; charset=utf-8");
            httpPost.addHeader("SOAPAction", "http://mediawide.com/webservices/AuthenticateUser2");
        }
        catch (Throwable e)
        {
            return e.getMessage();
        }

        try
        {
            HttpEntity postEntity = new StringEntity(
                    "<soap:Envelope xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/'>  <soap:Body>  <AuthenticateUser2 xmlns='http://mediawide.com/webservices/'> <AuthenticateUser2Request> <LoginName>"
                            + UserName.trim() + "</LoginName> <Password>" + Password + "</Password> </AuthenticateUser2Request></AuthenticateUser2>  </soap:Body></soap:Envelope>");
            httpPost.setEntity(postEntity);
        }
        catch (UnsupportedEncodingException e)
        {
            return e.getMessage();
        }
        HttpResponse httpResponse = null;
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try
        {
            httpResponse = httpclient.execute(httpPost);
        }
        catch (Throwable e)
        {
            return e.getMessage();
        }

        int responseStatusCode = httpResponse.getStatusLine().getStatusCode();
        if (responseStatusCode != 200 && responseStatusCode != 500)
        {
            @SuppressWarnings("unused")
            String errorMsg = "Got SOAP response code " + responseStatusCode + " " + httpResponse.getStatusLine().getReasonPhrase();

            return "Error from AuthenticateUser2 method:" + errorMsg;
        }

        HttpEntity httpEntity = httpResponse.getEntity();
        InputStream is = null;
        try
        {
            is = httpEntity.getContent();
        }
        catch (IllegalStateException e)
        {
            FlipickError.DisplayErrorAsLongToast(_context, e.getMessage());
        }
        catch (IOException e)
        {
            FlipickError.DisplayErrorAsLongToast(_context, e.getMessage());
        }

        String responsexml = FlipickIO.convertStreamToString(is);
        XMLParser parser = new XMLParser();

        Document xmlDocResponse = parser.getDomElement(responsexml);
        Element XEResponse = (Element) xmlDocResponse.getElementsByTagName("Response").item(0);
        String Status = XEResponse.getAttribute("Status");

        if (Status.equals("SUCCESS") != true)
        {
            return "Authentication Failed:" + XEResponse.getTextContent();
        }
        XEResponse = null;
        xmlDocResponse = null;
        parser = null;
        httpPost = null;
        httpclient = null;
        httpResponse = null;
        return "";
    }*/
}