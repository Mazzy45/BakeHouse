// 1. BakehouseServer.java

package com.bakehouse;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class BakeHouseServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new HomePageHandler());
        server.createContext("/products", new ProductsHandler());
        server.createContext("/cart", new CartHandler());
        server.createContext("/login", new UserLoginHandler());
        server.createContext("/register", new UserRegisterHandler());
        server.setExecutor(null); // default executor
        server.start();
        System.out.println("Server running at http://localhost:8000");
    }
}