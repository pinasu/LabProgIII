package labprogiii.client;

import java.util.Date;

class Account{
    String accountName;
    Date creationDate;

    public Account(String name, Date date){
        this.accountName = name;
        this.creationDate = date;
    }

    public String getAccountName() {
        return this.accountName;
    }

}
