package labprogiii.client;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;
import java.util.*;
import labprogiii.interfaces.EMail;


public class ClientController implements MouseListener, ActionListener {
    Client client;
    ClientView view;

    Account account;
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    ArrayList<EMail> emailListIn, emailListOut;

    int type;

    public ClientController(Client c, ClientView view) {
        this.client = c;
        this.account = client.getAccount();

        this.view = view;

        this.emailListIn = this.client.getEmailListIn();
        this.emailListOut = this.client.getEmailListOut();
    }

    public int getType(){
        return this.type;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (ev.getActionCommand().equals("New Email"))
            view.newMailView();

        else if (ev.getActionCommand().equals("Sent")) {
            this.type = view.SENT_MESSAGES;

            if(view.title.getText().equals("Received"))
                view.changeTitle("Sent", this.type);

            view.showMailList(this.type);
        }

        else if (ev.getActionCommand().equals("Received")) {
            this.type = view.RECEIVED_MESSAGES;

            if(view.title.getText().equals("Sent"))
                view.changeTitle("Received", this.type);

            view.showMailList(this.type);

        }
    }

    @Override
    public void mouseClicked(MouseEvent ev) {
        ArrayList<EMail> emailList = null;

        if(this.type == view.RECEIVED_MESSAGES)
            emailList = this.emailListIn;

        else if(this.type == view.SENT_MESSAGES)
            emailList = this.emailListOut;

        try {
            if(view.getTable().getSelectedRow() < emailList.size() && view.getTable().getSelectedRow() != -1)
                view.showMail(emailList.get(view.getTable().getSelectedRow()));

            } catch (RemoteException ex) {
            System.out.println(ex.getCause());
        }
        view.getTable().clearSelection();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    /*
    @Override
    public void update(Observable o, Object arg) {
        //view.setEmailList((ArrayList<EMail>) arg);
    }*/
}
