package labprogiii.server;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

class ServerView extends JFrame implements Observer {
    ServerController controller;
    JTextArea textArea;
    JScrollPane scrollPane;
    
    public ServerView(ServerController controller){
        super("Server Log");
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

        this.controller = controller;
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
