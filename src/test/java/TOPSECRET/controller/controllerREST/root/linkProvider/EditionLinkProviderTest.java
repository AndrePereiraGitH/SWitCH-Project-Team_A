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
    private final UserId userId = new UserId("user-1");

    @Test
    void getLinks_shouldReturnLink_whenUserCanCreateEdition() {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateEdition(user)).thenReturn(true);

        // ACT
        List<Link> result = sut.getLinks(userId);

        // ASSERT
        assertEquals(1, result.size());
        assertEquals("create-edition", result.get(0).getRel().value());
    }

    @Test
    void getLinks_shouldReturnHref_whenUserCanCreateEdition() {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateEdition(user)).thenReturn(true);

        // ACT
        List<Link> result = sut.getLinks(userId);

        // ASSERT
        assertFalse(result.isEmpty());
        assertNotNull(result.get(0).getHref());
    }

    @Test
    void getLinks_shouldReturnEmptyList_whenUserCannotCreateEdition() {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateEdition(user)).thenReturn(false);

        // ACT
        List<Link> result = sut.getLinks(userId);

        // ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

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

    @Test
    void getLinks_shouldReturnNonNullList_whenUserCannotCreateEdition() {

        // ARRANGE
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateEdition(user)).thenReturn(false);

        // ACT
        List<Link> result = sut.getLinks(userId);

        // ASSERT
        assertNotNull(result);
    }
}