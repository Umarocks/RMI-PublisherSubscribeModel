
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LoginService extends Remote {
    boolean authenticatePublisher(String username, String password) throws RemoteException;

    boolean authenticateSubscriber(String username, String password) throws RemoteException;
}
