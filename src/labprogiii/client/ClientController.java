package labprogiii.client;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.*;
import labprogiii.interfaces.EMail;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class ClientController implements MouseListener, ActionListener, Observer {
    Client client;
    ClientView view;

    int p = 0;

    Account account;
    ArrayList<EMail> emailListIn, emailListOut;

    NewMailView answer;

    EMail currentMail;

    int emailID = 4;

    int type = 0;

    public ClientController(Client c, ClientView view) {
        this.client = c;
        this.account = client.getAccount();

        this.view = view;

        this.emailListIn = this.client.getEmailListIn();
        this.emailListOut = this.client.getEmailListOut();
    }

    public int getType() {
        return this.type;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (ev.getActionCommand().equals("New Email")) {
            this.answer = view.newMailView();

        } else if (ev.getActionCommand().equals("Sent")) {
            this.type = view.SENT_MESSAGES;

            if (view.title.getText().equals("Received"))
                view.changeTitle("Sent", this.type);

            view.showMailList(this.type);
        } else if (ev.getActionCommand().equals("Received")) {
            this.type = view.RECEIVED_MESSAGES;

            if (view.title.getText().equals("Sent"))
                view.changeTitle("Received", this.type);

            view.showMailList(this.type);

        } else if (ev.getActionCommand().equals(("Answer"))) {
            try {
                this.answer = view.newMailView(this.currentMail);

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if (ev.getActionCommand().equals("Send")) {
            try {
                ArrayList<String> recipients = new ArrayList();
                String[] splitted = this.answer.getRecipient().split(",");

                for (String s : splitted)
                    recipients.add(s.replace(",", ""));

                this.currentMail = new EmailClientImpl(emailID++, account.getAccountName(), recipients, this.answer.getArgument(), this.answer.getContent(), 1, new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

                int resp = client.sendMail(this.currentMail);

                if (resp == -1)
                    view.showPopUp("One or more recipients do not exist: try again.");
                else {
                    view.showPopUp("EMail sent correctly.");

                    view.showMailList(1);
                    this.answer.setVisible(false);
                    this.answer.dispose();
                }

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else if (ev.getActionCommand().equals("Delete")) {
            try {
                int r;
                if(this.type == 0)
                    r = client.deleteReceivedMail(this.p);
                else
                    r = client.deleteSentMail(this.p);
                if(r == -1)
                    view.showPopUp("Error deleting Email.");
                else
                    view.showPopUp("Mail deleted.");

            } catch (RemoteException e) {
                e.printStackTrace();
            }
            view.showMailList(0);
        }
    }

    @Override
    public void mouseClicked(MouseEvent ev) {
        ArrayList<EMail> emailList = new ArrayList<>();

        if (this.type == view.RECEIVED_MESSAGES)
            emailList = this.emailListIn;

        else if (this.type == view.SENT_MESSAGES)
            emailList = this.emailListOut;

        try {
            if (view.getTable().getSelectedRow() < emailList.size() && view.getTable().getSelectedRow() != -1) {
                this.currentMail = emailList.get(view.getTable().getSelectedRow());
                view.showMail(currentMail);
            }

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
        if (SwingUtilities.isRightMouseButton(e)) {
            this.p = view.getTable().rowAtPoint(e.getPoint());
            view.deletePopUp();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void update(Observable o, Object arg) {

    }

}
