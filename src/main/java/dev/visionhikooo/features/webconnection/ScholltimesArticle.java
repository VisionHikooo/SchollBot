package dev.visionhikooo.features.webconnection;

public class ScholltimesArticle {
    private String title;
    private String url;
    private String imageURL;

    public ScholltimesArticle(String title, String url, String imageURL) {
        this.title = title;
        this.url = url;
        this.imageURL = imageURL;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getImageURL() {
        return imageURL;
    }
}
