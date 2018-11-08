package core;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class ImgUpload {

  public static String upload(String imgURL) {
    String directLink = "";
    try {

      // Creates Byte Array from picture
      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      // Download and write cover to a byte stream
      URL url;
      url = new URL(imgURL);
      InputStream in = url.openStream();
      for (int b; (b = in.read()) != -1; ) {
        baos.write(b);
      }
      
      String CLIENT_ID = "f3c818f21af99e8";
      URL apiUrl = new URL("https://api.imgur.com/3/image");
     
     // opens connection and sends data
      HttpURLConnection  conn = (HttpURLConnection) apiUrl.openConnection();
     conn.setDoOutput(true);
     conn.setDoInput(true);
     conn.setRequestMethod("POST");
     conn.setRequestProperty("Authorization", "Client-ID " + CLIENT_ID);
     conn.setRequestProperty("Content-Type",
             "applicationx-www-form-urlencoded");

     conn.connect();
     OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
     wr.write(imgURL);
     wr.flush();
     // Get the response
     BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("UTF-8")));
     // Get direct link
     String line;
     line = rd.readLine();
     directLink = StringUtils.substringBetween(line, "link\":\"", "\"}");
     directLink = directLink.replaceAll("\\\\", "");
     wr.close();
     rd.close();

    } catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return directLink;
  }
}
