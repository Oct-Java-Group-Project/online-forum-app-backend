package com.beaconfire.auth_service.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

public class EmailRequest implements Serializable {
    private String email;
    private String firstName;
    private String lastName;
    private String url;

    public EmailRequest(String email, String firstName, String lastName, String url) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}