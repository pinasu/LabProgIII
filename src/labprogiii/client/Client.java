package labprogiii.client;

import labprogiii.interfaces.ServerInterface;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.*;

import labprogiii.interfaces.EMail;

class Client extends Observable {
    Account account;
    ClientView view;
    ServerInterface server;
    ArrayList<EMail> emailListIn, emailListOut;
    EMail newMail;

    private class NewMail implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    System.out.println("["+account.getAccountName()+"]"+"Checking for new emails...");
                    if (server.getMapValue(account.getAccountName()) == true) {

                        server.setMapValue(account.getAccountName(), false);
                        setChanged();
                        notifyObservers();
                    }

                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Client(Account account) {
        try{
            try {
                this.account = account;

                server = (ServerInterface)(new InitialContext().lookup("rmi:server"));
                server.notifyConnection(0, account.getAccountName());

                getEmailList();

                this.view = new ClientView(this);

                new Thread(new NewMail()).start();

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

    public void getEmailList() {
        try {
            this.emailListIn = this.server.getMessagesIn(this.account.getAccountName());
            this.emailListOut = this.server.getMessagesOut(this.account.getAccountName());

        } catch (RemoteException e) {
            view.showPopUp("Error while retrieving emails. Try again later.");
        }
    }

    public Vector<Vector> populateData(int type){
        ArrayList<EMail> emailList = null;

        //Updates mail list for client
        getEmailList();

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
                    row.add(e.getEmailRecipient().toString().replace("[", "").replace("]", ""));

                row.add(e.getEmailArgument());
                row.add(e.getEmailDate());

            } catch (RemoteException ex) {
                System.out.println(ex.getCause());
            }
            tmp.add(row);
        }
        return tmp;
    }

    public int sendMail(EMail e) throws RemoteException {
        emailListOut.add(0, e);

        this.newMail = e;
        return this.server.sendMail(this.account.getAccountName(), e);
    }

    public int deleteReceivedMail(int index) throws RemoteException{
        emailListIn.remove(index);
        return this.server.deleteReceivedMail(this.account.getAccountName(), index);
    }

    public int deleteSentMail(int index) throws RemoteException{
        emailListOut.remove(index);
        return this.server.deleteSentMail(this.account.getAccountName(), index);
    }

    public void notifyServer() throws RemoteException {
        server.notifyConnection(1, account.getAccountName());
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

    public int getIDCount(){
        try {
            return this.server.getIDCount();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
