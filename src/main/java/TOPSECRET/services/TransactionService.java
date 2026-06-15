package TOPSECRET.services;

import TOPSECRET.domain.Enum.TransactionStatus;
import TOPSECRET.domain.Transaction.ITransactionRepository;
import TOPSECRET.domain.Transaction.Transaction;
import TOPSECRET.domain.Transaction.TransactionFactory;
import TOPSECRET.domain.ValueObjects.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Application service responsible for {@link Transaction} use cases.
 *
 * <p>Orchestrates interactions between the domain layer ({@link TransactionFactory})
 * and the persistence layer ({@link ITransactionRepository}). All public methods
 * validate their inputs before delegating to domain or repository operations.</p>
 */
@Service
public class TransactionService {

    private final ITransactionRepository transactionRepository;
    private final TransactionFactory transactionFactory;

    /**
     * Constructs a {@code TransactionService} with the required collaborators.
     *
     * @param transactionRepository the repository used to persist transactions; must not be {@code null}
     * @param transactionFactory    the factory used to create transaction instances; must not be {@code null}
     */
    public TransactionService(ITransactionRepository transactionRepository,
                              TransactionFactory transactionFactory) {
        this.transactionRepository = Objects.requireNonNull(transactionRepository, "transactionRepository must not be null");
        this.transactionFactory    = Objects.requireNonNull(transactionFactory,    "transactionFactory must not be null");
    }

    /**
     * Creates a new {@link Transaction} from the supplied parameters and persists it.
     *
     * @param buyerId         the identifier of the buyer; must not be {@code null}
     * @param price           the total transaction price; must not be {@code null}
     * @param transactionDate the date and time of the transaction; must not be {@code null}
     * @param shippingAddress the shipping address; must not be {@code null}
     * @param paymentDetails  the payment details; must not be {@code null}
     * @param listingIds      the identifiers of the purchased listings; must not be {@code null}
     * @return the persisted {@link Transaction}
     */
    public Transaction createAndSave(UserId buyerId,
                                     Price price,
                                     LocalDateTime transactionDate,
                                     Address shippingAddress,
                                     PaymentDetails paymentDetails,
                                     List<ListingId> listingIds) {

        Transaction transaction = transactionFactory.create(
                buyerId,
                price,
                transactionDate,
                shippingAddress,
                paymentDetails,
                TransactionStatus.COMPLETED,
                listingIds
        );

        return transactionRepository.save(transaction);
    }

    /**
     * Finds a transaction by its identifier.
     *
     * @param transactionId the identifier of the transaction to find; must not be {@code null}
     * @return the transaction associated with the given identifier
     * @throws EntityNotFoundException if no transaction exists with the given identifier
     */
    public Transaction findById(TransactionID transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found: " + transactionId.getId()));
    }

    /**
     * Retrieves all transactions.
     *
     * @return a list containing all existing transactions; may be empty if none exist
     */
    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    /**
     * Retrieves all transactions associated with the given buyer.
     *
     * @param buyerId the identifier of the buyer; must not be {@code null}
     * @return a list of transactions owned by the given buyer; may be empty if none exist
     */
    public List<Transaction> findAllByBuyerId(UserId buyerId) {
        Objects.requireNonNull(buyerId, "buyerId must not be null");
        return transactionRepository.findAllByBuyerId(buyerId);
    }
}