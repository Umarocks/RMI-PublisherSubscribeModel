import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Publisher extends Remote {
    // Method to send a CryptoObject to the server
    void receiveCryptoObject(CryptoObject cryptoObject) throws RemoteException;
}
