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

class EditionLinkProviderTest {

    private final EditionAuthorizationPolicy policy = mock(EditionAuthorizationPolicy.class);
    private final IUserRepository repository = mock(IUserRepository.class);
    private final UserId userId = new UserId("user-1");

    @Test
    void getLinks_shouldReturnTwoLinks_whenUserCanCreateEdition() {
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateEdition(user)).thenReturn(true);

        List<Link> result = sut.getLinks(userId);

        assertNotNull(result);
        assertEquals(2, result.size());

        assertTrue(result.stream()
                .allMatch(link -> link.getHref() != null && !link.getHref().isBlank()));

        verify(repository).findByID(userId);
        verify(policy).canCreateEdition(user);
    }

    @Test
    void getLinks_shouldReturnOneLink_whenUserCannotCreateEdition() {
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateEdition(user)).thenReturn(false);

        List<Link> result = sut.getLinks(userId);

        assertNotNull(result);
        assertEquals(1, result.size());

        assertTrue(result.get(0).getHref() != null && !result.get(0).getHref().isBlank());

        verify(repository).findByID(userId);
        verify(policy).canCreateEdition(user);
    }

    @Test
    void getLinks_shouldThrowException_whenUserDoesNotExist() {
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);

        when(repository.findByID(userId)).thenReturn(Optional.empty());

        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> sut.getLinks(userId)
        );

        assertEquals("User not found", ex.getMessage());

        verify(repository).findByID(userId);
        verifyNoInteractions(policy);
    }

    @Test
    void getLinks_shouldReturnNonNullList_whenUserCanCreateEdition() {
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateEdition(user)).thenReturn(true);

        List<Link> result = sut.getLinks(userId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void getLinks_shouldReturnNonNullList_whenUserCannotCreateEdition() {
        EditionLinkProvider sut = new EditionLinkProvider(policy, repository);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canCreateEdition(user)).thenReturn(false);

        List<Link> result = sut.getLinks(userId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}