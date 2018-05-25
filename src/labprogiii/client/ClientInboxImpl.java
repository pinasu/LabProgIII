package labprogiii.client;

import java.rmi.RemoteException;
import java.util.ArrayList;
import labprogiii.interfaces.Account;
import labprogiii.interfaces.EMail;
import labprogiii.interfaces.ServerInbox;

/**
 *
 * @author pinasu
 */
class ClientInboxImpl implements ServerInbox {
    Account account;
    ArrayList<EMail> messages;

    public ClientInboxImpl(Account account, ArrayList<EMail> messages) throws RemoteException{
        this.account = account;
        this.messages = messages;
    }
    
    @Override
    public Account getAccount() throws RemoteException{
        return this.account;
    }
    
    @Override
    public ArrayList<EMail> getMessages() throws RemoteException{
        return this.messages;
    }
    @Override
    public void setAccount(Account account) throws RemoteException{
        this.account = account;
    }
    
    @Override
    public void setMessages(ArrayList<EMail> messages) throws RemoteException{    
        this.messages = messages;
    }

}
