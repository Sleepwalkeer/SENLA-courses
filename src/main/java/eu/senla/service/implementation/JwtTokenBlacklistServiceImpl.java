package eu.senla.service.implementation;

import eu.senla.service.JwtTokenBlacklistService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class JwtTokenBlacklistServiceImpl implements JwtTokenBlacklistService {

    private final Set<String> tokenBlacklist = new HashSet<>();

    @Override
    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklist.contains(token);
    }

    @Override
    public void blacklistToken(String token) {
        tokenBlacklist.add(token);
    }
}
