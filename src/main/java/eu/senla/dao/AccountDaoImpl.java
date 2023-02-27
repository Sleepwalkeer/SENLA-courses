package eu.senla.dao;

import eu.senla.entities.Account;
import eu.senla.entities.Account_;
import eu.senla.entities.Credentials;
import eu.senla.entities.Credentials_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
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
        query.setParameter("id", id);
        return (Account) query.getSingleResult();
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Account> query = builder.createQuery(Account.class);
        Root<Account> root = query.from(Account.class);
        query.select(root);
        query.where(builder.equal(root.get(Account_.email), email));
        TypedQuery<Account> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList().stream().findFirst();

    }

    @Override
    public boolean delete(Account entity) {
        return deleteById(entity.getId());
    }
}
