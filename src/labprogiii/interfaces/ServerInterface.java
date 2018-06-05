package labprogiii.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServerInterface extends Remote {

    ArrayList<EMail> getMessagesIn(String account) throws RemoteException;

    ArrayList<EMail> getMessagesOut(String account) throws RemoteException;

    void notifyConnection(String account) throws RemoteException;

    int sendMail(String account, EMail e) throws RemoteException;

    int deleteReceivedMail(String account, int index) throws RemoteException;

    int deleteSentMail(String account, int index) throws RemoteException;

}
