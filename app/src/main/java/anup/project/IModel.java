package anup.project;

import anup.project.response.FlickrResponse;

public interface IModel {
    interface OnFinishedListener {
        void onFinished(FlickrResponse json);
    }

    void getImages(OnFinishedListener listener, String query, int page);
}
