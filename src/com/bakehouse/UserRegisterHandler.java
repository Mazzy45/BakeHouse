// 6. UserRegisterHandler.java

package src.com.bakehouse;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;

public class UserRegisterHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "<html><body><h2>Register</h2><form method='post'>" +
                "Username: <input type='text' name='username'><br>" +
                "Password: <input type='password' name='password'><br>" +
                "<input type='submit' value='Register'></form></body></html>";
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
