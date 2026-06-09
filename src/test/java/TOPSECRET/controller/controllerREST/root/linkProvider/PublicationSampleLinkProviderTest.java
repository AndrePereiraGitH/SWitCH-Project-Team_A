package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.PublicationSampleAuthorizationPolicy;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.BeforeEach;
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
 *   <li><b>getPublicLinks</b> — always-included links requiring no authorization.</li>
 *   <li><b>getOwnerLinks</b> — policy-gated owner links.</li>
 *   <li><b>getLibraryLinks</b> — policy-gated library links.</li>
 *   <li><b>getCollectionLinks</b> — policy-gated collection links.</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
class PublicationSampleLinkProviderTest {

    @Mock private PublicationSampleAuthorizationPolicy policy;
    @Mock private IUserRepository userRepository;

    @InjectMocks
    private PublicationSampleLinkProvider provider;

    private UserId userId;
    private User user;

    @BeforeEach
    void setUp() {
        userId = mock(UserId.class);
        user = mock(User.class);
    }

    // =========================================================================
    // getPublicLinks
    // =========================================================================

    @Test
    void getPublicLinks_shouldReturnFindByIdLink() {

        // Act
        List<Link> links = provider.getPublicLinks();

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("find-by-id")));
    }

    @Test
    void getPublicLinks_shouldReturnFindAllLink() {

        // Act
        List<Link> links = provider.getPublicLinks();

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("find-all")));
    }

    @Test
    void getPublicLinks_shouldReturnFindByBdIdLink() {

        // Act
        List<Link> links = provider.getPublicLinks();

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("find-by-bdId")));
    }

    @Test
    void getPublicLinks_shouldReturnFindByBdIdsLink() {

        // Act
        List<Link> links = provider.getPublicLinks();

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("find-by-bdIds")));
    }

    @Test
    void getPublicLinks_shouldReturnFindByEditionIdsLink() {

        // Act
        List<Link> links = provider.getPublicLinks();

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("find-by-editionIds")));
    }

    @Test
    void getPublicLinks_shouldReturnExactlyFiveLinks() {

        // Act
        List<Link> links = provider.getPublicLinks();

        // Assert
        assertEquals(5, links.size());
    }

    @Test
    void getPublicLinks_shouldThrowNullPointerException_whenUserIdIsNull() {

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                provider.getOwnerLinks(null));
    }

    // =========================================================================
    // getOwnerLinks
    // =========================================================================

    @Test
    void getOwnerLinks_shouldIncludeCreateLink_whenUserCanCreate() throws InstanceNotFoundException {

        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreatePublicationSample(user)).thenReturn(true);

        // Act
        List<Link> links = provider.getOwnerLinks(userId);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("create-publication-sample")));
    }

    @Test
    void getOwnerLinks_shouldNotIncludeCreateLink_whenUserCannotCreate() throws InstanceNotFoundException {

        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreatePublicationSample(user)).thenReturn(false);

        // Act
        List<Link> links = provider.getOwnerLinks(userId);

        // Assert
        assertFalse(links.stream().anyMatch(l -> l.getRel().value().equals("create-publication-sample")));
    }

    @Test
    void getOwnerLinks_shouldReturnEmptyList_whenNoPolicyGranted() throws InstanceNotFoundException {

        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreatePublicationSample(user)).thenReturn(false);

        // Act
        List<Link> links = provider.getOwnerLinks(userId);

        // Assert
        assertTrue(links.isEmpty());
    }

    @Test
    void getOwnerLinks_shouldThrowInstanceNotFoundException_whenUserNotFound() {

        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InstanceNotFoundException.class, () ->
                provider.getOwnerLinks(userId));
    }

    @Test
    void getOwnerLinks_shouldThrowNullPointerException_whenUserIdIsNull() {

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                provider.getOwnerLinks(null));
    }

    // =========================================================================
    // getLibraryLinks
    // =========================================================================

    @Test
    void getLibraryLinks_shouldIncludeFindInLibraryLink_whenAuthorized() throws InstanceNotFoundException {

        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindPublicationsInLibrary(user)).thenReturn(true);

        // Act
        List<Link> links = provider.getLibraryLinks(userId);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("find-in-library")));
    }

    @Test
    void getLibraryLinks_shouldNotIncludeFindInLibraryLink_whenNotAuthorized() throws InstanceNotFoundException {

        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindPublicationsInLibrary(user)).thenReturn(false);

        // Act
        List<Link> links = provider.getLibraryLinks(userId);

        // Assert
        assertFalse(links.stream().anyMatch(l -> l.getRel().value().equals("find-in-library")));
    }

    @Test
    void getLibraryLinks_shouldIncludeFindNotInLibraryLink_whenAuthorized() throws InstanceNotFoundException {

        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindPublicationsNotInLibrary(user)).thenReturn(true);

        // Act
        List<Link> links = provider.getLibraryLinks(userId);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("find-not-in-library")));
    }

    @Test
    void getLibraryLinks_shouldNotIncludeFindNotInLibraryLink_whenNotAuthorized() throws InstanceNotFoundException {

        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindPublicationsNotInLibrary(user)).thenReturn(false);

        // Act
        List<Link> links = provider.getLibraryLinks(userId);

        // Assert
        assertFalse(links.stream().anyMatch(l -> l.getRel().value().equals("find-not-in-library")));
    }

    @Test
    void getLibraryLinks_shouldIncludeFindAvailableInLibraryLink_whenAuthorized() throws InstanceNotFoundException {

        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindByLibraryIdAndPublicationStatusNotIn(user)).thenReturn(true);

        // Act
        List<Link> links = provider.getLibraryLinks(userId);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("find-available-in-library")));
    }

    @Test
    void getLibraryLinks_shouldNotIncludeFindAvailableInLibraryLink_whenNotAuthorized() throws InstanceNotFoundException {

        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindByLibraryIdAndPublicationStatusNotIn(user)).thenReturn(false);

        // Act
        List<Link> links = provider.getLibraryLinks(userId);

        // Assert
        assertFalse(links.stream().anyMatch(l -> l.getRel().value().equals("find-available-in-library")));
    }

    @Test
    void getLibraryLinks_shouldIncludeAddToLibraryLink_whenAuthorized() throws InstanceNotFoundException {

        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canAddPublicationToLibrary(user)).thenReturn(true);

        // Act
        List<Link> links = provider.getLibraryLinks(userId);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("add-to-library")));
    }

    @Test
    void getLibraryLinks_shouldNotIncludeAddToLibraryLink_whenNotAuthorized() throws InstanceNotFoundException {

        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canAddPublicationToLibrary(user)).thenReturn(false);

        // Act
        List<Link> links = provider.getLibraryLinks(userId);

        // Assert
        assertFalse(links.stream().anyMatch(l -> l.getRel().value().equals("add-to-library")));
    }

    @Test
    void getLibraryLinks_shouldReturnEmptyList_whenNoPolicyGranted() throws InstanceNotFoundException {

        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindPublicationsInLibrary(user)).thenReturn(false);
        when(policy.canFindPublicationsNotInLibrary(user)).thenReturn(false);
        when(policy.canFindByLibraryIdAndPublicationStatusNotIn(user)).thenReturn(false);
        when(policy.canAddPublicationToLibrary(user)).thenReturn(false);

        // Act
        List<Link> links = provider.getLibraryLinks(userId);

        // Assert
        assertTrue(links.isEmpty());
    }

    @Test
    void getLibraryLinks_shouldReturnAllFourLinks_whenAllPoliciesGranted() throws InstanceNotFoundException {

        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindPublicationsInLibrary(user)).thenReturn(true);
        when(policy.canFindPublicationsNotInLibrary(user)).thenReturn(true);
        when(policy.canFindByLibraryIdAndPublicationStatusNotIn(user)).thenReturn(true);
        when(policy.canAddPublicationToLibrary(user)).thenReturn(true);

        // Act
        List<Link> links = provider.getLibraryLinks(userId);

        // Assert
        assertEquals(4, links.size());
    }

    @Test
    void getLibraryLinks_shouldThrowInstanceNotFoundException_whenUserNotFound() {

        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InstanceNotFoundException.class, () ->
                provider.getLibraryLinks(userId));
    }

    @Test
    void getLibraryLinks_shouldThrowNullPointerException_whenUserIdIsNull() {

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                provider.getLibraryLinks(null));
    }

    // =========================================================================
    // getCollectionLinks
    // =========================================================================

    @Test
    void getCollectionLinks_shouldIncludeFindByCollectionIdLink_whenAuthorized() throws InstanceNotFoundException {

        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindByCollectionOfPublicationsId(user)).thenReturn(true);

        // Act
        List<Link> links = provider.getCollectionLinks(userId);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("find-by-collectionId")));
    }

    @Test
    void getCollectionLinks_shouldNotIncludeFindByCollectionIdLink_whenNotAuthorized() throws InstanceNotFoundException {

        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindByCollectionOfPublicationsId(user)).thenReturn(false);

        // Act
        List<Link> links = provider.getCollectionLinks(userId);

        // Assert
        assertFalse(links.stream().anyMatch(l -> l.getRel().value().equals("find-by-collectionId")));
    }

    @Test
    void getCollectionLinks_shouldIncludeFindAvailableForCollectionLink_whenAuthorized() throws InstanceNotFoundException {

        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindAvailableForCollection(user)).thenReturn(true);

        // Act
        List<Link> links = provider.getCollectionLinks(userId);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("find-available-for-collection")));
    }

    @Test
    void getCollectionLinks_shouldNotIncludeFindAvailableForCollectionLink_whenNotAuthorized() throws InstanceNotFoundException {

        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindAvailableForCollection(user)).thenReturn(false);

        // Act
        List<Link> links = provider.getCollectionLinks(userId);

        // Assert
        assertFalse(links.stream().anyMatch(l -> l.getRel().value().equals("find-available-for-collection")));
    }

    @Test
    void getCollectionLinks_shouldIncludeAssignToCollectionLink_whenAuthorized() throws InstanceNotFoundException {

        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canAssignPublicationToCollection(user)).thenReturn(true);

        // Act
        List<Link> links = provider.getCollectionLinks(userId);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("assign-to-collection")));
    }

    @Test
    void getCollectionLinks_shouldNotIncludeAssignToCollectionLink_whenNotAuthorized() throws InstanceNotFoundException {

        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canAssignPublicationToCollection(user)).thenReturn(false);

        // Act
        List<Link> links = provider.getCollectionLinks(userId);

        // Assert
        assertFalse(links.stream().anyMatch(l -> l.getRel().value().equals("assign-to-collection")));
    }

    @Test
    void getCollectionLinks_shouldReturnEmptyList_whenNoPolicyGranted() throws InstanceNotFoundException {

        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindByCollectionOfPublicationsId(user)).thenReturn(false);
        when(policy.canFindAvailableForCollection(user)).thenReturn(false);
        when(policy.canAssignPublicationToCollection(user)).thenReturn(false);

        // Act
        List<Link> links = provider.getCollectionLinks(userId);

        // Assert
        assertTrue(links.isEmpty());
    }

    @Test
    void getCollectionLinks_shouldReturnAllThreeLinks_whenAllPoliciesGranted() throws InstanceNotFoundException {

        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canFindByCollectionOfPublicationsId(user)).thenReturn(true);
        when(policy.canFindAvailableForCollection(user)).thenReturn(true);
        when(policy.canAssignPublicationToCollection(user)).thenReturn(true);

        // Act
        List<Link> links = provider.getCollectionLinks(userId);

        // Assert
        assertEquals(3, links.size());
    }

    @Test
    void getCollectionLinks_shouldThrowInstanceNotFoundException_whenUserNotFound() {

        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InstanceNotFoundException.class, () ->
                provider.getCollectionLinks(userId));
    }

    @Test
    void getCollectionLinks_shouldThrowNullPointerException_whenUserIdIsNull() {

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                provider.getCollectionLinks(null));
    }

    // =========================================================================
    // Public links — always present
    // =========================================================================


    @Test
    void getLinks_alwaysContainsFindById() throws InstanceNotFoundException {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreatePublicationSample(user)).thenReturn(false);
        when(policy.canFindPublicationsInLibrary(user)).thenReturn(false);
        when(policy.canFindPublicationsNotInLibrary(user)).thenReturn(false);
        when(policy.canFindByLibraryIdAndPublicationStatusNotIn(user)).thenReturn(false);
        when(policy.canAddPublicationToLibrary(user)).thenReturn(false);
        when(policy.canFindByCollectionOfPublicationsId(user)).thenReturn(false);
        when(policy.canFindAvailableForCollection(user)).thenReturn(false);
        when(policy.canAssignPublicationToCollection(user)).thenReturn(false);

        // Act
        List<Link> links = provider.getLinks(userId);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("find-by-id")));
    }


    @Test
    void getLinks_alwaysContainsFindAll() throws InstanceNotFoundException {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreatePublicationSample(user)).thenReturn(false);
        when(policy.canFindPublicationsInLibrary(user)).thenReturn(false);
        when(policy.canFindPublicationsNotInLibrary(user)).thenReturn(false);
        when(policy.canFindByLibraryIdAndPublicationStatusNotIn(user)).thenReturn(false);
        when(policy.canAddPublicationToLibrary(user)).thenReturn(false);
        when(policy.canFindByCollectionOfPublicationsId(user)).thenReturn(false);
        when(policy.canFindAvailableForCollection(user)).thenReturn(false);
        when(policy.canAssignPublicationToCollection(user)).thenReturn(false);

        // Act
        List<Link> links = provider.getLinks(userId);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("find-all")));
    }


    @Test
    void getLinks_alwaysContainsFindByBdId() throws InstanceNotFoundException {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreatePublicationSample(user)).thenReturn(false);
        when(policy.canFindPublicationsInLibrary(user)).thenReturn(false);
        when(policy.canFindPublicationsNotInLibrary(user)).thenReturn(false);
        when(policy.canFindByLibraryIdAndPublicationStatusNotIn(user)).thenReturn(false);
        when(policy.canAddPublicationToLibrary(user)).thenReturn(false);
        when(policy.canFindByCollectionOfPublicationsId(user)).thenReturn(false);
        when(policy.canFindAvailableForCollection(user)).thenReturn(false);
        when(policy.canAssignPublicationToCollection(user)).thenReturn(false);

        // Act
        List<Link> links = provider.getLinks(userId);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("find-by-bdId")));
    }


    @Test
    void getLinks_alwaysContainsFindByEditionIds() throws InstanceNotFoundException {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreatePublicationSample(user)).thenReturn(false);
        when(policy.canFindPublicationsInLibrary(user)).thenReturn(false);
        when(policy.canFindPublicationsNotInLibrary(user)).thenReturn(false);
        when(policy.canFindByLibraryIdAndPublicationStatusNotIn(user)).thenReturn(false);
        when(policy.canAddPublicationToLibrary(user)).thenReturn(false);
        when(policy.canFindByCollectionOfPublicationsId(user)).thenReturn(false);
        when(policy.canFindAvailableForCollection(user)).thenReturn(false);
        when(policy.canAssignPublicationToCollection(user)).thenReturn(false);

        // Act
        List<Link> links = provider.getLinks(userId);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("find-by-editionIds")));
    }


    @Test
    void getLinks_alwaysContainsFindByBdIds() throws InstanceNotFoundException {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreatePublicationSample(user)).thenReturn(false);
        when(policy.canFindPublicationsInLibrary(user)).thenReturn(false);
        when(policy.canFindPublicationsNotInLibrary(user)).thenReturn(false);
        when(policy.canFindByLibraryIdAndPublicationStatusNotIn(user)).thenReturn(false);
        when(policy.canAddPublicationToLibrary(user)).thenReturn(false);
        when(policy.canFindByCollectionOfPublicationsId(user)).thenReturn(false);
        when(policy.canFindAvailableForCollection(user)).thenReturn(false);
        when(policy.canAssignPublicationToCollection(user)).thenReturn(false);

        // Act
        List<Link> links = provider.getLinks(userId);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("find-by-bdIds")));
    }

    // =========================================================================
    // Owner links — conditional
    // =========================================================================


    @Test
    void getLinks_whenCanCreate_containsCreatePublicationSample() throws InstanceNotFoundException {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreatePublicationSample(user)).thenReturn(true);
        when(policy.canFindPublicationsInLibrary(user)).thenReturn(false);
        when(policy.canFindPublicationsNotInLibrary(user)).thenReturn(false);
        when(policy.canFindByLibraryIdAndPublicationStatusNotIn(user)).thenReturn(false);
        when(policy.canAddPublicationToLibrary(user)).thenReturn(false);
        when(policy.canFindByCollectionOfPublicationsId(user)).thenReturn(false);
        when(policy.canFindAvailableForCollection(user)).thenReturn(false);
        when(policy.canAssignPublicationToCollection(user)).thenReturn(false);

        // Act
        List<Link> links = provider.getLinks(userId);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("create-publication-sample")));
    }


    @Test
    void getLinks_whenCannotCreate_doesNotContainCreatePublicationSample() throws InstanceNotFoundException {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreatePublicationSample(user)).thenReturn(false);
        when(policy.canFindPublicationsInLibrary(user)).thenReturn(false);
        when(policy.canFindPublicationsNotInLibrary(user)).thenReturn(false);
        when(policy.canFindByLibraryIdAndPublicationStatusNotIn(user)).thenReturn(false);
        when(policy.canAddPublicationToLibrary(user)).thenReturn(false);
        when(policy.canFindByCollectionOfPublicationsId(user)).thenReturn(false);
        when(policy.canFindAvailableForCollection(user)).thenReturn(false);
        when(policy.canAssignPublicationToCollection(user)).thenReturn(false);

        // Act
        List<Link> links = provider.getLinks(userId);

        // Assert
        assertFalse(links.stream().anyMatch(l -> l.getRel().value().equals("create-publication-sample")));
    }

    // =========================================================================
    // Library links — conditional
    // =========================================================================


    @Test
    void getLinks_whenCanFindInLibrary_containsFindInLibrary() throws InstanceNotFoundException {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreatePublicationSample(user)).thenReturn(false);
        when(policy.canFindPublicationsInLibrary(user)).thenReturn(true);
        when(policy.canFindPublicationsNotInLibrary(user)).thenReturn(false);
        when(policy.canFindByLibraryIdAndPublicationStatusNotIn(user)).thenReturn(false);
        when(policy.canAddPublicationToLibrary(user)).thenReturn(false);
        when(policy.canFindByCollectionOfPublicationsId(user)).thenReturn(false);
        when(policy.canFindAvailableForCollection(user)).thenReturn(false);
        when(policy.canAssignPublicationToCollection(user)).thenReturn(false);

        // Act
        List<Link> links = provider.getLinks(userId);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("find-in-library")));
    }


    @Test
    void getLinks_whenCanAddToLibrary_containsAddToLibrary() throws InstanceNotFoundException {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreatePublicationSample(user)).thenReturn(false);
        when(policy.canFindPublicationsInLibrary(user)).thenReturn(false);
        when(policy.canFindPublicationsNotInLibrary(user)).thenReturn(false);
        when(policy.canFindByLibraryIdAndPublicationStatusNotIn(user)).thenReturn(false);
        when(policy.canAddPublicationToLibrary(user)).thenReturn(true);
        when(policy.canFindByCollectionOfPublicationsId(user)).thenReturn(false);
        when(policy.canFindAvailableForCollection(user)).thenReturn(false);
        when(policy.canAssignPublicationToCollection(user)).thenReturn(false);

        // Act
        List<Link> links = provider.getLinks(userId);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("add-to-library")));
    }

    // =========================================================================
    // Collection links — conditional
    // =========================================================================


    @Test
    void getLinks_whenCanAssignToCollection_containsAssignToCollection() throws InstanceNotFoundException {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreatePublicationSample(user)).thenReturn(false);
        when(policy.canFindPublicationsInLibrary(user)).thenReturn(false);
        when(policy.canFindPublicationsNotInLibrary(user)).thenReturn(false);
        when(policy.canFindByLibraryIdAndPublicationStatusNotIn(user)).thenReturn(false);
        when(policy.canAddPublicationToLibrary(user)).thenReturn(false);
        when(policy.canFindByCollectionOfPublicationsId(user)).thenReturn(false);
        when(policy.canFindAvailableForCollection(user)).thenReturn(false);
        when(policy.canAssignPublicationToCollection(user)).thenReturn(true);

        // Act
        List<Link> links = provider.getLinks(userId);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("assign-to-collection")));
    }


    @Test
    void getLinks_whenCanFindByCollectionId_containsFindByCollectionId() throws InstanceNotFoundException {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreatePublicationSample(user)).thenReturn(false);
        when(policy.canFindPublicationsInLibrary(user)).thenReturn(false);
        when(policy.canFindPublicationsNotInLibrary(user)).thenReturn(false);
        when(policy.canFindByLibraryIdAndPublicationStatusNotIn(user)).thenReturn(false);
        when(policy.canAddPublicationToLibrary(user)).thenReturn(false);
        when(policy.canFindByCollectionOfPublicationsId(user)).thenReturn(true);
        when(policy.canFindAvailableForCollection(user)).thenReturn(false);
        when(policy.canAssignPublicationToCollection(user)).thenReturn(false);

        // Act
        List<Link> links = provider.getLinks(userId);

        // Assert
        assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("find-by-collectionId")));
    }

    // =========================================================================
    // User not found
    // =========================================================================


    @Test
    void getLinks_userNotFound_throwsInstanceNotFoundException() {
        // Arrange
        when(userRepository.findByID(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InstanceNotFoundException.class, () -> provider.getLinks(userId));
    }
}
