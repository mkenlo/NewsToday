package com.mkenlo.newstoday;

//import android.content.AsyncTaskLoader;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import org.json.JSONArray;

/**
 * Created by Melanie on 10/16/2017.
 */

public class FetchNewsLoader extends AsyncTaskLoader<JSONArray> {
    private String params;

    public FetchNewsLoader(Context context, String params) {
        super(context);
        this.params = params;
    }


    @Override
    public JSONArray loadInBackground() {
        FetchNewsUtil fetch = new FetchNewsUtil();
        return fetch.getNewsData(params);

    }

    @Override
    public void deliverResult(JSONArray data) {
        super.deliverResult(data);
    }
}
