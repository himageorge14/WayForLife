package com.wayforlife.wayforlife.Model;

import android.media.Image;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Blob;
import java.util.Date;

public class Report {

    private String id;
    private Double Lat;
    private Double Long;
    private String date;
    private String description;
    private byte[] image;
    private static long serialId=10L;

    public Report() {
    }

    public Report(Double lat, Double aLong, String date, String description, byte[] img, String id) {
        Lat = lat;
        Long = aLong;
        this.date = date;
        this.description = description;
        image = img;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Double getLat() {
        return Lat;
    }

    public void setLat(Double lat) {
        Lat = lat;
    }

    public Double getLong() {
        return Long;
    }

    public void setLong(Double aLong) {
        Long = aLong;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }




}
