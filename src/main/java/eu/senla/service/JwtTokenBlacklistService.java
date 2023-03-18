package eu.senla.service;

public interface JwtTokenBlacklistService {

    /**
     * Checks whether a JWT token is blacklisted.
     *
     * @param token The JWT token to check.
     * @return true if the token is blacklisted, false otherwise.
     */
    boolean isTokenBlacklisted(String token);

    /**
     * Blacklists a JWT token.
     *
     * @param token The JWT token to blacklist.
     */
    void blacklistToken(String token);
}
