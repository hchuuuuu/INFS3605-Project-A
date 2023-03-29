package com.example.greentrailapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Marker implements Parcelable {

    String mName,img_url,iName,geoDistr,mSciName,tradUses;

    public Marker(){

    }

    protected Marker(Parcel in) {
        mName = in.readString();
        img_url = in.readString();
        iName = in.readString();
        geoDistr = in.readString();
        mSciName = in.readString();
        tradUses = in.readString();
    }

    public static final Creator<Marker> CREATOR = new Creator<Marker>() {
        @Override
        public Marker createFromParcel(Parcel parcel) {
            return new Marker(parcel);
        }

        @Override
        public Marker[] newArray(int i) {
            return new Marker[i];
        }
    };

    public String getmName(){
        return mName;
    }

    public String getiName(){
        return iName;
    }

    public String getGeoDistr(){
        return geoDistr;
    }

    public String getmSciName(){
        return mSciName;
    }

    public String getTradUses(){
        return tradUses;
    }

    public String getImg_url() {return img_url; }

    public void setmName(String mName){
        this.mName = mName;
    }

    public void setImg_url(String img_url) {this.img_url = img_url; }

    public void setiName(String iName){
        this.iName = iName;
    }

    public void setGeoDistr(String geoDistr){
        this.geoDistr = geoDistr;
    }

    public void setmSciName(String mSciName){
        this.mSciName = mSciName;
    }

    public void setTradUses(String tradUses){
        this.tradUses = tradUses;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mName);
    }
}
