import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Publisher extends Remote {
    // Method to send a CryptoObject to the server
    void sendCryptoObject(CryptoObject cryptoObject) throws RemoteException;
}
