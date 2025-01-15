package com.bakehouse.controller;

import com.bakehouse.model.Cart;
import com.bakehouse.model.Product;

public class CartController {
    private Cart cart;

    public CartController() {
        cart = new Cart();
    }

    public void addToCart(Product product) {
        cart.addItem(product);
    }

    public void removeFromCart(Product product) {
        cart.removeItem(product);
    }

    public Cart getCart() {
        return cart;
    }

    public double getTotal() {
        return cart.calculateTotal();
    }
}
