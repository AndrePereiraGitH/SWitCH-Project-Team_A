package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.AppraisalEntityAuthorizationPolicy;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AppraisalEntityLinkProvider}.
 *
 * <p>Validates HATEOAS link generation for both admin and public contexts,
 * according to user permissions and user existence.</p>
 */
@ExtendWith(MockitoExtension.class)
class AppraisalEntityLinkProviderTest {

    @Mock
    private AppraisalEntityAuthorizationPolicy policy;
    @Mock
    private IUserRepository repository;

    @InjectMocks
    private AppraisalEntityLinkProvider sut;

    // -------------------------------------------------------------------------
    // getAdminLinks
    // -------------------------------------------------------------------------

    @Test
    void getAdminLinks_shouldReturnOneLink_whenUserCanRegisterAppraisalEntity() {
        // ARRANGE
        UserId userId = mock(UserId.class);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canRegisterAppraisalEntity(user)).thenReturn(true);

        // ACT
        List<Link> result = sut.getAdminLinks(userId);

        // ASSERT
        assertEquals(1, result.size());
    }

    @Test
    void getAdminLinks_shouldReturnRegisterRelation_whenUserCanRegisterAppraisalEntity() {
        // ARRANGE
        UserId userId = mock(UserId.class);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canRegisterAppraisalEntity(user)).thenReturn(true);

        // ACT
        List<Link> result = sut.getAdminLinks(userId);

        // ASSERT
        assertEquals("register-appraisal-entity", result.get(0).getRel().value());
    }

    @Test
    void getAdminLinks_shouldReturnEmptyList_whenUserCannotRegisterAppraisalEntity() {
        // ARRANGE
        UserId userId = mock(UserId.class);
        User user = mock(User.class);

        when(repository.findByID(userId)).thenReturn(Optional.of(user));
        when(policy.canRegisterAppraisalEntity(user)).thenReturn(false);

        // ACT
        List<Link> result = sut.getAdminLinks(userId);

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void getAdminLinks_shouldThrowIllegalArgumentException_whenUserDoesNotExist() {
        // ARRANGE
        UserId userId = mock(UserId.class);

        when(repository.findByID(userId)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(
                IllegalArgumentException.class,
                () -> sut.getAdminLinks(userId)
        );
    }

    @Test
    void getAdminLinks_shouldIncludeUserIdInExceptionMessage_whenUserDoesNotExist() {
        // ARRANGE
        UserId userId = mock(UserId.class);
        when(userId.toString()).thenReturn("user-999");
        when(repository.findByID(userId)).thenReturn(Optional.empty());

        // ACT
        IllegalArgumentException result = assertThrows(
                IllegalArgumentException.class,
                () -> sut.getAdminLinks(userId)
        );

        // ASSERT
        assertTrue(result.getMessage().contains("user-999"));
    }

    // -------------------------------------------------------------------------
    // getPublicLinks
    // -------------------------------------------------------------------------

    @Test
    void getPublicLinks_shouldReturnTwoLinks() {
        // ACT
        List<Link> result = sut.getPublicLinks();

        // ASSERT
        assertEquals(2, result.size());
    }

    @Test
    void getPublicLinks_shouldContainFindByIdRelation() {
        // ACT
        List<Link> result = sut.getPublicLinks();

        // ASSERT
        assertTrue(result.stream().anyMatch(l -> l.getRel().value().equals("find-by-id")));
    }

    @Test
    void getPublicLinks_shouldContainFindAllRelation() {
        // ACT
        List<Link> result = sut.getPublicLinks();

        // ASSERT
        assertTrue(result.stream().anyMatch(l -> l.getRel().value().equals("find-all")));
    }

    @Test
    void getPublicLinks_shouldNeverReturnNull() {
        // ACT
        List<Link> result = sut.getPublicLinks();

        // ASSERT
        assertNotNull(result);
    }
}