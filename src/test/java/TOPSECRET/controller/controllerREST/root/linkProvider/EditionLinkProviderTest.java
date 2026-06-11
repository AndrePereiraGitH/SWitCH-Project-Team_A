package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.EditionAuthorizationPolicy;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Link;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link EditionLinkProvider}.
 *
 * <p>Updated for Sprint 7: {@code getLinks()} has been replaced by
 * {@code getPublicLinks()} and {@code getPrivateLinks(UserId)}.</p>
 */
class EditionLinkProviderTest {

    private final EditionAuthorizationPolicy policy = mock(EditionAuthorizationPolicy.class);
    private final IUserRepository repository = mock(IUserRepository.class);
    private final UserId userId = mock(UserId.class);

    // =========================================================
    // getPublicLinks — always available, no user needed
    // =========================================================

    @Test
    void getPublicLinks_shouldReturnTwoLinks_always() {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);

        // ACT
        List<Link> result = sut.getPublicLinks();

        // ASSERT
        assertEquals(2, result.size());
    }

    @Test
    void getPublicLinks_shouldContainFindEditionRel() {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);

        // ACT
        List<Link> result = sut.getPublicLinks();

        // ASSERT
        assertTrue(result.stream()
                .anyMatch(l -> l.getRel().value().equals("find-edition")));
    }

    @Test
    void getPublicLinks_shouldContainFindAllEditionsRel() {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);

        // ACT
        List<Link> result = sut.getPublicLinks();

        // ASSERT
        assertTrue(result.stream()
                .anyMatch(l -> l.getRel().value().equals("find-all-editions")));
    }

    @Test
    void getPublicLinks_shouldReturnNonNullHrefs() {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);

        // ACT
        List<Link> result = sut.getPublicLinks();

        // ASSERT
        result.forEach(l -> assertNotNull(l.getHref()));
    }


    // =========================================================
    // getPrivateLinks — user can create edition
    // =========================================================

    @Test
    void getCreateLinks_shouldReturnOneLink_whenUserCanCreateEdition()
            throws InstanceNotFoundException {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateEdition(user)).thenReturn(true);

        // ACT
        List<Link> result = sut.getCreateLinks(userId);

        // ASSERT
        assertEquals(1, result.size());
    }

    @Test
    void getCreateLinks_shouldContainCreateEditionRel_whenUserCanCreateEdition()
            throws InstanceNotFoundException {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateEdition(user)).thenReturn(true);

        // ACT
        List<Link> result = sut.getCreateLinks(userId);

        // ASSERT
        assertTrue(result.stream()
                .anyMatch(l -> l.getRel().value().equals("create-edition")));
    }

    @Test
    void getCreateLinks_shouldReturnNonNullHrefs_whenUserCanCreateEdition()
            throws InstanceNotFoundException {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateEdition(user)).thenReturn(true);

        // ACT
        List<Link> result = sut.getCreateLinks(userId);

        // ASSERT
        result.forEach(l -> assertNotNull(l.getHref()));
    }

    // =========================================================
    // getPrivateLinks — user cannot create edition
    // =========================================================

    @Test
    void getCreateLinks_shouldReturnEmptyList_whenUserCannotCreateEdition()
            throws InstanceNotFoundException {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateEdition(user)).thenReturn(false);

        // ACT
        List<Link> result = sut.getCreateLinks(userId);

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void getCreateLinks_shouldNotContainCreateEditionRel_whenUserCannotCreateEdition()
            throws InstanceNotFoundException {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateEdition(user)).thenReturn(false);

        // ACT
        List<Link> result = sut.getCreateLinks(userId);

        // ASSERT
        assertTrue(result.stream()
                .noneMatch(l -> l.getRel().value().equals("create-edition")));
    }

    // =========================================================
    // getPrivateLinks — error cases
    // =========================================================

    @Test
    void getCreateLinks_shouldThrowInstanceNotFoundException_whenUserDoesNotExist() {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);

        when(repository.findByID(userId)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(InstanceNotFoundException.class,
                () -> sut.getCreateLinks(userId));
    }

    @Test
    void getCreateLinks_shouldThrowNullPointerException_whenUserIdIsNull() {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);

        // ACT + ASSERT
        assertThrows(NullPointerException.class,
                () -> sut.getCreateLinks(null));
    }

    @Test
    void getLinks_shouldReturnPublicAndCreateLinks_whenUserCanCreateEdition()
            throws InstanceNotFoundException {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId))
                .thenReturn(Optional.of(user));

        when(policy.canCreateEdition(user))
                .thenReturn(true);

        // ACT
        List<Link> result = sut.getLinks(userId);

        // ASSERT
        assertEquals(3, result.size());

        assertTrue(
                result.stream()
                        .anyMatch(l -> l.getRel().value()
                                .equals("find-edition"))
        );

        assertTrue(
                result.stream()
                        .anyMatch(l -> l.getRel().value()
                                .equals("find-all-editions"))
        );

        assertTrue(
                result.stream()
                        .anyMatch(l -> l.getRel().value()
                                .equals("create-edition"))
        );
    }

    @Test
    void getLinks_shouldReturnOnlyPublicLinks_whenUserCannotCreateEdition()
            throws InstanceNotFoundException {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId))
                .thenReturn(Optional.of(user));

        when(policy.canCreateEdition(user))
                .thenReturn(false);

        // ACT
        List<Link> result = sut.getLinks(userId);

        // ASSERT
        assertEquals(2, result.size());

        assertTrue(
                result.stream()
                        .anyMatch(l -> l.getRel().value()
                                .equals("find-edition"))
        );

        assertTrue(
                result.stream()
                        .anyMatch(l -> l.getRel().value()
                                .equals("find-all-editions"))
        );

        assertFalse(
                result.stream()
                        .anyMatch(l -> l.getRel().value()
                                .equals("create-edition"))
        );
    }

    @Test
    void getLinks_shouldThrowInstanceNotFoundException_whenUserDoesNotExist() {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);

        when(repository.findByID(userId))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(
                InstanceNotFoundException.class,
                () -> sut.getLinks(userId)
        );
    }

    @Test
    void getLinks_shouldThrowNullPointerException_whenUserIdIsNull() {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);

        // ACT + ASSERT
        assertThrows(
                NullPointerException.class,
                () -> sut.getLinks(null)
        );
    }

    @Test
    void getLinks_shouldReturnAllLinks_whenUserHasAllPermissions()
            throws InstanceNotFoundException {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateEdition(user)).thenReturn(true);
        when(policy.canFindByPublisherId(user)).thenReturn(true);
        when(policy.canFindByPublicationTypeId(user)).thenReturn(true);

        // ACT
        List<Link> result = sut.getLinks(userId);

        // ASSERT
        assertEquals(5, result.size());

        assertTrue(result.stream()
                .anyMatch(l -> l.getRel().value().equals("find-edition")));

        assertTrue(result.stream()
                .anyMatch(l -> l.getRel().value().equals("find-all-editions")));

        assertTrue(result.stream()
                .anyMatch(l -> l.getRel().value().equals("create-edition")));

        assertTrue(result.stream()
                .anyMatch(l -> l.getRel().value().equals("find-by-publisher-id")));

        assertTrue(result.stream()
                .anyMatch(l -> l.getRel().value().equals("find-by-publication-type-id")));
    }
    @Test
    void getLinks_shouldReturnOnlyPublicLinks_whenUserHasNoPermissions()
            throws InstanceNotFoundException {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateEdition(user)).thenReturn(false);
        when(policy.canFindByPublisherId(user)).thenReturn(false);
        when(policy.canFindByPublicationTypeId(user)).thenReturn(false);

        // ACT
        List<Link> result = sut.getLinks(userId);

        // ASSERT
        assertEquals(2, result.size());

        assertTrue(result.stream()
                .anyMatch(l -> l.getRel().value().equals("find-edition")));

        assertTrue(result.stream()
                .anyMatch(l -> l.getRel().value().equals("find-all-editions")));

        assertFalse(result.stream()
                .anyMatch(l -> l.getRel().value().equals("create-edition")));

        assertFalse(result.stream()
                .anyMatch(l -> l.getRel().value().equals("find-by-publisher-id")));

        assertFalse(result.stream()
                .anyMatch(l -> l.getRel().value().equals("find-by-publication-type-id")));
    }

}
