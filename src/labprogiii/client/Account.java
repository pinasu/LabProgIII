package labprogiii.client;

import java.util.Date;

class Account{
    String accountName;
    String creationDate;

    public Account(String name, String date){
        this.accountName = name+"@sasi.com";
        this.creationDate = date;
    }

    public String getAccountName() {
        return this.accountName;
    }

}
