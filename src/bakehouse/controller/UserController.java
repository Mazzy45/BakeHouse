package bakehouse.controller;

import bakehouse.model.User;

public class UserController {
    public boolean authenticate(String username, String password) {
        // Mock authentication
        return "admin".equals(username) && "password123".equals(password);
    }
}
