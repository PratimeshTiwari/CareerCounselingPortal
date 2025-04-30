package com.example.careercounseling.model;

public class Mentor extends User {
    private String expertise;

    public Mentor(String id, String name, String email, String expertise) {
        super(id, name, email);
        this.expertise = expertise;
    }

    public String getExpertise() { return expertise; }
    public void setExpertise(String expertise) { this.expertise = expertise; }
}
