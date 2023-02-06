package eu.senla.dao;

import eu.senla.entities.Account;
import eu.senla.entities.Account;

import java.util.List;

public interface AccountDao {

    List<Account> findAll();

    Account findById(Integer id);

    Account update(Account account);

    Account save(Account account);

    void delete(Account account);

    void deleteById(Integer id);
}
