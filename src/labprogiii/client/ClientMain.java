package labprogiii.client;

import java.rmi.RemoteException;
import java.util.Date;
import javax.naming.NamingException;

public class ClientMain {
    public static void main(String[] args) {

        new Client(new Account("gianni", new Date()));
    }
}