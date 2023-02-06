package eu.senla.dao;

import eu.senla.entities.Account;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Component;

@Component
public class AccountDaoImpl extends AbstractDAO<Integer, Account> implements AccountDao {

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    Class<Account> getEntityClass() {
        return Account.class;
    }

    @Override
    public Account findByIdEager(Integer id) {
        Query query = entityManager.createQuery
                ("Select acc from Account acc JOIN FETCH acc.credentials creds WHERE acc.id = :id");
        query.setParameter("id",id);
        Account account = (Account) query.getSingleResult();
        return account;
    }
}
