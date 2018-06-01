package labprogiii.client;

import java.rmi.RemoteException;
import java.util.Date;
import javax.naming.NamingException;

/**
 *
 * @author pinasu
 */

public class ClientMain {
    public static void main(String[] args) throws NamingException, RemoteException {

        new Client(new Account("gianni", new Date()));

        /*
        client.addObserver(controller);
        */
    }
}