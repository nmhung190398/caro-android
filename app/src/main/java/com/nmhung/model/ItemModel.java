package com.nmhung.model;

public class ItemModel {
    private String owned;
    public int x;
    public int y;

    public ItemModel(String owned, int x, int y) {
        this.owned = owned;
        this.x = x;
        this.y = y;
    }

    public ItemModel() {
    }

    public boolean existOwned() {
        return owned != null;
    }

    public void setOwned(String owned) {
        this.owned = owned;
    }
}
