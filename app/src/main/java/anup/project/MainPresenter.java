package anup.project;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import anup.project.response.FlickrResponse;

public class MainPresenter implements IPresenter,IModel.OnFinishedListener {

    private final IView view;
    private final IModel model;

    public MainPresenter(IView view, IModel model) {
        this.view=view;
        this.model=model;
    }

    @Override
    public void searchImages(String query, int page) {
        view.hideKeyBoard();
        if(query==null || query.equals("")){
            view.showError(AppConstants.ErrorType.VALIDATION);
            return;
        }
        view.showProgress();
        model.getImages(this,query,page);
    }

    @Override
    public void loadMore(String s, int page) {
        model.getImages(this,s,page);
    }

    @Override
    public void onFinished(FlickrResponse json) {
       view.hideProgress();
       if(json==null || json.getPhotos() ==null || json.getPhotos()==null){
           view.showError(AppConstants.ErrorType.API);
           return;
       }
        view.onResult(json);
    }



}
