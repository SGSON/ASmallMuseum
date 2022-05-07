package xyz.asmallmuseum.android.persistence;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SomaGallery {
    public SomaGallery(){}

    public void getArtInfo(String author, String title) throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://www.kspo.or.kr/openapi/service/somaWorkInfoService/getList"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=Zty42NM%2FUj%2FwcSEKa%2BXBrpzXmGZYvsSid5GZlO1TxrsN9Zn%2F%2B5ZPlc6z3B7sqoBmdmCOFr5JJQi5AZ29VjF2mw%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("titleEng","UTF-8") + "=" + URLEncoder.encode(title, "UTF-8")); /*영문 작품명*/
        urlBuilder.append("&" + URLEncoder.encode("artistEng","UTF-8") + "=" + URLEncoder.encode(author, "UTF-8")); /*영문 작가명*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        Log.d("OUTPUT", sb.toString());
    }
}
