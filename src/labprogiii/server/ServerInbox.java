package labprogiii.server;

import java.util.ArrayList;

/**
 *
 * @author pinasu
 */
class ServerInbox {
    AccountServerImpl account;
    ArrayList<EmailServerImpl> messages;

    public ServerInbox(AccountServerImpl account, ArrayList<EmailServerImpl> messages){
        this.account = account;
        this.messages = messages;
    }
    
    public AccountServerImpl getAccount(){
        return this.account;
    }
    
    public ArrayList getMessages(){
        return this.messages;
    }
    
    public void setAccount(AccountServerImpl account){
        this.account = account;
    }
    
    public void setMessages(ArrayList messages){    
        this.messages = (ArrayList<EmailServerImpl>)messages;
    }

}
