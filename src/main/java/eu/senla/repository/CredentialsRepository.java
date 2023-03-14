package eu.senla.repository;

import eu.senla.entity.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CredentialsRepository extends JpaRepository<Credentials, Long> {


    Optional<Credentials> findByUsername(String username);
}
