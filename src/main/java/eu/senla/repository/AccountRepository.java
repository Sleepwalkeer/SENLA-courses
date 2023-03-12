package eu.senla.repository;

import eu.senla.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    //    Account findByIdEager(Integer id);
    Optional<Account> findByEmail(String email);

    @Override
    Page<Account> findAll(@NonNull Pageable pageable);
}
