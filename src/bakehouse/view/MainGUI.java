package bakehouse.view;

import javax.swing.*;

public class MainGUI {
    public static void main(String[] args) {
        JFrame frame = new JFrame("BakeHouse");
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            new LoginGUI().setVisible(true);
        });

        frame.add(loginButton);
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
