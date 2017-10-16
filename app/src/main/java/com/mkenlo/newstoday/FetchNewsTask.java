package com.mkenlo.newstoday;


import android.os.AsyncTask;

import org.json.JSONArray;

public class FetchNewsTask extends AsyncTask<String, Void, JSONArray> {

    private OnTaskCompleted listener;

    public FetchNewsTask(OnTaskCompleted listener) {
        this.listener = listener;
    }

    @Override
    protected void onPostExecute(JSONArray jArray) {
        listener.onTaskCompleted(jArray);
    }

    @Override
    protected JSONArray doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }
        FetchNewsUtil fetch = new FetchNewsUtil();
        return fetch.getNewsData(params[0]);
    }


}