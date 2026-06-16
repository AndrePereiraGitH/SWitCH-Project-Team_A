package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.Transaction.Transaction;
import TOPSECRET.domain.Transaction.TransactionFactory;
import TOPSECRET.persistence.repositoryjpa.datamodel.TransactionDM;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Assembler responsible for converting between {@link Transaction} domain objects
 * and {@link TransactionDM} persistence entities.
 *
 * <p>This class handles two conversion directions:</p>
 * <ul>
 *   <li><b>toDM</b> — domain object → data model, used during the save flow.</li>
 *   <li><b>toDomain</b> — data model → domain object, used during the reconstruction flow.</li>
 * </ul>
 *
 * <p>This assembler is the only component permitted to call the reconstruction
 * overload of {@link TransactionFactory#create}, which accepts a pre-existing
 * {@code TransactionID}. All other callers must use the creation overload.</p>
 */
@Service
public class TransactionAssembler {

    private final TransactionFactory _factory;

    /**
     * Constructs a {@code TransactionAssembler} with the required factory dependency.
     *
     * @param factory the factory used to reconstruct {@link Transaction} domain objects;
     *                must not be {@code null}
     */
    public TransactionAssembler(TransactionFactory factory) {
        _factory = Objects.requireNonNull(factory, "TransactionFactory cannot be null.");
    }

    /**
     * Converts a {@link Transaction} domain object into a {@link TransactionDM}
     * persistence entity.
     *
     * <p>Used during the save flow, before persisting to the database.
     * All fields are extracted from the domain object via its getters and
     * passed directly to the {@link TransactionDM} constructor.</p>
     *
     * @param transaction the domain object to convert; must not be {@code null}
     * @return a fully populated {@link TransactionDM} ready for persistence
     */
    public TransactionDM toDM(Transaction transaction) {

        Objects.requireNonNull(transaction, "Transaction cannot be null.");

        return new TransactionDM(
                transaction.id(),
                transaction.getBuyerId(),
                transaction.getPrice(),
                transaction.getTransactionDate(),
                transaction.getShippingAddress(),
                transaction.getPaymentDetails(),
                transaction.getStatus(),
                transaction.getListingIds()
        );
    }

    /**
     * Reconstructs a {@link Transaction} domain object from a {@link TransactionDM}
     * persistence entity.
     *
     * <p>Used during the reconstruction flow, after loading from the database.
     * Delegates to the reconstruction overload of {@link TransactionFactory#create},
     * supplying the persisted {@code TransactionID} to preserve the original identity.
     * No new identity is generated during this process.</p>
     *
     * <p>Domain invariants are still enforced — the factory delegates to the
     * {@link Transaction} constructor, which validates all arguments.</p>
     *
     * @param transactionDM the persistence entity to convert; must not be {@code null}
     * @return a fully reconstructed {@link Transaction} with its original identity restored
     */
    public Transaction toDomain(TransactionDM transactionDM) {

        Objects.requireNonNull(transactionDM, "TransactionDM cannot be null.");

        return _factory.create(
                transactionDM.getTransactionId(),
                transactionDM.getBuyerId(),
                transactionDM.getPrice(),
                transactionDM.getTransactionDate(),
                transactionDM.getShippingAddress(),
                transactionDM.getPaymentDetails(),
                transactionDM.getStatus(),
                transactionDM.getListingIds()
        );
    }
}