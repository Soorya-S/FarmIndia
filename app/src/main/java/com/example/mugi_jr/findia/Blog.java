package com.example.mugi_jr.findia;

/**
 * Created by hareeshlingaswamy on 23-03-2017.
 */

public class Blog {

    private String title;

    private String image;
    private String Dpimage;
    private String username;





    public Blog() {

    }


    public Blog(String title, String desc, String image, String username, String Dpimage ) {
        this.title = title;

        this.image = image;
        this.username = username;
        this.Dpimage = Dpimage;


        //  int likecount        = 0;

    }




    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDpimage() {
        return Dpimage;
    }

    public void setDpimage(String dpimage) {
        Dpimage = dpimage;
    }



}


