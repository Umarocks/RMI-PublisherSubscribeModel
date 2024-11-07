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
import java.util.Set;
import java.awt.event.*;

public class CryptoPublisherClient {
    public static String serverAddressString = "rmi://Umar/10.91.80.240:1099";

    private static void handleLogin() {

        try {
            // Look up the LoginService in the RMI registry

            // String serverAddress = "rmi://" + InetAddress.getLocalHost() + ":1099";
            // System.out.println("Connecting to server at: " + serverAddress +
            // "/LoginService");
            LoginService loginService = (LoginService) Naming
                    .lookup(serverAddressString + "/LoginService");
            System.out.println("Connectedto server at: " + "10.0.0.239" + "/LoginService");

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
                JButton getCryptoTypesButton = new JButton("Get Crypto Types");
                getCryptoTypesButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        getCryptoTypes();
                    }
                });
                JFrame frame = new JFrame("Publisher Dashboard");
                frame.setSize(300, 100);
                frame.setLayout(new FlowLayout());
                frame.add(publishButton);
                frame.add(getCryptoTypesButton);
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

    public static void getCryptoTypes() {

        try {
            // Lookup the SubscriberServer in the RMI registry
            Subscriber subscriber = (Subscriber) Naming.lookup(serverAddressString + "/TopicList");

            // Request the list of topics
            Set<String> topics = subscriber.getCryptoKeys();

            // Display the received topics
            System.out.println("Available topics from the Subscriber Server:");
            for (String topic : topics) {
                System.out.println("- " + topic);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

            // Look up the remote Publisher object in the RMI registry
            Publisher publisher = (Publisher) Naming.lookup(serverAddressString + "/CryptoPublisher");

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
