package labprogiii.client;

import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import labprogiii.interfaces.EMail;

/**
 *
 * @author pinasu
 */
class ClientView extends JFrame {
    JPanel menu;
    JButton sentButton, receivedButton, newMailButton;

    JScrollPane body;
    JPanel extern;
    JLabel title;

    JTable table;

    //ON UPDATE -> ADD
    Vector<Vector> data;

    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

    ClientController controller;

    public ClientView(ClientController c) {
        this.controller = c;

        this.menu = newMenu();

        this.title = new JLabel();
        this.body = new JScrollPane();

        this.setLayout(new BorderLayout());

        //Received
        showMail(0);

        this.add(menu, BorderLayout.WEST);

        extern = new JPanel();
        extern.setLayout(new BorderLayout());
        extern.add(title, BorderLayout.NORTH);
        extern.add(body);

        this.add(extern, BorderLayout.CENTER);

        this.setTitle(controller.getAccount().getAccountName()+"@pmail.com");
        this.setDefaultCloseOperation(3);
        this.setSize(900, 600);
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        this.setVisible(true);


    }

    JPanel newMenu(){
        JPanel menu = new JPanel();
        menu.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        menu.setLayout(new BoxLayout(menu, BoxLayout.PAGE_AXIS));

        menu.add(receivedButton = new JButton("Received"));
        menu.add(sentButton = new JButton("Sent"));
        menu.add(newMailButton = new JButton("New Email"));

        newMailButton.addActionListener(this.controller);
        return menu;
    }

    Vector<Vector> populateData(ArrayList<EMail> emailList){
        Vector<Vector> tmp = new Vector<>();
        for (EMail e : emailList) {
            Vector<String> row = new Vector<>();

            try {
                row.add(e.getEmailSender());
                row.add(e.getEmailArgument());
                row.add(e.getEmailDate().toString());
            } catch (RemoteException ex) {
                System.out.println(ex.getCause());
            }
            tmp.add(row);
        }
        return tmp;
    }

    void showMail(int type) {
        Vector<String> columnNames;
        String v = "";
        MyTableModel model;

        if (type == 0){
            this.title.setText("Received");
            v = "From";
        }
        else if(type == 1){
            this.title.setText("Sent");
            v = "To";
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        columnNames = new Vector<>();
        columnNames.add(v);
        columnNames.add("Argument");
        columnNames.add("Date");

        model = new MyTableModel(this.data, columnNames);

        table = new JTable(model);
        table.setShowGrid(false);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        body = new JScrollPane(table);

        table.addMouseListener(this.controller);

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
