package labprogiii.server;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author pinasu
 */
class ServerView extends JFrame implements Observer {
    
    JTextArea textArea;
    JScrollPane scrollPane;
    
    public ServerView(ServerController controller){
        super("Server Log");
        this.textArea = new JTextArea("All Server events will be registered here.");
        this.textArea.setEditable(false);
        this.scrollPane = new JScrollPane(textArea);
        this.getContentPane().add(scrollPane);
        this.setVisible(true);
        this.setDefaultCloseOperation(3);
        this.setSize(600, 300);
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
