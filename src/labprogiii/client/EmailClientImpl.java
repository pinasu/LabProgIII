package labprogiii.client;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import labprogiii.interfaces.EMail;

class EmailClientImpl implements EMail{
        String ID;

        String sender;

        ArrayList<String> recipient;

        String argument;

        String text;

        int priority;

        Date date;
        
        public EmailClientImpl(String ID, String sender, ArrayList<String> recipient, String argument, String text) throws RemoteException{
            this.ID = ID;
            this.sender = sender;
            this.recipient = recipient;
            this.argument = argument;
            this.text = text;
            this.priority = 1;
            this.date = new Date();
        }

        @Override
        public String getEmailSender() throws RemoteException{
            return this.sender;
        }

        @Override
        public ArrayList<String> getEmailRecipient() throws RemoteException{
            return this.recipient;
        }

        @Override
        public String getEmailArgument() throws RemoteException{
            return this.argument;
        }

        @Override
        public String getEmailText() throws RemoteException{
            return this.text;
        }

        @Override
        public Date getEmailDate() throws RemoteException{
            return this.date;
        }

}   