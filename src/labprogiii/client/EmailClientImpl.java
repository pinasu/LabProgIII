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