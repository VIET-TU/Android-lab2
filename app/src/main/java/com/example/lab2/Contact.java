package com.example.lab2;

import android.graphics.Bitmap;
import android.net.Uri;

public class Contact {
    public int id;
    public String fullName;
    public String phoneNumber;
    public boolean status;

    public Bitmap avartar;

    public Bitmap getAvartar() {
        return avartar;
    }

    public void setAvartar(Bitmap avartar) {
        this.avartar = avartar;
    }

    public Contact(int id, String fullName, String phoneNumber, Bitmap avartar, boolean status) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.avartar = avartar;
    }

    public Contact(int id, String fullName, String phoneNumber, boolean status) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
