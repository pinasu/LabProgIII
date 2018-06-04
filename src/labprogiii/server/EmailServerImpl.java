package labprogiii.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import labprogiii.interfaces.EMail;

class EmailServerImpl extends UnicastRemoteObject implements EMail{
    int ID;
    String sender;
    ArrayList<String> recipient;
    String argument;
    String text;
    int priority;
    String date;

    public EmailServerImpl(int ID, String sender, ArrayList<String> recipient, String argument, String text, int priority, String date) throws RemoteException {
        this.ID = ID;
        this.sender = sender;
        this.recipient = recipient;
        this.argument = argument;
        this.text = text;
        this.priority = priority;
        this.date = date;
    }

    public String getEmailSender() {
        return this.sender;
    }

    public ArrayList<String> getEmailRecipient() {
        return this.recipient;
    }

    public String getEmailArgument() {
        return this.argument;
    }

    public String getEmailText() {
        return this.text;
    }

    public String getEmailDate() {
        return this.date;
    }

    public int getEmailID(){
        return this.ID;
    }

    public int getEmailPriority(){ return this.priority; }


}