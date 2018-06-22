package anup.project;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import anup.project.response.FlickrResponse;
import anup.project.response.Photo;

class GridAdapter extends BaseAdapter {
    private final Context context;
    private List<Photo> mItems = new ArrayList<Photo>();
    private final LayoutInflater mInflater;
    private final FlickrResponse flickrResponse;

    public GridAdapter(Context context, FlickrResponse json) {
        mInflater = LayoutInflater.from(context);
        this.context=context;
        this.flickrResponse=json;
        mItems.addAll(flickrResponse.getPhotos().getPhoto());
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Photo getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mItems.get(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        ImageView picture;

        if (v == null) {
            v = mInflater.inflate(R.layout.grid_item, viewGroup, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
        }

        picture = (ImageView) v.getTag(R.id.picture);
        Photo item = getItem(i);
        String url = String.format(AppConstants.IMG_URL,item.getFarm(),item.getServer(),item.getId(),item.getSecret());
        if(Caching.get(url)!=null){
            picture.setImageBitmap(Caching.get(url));
        }else{
            new ImageDownloaderTask(picture).execute(url);
        }

        return v;
    }

    public void addMore(FlickrResponse json) {
        mItems.addAll(json.getPhotos().getPhoto());
    }

    class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        public ImageDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadBitmap(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    }
                }
            }
        }

        private Bitmap downloadBitmap(String url) {
            HttpURLConnection urlConnection = null;
            try {
                URL uri = new URL(url);
                urlConnection = (HttpURLConnection) uri.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Caching.add(url,bitmap);
                    return bitmap;
                }
            } catch (Exception e) {
                urlConnection.disconnect();
                Log.w("ImageDownloader", "Error downloading image from " + url);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }
    }
}
