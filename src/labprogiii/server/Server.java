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
            mail.append(String.valueOf(e.getEmailPriority())+";\n");
            mail.append(e.getEmailDate()+";");

            mail.close();

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


    private void setUpInbox() throws RemoteException {

        String PATH = System.getProperty("user.dir")+"/src/labprogiii/server/";

        File dir = new File(PATH);

        for(File child : dir.listFiles()){
            ArrayList<EMail> emailListIn = new ArrayList<>();
            ArrayList<EMail> emailListOut = new ArrayList<>();

            if(child.isDirectory()){

                for(File f : child.listFiles()) {

                    if (f.getName().equals("sent")) {

                        for (File m : f.listFiles()) {
                            String ID = m.getName().replace(".csv", "");
                            emailListOut.add(getEmailFromPath(PATH + child.getName() + "/" + f.getName() + "/" + ID + ".csv", Integer.parseInt(ID)));
                        }

                    }
                    else if (f.getName().equals("received")) {
                        for (File m : f.listFiles()) {
                            String ID = m.getName().replace(".csv", "");
                            EMail a = getEmailFromPath(PATH + child.getName() + "/" + f.getName() + "/" + ID + ".csv", Integer.parseInt(ID));

                            System.out.println("AGGIUNGO LA MAIL CON ID DEL CAZZO "+a.getEmailID()+" A QUEL FROCIO DI "+child.getName());
                            emailListIn.add(a);

                        }

                    }

                }

                ServerInbox inbox = new ServerInbox(emailListIn, emailListOut);
                inboxMap.put(child.getName(), inbox);
                this.controller.printLog("Inbox for account "+child.getName()+" created.");

            }
        }
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
        System.out.println(sender);

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
