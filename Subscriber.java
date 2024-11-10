import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface Subscriber extends Remote {
    // Method to send a CryptoObject to the server
    Set<String> getCryptoKeys() throws RemoteException;

    void subscribe(String topicNameToSubscribe, String username) throws RemoteException;

    public void unsubscribe(String topicNameToUnsubscribe, String username) throws RemoteException;
    // void sendTopicList(TopicList topicList) throws RemoteException;

}
