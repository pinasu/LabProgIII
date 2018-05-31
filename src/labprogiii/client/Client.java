package labprogiii.client;

import static java.lang.System.exit;
import labprogiii.interfaces.ServerInterface;

import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Vector;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.*;

import labprogiii.interfaces.EMail;

class Client extends Observable {
    Account account;
    ClientView view;
    ServerInterface server;
    ArrayList<EMail> emailListIn, emailListOut;
    
    public Client(Account account) {
        this.account = account;

        try{
            try {
                server = (ServerInterface)(new InitialContext().lookup("rmi:server"));
                server.notifyConnection(account.getAccountName());

                retrieveMessagesIn();
                retrieveMessagesOut();

                this.view = new ClientView(this);

            } catch (NamingException e) {
                launchError(String.valueOf(e.getCause()));
            }

        } catch(RemoteException e){
            launchError(String.valueOf(e.getCause()));
        }

    }

    public Account getAccount(){
        return this.account;
    }

    public ArrayList<EMail> getEmailListIn() {
        return this.emailListIn;
    }

    public ArrayList<EMail> getEmailListOut() {
        return this.emailListOut;
    }

    public void retrieveMessagesIn() throws RemoteException {
        this.emailListIn = this.server.getMessagesIn(this.account.getAccountName());

        setChanged();
        notifyObservers(emailListIn);
    }

    public void retrieveMessagesOut() throws RemoteException{
        this.emailListOut = this.server.getMessagesOut(this.account.getAccountName());

        setChanged();
        notifyObservers(emailListOut);
    }

    public Vector<Vector> populateData(int type){
        ArrayList<EMail> emailList = null;

        if (type == 0)
            emailList = this.emailListIn;
        else if(type == 1)
            emailList = this.emailListOut;

        Vector<Vector> tmp = new Vector<>();

        for (EMail e : emailList) {
            Vector<String> row = new Vector<>();

            try {
                if(type == 0)
                    row.add(e.getEmailSender());
                else if (type == 1)
                    row.add(e.getEmailRecipient().toString());

                row.add(e.getEmailArgument());
                row.add(e.getEmailDate().toString());
            } catch (RemoteException ex) {
                System.out.println(ex.getCause());
            }
            tmp.add(row);
        }
        return tmp;
    }

    private void launchError(String s){
        JFrame frame = new JFrame("Errore");
        frame.setLayout(new BorderLayout());

        JTextArea area = new JTextArea(s);
        area.setEditable(false);

        frame.add(area);
        frame.setDefaultCloseOperation(3);
        frame.setSize(500, 200);
        frame.setVisible(true);
    }
}