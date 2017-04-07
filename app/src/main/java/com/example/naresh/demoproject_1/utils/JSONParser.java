package com.example.naresh.demoproject_1.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by naresh on 27/2/17.
 */

public class JSONParser {

    public JSONParser() {
    }

    public String makeServiceCall(String reqUrl) throws MalformedURLException {
        String response = null;
        URL url = new URL(reqUrl);
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            //read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            response = sb.toString();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

}
