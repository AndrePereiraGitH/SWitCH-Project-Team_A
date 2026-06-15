package TOPSECRET.services;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.TransactionAuthorizationPolicy;
import TOPSECRET.domain.Enum.TransactionStatus;
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

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> new TransactionService(null, transactionFactory, userRepository, policy));
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenFactoryIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> new TransactionService(transactionRepository, null, userRepository, policy));
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenUserRepositoryIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> new TransactionService(transactionRepository, transactionFactory, null, policy));
    }

    @Test
    void constructor_shouldThrowNullPointerException_whenPolicyIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> new TransactionService(transactionRepository, transactionFactory, userRepository, null));
    }

    // =========================================================================
    // createAndSave
    // =========================================================================

    @Test
    void createAndSave_shouldReturnSavedTransaction_whenUserIsAuthorized() {

        // Arrange
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

        // Act
        Transaction result = sut.createAndSave(
                buyerIdDouble, priceDouble, transactionDate,
                shippingAddressDouble, paymentDetailsDouble, listingIds);

        // Assert
        assertSame(savedTransaction, result);
    }

    @Test
    void createAndSave_shouldThrowEntityNotFoundException_whenUserDoesNotExist() {

        // Arrange
        when(userRepository.findByID(buyerIdDouble)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(EntityNotFoundException.class,
                () -> sut.createAndSave(
                        buyerIdDouble, priceDouble, transactionDate,
                        shippingAddressDouble, paymentDetailsDouble, listingIds));
    }

    @Test
    void createAndSave_shouldThrowAccessDeniedException_whenUserIsNotAuthorized() {

        // Arrange
        when(userRepository.findByID(buyerIdDouble)).thenReturn(Optional.of(userDouble));
        when(policy.canCreateTransaction(userDouble)).thenReturn(false);

        // Act + Assert
        assertThrows(AccessDeniedException.class,
                () -> sut.createAndSave(
                        buyerIdDouble, priceDouble, transactionDate,
                        shippingAddressDouble, paymentDetailsDouble, listingIds));
    }

    // =========================================================================
    // findById
    // =========================================================================

    @Test
    void findById_shouldReturnTransaction_whenRequesterIsOwner() {

        // Arrange
        Transaction transaction = mock(Transaction.class);

        when(userRepository.findByID(requesterIdSameAsBuyer)).thenReturn(Optional.of(userDouble));
        when(policy.canViewTransaction(userDouble)).thenReturn(true);
        when(transactionRepository.findById(transactionIdDouble))
                .thenReturn(Optional.of(transaction));
        when(transaction.getBuyerId()).thenReturn(buyerIdDouble);

        // Act
        Transaction result = sut.findById(transactionIdDouble, requesterIdSameAsBuyer);

        // Assert
        assertSame(transaction, result);
    }

    @Test
    void findById_shouldThrowEntityNotFoundException_whenRequesterDoesNotExist() {

        // Arrange
        when(userRepository.findByID(requesterIdSameAsBuyer)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(EntityNotFoundException.class,
                () -> sut.findById(transactionIdDouble, requesterIdSameAsBuyer));
    }

    @Test
    void findById_shouldThrowAccessDeniedException_whenRequesterHasNoPermission() {

        // Arrange
        when(userRepository.findByID(requesterIdSameAsBuyer)).thenReturn(Optional.of(userDouble));
        when(policy.canViewTransaction(userDouble)).thenReturn(false);

        // Act + Assert
        assertThrows(AccessDeniedException.class,
                () -> sut.findById(transactionIdDouble, requesterIdSameAsBuyer));
    }

    @Test
    void findById_shouldThrowEntityNotFoundException_whenTransactionDoesNotExist() {

        // Arrange
        when(userRepository.findByID(requesterIdSameAsBuyer)).thenReturn(Optional.of(userDouble));
        when(policy.canViewTransaction(userDouble)).thenReturn(true);
        when(transactionRepository.findById(transactionIdDouble)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(EntityNotFoundException.class,
                () -> sut.findById(transactionIdDouble, requesterIdSameAsBuyer));
    }

    @Test
    void findById_shouldThrowAccessDeniedException_whenRequesterIsNotOwner() {

        // Arrange
        Transaction transaction = mock(Transaction.class);

        when(userRepository.findByID(requesterIdDifferentFromBuyer)).thenReturn(Optional.of(userDouble));
        when(policy.canViewTransaction(userDouble)).thenReturn(true);
        when(transactionRepository.findById(transactionIdDouble))
                .thenReturn(Optional.of(transaction));
        when(transaction.getBuyerId()).thenReturn(buyerIdDouble);

        // Act + Assert
        assertThrows(AccessDeniedException.class,
                () -> sut.findById(transactionIdDouble, requesterIdDifferentFromBuyer));
    }

    // =========================================================================
    // findAll
    // =========================================================================

    @Test
    void findAll_shouldReturnAllTransactions() {

        // Arrange
        Transaction tx1 = mock(Transaction.class);
        Transaction tx2 = mock(Transaction.class);
        List<Transaction> transactions = List.of(tx1, tx2);

        when(transactionRepository.findAll()).thenReturn(transactions);

        // Act
        List<Transaction> result = sut.findAll();

        // Assert
        assertSame(transactions, result);
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoTransactionsExist() {

        // Arrange
        when(transactionRepository.findAll()).thenReturn(List.of());

        // Act
        List<Transaction> result = sut.findAll();

        // Assert
        assertTrue(result.isEmpty());
    }

    // =========================================================================
    // findAllByBuyerId
    // =========================================================================

    @Test
    void findAllByBuyerId_shouldReturnTransactions_whenRequesterMatchesBuyer() {

        // Arrange
        Transaction tx1 = mock(Transaction.class);
        Transaction tx2 = mock(Transaction.class);
        List<Transaction> transactions = List.of(tx1, tx2);

        when(userRepository.findByID(requesterIdSameAsBuyer)).thenReturn(Optional.of(userDouble));
        when(policy.canViewBuyerTransactions(userDouble)).thenReturn(true);
        when(transactionRepository.findAllByBuyerId(buyerIdDouble)).thenReturn(transactions);

        // Act
        List<Transaction> result = sut.findAllByBuyerId(buyerIdDouble, requesterIdSameAsBuyer);

        // Assert
        assertSame(transactions, result);
    }

    @Test
    void findAllByBuyerId_shouldReturnEmptyList_whenBuyerHasNoTransactions() {

        // Arrange
        when(userRepository.findByID(requesterIdSameAsBuyer)).thenReturn(Optional.of(userDouble));
        when(policy.canViewBuyerTransactions(userDouble)).thenReturn(true);
        when(transactionRepository.findAllByBuyerId(buyerIdDouble)).thenReturn(List.of());

        // Act
        List<Transaction> result = sut.findAllByBuyerId(buyerIdDouble, requesterIdSameAsBuyer);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllByBuyerId_shouldThrowEntityNotFoundException_whenRequesterDoesNotExist() {

        // Arrange
        when(userRepository.findByID(requesterIdSameAsBuyer)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(EntityNotFoundException.class,
                () -> sut.findAllByBuyerId(buyerIdDouble, requesterIdSameAsBuyer));
    }

    @Test
    void findAllByBuyerId_shouldThrowAccessDeniedException_whenRequesterHasNoPermission() {

        // Arrange
        when(userRepository.findByID(requesterIdSameAsBuyer)).thenReturn(Optional.of(userDouble));
        when(policy.canViewBuyerTransactions(userDouble)).thenReturn(false);

        // Act + Assert
        assertThrows(AccessDeniedException.class,
                () -> sut.findAllByBuyerId(buyerIdDouble, requesterIdSameAsBuyer));
    }

    @Test
    void findAllByBuyerId_shouldThrowAccessDeniedException_whenRequesterDoesNotMatchBuyer() {

        // Arrange
        when(userRepository.findByID(requesterIdDifferentFromBuyer)).thenReturn(Optional.of(userDouble));
        when(policy.canViewBuyerTransactions(userDouble)).thenReturn(true);

        // Act + Assert
        assertThrows(AccessDeniedException.class,
                () -> sut.findAllByBuyerId(buyerIdDouble, requesterIdDifferentFromBuyer));
    }

    @Test
    void findAllByBuyerId_shouldThrowNullPointerException_whenBuyerIdIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> sut.findAllByBuyerId(null, requesterIdSameAsBuyer));
    }

    @Test
    void findAllByBuyerId_shouldThrowNullPointerException_whenRequesterIdIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> sut.findAllByBuyerId(buyerIdDouble, null));
    }
}