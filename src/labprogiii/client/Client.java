package labprogiii.client;

import static java.lang.System.exit;
import labprogiii.interfaces.ServerInterface;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Observable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import labprogiii.interfaces.EMail;

/**
 *
 * @author pinasu
 */

final class Client extends Observable {
    Account account;
    ClientController controller;
    Context namingContext;
    ServerInterface server;
    ArrayList<EMail> emailList, emailSent;
    
    public Client(Account account) throws NamingException, RemoteException{
        this.account = account;


        this.namingContext = new InitialContext();
        try{
            server = (ServerInterface)namingContext.lookup("rmi:server");
            server.notifyConnection(account.getAccountName());

            retrieveMessages();
            retrieveSentMessages();

        }catch(NamingException e){
            System.out.println(e.getCause());
            exit(1);
        }

        this.controller = new ClientController(this);
    }

    public void setController(ClientController controller){
        this.controller = controller;
    }

    public ArrayList<EMail> getEmailList() {
        return this.emailList;
    }

    public ArrayList<EMail> getEmailListOut() throws RemoteException {
        return this.emailSent;
    }

    public void retrieveMessages() throws RemoteException {
        this.emailList = server.getMessagesIn(this.account.getAccountName());

        setChanged();
        notifyObservers(emailList);
    }

    public void retrieveSentMessages() throws RemoteException{
        this.emailSent = this.server.getMessagesOut(this.account.getAccountName());

        setChanged();
        notifyObservers(emailSent);
    }

}