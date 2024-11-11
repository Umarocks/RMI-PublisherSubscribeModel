
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;
import java.util.List;

public interface Subscriber extends Remote {
    // Method to send a CryptoObject to the server
    Set<String> getCryptoKeys() throws RemoteException;

    void subscribe(String topicNameToSubscribe, String username) throws RemoteException;

    void unsubscribe(String topicNameToUnsubscribe, String username) throws RemoteException;

    void IpRegister(String username, String ip) throws RemoteException;

    // void sendTopicList(TopicList topicList) throws RemoteException;
    List<CryptoObject> getArticleList(String username) throws RemoteException;
}
