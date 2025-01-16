// 3. ProductsHandler.java
package src.com.bakehouse;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ProductsHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String products = new String(Files.readAllBytes(Paths.get("data/products.txt")));
        String response = "<html><body><h2>Our Products</h2><pre>" + products + "</pre>" +
                "<a href='/cart'>Go to Cart</a></body></html>";
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}