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

        Client client = new Client(new Account("gianni", new Date()));

        ClientController controller =  new ClientController(client);

        controller.view = new ClientView(controller);

        client.addObserver(controller);
    }
}