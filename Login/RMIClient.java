
import java.rmi.Naming;
import javax.swing.*;

public class RMIClient {
    public static void main(String[] args) {
        try {
            // Look up the LoginService in the RMI registry
            LoginService loginService = (LoginService) Naming.lookup("rmi://localhost/LoginService");

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
            } else {
                JOptionPane.showMessageDialog(null, "Invalid credentials. Please try again.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error connecting to server: " + e.getMessage());
        }
    }
}
