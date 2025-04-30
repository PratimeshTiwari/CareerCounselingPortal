package com.example.careercounseling.model;

public class Student extends User {
    private String academicProfile;

    public Student(String id, String name, String email, String academicProfile) {
        super(id, name, email);
        this.academicProfile = academicProfile;
    }

    public String getAcademicProfile() { return academicProfile; }
    public void setAcademicProfile(String academicProfile) { this.academicProfile = academicProfile; }
}
