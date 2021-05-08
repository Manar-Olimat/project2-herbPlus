package com.example.project_2.Models;

public class plantTypeGalleryModel {

    String name1,name2;
    int imgURL1;

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public int getImgURL1() {
        return imgURL1;
    }

    public void setImgURL1(int imgURL1) {
        this.imgURL1 = imgURL1;
    }

    public int getImgURL2() {
        return imgURL2;
    }

    public void setImgURL2(int imgURL2) {
        this.imgURL2 = imgURL2;
    }

    public plantTypeGalleryModel() {
    }

    public plantTypeGalleryModel(String name1, String name2, int imgURL1, int imgURL2) {
        this.name1 = name1;
        this.name2 = name2;
        this.imgURL1 = imgURL1;
        this.imgURL2 = imgURL2;
    }

    int imgURL2;
}
