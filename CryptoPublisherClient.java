import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.rmi.Naming;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import javax.swing.JOptionPane;

import java.awt.event.*;

public class CryptoPublisherClient {

    private static Publisher publisher;

    public static void main(String[] args) {
        handleLogin();
    }

    // private static void setupLoginGUI() {
    // JFrame loginFrame = createFrame("Login", 300, 150);
    // loginFrame.setLayout(new GridLayout(3, 2));

    // JTextField usernameField = new JTextField();
    // JPasswordField passwordField = new JPasswordField();
    // JButton loginButton = new JButton("Login");

    // // Populate login frame
    // loginFrame.add(new JLabel("Username:"));
    // loginFrame.add(usernameField);
    // loginFrame.add(new JLabel("Password:"));
    // loginFrame.add(passwordField);
    // loginFrame.add(new JLabel()); // Empty cell
    // loginFrame.add(loginButton);

    // loginFrame.setVisible(true);

    // // Login action
    // loginButton.addActionListener(e -> handleLogin(usernameField, passwordField,
    // loginFrame));
    // }

    private static void handleLogin() {
        try {
            // Look up the LoginService in the RMI registry
            String serverAddress = "rmi://" + InetAddress.getLocalHost() + ":1099";
            LoginService loginService = (LoginService) Naming.lookup(serverAddress + "/LoginService");

            // Choose user type
            String[] options = { "Publisher", "Subscriber" };
            String userType = (String) JOptionPane.showInputDialog(null, "Select user type:",
                    "Login", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if (userType == null) {
                System.out.println("User cancelled login.");
                return;
            }

            // Prompt for credentials
            String username = JOptionPane.showInputDialog("Username:");
            String password = JOptionPane.showInputDialog("Password:");

            boolean isAuthenticated = false;
            if ("Publisher".equals(userType)) {
                isAuthenticated = loginService.authenticatePublisher(username, password);
            } else if ("Subscriber".equals(userType)) {
                isAuthenticated = loginService.authenticateSubscriber(username, password);
            }

            if (isAuthenticated) {
                JOptionPane.showMessageDialog(null, "Login successful!");
                JButton publishButton = new JButton("Go to Publish GUI");
                publishButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        publishGUI();
                    }
                });

                JFrame frame = new JFrame("Publisher Dashboard");
                frame.setSize(300, 100);
                frame.setLayout(new FlowLayout());
                frame.add(publishButton);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);

            } else {
                JOptionPane.showMessageDialog(null, "Invalid credentials. Please try again.");

            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error connecting to server: " + e.getMessage());
        }
    }

    public static void publishGUI() {
        // Collecting the information needed for the publishing
        String topic = JOptionPane.showInputDialog("Enter Topic:");
        String headline = JOptionPane.showInputDialog("Enter Headline:");
        String content = JOptionPane.showInputDialog("Enter Content:");
        String priceStr = JOptionPane.showInputDialog("Enter Price:");
        String cryptoName = JOptionPane.showInputDialog("Enter Crypto Name:");

        // Validate the price input
        double price = 0;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid price. Please enter a valid number.");
            return;
        }

        // Creating the CryptoObject with the input data
        CryptoObject cryptoNews = new CryptoObject(headline, content, topic, price, cryptoName);

        try {
            String serverAddress = "rmi://" + InetAddress.getLocalHost() + ":1099";

            // Look up the remote Publisher object in the RMI registry
            Publisher publisher = (Publisher) Naming.lookup(serverAddress + "/CryptoPublisher");

            // Send the CryptoObject to the server
            publisher.sendCryptoObject(cryptoNews);
            JOptionPane.showMessageDialog(null, "CryptoObject sent to the server!");

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error sending CryptoObject: " + ex.getMessage());
        }
    }

    // private static void setupPublisherGUI() {
    // JFrame frame = createFrame("Crypto Publisher", 400, 300);
    // frame.setLayout(new GridLayout(6, 2));

    // // Input fields
    // JTextField topicField = new JTextField();
    // JTextField headlineField = new JTextField();
    // JTextField contentField = new JTextField();
    // JTextField priceField = new JTextField();
    // JTextField cryptoNameField = new JTextField();

    // JButton publishButton = new JButton("Publish");

    // // Add components to the frame
    // frame.add(new JLabel("Topic:"));
    // frame.add(topicField);
    // frame.add(new JLabel("Headline:"));
    // frame.add(headlineField);
    // frame.add(new JLabel("Content:"));
    // frame.add(contentField);
    // frame.add(new JLabel("Price:"));
    // frame.add(priceField);
    // frame.add(new JLabel("Crypto Name:"));
    // frame.add(cryptoNameField);
    // frame.add(publishButton);

    // publishButton.addActionListener(
    // e -> handlePublish(topicField, headlineField, contentField, priceField,
    // cryptoNameField, frame));

    // frame.setVisible(true);
    // }

    // private static void handlePublish(JTextField topicField, JTextField
    // headlineField, JTextField contentField,
    // JTextField priceField, JTextField cryptoNameField, JFrame frame) {
    // try {
    // // Get input data
    // String topic = topicField.getText();
    // String headline = headlineField.getText();
    // String content = contentField.getText();
    // double price = Double.parseDouble(priceField.getText());
    // String cryptoName = cryptoNameField.getText();

    // // Create and send CryptoObject
    // CryptoObject cryptoNews = new CryptoObject(headline, content, topic, price,
    // cryptoName);
    // publisher.sendCryptoObject(cryptoNews);
    // JOptionPane.showMessageDialog(frame, "CryptoObject sent to the server!");

    // // Clear fields
    // clearFields(topicField, headlineField, contentField, priceField,
    // cryptoNameField);

    // } catch (Exception ex) {
    // showError("Error sending CryptoObject: " + ex.getMessage(), frame);
    // }
    // }

    // private static JFrame createFrame(String title, int width, int height) {
    // JFrame frame = new JFrame(title);
    // frame.setSize(width, height);
    // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // return frame;
    // }

    // private static void clearFields(JTextField... fields) {
    // for (JTextField field : fields) {
    // field.setText("");
    // }
    // }

    // private static void showError(String message, Component parent) {
    // JOptionPane.showMessageDialog(parent, message, "Error",
    // JOptionPane.ERROR_MESSAGE);
    // }
}
