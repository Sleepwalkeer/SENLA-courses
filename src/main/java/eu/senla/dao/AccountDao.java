package eu.senla.dao;

import eu.senla.entities.Account;

import java.util.List;
import java.util.Optional;

public interface AccountDao {

    List<Account> findAll();

    Optional<Account> findById(Integer id);

    Account findByIdEager(Integer id);

    Optional<Account> findByEmail(String email);

    Account update(Account account);

    void save(Account account);

    boolean delete(Account account);

    boolean deleteById(Integer id);
}
