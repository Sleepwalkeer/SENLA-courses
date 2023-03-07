package eu.senla.repository;

import eu.senla.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    //    Account findByIdEager(Integer id);
    Optional<Account> findByEmail(String email);
}
