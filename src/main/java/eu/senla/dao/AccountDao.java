package eu.senla.dao;

import eu.senla.entities.Account;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AccountDao {

    List<Account> findAll();

    Account findById(Integer id);

    Account findByIdEager(Integer id);

    Account update(Account account);

    Account save(Account account);

    void delete(Account account);

    void deleteById(Integer id);
}
