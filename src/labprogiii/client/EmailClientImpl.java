package labprogiii.client;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import labprogiii.interfaces.EMail;

/**
 *
 * @author pinasu
 */
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
        public String getEmailID() throws RemoteException{
            return this.ID;
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
        public int getEmailPriority() throws RemoteException{
            return this.priority;
        }

        @Override
        public Date getEmailDate() throws RemoteException{
            return this.date;
        }

        @Override
        public void setEmailID(String ID) throws RemoteException{
            this.ID = ID;
        }

        @Override
        public void setEmailSender(String sender) throws RemoteException{
            this.sender = sender;
        }

        @Override
        public void setEmailRecipient(ArrayList<String> recipient) throws RemoteException{
            this.recipient = recipient;
        }

        @Override
        public void setEmailArgument(String argument) throws RemoteException{
            this.argument = argument;
        }

        @Override
        public void setEmailText(String text) throws RemoteException{
            this.text = text;
        }

        @Override
        public void setEmailPriority(int priority) throws RemoteException{
            if(priority <= 2 || priority >= 0)
                this.priority = priority;
        }

        @Override
        public void setEmailDate(Date date) throws RemoteException{
            this.date = date;
        }

        @Override
        public void printMail() throws RemoteException {
            System.out.println("ID: "+this.ID);
        }
        
}   