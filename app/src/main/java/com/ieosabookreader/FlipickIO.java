package com.ieosabookreader;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;
import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.PermissionChecker.checkSelfPermission;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import org.w3c.dom.Document;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.util.Enumeration;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


public class FlipickIO {



    static RowTransposition oRowTransposition = new RowTransposition();

    public static void WriteTextFile(File file, String text) throws IOException
    {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(text);
        writer.close();
    }

    public static void CopyDataBaseToSdCard(Context context)
    {

        try
        {
            File sd = Environment.getExternalStorageDirectory();
            @SuppressWarnings("unused")
            File data = Environment.getDataDirectory();

            if (sd.canWrite())
            {
                String currentDBPath = data + "/data/" + context.getPackageName() + "/databases/IEOSABookReader_db";
                String backupDBPath = "IEOSA.db";

                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists())
                {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        }
        catch (Exception e)
        {
            FlipickError.DisplayErrorAsLongToast(context, e.getMessage());
        }

    }

    public static void copyfolderDirectoryRecursively(File sourceLocation, File targetLocation) throws IOException
    {
        try
        {
            if (sourceLocation.isDirectory())
            {
                if (!targetLocation.exists() && !targetLocation.mkdirs())
                {
                    throw new IOException("Cannot create dir " + targetLocation.getAbsolutePath());
                }
                String[] children = sourceLocation.list();
                for (int i = 0; i < children.length; i++)
                {
                    copyfolderDirectoryRecursively(new File(sourceLocation.getAbsolutePath(), children[i]), new File(targetLocation.getAbsolutePath(), children[i]));
                }
            }
            else
            {
                // make sure the directory we plan to store the recording in
                // exists
                File directory = targetLocation.getParentFile();
                if (directory != null && !directory.exists() && !directory.mkdirs())
                {
                    throw new IOException("Cannot create dir " + directory.getAbsolutePath());
                }
                InputStream in = new FileInputStream(sourceLocation.getAbsolutePath());
                OutputStream out = new FileOutputStream(targetLocation.getAbsolutePath());
                // Copy the bits from instream to outstream
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0)
                {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            }
        }
        catch (Exception e)
        {
            e.getStackTrace();
        }
    }

    public static String ReadHTMLFileText(String FilePath) throws IOException
    {
        StringBuilder buf_text = null;
        BufferedReader in = new BufferedReader(new FileReader(FilePath));
        buf_text = new StringBuilder();
        char[] bytes = new char[1000];
        int numRead = 0;
        while ((numRead = in.read(bytes)) >= 0)
        {
            buf_text.append(new String(bytes, 0, numRead));
        }
        in.close();

        return buf_text.toString();
    }

    public static String WriteHTMLFileText(String FilePath, String decrypt_html) throws IOException
    {
        FileWriter fWriter;
        fWriter = new FileWriter(FilePath);
        fWriter.write(decrypt_html);
        fWriter.flush();
        fWriter.close();
        return "";
    }

    public static Document GetDocumentFromFile(String XMLFilePath) throws Exception
    {
        try
        {
            File fXmlFile = new File(XMLFilePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            return doc;
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    public static Document GetXMLDocument() throws Exception
    {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try
        {
            docBuilder = docFactory.newDocumentBuilder();
        }
        catch (ParserConfigurationException e)
        {
            throw e;
        }
        Document doc = docBuilder.newDocument();
        return doc;

    }

    public static String convertStreamToString(InputStream is) throws Exception
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try
        {
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            try
            {
                is.close();
            }
            catch (IOException e)
            {
            }
        }
        return sb.toString();
    }

    public static void CreateRootFolder() {

        File appSpecificDir = new File(FlipickConfig.Part1ContentRootPath);
        if (!appSpecificDir.exists()) {
            boolean isCreated = appSpecificDir.mkdir();
            if (isCreated) {
                Log.d("MainActivity", "Directory created: " + appSpecificDir.getPath());
            } else {
                Log.e("MainActivity", "Failed to create directory: " + appSpecificDir.getPath());
            }
        } else {
            Log.d("MainActivity", "Directory already exists: " + appSpecificDir.getPath());
        }

        //File EPUBDirectory = new File(FlipickConfig.Part1ContentRootPath);
        //if (!EPUBDirectory.exists()) EPUBDirectory.mkdirs();


    }

    public static void CreateUserRootFolder() {

        File appSpecificDir = new File(FlipickConfig.ContentRootPath);
        if (!appSpecificDir.exists()) {
            boolean isCreated = appSpecificDir.mkdir();
            if (isCreated) {
                Log.d("MainActivity", "Directory created: " + appSpecificDir.getPath());
            } else {
                Log.e("MainActivity", "Failed to create directory: " + appSpecificDir.getPath());
            }
        } else {
            Log.d("MainActivity", "Directory already exists: " + appSpecificDir.getPath());
        }

       // File EPUBDirectory = new File(FlipickConfig.ContentRootPath);
       // if (!EPUBDirectory.exists()) EPUBDirectory.mkdirs();

    }

    static void createAppSpecificDirectory() {
        File appSpecificDir = new File(FlipickConfig.ContentRootPath);
        if (!appSpecificDir.exists()) {
            boolean isCreated = appSpecificDir.mkdir();
            if (isCreated) {
                Log.d("MainActivity", "Directory created: " + appSpecificDir.getPath());
            } else {
                Log.e("MainActivity", "Failed to create directory: " + appSpecificDir.getPath());
            }
        } else {
            Log.d("MainActivity", "Directory already exists: " + appSpecificDir.getPath());
        }
    }



    /*
     * static public void extractFolder(String _zipFile, String _location)
     * throws Exception { try { System.out.println(_zipFile); int BUFFER = 2048;
     * int finalSize = 0; float prev = -1; float current = 0;
     *
     * File file = new File(_zipFile); ZipFile zip = new ZipFile(file); File
     * file_location = new File(_location); if (!file_location.isDirectory()) {
     * file_location.mkdir(); } // new File(newPath).mkdir(); Enumeration
     * zipFileEntries = zip.entries();
     *
     * finalSize = (int) new File(_zipFile).length();
     *
     * // Process each entry while (zipFileEntries.hasMoreElements()) { // grab
     * a zip file entry ZipEntry ze = (ZipEntry) zipFileEntries.nextElement();
     * current += ze.getCompressedSize(); String currentEntry = ze.getName();
     * File destFile = new File(_location, currentEntry); // destFile = new
     * File(newPath, destFile.getName()); File destinationParent =
     * destFile.getParentFile();
     *
     * // create the parent directory structure if needed if
     * (!destinationParent.isDirectory()) { destinationParent.mkdirs(); } if
     * (!ze.isDirectory()) { BufferedInputStream is = new
     * BufferedInputStream(zip.getInputStream(ze));
     *
     * int currentByte; // establish buffer for writing file byte data[] = new
     * byte[BUFFER]; // write the current file to disk FileOutputStream fos =
     * new FileOutputStream(destFile);
     *
     * for (int c = is.read(data); c != -1; c = is.read(data)) { fos.write(data,
     * 0, c); } if (prev != current / finalSize * 100) { prev = current /
     * finalSize * 100; FlipickStaticData.progressMessage =
     * FlipickStaticData.MsgUnzip + (int) prev + "%"; }
     *
     * BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
     *
     * // read and write until last byte is encountered while ((currentByte =
     * is.read(data, 0, BUFFER)) != -1) { dest.write(data, 0, currentByte); }
     *
     * dest.flush(); dest.close(); is.close(); }
     *
     * } } catch (Exception e) { Exception exception=new
     * Exception(e.getMessage()); throw exception;
     *
     *
     * } }
     */

    static public void extractFolder(String _zipFile, String _location) throws Exception, ZipException, IOException, SecurityException
    {
        try
        {
            System.out.println(_zipFile);
            int BUFFER = 2048;
            int finalSize = 0;
            float prev = -1;
            float current = 0;

            File file = new File(_zipFile);
            ZipFile zip = new ZipFile(file);
            File file_location = new File(_location);
            if (!file_location.isDirectory())
            {
                file_location.mkdir();
            }
            // new File(newPath).mkdir();
            Enumeration zipFileEntries = zip.entries();

            finalSize = (int) new File(_zipFile).length();

            // Process each entry
            while (zipFileEntries.hasMoreElements())
            {
                // grab a zip file entry
                ZipEntry ze = (ZipEntry) zipFileEntries.nextElement();
                current += ze.getCompressedSize();
                String currentEntry = ze.getName();
                File destFile = newFile(file_location, currentEntry);
                // destFile = new File(newPath, destFile.getName());
                File destinationParent = destFile.getParentFile();

                // create the parent directory structure if needed
                if (!destinationParent.isDirectory())
                {
                    destinationParent.mkdirs();
                }
                if (!ze.isDirectory())
                {
                    BufferedInputStream is = new BufferedInputStream(zip.getInputStream(ze));

                    int currentByte;
                    // establish buffer for writing file
                    byte data[] = new byte[BUFFER];
                    // write the current file to disk
                    FileOutputStream fos = new FileOutputStream(destFile);

                    // for (int c = zin.read(buffer); c != -1; c =
                    // zin.read(buffer))
                    // {
                    // fout.write(buffer, 0, c);
                    // }
                    // if (prev != current / finalSize * 100)
                    // {
                    // prev = current / finalSize * 100;
                    // FlipickStaticData.progressMessage =
                    // FlipickStaticData.MsgUnzip + (int) prev + "%";
                    // }

                    for (int c = is.read(data); c != -1; c = is.read(data))
                    {
                        fos.write(data, 0, c);
                    }
                    if (prev != current / finalSize * 100)
                    {
                        prev = current / finalSize * 100;
                        // FlipickStaticData.progressMessage =
                        // FlipickStaticData.MsgUnzip + (int) prev + "%";
                    }

                    BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);

                    // read and write until last byte is encountered
                    while ((currentByte = is.read(data, 0, BUFFER)) != -1)
                    {
                        dest.write(data, 0, currentByte);
                    }

                    dest.flush();
                    dest.close();
                    is.close();
                }

            }
        }
        catch (Exception e)
        {
            Exception exception = new Exception(e.getMessage());
            // e.getMessage();
            throw exception;

        }
    }

    // for Corse Download process
    public static File newFile(File destinationDir, String zipEntryName) throws IOException
    {
        File destFile = new File(destinationDir, zipEntryName);

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator))
        {
            throw new IOException("Entry is outside of the target dir: " + zipEntryName);
        }

        return destFile;
    }

    public static void DeleteFolder(File dir) throws Exception
    {
        try
        {
            if (dir.isDirectory())
            {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++)
                {
                    new File(dir, children[i]).delete();
                }
            }
        }
        catch (Exception e)
        {
            throw e;

        }

    }

    public static boolean DeleteDirectoryRecursive(File path) throws Exception
    {
        try
        {
            if (path.exists())
            {
                File[] files = path.listFiles();
                if (files == null)
                {
                    return true;
                }
                for (int i = 0; i < files.length; i++)
                {
                    if (files[i].isDirectory())
                    {
                        DeleteDirectoryRecursive(files[i]);
                    }
                    else
                    {
                        files[i].delete();
                    }
                }
            }
            return (path.delete());
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    public static void SaveXMLFileFromDocument(String Path, String FileName, Document oDoc) throws Exception
    {
        try
        {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            Properties outFormat = new Properties();
            outFormat.setProperty(OutputKeys.INDENT, "yes");
            outFormat.setProperty(OutputKeys.METHOD, "xml");
            outFormat.setProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            outFormat.setProperty(OutputKeys.VERSION, "1.0");
            outFormat.setProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperties(outFormat);
            DOMSource source = new DOMSource(oDoc);
            String UserinfoXML_Path = Path + "/";
            String Userinfo_FileName = FileName;// "UserInfo.xml";
            StreamResult result = new StreamResult(new File(UserinfoXML_Path + Userinfo_FileName));
            transformer.transform(source, result);
            outFormat = null;
            transformer = null;
            factory = null;
            oDoc = null;
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    public static void SaveXMLFileFromDocument(String FullPath, Document oDoc) throws Exception
    {
        try
        {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            Properties outFormat = new Properties();
            outFormat.setProperty(OutputKeys.INDENT, "yes");
            outFormat.setProperty(OutputKeys.METHOD, "xml");
            outFormat.setProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            outFormat.setProperty(OutputKeys.VERSION, "1.0");
            outFormat.setProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperties(outFormat);
            DOMSource source = new DOMSource(oDoc);
            StreamResult result = new StreamResult(new File(FullPath));
            result.setSystemId(java.net.URLDecoder.decode(result.getSystemId(), "UTF-8"));
            transformer.transform(source, result);
            outFormat = null;
            transformer = null;
            factory = null;
            oDoc = null;
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    public static void CopyFile(String FromName, String ToName) throws IOException
    {
        InputStream in = new FileInputStream(FromName);
        OutputStream out = new FileOutputStream(ToName);
        byte[] buf = new byte[1024];
        int len;

        while ((len = in.read(buf)) > 0)
        {
            out.write(buf, 0, len);
        }

        in.close();
        out.close();
        buf = null;
    }

    public static void CopyFile(InputStream in, OutputStream out) throws Exception
    {
        try
        {
            byte[] buffer = new byte[1024];
            int read;

            while ((read = in.read(buffer)) != -1)
            {
                out.write(buffer, 0, read);
            }

            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    public static void DonwloadAndSaveFile(int Attempt, String HttpUrl, String Path) throws Exception
    {
        InputStream input = null;
        OutputStream output = null;
        try
        {
            if (HttpUrl != null)
            {
                long donwloaded_FileLenght = 0;

                URL url = new URL(HttpUrl);
                URLConnection connection = url.openConnection();

                File oFile = new File(Path);
                if (oFile.exists())
                {
                    donwloaded_FileLenght = (int) oFile.length();
                    connection.setRequestProperty("Range", "bytes=" + (oFile.length()) + "-");
                }
                else
                {
                    connection.setRequestProperty("Range", "bytes=" + donwloaded_FileLenght + "-");
                }
                connection.connect();
                int fileLength = 0;
                int File_lenght_check = 0;

                fileLength = connection.getContentLength();
                if (fileLength == -1 || fileLength == 0)
                {

                    if (Attempt > 4)
                    {
                        Exception exception = new Exception("Unable to download.Check your internet connection.");
                        throw exception;
                    }
                    else
                    {
                        Thread.sleep(5000);
                        Attempt++;
                        connection = null;
                        DonwloadAndSaveFile(Attempt, HttpUrl, Path);
                    }
                }
                else
                {
                    File_lenght_check = fileLength;
                    if (File_lenght_check == 362)
                    {
                        return;
                    }
                    fileLength = (int) (fileLength + donwloaded_FileLenght);

                    input = new BufferedInputStream(connection.getInputStream());

                    output = (donwloaded_FileLenght == 0) ? new FileOutputStream(Path) : new FileOutputStream(Path, true);
                    byte data[] = new byte[1024];
                    long total = donwloaded_FileLenght;
                    int count;
                    while ((count = input.read(data)) != -1)
                    {

                        total += count;

                        if (fileLength > 0)
                        {
                            FlipickStaticData.progressMessage = FlipickStaticData.MsgDownload + (int) ((total * 100 / fileLength)) + "%";
                        }
                        else
                        {
                            FlipickStaticData.progressMessage = FlipickStaticData.MsgDownload + 0 + "%";
                        }

                        output.write(data, 0, count);
                    }
                }

            }
            if (output != null)
            {
                output.flush();
                output.close();
                input.close();
            }
        }
        catch (Exception e)
        {
            Exception exception = new Exception("Unable to download.");
            throw exception;
        }
    }

    public static void DonwloadAndSaveFileWithoutProgress(String HttpUrl, String Path) throws Exception {

         final StringBuilder result = new StringBuilder();
         final char[] buffer = new char[512]; //allocating buffer
        try
        {
          /*  result.setLength(0); //clearing StringBuilder
            URL url = new URL(HttpUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            // now reading not by 1 char, but by 512 for once
            int i;
            while ((i = inputStreamReader.read(buffer)) != -1) {
                result.append(buffer, 0, i);
            }

            inputStreamReader.close(); //close reader instead of InputStream*/

            URL url = new URL(HttpUrl);
            URLConnection connection = url.openConnection();
            connection.connect();
            int fileLength = connection.getContentLength();
            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(Path);
            //byte data[] = new byte[1024];
            byte data[] = new byte[256000];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1)
            {
                total += count;
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        }
        catch (Exception e)
        {
            throw e;
        }
    }

}
