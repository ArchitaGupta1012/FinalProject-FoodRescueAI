package com.example.FinalProject.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity

public class NGO {
    private String ngoEmail;
    private String ngoAddress;
    private String ngoCity;
    private String ngoState;
    private double latitude;
    private double longitude;

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    private double preferredDistance;

    public double getPreferredDistance() {
        return preferredDistance;
    }

    public void setPreferredDistance(double preferredDistance) {
        this.preferredDistance = preferredDistance;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ngoName;
    private String ngoPhone;

    public String getNgoCity() {
        return ngoCity;
    }

    public void setNgoCity(String ngoCity) {
        this.ngoCity = ngoCity;
    }

    public String getNgoState() {
        return ngoState;
    }

    public void setNgoState(String ngoState) {
        this.ngoState = ngoState;
    }

    public String getNgoPhone() {
        return ngoPhone;
    }

    public String getNgoAddress() {
        return ngoAddress;
    }

    public void setNgoAddress(String ngoAddress) {
        this.ngoAddress = ngoAddress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNgoPhone(String ngoPhone) {
        this.ngoPhone = ngoPhone;
    }

    public String getNgoName() {
        return ngoName;
    }

    public void setNgoName(String ngoName) {
        this.ngoName = ngoName;
    }



    public String getNgoEmail() {
        return ngoEmail;
    }

    public void setNgoEmail(String ngoEmail) {
        this.ngoEmail = ngoEmail;
    }




}
