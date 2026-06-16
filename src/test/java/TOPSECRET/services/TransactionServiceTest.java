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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link TransactionService}.
 *
 * <p>Verifies the service's behaviour in isolation by mocking the repository,
 * factory, user repository, and authorization policy. Tests are grouped by
 * method under test.</p>
 *
 * <p>Each test follows the Arrange / Act / Assert structure and exercises
 * a single logical scenario.</p>
 */
@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private ITransactionRepository transactionRepository;

    @Mock
    private TransactionFactory transactionFactory;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private TransactionAuthorizationPolicy policy;

    @InjectMocks
    private TransactionService sut;

    private UserId buyerIdDouble;
    private UserId requesterIdSameAsBuyer;
    private UserId requesterIdDifferentFromBuyer;
    private Price priceDouble;
    private LocalDateTime transactionDate;
    private Address shippingAddressDouble;
    private PaymentDetails paymentDetailsDouble;
    private List<ListingId> listingIds;
    private TransactionID transactionIdDouble;
    private User userDouble;

    @BeforeEach
    void setUp() {
        buyerIdDouble                 = new UserId("buyer-1");
        requesterIdSameAsBuyer        = new UserId("buyer-1");
        requesterIdDifferentFromBuyer = new UserId("other-user");
        priceDouble                   = mock(Price.class);
        transactionDate               = LocalDateTime.now().minusMinutes(1);
        shippingAddressDouble         = mock(Address.class);
        paymentDetailsDouble          = mock(PaymentDetails.class);
        listingIds                    = List.of(mock(ListingId.class));
        transactionIdDouble           = mock(TransactionID.class);
        userDouble                    = mock(User.class);
    }

    // =========================================================================
    // Constructor
    // =========================================================================

    @Test
    void constructor_shouldThrowNullPointerException_whenRepositoryIsNull() {

        // ACT + ASSERT
        assertThrows(NullPointerException.class,
                () -> new TransactionService(null, transactionFactory, userRepository, policy));
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenFactoryIsNull() {

        // ACT + ASSERT
        assertThrows(NullPointerException.class,
                () -> new TransactionService(transactionRepository, null, userRepository, policy));
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenUserRepositoryIsNull() {

        // ACT + ASSERT
        assertThrows(NullPointerException.class,
                () -> new TransactionService(transactionRepository, transactionFactory, null, policy));
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenPolicyIsNull() {

        // ACT + ASSERT
        assertThrows(NullPointerException.class,
                () -> new TransactionService(transactionRepository, transactionFactory, userRepository, null));
    }

    // =========================================================================
    // createAndSave
    // =========================================================================

    @Test
    void createAndSave_shouldReturnSavedTransaction_whenUserIsAuthorized() {

        // ARRANGE
        Transaction createdTransaction = mock(Transaction.class);
        Transaction savedTransaction   = mock(Transaction.class);

        when(userRepository.findByID(buyerIdDouble)).thenReturn(Optional.of(userDouble));
        when(policy.canCreateTransaction(userDouble)).thenReturn(true);
        when(transactionFactory.create(
                buyerIdDouble, priceDouble, transactionDate,
                shippingAddressDouble, paymentDetailsDouble,
                TransactionStatus.COMPLETED, listingIds
        )).thenReturn(createdTransaction);
        when(transactionRepository.save(createdTransaction)).thenReturn(savedTransaction);

        // ACT
        Transaction result = sut.createAndSave(
                buyerIdDouble, priceDouble, transactionDate,
                shippingAddressDouble, paymentDetailsDouble, listingIds);

        // ASSERT
        assertSame(savedTransaction, result);
    }

    @Test
    void createAndSave_shouldThrowEntityNotFoundException_whenUserDoesNotExist() {

        // ARRANGE
        when(userRepository.findByID(buyerIdDouble)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(EntityNotFoundException.class,
                () -> sut.createAndSave(
                        buyerIdDouble, priceDouble, transactionDate,
                        shippingAddressDouble, paymentDetailsDouble, listingIds));
    }

    @Test
    void createAndSave_shouldThrowAccessDeniedException_whenUserIsNotAuthorized() {

        // ARRANGE
        when(userRepository.findByID(buyerIdDouble)).thenReturn(Optional.of(userDouble));
        when(policy.canCreateTransaction(userDouble)).thenReturn(false);

        // ACT + ASSERT
        assertThrows(AccessDeniedException.class,
                () -> sut.createAndSave(
                        buyerIdDouble, priceDouble, transactionDate,
                        shippingAddressDouble, paymentDetailsDouble, listingIds));
    }

    @Test
    void createAndSave_shouldThrowNullPointerException_whenBuyerIdIsNull() {

        // ACT + ASSERT
        assertThrows(NullPointerException.class,
                () -> sut.createAndSave(null, priceDouble, transactionDate,
                        shippingAddressDouble, paymentDetailsDouble, listingIds));
    }

    @Test
    void createAndSave_shouldThrowNullPointerException_whenPriceIsNull() {

        // ACT + ASSERT
        assertThrows(NullPointerException.class,
                () -> sut.createAndSave(buyerIdDouble, null, transactionDate,
                        shippingAddressDouble, paymentDetailsDouble, listingIds));
    }

    @Test
    void createAndSave_shouldThrowNullPointerException_whenTransactionDateIsNull() {

        // ACT + ASSERT
        assertThrows(NullPointerException.class,
                () -> sut.createAndSave(buyerIdDouble, priceDouble, null,
                        shippingAddressDouble, paymentDetailsDouble, listingIds));
    }

    @Test
    void createAndSave_shouldThrowNullPointerException_whenShippingAddressIsNull() {

        // ACT + ASSERT
        assertThrows(NullPointerException.class,
                () -> sut.createAndSave(buyerIdDouble, priceDouble, transactionDate,
                        null, paymentDetailsDouble, listingIds));
    }

    @Test
    void createAndSave_shouldThrowNullPointerException_whenPaymentDetailsIsNull() {

        // ACT + ASSERT
        assertThrows(NullPointerException.class,
                () -> sut.createAndSave(buyerIdDouble, priceDouble, transactionDate,
                        shippingAddressDouble, null, listingIds));
    }

    @Test
    void createAndSave_shouldThrowNullPointerException_whenListingIdsIsNull() {

        // ACT + ASSERT
        assertThrows(NullPointerException.class,
                () -> sut.createAndSave(buyerIdDouble, priceDouble, transactionDate,
                        shippingAddressDouble, paymentDetailsDouble, null));
    }

    // =========================================================================
    // findById
    // =========================================================================

    @Test
    void findById_shouldReturnTransaction_whenRequesterIsOwner() {

        // ARRANGE
        Transaction transaction = mock(Transaction.class);

        when(userRepository.findByID(requesterIdSameAsBuyer)).thenReturn(Optional.of(userDouble));
        when(policy.canViewTransaction(userDouble)).thenReturn(true);
        when(transactionRepository.findById(transactionIdDouble)).thenReturn(Optional.of(transaction));
        when(transaction.getBuyerId()).thenReturn(buyerIdDouble);
        when(userDouble.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // ACT
        Transaction result = sut.findById(transactionIdDouble, requesterIdSameAsBuyer);

        // ASSERT
        assertSame(transaction, result);
    }

    @Test
    void findById_shouldReturnTransaction_whenRequesterIsAdmin() {

        // ARRANGE
        Transaction transaction = mock(Transaction.class);

        when(userRepository.findByID(requesterIdDifferentFromBuyer)).thenReturn(Optional.of(userDouble));
        when(policy.canViewTransaction(userDouble)).thenReturn(true);
        when(transactionRepository.findById(transactionIdDouble)).thenReturn(Optional.of(transaction));
        when(transaction.getBuyerId()).thenReturn(buyerIdDouble);
        when(userDouble.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));

        // ACT
        Transaction result = sut.findById(transactionIdDouble, requesterIdDifferentFromBuyer);

        // ASSERT
        assertSame(transaction, result);
    }

    @Test
    void findById_shouldThrowEntityNotFoundException_whenRequesterDoesNotExist() {

        // ARRANGE
        when(userRepository.findByID(requesterIdSameAsBuyer)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(EntityNotFoundException.class,
                () -> sut.findById(transactionIdDouble, requesterIdSameAsBuyer));
    }

    @Test
    void findById_shouldThrowAccessDeniedException_whenRequesterHasNoPermission() {

        // ARRANGE
        when(userRepository.findByID(requesterIdSameAsBuyer)).thenReturn(Optional.of(userDouble));
        when(policy.canViewTransaction(userDouble)).thenReturn(false);

        // ACT + ASSERT
        assertThrows(AccessDeniedException.class,
                () -> sut.findById(transactionIdDouble, requesterIdSameAsBuyer));
    }

    @Test
    void findById_shouldThrowEntityNotFoundException_whenTransactionDoesNotExist() {

        // ARRANGE
        when(userRepository.findByID(requesterIdSameAsBuyer)).thenReturn(Optional.of(userDouble));
        when(policy.canViewTransaction(userDouble)).thenReturn(true);
        when(transactionRepository.findById(transactionIdDouble)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(EntityNotFoundException.class,
                () -> sut.findById(transactionIdDouble, requesterIdSameAsBuyer));
    }

    @Test
    void findById_shouldThrowAccessDeniedException_whenRequesterIsNotOwnerNorAdmin() {

        // ARRANGE
        Transaction transaction = mock(Transaction.class);

        when(userRepository.findByID(requesterIdDifferentFromBuyer)).thenReturn(Optional.of(userDouble));
        when(policy.canViewTransaction(userDouble)).thenReturn(true);
        when(transactionRepository.findById(transactionIdDouble)).thenReturn(Optional.of(transaction));
        when(transaction.getBuyerId()).thenReturn(buyerIdDouble);
        when(userDouble.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // ACT + ASSERT
        assertThrows(AccessDeniedException.class,
                () -> sut.findById(transactionIdDouble, requesterIdDifferentFromBuyer));
    }

    // =========================================================================
    // findAll
    // =========================================================================

    @Test
    void findAll_shouldReturnAllTransactions_whenRequesterIsAdmin() {

        // ARRANGE
        Transaction tx1 = mock(Transaction.class);
        Transaction tx2 = mock(Transaction.class);
        List<Transaction> transactions = List.of(tx1, tx2);

        UserId adminId = new UserId("admin-1");
        when(userRepository.findByID(adminId)).thenReturn(Optional.of(userDouble));
        when(policy.canViewAllTransactions(userDouble)).thenReturn(true);
        when(transactionRepository.findAll()).thenReturn(transactions);

        // ACT
        List<Transaction> result = sut.findAll(adminId);

        // ASSERT
        assertSame(transactions, result);
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoTransactionsExist() {

        // ARRANGE
        UserId adminId = new UserId("admin-1");
        when(userRepository.findByID(adminId)).thenReturn(Optional.of(userDouble));
        when(policy.canViewAllTransactions(userDouble)).thenReturn(true);
        when(transactionRepository.findAll()).thenReturn(List.of());

        // ACT
        List<Transaction> result = sut.findAll(adminId);

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_shouldThrowAccessDeniedException_whenRequesterIsNotAdmin() {

        // ARRANGE
        UserId normalUserId = new UserId("user-1");
        when(userRepository.findByID(normalUserId)).thenReturn(Optional.of(userDouble));
        when(policy.canViewAllTransactions(userDouble)).thenReturn(false);

        // ACT + ASSERT
        assertThrows(AccessDeniedException.class,
                () -> sut.findAll(normalUserId));
    }

    @Test
    void findAll_shouldThrowEntityNotFoundException_whenRequesterDoesNotExist() {

        // ARRANGE
        UserId unknownId = new UserId("unknown");
        when(userRepository.findByID(unknownId)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(EntityNotFoundException.class,
                () -> sut.findAll(unknownId));
    }

    @Test
    void findAll_shouldThrowNullPointerException_whenRequesterIdIsNull() {

        // ACT + ASSERT
        assertThrows(NullPointerException.class,
                () -> sut.findAll(null));
    }

    // =========================================================================
    // findAllByBuyerId
    // =========================================================================

    @Test
    void findAllByBuyerId_shouldReturnTransactions_whenRequesterMatchesBuyer() {

        // ARRANGE
        Transaction tx1 = mock(Transaction.class);
        Transaction tx2 = mock(Transaction.class);
        List<Transaction> transactions = List.of(tx1, tx2);

        when(userRepository.findByID(requesterIdSameAsBuyer)).thenReturn(Optional.of(userDouble));
        when(policy.canViewTransaction(userDouble)).thenReturn(true);
        when(userDouble.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));
        when(transactionRepository.findAllByBuyerId(buyerIdDouble)).thenReturn(transactions);

        // ACT
        List<Transaction> result = sut.findAllByBuyerId(buyerIdDouble, requesterIdSameAsBuyer);

        // ASSERT
        assertSame(transactions, result);
    }

    @Test
    void findAllByBuyerId_shouldReturnTransactions_whenRequesterIsAdmin() {

        // ARRANGE
        Transaction tx1 = mock(Transaction.class);
        List<Transaction> transactions = List.of(tx1);

        when(userRepository.findByID(requesterIdDifferentFromBuyer)).thenReturn(Optional.of(userDouble));
        when(policy.canViewTransaction(userDouble)).thenReturn(true);
        when(userDouble.getUserRoles()).thenReturn(Set.of(UserRole.ADMIN));
        when(transactionRepository.findAllByBuyerId(buyerIdDouble)).thenReturn(transactions);

        // ACT
        List<Transaction> result = sut.findAllByBuyerId(buyerIdDouble, requesterIdDifferentFromBuyer);

        // ASSERT
        assertSame(transactions, result);
    }

    @Test
    void findAllByBuyerId_shouldReturnEmptyList_whenBuyerHasNoTransactions() {

        // ARRANGE
        when(userRepository.findByID(requesterIdSameAsBuyer)).thenReturn(Optional.of(userDouble));
        when(policy.canViewTransaction(userDouble)).thenReturn(true);
        when(userDouble.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));
        when(transactionRepository.findAllByBuyerId(buyerIdDouble)).thenReturn(List.of());

        // ACT
        List<Transaction> result = sut.findAllByBuyerId(buyerIdDouble, requesterIdSameAsBuyer);

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllByBuyerId_shouldThrowEntityNotFoundException_whenRequesterDoesNotExist() {

        // ARRANGE
        when(userRepository.findByID(requesterIdSameAsBuyer)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(EntityNotFoundException.class,
                () -> sut.findAllByBuyerId(buyerIdDouble, requesterIdSameAsBuyer));
    }

    @Test
    void findAllByBuyerId_shouldThrowAccessDeniedException_whenRequesterHasNoPermission() {

        // ARRANGE
        when(userRepository.findByID(requesterIdSameAsBuyer)).thenReturn(Optional.of(userDouble));
        when(policy.canViewTransaction(userDouble)).thenReturn(false);

        // ACT + ASSERT
        assertThrows(AccessDeniedException.class,
                () -> sut.findAllByBuyerId(buyerIdDouble, requesterIdSameAsBuyer));
    }

    @Test
    void findAllByBuyerId_shouldThrowAccessDeniedException_whenRequesterIsNotOwnerNorAdmin() {

        // ARRANGE
        when(userRepository.findByID(requesterIdDifferentFromBuyer)).thenReturn(Optional.of(userDouble));
        when(policy.canViewTransaction(userDouble)).thenReturn(true);
        when(userDouble.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        // ACT + ASSERT
        assertThrows(AccessDeniedException.class,
                () -> sut.findAllByBuyerId(buyerIdDouble, requesterIdDifferentFromBuyer));
    }

    @Test
    void findAllByBuyerId_shouldThrowNullPointerException_whenBuyerIdIsNull() {

        // ACT + ASSERT
        assertThrows(NullPointerException.class,
                () -> sut.findAllByBuyerId(null, requesterIdSameAsBuyer));
    }

    @Test
    void findAllByBuyerId_shouldThrowNullPointerException_whenRequesterIdIsNull() {

        // ACT + ASSERT
        assertThrows(NullPointerException.class,
                () -> sut.findAllByBuyerId(buyerIdDouble, null));
    }
}