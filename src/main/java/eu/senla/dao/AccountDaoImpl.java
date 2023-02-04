package eu.senla.dao;

import eu.senla.entities.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountDaoImpl extends AbstractDAO<Integer, Account> implements AccountDao {


    @Override
    Class<Account> getEntityClass() {
        return Account.class;
    }
}
