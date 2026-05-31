package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.PublicationSampleAuthorizationPolicy;
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
 * Unit tests for {@link PublicationSampleLinkProvider}.
 *
 * <p>Validates HATEOAS link generation based on user permissions and existence.</p>
 *
 * <p>Tests are organized into the following sections:</p>
 * <ul>
 *   <li><b>getPublicationSampleLinks — success</b> — links present for authorized users.</li>
 *   <li><b>getPublicationSampleLinks — authorization</b> — conditional links based on policy.</li>
 *   <li><b>getPublicationSampleLinks — exceptions</b> — user not found.</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
class PublicationSampleLinkProviderTest {

    @Mock private PublicationSampleAuthorizationPolicy policy;
    @Mock private IUserRepository userRepository;

    @InjectMocks
    private PublicationSampleLinkProvider linkProvider;

    // =========================================================================
    // getPublicationSampleLinks — success
    // =========================================================================

    @Test
    void getPublicationSampleLinks_shouldReturnFindByIdLink() throws InstanceNotFoundException {

        // Arrange
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreatePublicationSample(user)).thenReturn(false);

        // Act
        List<Link> links = linkProvider.getPublicationSampleLinks(userId);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("find-by-id")));
    }

    @Test
    void getPublicationSampleLinks_shouldReturnFindAllLink() throws InstanceNotFoundException {

        // Arrange
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreatePublicationSample(user)).thenReturn(false);

        // Act
        List<Link> links = linkProvider.getPublicationSampleLinks(userId);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("find-all")));
    }

    @Test
    void getPublicationSampleLinks_shouldReturnFindByBdIdLink() throws InstanceNotFoundException {

        // Arrange
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreatePublicationSample(user)).thenReturn(false);

        // Act
        List<Link> links = linkProvider.getPublicationSampleLinks(userId);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("find-by-bdId")));
    }

    @Test
    void getPublicationSampleLinks_shouldReturnFindByEditionIdLink() throws InstanceNotFoundException {

        // Arrange
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreatePublicationSample(user)).thenReturn(false);

        // Act
        List<Link> links = linkProvider.getPublicationSampleLinks(userId);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("find-by-editionId")));
    }

    @Test
    void getPublicationSampleLinks_shouldReturnFourLinks_whenCannotCreate() throws InstanceNotFoundException {

        // Arrange
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreatePublicationSample(user)).thenReturn(false);

        // Act
        List<Link> links = linkProvider.getPublicationSampleLinks(userId);

        // Assert
        assertEquals(4, links.size());
    }

    // =========================================================================
    // getPublicationSampleLinks — authorization
    // =========================================================================

    @Test
    void getPublicationSampleLinks_shouldIncludeCreateLink_whenUserCanCreate() throws InstanceNotFoundException {

        // Arrange
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreatePublicationSample(user)).thenReturn(true);

        // Act
        List<Link> links = linkProvider.getPublicationSampleLinks(userId);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("create-publication-sample")));
    }

    @Test
    void getPublicationSampleLinks_shouldNotIncludeCreateLink_whenUserCannotCreate() throws InstanceNotFoundException {

        // Arrange
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreatePublicationSample(user)).thenReturn(false);

        // Act
        List<Link> links = linkProvider.getPublicationSampleLinks(userId);

        // Assert
        assertFalse(links.stream().anyMatch(l -> l.getRel().value().equals("create-publication-sample")));
    }

    @Test
    void getPublicationSampleLinks_shouldReturnFiveLinks_whenUserCanCreate() throws InstanceNotFoundException {

        // Arrange
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreatePublicationSample(user)).thenReturn(true);

        // Act
        List<Link> links = linkProvider.getPublicationSampleLinks(userId);

        // Assert
        assertEquals(5, links.size());
    }

    // =========================================================================
    // getPublicationSampleLinks — exceptions
    // =========================================================================

    @Test
    void getPublicationSampleLinks_shouldThrowInstanceNotFoundException_whenUserNotFound() {

        // Arrange
        UserId userId = mock(UserId.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InstanceNotFoundException.class, () ->
                linkProvider.getPublicationSampleLinks(userId));
    }
}