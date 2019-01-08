package com.droid.matt.matt;

public class Images {

    public String getLargeImageURL() {
        return largeImageURL;
    }

    public void setLargeImageURL(String largeImageURL) {
        this.largeImageURL = largeImageURL;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    private String largeImageURL;
    private String user;

    public Images(String largeImageURL, String user) {
        this.largeImageURL = largeImageURL;
        this.user = user;
    }
}
