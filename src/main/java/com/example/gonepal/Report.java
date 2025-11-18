package com.example.gonepal;

public class Report {
    private String fullName;
    private String location;
    private String contact;
    private String type;
    private String details;

    public Report(String fullName, String location, String contact, String type, String details) {
        this.fullName = fullName;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.details = details;
    }

    public String getFullName() { return fullName; }
    public String getLocation() { return location; }
    public String getContact() { return contact; }
    public String getType() { return type; }
    public String getDetails() { return details; }

    public void setFullName(String f) { this.fullName = f; }
    public void setLocation(String l) { this.location = l; }
    public void setContact(String c) { this.contact = c; }
    public void setType(String t) { this.type = t; }
    public void setDetails(String d) { this.details = d; }
}