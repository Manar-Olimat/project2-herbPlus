package com.example.project_2.Models;

public class plantBD {
    String name;
    String Symptoms;
    String description;

    public plantBD() {
    }

    public plantBD(String name, String symptoms, String description) {
        this.name = name;
        this.Symptoms = symptoms;
        this.description = description;
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

}
