package labprogiii.client;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientMain {
    public static void main(String[] args) {

        //new Client(new Account("lino", new SimpleDateFormat("dd/MM/yyyy").format(new Date())));

        new Client(new Account("mino", new SimpleDateFormat("dd/MM/yyyy").format(new Date())));

        //new Client(new Account("pino", new SimpleDateFormat("dd/MM/yyyy").format(new Date())));

    }
}