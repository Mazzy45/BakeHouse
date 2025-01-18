package src.com.bakehouse;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class CartHandler {

    // In-memory cart data
    private static Map<String, CartItem> cartItems = new HashMap<>();

    // Handler to add items to the cart
    static class AddToCartHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Allow cross-origin requests (CORS)
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

            // Get the request body (form data sent by the client)
            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }

            // Parse the form data manually
            String[] pairs = requestBody.toString().split("&");
            String productName = null, price = null, quantity = null;

            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    String key = URLDecoder.decode(keyValue[0], "UTF-8");
                    String value = URLDecoder.decode(keyValue[1], "UTF-8");

                    if ("productName".equals(key)) {
                        productName = value;
                    } else if ("price".equals(key)) {
                        price = value;
                    } else if ("quantity".equals(key)) {
                        quantity = value;
                    }
                }
            }

            // Check if all parameters are present
            if (productName != null && price != null && quantity != null) {
                try {
                    // Format price to 2 decimal places using String.format()
                    double parsedPrice = Double.parseDouble(price);
                    String formattedPrice = String.format("%.2f", parsedPrice); // Fix price to 2 decimals

                    int parsedQuantity = Integer.parseInt(quantity); // Convert quantity to int
                    updateCartInMemory(productName, formattedPrice, parsedQuantity);

                    // Respond with a success message
                    String response = "Product added to cart!";
                    exchange.sendResponseHeaders(200, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } catch (NumberFormatException e) {
                    String response = "Invalid price or quantity format!";
                    exchange.sendResponseHeaders(400, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            } else {
                String response = "Missing parameters!";
                exchange.sendResponseHeaders(400, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }

        private void updateCartInMemory(String productName, String formattedPrice, int quantity) {
            double price = Double.parseDouble(formattedPrice); // Convert the formatted price back to double
            if (cartItems.containsKey(productName)) {
                // Update the quantity and price
                CartItem existingItem = cartItems.get(productName);
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
                existingItem.setPrice(existingItem.getQuantity() * price); // Update the total price based on new quantity
            } else {
                // Add new product to the cart
                cartItems.put(productName, new CartItem(productName, price, quantity));
            }
        }
    }

    // Handler to display the cart (returns in-memory cart content as plain text)
    static class ViewCartHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Allow cross-origin requests (CORS)
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

            if (!cartItems.isEmpty()) {
                StringBuilder cartContent = new StringBuilder();
                for (CartItem item : cartItems.values()) {
                    // Formatting price to 2 decimal places when displaying
                    cartContent.append(item.toString()).append("\n");
                }

                exchange.getResponseHeaders().add("Content-Type", "text/plain");
                exchange.sendResponseHeaders(200, cartContent.toString().getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(cartContent.toString().getBytes());
                os.close();
            } else {
                String response = "Your cart is empty.";
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }

    // Class to represent a cart item
    static class CartItem {
        private String productName;
        private double price;
        private int quantity;

        public CartItem(String productName, double price, int quantity) {
            this.productName = productName;
            this.price = price;
            this.quantity = quantity;
        }

        public String getProductName() {
            return productName;
        }

        public double getPrice() {
            return price;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        @Override
        public String toString() {
            // Ensure price is always formatted with two decimals
            return productName + ", " + String.format("%.2f", price) + ", " + quantity;
        }
    }
}
