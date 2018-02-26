package com.firebase.demo.model;

/**
 * Created by Juned on 7/25/2017.
 */

public class StudentDetails {


    private String name;
    private String phoneNumber;

    public StudentDetails() {
        // This is default constructor.
    }

    public String getStudentName() {

        return name;
    }

    public void setStudentName(String name) {

        this.name = name;
    }

    public String getStudentPhoneNumber() {

        return phoneNumber;
    }

    public void setStudentPhoneNumber(String phonenumber) {

        this.phoneNumber = phonenumber;
    }

}