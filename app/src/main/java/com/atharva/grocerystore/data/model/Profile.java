package com.atharva.grocerystore.data.model;

import java.util.List;

public class Profile {

    private String name;
    private String email;
    private String phone;
    private String address;
    private List<Order> orders;

    public Profile() {
    }

    public Profile(String name, String email, String phone, String address, List<Order> orders) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.orders = orders;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
