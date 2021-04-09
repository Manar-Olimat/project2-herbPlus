package com.example.project_2.Models;

public class confirmplantBD {
    String name;
    String Symptoms;
    String description;
    String information;
    String added_by;
    String date;

    public confirmplantBD() {
    }

    public confirmplantBD(String name, String symptoms, String description, String information, String added_by, String date) {
        this.name = name;
        Symptoms = symptoms;
        this.description = description;
        this.information = information;
        this.added_by = added_by;
        this.date = date;
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
}
