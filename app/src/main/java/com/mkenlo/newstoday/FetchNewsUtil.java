package com.mkenlo.newstoday;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;


public class FetchNewsUtil {

    //final static String API_KEY = "5a8dd4c8-0c8b-4436-a836-15bb0ddb5bb9";
    private final static String API_BASEURL = "https://content.guardianapis.com/search?api-key=5a8dd4c8-0c8b-4436-a836-15bb0ddb5bb9";

    private String fetchNewsRequest(String queryParam) {

        String jsonResponse = "";
        HttpURLConnection request = null;
        try {
            Uri.Builder built = Uri.parse(API_BASEURL).buildUpon();
            built.appendQueryParameter("show-fields", "thumbnail");
            built.appendQueryParameter("section", queryParam);

            /*if (!queryParam.isEmpty() || queryParam != null)
                built.appendQueryParameter("q", queryParam);
            */
            Uri builtUri = built.build();
            Log.d("GetNewsData", builtUri.toString());
            URL url = new URL(builtUri.toString());
            request = (HttpURLConnection) url.openConnection();
            request.setRequestMethod("GET");
            InputStream in = new BufferedInputStream(request.getInputStream());
            InputStreamReader inputStreamReader = new InputStreamReader(in, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            StringBuilder buffer = new StringBuilder();
            while (line != null) {
                buffer.append(line);
                line = reader.readLine();
            }
            in.close();
            jsonResponse = buffer.toString();
        } catch (IOException ex) {
            Log.e("I/O EXCEPTION", ex.getMessage());
        } finally {
            if (request != null)
                request.disconnect();
        }
        return jsonResponse;

    }

    public JSONArray getNewsData(String queryParam) {
        JSONArray newsData = null;
        try {
            String str = fetchNewsRequest(queryParam);
            JSONObject jsonObject = new JSONObject(str);
            if (jsonObject.getJSONObject("response").has("results")) ;
            newsData = jsonObject.getJSONObject("response").getJSONArray("results");

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return newsData;
    }
}
