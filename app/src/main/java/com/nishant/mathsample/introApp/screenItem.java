package com.nishant.mathsample.introApp;

public class screenItem {
    private String Title,Description;
    private int ScreenImage;

    public screenItem(String title, String description, int screenImage) {
        Title = title;
        Description = description;
        ScreenImage = screenImage;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getScreenImage() {
        return ScreenImage;
    }

    public void setScreenImage(int screenImage) {
        ScreenImage = screenImage;
    }
}
