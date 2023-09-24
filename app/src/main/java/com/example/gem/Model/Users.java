package com.example.gem.Model;

public class Users
{
    private String inputUser,inputEmail,image,address;

    public Users()
    {

    }

    public Users(String inputUser, String inputEmail, String image, String address) {
        this.inputUser = inputUser;
        this.inputEmail = inputEmail;
        this.image = image;
        this.address = address;
    }

    public String getInputUser() {
        return inputUser;
    }

    public void setInputUser(String inputUser) {
        this.inputUser = inputUser;
    }

    public String getInputEmail() {
        return inputEmail;
    }

    public void setInputEmail(String inputEmail) {
        this.inputEmail = inputEmail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
