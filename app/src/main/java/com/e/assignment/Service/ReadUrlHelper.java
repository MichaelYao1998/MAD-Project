package com.e.assignment.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*
 * The helper class to fetch the json string from URL
 * And handle the string
 */
public class ReadUrlHelper {
    private String urlStr;
    public ReadUrlHelper(String urlStr){
        this.urlStr = urlStr;
    }

    /*
     * handle the json string
     * extract the travel time and return it
     */
    public int getDuration(String jsonStr){
        int durationInSec = 0;

        try {
            JSONObject body = new JSONObject(jsonStr);
            JSONArray rowArray = body.getJSONArray("rows");
            JSONObject row = rowArray.getJSONObject(0);
            JSONArray elementsArray = row.getJSONArray("elements");
            JSONObject elements = elementsArray.getJSONObject(0);
            JSONObject duration = elements.getJSONObject("duration");
            durationInSec = duration.getInt("value");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return durationInSec;
    }
    /*
     * used to read the url
     * return the fetched data in string
     */
    public String readFromUrl(){
        HttpURLConnection urlConnection = null;
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

        }catch( Exception e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }
        return result.toString();
    }
}
