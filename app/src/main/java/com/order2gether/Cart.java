package com.order2gether;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by aditya on 11/17/15.
 */
public class Cart {
    String location;
    String restaurantName;
    ArrayList<CartItem> cart;
    Double totalPrice;
    String merchantID;

    public Cart(){
        location="";
        merchantID="";
        restaurantName="";
        cart = new ArrayList<CartItem>();
        totalPrice = 0.0;
    }

    public void addToCart(String name, String id, String description, Double price, int quantity){
        cart.add(new CartItem(name, id, description, price, quantity));
        totalPrice+=price;
    }

    public void removeFromCart(CartItem cartItem){
        cart.remove(cartItem);
    }

    public Double getTotalPrice(){
        return totalPrice;
    }

    public ArrayList<String> getCartItems(){
        ArrayList<String> list = new ArrayList<String>();

        for(CartItem c : cart){
           list.add(c.name);
        }
        return list;
    }

    public String getCartIDs(){
        String ids="";

        for(int i = 0; i < cart.size(); i++){
            if (i!=cart.size()-1)
                ids += cart.get(i).getID() + "|";
            else
                ids += cart.get(i).getID();
        }

        return ids;
    }

    public int size(){
        return cart.size();
    }

    public void setLocation(String loc){
        location = loc;
    }

    public String getLocation(){
        return location;
    }

    public void setMerchantID(String id){
        merchantID = id;
    }

    public String getMerchantID(){
        return merchantID;
    }

    public void setName(String name){
        restaurantName = name;
    }

    public String getRestaurantName(){
        return restaurantName;
    }

    //public String cartToString();

    public class CartItem{

        String name;
        String id;
        String description;
        Double price;
        int quantity;

        public CartItem(String name, String id, String description, Double price, int quantity){
            this.name = name;
            this.id = id;
            this.description = description;
            this.price = price;
            this.quantity = quantity;
        }

        public String getID(){
            return id;
        }

    }


}
