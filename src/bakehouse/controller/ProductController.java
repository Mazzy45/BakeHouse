package bakehouse.controller;

import bakehouse.model.Product;
import java.util.List;

public class ProductController {
    public List<Product> getAllProducts() {
        // Fetch all products (mock data here)
        return List.of(new Product("Cake", 10.0, 5), new Product("Bread", 2.5, 10));
    }
}
