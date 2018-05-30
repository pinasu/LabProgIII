package labprogiii.client;

import java.awt.*;
import java.rmi.RemoteException;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import labprogiii.interfaces.EMail;

class ClientView extends JFrame {
    final int RECEIVED_MESSAGES = 0;
    final int SENT_MESSAGES = 1;

    JPanel menu;
    JButton sentButton, receivedButton, newMailButton;

    JPanel extern;
    JLabel title;
    JScrollPane body;

    JTable table;

    Vector<Vector> data;

    Dimension dim;

    Client client;

    ClientController controller;

    MyTableModel model;

    public ClientView(Client c) {

        this.client = c;

        this.controller = new ClientController(c, this);

        this.setTitle(client.getAccount().getAccountName()+"@sasi.com");

        this.menu = newMenu();

        showMailList(RECEIVED_MESSAGES);

        this.setDefaultCloseOperation(3);
        this.setSize(900, 600);

        this.dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        this.setVisible(true);

    }

    public void showMail(EMail e) throws RemoteException{
        JFrame frame = new JFrame();
        if(controller.getType() == SENT_MESSAGES)
            frame.setTitle("Email to "+e.getEmailRecipient().toString());
        else if(controller.getType() == RECEIVED_MESSAGES)
            frame.setTitle("Email from "+e.getEmailSender());

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

    void showMailList(int type) {
        this.data = null;

        this.setLayout(new BorderLayout());

        this.title = new JLabel();

        this.add(this.menu, BorderLayout.WEST);

        this.extern = new JPanel();
        this.extern.setLayout(new BorderLayout());
        this.extern.add(this.title, BorderLayout.NORTH);

        this.add(this.extern, BorderLayout.CENTER);
        Vector<String> columnNames;
        String v = "";

        if (type == 0){
            this.title.setText("Received");
            v = "From";
        }
        else if(type == 1){
            this.title.setText("Sent");
            v = "To";
        }

        this.data = this.client.populateData(type);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        columnNames = new Vector<>();
        columnNames.add(v);
        columnNames.add("Argument");
        columnNames.add("Date");

        this.model = new MyTableModel(this.data, columnNames);

        this.table = new JTable(model);
        this.table.setShowGrid(false);
        this.table.setFillsViewportHeight(true);
        this.table.setRowHeight(30);
        this.table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        this.table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        this.table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        this.body = new JScrollPane(this.table);
        this.extern.add(body);

        this.table.addMouseListener(this.controller);

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

    JPanel newMenu(){
        JPanel menu = new JPanel();
        menu.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        menu.setLayout(new BoxLayout(menu, BoxLayout.PAGE_AXIS));

        menu.add(receivedButton = new JButton("Received"));
        menu.add(sentButton = new JButton("Sent"));
        menu.add(newMailButton = new JButton("New Email"));

        newMailButton.addActionListener(this.controller);
        sentButton.addActionListener(this.controller);
        receivedButton.addActionListener(this.controller);

        return menu;
    }

}
