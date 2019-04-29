/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.Demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class GetTextFromGit {

    public List<String> getStringFromGithubRaw(String path) throws MalformedURLException, IOException {
        URL crunchifyUrl = new URL(path);
        HttpURLConnection crunchifyHttp = (HttpURLConnection) crunchifyUrl.openConnection();
        Map<String, List<String>> crunchifyHeader = crunchifyHttp.getHeaderFields();

        // If URL is getting 301 and 302 redirection HTTP code then get new URL link.
        // This below for loop is totally optional if you are sure that your URL is not getting redirected to anywhere
        for (String header : crunchifyHeader.get(null)) {
            if (header.contains(" 302 ") || header.contains(" 301 ")) {
                path = crunchifyHeader.get("Location").get(0);
                crunchifyUrl = new URL(path);
                crunchifyHttp = (HttpURLConnection) crunchifyUrl.openConnection();
                crunchifyHeader = crunchifyHttp.getHeaderFields();
            }
        }
        InputStream crunchifyStream = crunchifyHttp.getInputStream();
        List<String> crunchifyResponse = getListStringFromStream(crunchifyStream);
        return crunchifyResponse;
    }

    public String getStringFromStream(InputStream crunchifyStream) throws IOException {
        if (crunchifyStream != null) {
            Writer crunchifyWriter = new StringWriter();

            char[] crunchifyBuffer = new char[2048];
            try {
                Reader crunchifyReader = new BufferedReader(new InputStreamReader(crunchifyStream, "UTF-8"));
                int counter;
                while ((counter = crunchifyReader.read(crunchifyBuffer)) != -1) {
                    crunchifyWriter.write(crunchifyBuffer, 0, counter);
                }
            } finally {
                crunchifyStream.close();
            }
            return crunchifyWriter.toString();
        } else {
            return "No Contents";
        }
    }

    public List<String> getListStringFromStream(InputStream crunchifyStream) throws IOException {
        List<String> temps = new ArrayList<>();
        if (crunchifyStream != null) {
            try {
                BufferedReader crunchifyReader = new BufferedReader(new InputStreamReader(crunchifyStream, "UTF-8"));
                String read = "";
                while ((read = crunchifyReader.readLine()) != null) {
                    temps.add(read);
                }
            } finally {
                crunchifyStream.close();
            }
            return temps;
        } else {
            return null;
        }
    }
}
