package labprogiii.server;

import labprogiii.interfaces.ServerInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import labprogiii.interfaces.Account;
import labprogiii.interfaces.EMail;
import labprogiii.interfaces.ServerInbox;


/**
 *
 * @author pinasu
 */
class Server extends UnicastRemoteObject implements ServerInterface {
    Context naming;
    Registry reg;
    ServerView view;
    HashMap<String, List<ServerInbox>> inboxMap;

    public Server(ServerView view) throws RemoteException, NamingException{
        this.inboxMap = new HashMap();

        this.view = view;
        this.reg = LocateRegistry.createRegistry(1099);
        this.naming = new InitialContext();
        this.naming.rebind("rmi:server", this);

        ArrayList<Account> accountList = new ArrayList<>();

        accountList.add(new AccountImpl("gianni", new Date()));
        accountList.add(new AccountImpl("pino", new Date()));
        accountList.add(new AccountImpl("mila", new Date()));
        initiateInbox(accountList);

        this.view.printLog("Waiting for clients...");
    }
    
    @Override
    public ArrayList<EMail> getMailList(Account a) throws RemoteException{

        view.printLog("User "+a.getAccountName()+" retrieved his messages.");

        return(this.inboxMap.get(a.getAccountName()).get(0).getMessages());

    }

    @Override
    public ArrayList<EMail> getSentMail(Account a) throws RemoteException{

        view.printLog("User "+a.getAccountName()+" retrieved his sent messages.");

        return(this.inboxMap.get(a.getAccountName()).get(1).getMessages());

    }

    private void initiateInbox(ArrayList<Account> accountList) throws RemoteException {
        ArrayList<EMail> emailListIn = new ArrayList<>();
        ArrayList<EMail> emailListOut = new ArrayList<>();


        String PATH = System.getProperty("user.dir")+"/src/labprogiii/server/";
        System.out.println(PATH);

        //Get all mails
        File dir = new File(PATH);
        if(dir.isDirectory()) {
            //Main directory
            File[] userDir = dir.listFiles();

            //Iterate thought main directory files
            for(File child : userDir){

                if(child.isDirectory()){
                    //Single users directories
                    File[] dirMail = child.listFiles();

                    for(File f : dirMail){
                        //Received or sent
                        File[] dirSR = f.listFiles();

                        for(File m : dirSR) {
                            if (m.isFile()) {
                                //Mail ID
                                String[] ID = m.getName().split(".csv");

                                try {
                                    Scanner in = new Scanner(new File(PATH + child.getName()+"/"+f.getName()+"/"+m.getName())).useDelimiter(";\n");
                                    String sender = in.next();

                                    //Mail recipients
                                    String rc = in.next();
                                    Scanner sc = new Scanner(rc).useDelimiter(",");

                                    HashSet<String> set = new HashSet<>();
                                    while (sc.hasNext())
                                        set.add(sc.next());

                                    //Mail argument
                                    String argument = in.next();

                                    //Mail text
                                    String text = in.next();

                                    if(f.getName().equals("sent"))
                                        emailListOut.add(new EmailServerImpl(ID[0], sender, new ArrayList<>(set), argument, text));
                                    else if(f.getName().equals("received"))
                                        emailListIn.add(new EmailServerImpl(    ID[0], sender, new ArrayList<>(set), argument, text));

                                    in.close();

                                } catch (FileNotFoundException e) {
                                    this.view.printLog(e.getMessage());
                                }
                            }
                        }
                    }
                }
            }
        }
        for(Account a : accountList){
            ServerInbox inboxIn = new ServerInboxImpl(a, emailListIn);
            ServerInbox inboxOut = new ServerInboxImpl(a, emailListOut);

            ArrayList inboxList = new ArrayList<ServerInbox>();
            inboxList.add(inboxIn);
            inboxList.add(inboxOut);

            inboxMap.put(a.getAccountName(), inboxList);
            view.printLog("Inbox for account "+a.getAccountName()+" created.");
        }
    }
   
}
