package labprogiii.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

public interface EMail extends Remote{

    String getEmailSender() throws RemoteException;
    
    ArrayList<String> getEmailRecipient() throws RemoteException;
    
    String getEmailArgument() throws RemoteException;
    
    String getEmailText() throws RemoteException;

    int getEmailID() throws RemoteException;

    String getEmailDate() throws RemoteException;

    int getEmailPriority() throws RemoteException;

}

