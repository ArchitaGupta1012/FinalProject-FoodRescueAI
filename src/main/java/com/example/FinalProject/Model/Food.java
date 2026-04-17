package com.example.FinalProject.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Food {
    private String food_type;
    private String food_description;
    private String quantity;
    private String prep_time;
    private String expiry_time;
    private String pickup_address;
    private String city;
    private String contact_person;
    private String ph_no;
    private String status; // AVAILABLE, CLAIMED, EXPIRED
    private String claimedByNgoName;

    public String getNgoEmail() {
        return ngoEmail;
    }

    public void setNgoEmail(String ngoEmail) {
        this.ngoEmail = ngoEmail;
    }

    private String claimedByNgoPhone;
    private double latitude;
    private double longitude;
    private String requestStatus; // REQUESTED, APPROVED, REJECTED
    private String requestedByNgoName;
    private String requestedByNgoPhone;

    public String getHotelEmail() {
        return hotelEmail;
    }

    public void setHotelEmail(String hotelEmail) {
        this.hotelEmail = hotelEmail;
    }

    private String hotelEmail;
    private String ngoEmail;

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    private String distanceStatus;

    public String getDistanceStatus() {
        return distanceStatus;
    }

    public void setDistanceStatus(String distanceStatus) {
        this.distanceStatus = distanceStatus;
    }

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String hotelName;

    public String getPickup_address() {
        return pickup_address;
    }

    public void setPickup_address(String pickup_address) {
        this.pickup_address = pickup_address;
    }

    public String getExpiry_time() {
        return expiry_time;
    }

    public void setExpiry_time(String expiry_time) {
        this.expiry_time = expiry_time;
    }

    public String getPrep_time() {
        return prep_time;
    }

    public void setPrep_time(String prep_time) {
        this.prep_time = prep_time;
    }

    public String getFood_description() {
        return food_description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setFood_description(String food_description) {
        this.food_description = food_description;
    }



    public String getFood_type() {
        return food_type;
    }

    public String getPh_no() {
        return ph_no;
    }

    public void setPh_no(String ph_no) {
        this.ph_no = ph_no;
    }

    public void setFood_type(String food_type) {
        this.food_type = food_type;
    }

    public String getHotelName() {
        return hotelName;
    }

    public String getContact_person() {
        return contact_person;
    }

    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getRequestedByNgoPhone() {
        return requestedByNgoPhone;
    }

    public void setRequestedByNgoPhone(String requestedByNgoPhone) {
        this.requestedByNgoPhone = requestedByNgoPhone;
    }

    public String getClaimedByNgoPhone() {
        return claimedByNgoPhone;
    }

    public void setClaimedByNgoPhone(String claimedByNgoPhone) {
        this.claimedByNgoPhone = claimedByNgoPhone;
    }
    public String getClaimedByNgoName() {
        return claimedByNgoName;
    }

    public void setClaimedByNgoName(String claimedByNgoName) {
        this.claimedByNgoName = claimedByNgoName;
    }

    public String getRequestedByNgoName() {
        return requestedByNgoName;
    }

    public void setRequestedByNgoName(String requestedByNgoName) {
        this.requestedByNgoName = requestedByNgoName;
    }
}
