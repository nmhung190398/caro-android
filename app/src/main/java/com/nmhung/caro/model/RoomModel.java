package com.nmhung.caro.model;

import java.util.List;

public class RoomModel {
    private String id;
    private String name;
    private int length;

    public RoomModel(String id) {
        this.id = id;
        this.length = 0;
        this.name = "Phòng " + id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
