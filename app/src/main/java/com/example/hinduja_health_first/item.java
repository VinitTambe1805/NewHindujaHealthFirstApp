package com.example.hinduja_health_first;

public class item {

    String name;
    int image;

    public item(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {

        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
