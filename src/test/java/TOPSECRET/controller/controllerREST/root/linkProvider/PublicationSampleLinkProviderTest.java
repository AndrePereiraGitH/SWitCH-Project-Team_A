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
 *   <li><b>getPublicationSampleLinks — fixed links</b> — always-included links.</li>
 *   <li><b>getPublicationSampleLinks — conditional links</b> — policy-gated links.</li>
 *   <li><b>getPublicationSampleLinks — link counts</b> — total link count validation.</li>
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
    // getPublicationSampleLinks — fixed links (always included)
    // =========================================================================

    @Test
    void getPublicationSampleLinks_shouldReturnFindByIdLink() throws InstanceNotFoundException {

        // Arrange
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));

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

        // Act
        List<Link> links = linkProvider.getPublicationSampleLinks(userId);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("find-by-bdId")));
    }

    @Test
    void getPublicationSampleLinks_shouldReturnFindByBdIdsLink() throws InstanceNotFoundException {

        // Arrange
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));

        // Act
        List<Link> links = linkProvider.getPublicationSampleLinks(userId);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("find-by-bdIds")));
    }

    @Test
    void getPublicationSampleLinks_shouldReturnFindByEditionIdLink() throws InstanceNotFoundException {

        // Arrange
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));

        // Act
        List<Link> links = linkProvider.getPublicationSampleLinks(userId);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("find-by-editionIds")));
    }

    // =========================================================================
    // getPublicationSampleLinks — conditional links (policy-gated)
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

        // Act
        List<Link> links = linkProvider.getPublicationSampleLinks(userId);

        // Assert
        assertFalse(links.stream().anyMatch(l -> l.getRel().value().equals("create-publication-sample")));
    }

    @Test
    void getPublicationSampleLinks_shouldIncludeFindByCollectionIdLink_whenAuthorized() throws InstanceNotFoundException {

        // Arrange
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindByCollectionOfPublicationsId(user)).thenReturn(true);

        // Act
        List<Link> links = linkProvider.getPublicationSampleLinks(userId);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("find-by-collectionId")));
    }

    @Test
    void getPublicationSampleLinks_shouldNotIncludeFindByCollectionIdLink_whenNotAuthorized() throws InstanceNotFoundException {

        // Arrange
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));

        // Act
        List<Link> links = linkProvider.getPublicationSampleLinks(userId);

        // Assert
        assertFalse(links.stream().anyMatch(l -> l.getRel().value().equals("find-by-collectionId")));
    }

    @Test
    void getPublicationSampleLinks_shouldIncludeFindInLibraryLink_whenAuthorized() throws InstanceNotFoundException {

        // Arrange
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindByUserIdAndLibraryIdIsNotNull(user)).thenReturn(true);

        // Act
        List<Link> links = linkProvider.getPublicationSampleLinks(userId);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("find-in-library")));
    }

    @Test
    void getPublicationSampleLinks_shouldNotIncludeFindInLibraryLink_whenNotAuthorized() throws InstanceNotFoundException {

        // Arrange
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));

        // Act
        List<Link> links = linkProvider.getPublicationSampleLinks(userId);

        // Assert
        assertFalse(links.stream().anyMatch(l -> l.getRel().value().equals("find-in-library")));
    }

    @Test
    void getPublicationSampleLinks_shouldIncludeFindNotInLibraryLink_whenAuthorized() throws InstanceNotFoundException {

        // Arrange
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindByUserIdAndLibraryIdIsNull(user)).thenReturn(true);

        // Act
        List<Link> links = linkProvider.getPublicationSampleLinks(userId);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("find-not-in-library")));
    }

    @Test
    void getPublicationSampleLinks_shouldNotIncludeFindNotInLibraryLink_whenNotAuthorized() throws InstanceNotFoundException {

        // Arrange
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));

        // Act
        List<Link> links = linkProvider.getPublicationSampleLinks(userId);

        // Assert
        assertFalse(links.stream().anyMatch(l -> l.getRel().value().equals("find-not-in-library")));
    }

    @Test
    void getPublicationSampleLinks_shouldIncludeFindAvailableInLibraryLink_whenAuthorized() throws InstanceNotFoundException {

        // Arrange
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindByLibraryIdAndPublicationStatusNotIn(user)).thenReturn(true);

        // Act
        List<Link> links = linkProvider.getPublicationSampleLinks(userId);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("find-available-in-library")));
    }

    @Test
    void getPublicationSampleLinks_shouldNotIncludeFindAvailableInLibraryLink_whenNotAuthorized() throws InstanceNotFoundException {

        // Arrange
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));

        // Act
        List<Link> links = linkProvider.getPublicationSampleLinks(userId);

        // Assert
        assertFalse(links.stream().anyMatch(l -> l.getRel().value().equals("find-available-in-library")));
    }

    @Test
    void getPublicationSampleLinks_shouldIncludeFindAvailableForCollectionLink_whenAuthorized() throws InstanceNotFoundException {

        // Arrange
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindAvailableForCollection(user)).thenReturn(true);

        // Act
        List<Link> links = linkProvider.getPublicationSampleLinks(userId);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("find-available-for-collection")));
    }

    @Test
    void getPublicationSampleLinks_shouldNotIncludeFindAvailableForCollectionLink_whenNotAuthorized() throws InstanceNotFoundException {

        // Arrange
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));

        // Act
        List<Link> links = linkProvider.getPublicationSampleLinks(userId);

        // Assert
        assertFalse(links.stream().anyMatch(l -> l.getRel().value().equals("find-available-for-collection")));
    }

    @Test
    void getPublicationSampleLinks_shouldIncludeAddToLibraryLink_whenAuthorized() throws InstanceNotFoundException {

        // Arrange
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canAddPublicationToLibrary(user)).thenReturn(true);

        // Act
        List<Link> links = linkProvider.getPublicationSampleLinks(userId);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("add-to-library")));
    }

    @Test
    void getPublicationSampleLinks_shouldNotIncludeAddToLibraryLink_whenNotAuthorized() throws InstanceNotFoundException {

        // Arrange
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));

        // Act
        List<Link> links = linkProvider.getPublicationSampleLinks(userId);

        // Assert
        assertFalse(links.stream().anyMatch(l -> l.getRel().value().equals("add-to-library")));
    }

    @Test
    void getPublicationSampleLinks_shouldIncludeAssignToCollectionLink_whenAuthorized() throws InstanceNotFoundException {

        // Arrange
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canAssignPublicationToCollection(user)).thenReturn(true);

        // Act
        List<Link> links = linkProvider.getPublicationSampleLinks(userId);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("assign-to-collection")));
    }

    @Test
    void getPublicationSampleLinks_shouldNotIncludeAssignToCollectionLink_whenNotAuthorized() throws InstanceNotFoundException {

        // Arrange
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));

        // Act
        List<Link> links = linkProvider.getPublicationSampleLinks(userId);

        // Assert
        assertFalse(links.stream().anyMatch(l -> l.getRel().value().equals("assign-to-collection")));
    }

    // =========================================================================
    // getPublicationSampleLinks — link counts
    // =========================================================================

    @Test
    void getPublicationSampleLinks_shouldReturnFiveFixedLinks_whenNoPolicyGranted() throws InstanceNotFoundException {

        // Arrange
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));

        // Act
        List<Link> links = linkProvider.getPublicationSampleLinks(userId);

        // Assert
        assertEquals(5, links.size());
    }

    @Test
    void getPublicationSampleLinks_shouldReturnThirteenLinks_whenAllPoliciesGranted() throws InstanceNotFoundException {

        // Arrange
        UserId userId = mock(UserId.class);
        User user = mock(User.class);
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreatePublicationSample(user)).thenReturn(true);
        when(policy.canFindByCollectionOfPublicationsId(user)).thenReturn(true);
        when(policy.canFindByUserIdAndLibraryIdIsNotNull(user)).thenReturn(true);
        when(policy.canFindByUserIdAndLibraryIdIsNull(user)).thenReturn(true);
        when(policy.canFindByLibraryIdAndPublicationStatusNotIn(user)).thenReturn(true);
        when(policy.canFindAvailableForCollection(user)).thenReturn(true);
        when(policy.canAddPublicationToLibrary(user)).thenReturn(true);
        when(policy.canAssignPublicationToCollection(user)).thenReturn(true);

        // Act
        List<Link> links = linkProvider.getPublicationSampleLinks(userId);

        // Assert
        assertEquals(13, links.size());
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