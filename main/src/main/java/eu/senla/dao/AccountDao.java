package eu.senla.dao;

import eu.senla.entities.Account;

import java.util.List;

public interface AccountDao {

    List<Account> getAll();

    Account getById(Account passedAccount);

    Account update(Account passedAccount, String phoneCode);

    Account create(Account passedAccount);

    void delete(Account passedAccount);
}
