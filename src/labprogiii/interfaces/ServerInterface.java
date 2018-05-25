package labprogiii.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author pinasu
 */
public interface ServerInterface extends Remote {
    
    ArrayList<EMail> getMailList(Account a) throws RemoteException;

    ArrayList<EMail> getSentMail(Account a) throws RemoteException;
    
}
