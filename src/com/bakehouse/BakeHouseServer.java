package src.com.bakehouse;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.net.URLDecoder;
import java.util.*;

public class BakeHouseServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Serve static files (e.g., the product page)
        server.createContext("/", new StaticFileHandler("public"));

        // Add Cart Handlers
        server.createContext("/addToCart", new CartHandler.AddToCartHandler());
        server.createContext("/viewCart", new CartHandler.ViewCartHandler());

        System.out.println("Server is running on http://localhost:8080");
        server.start();
    }
}

class StaticFileHandler implements HttpHandler {
    private final String rootDirectory;

    public StaticFileHandler(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestedPath = exchange.getRequestURI().getPath();
        if (requestedPath.equals("/")) {
            requestedPath = "/index.html"; // Default to index.html
        }

        File file = new File(rootDirectory + requestedPath).getCanonicalFile();
        if (!file.exists() || !file.getPath().startsWith(new File(rootDirectory).getCanonicalPath())) {
            // File not found or outside root directory
            String errorResponse = "<h1>404 Not Found</h1>";
            exchange.sendResponseHeaders(404, errorResponse.length());
            OutputStream os = exchange.getResponseBody();
            os.write(errorResponse.getBytes());
            os.close();
            return;
        }

        // Set appropriate content type
        String contentType = Files.probeContentType(file.toPath());
        if (contentType != null) {
            exchange.getResponseHeaders().add("Content-Type", contentType);
        }

        // Serve the file
        exchange.sendResponseHeaders(200, file.length());
        OutputStream os = exchange.getResponseBody();
        Files.copy(file.toPath(), os);
        os.close();
    }
}

