package labprogiii.server;

import java.rmi.RemoteException;
import java.util.ArrayList;

import labprogiii.interfaces.EMail;

class ServerInbox {
    ArrayList<EMail> messagesIn;
    ArrayList<EMail> messagesOut;

    public ServerInbox(ArrayList<EMail> emailListIn, ArrayList<EMail> emailListOut) {
        this.messagesIn = emailListIn;
        this.messagesOut = emailListOut;
    }

    public ArrayList<EMail> getMessagesIn() {
        return this.messagesIn;
    }

    public ArrayList<EMail> getMessagesOut() {
        return this.messagesOut;
    }

}
