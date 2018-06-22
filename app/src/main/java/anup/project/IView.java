package anup.project;

import anup.project.response.FlickrResponse;

public interface IView {

    void showProgress();
    void hideProgress();
    void showError(AppConstants.ErrorType api);
    void hideKeyBoard();
    void onResult(FlickrResponse json);
}
