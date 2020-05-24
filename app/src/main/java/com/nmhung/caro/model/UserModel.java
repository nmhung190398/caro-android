package com.nmhung.caro.model;

public class UserModel {
    private String username;
    private String password;
    private String urlImg;
    private long totalWin;

    public UserModel(String username, String urlImg, long hall){
        this.urlImg = urlImg;
        this.username = username;
        this.totalWin = hall;
    }

    public UserModel(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public long getTotalWin() {
        return totalWin;
    }

    public void setTotalWin(long totalWin) {
        this.totalWin = totalWin;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
