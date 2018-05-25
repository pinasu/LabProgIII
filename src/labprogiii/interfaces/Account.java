package labprogiii.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

/**
 *
 * @author pinasu
 */
public interface Account extends Remote {
    
    String getAccountName() throws RemoteException;

    Date getAccountdate() throws RemoteException;

    void setAccountName(String name) throws RemoteException;

    void setAccountDate(Date date) throws RemoteException;
    
}
