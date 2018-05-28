package labprogiii.server;

import java.rmi.RemoteException;
import javax.naming.NamingException;

/**
 *
 * @author pinasu
 */

public class ServerMain {
    public static void main(String[] args) throws RemoteException, NamingException {
        Server server = new Server(new ServerView());
        //provaCommitPush
    }
}
