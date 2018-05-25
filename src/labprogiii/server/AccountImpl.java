package labprogiii.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import labprogiii.interfaces.Account;

/**
 *
 * @author pinasu
 */
    class AccountImpl extends UnicastRemoteObject implements Account{
        String accountName;
        Date creationDate;
        
        public AccountImpl(String name, Date date) throws RemoteException {
            this.accountName = name+"@pmail.it";
            this.creationDate = date;
        }
        
        @Override
        public String getAccountName() throws RemoteException{
            return this.accountName;
        }
        
        @Override
        public Date getAccountdate() throws RemoteException{
            return this.creationDate;
        }
        
        @Override
        public void setAccountName(String name) throws RemoteException{
            this.accountName = name+"@pmail.it";
        }
        
        @Override
        public void setAccountDate(Date date) throws RemoteException{
            this.creationDate = date;
        }
        
    }
