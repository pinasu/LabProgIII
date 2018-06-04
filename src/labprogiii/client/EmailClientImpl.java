package labprogiii.client;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import labprogiii.interfaces.EMail;

class EmailClientImpl implements EMail, Serializable {
        int ID;

        String sender;

        ArrayList<String> recipient;

        String argument;

        String text;

        int priority;

        String date;
        
        public EmailClientImpl(int ID, String sender, ArrayList<String> recipient, String argument, String text, int priority, String date){
            this.ID = ID;
            this.sender = sender;
            this.recipient = recipient;
            this.argument = argument;
            this.text = text;
            this.priority = priority;
            this.date = date;
        }

        public String getEmailSender(){
            return this.sender;
        }

        public ArrayList<String> getEmailRecipient(){
            return this.recipient;
        }

        public String getEmailArgument(){
            return this.argument;
        }

        public String getEmailText(){
            return this.text;
        }

        public String getEmailDate(){
            return this.date;
        }

        public int getEmailID(){
            return this.ID;
        }

        public int getEmailPriority(){
            return this.priority;
        }
}   