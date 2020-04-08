package com.atharva.grocerystore.data.model;

public class Product {

    private String name;
    private int price;
    private String quantity;
    private String photo;

    public Product() {
    }

    public Product(String name, int price, String quantity, String photo) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void updateProduct(Product product) {
        price = product.price;
        photo = product.photo;
        quantity = product.quantity;
    }
}
