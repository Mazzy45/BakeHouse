package src.com.bakehouse;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HomePageHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Path to your index.html file
        String filePath = "public/index.html";

        try {
            // Read the file content
            byte[] response = Files.readAllBytes(Paths.get(filePath));

            // Send HTTP response headers
            exchange.sendResponseHeaders(200, response.length);
            exchange.getResponseHeaders().add("Content-Type", "text/html");


            // Write the file content to the response body
            OutputStream os = exchange.getResponseBody();
            os.write(response);
            os.close();
        } catch (IOException e) {
            // Handle file not found or other errors
            String errorResponse = "<h1>404 Not Found</h1>";
            exchange.sendResponseHeaders(404, errorResponse.length());
            OutputStream os = exchange.getResponseBody();
            os.write(errorResponse.getBytes());
            os.close();
        }
    }
}
