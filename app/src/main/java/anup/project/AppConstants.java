package anup.project;

public class AppConstants {
    public static String URL ="https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=3e7cc266ae2b0e0d78e279ce8e361736&page=%s&format=json&nojsoncallback=1&safe_search=1&text=%s&per_page=40";
    enum ErrorType{
        VALIDATION,API,DEFAULT
    }
    public static String IMG_URL ="http://farm%s.static.flickr.com/%s/%s_%s.jpg";
}
