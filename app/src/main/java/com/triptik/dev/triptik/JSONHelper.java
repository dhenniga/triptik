package com.triptik.dev.triptik;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.Exception;import java.lang.String;import java.lang.StringBuilder;import java.net.URL;
import java.net.URLConnection;

public class JSONHelper {
    private static final String TAG = JSONHelper.class.getSimpleName();
    private JSONObject mJsonObject = null;
    private String json = "";

        public JSONObject getJSONFromString(String JSON) {

        String incoming = new String(JSON);


        try {
            // Convert the JSON String from InputStream to a JSONObject
            mJsonObject = new JSONObject(incoming);
        } catch (JSONException e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return mJsonObject;
    }



    public JSONObject getJSONFromUrl(String URL_MAIN) {
        try {
            URL url = new URL(URL_MAIN);
            URLConnection urlConnection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                builder.append(line).append("\n");
            }
            json = builder.toString();
            in.close();
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            // Convert the JSON String from InputStream to a JSONObject
            mJsonObject = new JSONObject(json);
        } catch (JSONException e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return mJsonObject;
    }
}
