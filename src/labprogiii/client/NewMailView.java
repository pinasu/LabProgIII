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
    JScrollPane scrollPane;

    public NewMailView(ClientController controller){
        DocumentListener dl = null;

        this.setTitle(controller.getAccount()+", write a new email");

        JPanel mail = new JPanel();
        this.mailContent = new JTextArea("");

        scrollPane = new JScrollPane(mailContent);

        JButton sendBtn = new JButton("Send");
        JPanel buttonPanel = new JPanel();

        this.recipientMail = new JTextArea("Insert comma-separated recipients here");
        this.argumentMail = new JTextArea("Insert argument here");

        Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY);

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
                recipientMail.removeMouseListener(this);
            }
        });

        argumentMail.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent ev){
                argumentMail.setText("");
                argumentMail.removeMouseListener(this);
            }
        });

        DocumentListener finalDl = dl;
        recipientMail.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent de) {
                sendBtn.setEnabled(true);
                recipientMail.getDocument().removeDocumentListener(finalDl);
            }

            @Override
            public void removeUpdate(DocumentEvent de) {}

            @Override
            public void changedUpdate(DocumentEvent de) {}

        });

        mailContent.setLineWrap(true);

        scrollPane.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        mail.add(scrollPane, BorderLayout.CENTER);

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

    public NewMailView(ClientController controller, EMail e, int type) throws RemoteException {
        JPanel mail = new JPanel();
        JButton sendBtn = new JButton("Send");
        JPanel buttonPanel = new JPanel();

        if(type == 0) {
            this.setTitle("Answer " + e.getEmailSender());
            mailContent = new JTextArea(e.getEmailText()+"\n\t"+e.getEmailSender()+" wrote on "+e.getEmailDate()+"\n\nRE:\n");
            scrollPane = new JScrollPane(mailContent);
            recipientMail = new JTextArea(e.getEmailSender());
            argumentMail = new JTextArea("RE: " + e.getEmailArgument());
        }
        else {
            this.setTitle("Forward email");
            mailContent = new JTextArea(e.getEmailText()+"\n\tforwarded by "+e.getEmailSender()+"\n\n");
            scrollPane = new JScrollPane(mailContent);
            recipientMail = new JTextArea("Insert comma-separated recipients whom forward here");
            argumentMail = new JTextArea("FW: " + e.getEmailArgument());
        }

        Border border = BorderFactory.createLineBorder(Color.LIGHT_GRAY);

        mail.setLayout(new BorderLayout(5, 5));
        buttonPanel.setLayout(new BorderLayout(5, 5));

        buttonPanel.add(recipientMail, BorderLayout.PAGE_START);
        buttonPanel.add(argumentMail, BorderLayout.CENTER);
        recipientMail.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        argumentMail.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        if(type == 0) {
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
        }

        else{
            recipientMail.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent de) {
                    recipientMail.removeAll();
                    sendBtn.setEnabled(true);
                }

                @Override
                public void removeUpdate(DocumentEvent de) {
                }

                @Override
                public void changedUpdate(DocumentEvent de) {
                }

            });

            recipientMail.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent ev){
                    recipientMail.setText("");
                    recipientMail.removeAll();
                }
            });
        }

        this.setSize(700, 400);
        mail.add(buttonPanel, BorderLayout.NORTH);

        mailContent.setLineWrap(true);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        mail.add(scrollPane, BorderLayout.CENTER);

        sendBtn.setSize(50, 100);
        sendBtn.setEnabled(false);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(sendBtn);

        mail.add(bottomPanel, BorderLayout.SOUTH);

        this.add(mail);

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
