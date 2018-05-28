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

/**
 *
 * @author pinasu
 */

public class ClientController implements MouseListener, ActionListener, Observer {
    
    Client client;
    Account account;
    ClientView view;
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    ArrayList<EMail> emailListIn, emailListOut;
    JTable table = new JTable();

    public ClientController(Client c) throws RemoteException {
        this.client = c;
        this.account = client.account;
        this.emailListIn = client.getEmailList();
        this.emailListOut = client.getEmailListOut();
    }
    
    public Account getAccount(){
        return this.account;
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
    
    public void showMailView(int index) throws RemoteException{
        EMail e = emailListIn.get(index);

        JFrame frame = new JFrame("Email from "+e.getEmailSender());
        JTextArea content = new JTextArea("Argument: "+e.getEmailArgument()+"\n\n\t"+e.getEmailText());
        content.setEditable(false);
        content.setBorder(BorderFactory.createCompoundBorder(
        content.getBorder(), 
        BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        
        frame.add(content);
        frame.setSize(600, 300);
        Dimension dimMv = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dimMv.width/2-frame.getSize().width/2, dimMv.height/2-frame.getSize().height/2);
        frame.setVisible(true);
    }

    public void sentMailView() throws RemoteException {
        JFrame frame = new JFrame();
        JScrollPane scrollPane = new JScrollPane();

        frame.setLayout(new BorderLayout());

        setEmailList(emailListOut);
        scrollPane = new JScrollPane(this.table);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        frame.add(panel, BorderLayout.SOUTH);

        frame.setTitle(this.getAccount().getAccountName());
        frame.setSize(600, 300);
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        frame.setVisible(true);
    }

    void setEmailList(ArrayList<EMail> emailList){
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        Vector<String> columnNames = new Vector<>();
        columnNames.add("From");
        columnNames.add("Argument");
        columnNames.add("Date");

        Vector<Vector> data = new Vector<>();

        for (EMail e : emailList) {
            Vector<String> row = new Vector<>();

            try {
                row.add(e.getEmailSender());
                row.add(e.getEmailArgument());
                row.add(e.getEmailDate().toString());
            } catch (RemoteException ex) {
                System.out.println(ex.getCause());
            }
            data.add(row);
        }

        MyTableModel model = new MyTableModel(data, columnNames);
        this.table = new JTable(model);
        this.table.setShowGrid(false);
        this.table.setFillsViewportHeight(true);
        this.table.setRowHeight(30);

        this.table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        this.table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        this.table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
    }

    public class MyTableModel extends DefaultTableModel {
        private MyTableModel(Vector<Vector> data, Vector<String> columnNames) {
            super(data, columnNames);
        }
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Write a new Email"))
            sendMailView();
        else if(e.getActionCommand().equals("Sent messages")) {
            try {
                sentMailView();
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
        }

    }
    
    @Override
    public void mouseClicked(MouseEvent ev) {
        if (client.getEmailList() != null) {
            try {
                if(view.table.getSelectedRow() < this.emailListIn.size() && view.table.getSelectedRow() != -1)
                    showMailView(view.table.getSelectedRow());
            } catch (RemoteException ex) {
                System.out.println(ex.getCause());
            }
        }
    }
    
    @Override
    public void update(Observable o, Object arg) {
        view.setEmailList((ArrayList<EMail>) arg);
    }
    
    public ArrayList<EMail> getEmailList(){
        return this.emailListIn;
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
