package labprogiii.server;

import labprogiii.interfaces.ServerInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
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
    HashMap<String, Boolean> newMailMap;

    int IDCount = 0;

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

        setUpInbox();

        controller.printLog("Waiting for clients...");
    }

    public synchronized int getIDCount(){
        return this.IDCount;
    }

    public synchronized void setIDCount(){
        this.IDCount++;
    }

    public boolean getMapValue(String account){
        return this.newMailMap.get(account);
    }

    public void setMapValue(String account, boolean value){
        this.newMailMap.replace(account, value);
    }

    public void notifyConnection(int type, String account){
        if(type == 0)
            this.controller.printLog("User "+account+" has connected.");
        else if(type == 1)
            this.controller.printLog("User "+account+" has disconnected.");
    }

    public ArrayList<EMail> getMessagesIn(String account)   {

        this.controller.printLog("User "+account+" retrieved his messages.");

        return(this.inboxMap.get(account).getMessagesIn());

    }

    public ArrayList<EMail> getMessagesOut(String account) {

        this.controller.printLog("User "+account+" retrieved his sent messages.");

        return(this.inboxMap.get(account).getMessagesOut());

    }

    public int deleteReceivedMail(String account, int index){
        int id = -1;
        try {
            id = this.inboxMap.get(account).getMessagesIn().get(index).getEmailID();
            view.printLog(account+" deleted Email with ID "+id+" (received).");

            removeRcvMailFile(account, id);

            this.inboxMap.get(account).getMessagesIn().remove(index);
        } catch (RemoteException e) {
            view.printLog("Error: cannot delete email with ID "+id+".");
        }
        return 0;
    }

    public int deleteSentMail(String account, int index){
        int id = -1;
        try {
            id = this.inboxMap.get(account).getMessagesOut().get(index).getEmailID();
            view.printLog(account+" deleted Email with ID "+id+".");

            removeSntMailFile(account, id);

            this.inboxMap.get(account).getMessagesOut().remove(index);

        } catch (RemoteException e) {
            view.printLog("Error: cannot delete email with ID "+id+" (sent).");
        }
        return 0;
    }

    public int sendMail(String account, EMail e){
        this.inboxMap.get(account).getMessagesOut().add(e);
        try {
            for(String rec : e.getEmailRecipient())
                this.inboxMap.get(rec).getMessagesIn().add(0, e);

            writeMail(e);
            setIDCount();

            for(String rec : e.getEmailRecipient())
                newMailMap.put(rec, true);

            view.printLog(e.getEmailSender()+" sent an email to "+e.getEmailRecipient().toString().replace("[", "").replace("]", "")+".");

        } catch (Exception e1) {
            try {
                view.printLog("Error: "+e.getEmailSender()+" tried to send an email to unexistent recipient(s).");
            } catch (RemoteException e2) {
                view.printLog("Error: "+e2.getCause());
            }
            return -1;
        }
        return 0;
    }

    private synchronized void removeRcvMailFile(String account, int mailID){
        String PATH = System.getProperty("user.dir")+"/src/labprogiii/server/";

        File dir = new File(PATH);
        for(File child : dir.listFiles()) {

            if(child.getName().equals(account)) {

                if (child.isDirectory()) {

                    for (File f : child.listFiles()) {

                        if (f.getName().equals("received")) {
                            for (File m : f.listFiles()) {
                                if (m.getName().equals(mailID + ".csv"))
                                    m.delete();
                            }
                        }
                    }
                }
            }
        }
    }

    private synchronized void removeSntMailFile(String account, int mailID){
        String PATH = System.getProperty("user.dir")+"/src/labprogiii/server/";

        File dir = new File(PATH);
        for(File child : dir.listFiles()) {

            if(child.getName().equals(account)) {

                if (child.isDirectory()) {

                    for (File f : child.listFiles()) {

                        if (f.getName().equals("sent")) {
                            for (File m : f.listFiles()) {
                                if (m.getName().equals(mailID + ".csv"))
                                    m.delete();
                            }
                        }
                    }
                }
            }
        }
    }

    private synchronized void writeMail(EMail e) throws RemoteException {
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

    private synchronized void createFile(EMail e, String path) {
        try {
            FileWriter mail = new FileWriter(path+e.getEmailID()+".csv");

            mail.append(e.getEmailSender()+";\n");
            mail.append(e.getEmailRecipient().toString().replace("[", "").replace("]", "")+";\n");
            mail.append(e.getEmailArgument()+";\n");
            mail.append(e.getEmailText()+";\n");
            mail.append(String.valueOf(e.getEmailPriority())+";\n");
            mail.append(e.getEmailDate()+";");

            mail.close();

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void setUpInbox() throws RemoteException {
        this.newMailMap = new HashMap<>();

        String PATH = System.getProperty("user.dir")+"/src/labprogiii/server/";

        File dir = new File(PATH);

        for(File child : dir.listFiles()){
            ArrayList<EMail> emailListIn = new ArrayList<>();
            ArrayList<EMail> emailListOut = new ArrayList<>();

            if(child.isDirectory()){

                for(File f : child.listFiles()) {

                    for (File m : f.listFiles()) {

                        String ID = m.getName().replace(".csv", "");

                        if(Integer.parseInt(ID) > this.IDCount)
                            this.IDCount = Integer.parseInt(ID);

                        if (f.getName().equals("sent"))
                            emailListOut.add(0, getEmailFromPath(PATH + child.getName() + "/" + f.getName() + "/" + ID + ".csv", Integer.parseInt(ID)));

                        else if (f.getName().equals("received"))
                            emailListIn.add(0 ,getEmailFromPath(PATH + child.getName() + "/" + f.getName() + "/" + ID + ".csv", Integer.parseInt(ID)));
                    }

                }

            ServerInbox inbox = new ServerInbox(emailListIn, emailListOut);
            this.inboxMap.put(child.getName(), inbox);

            this.newMailMap.put(child.getName(), false);

            this.controller.printLog("Inbox for account "+child.getName()+" created.");

            }
        }
        setIDCount();
    }

    private EMail getEmailFromPath(String path, int ID) throws RemoteException {
        File file = new File(path);

        Scanner in = null;
        try {
            in = new Scanner(file).useDelimiter(";\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String sender = in.next();

        String rc = in.next();

        Scanner sc = new Scanner(rc).useDelimiter(",");
        ArrayList<String> recipients = new ArrayList();
        while (sc.hasNext()) {
            String tmp = sc.next();
            if(!recipients.contains(tmp))
                recipients.add(tmp);
        }

        String argument = in.next();

        String text = in.next();

        int priority = Integer.parseInt(in.next());

        String date = in.next();

        in.close();

        return new EmailServerImpl(ID, sender, recipients, argument, text, priority, date);
    }
}
