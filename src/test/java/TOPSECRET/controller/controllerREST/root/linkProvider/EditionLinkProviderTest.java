package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.EditionAuthorizationPolicy;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Link;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link EditionLinkProvider}.
 *
 * <p>Validates link generation based on user authorization rules.</p>
 */
class EditionLinkProviderTest {

    private final EditionAuthorizationPolicy policy = mock(EditionAuthorizationPolicy.class);
    private final IUserRepository repository = mock(IUserRepository.class);
    private final UserId userId = mock(UserId.class);

    // =========================================================
    // USER WITH PERMISSION TO CREATE
    // =========================================================

    @Test
    void getLinks_shouldReturnTwoLinks_whenUserCanCreateEdition() {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateEdition(user)).thenReturn(true);

        // ACT
        List<Link> result = sut.getLinks(userId);

        // ASSERT
        assertEquals(2, result.size());
    }

    @Test
    void getLinks_shouldContainCreateEditionRel_whenUserCanCreateEdition() {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateEdition(user)).thenReturn(true);

        // ACT
        List<Link> result = sut.getLinks(userId);

        // ASSERT
        assertTrue(result.stream()
                .anyMatch(l -> l.getRel().value().equals("create-edition")));
    }

    @Test
    void getLinks_shouldContainFindAllEditionsRel_whenUserCanCreateEdition() {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateEdition(user)).thenReturn(true);

        // ACT
        List<Link> result = sut.getLinks(userId);

        // ASSERT
        assertTrue(result.stream()
                .anyMatch(l -> l.getRel().value().equals("find-all-editions")));
    }

    @Test
    void getLinks_shouldReturnNonNullHref_whenUserCanCreateEdition() {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateEdition(user)).thenReturn(true);

        // ACT
        List<Link> result = sut.getLinks(userId);

        // ASSERT
        result.forEach(l -> assertNotNull(l.getHref()));
    }

    // =========================================================
    // USER WITHOUT PERMISSION
    // =========================================================

    @Test
    void getLinks_shouldReturnOneLink_whenUserCannotCreateEdition() {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateEdition(user)).thenReturn(false);

        // ACT
        List<Link> result = sut.getLinks(userId);

        // ASSERT
        assertEquals(1, result.size());
    }

    @Test
    void getLinks_shouldContainFindAllEditionsRel_whenUserCannotCreateEdition() {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateEdition(user)).thenReturn(false);

        // ACT
        List<Link> result = sut.getLinks(userId);

        // ASSERT
        assertTrue(result.stream()
                .anyMatch(l -> l.getRel().value().equals("find-all-editions")));
    }

    @Test
    void getLinks_shouldNotContainCreateEditionRel_whenUserCannotCreateEdition() {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateEdition(user)).thenReturn(false);

        // ACT
        List<Link> result = sut.getLinks(userId);

        // ASSERT
        assertTrue(result.stream()
                .noneMatch(l -> l.getRel().value().equals("create-edition")));
    }

    // =========================================================
    // USER NOT FOUND
    // =========================================================

    @Test
    void getLinks_shouldThrowException_whenUserDoesNotExist() {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);

        when(repository.findByID(userId)).thenReturn(Optional.empty());

        // ACT
        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> sut.getLinks(userId)
        );

        // ASSERT
        assertEquals("User not found", ex.getMessage());
    }
}