package com.nmhung.caro.model;

public class ItemModel {
    private String owned;
    private int x;
    private int y;
    private String roomId;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public ItemModel(String owned, int x, int y) {
        this.owned = owned;
        this.x = x;
        this.y = y;
    }

    public ItemModel() {
    }

    public String getOwned() {
        return owned;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


    public boolean existOwned() {
        return owned != null;
    }

    public void setOwned(String owned) {
        this.owned = owned;
    }
}
