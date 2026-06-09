package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.CityAuthorizationPolicy;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.hateoas.Link;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CityLinkProvider}.
 *
 * <p>Validates that HATEOAS links are generated according to
 * the authenticated user's permissions.</p>
 * <ul>
 *   <li><b>Public links</b> — verify that links always available regardless of permissions.</li>
 *   <li><b>Conditional links</b> — verify that links are included or omitted based on
 *       the user's authorization and existence.</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
class CityLinkProviderTest {

    @Mock
    private CityAuthorizationPolicy doublePolicy;

    @Mock
    private IUserRepository doubleRepository;

    @Mock
    private UserId doubleUserId;

    @Mock
    private User doubleUser;

    @Test
    void getPublicLinks_shouldReturnCitiesLink() {

        // ARRANGE
        CityLinkProvider sut = new CityLinkProvider(doublePolicy, doubleRepository);

        // ACT
        List<Link> result = sut.getPublicLinks();

        // ASSERT
        assertEquals(1, result.size());
        assertTrue(result.get(0).getRel().value().equals("cities"));
    }

    @Test
    void getLinks_shouldReturnCreateCityLink_whenUserIsAdmin() throws InstanceNotFoundException {

        // ARRANGE
        CityLinkProvider sut = new CityLinkProvider(doublePolicy, doubleRepository);

        when(doubleRepository.findByID(doubleUserId)).thenReturn(Optional.of(doubleUser));
        when(doublePolicy.canCreateCity(doubleUser)).thenReturn(true);

        // ACT
        List<Link> result = sut.getLinks(doubleUserId);

        // ASSERT
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(l -> l.getRel().value().equals("cities")));
        assertTrue(result.stream().anyMatch(l -> l.getRel().value().equals("create-city")));
    }

    @Test
    void getLinks_shouldNotReturnCreateCityLink_whenUserIsNotAdmin() throws InstanceNotFoundException {

        // ARRANGE
        CityLinkProvider sut = new CityLinkProvider(doublePolicy, doubleRepository);

        when(doubleRepository.findByID(doubleUserId)).thenReturn(Optional.of(doubleUser));
        when(doublePolicy.canCreateCity(doubleUser)).thenReturn(false);

        // ACT
        List<Link> result = sut.getLinks(doubleUserId);

        // ASSERT
        assertEquals(1, result.size());
        assertTrue(result.stream().anyMatch(l -> l.getRel().value().equals("cities")));
        assertFalse(result.stream().anyMatch(l -> l.getRel().value().equals("create-city")));
    }

    @Test
    void getLinks_shouldThrowIllegalArgument_whenUserNotFound() {

        // ARRANGE
        CityLinkProvider sut = new CityLinkProvider(doublePolicy, doubleRepository);

        when(doubleRepository.findByID(doubleUserId)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(InstanceNotFoundException.class,
                () -> sut.getLinks(doubleUserId));
    }
}