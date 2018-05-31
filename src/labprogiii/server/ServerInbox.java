package labprogiii.server;

import java.util.ArrayList;

import labprogiii.interfaces.EMail;

class ServerInbox {
    ArrayList<EMail> messagesIn;
    ArrayList<EMail> messagesOut;

    ServerInbox(ArrayList<EMail> emailListIn, ArrayList<EMail> emailListOut) {
        this.messagesIn = emailListIn;
        this.messagesOut = emailListOut;
    }

    ArrayList<EMail> getMessagesIn() {
        return this.messagesIn;
    }

    ArrayList<EMail> getMessagesOut() {
        return this.messagesOut;
    }

}
