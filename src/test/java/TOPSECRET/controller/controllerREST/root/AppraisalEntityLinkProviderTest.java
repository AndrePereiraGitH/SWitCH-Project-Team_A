package TOPSECRET.controller.controllerREST.root;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.AppraisalEntityAuthorizationPolicy;
import TOPSECRET.controller.controllerREST.root.linkProvider.AppraisalEntityLinkProvider;
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
 * Unit tests for {@link AppraisalEntityLinkProvider}.
 *
 * <p>Validates HATEOAS link generation according to user permissions
 * and user existence.</p>
 */
class AppraisalEntityLinkProviderTest {

    @Test
    void shouldReturnOneLinkWhenUserCanRegisterAppraisalEntity() {

        // ARRANGE
        AppraisalEntityAuthorizationPolicy policy = mock(AppraisalEntityAuthorizationPolicy.class);

        IUserRepository repository = mock(IUserRepository.class);

        AppraisalEntityLinkProvider sut =
                new AppraisalEntityLinkProvider(
                        policy,
                        repository
                );

        UserId userId = mock(UserId.class);

        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canRegisterAppraisalEntity(user)).thenReturn(true);

        // ACT
        List<Link> result = sut.getLinks(userId);

        // ASSERT
        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnRegisterAppraisalEntityRelationWhenUserCanRegister() {

        // ARRANGE
        AppraisalEntityAuthorizationPolicy policy = mock(AppraisalEntityAuthorizationPolicy.class);

        IUserRepository repository = mock(IUserRepository.class);

        AppraisalEntityLinkProvider sut =
                new AppraisalEntityLinkProvider(
                        policy,
                        repository
                );

        UserId userId = mock(UserId.class);

        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canRegisterAppraisalEntity(user)).thenReturn(true);

        // ACT
        List<Link> result = sut.getLinks(userId);

        // ASSERT
        assertEquals("register-appraisal-entity", result.get(0).getRel().value());
    }

    @Test
    void shouldReturnEmptyListWhenUserCannotRegisterAppraisalEntity() {

        // ARRANGE
        AppraisalEntityAuthorizationPolicy policy = mock(AppraisalEntityAuthorizationPolicy.class);

        IUserRepository repository = mock(IUserRepository.class);

        AppraisalEntityLinkProvider sut =
                new AppraisalEntityLinkProvider(
                        policy,
                        repository
                );

        UserId userId = mock(UserId.class);

        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));

        when(policy.canRegisterAppraisalEntity(user)).thenReturn(false);

        // ACT
        List<Link> result = sut.getLinks(userId);

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        // ARRANGE
        AppraisalEntityAuthorizationPolicy policy = mock(AppraisalEntityAuthorizationPolicy.class);

        IUserRepository repository = mock(IUserRepository.class);

        AppraisalEntityLinkProvider sut =
                new AppraisalEntityLinkProvider(
                        policy,
                        repository
                );

        UserId userId = mock(UserId.class);

        when(repository.findByID(userId)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.getLinks(userId)
        );
    }

    @Test
    void shouldThrowExceptionWithCorrectMessageWhenUserDoesNotExist() {

        // ARRANGE
        AppraisalEntityAuthorizationPolicy policy = mock(AppraisalEntityAuthorizationPolicy.class);

        IUserRepository repository = mock(IUserRepository.class);

        AppraisalEntityLinkProvider sut =
                new AppraisalEntityLinkProvider(
                        policy,
                        repository
                );

        UserId userId = mock(UserId.class);

        when(repository.findByID(userId)).thenReturn(Optional.empty());

        // ACT
        IllegalArgumentException result =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> sut.getLinks(userId)
                );

        // ASSERT
        assertEquals("User not found", result.getMessage());
    }
}