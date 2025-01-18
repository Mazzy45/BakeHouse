package src.com.bakehouse;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class CheckoutHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Allow cross-origin requests (CORS)
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        // Check if it's a POST request
        if ("POST".equals(exchange.getRequestMethod())) {
            // Parse multipart form data (including file upload)
            Map<String, String> formData = parseMultipartFormData(exchange);

            String pickupDate = formData.get("pickupDate");
            String pickupTime = formData.get("pickupTime");
            String address = formData.get("address");
            String receiptFilePath = formData.get("paymentReceipt");

            // Handle the checkout logic (e.g., save order to database or confirmation)
            if (pickupDate != null && pickupTime != null && address != null && receiptFilePath != null) {
                String response = "Order placed successfully! Pickup on " + pickupDate + " at " + pickupTime;
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                String response = "Missing required fields!";
                exchange.sendResponseHeaders(400, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }

    // Utility method to parse multipart form data
    private Map<String, String> parseMultipartFormData(HttpExchange exchange) throws IOException {
        Map<String, String> formData = new HashMap<>();

        // Get the content type header
        String contentType = exchange.getRequestHeaders().getFirst("Content-Type");

        // Parse boundary from content type header
        String boundary = "--" + contentType.split("boundary=")[1];

        InputStream is = exchange.getRequestBody();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder body = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line).append("\n");
        }

        // Split the body into individual parts based on boundary
        String[] parts = body.toString().split(Pattern.quote(boundary));
        for (String part : parts) {
            if (part.trim().isEmpty()) continue;

            // Extract field name and value
            String fieldName = null;
            String fieldValue = null;
            String fileName = null;

            // Split part into lines
            String[] partLines = part.split("\n");

            for (String partLine : partLines) {
                if (partLine.startsWith("Content-Disposition")) {
                    // Extract the field name and file name
                    Matcher matcher = Pattern.compile("name=\"(.*?)\"").matcher(partLine);
                    if (matcher.find()) {
                        fieldName = matcher.group(1);
                    }
                    Matcher fileMatcher = Pattern.compile("filename=\"(.*?)\"").matcher(partLine);
                    if (fileMatcher.find()) {
                        fileName = fileMatcher.group(1);
                    }
                } else if (partLine.startsWith("Content-Type")) {
                    // File content type (we're ignoring it in this case)
                } else if (partLine.trim().isEmpty()) {
                    // If the part is empty, continue
                } else {
                    // The actual content of the field (text or file)
                    fieldValue = partLine.trim();
                }
            }

            // If we found a file, save it
            if (fileName != null) {
                // Save file to server (adjust path as needed)
                String filePath = "uploads/" + fileName;
                Files.write(Paths.get(filePath), body.toString().getBytes());
                formData.put("paymentReceipt", filePath);
            } else if (fieldName != null && fieldValue != null) {
                formData.put(fieldName, fieldValue);
            }
        }

        return formData;
    }
}
