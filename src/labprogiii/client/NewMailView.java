package labprogiii.client;

import labprogiii.interfaces.EMail;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;

public class NewMailView extends JFrame{
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    JTextArea recipientMail, mailContent, argumentMail;

    public NewMailView(ClientController controller){
        this.setTitle("Write a new email");

        JPanel mail = new JPanel();
        this.mailContent = new JTextArea("");
        JButton sendBtn = new JButton("Send");
        JPanel buttonPanel = new JPanel();

        this.recipientMail = new JTextArea("Insert comma-separated recipient here");
        this.argumentMail = new JTextArea("Insert argument here");

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

        this.setSize(700, 400);
        mail.add(buttonPanel, BorderLayout.NORTH);

        sendBtn.setSize(50, 100);
        sendBtn.setEnabled(false);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(sendBtn);

        mail.add(bottomPanel, BorderLayout.SOUTH);

        this.add(mail);

        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        this.setVisible(true);

        sendBtn.addActionListener(controller);
    }

    public NewMailView(ClientController controller, EMail e) throws RemoteException {
        this.setTitle("Answer " + e.getEmailSender());
        JPanel mail = new JPanel();

        mailContent = new JTextArea("");
        JButton sendBtn = new JButton("Send");
        JPanel buttonPanel = new JPanel();

        recipientMail = new JTextArea(e.getEmailSender());
        argumentMail = new JTextArea("RE: " + e.getEmailArgument());

        Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY);

        mailContent.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        mail.setLayout(new BorderLayout(5, 5));
        buttonPanel.setLayout(new BorderLayout(5, 5));

        buttonPanel.add(recipientMail, BorderLayout.PAGE_START);
        buttonPanel.add(argumentMail, BorderLayout.CENTER);
        recipientMail.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        argumentMail.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        mailContent.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent de) {
                sendBtn.setEnabled(true);
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
            }

        });


        this.setSize(700, 400);
        mail.add(buttonPanel, BorderLayout.NORTH);

        sendBtn.setSize(50, 100);
        sendBtn.setEnabled(false);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(sendBtn);

        mail.add(bottomPanel, BorderLayout.SOUTH);

        this.add(mail);

        mailContent.setLineWrap(true);

        mailContent.setCaretPosition(recipientMail.getSelectionStart());
        mailContent.moveCaretPosition(recipientMail.getSelectionStart());

        mail.add(mailContent, BorderLayout.CENTER);

        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        this.setVisible(true);

        sendBtn.addActionListener(controller);
    }

    public String getRecipient(){
        return this.recipientMail.getText();
    }
    public String getArgument(){
        return this.argumentMail.getText();
    }
    public String getContent(){
        return this.mailContent.getText();
    }
}
