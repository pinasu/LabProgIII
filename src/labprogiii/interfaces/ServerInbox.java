package labprogiii.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author pinasu
 */
public interface ServerInbox extends Remote {
    
    Account getAccount() throws RemoteException;
    
    ArrayList getMessages() throws RemoteException;
    
    void setAccount(Account account) throws RemoteException;
    
    void setMessages(ArrayList<EMail> messages) throws RemoteException;

}
