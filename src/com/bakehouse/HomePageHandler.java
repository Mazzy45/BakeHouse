// 2. HomePageHandler.java

package src.com.bakehouse;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;

public class HomePageHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "<html><head><link rel='stylesheet' href='/public/css/styles.css'></head>" +
                "<body><h1>Welcome to Bakehouse!</h1><a href='/products'>Browse Products</a> | " +
                "<a href='/login'>Login</a> | <a href='/register'>Register</a></body></html>";
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}