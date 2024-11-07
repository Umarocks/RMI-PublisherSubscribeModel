import javax.swing.*;
import java.awt.*;
import java.rmi.Naming;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.awt.event.*;

public class CryptoPublisherClient {

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
            publisher.receiveCryptoObject(cryptoNews);
            JOptionPane.showMessageDialog(null, "CryptoObject sent to the server!");

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error sending CryptoObject: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        handleLogin();
    }

}
