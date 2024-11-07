import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface Publisher extends Remote {
    // Method to send a CryptoObject to the server

    void receiveCryptoObject(CryptoObject cryptoObject) throws RemoteException;

}
