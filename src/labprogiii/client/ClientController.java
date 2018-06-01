package labprogiii.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;
import java.util.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import labprogiii.interfaces.EMail;

import static java.lang.System.exit;
import static java.lang.System.setOut;

/**
 *
 * @author pinasu
 */

public class ClientController implements MouseListener, ActionListener, Observer {
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

    public void sendMailView(){        
        JFrame mailFrame = new JFrame("Write a new email");
        JPanel mail = new JPanel();
        JTextArea mailContent = new JTextArea("");
        JButton sendBtn = new JButton("Send");
        JPanel buttonPanel = new JPanel();
        JTextArea recipientMail = new JTextArea("Insert recipient here");
        JTextArea argumentMail = new JTextArea("Insert argument here");
        Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY);

        mailContent.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        mail.setLayout(new BorderLayout(5,5));
        buttonPanel.setLayout(new BorderLayout(5,5));

        buttonPanel.add(recipientMail, BorderLayout.PAGE_START);
        buttonPanel.add(argumentMail, BorderLayout.CENTER);
        recipientMail.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        argumentMail.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        recipientMail.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent ev){
                recipientMail.setText("");
            }
        });
      
        argumentMail.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent ev){
                argumentMail.setText("");
            }
        }); 
        
        recipientMail.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent de) {
                sendBtn.setEnabled(true);
            }

            @Override
            public void removeUpdate(DocumentEvent de) {}

            @Override
            public void changedUpdate(DocumentEvent de) {}
            
        });
        
        mailContent.setLineWrap(true);
        mail.add(mailContent, BorderLayout.CENTER);

        mailFrame.setSize(700, 400);
        mail.add(buttonPanel, BorderLayout.NORTH);
        
        sendBtn.setSize(50, 100);
        sendBtn.setEnabled(false);
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(sendBtn);
        
        mail.add(bottomPanel, BorderLayout.SOUTH);

        mailFrame.add(mail);

        mailFrame.setLocation(dim.width/2-mailFrame.getSize().width/2, dim.height/2-mailFrame.getSize().height/2);
        mailFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (ev.getActionCommand().equals("New Email"))
            sendMailView();
        else if (ev.getActionCommand().equals("Sent"))
            view.showMailList(this.type = view.SENT_MESSAGES);

        else if (ev.getActionCommand().equals("Received"))
            view.showMailList(this.type = view.RECEIVED_MESSAGES);

    }
    
    @Override
    public void mouseClicked(MouseEvent ev) {
        ArrayList<EMail> emailList = null;

        if(this.type == view.RECEIVED_MESSAGES)
            emailList = this.emailListIn;

        else if(this.type == view.SENT_MESSAGES)
            emailList = this.emailListOut;

        try {
            if(view.table.getSelectedRow() < emailList.size() && view.table.getSelectedRow() != -1)
                view.showMail(emailList.get(view.table.getSelectedRow()));
        } catch (RemoteException ex) {
            System.out.println(ex.getCause());
        }
    }
    
    @Override
    public void update(Observable o, Object arg) {
        //view.setEmailList((ArrayList<EMail>) arg);
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

}
