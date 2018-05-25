/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labprogiii.server;

import java.util.Date;
import labprogiii.interfaces.Account;

/**
 *
 * @author pinasu
 */
    class AccountServerImpl implements Account{
        String accountName;
        Date creationDate;
        
        public AccountServerImpl(String name, Date date){
            this.accountName = name+"@pmail.it";
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
            this.accountName = name+"@pmail.it";
        }
        
        @Override
        public void setAccountDate(Date date) {
            this.creationDate = date;
        }
        
    }
