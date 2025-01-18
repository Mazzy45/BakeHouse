package src.com.bakehouse;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ProductsHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Read product data from the CSV file
        List<String> productLines = Files.readAllLines(Paths.get("products.csv"));

        // Start building the HTML response
        StringBuilder response = new StringBuilder();
        response.append("<html><body><h2>Our Products</h2>");

        // Separate products by type
        response.append("<h3>Cakes</h3><div class='product-grid'> id='cake-products'>");
        for (int i = 1; i < productLines.size(); i++) {
            String line = productLines.get(i);
            String[] details = line.split(",");

            // Skip invalid lines or incomplete data
            if (details.length != 6) continue;

            String typecode = details[0];
            String id = details[1];
            String type = details[2];
            String name = details[3];
            String price = details[4];
            String imageUrl = details[5];

            // Only include cakes
            if ("2".equals(typecode)) {
                response.append("<div class='product-card'>")
                        .append("<img src='").append(imageUrl).append("' alt='").append(name).append("'>")
                        .append("<h3>").append(name).append("</h3>")
                        .append("<p>One whole cake: RM").append(price).append("(8' whole cake)</p>")
                        .append("<button class='btn' onclick='addToCart(").append(id).append(")'>Add to Cart</button>")
                        .append("</div>");
            }
        }
        response.append("</div>"); // End Cake section

        response.append("<h3>Cookies</h3><div class='product-grid' id='cookie-products'>");
        // Now process cookies (typecode 1)
        for (int i = 1; i < productLines.size(); i++) {
            String line = productLines.get(i);
            String[] details = line.split(",");

            // Skip invalid lines or incomplete data
            if (details.length != 6) continue;

            String typecode = details[0];
            String id = details[1];
            String type = details[2];
            String name = details[3];
            String price = details[4];
            String imageUrl = details[5];

            // Only include cookies
            if ("1".equals(typecode)) {
                response.append("<div class='product-card'>")
                        .append("<img src='").append(imageUrl).append("' alt='").append(name).append("'>")
                        .append("<h3>").append(name).append("</h3>")
                        .append("<p>23 pieces: RM").append(price).append("</p>")
                        .append("<button class='btn' onclick='addToCart(").append(id).append(")'>Add to Cart</button>")
                        .append("</div>");
            }
        }
        response.append("</div>"); // End Cookie section

        response.append("</body></html>");

        // Send response back to the client
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }
}


/*
package src.com.bakehouse;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ProductsHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Read product data from a plain text file
        List<String> productLines = Files.readAllLines(Paths.get("data/products.txt"));

        // Build the HTML response dynamically
        StringBuilder response = new StringBuilder("<html><body><h2>Our Products</h2><div class='product-grid'>");

        for (String line : productLines) {
            // Each line is formatted as: id|name|price|image
            String[] details = line.split("\\|");
            if (details.length == 4) {
                String id = details[0];
                String name = details[1];
                String price = details[2];
                String image = details[3];

                response.append("<div class='product-card'>")
                        .append("<img src='").append(image).append("' alt='").append(name).append("'>")
                        .append("<h3>").append(name).append("</h3>")
                        .append("<p>").append(price).append("</p>")
                        .append("<button class='btn' onclick='addToCart(").append(id).append(")'>Add to Cart</button>")
                        .append("</div>");
            }
        }

        response.append("</div><a href='/cart'>Go to Cart</a></body></html>");

        // Send the response
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.toString().getBytes());
        os.close();
    }
}
*/