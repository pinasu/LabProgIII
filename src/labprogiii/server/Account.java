package labprogiii.server;

import java.util.Date;

/**
 *
 * @author pinasu
 */
    class Account {
        String accountName;
        Date creationDate;
        
        public Account(String name, Date date){
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
