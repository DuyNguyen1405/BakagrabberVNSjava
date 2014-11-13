package core;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

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

      String API_URL = "http://api.imgur.com/2/upload.xml";
      String API_KEY = "4feabdcc44fea406348aedf44d1fd923";
      URL apiUrl = new URL(API_URL);

      // Encodes picture with Base64 and inserts api key
      String data = URLEncoder.encode("image", "UTF-8") + "="
                    + URLEncoder.encode(lib.Base64Coder.encodeLines(baos.toByteArray()), "UTF-8");

      data += "&" + URLEncoder.encode("key", "UTF-8") + "=" + URLEncoder.encode(API_KEY, "UTF-8");

      // opens connection and sends data
      URLConnection conn = apiUrl.openConnection();
      conn.setDoOutput(true);
      OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
      wr.write(data);
      wr.flush();

      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

      // Get direct link
      String line;
      line = rd.readLine();
      line = rd.readLine();
      directLink = StringUtils.substringBetween(line, "<original>", "</original>");
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
