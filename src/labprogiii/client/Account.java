package labprogiii.client;

import java.util.Date;

/**
 *
 * @author pinasu
 */
    class AccountClient{
        String accountName;
        Date creationDate;
        
        public AccountClient(String name, Date date){
            this.accountName = name;
            this.creationDate = date;
        }
        
        public String getAccountName() {
            return this.accountName;
        }

        public Date getAccountdate() {
            return this.creationDate;
        }
        
        public void setAccountName(String name) {
            this.accountName = name;
        }
        
        public void setAccountDate(Date date) {
            this.creationDate = date;
        }
        
    }
