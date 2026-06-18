package TOPSECRET.persistence.mem;

import TOPSECRET.domain.Transaction.ITransactionRepository;
import TOPSECRET.domain.Transaction.Transaction;
import TOPSECRET.domain.ValueObjects.TransactionID;
import TOPSECRET.domain.ValueObjects.UserId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * In-memory implementation of {@link ITransactionRepository}.
 * <p>
 * This repository stores transactions in a local collection and is intended
 * primarily for testing and development purposes.
 * </p>
 */
public class TransactionRepositoryMem implements ITransactionRepository {

    private final List<Transaction> transactions;

    public TransactionRepositoryMem() {
        this.transactions = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Transaction save(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("transaction cannot be null");
        }

        if (existsById(transaction.id())) {
            throw new IllegalArgumentException("transaction already exists");
        }

        transactions.add(transaction);
        return transaction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Transaction> findById(TransactionID id) {
        if (id == null) {
            throw new IllegalArgumentException("transaction id cannot be null");
        }

        return transactions.stream()
                .filter(transaction -> transaction.id().equals(id))
                .findFirst();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(TransactionID id) {
        if (id == null) {
            throw new IllegalArgumentException("transaction id cannot be null");
        }

        return transactions.stream()
                .anyMatch(transaction -> transaction.id().equals(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Transaction> findAll() {
        return List.copyOf(transactions);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Transaction> findAllByBuyerId(UserId buyerId) {
        if (buyerId == null) {
            throw new IllegalArgumentException("buyerId cannot be null");
        }

        return transactions.stream()
                .filter(transaction -> transaction.getBuyerId().equals(buyerId))
                .toList();
    }
}