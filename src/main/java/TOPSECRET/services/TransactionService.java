package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.TransactionAuthorizationPolicy;
import TOPSECRET.domain.Enum.TransactionStatus;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.Transaction.ITransactionRepository;
import TOPSECRET.domain.Transaction.Transaction;
import TOPSECRET.domain.Transaction.TransactionFactory;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
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
    private final IUserRepository userRepository;
    private final TransactionAuthorizationPolicy policy;

    /**
     * Constructs a {@code TransactionService} with the required collaborators.
     *
     * @param transactionRepository the repository used to persist transactions; must not be {@code null}
     * @param transactionFactory    the factory used to create transaction instances; must not be {@code null}
     * @param userRepository        the repository used to retrieve users; must not be {@code null}
     * @param policy                the authorization policy for transaction operations; must not be {@code null}
     */
    public TransactionService(ITransactionRepository transactionRepository,
                              TransactionFactory transactionFactory,
                              IUserRepository userRepository,
                              TransactionAuthorizationPolicy policy) {
        this.transactionRepository = Objects.requireNonNull(transactionRepository, "transactionRepository must not be null");
        this.transactionFactory    = Objects.requireNonNull(transactionFactory,    "transactionFactory must not be null");
        this.userRepository        = Objects.requireNonNull(userRepository,        "userRepository must not be null");
        this.policy                = Objects.requireNonNull(policy,                "policy must not be null");
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
     * @throws EntityNotFoundException if no user exists with the given buyer identifier
     * @throws AccessDeniedException   if the user does not have permission to create a transaction
     */
    public Transaction createAndSave(UserId buyerId,
                                     Price price,
                                     LocalDateTime transactionDate,
                                     Address shippingAddress,
                                     PaymentDetails paymentDetails,
                                     List<ListingId> listingIds) {

        Objects.requireNonNull(buyerId,          "buyerId must not be null");
        Objects.requireNonNull(price,            "price must not be null");
        Objects.requireNonNull(transactionDate,  "transactionDate must not be null");
        Objects.requireNonNull(shippingAddress,  "shippingAddress must not be null");
        Objects.requireNonNull(paymentDetails,   "paymentDetails must not be null");
        Objects.requireNonNull(listingIds,       "listingIds must not be null");

        User user = userRepository.findByID(buyerId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + buyerId));

        if (!policy.canCreateTransaction(user)) {
            throw new AccessDeniedException("You do not have permission to create a transaction");
        }

        Transaction transaction = transactionFactory.create(
                buyerId,
                price,
                transactionDate,
                shippingAddress,
                paymentDetails,
                listingIds
        );

        return transactionRepository.save(transaction);
    }

    /**
     * Finds a transaction by its identifier, ensuring the requester has permission
     * to view it and is either the transaction buyer or an admin.
     *
     * @param transactionId the identifier of the transaction to find; must not be {@code null}
     * @param requesterId   the identifier of the user requesting access; must not be {@code null}
     * @return the transaction associated with the given identifier
     * @throws EntityNotFoundException if no user exists with the given requester identifier,
     *                                 or no transaction exists with the given transaction identifier
     * @throws AccessDeniedException   if the requester does not have permission to view transactions,
     *                                 or is neither the buyer nor an admin
     */
    public Transaction findById(TransactionID transactionId, UserId requesterId) {

        User requester = userRepository.findByID(requesterId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + requesterId));

        if (!policy.canViewTransaction(requester)) {
            throw new AccessDeniedException("You do not have permission to view transactions");
        }

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found: " + transactionId.getId()));

        boolean isAdmin = requester.getUserRoles().contains(UserRole.ADMIN);
        boolean isBuyer = transaction.getBuyerId().equals(requesterId);

        if (!isAdmin && !isBuyer) {
            throw new AccessDeniedException("You do not have permission to access this transaction");
        }

        return transaction;
    }

    /**
     * Retrieves all transactions in the system.
     *
     * <p>Only users with the {@link UserRole#ADMIN} role are permitted
     * to retrieve the full list of transactions.</p>
     *
     * @param requesterId the identifier of the user requesting access; must not be {@code null}
     * @return a list containing all existing transactions; may be empty if none exist
     * @throws EntityNotFoundException if no user exists with the given requester identifier
     * @throws AccessDeniedException   if the requester does not have the admin role
     */
    public List<Transaction> findAll(UserId requesterId) {

        Objects.requireNonNull(requesterId, "requesterId must not be null");

        User requester = userRepository.findByID(requesterId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + requesterId));

        if (!policy.canViewAllTransactions(requester)) {
            throw new AccessDeniedException("Only admins can view all transactions");
        }

        return transactionRepository.findAll();
    }

    /**
     * Retrieves all transactions associated with the given buyer,
     * ensuring the requester has permission and is either the buyer or an admin.
     *
     * @param buyerId     the identifier of the buyer; must not be {@code null}
     * @param requesterId the identifier of the user requesting access; must not be {@code null}
     * @return a list of transactions owned by the given buyer; may be empty if none exist
     * @throws EntityNotFoundException if no user exists with the given requester identifier
     * @throws AccessDeniedException   if the requester does not have permission to view transactions,
     *                                 or is neither the buyer nor an admin
     */
    public List<Transaction> findAllByBuyerId(UserId buyerId, UserId requesterId) {

        Objects.requireNonNull(buyerId,     "buyerId must not be null");
        Objects.requireNonNull(requesterId, "requesterId must not be null");

        User requester = userRepository.findByID(requesterId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + requesterId));

        if (!policy.canViewTransaction(requester)) {
            throw new AccessDeniedException("You do not have permission to view transactions");
        }

        boolean isAdmin = requester.getUserRoles().contains(UserRole.ADMIN);
        boolean isBuyer = buyerId.equals(requesterId);

        if (!isAdmin && !isBuyer) {
            throw new AccessDeniedException("You do not have permission to access these transactions");
        }

        return transactionRepository.findAllByBuyerId(buyerId);
    }
}