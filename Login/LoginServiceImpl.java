
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class LoginServiceImpl extends UnicastRemoteObject implements LoginService {
    private HashMap<String, String> publisherDatabase;
    private HashMap<String, String> subscriberDatabase;

    protected LoginServiceImpl() throws RemoteException {
        super();
        publisherDatabase = new HashMap<>();
        subscriberDatabase = new HashMap<>();
        loadUserData();
    }

    private void loadUserData() {
        loadFromFile("Publisher.txt", publisherDatabase);
        loadFromFile("subscriber.txt", subscriberDatabase);
    }

    private void loadFromFile(String filename, HashMap<String, String> database) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    database.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading data from " + filename + ": " + e.getMessage());
        }
    }

    public boolean authenticatePublisher(String username, String password) throws RemoteException {
        return publisherDatabase.containsKey(username) && publisherDatabase.get(username).equals(password);
    }

    public boolean authenticateSubscriber(String username, String password) throws RemoteException {
        return subscriberDatabase.containsKey(username) && subscriberDatabase.get(username).equals(password);
    }
}
