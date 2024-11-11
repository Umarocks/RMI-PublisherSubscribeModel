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
import java.util.List;

public class CryptoPublisherClient {
    public static String serverAddressString = "rmi://Umar/10.0.0.239:1099";
    public static String LoggedUsername = null;

    // private static void createArticleUI(List<CryptoObject> cryptoArticles) {
    // // Frame to hold all the buttons
    // JFrame frame = new JFrame("Crypto Articles");
    // frame.setSize(400, 300);
    // frame.setLayout(new FlowLayout());

    // // Add each article headline as a button
    // for (CryptoObject cryptoObject : cryptoArticles) {
    // JButton headlineButton = new JButton(cryptoObject.getHeadline());

    // // Action listener to show the full article when the button is clicked
    // headlineButton.addActionListener(new ActionListener() {
    // @Override
    // public void actionPerformed(ActionEvent e) {
    // // When a headline is clicked, show the article's details
    // String fullArticle = "Topic: " + cryptoObject.getTopicName() + "\n"
    // + "Crypto Name: " + cryptoObject.getCryptoName() + "\n"
    // + "Price: $" + cryptoObject.getPrice() + "\n\n"
    // + "Content: \n" + cryptoObject.getContent();

    // JOptionPane.showMessageDialog(frame, fullArticle, cryptoObject.getHeadline(),
    // JOptionPane.INFORMATION_MESSAGE);
    // }
    // });

    // // Add the headline button to the frame
    // frame.add(headlineButton);
    // }

    // // Set up the frame and make it visible
    // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // frame.setVisible(true);
    // JButton backButton = new JButton("Back"); // Back button
    // backButton.addActionListener(new ActionListener() {
    // @Override
    // public void actionPerformed(ActionEvent e) {
    // // Here we can simply call showDashboard to go back to the dashboard
    // // If you are navigating between different screens, you can hide the current
    // // window or frame
    // JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(backButton);
    // currentFrame.setVisible(false); // Hide current window

    // // Call the dashboard (showDashboard will show the dashboard UI again)
    // showDashboard();
    // }
    // });
    // frame.add(backButton);

    // }

    private static void createArticleUI(List<CryptoObject> cryptoArticles) {
        // Frame to hold all the buttons for the articles
        JFrame frame = new JFrame("Crypto Articles");
        frame.setSize(400, 300);
        frame.setLayout(new FlowLayout());

        // Add each article headline as a button
        for (CryptoObject cryptoObject : cryptoArticles) {
            JButton headlineButton = new JButton(cryptoObject.getHeadline());

            // Action listener to show the full article when the button is clicked
            headlineButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // When a headline is clicked, show the article's details
                    String fullArticle = "Topic: " + cryptoObject.getTopicName() + "\n"
                            + "Crypto Name: " + cryptoObject.getCryptoName() + "\n"
                            + "Price: $" + cryptoObject.getPrice() + "\n\n"
                            + "Content: \n" + cryptoObject.getContent();

                    JOptionPane.showMessageDialog(frame, fullArticle, cryptoObject.getHeadline(),
                            JOptionPane.INFORMATION_MESSAGE);
                }
            });

            // Add the headline button to the frame
            frame.add(headlineButton);
        }

        // // Back button to return to the dashboard
        // JButton backButton = new JButton("Back");
        // backButton.addActionListener(new ActionListener() {
        // @Override
        // public void actionPerformed(ActionEvent e) {
        // // Close this frame (Crypto Articles UI) when the back button is clicked
        // frame.dispose(); // Disposes the current frame

        // // Call the dashboard (assuming showDashboard() brings up the main dashboard)
        // showDashboard(); // Calls the method that shows your dashboard UI
        // }
        // });
        // frame.add(backButton);

        // Set up the frame and make it visible
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Don't close the app, just dispose this frame
        frame.setVisible(true);
    }

    public static void getArticles() {
        try {
            // Look up the remote Subscriber object in the RMI registry
            Subscriber subscriber = (Subscriber) Naming.lookup(serverAddressString + "/TopicList");

            // Request the list of articles for the logged in user
            List<CryptoObject> articles = subscriber.getArticleList(LoggedUsername);

            // Display the received articles
            System.out.println("Received articles from the Subscriber Server:");
            for (CryptoObject article : articles) {
                System.out.println("- " + article.getHeadline());
            }

            // Create a UI to display the articles
            createArticleUI(articles);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error getting articles: " + e.getMessage());
        }
    }

    // private static void handleLogin() {
    // try {
    // // Look up the LoginService in the RMI registry

    // // String serverAddress = "rmi://" + InetAddress.getLocalHost() + ":1099";
    // // System.out.println("Connecting to server at: " + serverAddress +
    // // "/LoginService");
    // LoginService loginService = (LoginService) Naming
    // .lookup(serverAddressString + "/LoginService");
    // System.out.println("Connectedto server at: " + "10.0.0.239" +
    // "/LoginService");
    // // Choose user type
    // String[] options = { "Publisher", "Subscriber" };
    // String userType = (String) JOptionPane.showInputDialog(null, "Select user
    // type:",
    // "Login", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    // if (userType == null) {
    // System.out.println("User cancelled login.");
    // return;
    // }
    // // Prompt for credentials
    // String username = JOptionPane.showInputDialog("Username:");
    // String password = JOptionPane.showInputDialog("Password:");
    // LoggedUsername = username;
    // boolean isAuthenticated = false;
    // if ("Publisher".equals(userType)) {
    // isAuthenticated = loginService.authenticatePublisher(username, password);
    // } else if ("Subscriber".equals(userType)) {
    // isAuthenticated = loginService.authenticateSubscriber(username, password);
    // }
    // if (isAuthenticated) {
    // JOptionPane.showMessageDialog(null, "Login successful!");
    // JButton publishButton = new JButton("Go to Publish GUI");
    // publishButton.addActionListener(new ActionListener() {
    // @Override
    // public void actionPerformed(ActionEvent e) {
    // publishGUI();
    // }
    // });
    // JButton getCryptoTypesButton = new JButton("Get Crypto Topics");
    // getCryptoTypesButton.addActionListener(new ActionListener() {
    // @Override
    // public void actionPerformed(ActionEvent e) {
    // getCryptoTypes();
    // }
    // });
    // JButton getArticlesButton = new JButton("Get Articles");
    // getArticlesButton.addActionListener(new ActionListener() {
    // @Override
    // public void actionPerformed(ActionEvent e) {
    // getArticles();
    // }
    // });
    // JTextField topicInputField = new JTextField(15); // Input field for topic
    // name
    // JButton subscribeButton = new JButton("Subscribe");
    // JButton unsubscribeButton = new JButton("Unsubscribe");
    // subscribeButton.addActionListener(new ActionListener() {
    // @Override
    // public void actionPerformed(ActionEvent e) {
    // String topicName = topicInputField.getText();
    // if (!topicName.isEmpty()) {
    // subscribe(topicName);
    // } else {
    // JOptionPane.showMessageDialog(null, "Please enter a topic name to
    // subscribe.",
    // "Input Error", JOptionPane.ERROR_MESSAGE);
    // }
    // }
    // });
    // unsubscribeButton.addActionListener(new ActionListener() {
    // @Override
    // public void actionPerformed(ActionEvent e) {
    // String topicName = topicInputField.getText();
    // if (!topicName.isEmpty()) {
    // unsubscribe(topicName); // Call the unsubscribe function
    // } else {
    // JOptionPane.showMessageDialog(null, "Please enter a topic name to
    // unsubscribe.",
    // "Input Error", JOptionPane.ERROR_MESSAGE);
    // }
    // }
    // });

    // JFrame frame = new JFrame("Publisher Dashboard");
    // frame.setSize(400, 200); // Increase frame size to accommodate both buttons
    // frame.setLayout(new FlowLayout());
    // frame.add(publishButton); // Assuming publishButton is already defined
    // frame.add(getCryptoTypesButton); // Assuming getCryptoTypesButton is already
    // defined
    // // frame.add(new JLabel("Topic:"));
    // frame.add(getArticlesButton); // Assuming getArticlesButton is already
    // defined
    // frame.add(topicInputField);
    // frame.add(subscribeButton);
    // frame.add(unsubscribeButton); // Add the unsubscribe button
    // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // frame.setVisible(true);
    // } else {
    // JOptionPane.showMessageDialog(null, "Invalid credentials. Please try
    // again.");
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // JOptionPane.showMessageDialog(null, "Error connecting to server: " +
    // e.getMessage());
    // }
    // }

    private static void handleLogin() {
        try {
            // Look up the LoginService in the RMI registry
            LoginService loginService = (LoginService) Naming.lookup(serverAddressString + "/LoginService");
            System.out.println("Connected to server at: " + "10.0.0.239" + "/LoginService");

            // Get user type
            String userType = getUserType();
            if (userType == null) {
                System.out.println("User cancelled login.");
                return;
            }

            // Prompt for credentials
            String username = JOptionPane.showInputDialog("Username:");
            String password = JOptionPane.showInputDialog("Password:");
            LoggedUsername = username;

            // Authenticate user
            boolean isAuthenticated = authenticateUser(loginService, username, password, userType);

            if (isAuthenticated) {
                JOptionPane.showMessageDialog(null, "Login successful!");
                showDashboard();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid credentials. Please try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error connecting to server: " + e.getMessage());
        }
    }

    private static String getUserType() {
        String[] options = { "Publisher", "Subscriber" };
        return (String) JOptionPane.showInputDialog(null, "Select user type:", "Login", JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);
    }

    private static boolean authenticateUser(LoginService loginService, String username, String password,
            String userType) throws RemoteException {
        boolean isAuthenticated = false;
        if ("Publisher".equals(userType)) {
            isAuthenticated = loginService.authenticatePublisher(username, password);
        } else if ("Subscriber".equals(userType)) {
            isAuthenticated = loginService.authenticateSubscriber(username, password);
        }
        return isAuthenticated;
    }

    private static void showDashboard() {
        JButton publishButton = new JButton("Go to Publish GUI");
        JButton getCryptoTypesButton = new JButton("Get Crypto Topics");
        JButton getArticlesButton = new JButton("Get Articles");

        JTextField topicInputField = new JTextField(15); // Input field for topic name
        JButton subscribeButton = new JButton("Subscribe");
        JButton unsubscribeButton = new JButton("Unsubscribe");
        JButton backButton = new JButton("Back"); // Back button
        // Initialize buttons' actions
        // initializeButtonActions(subscribeButton, unsubscribeButton, topicInputField);

        // Back button action to go back to the dashboard
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Here we can simply call showDashboard to go back to the dashboard
                // If you are navigating between different screens, you can hide the current
                // window or frame
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(backButton);
                currentFrame.setVisible(false); // Hide current window

                // Call the dashboard (showDashboard will show the dashboard UI again)
                showDashboard();
            }
        });
        subscribeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String topicName = topicInputField.getText();
                if (!topicName.isEmpty()) {
                    subscribe(topicName);
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a topic name to subscribe.", "Input Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        unsubscribeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String topicName = topicInputField.getText();
                if (!topicName.isEmpty()) {
                    unsubscribe(topicName); // Call the unsubscribe function
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a topic name to unsubscribe.", "Input Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        publishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                publishGUI();
            }
        });

        getCryptoTypesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getCryptoTypes();
            }
        });

        getArticlesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getArticles();
            }
        });

        // Create frame and add buttons
        JFrame frame = new JFrame("Publisher Dashboard");
        frame.setSize(400, 200); // Adjust frame size
        frame.setLayout(new FlowLayout());
        frame.add(publishButton);
        frame.add(getCryptoTypesButton);
        frame.add(getArticlesButton);
        frame.add(topicInputField);
        frame.add(subscribeButton);
        frame.add(unsubscribeButton); // Add the unsubscribe button
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // private static void initializeButtonActions(JButton subscribeButton, JButton
    // unsubscribeButton,
    // JTextField topicInputField) {

    // }

    public static void subscribe(String topicName) {
        try {
            // Look up the remote Subscriber object in the RMI registry
            Subscriber subscriber = (Subscriber) Naming.lookup(serverAddressString + "/TopicList");
            // Subscribe to the specified topic
            subscriber.subscribe(topicName, LoggedUsername);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error subscribing to topic: " + e.getMessage());
        }
    }

    public static void unsubscribe(String topicName) {
        try {
            // Look up the remote Subscriber object in the RMI registry
            Subscriber subscriber = (Subscriber) Naming.lookup(serverAddressString + "/TopicList");

            // Unsubscribe from the specified topic
            subscriber.unsubscribe(topicName, LoggedUsername);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error unsubscribing from topic: " + e.getMessage());
        }
    }

    public static void getCryptoTypes() {
        try {
            // Lookup the SubscriberServer in the RMI registry
            System.out.println(LoggedUsername);
            Subscriber subscriber = (Subscriber) Naming.lookup(serverAddressString + "/TopicList");
            // Request the list of topics
            Set<String> topics = subscriber.getCryptoKeys();
            // Display the received topics
            System.out.println("Available topics from the Subscriber Server:");
            for (String topic : topics) {
                System.out.println("- " + topic);
            }
            StringBuilder message = new StringBuilder("Available topics from the Subscriber Server:\n");
            for (String topic : topics) {
                message.append("- ").append(topic).append("\n");
            }
            JOptionPane.showMessageDialog(null, message.toString(), "Available Topics",
                    JOptionPane.INFORMATION_MESSAGE);
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
