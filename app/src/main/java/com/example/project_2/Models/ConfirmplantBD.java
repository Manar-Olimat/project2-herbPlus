package com.example.project_2.Models;

public class ConfirmplantBD extends plantBD {
    String added_by;
    String id;
    String date;

    public ConfirmplantBD() {
    }



    public ConfirmplantBD(String name, String symptoms, String description, String information, String plant_image, String used, String location, String added_by, String id, String date) {
        super(name, symptoms, description, information, plant_image, used, location);
        this.added_by = added_by;
        this.id = id;
        this.date = date;

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
