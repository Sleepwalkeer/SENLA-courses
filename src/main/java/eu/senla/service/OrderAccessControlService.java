package eu.senla.service;

public interface OrderAccessControlService {
    /**
     * Checks whether a given order is accessible by a given account.
     *
     * @param orderId   The ID of the order to check.
     * @param accountId The ID of the account to check against.
     * @return true if the order is accessible by the account, false otherwise.
     */
    boolean isOrderAccessibleByUser(Long orderId, Long accountId);
}
