package labprogiii.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import labprogiii.interfaces.EMail;

class EmailServerImpl extends UnicastRemoteObject implements EMail{
        String ID;
        String sender;
        ArrayList<String> recipient;
        String argument;
        String text;
        int priority;
        Date date;
        
        public EmailServerImpl(String ID, String sender, ArrayList<String> recipient, String argument, String text) throws RemoteException {
            this.ID = ID;
            this.sender = sender;
            this.recipient = recipient;
            this.argument = argument;
            this.text = text;
            this.priority = 1;
            this.date = new Date();
        }

        @Override
        public String getEmailSender() {
            return this.sender;
        }

        @Override
        public ArrayList<String> getEmailRecipient() {
            return this.recipient;
        }

        @Override
        public String getEmailArgument() {
            return this.argument;
        }

        @Override
        public String getEmailText() {
            return this.text;
        }

        @Override
        public Date getEmailDate() {
            return this.date;
        }
 
}