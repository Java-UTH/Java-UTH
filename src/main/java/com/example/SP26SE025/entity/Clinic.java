package com.example.SP26SE025.entity;

import jakarta.persistence.*;

@Entity
public class Clinic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String phone;

    @Enumerated(EnumType.STRING)
    private ClinicStatus status;

    // ===== GETTERS & SETTERS =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ClinicStatus getStatus() {
        return status;
    }

    public void setStatus(ClinicStatus status) {
        this.status = status;
    }
}
