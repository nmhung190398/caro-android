package com.nmhung.caro.model;

public class SocketModel {
    private String id;
    private boolean isSanSang;
    private String username;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSanSang() {
        return isSanSang;
    }

    public void setSanSang(boolean sanSang) {
        isSanSang = sanSang;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
