import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import javax.swing.JOptionPane;

public class DealingRoomServer extends UnicastRemoteObject implements Publisher {

    private HashMap<String, String> userLogin;
    private HashMap<String, String> publisherLogin;

    protected DealingRoomServer() throws RemoteException {
        super();
        userLogin = new HashMap<>();
        publisherLogin = new HashMap<>();
    }

    // public boolean authenticateUser(String username, String password) throws
    // RemoteException {
    // return userLogin.containsKey(username) &&
    // userLogin.get(username).equals(password);
    // }

    // public boolean authenticatePublisher(String username, String password) throws
    // RemoteException {
    // return publisherLogin.containsKey(username) &&
    // publisherLogin.get(username).equals(password);
    // }

    @Override
    public void sendCryptoObject(CryptoObject cryptoObject) throws RemoteException {
        System.out.println("Received Crypto Object:");
        System.out.println(cryptoObject);
    }

    public static void main(String[] args) {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            System.setProperty("java.rmi.server.hostname", inetAddress.getHostAddress());

            // Unique identifier for each instance
            // String cryptoName = "BTC"; // This can be dynamically set
            // String id = "001"; // Example ID
            // String uniqueBindingName = String.format("rmi://10.91.80.240:1099/Crypto",

            // cryptoName, id);

            // Create and export the DealingRoomServer object
            DealingRoomServer server = new DealingRoomServer();

            // Load login data from file
            // server.loadLoginData("./login.txt");

            // Create the RMI registry on port 1099
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            LoginServiceImpl loginService = new LoginServiceImpl();
            Naming.rebind("rmi://" + inetAddress + ":1099/LoginService", loginService);
            // Bind the server object with the unique binding name
            Naming.rebind("rmi://" + inetAddress + ":1099/CryptoPublisher", server);

            System.out.println("Server ready to receive CryptoObject...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
