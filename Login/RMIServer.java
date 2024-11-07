
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class RMIServer {
    public static void main(String[] args) {
        try {
            // Create and export the remote object
            LoginServiceImpl loginService = new LoginServiceImpl();

            // Start the RMI registry (default port 1099)
            LocateRegistry.createRegistry(1099);

            // Bind the remote object to the RMI registry
            Naming.rebind("rmi://localhost/LoginService", loginService);

            System.out.println("Login Service is running and waiting for clients...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
