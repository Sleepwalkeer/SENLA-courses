package eu.senla.security;

import eu.senla.dao.CredentialsDao;
import eu.senla.entities.Credentials;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private  CredentialsDao credentialsDao;

    @Autowired
    public UserDetailsServiceImpl(CredentialsDao credentialsDao) {
        this.credentialsDao = credentialsDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Credentials credentials = credentialsDao.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
       return SecurityUser.fromCredentials(credentials);
    }
}
