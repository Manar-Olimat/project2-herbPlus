package com.example.project_2.Models;

public class plantBD {
    String name;
    String Symptoms;
    String description;
    String information;

    public plantBD() {
    }

    public plantBD(String name, String symptoms, String description,String information) {
        this.name = name;
        this.Symptoms = symptoms;
        this.description = description;
        this.information=information;
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

}
