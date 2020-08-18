package com.example.memorybook;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Item {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private Double lat;
    private Double lng;
    private String date;

    private String description;
    private String path;
    private String title;

    public Item(){}

    public Item(Date date, String desctiption, String path, LatLng loc,String title) {
        SimpleDateFormat formatter= new SimpleDateFormat("dd.MM");
        this.date = formatter.format(date);
        this.description = desctiption;
        this.path = path;
        this.lat = loc.latitude;
        this.lng = loc.longitude;
        this.title = title;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof Item)) return false;

        Item item = (Item) o;
        if(item.getId() != id) return false;
        //what makes it different?
        return true;
    }


    @Override
    public String toString(){
        return "Item: date= "+date+", description= "+title;
    }
    public Integer getId() {
        return id;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String desctiption) {
        this.description = desctiption;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
