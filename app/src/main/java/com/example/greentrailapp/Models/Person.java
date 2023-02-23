package com.example.greentrailapp.Models;

public class Person{
    public String fullName;
    public int progress;

    public Person(String fullName, int progress) {
        this.fullName = fullName;
        this.progress = progress;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public Person(){

    }

}
