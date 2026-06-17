package TOPSECRET.domain.Transaction;

import TOPSECRET.domain.ValueObjects.TransactionID;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Transaction} aggregate roots.
 */
public interface ITransactionRepository {

    /**
     * Stores a transaction.
     *
     * @param transaction the transaction to store
     * @return the stored transaction
     * @throws IllegalArgumentException if the transaction is null
     */
    Transaction save(Transaction transaction);

    /**
     * Finds a transaction by its unique identifier.
     *
     * @param id the transaction identifier
     * @return an optional containing the transaction if found, otherwise empty
     */
    Optional<Transaction> findById(TransactionID id);

    /**
     * Checks whether a transaction with the given identifier exists.
     *
     * @param id the transaction identifier
     * @return {@code true} if the transaction exists, otherwise {@code false}
     */
    boolean existsById(TransactionID id);

    /**
     * Returns all stored transactions.
     *
     * @return a list containing all transactions
     */
    List<Transaction> findAll();
}