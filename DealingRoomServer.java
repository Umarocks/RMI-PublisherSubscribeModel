import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class DealingRoomServer extends UnicastRemoteObject implements Publisher {

    private HashMap<String, String> userLogin;
    private HashMap<String, String> publisherLogin;

    protected DealingRoomServer() throws RemoteException {
        super();
        userLogin = new HashMap<>();
        publisherLogin = new HashMap<>();
    }

    public boolean authenticateUser(String username, String password) throws RemoteException {
        return userLogin.containsKey(username) && userLogin.get(username).equals(password);
    }

    public boolean authenticatePublisher(String username, String password) throws RemoteException {
        return publisherLogin.containsKey(username) && publisherLogin.get(username).equals(password);
    }

    @Override
    public void sendCryptoObject(CryptoObject cryptoObject) throws RemoteException {
        System.out.println("Received Crypto Object:");
        System.out.println(cryptoObject);
    }

    private void loadLoginData(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Split by the colon to get the role (Publisher/Subscriber) and credentials
                String[] roleAndCredentials = line.split(":", 2);
                if (roleAndCredentials.length == 2) {
                    String role = roleAndCredentials[0].trim();
                    String[] credentials = roleAndCredentials[1].split(",", 2);

                    if (credentials.length == 2) {
                        String username = credentials[0].trim();
                        String password = credentials[1].trim();

                        // Add to the respective HashMap based on role
                        if (role.equalsIgnoreCase("Publisher")) {
                            publisherLogin.put(username, password);
                        } else if (role.equalsIgnoreCase("Subscriber")) {
                            userLogin.put(username, password);
                        }
                    }
                }
            }
            System.out.println("Login data loaded successfully.");
        } catch (IOException e) {
            System.err.println("Error loading login data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            System.setProperty("java.rmi.server.hostname", "10.91.80.240");

            // Unique identifier for each instance
            String cryptoName = "BTC"; // This can be dynamically set
            String id = "001"; // Example ID
            String uniqueBindingName = String.format("rmi://10.91.80.240:1099/%s_%s", cryptoName, id);

            // Create and export the DealingRoomServer object
            DealingRoomServer server = new DealingRoomServer();

            // Load login data from file
            server.loadLoginData("./login.txt");

            // Create the RMI registry on port 1099
            java.rmi.registry.LocateRegistry.createRegistry(1099);

            // Bind the server object with the unique binding name
            Naming.rebind(uniqueBindingName, server);

            System.out.println("Server bound with name: " + uniqueBindingName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
