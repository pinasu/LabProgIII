package labprogiii.client;

import java.util.Date;
import labprogiii.interfaces.Account;

/**
 *
 * @author pinasu
 */
    class AccountClientImpl implements Account{
        String accountName;
        Date creationDate;
        
        public AccountClientImpl(String name, Date date){
            this.accountName = name;
            this.creationDate = date;
        }
        
        @Override
        public String getAccountName() {
            return this.accountName;
        }
        
        @Override
        public Date getAccountdate() {
            return this.creationDate;
        }
        
        @Override
        public void setAccountName(String name) {
            this.accountName = name;
        }
        
        @Override
        public void setAccountDate(Date date) {
            this.creationDate = date;
        }
        
    }
