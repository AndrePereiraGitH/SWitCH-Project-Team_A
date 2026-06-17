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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthorLinkProviderTest {

    private final AuthorAuthorizationPolicy policy = mock(AuthorAuthorizationPolicy.class);
    private final IUserRepository repository = mock(IUserRepository.class);

    // =========================================================================
    // canCreateAuthor = true  →  3 links: create-author + find-author + find-all
    // =========================================================================

    @Test
    void getLinks_shouldReturnThreeLinks_whenUserCanCreateAuthor() throws InstanceNotFoundException {

        // ARRANGE
        AuthorLinkProvider sut = new AuthorLinkProvider(policy, repository);
        UserId userId = mock(UserId.class);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateAuthor(user)).thenReturn(true);

        // ACT
        List<Link> result = sut.getLinks(userId);

        // ASSERT
        assertEquals(3, result.size());
    }

    @Test
    void getLinks_shouldIncludeCreateAuthorLink_whenUserCanCreateAuthor() throws InstanceNotFoundException {

        // ARRANGE
        AuthorLinkProvider sut = new AuthorLinkProvider(policy, repository);
        UserId userId = mock(UserId.class);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateAuthor(user)).thenReturn(true);

        // ACT
        List<Link> result = sut.getLinks(userId);

        // ASSERT
        assertTrue(result.stream().anyMatch(l -> l.getRel().value().equals("create-author")));
    }

    @Test
    void getLinks_shouldIncludeFindAuthorLink_whenUserCanCreateAuthor() throws InstanceNotFoundException {

        // ARRANGE
        AuthorLinkProvider sut = new AuthorLinkProvider(policy, repository);
        UserId userId = mock(UserId.class);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateAuthor(user)).thenReturn(true);

        // ACT
        List<Link> result = sut.getLinks(userId);

        // ASSERT
        assertTrue(result.stream().anyMatch(l -> l.getRel().value().equals("find-author")));
    }

    @Test
    void getLinks_shouldIncludeFindAllLink_whenUserCanCreateAuthor() throws InstanceNotFoundException {

        // ARRANGE
        AuthorLinkProvider sut = new AuthorLinkProvider(policy, repository);
        UserId userId = mock(UserId.class);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateAuthor(user)).thenReturn(true);

        // ACT
        List<Link> result = sut.getLinks(userId);

        // ASSERT
        assertTrue(result.stream().anyMatch(l -> l.getRel().value().equals("find-all")));
    }

    @Test
    void getLinks_shouldReturnNonNullHrefs_whenUserCanCreateAuthor() throws InstanceNotFoundException {

        // ARRANGE
        AuthorLinkProvider sut = new AuthorLinkProvider(policy, repository);
        UserId userId = mock(UserId.class);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateAuthor(user)).thenReturn(true);

        // ACT
        List<Link> result = sut.getLinks(userId);

        // ASSERT
        result.forEach(link -> assertNotNull(link.getHref()));
    }

    // =========================================================================
    // canCreateAuthor = false  →  2 links: find-author + find-all
    // =========================================================================

    @Test
    void getLinks_shouldReturnTwoLinks_whenUserCannotCreateAuthor() throws InstanceNotFoundException {

        // ARRANGE
        AuthorLinkProvider sut = new AuthorLinkProvider(policy, repository);
        UserId userId = mock(UserId.class);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateAuthor(user)).thenReturn(false);

        // ACT
        List<Link> result = sut.getLinks(userId);

        // ASSERT
        assertEquals(2, result.size());
    }

    @Test
    void getLinks_shouldNotIncludeCreateAuthorLink_whenUserCannotCreateAuthor() throws InstanceNotFoundException {

        // ARRANGE
        AuthorLinkProvider sut = new AuthorLinkProvider(policy, repository);
        UserId userId = mock(UserId.class);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateAuthor(user)).thenReturn(false);

        // ACT
        List<Link> result = sut.getLinks(userId);

        // ASSERT
        assertTrue(result.stream().noneMatch(l -> l.getRel().value().equals("create-author")));
    }

    @Test
    void getLinks_shouldIncludeFindAuthorLink_whenUserCannotCreateAuthor() throws InstanceNotFoundException {

        // ARRANGE
        AuthorLinkProvider sut = new AuthorLinkProvider(policy, repository);
        UserId userId = mock(UserId.class);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateAuthor(user)).thenReturn(false);

        // ACT
        List<Link> result = sut.getLinks(userId);

        // ASSERT
        assertTrue(result.stream().anyMatch(l -> l.getRel().value().equals("find-author")));
    }

    @Test
    void getLinks_shouldIncludeFindAllLink_whenUserCannotCreateAuthor() throws InstanceNotFoundException {

        // ARRANGE
        AuthorLinkProvider sut = new AuthorLinkProvider(policy, repository);
        UserId userId = mock(UserId.class);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateAuthor(user)).thenReturn(false);

        // ACT
        List<Link> result = sut.getLinks(userId);

        // ASSERT
        assertTrue(result.stream().anyMatch(l -> l.getRel().value().equals("find-all")));
    }

    // =========================================================================
    // user not found
    // =========================================================================

    @Test
    void getLinks_shouldThrowException_whenUserDoesNotExist() {

        // ARRANGE
        AuthorLinkProvider sut = new AuthorLinkProvider(policy, repository);
        UserId userId = mock(UserId.class);

        when(repository.findByID(userId)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(
                InstanceNotFoundException.class,
                () -> sut.getLinks(userId)
        );
    }
}