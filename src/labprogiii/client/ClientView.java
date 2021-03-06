package labprogiii.client;

import java.awt.*;
import java.rmi.RemoteException;
import java.util.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import labprogiii.interfaces.EMail;

class ClientView extends JFrame implements Observer {
    final int RECEIVED_MESSAGES = 0;
    final int SENT_MESSAGES = 1;

    String v = "From";

    JPanel menu;

    JPanel extern;
    JLabel title;
    JScrollPane body;

    JTable table;

    Vector<Vector> data;

    Dimension dim;

    Client client;
    ClientController controller;

    public ClientView(Client c) {
        this.client = c;
        this.controller = new ClientController(c, this);

        client.addObserver(this);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        this.setTitle(client.getAccount().getAccountName());

        this.setLayout(new BorderLayout());

        this.extern = new JPanel();
        this.extern.setLayout(new BorderLayout());
        this.table = new JTable();
        this.title = new JLabel("Received");
        this.extern.add(this.title, BorderLayout.NORTH);
        this.add(this.menu = newMenu(), BorderLayout.WEST);
        this.add(this.extern, BorderLayout.CENTER);

        showMailList(0);

        this.body = new JScrollPane(this.table);
        this.extern.add(body);

        this.setDefaultCloseOperation(3);
        this.setSize(900, 600);

        this.dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        this.setVisible(true);

        this.addWindowListener(this.controller);
    }

    public JTable getTable() {
        return this.table;
    }

    public void showMail(EMail e) throws RemoteException {
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());

        if (controller.getType() == SENT_MESSAGES) {
            frame.setTitle("Email sent to " + e.getEmailRecipient().toString().replace("[", "").replace("]", ""));
        } else if (controller.getType() == RECEIVED_MESSAGES) {
            frame.setTitle("Email from " + e.getEmailSender());
            JPanel buttons = new JPanel();

            buttons.setLayout(new GridLayout(1, 0, 2, 1));
            GridBagConstraints c = new GridBagConstraints();
            c.gridheight = 1;

            JButton answerButton = new JButton("Answer");
            c.gridy = 0;
            answerButton.addActionListener(this.controller);

            JButton forwardButton = new JButton("Forward");
            c.gridy = 1;
            forwardButton.addActionListener(this.controller);

            buttons.add(answerButton, c);
            buttons.add(forwardButton, c);

            frame.add(buttons, BorderLayout.SOUTH);
        }

        JTextArea content = new JTextArea("Argument: " + e.getEmailArgument() + "\n\n" + e.getEmailText());

        JScrollPane scrollPane = new JScrollPane(content);

        content.setEditable(false);
        content.setBorder(BorderFactory.createCompoundBorder(
                content.getBorder(),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setSize(600, 300);
        Dimension dimMv = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dimMv.width / 2 - frame.getSize().width / 2, dimMv.height / 2 - frame.getSize().height / 2);
        frame.setVisible(true);

    }

    void changeTitle(String title, int type) {
        this.title.setText(title);

        if (type == RECEIVED_MESSAGES)
            this.v = "From";
        else if (type == SENT_MESSAGES)
            this.v = "To";

    }

    void showMailList(int type) {
        Vector<String> columnNames;

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        columnNames = new Vector<>();
        columnNames.add(this.v);
        columnNames.add("Argument");
        columnNames.add("Date");

        this.data = this.controller.populateData(type);

        this.table.setModel(new MyTableModel(this.data, columnNames));

        this.table.setShowGrid(false);
        this.table.setFillsViewportHeight(true);
        this.table.setRowHeight(25);

        this.table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        this.table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        this.table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        this.table.getTableHeader().setReorderingAllowed(false);

        this.table.addMouseListener(this.controller);

    }

    void showPopUp(String info) {
        JOptionPane.showMessageDialog(null, info, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    public NewMailView newMailView(EMail e, int type) throws RemoteException {
        return new NewMailView(this.controller, e, type);
    }

    public NewMailView newMailView() {
        return new NewMailView(this.controller);
    }

    public void deletePopUp() {
        final JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Delete");

        popupMenu.add(deleteItem);

        table.setComponentPopupMenu(popupMenu);

        deleteItem.addActionListener(this.controller);
    }

    private JPanel newMenu(){
        JPanel ext = new JPanel();
        JPanel menu = new JPanel();

        menu.setLayout(new GridLayout(3,0,0,5));
        GridBagConstraints c = new GridBagConstraints();
        c.gridheight = 1;

        JButton receivedButton = new JButton("Received");
        c.gridy = 0;
        menu.add(receivedButton, c);

        JButton sentButton = new JButton("Sent");
        c.gridy = 1;
        menu.add(sentButton, c);

        JButton newMailButton = new JButton("New Email");
        c.gridy = 2;
        menu.add(newMailButton, c);

        this.title.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));

        newMailButton.addActionListener(this.controller);
        sentButton.addActionListener(this.controller);
        receivedButton.addActionListener(this.controller);

        ext.add(menu);
        ext.setBorder(BorderFactory.createEmptyBorder(19,3,3,3));
        return ext;
    }

    @Override
    public void update(Observable o, Object arg) {
        this.data = (Vector<Vector>) arg;

        if (controller.getType() == 0)
            showMailList(0);

        ToastMessage toastMessage = new ToastMessage("New mail from "+this.data.get(0).get(0)+": "+this.data.get(0).get(1), 8000, this);
        toastMessage.setVisible(true);


    }

    private class ToastMessage extends JDialog {
        int miliseconds;
        public ToastMessage(String toastString, int time, JFrame view) {
            this.miliseconds = time;
            this.setLocationRelativeTo(view);

            setUndecorated(true);
            getContentPane().setLayout(new BorderLayout(0, 0));

            JPanel panel = new JPanel();
            panel.setBackground(Color.BLACK);
            panel.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
            getContentPane().add(panel, BorderLayout.SOUTH);

            JLabel toastLabel = new JLabel("");
            toastLabel.setText(toastString);
            toastLabel.setForeground(Color.WHITE);

            setBounds(100, 100, toastLabel.getPreferredSize().width+20, 31);

            setAlwaysOnTop(true);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            int y = dim.height/2-getSize().height/2;
            int half = y/2;
            setLocation(dim.width/2-getSize().width/2, y+half);
            panel.add(toastLabel);
            setVisible(false);

            new Thread(() -> {
                try {
                    Thread.sleep(miliseconds);
                    dispose();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private class MyTableModel extends DefaultTableModel {

        private MyTableModel(Vector<Vector> data, Vector<String> columnNames) {
            super(data, columnNames);

        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

    }


}
