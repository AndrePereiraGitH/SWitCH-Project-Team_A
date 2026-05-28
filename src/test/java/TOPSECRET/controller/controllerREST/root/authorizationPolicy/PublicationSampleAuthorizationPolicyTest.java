package TOPSECRET.controller.controllerREST.root.authorizationPolicy;

import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.User.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link PublicationSampleAuthorizationPolicy} class.
 * <p>
 * This test suite verifies the access control logic for operations related to
 * publication samples. It ensures that only users possessing the
 * {@link UserRole#NORMAL_USER} role are granted authorization to perform
 * specific actions, such as creating, retrieving, and assigning publication samples.
 * </p>
 * <p>
 * The tests utilize Mockito to simulate both authorized and unauthorized
 * users by mocking the collection of roles returned by the
 * {@link User#getUserRoles()} method. To ensure strict test isolation, the
 * System Under Test (SUT) is explicitly instantiated within each test method.
 * </p>
 */
class PublicationSampleAuthorizationPolicyTest {

    private User authorizedUser;
    private User unauthorizedUser;

    @BeforeEach
    void setUp() {
        authorizedUser = mock(User.class);
        when(authorizedUser.getUserRoles()).thenReturn(Set.of(UserRole.NORMAL_USER));

        unauthorizedUser = mock(User.class);
        when(unauthorizedUser.getUserRoles()).thenReturn(Set.of());
    }

    @Test
    void canCreatePublicationSample_shouldReturnTrue_whenUserIsNormalUser() {
        // Arrange
        PublicationSampleAuthorizationPolicy policy = new PublicationSampleAuthorizationPolicy();

        // Act
        boolean result = policy.canCreatePublicationSample(authorizedUser);

        // Assert
        assertTrue(result);
    }

    @Test
    void canCreatePublicationSample_shouldReturnFalse_whenUserIsNotNormalUser() {
        // Arrange
        PublicationSampleAuthorizationPolicy policy = new PublicationSampleAuthorizationPolicy();

        // Act
        boolean result = policy.canCreatePublicationSample(unauthorizedUser);

        // Assert
        assertFalse(result);
    }

    @Test
    void canFindByLibraryId_shouldReturnTrue_whenUserIsNormalUser() {
        // Arrange
        PublicationSampleAuthorizationPolicy policy = new PublicationSampleAuthorizationPolicy();

        // Act
        boolean result = policy.canFindByLibraryId(authorizedUser);

        // Assert
        assertTrue(result);
    }

    @Test
    void canFindByLibraryId_shouldReturnFalse_whenUserIsNotNormalUser() {
        // Arrange
        PublicationSampleAuthorizationPolicy policy = new PublicationSampleAuthorizationPolicy();

        // Act
        boolean result = policy.canFindByLibraryId(unauthorizedUser);

        // Assert
        assertFalse(result);
    }

    @Test
    void canFindByCollectionOfPublicationsId_shouldReturnTrue_whenUserIsNormalUser() {
        // Arrange
        PublicationSampleAuthorizationPolicy policy = new PublicationSampleAuthorizationPolicy();

        // Act
        boolean result = policy.canFindByCollectionOfPublicationsId(authorizedUser);

        // Assert
        assertTrue(result);
    }

    @Test
    void canFindByCollectionOfPublicationsId_shouldReturnFalse_whenUserIsNotNormalUser() {
        // Arrange
        PublicationSampleAuthorizationPolicy policy = new PublicationSampleAuthorizationPolicy();

        // Act
        boolean result = policy.canFindByCollectionOfPublicationsId(unauthorizedUser);

        // Assert
        assertFalse(result);
    }

    @Test
    void canFindByUserIdAndLibraryIdIsNotNull_shouldReturnTrue_whenUserIsNormalUser() {
        // Arrange
        PublicationSampleAuthorizationPolicy policy = new PublicationSampleAuthorizationPolicy();

        // Act
        boolean result = policy.canFindByUserIdAndLibraryIdIsNotNull(authorizedUser);

        // Assert
        assertTrue(result);
    }

    @Test
    void canFindByUserIdAndLibraryIdIsNotNull_shouldReturnFalse_whenUserIsNotNormalUser() {
        // Arrange
        PublicationSampleAuthorizationPolicy policy = new PublicationSampleAuthorizationPolicy();

        // Act
        boolean result = policy.canFindByUserIdAndLibraryIdIsNotNull(unauthorizedUser);

        // Assert
        assertFalse(result);
    }

    @Test
    void canFindByUserIdAndLibraryIdIsNull_shouldReturnTrue_whenUserIsNormalUser() {
        // Arrange
        PublicationSampleAuthorizationPolicy policy = new PublicationSampleAuthorizationPolicy();

        // Act
        boolean result = policy.canFindByUserIdAndLibraryIdIsNull(authorizedUser);

        // Assert
        assertTrue(result);
    }

    @Test
    void canFindByUserIdAndLibraryIdIsNull_shouldReturnFalse_whenUserIsNotNormalUser() {
        // Arrange
        PublicationSampleAuthorizationPolicy policy = new PublicationSampleAuthorizationPolicy();

        // Act
        boolean result = policy.canFindByUserIdAndLibraryIdIsNull(unauthorizedUser);

        // Assert
        assertFalse(result);
    }

    @Test
    void canFindByLibraryIdAndPublicationStatusNotIn_shouldReturnTrue_whenUserIsNormalUser() {
        // Arrange
        PublicationSampleAuthorizationPolicy policy = new PublicationSampleAuthorizationPolicy();

        // Act
        boolean result = policy.canFindByLibraryIdAndPublicationStatusNotIn(authorizedUser);

        // Assert
        assertTrue(result);
    }

    @Test
    void canFindByLibraryIdAndPublicationStatusNotIn_shouldReturnFalse_whenUserIsNotNormalUser() {
        // Arrange
        PublicationSampleAuthorizationPolicy policy = new PublicationSampleAuthorizationPolicy();

        // Act
        boolean result = policy.canFindByLibraryIdAndPublicationStatusNotIn(unauthorizedUser);

        // Assert
        assertFalse(result);
    }

    @Test
    void canFindAvailableForCollection_shouldReturnTrue_whenUserIsNormalUser() {
        // Arrange
        PublicationSampleAuthorizationPolicy policy = new PublicationSampleAuthorizationPolicy();

        // Act
        boolean result = policy.canFindAvailableForCollection(authorizedUser);

        // Assert
        assertTrue(result);
    }

    @Test
    void canFindAvailableForCollection_shouldReturnFalse_whenUserIsNotNormalUser() {
        // Arrange
        PublicationSampleAuthorizationPolicy policy = new PublicationSampleAuthorizationPolicy();

        // Act
        boolean result = policy.canFindAvailableForCollection(unauthorizedUser);

        // Assert
        assertFalse(result);
    }

    @Test
    void canAddPublicationToLibrary_shouldReturnTrue_whenUserIsNormalUser() {
        // Arrange
        PublicationSampleAuthorizationPolicy policy = new PublicationSampleAuthorizationPolicy();

        // Act
        boolean result = policy.canAddPublicationToLibrary(authorizedUser);

        // Assert
        assertTrue(result);
    }

    @Test
    void canAddPublicationToLibrary_shouldReturnFalse_whenUserIsNotNormalUser() {
        // Arrange
        PublicationSampleAuthorizationPolicy policy = new PublicationSampleAuthorizationPolicy();

        // Act
        boolean result = policy.canAddPublicationToLibrary(unauthorizedUser);

        // Assert
        assertFalse(result);
    }

    @Test
    void canAssignPublicationToCollection_shouldReturnTrue_whenUserIsNormalUser() {
        // Arrange
        PublicationSampleAuthorizationPolicy policy = new PublicationSampleAuthorizationPolicy();

        // Act
        boolean result = policy.canAssignPublicationToCollection(authorizedUser);

        // Assert
        assertTrue(result);
    }

    @Test
    void canAssignPublicationToCollection_shouldReturnFalse_whenUserIsNotNormalUser() {
        // Arrange
        PublicationSampleAuthorizationPolicy policy = new PublicationSampleAuthorizationPolicy();

        // Act
        boolean result = policy.canAssignPublicationToCollection(unauthorizedUser);

        // Assert
        assertFalse(result);
    }
}