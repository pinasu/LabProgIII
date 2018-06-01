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

import labprogiii.interfaces.EMail;


/**
 *
 * @author pinasu
 */
class Server extends UnicastRemoteObject implements ServerInterface {
    ServerController controller;
    Context naming;
    HashMap<String, ServerInbox> inboxMap;

    public Server() throws RemoteException, NamingException {
        this.controller = new ServerController(this);

        LocateRegistry.createRegistry(1099);

        this.inboxMap = new HashMap();

        this.naming = new InitialContext();
        this.naming.bind("rmi:server", this);

        ArrayList<Account> accountList = new ArrayList<>();
        accountList.add(new Account("gianni", new Date()));
        accountList.add(new Account("pino", new Date()));
        accountList.add(new Account("mila", new Date()));

        setUpInbox(accountList);

        controller.printLog("Waiting for clients...");
    }

    public void notifyConnection(String account){
        this.controller.printLog("User "+account+" has connected.");
    }

    public ArrayList<EMail> getMessagesIn(String account) throws RemoteException{

        this.controller.printLog("User "+account+" retrieved his messages.");

        return(this.inboxMap.get(account).getMessagesIn());

    }

    public ArrayList<EMail> getMessagesOut(String account) throws RemoteException{

        this.controller.printLog("User "+account+" retrieved his sent messages.");

        return(this.inboxMap.get(account).getMessagesOut());

    }

    private void setUpInbox(ArrayList<Account> accountList) throws RemoteException {
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
                                        emailListIn.add(new EmailServerImpl(ID[0], sender, new ArrayList<>(set), argument, text));

                                    in.close();

                                } catch (FileNotFoundException e) {
                                    this.controller.printLog(e.getMessage());
                                }
                            }
                        }
                    }
                }
            }
        }
        for(Account a : accountList){
            ServerInbox inbox = new ServerInbox(emailListIn, emailListOut);

            inboxMap.put(a.getAccountName(), inbox);

            this.controller.printLog("Inbox for account "+a.getAccountName()+" created.");
        }
    }

}
