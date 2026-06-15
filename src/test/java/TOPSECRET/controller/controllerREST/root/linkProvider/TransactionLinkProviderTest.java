package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link TransactionLinkProvider}.
 *
 * <p>Verifies that the correct HATEOAS links are returned for each context:
 * public links, buyer-specific links, and the combined result of {@code getLinks}.</p>
 */
@ExtendWith(MockitoExtension.class)
class TransactionLinkProviderTest {

    @InjectMocks
    private TransactionLinkProvider sut;

    // =========================================================================
    // getPublicLinks
    // =========================================================================

    @Test
    void getPublicLinks_shouldReturnNonEmptyList() {

        // Act
        List<Link> links = sut.getPublicLinks();

        // Assert
        assertFalse(links.isEmpty());
    }

    @Test
    void getPublicLinks_shouldContainCreateTransactionLink() {

        // Act
        List<Link> links = sut.getPublicLinks();

        // Assert
        assertTrue(links.stream()
                .anyMatch(link -> link.getRel().value().equals("create-transaction")));
    }

    @Test
    void getPublicLinks_shouldContainFindTransactionByIdLink() {

        // Act
        List<Link> links = sut.getPublicLinks();

        // Assert
        assertTrue(links.stream()
                .anyMatch(link -> link.getRel().value().equals("find-transaction-by-id")));
    }

    // =========================================================================
    // getBuyerLinks
    // =========================================================================

    @Test
    void getBuyerLinks_shouldReturnNonEmptyList() {

        // Arrange
        UserId userId = mock(UserId.class);
        when(userId.getUserId()).thenReturn("buyer-1");

        // Act
        List<Link> links = sut.getBuyerLinks(userId);

        // Assert
        assertFalse(links.isEmpty());
    }

    @Test
    void getBuyerLinks_shouldContainMyTransactionsLink() {

        // Arrange
        UserId userId = mock(UserId.class);
        when(userId.getUserId()).thenReturn("buyer-1");

        // Act
        List<Link> links = sut.getBuyerLinks(userId);

        // Assert
        assertTrue(links.stream()
                .anyMatch(link -> link.getRel().value().equals("my-transactions")));
    }

    @Test
    void getBuyerLinks_shouldThrowNullPointerException_whenUserIdIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> sut.getBuyerLinks(null));
    }

    // =========================================================================
    // getLinks
    // =========================================================================

    @Test
    void getLinks_shouldReturnNonEmptyList() throws Exception {

        // Arrange
        UserId userId = mock(UserId.class);
        when(userId.getUserId()).thenReturn("buyer-1");

        // Act
        List<Link> links = sut.getLinks(userId);

        // Assert
        assertFalse(links.isEmpty());
    }

    @Test
    void getLinks_shouldContainCreateTransactionLink() throws Exception {

        // Arrange
        UserId userId = mock(UserId.class);
        when(userId.getUserId()).thenReturn("buyer-1");

        // Act
        List<Link> links = sut.getLinks(userId);

        // Assert
        assertTrue(links.stream()
                .anyMatch(link -> link.getRel().value().equals("create-transaction")));
    }

    @Test
    void getLinks_shouldContainMyTransactionsLink() throws Exception {

        // Arrange
        UserId userId = mock(UserId.class);
        when(userId.getUserId()).thenReturn("buyer-1");

        // Act
        List<Link> links = sut.getLinks(userId);

        // Assert
        assertTrue(links.stream()
                .anyMatch(link -> link.getRel().value().equals("my-transactions")));
    }
}