package src.com.bakehouse;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class AdminLogin implements HttpHandler {
    private static final List<String> validUsernames = Arrays.asList("Mazzy45", "IzzMai", "ctmaikz", "harini00");
    private static final String validPassword = "Admin123";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            String body = new String(exchange.getRequestBody().readAllBytes());
            String[] params = body.split("&");

            String username = null;
            String password = null;

            for (String param : params) {
                String[] pair = param.split("=");
                if ("username".equals(pair[0])) username = pair[1];
                if ("password".equals(pair[0])) password = pair[1];
            }

            if (validUsernames.contains(username) && validPassword.equals(password)) {
                // Successful login: redirect to Admin Dashboard
                exchange.getResponseHeaders().add("Location", "/adminDashboard.html");
                exchange.sendResponseHeaders(302, -1);
            } else {
                // Invalid login: show error message on login page
                String errorMessage = "<html><body><h3>Invalid Username or Password</h3>" +
                        "<a href='/adminLogin.html'>Try Again</a></body></html>";
                exchange.sendResponseHeaders(200, errorMessage.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(errorMessage.getBytes());
                os.close();
            }
        } else {
            // Show login page for GET requests
            String loginPage = Files.readString(Paths.get("adminLogin.html"));
            exchange.sendResponseHeaders(200, loginPage.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(loginPage.getBytes());
            os.close();
        }
    }
}
