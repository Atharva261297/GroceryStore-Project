package com.atharva.grocerystore.data.model;

import java.util.HashMap;
import java.util.Map;

public class Order {

    private Map<String, Integer> products;
    private Integer price;
    private String address;

    public Order() {
    }

    public Order(Map<String, Integer> products, Integer price, String address) {
        this.products = products;
        this.price = price;
        this.address = address;
    }

    public Order(Cart cart, int price, String address) {
        this.price = price;
        this.address = address;
        products = new HashMap<>();
        cart.forEach((p, i)-> {
            products.put(p.getName(), i);
        });
    }

    public Map<String, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<String, Integer> products) {
        this.products = products;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
