// 4. CartHandler.java
package src.com.bakehouse;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;

public class CartHandler implements HttpHandler {
    private static final String CART_FILE = "cart.txt";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response;
        if ("POST".equals(exchange.getRequestMethod())) {
            handleAddToCart(exchange);
            response = "<html><body><h2>Item added to cart!</h2><a href=\"/products\">Back to Products</a></body></html>";
        } else {
            response = loadCart();
        }
        sendResponse(exchange, response);
    }

    private void handleAddToCart(HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
        BufferedReader reader = new BufferedReader(isr);
        String[] params = reader.readLine().split("&");
        String product = params[0].split("=")[1];
        String quantity = params[1].split("=")[1];

        try (FileWriter writer = new FileWriter(CART_FILE, true)) {
            writer.write(product + " - Quantity: " + quantity + "\n");
        }
    }

    private String loadCart() throws IOException {
        StringBuilder response = new StringBuilder("<html><body><h2>Your Cart</h2><ul>");
        try (BufferedReader reader = new BufferedReader(new FileReader(CART_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append("<li>").append(line).append("</li>");
            }
        }
        response.append("</ul><a href=\"/checkout\">Proceed to Checkout</a></body></html>");
        return response.toString();
    }

    private void sendResponse(HttpExchange exchange, String response) throws IOException {
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
