package com.example.project_2.Models;

public class plantBD {
    String name;
    String Symptoms;
    String description;
    String information;
    String plant_image;

    public plantBD() {
    }

    public plantBD(String name, String symptoms, String description,String information ,String plant_image) {
        this.name = name;
        this.Symptoms = symptoms;
        this.description = description;
        this.information=information;
        this.plant_image=plant_image;
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

    public String getPlant_image() {
        return plant_image;
    }

    public void setPlant_image(String plant_image) {
        this.plant_image = plant_image;
    }

}
