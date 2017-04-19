package com.yinzhiwu.yiwu.view;

/**
 * Created by ping on 2017/4/19.
 */

public class Store {

    private int id;
    private String name;

    private String address;

    private String telePhone;

    public Store(int id) {
    }

    public Store(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelePhone() {
        return telePhone;
    }

    public void setTelePhone(String telePhone) {
        this.telePhone = telePhone;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
