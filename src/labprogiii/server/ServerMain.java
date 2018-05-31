package labprogiii.server;

import java.rmi.RemoteException;
import javax.naming.NamingException;

public class ServerMain {
    public static void main(String[] args) throws RemoteException, NamingException {
        new Server();
    }
}
