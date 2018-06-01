package labprogiii.server;

import java.rmi.RemoteException;
import java.util.ArrayList;

import labprogiii.interfaces.EMail;

/**
 *
 * @author pinasu
 */
class ServerInbox {
    ArrayList<EMail> messagesIn;
    ArrayList<EMail> messagesOut;

    public ServerInbox(ArrayList<EMail> emailListIn, ArrayList<EMail> emailListOut) throws RemoteException {
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
