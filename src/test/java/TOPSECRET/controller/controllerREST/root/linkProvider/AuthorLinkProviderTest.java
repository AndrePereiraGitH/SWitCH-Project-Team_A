package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.AuthorAuthorizationPolicy;
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
 * Unit tests for {@link AuthorLinkProvider}.
 *
 * <p>Verifies the behaviour of each method, including:</p>
 * <ul>
 *     <li>{@code getPublicLinks} — always returns the {@code find-author} and
 *         {@code find-all} links, regardless of the requesting user,
 *         and never interacts with the user repository;</li>
 *     <li>{@code getPrivateLinks} — returns the {@code create-author} link
 *         when the user is authorized to create an author,
 *         returns an empty list when the user lacks permission,
 *         throws {@link InstanceNotFoundException} when the user cannot be resolved,
 *         throws {@link NullPointerException} when the given {@code userId} is {@code null};</li>
 *     <li>{@code getLinks} — aggregates public and private links for the given user,
 *         returning all three links when the user can create an author,
 *         returning only the public links when the user cannot,
 *         and propagating {@link InstanceNotFoundException} when the user cannot be resolved.</li>
 * </ul>
 */
class AuthorLinkProviderTest {

    private final AuthorAuthorizationPolicy policy = mock(AuthorAuthorizationPolicy.class);
    private final IUserRepository repository = mock(IUserRepository.class);
    private final UserId userId = mock(UserId.class);

    // =========================================================
    // getPublicLinks — always available, no user needed
    // =========================================================

    @Test
    void getPublicLinks_shouldReturnTwoLinks_always() {

        // ARRANGE
        AuthorLinkProvider sut = new AuthorLinkProvider(policy, repository);

        // ACT
        List<Link> result = sut.getPublicLinks();

        // ASSERT
        assertEquals(2, result.size());
    }

    @Test
    void getPublicLinks_shouldContainFindAuthorRel() {

        // ARRANGE
        AuthorLinkProvider sut = new AuthorLinkProvider(policy, repository);

        // ACT
        List<Link> result = sut.getPublicLinks();

        // ASSERT
        assertTrue(result.stream()
                .anyMatch(l -> l.getRel().value().equals("find-author")));
    }

    @Test
    void getPublicLinks_shouldContainFindAllRel() {

        // ARRANGE
        AuthorLinkProvider sut = new AuthorLinkProvider(policy, repository);

        // ACT
        List<Link> result = sut.getPublicLinks();

        // ASSERT
        assertTrue(result.stream()
                .anyMatch(l -> l.getRel().value().equals("find-all")));
    }

    @Test
    void getPublicLinks_shouldReturnNonNullHrefs() {

        // ARRANGE
        AuthorLinkProvider sut = new AuthorLinkProvider(policy, repository);

        // ACT
        List<Link> result = sut.getPublicLinks();

        // ASSERT
        result.forEach(l -> assertNotNull(l.getHref()));
    }

    @Test
    void getPublicLinks_shouldNotInteractWithRepository() {

        // ARRANGE
        AuthorLinkProvider sut = new AuthorLinkProvider(policy, repository);

        // ACT
        sut.getPublicLinks();

        // ASSERT
        verifyNoInteractions(repository);
    }

    // =========================================================
    // getPrivateLinks — user can create author
    // =========================================================

    @Test
    void getPrivateLinks_shouldReturnOneLink_whenUserCanCreateAuthor()
            throws InstanceNotFoundException {

        // ARRANGE
        AuthorLinkProvider sut = new AuthorLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateAuthor(user)).thenReturn(true);

        // ACT
        List<Link> result = sut.getPrivateLinks(userId);

        // ASSERT
        assertEquals(1, result.size());
    }

    @Test
    void getPrivateLinks_shouldContainCreateAuthorRel_whenUserCanCreateAuthor()
            throws InstanceNotFoundException {

        // ARRANGE
        AuthorLinkProvider sut = new AuthorLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateAuthor(user)).thenReturn(true);

        // ACT
        List<Link> result = sut.getPrivateLinks(userId);

        // ASSERT
        assertTrue(result.stream()
                .anyMatch(l -> l.getRel().value().equals("create-author")));
    }

    @Test
    void getPrivateLinks_shouldReturnNonNullHrefs_whenUserCanCreateAuthor()
            throws InstanceNotFoundException {

        // ARRANGE
        AuthorLinkProvider sut = new AuthorLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateAuthor(user)).thenReturn(true);

        // ACT
        List<Link> result = sut.getPrivateLinks(userId);

        // ASSERT
        result.forEach(l -> assertNotNull(l.getHref()));
    }

    // =========================================================
    // getPrivateLinks — user cannot create author
    // =========================================================

    @Test
    void getPrivateLinks_shouldReturnEmptyList_whenUserCannotCreateAuthor()
            throws InstanceNotFoundException {

        // ARRANGE
        AuthorLinkProvider sut = new AuthorLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateAuthor(user)).thenReturn(false);

        // ACT
        List<Link> result = sut.getPrivateLinks(userId);

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void getPrivateLinks_shouldNotContainCreateAuthorRel_whenUserCannotCreateAuthor()
            throws InstanceNotFoundException {

        // ARRANGE
        AuthorLinkProvider sut = new AuthorLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateAuthor(user)).thenReturn(false);

        // ACT
        List<Link> result = sut.getPrivateLinks(userId);

        // ASSERT
        assertTrue(result.stream()
                .noneMatch(l -> l.getRel().value().equals("create-author")));
    }

    // =========================================================
    // getLinks — aggregates public and private
    // =========================================================

    @Test
    void getLinks_shouldReturnPublicAndPrivateLinks_whenUserCanCreateAuthor()
            throws InstanceNotFoundException {

        // ARRANGE
        AuthorLinkProvider sut = new AuthorLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateAuthor(user)).thenReturn(true);

        // ACT
        List<Link> result = sut.getLinks(userId);

        // ASSERT
        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(l -> l.getRel().value().equals("find-author")));
        assertTrue(result.stream().anyMatch(l -> l.getRel().value().equals("find-all")));
        assertTrue(result.stream().anyMatch(l -> l.getRel().value().equals("create-author")));
    }

    @Test
    void getLinks_shouldReturnPublicLinksOnly_whenUserCannotCreateAuthor()
            throws InstanceNotFoundException {

        // ARRANGE
        AuthorLinkProvider sut = new AuthorLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateAuthor(user)).thenReturn(false);

        // ACT
        List<Link> result = sut.getLinks(userId);

        // ASSERT
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(l -> l.getRel().value().equals("find-author")));
        assertTrue(result.stream().anyMatch(l -> l.getRel().value().equals("find-all")));
        assertTrue(result.stream().noneMatch(l -> l.getRel().value().equals("create-author")));
    }

    @Test
    void getLinks_shouldThrowInstanceNotFoundException_whenUserDoesNotExist() {

        // ARRANGE
        AuthorLinkProvider sut = new AuthorLinkProvider(policy, repository);
        when(repository.findByID(userId)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(InstanceNotFoundException.class,
                () -> sut.getLinks(userId));
    }

    // =========================================================
    // getPrivateLinks — error cases
    // =========================================================

    @Test
    void getPrivateLinks_shouldThrowInstanceNotFoundException_whenUserDoesNotExist() {

        // ARRANGE
        AuthorLinkProvider sut = new AuthorLinkProvider(policy, repository);
        when(repository.findByID(userId)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(InstanceNotFoundException.class,
                () -> sut.getPrivateLinks(userId));
    }

    @Test
    void getPrivateLinks_shouldThrowNullPointerException_whenUserIdIsNull() {

        // ARRANGE
        AuthorLinkProvider sut = new AuthorLinkProvider(policy, repository);

        // ACT + ASSERT
        assertThrows(NullPointerException.class,
                () -> sut.getPrivateLinks(null));
    }
}