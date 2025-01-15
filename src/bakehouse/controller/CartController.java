package bakehouse.controller;

import bakehouse.model.Cart;
import bakehouse.model.Product;

public class CartController {
    private Cart cart;

    public CartController() {
        cart = new Cart();
    }

    public void addToCart(Product product) {
        cart.addProduct(product);
    }

    public Cart getCart() {
        return cart;
    }
}
