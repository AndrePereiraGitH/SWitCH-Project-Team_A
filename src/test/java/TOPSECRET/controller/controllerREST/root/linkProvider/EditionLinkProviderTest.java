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

    @Test
    void getPublicLinks_shouldNotInteractWithRepository() {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);

        // ACT
        sut.getPublicLinks();

        // ASSERT
        verifyNoInteractions(repository);
    }

    // =========================================================
    // getPrivateLinks — user can create edition
    // =========================================================

    @Test
    void getPrivateLinks_shouldReturnOneLink_whenUserCanCreateEdition()
            throws InstanceNotFoundException {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateEdition(user)).thenReturn(true);

        // ACT
        List<Link> result = sut.getPrivateLinks(userId);

        // ASSERT
        assertEquals(1, result.size());
    }

    @Test
    void getPrivateLinks_shouldContainCreateEditionRel_whenUserCanCreateEdition()
            throws InstanceNotFoundException {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateEdition(user)).thenReturn(true);

        // ACT
        List<Link> result = sut.getPrivateLinks(userId);

        // ASSERT
        assertTrue(result.stream()
                .anyMatch(l -> l.getRel().value().equals("create-edition")));
    }

    @Test
    void getPrivateLinks_shouldReturnNonNullHrefs_whenUserCanCreateEdition()
            throws InstanceNotFoundException {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateEdition(user)).thenReturn(true);

        // ACT
        List<Link> result = sut.getPrivateLinks(userId);

        // ASSERT
        result.forEach(l -> assertNotNull(l.getHref()));
    }

    // =========================================================
    // getPrivateLinks — user cannot create edition
    // =========================================================

    @Test
    void getPrivateLinks_shouldReturnEmptyList_whenUserCannotCreateEdition()
            throws InstanceNotFoundException {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateEdition(user)).thenReturn(false);

        // ACT
        List<Link> result = sut.getPrivateLinks(userId);

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void getPrivateLinks_shouldNotContainCreateEditionRel_whenUserCannotCreateEdition()
            throws InstanceNotFoundException {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateEdition(user)).thenReturn(false);

        // ACT
        List<Link> result = sut.getPrivateLinks(userId);

        // ASSERT
        assertTrue(result.stream()
                .noneMatch(l -> l.getRel().value().equals("create-edition")));
    }

    // =========================================================
    // getPrivateLinks — error cases
    // =========================================================

    @Test
    void getPrivateLinks_shouldThrowInstanceNotFoundException_whenUserDoesNotExist() {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);

        when(repository.findByID(userId)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(InstanceNotFoundException.class,
                () -> sut.getPrivateLinks(userId));
    }

    @Test
    void getPrivateLinks_shouldThrowNullPointerException_whenUserIdIsNull() {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);

        // ACT + ASSERT
        assertThrows(NullPointerException.class,
                () -> sut.getPrivateLinks(null));
    }
}
