package labprogiii.server;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

class ServerView extends JFrame implements Observer {
    ServerController controller;
    Server server;
    JTextArea textArea;
    JScrollPane scrollPane;
    
    public ServerView(Server server){
        super("Server Log");

        this.controller = new ServerController(server, this);

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

        this.textArea = new JTextArea("All Server events will be registered here.");
        this.textArea.setEditable(false);
        this.scrollPane = new JScrollPane(textArea);
        this.getContentPane().add(scrollPane);
        this.setVisible(true);
        this.setDefaultCloseOperation(3);
        this.setSize(600, 300);

        this.server = server;
    }

    public ServerController getController(){
        return this.controller;
    }
    
    public void printLog(String message) {
        DateFormat dateFormat = new SimpleDateFormat("[HH:mm:ss] ");
        Date date = new Date();
        this.textArea.setText(this.textArea.getText()+"\n"+dateFormat.format(date)+message);
        this.textArea.repaint();
    }

    @Override
    public void update(Observable o, Object arg) {
        
    }

}
