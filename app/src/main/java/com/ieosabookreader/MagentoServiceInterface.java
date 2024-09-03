package com.ieosabookreader;




public class MagentoServiceInterface {
    String website_id = "";
    String store_name = "";
    String store_id = "";
    String group_id = "";
    public String EPUBUrl = "";
    public String JobTitle = "  ";
    public String JobUrl = "";
    public String JobId = "";
    public String ProductUpdated = "";

    /*public String GetMagentoOrderList(String MethodName, String RequestXML) throws Throwable
    {
        String ResponseXML = "";
        try
        {
            HttpPost httpPost = null;
            String httpPostUrl = FlipickConfig.ServiceUrl + "/" + MethodName;

            httpPost = new HttpPost(httpPostUrl);
            httpPost.addHeader("Content-Type", "text/xml; charset=utf-8");
            HttpEntity postEntity;

            postEntity = new StringEntity(RequestXML);
            httpPost.setEntity(postEntity);

            HttpResponse httpResponse = null;
            DefaultHttpClient httpclient = new DefaultHttpClient();
            httpResponse = httpclient.execute(httpPost);
            final int responseStatusCode = httpResponse.getStatusLine().getStatusCode();
            final String status = httpResponse.getStatusLine().getReasonPhrase();

            if (responseStatusCode != 200 && responseStatusCode != 500)
            {
                String errorMsg = "Got SOAP response code " + responseStatusCode + " " + status;
                Exception myException = new Exception(errorMsg);
                throw myException;
            }

            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream is = null;
            is = httpEntity.getContent();
            ResponseXML = FlipickIO.convertStreamToString(is);
            return ResponseXML;
        }
        catch (Throwable e)
        {
            throw e;
        }
    }*/

	/*public String ServiceDonwloadConfirmation(String MethodName, String RequestXML) throws Throwable
	{
		String ResponseXML = "";
		try
		{
			HttpPost httpPost = null;
			String httpPostUrl = FlipickConfig.ServiceUrl + "/" + MethodName;
			httpPost = new HttpPost(httpPostUrl);
			httpPost.addHeader("Content-Type", "text/xml; charset=utf-8");
			HttpEntity postEntity;

			postEntity = new StringEntity(RequestXML);
			httpPost.setEntity(postEntity);

			HttpResponse httpResponse = null;
			DefaultHttpClient httpclient = new DefaultHttpClient();
			httpResponse = httpclient.execute(httpPost);
			final int responseStatusCode = httpResponse.getStatusLine().getStatusCode();
			final String status = httpResponse.getStatusLine().getReasonPhrase();

			if (responseStatusCode != 200 && responseStatusCode != 500)
			{
				String errorMsg = "Got SOAP response code " + responseStatusCode + " " + status;
				Exception myException = new Exception(errorMsg);
				throw myException;
			}

			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream is = null;
			is = httpEntity.getContent();
			ResponseXML = FlipickIO.convertStreamToString(is);
		}
		catch (Throwable e)
		{
			throw e;
		}
		return "";
	}*/
}
