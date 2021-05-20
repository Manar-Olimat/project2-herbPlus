package com.example.project_2.Models;

public class confirmplantBD {
    String name;
    String Symptoms;
    String description;
    String information;
    String added_by;
    String id;
    String date;
    String plant_image;
    String used;
    String location;

    public confirmplantBD() {
    }

    public confirmplantBD(String name, String symptoms, String description, String information,String id,
                          String added_by, String date,String plant_image ,String used ,String location ) {
        this.name = name;
        Symptoms = symptoms;
        this.description = description;
        this.information = information;
        this.id=id;
        this.added_by = added_by;
        this.date = date;
        this.plant_image=plant_image;
        this.used=used;
        this.location=location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymptoms() {
        return Symptoms;
    }

    public void setSymptoms(String symptoms) {
        Symptoms = symptoms;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getAdded_by() {
        return added_by;
    }

    public void setAdded_by(String added_by) {
        this.added_by = added_by;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlant_image() {
        return plant_image;
    }

    public void setPlant_image(String plant_image) {
        this.plant_image = plant_image;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
