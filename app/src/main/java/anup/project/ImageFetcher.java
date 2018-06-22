package anup.project;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import anup.project.response.FlickrResponse;

public class ImageFetcher  implements IModel {

    @Override
    public void getImages(OnFinishedListener listener, String query, int page) {
          new ImageFetcherTask(listener,query,page).execute();
    }

    class ImageFetcherTask extends AsyncTask<Void, Void, String>{
        private final int page;
        private OnFinishedListener listener;
        private String query;

        ImageFetcherTask(OnFinishedListener listener, String query, int page) {
            this.listener=listener;
            this.query=query;
            this.page=page;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String urlString=String.format(AppConstants.URL,page,query);
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            Log.i("INFO", response);
            Gson gson = new GsonBuilder().create();
            FlickrResponse flickrResponse = gson.fromJson(response, FlickrResponse.class);
            listener.onFinished(flickrResponse);
        }
    }
}
