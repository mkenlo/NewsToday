package com.mkenlo.newstoday;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;


public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.ViewHolder> {

    JSONArray newsData;

    public NewsRecyclerAdapter(JSONArray newsData) {
        this.newsData = newsData;
    }

    @Override
    public NewsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_item, parent, false);
        return new NewsRecyclerAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NewsRecyclerAdapter.ViewHolder holder, int position) {
        try {
            JSONObject article = newsData.getJSONObject(position);
            holder.articleTitle.setText(article.getString("webTitle"));
            holder.articleSection.setText(article.getString("sectionName"));
            String formattedDate = getFormattedDate(article.getString("webPublicationDate"));
            holder.articlePublishedDate.setText(formattedDate);

            if(article.has("fields")){
                if (article.getJSONObject("fields").has("thumbnail")){
                    DownloadImageTask task = new DownloadImageTask(holder.articleThumbnail);
                    task.execute(article.getJSONObject("fields").getString("thumbnail"));
                }
            }

            final String articleUrl = article.getString("webUrl");
            holder.articleCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent openPageIntent = new Intent();
                    openPageIntent.setAction(Intent.ACTION_VIEW);
                    openPageIntent.setData(Uri.parse(articleUrl));
                    v.getContext().startActivity(openPageIntent);
                }
            });

        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return newsData.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView articleTitle;
        TextView articleSection;
        TextView articlePublishedDate;
        ImageView articleThumbnail;
        CardView articleCard;

        public ViewHolder(View v) {
            super(v);

            articleTitle = (TextView) v.findViewById(R.id.article_title);
            articleSection = (TextView) v.findViewById(R.id.article_section);
            articlePublishedDate = (TextView) v.findViewById(R.id.article_date);
            articleThumbnail = (ImageView) v.findViewById(R.id.article_thumbnail);
            articleCard = (CardView) v.findViewById(R.id.article);

        }
    }

    private String getFormattedDate(String date){
        try{
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm z");

            String[] dateAndTime = date.split("T|Z");
            StringBuilder strDate =  new StringBuilder();
            strDate.append(dateAndTime[0]);
            strDate.append(" ");
            strDate.append(dateAndTime[1]);

            date = strDate.toString();
            Date newDate = format.parse(date);
            date = newDate.toString();

        }catch(ParseException ex){
            Log.e("ParseException", ex.getMessage());
        }

        return date;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap thumbnail = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                thumbnail = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return thumbnail;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
