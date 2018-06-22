package anup.project;

import android.text.TextUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import anup.project.response.FlickrResponse;
import anup.project.response.Photo;
import anup.project.response.ResponsePhotos;

public class MainPresenterTest {

    private MainPresenter presenter;

    @Mock
    private IModel repository;
    @Mock
    private IView viewContract;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        // Make presenter a mock while using mock repository and viewContract created above
        presenter = Mockito.spy(new MainPresenter(viewContract, repository));
    }

    @Test
    public void searchImages_noQuery() {
        String searchQuery = null;
        // Trigger
        presenter.searchImages(searchQuery,1);
        // Validation
        Mockito.verify(repository, Mockito.never()).getImages( presenter,searchQuery,1);
    }

    @Test
    public void searchImages() {
        String searchQuery = "kittens";
        // Trigger
        presenter.searchImages(searchQuery,1);
        // Validation
        Mockito.verify(repository, Mockito.times(1)).getImages( presenter,searchQuery,1);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void handleImageResponse_Success() {
        FlickrResponse response = Mockito.mock(FlickrResponse.class);
        ResponsePhotos searchResponse = Mockito.mock(ResponsePhotos.class);
        Mockito.doReturn(searchResponse).when(response).getPhotos();
        List<Photo> searchResults = new ArrayList<>();
        searchResults.add(new Photo());
        searchResults.add(new Photo());
        searchResults.add(new Photo());
        Mockito.doReturn(searchResults).when(searchResponse).getPhoto();
        // trigger
        presenter.onFinished(response);
        // validation
        Mockito.verify(viewContract, Mockito.times(1)).onResult(response);
    }

    @Test
    public void handleImageResponse_Failure() {
        FlickrResponse response = Mockito.mock(FlickrResponse.class);

        // case No. 1
        Mockito.doReturn(null).when(response).getPhotos();
        // trigger
        presenter.onFinished(response);
        // validation
        Mockito.verify(viewContract, Mockito.times(1)).showError(AppConstants.ErrorType.API);
        /*
        if the following is not called, viewContract.displayError() will be
        mistakenly considered as invoked twice
         */
        Mockito.reset(viewContract);

        // case No. 2
        Mockito.doReturn(null).when(response).getPhotos();
        // trigger
        presenter.onFinished(response);
        // validation
        Mockito.verify(viewContract, Mockito.times(1)).showError(AppConstants.ErrorType.API);
    }
}
