package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.Transaction.ITransactionRepository;
import TOPSECRET.domain.Transaction.Transaction;
import TOPSECRET.domain.ValueObjects.TransactionID;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.persistence.repositoryjpa.assembler.TransactionAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.TransactionDM;
import TOPSECRET.persistence.repositoryjpa.jpa.ITransactionRepositoryJpa;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * JPA-backed implementation of {@link ITransactionRepository}.
 *
 * <p>Acts as an anti-corruption layer between the domain and the persistence
 * infrastructure. All reads and writes are routed through
 * {@link TransactionAssembler}, which converts between {@link Transaction}
 * domain objects and {@link TransactionDM} persistence entities.</p>
 *
 * <p>This class is the only component in the persistence layer that is
 * permitted to interact directly with {@link ITransactionRepositoryJpa}.
 * Domain services and application services must depend solely on
 * {@link ITransactionRepository}.</p>
 */
@Repository
public class TransactionRepositoryJPAImpl implements ITransactionRepository {

    private final ITransactionRepositoryJpa transactionRepositoryJpa;
    private final TransactionAssembler transactionAssembler;

    public TransactionRepositoryJPAImpl(ITransactionRepositoryJpa transactionRepositoryJpa,
                                        TransactionAssembler transactionAssembler) {
        this.transactionRepositoryJpa = transactionRepositoryJpa;
        this.transactionAssembler = transactionAssembler;
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public Transaction save(Transaction transaction) {
        Objects.requireNonNull(transaction, "Transaction cannot be null");

        TransactionDM dm = transactionAssembler.toDM(transaction);
        TransactionDM saved = transactionRepositoryJpa.save(dm);
        return transactionAssembler.toDomain(saved);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Transaction> findById(TransactionID id) {
        Objects.requireNonNull(id, "TransactionID cannot be null");

        Optional<Transaction> transaction =
                transactionRepositoryJpa.findById(id)
                        .map(transactionAssembler::toDomain);

        return transaction;
    }

    /** {@inheritDoc} */
    @Override
    public boolean existsById(TransactionID id) {
        Objects.requireNonNull(id, "TransactionID cannot be null");

        return transactionRepositoryJpa.existsById(id);
    }

    /** {@inheritDoc} */
    @Override
    public List<Transaction> findAll() {
        List<Transaction> allTransactions =
                transactionRepositoryJpa.findAll()
                        .stream()
                        .map(transactionAssembler::toDomain)
                        .toList();

        return allTransactions;
    }

    /**
     * Returns all transactions associated with the given buyer.
     *
     * @param buyerId the identifier of the buyer; must not be {@code null}
     * @return a list of transactions owned by the given buyer;
     *         never {@code null}, may be empty
     */
    public List<Transaction> findAllByBuyerId(UserId buyerId) {
        Objects.requireNonNull(buyerId, "UserId cannot be null");

        List<Transaction> allTransactionsByBuyer =
                transactionRepositoryJpa.findAllByBuyerId(buyerId)
                        .stream()
                        .map(transactionAssembler::toDomain)
                        .toList();

        return allTransactionsByBuyer;
    }
}