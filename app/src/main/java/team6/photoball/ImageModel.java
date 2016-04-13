package team6.photoball;

/**
 * Created by nelma on 4/13/2016.
 */

public class ImageModel {
    private String text;
    private String image;

    public ImageModel(String text, String image) {
        this.text = text;
        this.image = image;
    }

    public String getText() {
        return this.text;
    }

    public String getImage() {
        return this.image;
    }
}
