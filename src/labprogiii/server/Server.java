package labprogiii.server;

import labprogiii.interfaces.ServerInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import labprogiii.interfaces.EMail;

class Server extends UnicastRemoteObject implements ServerInterface {
    ServerView view;
    ServerController controller;
    Context naming;
    HashMap<String, ServerInbox> inboxMap;

    public Server() throws RemoteException, NamingException {
        this.view = new ServerView(this);
        this.controller = this.view.getController();
        try {
            LocateRegistry.createRegistry(1099);
        }catch(Exception e){
            controller.printLog("Error."+e.getCause());
        }
        this.inboxMap = new HashMap();

        this.naming = new InitialContext();
        this.naming.bind("rmi:server", this);

        ArrayList<Account> accountList = new ArrayList<>();
        accountList.add(new Account("mino", new SimpleDateFormat("dd/MM/yyyy").format(new Date())));
        accountList.add(new Account("pino", new SimpleDateFormat("dd/MM/yyyy").format(new Date())));
        accountList.add(new Account("lino", new SimpleDateFormat("dd/MM/yyyy").format(new Date())));

        setUpInbox(accountList);

        controller.printLog("Waiting for clients...");
    }

    public void notifyConnection(String account){
        this.controller.printLog("User "+account+" has connected.");
    }

    public ArrayList<EMail> getMessagesIn(String account)   {

        this.controller.printLog("User "+account+" retrieved his messages.");

        return(this.inboxMap.get(account).getMessagesIn());

    }

    public ArrayList<EMail> getMessagesOut(String account) {

        this.controller.printLog("User "+account+" retrieved his sent messages.");

        return(this.inboxMap.get(account).getMessagesOut());

    }

    public int sendMail(String account, EMail e){
        this.inboxMap.get(account).getMessagesOut().add(e);
        try {
            for(String rec : e.getEmailRecipient())
                this.inboxMap.get(rec).getMessagesIn().add(e);

            writeMail(e);
            view.printLog(e.getEmailSender()+" sent an email to "+e.getEmailRecipient()+".");

        } catch (RemoteException e1) {
            view.printLog("Errore.");
            return -1;
        }
        return 0;
    }

    private void writeMail(EMail e) throws RemoteException {
        String PATH = System.getProperty("user.dir") + "/src/labprogiii/server/";

        File dir = new File(PATH);

        if (dir.isDirectory()) {
            File[] userDir = dir.listFiles();

            for (String s : e.getEmailRecipient()) {

                for (File f : userDir) {
                    if (f.isDirectory()) {
                        if (f.getName().equals(s)) {

                            File[] rcv = f.listFiles();
                            for(File r : rcv) {

                                if (r.getName().equals("received")) {
                                    createFile(e, PATH + f.getName() + "/" + r.getName() + "/");
                                }
                            }
                        }
                    }
                }
            }

            for(File f : userDir){
                if(f.isDirectory()) {
                    if(f.getName().equals(e.getEmailSender())) {

                        File[] snt = f.listFiles();
                        for(File s : snt) {

                            if (s.getName().equals("sent")) {
                                createFile(e, PATH +f.getName()+"/"+s.getName() + "/");
                            }
                        }
                    }
                }
            }

        }
    }

    private void createFile(EMail e, String path) {
        try {
            FileWriter mail = new FileWriter(path+e.getEmailID()+".csv");

            mail.append(e.getEmailSender()+";\n");
            mail.append(e.getEmailRecipient().toString().replace("[", "").replace("]", "")+";\n");
            mail.append(e.getEmailArgument()+";\n");
            mail.append(e.getEmailText()+";\n");
            mail.append(e.getEmailDate()+";\n");
            mail.append(String.valueOf(e.getEmailPriority())+";");

            mail.close();

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


    private void setUpInbox(ArrayList<Account> accountList) throws RemoteException {
        ArrayList<EMail> emailListIn = new ArrayList<>();
        ArrayList<EMail> emailListOut = new ArrayList<>();

        String PATH = System.getProperty("user.dir")+"/src/labprogiii/server/";

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

                                    int priority = Integer.parseInt(in.next());

                                    String date = in.next();

                                    if(f.getName().equals("sent"))
                                        emailListOut.add(new EmailServerImpl(Integer.parseInt(ID[0]), sender, new ArrayList<>(set), argument, text, priority, date));

                                    else if(f.getName().equals("received"))
                                        emailListIn.add(new EmailServerImpl(Integer.parseInt(ID[0]), sender, new ArrayList<>(set), argument, text, priority, date));

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
