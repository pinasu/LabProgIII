package labprogiii.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import labprogiii.interfaces.EMail;

/**
 *
 * @author pinasu
 */
class ClientView extends JFrame {
    JTable table;
    JScrollPane scrollPane;
    JPanel panel;
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    ClientController controller;
    MyTableModel model;


    public ClientView(ClientController c) throws RemoteException{
        this.controller = c;

        this.setLayout(new BorderLayout());
        
        setEmailList(controller.getEmailList());
        this.scrollPane = new JScrollPane(table);
        this.add(scrollPane, BorderLayout.CENTER);
        
        table.addMouseListener(this.controller);

        JButton showSent = new JButton("Sent messages");
        showSent.addActionListener(this.controller);

        JButton sendMail = new JButton("Write a new Email");
        panel = new JPanel();
        panel.add(sendMail);
        panel.add(showSent);
        this.add(panel, BorderLayout.SOUTH);
        
        sendMail.addActionListener(this.controller);
        
        this.setTitle(controller.getAccount().getAccountName()+"@pmail.com");
        this.setDefaultCloseOperation(3);
        this.setSize(900, 600);
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        this.setVisible(true);
        
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
        
        model = new MyTableModel(data, columnNames);
        this.table = new JTable(model);
        this.table.setShowGrid(false);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);    
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

}
