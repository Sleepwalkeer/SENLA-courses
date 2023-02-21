package eu.senla.security;

import eu.senla.dao.CredentialsDao;
import eu.senla.entities.Credentials;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final CredentialsDao credentialsDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Credentials credentials = credentialsDao.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
       return SecurityUser.fromCredentials(credentials);
    }
}
