package com.example.lab2.models;

public class Student {
    public String id,name;
    public Double toan,ly,hoa;

    public Student(String id, String name, Double toan, Double ly, Double hoa) {
        this.id = id;
        this.name = name;
        this.toan = toan;
        this.ly = ly;
        this.hoa = hoa;
    }

    public Double diemTB() {
        return (toan + ly + hoa)/3;
    }


}
