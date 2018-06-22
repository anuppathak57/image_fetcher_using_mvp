package anup.project;

public interface IPresenter {
    void searchImages(String query, int page);
    void loadMore(String s, int page);
}
