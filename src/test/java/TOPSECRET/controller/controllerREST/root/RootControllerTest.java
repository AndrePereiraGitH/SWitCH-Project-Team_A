package TOPSECRET.controller.controllerREST.root;

import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.management.InstanceNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link RootController}.
 *
 * <p>Verifies the behaviour of the API root entry point across three scenarios:</p>
 * <ul>
 *   <li><b>200 OK</b> — valid requests that successfully assemble and return the root model.</li>
 *   <li><b>400 Bad Request</b> — invalid or missing user identifiers, and unresolvable users.</li>
 *   <li><b>500 Internal Server Error</b> — unexpected failures thrown by link providers.</li>
 * </ul>
 *
 * <p>All tests are pure unit tests using Mockito. No Spring context is loaded.</p>
 */
@ExtendWith(MockitoExtension.class)
class RootControllerTest {

    @Mock
    private RootLinkProvider providerA;

    @Mock
    private RootLinkProvider providerB;

    // =========================================================================
    // 200 OK
    // =========================================================================

    @Test
    void getRoot_validUserId_returns200() throws InstanceNotFoundException {
        // Arrange
        RootController controller = new RootController(List.of(providerA, providerB));
        when(providerA.getLinks(any(UserId.class))).thenReturn(List.of());
        when(providerB.getLinks(any(UserId.class))).thenReturn(List.of());

        // Act
        ResponseEntity<RepresentationModel<?>> response = controller.getRoot("user-001");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getRoot_validUserId_bodyIsNotNull() throws InstanceNotFoundException {
        // Arrange
        RootController controller = new RootController(List.of(providerA, providerB));
        when(providerA.getLinks(any(UserId.class))).thenReturn(List.of());
        when(providerB.getLinks(any(UserId.class))).thenReturn(List.of());

        // Act
        ResponseEntity<RepresentationModel<?>> response = controller.getRoot("user-001");

        // Assert
        assertNotNull(response.getBody());
    }

    @Test
    void getRoot_validUserId_selfLinkIsPresent() throws InstanceNotFoundException {
        // Arrange
        RootController controller = new RootController(List.of(providerA, providerB));
        when(providerA.getLinks(any(UserId.class))).thenReturn(List.of());
        when(providerB.getLinks(any(UserId.class))).thenReturn(List.of());

        // Act
        ResponseEntity<RepresentationModel<?>> response = controller.getRoot("user-001");

        // Assert
        assertTrue(response.getBody().hasLink("self"));
    }

    @Test
    void getRoot_linksFromProvidersAreAttachedToBody() throws InstanceNotFoundException {
        // Arrange
        RootController controller = new RootController(List.of(providerA, providerB));
        Link linkA = Link.of("http://localhost/authors", "find-all-authors");
        Link linkB = Link.of("http://localhost/genres", "get-all-genres");
        when(providerA.getLinks(any(UserId.class))).thenReturn(List.of(linkA));
        when(providerB.getLinks(any(UserId.class))).thenReturn(List.of(linkB));

        // Act
        ResponseEntity<RepresentationModel<?>> response = controller.getRoot("user-001");

        // Assert
        assertTrue(response.getBody().hasLink("find-all-authors"));
        assertTrue(response.getBody().hasLink("get-all-genres"));
    }

    @Test
    void getRoot_noProvidersRegistered_returns200WithSelfLinkOnly() {
        // Arrange
        RootController emptyController = new RootController(List.of());

        // Act
        ResponseEntity<RepresentationModel<?>> response = emptyController.getRoot("user-001");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().hasLink("self"));
        assertEquals(1, response.getBody().getLinks().toList().size());
    }

    // =========================================================================
    // 400 Bad Request
    // =========================================================================

    @Test
    void getRoot_nullUserId_returns400() {
        // Arrange
        RootController controller = new RootController(List.of(providerA, providerB));

        // Act
        ResponseEntity<RepresentationModel<?>> response = controller.getRoot(null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getRoot_emptyUserId_returns400() {
        // Arrange
        RootController controller = new RootController(List.of(providerA, providerB));

        // Act
        ResponseEntity<RepresentationModel<?>> response = controller.getRoot("");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getRoot_providerThrowsInstanceNotFoundException_returns400()
            throws InstanceNotFoundException {
        // Arrange
        RootController controller = new RootController(List.of(providerA, providerB));
        when(providerA.getLinks(any(UserId.class)))
                .thenThrow(new InstanceNotFoundException("User not found"));

        // Act
        ResponseEntity<RepresentationModel<?>> response = controller.getRoot("user-001");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // =========================================================================
    // 500 Internal Server Error
    // =========================================================================

    @Test
    void getRoot_providerThrowsUnexpectedException_returns500()
            throws InstanceNotFoundException {
        // Arrange
        RootController controller = new RootController(List.of(providerA, providerB));
        when(providerA.getLinks(any(UserId.class)))
                .thenThrow(new RuntimeException("Unexpected failure"));

        // Act
        ResponseEntity<RepresentationModel<?>> response = controller.getRoot("user-001");

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}