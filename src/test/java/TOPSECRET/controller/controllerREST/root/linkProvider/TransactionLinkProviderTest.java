package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.TransactionAuthorizationPolicy;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link TransactionLinkProvider}.
 *
 * <p>Verifies that the correct HATEOAS links are returned based on
 * the user's role and authorization policy.</p>
 */
@ExtendWith(MockitoExtension.class)
class TransactionLinkProviderTest {

    @Mock
    private IUserRepository repository;

    @Mock
    private TransactionAuthorizationPolicy policy;

    @InjectMocks
    private TransactionLinkProvider sut;

    // =========================================================================
    // getLinks
    // =========================================================================

    @Test
    void getLinks_shouldContainCreateTransactionLink_whenUserCanCreateTransaction() throws InstanceNotFoundException {

        // ARRANGE
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateTransaction(user)).thenReturn(true);
        when(policy.canViewTransaction(user)).thenReturn(false);

        // ACT
        List<Link> links = sut.getLinks(userId);

        // ASSERT
        assertTrue(links.stream()
                .anyMatch(link -> link.getRel().value().equals("create-transaction")));
    }

    @Test
    void getLinks_shouldNotContainCreateTransactionLink_whenUserCannotCreateTransaction() throws InstanceNotFoundException {

        // ARRANGE
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateTransaction(user)).thenReturn(false);
        when(policy.canViewTransaction(user)).thenReturn(false);

        // ACT
        List<Link> links = sut.getLinks(userId);

        // ASSERT
        assertTrue(links.stream()
                .noneMatch(link -> link.getRel().value().equals("create-transaction")));
    }

    @Test
    void getLinks_shouldContainFindTransactionByIdLink_whenUserCanViewTransaction() throws InstanceNotFoundException {

        // ARRANGE
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateTransaction(user)).thenReturn(false);
        when(policy.canViewTransaction(user)).thenReturn(true);
        when(userId.getUserId()).thenReturn("user-1");

        // ACT
        List<Link> links = sut.getLinks(userId);

        // ASSERT
        assertTrue(links.stream()
                .anyMatch(link -> link.getRel().value().equals("find-transaction-by-id")));
    }

    @Test
    void getLinks_shouldContainMyTransactionsLink_whenUserCanViewTransaction() throws InstanceNotFoundException {

        // ARRANGE
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateTransaction(user)).thenReturn(false);
        when(policy.canViewTransaction(user)).thenReturn(true);
        when(userId.getUserId()).thenReturn("user-1");

        // ACT
        List<Link> links = sut.getLinks(userId);

        // ASSERT
        assertTrue(links.stream()
                .anyMatch(link -> link.getRel().value().equals("my-transactions")));
    }

    @Test
    void getLinks_shouldNotContainViewLinks_whenUserCannotViewTransaction() throws InstanceNotFoundException {

        // ARRANGE
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateTransaction(user)).thenReturn(false);
        when(policy.canViewTransaction(user)).thenReturn(false);

        // ACT
        List<Link> links = sut.getLinks(userId);

        // ASSERT
        assertTrue(links.stream()
                .noneMatch(link -> link.getRel().value().equals("find-transaction-by-id")));
        assertTrue(links.stream()
                .noneMatch(link -> link.getRel().value().equals("my-transactions")));
    }

    @Test
    void getLinks_shouldThrowInstanceNotFoundException_whenUserNotFound() {

        // ARRANGE
        UserId userId = mock(UserId.class);
        when(repository.findByID(userId)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(InstanceNotFoundException.class,
                () -> sut.getLinks(userId));
    }

    @Test
    void getLinks_shouldThrowNullPointerException_whenUserIdIsNull() {

        // ACT + ASSERT
        assertThrows(NullPointerException.class,
                () -> sut.getLinks(null));
    }
}