package com.atharva.grocerystore.data;

import com.atharva.grocerystore.data.model.Cart;
import com.atharva.grocerystore.data.model.Order;
import com.atharva.grocerystore.data.model.Product;
import com.atharva.grocerystore.data.model.Profile;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.Map;

public class RuntimeData {
    public static FirebaseUser user = null;
    public static Profile userData = null;

    public static DatabaseReference userDb;
    public static DatabaseReference productDb;

    public static Map<String, Product> products;

    public static Order tempOrder;
    public static Cart cart;

}
