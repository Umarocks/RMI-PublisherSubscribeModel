import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JOptionPane;
import java.util.Set;

public class DealingRoomServer extends UnicastRemoteObject implements Publisher, Subscriber {
    private HashMap<String, ArrayList<CryptoObject>> cryptoMap = new HashMap<>();

    protected DealingRoomServer() throws RemoteException {
        super();
        loadCryptoMapFromFile();
    }

    @Override
    public Set<String> getCryptoKeys() throws RemoteException {
        // Return the Set of keys (i.e., names of cryptocurrencies) from the map
        try {
            System.out.println("Sending Crypto Keys to client...");
            System.out.println("Crypto Keys: " + cryptoMap.keySet());
            return new HashSet<>(cryptoMap.keySet());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error sending CryptoObject: " + ex.getMessage());
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void loadCryptoMapFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("PublishedArticles.txt"))) {
            Object obj = in.readObject();
            cryptoMap = (HashMap<String, ArrayList<CryptoObject>>) obj;
            System.out.println("HashMap loaded from file.");
            System.out.println("Loaded Crypto Map: " + cryptoMap);
        } catch (FileNotFoundException e) {
            System.out.println("File not found, initializing empty map.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error loading HashMap from file.");
        }
    }

    public void saveCryptoMapToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("PublishedArticles.txt"))) {
            out.writeObject(cryptoMap); // Write the entire HashMap to the file
            System.out.println("HashMap saved to file.");
            System.out.println("Loaded Crypto Map: " + cryptoMap);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving HashMap to file.");
        }
    }

    @Override
    public void receiveCryptoObject(CryptoObject cryptoObject) throws RemoteException {
        System.out.println("Received Crypto Object:");
        System.out.println(cryptoObject);
        String cryptoName = cryptoObject.getCryptoName();
        if (cryptoMap.containsKey(cryptoName)) {
            // If the key exists, add the new CryptoObject to the existing list
            cryptoMap.get(cryptoName).add(cryptoObject);
        } else {
            // If the key does not exist, create a new ArrayList and add the CryptoObject
            ArrayList<CryptoObject> newList = new ArrayList<>();
            newList.add(cryptoObject);
            cryptoMap.put(cryptoName, newList);
        }
        saveCryptoMapToFile();
    }

    public static void main(String[] args) {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            System.setProperty("java.rmi.server.hostname", inetAddress.getHostAddress());
            System.out.println("Server IP: " + inetAddress.getHostAddress());
            // Create and export the DealingRoomServer object
            DealingRoomServer server = new DealingRoomServer();
            // Create the RMI registry on port 1099
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            LoginServiceImpl loginService = new LoginServiceImpl();
            Naming.rebind("rmi://" + inetAddress + ":1099/LoginService", loginService);
            // Bind the server object with the unique binding name
            Naming.rebind("rmi://" + inetAddress + ":1099/CryptoPublisher", server);
            Naming.rebind("rmi://" + inetAddress + ":1099/TopicList", server);

            System.out.println("Server ready to receive CryptoObject...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
