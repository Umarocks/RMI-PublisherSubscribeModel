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
import javax.swing.JOptionPane;

public class DealingRoomServer extends UnicastRemoteObject implements Publisher {
    private HashMap<String, ArrayList<CryptoObject>> cryptoMap = new HashMap<>();

    protected DealingRoomServer() throws RemoteException {
        super();
        loadCryptoMapFromFile();
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
