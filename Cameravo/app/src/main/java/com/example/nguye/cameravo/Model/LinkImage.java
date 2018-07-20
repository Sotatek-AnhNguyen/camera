package com.example.nguye.cameravo.Model;

public class LinkImage {
    private String path;
    private boolean click;
    private boolean isImage;

    public LinkImage(String path, boolean click, boolean isImage) {
        this.path = path;
        this.click = click;
        this.isImage = isImage;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isClick() {
        return click;
    }

    public void setClick(boolean click) {
        this.click = click;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
    }
}
