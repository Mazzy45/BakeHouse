package bakehouse.view;

import bakehouse.controller.UserController;
import javax.swing.*;

public class LoginGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginGUI() {
        setTitle("Login");

        JLabel usernameLabel = new JLabel("Username");
        JLabel passwordLabel = new JLabel("Password");

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            UserController userController = new UserController();
            if (userController.authenticate(username, password)) {
                JOptionPane.showMessageDialog(this, "Login Successful");
                dispose();  // Close login screen
                new ProductListGUI().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials");
            }
        });

        setLayout(null);
        usernameLabel.setBounds(50, 50, 100, 30);
        usernameField.setBounds(150, 50, 150, 30);
        passwordLabel.setBounds(50, 100, 100, 30);
        passwordField.setBounds(150, 100, 150, 30);
        loginButton.setBounds(150, 150, 100, 30);

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);

        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
