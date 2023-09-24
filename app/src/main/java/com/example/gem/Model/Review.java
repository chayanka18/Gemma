package com.example.gem.Model;

import com.example.gem.ProductDetails;

public class Review {
    private String username;
    private String review;
    private float rating;
    private String pid;

    public Review() {
        // Default constructor required for Firebase
    }

    public Review(float rating,String review ,String username ) {
        this.username = username;
        this.review = review;
        this.rating = rating;
        this.pid=pid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
  

    public String getUsername() {
        return username;
    }

    public String getReview() {
        return review;
    }

    public float getRating() {
        return rating;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
