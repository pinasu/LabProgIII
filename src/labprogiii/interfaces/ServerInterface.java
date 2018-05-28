package labprogiii.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author pinasu
 */
public interface ServerInterface extends Remote {

    ArrayList<EMail> getMessagesIn(String account) throws RemoteException;

    ArrayList<EMail> getMessagesOut(String account) throws RemoteException;

    void notifyConnection(String account) throws RemoteException;
}
