package anup.project.response;

import com.google.gson.annotations.SerializedName;

public class FlickrResponse {

@SerializedName("photos")
private ResponsePhotos photos;

public ResponsePhotos getPhotos() {
return photos;
}

public void setPhotos(ResponsePhotos photos) {
this.photos = photos;
}

}