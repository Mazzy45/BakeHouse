package com.bakehouse.view;

import com.bakehouse.controller.CartController;
import com.bakehouse.controller.ProductController;
import com.bakehouse.model.Product;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class BakeHouseGUI extends Application {
    private ProductController productController;
    private CartController cartController;

    // Default constructor
    public BakeHouseGUI() {
        // Initialize the controllers in the default constructor
        this.productController = new ProductController();
        this.cartController = new CartController();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("BakeHouse - Bakery E-Commerce");

        // Set up the product list and cart UI
        List<Product> products = productController.getProducts();
        VBox productBox = new VBox();
        for (Product product : products) {
            Button button = new Button("Add to Cart: " + product.getName() + " - $" + product.getPrice());
            button.setOnAction(e -> cartController.addToCart(product));
            productBox.getChildren().add(button);
        }

        // Display cart
        Button viewCartButton = new Button("View Cart");
        viewCartButton.setOnAction(e -> showCart(primaryStage));

        VBox layout = new VBox(10);
        layout.getChildren().addAll(productBox, viewCartButton);

        Scene scene = new Scene(layout, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showCart(Stage stage) {
        List<Product> cartItems = cartController.getCart().getItems();
        VBox cartBox = new VBox();
        for (Product product : cartItems) {
            cartBox.getChildren().add(new Label(product.getName() + " - $" + product.getPrice()));
        }

        Label totalLabel = new Label("Total: $" + cartController.getTotal());
        Button backButton = new Button("Back to Shop");
        backButton.setOnAction(e -> start(stage));

        cartBox.getChildren().addAll(totalLabel, backButton);

        Scene scene = new Scene(cartBox, 300, 250);
        stage.setScene(scene);
        stage.show();
    }

    // Main method to launch the application
    public static void main(String[] args) {
        launch(args);
    }
}
