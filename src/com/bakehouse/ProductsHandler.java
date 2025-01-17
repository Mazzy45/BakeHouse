// 3. ProductsHandler.java
package src.com.bakehouse;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ProductsHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            List<String> products = List.of(
                    "{\"id\":1,\"name\":\"Chocolate Cake\",\"price\":20.0}",
                    "{\"id\":2,\"name\":\"Croissant\",\"price\":2.5}",
                    "{\"id\":3,\"name\":\"Cupcake\",\"price\":3.0}"
            );
            String response = "[" + String.join(",", products) + "]";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
