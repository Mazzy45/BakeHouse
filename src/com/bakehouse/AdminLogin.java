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
                exchange.getResponseHeaders().add("Location", "/admindashboard.html");
                exchange.sendResponseHeaders(302, -1);
            } else {
                // Invalid login: show error message on login page
                String errorMessage = "<html><body><h3>Invalid Username or Password</h3>" +
                        "<a href='/adminlogin.html'>Try Again</a></body></html>";
                exchange.sendResponseHeaders(200, errorMessage.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(errorMessage.getBytes());
                os.close();
            }
        } else {
            // Show login page for GET requests
            String loginPage = Files.readString(Paths.get("adminlogin.html"));
            exchange.sendResponseHeaders(200, loginPage.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(loginPage.getBytes());
            os.close();
        }
    }
}


/*
package src.com.bakehouse;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class AdminLogin implements HttpHandler {
    // Predefined admin credentials
    private static final List<String> allowedUsers = Arrays.asList("Mazzy45", "IzzMai", "ctmaikz", "harini00");
    private static final String commonPassword = "Crumbs&Codes201";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            // Parse form data
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            String[] params = body.split("&");

            String username = null;
            String password = null;

            for (String param : params) {
                String[] pair = param.split("=");
                if (pair.length == 2) {
                    if ("username".equals(pair[0])) username = pair[1];
                    if ("password".equals(pair[0])) password = pair[1];
                }
            }

            // Validate credentials
            if (allowedUsers.contains(username) && commonPassword.equals(password)) {
                // Create a session using cookies
                HttpCookie sessionCookie = new HttpCookie("session", "admin_logged_in");
                exchange.getResponseHeaders().add("Set-Cookie", sessionCookie.toString());

                // Redirect to the product page
                exchange.getResponseHeaders().add("Location", "/products");
                exchange.sendResponseHeaders(302, -1);
            } else {
                // Invalid login, reload the login page with error
                String response = "<html><body><h3>Invalid Username or Password</h3>" +
                        "<a href='/adminlogin.html'>Try Again</a></body></html>";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        } else {
            // For GET requests, serve the login page
            String loginPage = Files.readString(Paths.get("adminlogin.html"));
            exchange.sendResponseHeaders(200, loginPage.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(loginPage.getBytes());
            os.close();
        }
    }
}
*/


/*
import java.util.HashMap;
import java.util.Scanner;

public class AdminLogin {
    // Admin credentials (could later be moved to a file or database)
    private static final HashMap<String, String> adminCredentials = new HashMap<>();

    static {
        adminCredentials.put("admin", "admin123"); // Default credentials: admin/admin123
    }

    public static boolean login(String username, String password) {
        return adminCredentials.containsKey(username) && adminCredentials.get(username).equals(password);
    }
}
*/


/*
<script>
document.getElementById("loginForm").addEventListener("submit", function (e) {
    e.preventDefault();

  const username = document.getElementById("username").value;
  const password = document.getElementById("password").value;

    if (username === "admin" && password === "admin123") {
        window.location.href = "dashboard.html";
    } else {
        document.getElementById("errorMsg").textContent = "Invalid username or password!";
    }
});
</script>
*/