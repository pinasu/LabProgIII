package labprogiii.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author pinasu
 */
public interface EMail extends Remote{
    
    String getEmailID() throws RemoteException;
    
    String getEmailSender() throws RemoteException;
    
    ArrayList<String> getEmailRecipient() throws RemoteException;
    
    String getEmailArgument() throws RemoteException;
    
    String getEmailText() throws RemoteException;
    
    int getEmailPriority() throws RemoteException;
    
    Date getEmailDate() throws RemoteException;
    
    void setEmailID(String ID) throws RemoteException;
    
    void setEmailSender(String sender) throws RemoteException;
    
    void setEmailRecipient(ArrayList<String> recipient) throws RemoteException;
    
    void setEmailArgument(String argument) throws RemoteException;
    
    void setEmailText(String text) throws RemoteException;
    
    void setEmailPriority(int priority) throws RemoteException;
    
    void setEmailDate(Date date) throws RemoteException;
    
    void printMail() throws RemoteException;
}

