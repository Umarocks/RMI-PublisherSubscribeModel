import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;

public class CryptoPublisherClient {

    public static void main(String[] args) {
        // Create the frame for the GUI
        JFrame frame = new JFrame("Crypto Publisher");

        // Set the layout and dimensions
        frame.setLayout(new GridLayout(6, 2));
        frame.setSize(400, 300);

        // Labels and input fields for topic, headline, etc.
        JLabel topicLabel = new JLabel("Topic:");
        JTextField topicField = new JTextField();
        JLabel headlineLabel = new JLabel("Headline:");
        JTextField headlineField = new JTextField();
        JLabel contentLabel = new JLabel("Content:");
        JTextField contentField = new JTextField();
        JLabel priceLabel = new JLabel("Price:");
        JTextField priceField = new JTextField();
        JLabel cryptoNameLabel = new JLabel("Crypto Name:");
        JTextField cryptoNameField = new JTextField();

        // Button to publish the data
        JButton publishButton = new JButton("Publish");

        // Add components to the frame
        frame.add(topicLabel);
        frame.add(topicField);
        frame.add(headlineLabel);
        frame.add(headlineField);
        frame.add(contentLabel);
        frame.add(contentField);
        frame.add(priceLabel);
        frame.add(priceField);
        frame.add(cryptoNameLabel);
        frame.add(cryptoNameField);
        frame.add(publishButton);

        // Action listener for the Publish button
        publishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // Get the input data from the fields
                    String topic = topicField.getText();
                    String headline = headlineField.getText();
                    String content = contentField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    String cryptoName = cryptoNameField.getText();

                    // Create the CryptoObject with the input data
                    CryptoObject cryptoNews = new CryptoObject(headline, content, topic, price, cryptoName);

                    // Look up the remote Publisher object in the RMI registry
                    Publisher publisher = (Publisher) Naming.lookup("rmi://localhost/CryptoPublisher");

                    // Send the CryptoObject to the server
                    publisher.sendCryptoObject(cryptoNews);
                    JOptionPane.showMessageDialog(frame, "CryptoObject sent to the server!");

                    // Clear the fields after sending
                    topicField.setText("");
                    headlineField.setText("");
                    contentField.setText("");
                    priceField.setText("");
                    cryptoNameField.setText("");

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error sending CryptoObject: " + ex.getMessage());
                }
            }
        });

        // Set the frame to be visible and set default close operation
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
