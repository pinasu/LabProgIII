package labprogiii.client;

import static java.lang.System.exit;
import labprogiii.interfaces.ServerInterface;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Observable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import labprogiii.interfaces.Account;
import labprogiii.interfaces.EMail;

/**
 *
 * @author pinasu
 */

final class Client extends Observable {
    Account account;
    Context namingContext;
    ServerInterface server;
    ArrayList<EMail> emailList, emailSent;
    
    public Client(Account account) throws NamingException, RemoteException{
        this.account = account;
        this.namingContext = new InitialContext();
        try{
            server = (ServerInterface)namingContext.lookup("rmi:server");
            retrieveMessages();
        }catch(NamingException e){
            System.out.println(e.getCause());
            exit(1);
        }
    }

    public ArrayList<EMail> getEmailList() {
        return this.emailList;
    }

    public ArrayList<EMail> getEmailListOut() throws RemoteException {
        retrieveSentMessages();
        return this.emailSent;
    }

    public Account getAccountClient() {
        return this.account;
    }

    public void setAccountClient(Account account) {
        this.account = account;
    }

    public void retrieveMessages() throws RemoteException {
        this.emailList = server.getMailList(this.account);
        setChanged();
        notifyObservers(emailList);
    }

    public void retrieveSentMessages() throws RemoteException{
        this.emailSent = this.server.getSentMail(this.account);
        setChanged();
        notifyObservers(emailSent);
    }
}
