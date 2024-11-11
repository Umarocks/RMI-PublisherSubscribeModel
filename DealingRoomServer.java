import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DealingRoomServer extends UnicastRemoteObject implements Publisher, Subscriber {
    private HashMap<String, ArrayList<CryptoObject>> cryptoMap = new HashMap<>();
    private static HashMap<String, Set<String>> SubscriptionList = new HashMap<>();
    public static HashMap<PrintWriter, String> clientOutputStreams = new HashMap<>();

    public static Map<String, Integer> ipMap = new ConcurrentHashMap<>();

    protected DealingRoomServer() throws RemoteException {
        super();
        loadCryptoMapFromFile();
    }

    @Override
    public void IpRegister(String username, String ip) throws RemoteException {
        ipMap.put(ip, 0);
        System.out.println("IP Registered: " + ipMap);
    }

    @Override
    public List<CryptoObject> getArticleList(String username) throws RemoteException {
        // Initialize an ArrayList to hold CryptoObject instances
        List<CryptoObject> articleList = new ArrayList<>();

        // Check if the user is subscribed to any topics
        if (SubscriptionList.containsKey(username)) {
            // Iterate over each topic the user is subscribed to
            for (String topic : SubscriptionList.get(username)) {
                // Check if the topic exists in the cryptoMap
                if (cryptoMap.containsKey(topic)) {
                    // Add each CryptoObject for the given topic to the article list
                    for (CryptoObject cryptoObject : cryptoMap.get(topic)) {
                        articleList.add(cryptoObject); // Add the entire CryptoObject
                    }
                }
            }
        }
        return articleList;
    }

    @Override
    public void unsubscribe(String topicNameToUnsubscribe, String username) throws RemoteException {

        // Check if the user exists and if they are subscribed to the topic
        if (SubscriptionList.containsKey(username) && SubscriptionList.get(username).contains(topicNameToUnsubscribe)) {

            // Remove the topic from the user's subscription list
            SubscriptionList.get(username).remove(topicNameToUnsubscribe);
            System.out.println("Subscriber removed from topic: " + topicNameToUnsubscribe);

            // If the user has no more topics, remove the user from the SubscriptionList
            if (SubscriptionList.get(username).isEmpty()) {
                SubscriptionList.remove(username);
                System.out.println("User " + username + " has no more subscriptions and was removed.");
            }

            // Save the updated SubscriptionList to file
            saveSubscriptionListToFile();
        } else {
            JOptionPane.showMessageDialog(null, "User is not subscribed to the topic.");
        }

        System.out.println(SubscriptionList);
    }

    @Override
    public void subscribe(String topicNameToSubscribe, String username) throws RemoteException {

        if (cryptoMap.containsKey(topicNameToSubscribe)) {

            SubscriptionList.computeIfAbsent(username, key -> new HashSet<>()).add(topicNameToSubscribe);
            System.out.println("Subscriber added to topic: " + topicNameToSubscribe);
            saveSubscriptionListToFile();
        } else {
            System.out.println("CALLING HELLO FUNCTION");
            sendHelloToAllClients("m");

            JOptionPane.showMessageDialog(null, "Topic does not exist.");
        }
        System.out.println(SubscriptionList);

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
    private void saveSubscriptionListToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("SubscriptionRecord.txt"))) {
            out.writeObject(SubscriptionList);
            System.out.println("Subscription List saved to file.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving Subscription List to file.");
        }
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
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("SubscriptionRecord.txt"))) {
            Object obj = in.readObject();
            SubscriptionList = (HashMap<String, Set<String>>) obj;
            System.out.println("Subscription List loaded from file.");
            System.out.println("Loaded Subscription List: " + SubscriptionList);
        } catch (FileNotFoundException e) {
            System.out.println("File not found, initializing empty SubscriptionList.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error loading Subscription List from file.");
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
        sendHelloToAllClients(cryptoObject.getCryptoName());
    }

    public static void sendHelloToAllClients(String CryptoName) {
        // for (PrintWriter out : clientOutputStreams) {
        // // String[] clientInfo = clientKey.split(":");
        // // String clientIP = clientInfo[0];
        // // int clientPort = Integer.parseInt(clientInfo[1]);

        // // System.out.println(socket.getInetAddress());
        // out.println("HELLO");
        // System.out.println("Sent 'HELLO' to " + out);

        // }
        System.out.println(clientOutputStreams);
        // private HashMap<String, Set<String>> SubscriptionList = new HashMap<>();
        System.out.println(SubscriptionList);
        for (Map.Entry<PrintWriter, String> entry : clientOutputStreams.entrySet()) {
            String clientValue = entry.getValue(); // This is the "IP:port" string
            PrintWriter out = entry.getKey(); // The PrintWriter for this client
            // System.out.println();
            System.out.println(clientValue + " /" + out + " /" + "RETRIEVED FROM MAP BEFORE HELLO");
            if (SubscriptionList.containsKey(clientValue)) {
                if (SubscriptionList.get(clientValue).contains(CryptoName)) {
                    out.println("HELLO");
                    System.out.println("Sent 'HELLO' to " + clientValue + " with PrintWriter: " + out);
                }
            }

            // Send the "HELLO" message
            // out.println("HELLO");

            // Print to console the client information and confirmation of message sent
            System.out.println("Sent 'HELLO' to " + clientValue + " with PrintWriter: " + out);
        }

    }

    private static class ClientHandler extends Thread {
        private final Socket clientSocket;
        protected String username = "";

        public ClientHandler(Socket socket, String username) {
            this.username = username;
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                // Read the message from the client (optional)
                // clientOutputStreams.put(out, this.username);
                for (int i = 0; i < 2; i++) {

                    System.out.println("Waiting for username...");
                    String message = in.readLine();
                    System.out.println("Message: " + message);
                    String[] parts = message.split(":");
                    System.out.println(parts.length);
                    for (String part : parts) {
                        System.out.println(part);
                    }
                    clientOutputStreams.put(out, message);
                    System.out.println("Received from client: " + message);
                }
                while (true) {
                    // System.out.println("Received from client: " + message);
                }
            } catch (IOException e) {
                System.err.println("Error handling client communication: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Error closing client socket: " + e.getMessage());
                }
            }
        }
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
            try (ServerSocket serverSocket = new ServerSocket(10655)) {
                System.out.println("Server is listening on port 10655...");

                // Continuously accept client connections
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    InetAddress clientAddress = clientSocket.getInetAddress();
                    int clientPort = clientSocket.getPort();
                    String clientKey = clientAddress.getHostAddress() + ":" + clientPort;

                    // Add client IP and port to the map
                    ipMap.put(clientKey, clientPort);
                    System.out.println("Client connected: " + clientKey);
                    String username = "";
                    // Start a new thread to handle the client
                    new ClientHandler(clientSocket, username).start();
                }
            } catch (IOException e) {
                System.err.println("Server exception: " + e.getMessage());
            }
            System.out.println("Server ready to receive CryptoObject...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
