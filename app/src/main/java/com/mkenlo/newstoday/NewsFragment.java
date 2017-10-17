package com.mkenlo.newstoday;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;


public class NewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<JSONArray>{

    private static final String ARG_PARAM = "default news section";
    private String defaultNewsSection;
    NewsRecyclerAdapter mAdapter;
    RecyclerView recyclerNewsList;


    public NewsFragment() {
    }

    public static NewsFragment newInstance(String param) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            defaultNewsSection = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        recyclerNewsList = (RecyclerView) rootView.findViewById(R.id.news_list);
        recyclerNewsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new NewsRecyclerAdapter();

        getLoaderManager().initLoader(0, null, this).forceLoad();
        return rootView;
    }


    @Override
    public Loader<JSONArray> onCreateLoader(int id, Bundle args) {
        return new FetchNewsLoader(getActivity(), defaultNewsSection);
    }

    @Override
    public void onLoadFinished(Loader<JSONArray> loader, JSONArray data) {
        if(data !=null){
            mAdapter.setNewsData(data);
            recyclerNewsList.setAdapter(mAdapter);
        }

    }

    @Override
    public void onLoaderReset(Loader<JSONArray> loader) {

    }

}
