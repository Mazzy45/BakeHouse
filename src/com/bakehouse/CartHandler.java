package src.com.bakehouse;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CartHandler {

    // Thread-safe in-memory cart data
    private static final Map<Integer, CartItem> cart = new ConcurrentHashMap<>();

    // Add to cart handler
    public static class AddToCartHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            Map<String, String> queryParams = parseQueryParams(query);

            String name = queryParams.get("name");
            String price = queryParams.get("price");
            String productId = queryParams.get("productId");

            if (name != null && price != null && productId != null) {
                try {
                    int id = Integer.parseInt(productId);
                    double parsedPrice = Double.parseDouble(price);

                    CartItem item = cart.getOrDefault(id, new CartItem(id, name, parsedPrice, 0));
                    item.quantity += 1; // Increment quantity
                    cart.put(id, item);

                    System.out.println("Item added to cart: " + name);

                    String response = "Item added to cart: " + name;
                    exchange.getResponseHeaders().set("Content-Type", "text/plain");
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } catch (NumberFormatException e) {
                    String errorResponse = "Invalid productId or price format!";
                    exchange.getResponseHeaders().set("Content-Type", "text/plain");
                    exchange.sendResponseHeaders(400, errorResponse.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(errorResponse.getBytes());
                    os.close();
                }
            } else {
                String errorResponse = "Missing product details!";
                exchange.getResponseHeaders().set("Content-Type", "text/plain");
                exchange.sendResponseHeaders(400, errorResponse.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(errorResponse.getBytes());
                os.close();
            }
        }
    }

    // View cart handler
    public static class ViewCartHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            StringBuilder response = new StringBuilder();
            for (CartItem item : cart.values()) {
                response.append(item.productId).append("|")
                        .append(item.name).append("|")
                        .append(item.price).append("|")
                        .append(item.quantity).append("\n");
            }

            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(200, response.toString().getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.toString().getBytes());
            os.close();
        }
    }

    // Remove item from cart handler
    public static class RemoveFromCartHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String query = exchange.getRequestURI().getQuery();
            Map<String, String> queryParams = parseQueryParams(query);

            String productId = queryParams.get("productId");
            if (productId != null) {
                try {
                    int id = Integer.parseInt(productId);
                    if (cart.containsKey(id)) {
                        cart.remove(id);
                        System.out.println("Item removed from cart, productId: " + productId);

                        String response = "Item removed from cart!";
                        exchange.getResponseHeaders().set("Content-Type", "text/plain");
                        exchange.sendResponseHeaders(200, response.getBytes().length);
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    } else {
                        String response = "Item not found in cart!";
                        exchange.getResponseHeaders().set("Content-Type", "text/plain");
                        exchange.sendResponseHeaders(404, response.getBytes().length);
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    }
                } catch (NumberFormatException e) {
                    String errorResponse = "Invalid productId format!";
                    exchange.getResponseHeaders().set("Content-Type", "text/plain");
                    exchange.sendResponseHeaders(400, errorResponse.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(errorResponse.getBytes());
                    os.close();
                }
            } else {
                String errorResponse = "Missing productId parameter!";
                exchange.getResponseHeaders().set("Content-Type", "text/plain");
                exchange.sendResponseHeaders(400, errorResponse.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(errorResponse.getBytes());
                os.close();
            }
        }
    }

    // Clear cart handler
    public static class ClearCartHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            cart.clear();
            System.out.println("Cart cleared!");

            String response = "Cart cleared!";
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    // CartItem class to represent a cart item
    public static class CartItem {
        int productId;
        String name;
        double price;
        int quantity;

        CartItem(int productId, String name, double price, int quantity) {
            this.productId = productId;
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }
    }

    // Utility function to parse query parameters
    private static Map<String, String> parseQueryParams(String query) {
        Map<String, String> params = new HashMap<>();
        if (query == null || query.isEmpty()) {
            return params; // Return empty map for invalid queries
        }
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                params.put(keyValue[0], URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8));
            }
        }
        return params;
    }
}
