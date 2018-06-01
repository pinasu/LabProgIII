package labprogiii.server;

import java.util.Date;

class Account {
    String accountName;
    Date creationDate;

    Account(String name, Date date){
        this.accountName = name;
        this.creationDate = date;
    }

    String getAccountName() {
        return this.accountName;
    }

}
