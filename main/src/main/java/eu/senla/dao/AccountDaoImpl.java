package eu.senla.dao;

import eu.senla.entities.Account;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class AccountDaoImpl implements AccountDao {
    private final List<Account> accounts = new LinkedList<>();

    public List<Account> getAccounts() {
        return accounts;
    }

    public List<Account> getAll() {
        return getAccounts();
    }

    public Account getById(Account passedAccount) {
        for (Account account : accounts) {
            if (passedAccount.getId() == account.getId()) {
                return account;
            }
        }
        return null;
    }

    public Account update(Account passedAccount, String phoneCode) {
        for (Account account : accounts) {
            if (passedAccount.getId() == account.getId()) {
                String newPhone = phoneCode + account.getPhone();
                account.setPhone(newPhone);
                return account;
            }
        }
        return null;
    }

    public Account create(Account passedAccount) {
        accounts.add(passedAccount);
        return passedAccount;
    }

    public void delete(Account passedAccount) {
        for (int i = 0; i < accounts.size(); i++) {
            if (passedAccount.getId() == accounts.get(i).getId()) {
                accounts.remove(i);
                return;
            }
        }
    }
}
