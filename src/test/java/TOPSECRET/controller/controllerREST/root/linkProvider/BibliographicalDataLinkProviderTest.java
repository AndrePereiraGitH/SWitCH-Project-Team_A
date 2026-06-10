package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.root.authorizationPolicy.BibliographicalDataAuthorizationPolicy;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BibliographicalDataLinkProviderTest {

    @Mock
    BibliographicalDataAuthorizationPolicy authorizationPolicy;

    @Mock
    IUserRepository userRepository;

    @InjectMocks
    BibliographicalDataLinkProvider provider;

    @Test
    void shouldReturnOnlyPublicLinksForBibliographicalDataWhenUserIsNotRegistered(){
        // Act
        List<Link> result = provider.getPublicLinksForBibliographicalData();

        // Assert
        assertEquals(2, result.size());

        assertTrue(result.stream()
                .anyMatch(link -> link.getRel().value()
                        .equals("get-bibliographical-data")));

        assertTrue(result.stream()
                .anyMatch(link -> link.getRel().value()
                        .equals("get-bibliographical-data-by-id")));

        assertFalse(result.stream()
                .anyMatch(link -> link.getRel().value()
                        .equals("create-bibliographical-data")));
    }

    @Test
    void shouldReturnCreateLinkWhenUserCanCreateBibliographicalData() {
        // Arrange
        UserId userId = new UserId("user-1");
        User user = mock(User.class);

        when(userRepository.findByID(userId)).thenReturn(Optional.of(user));
        when(authorizationPolicy.canCreateBibliographicalData(user)).thenReturn(true);

        // Act
        List<Link> result = provider.getRegisteredUserLinksForBibliographicalData(userId);

        // Assert
        assertEquals(3, result.size());

        assertTrue(result.stream()
                .anyMatch(link -> link.getRel().value()
                        .equals("get-bibliographical-data")));

        assertTrue(result.stream()
                .anyMatch(link -> link.getRel().value()
                        .equals("get-bibliographical-data-by-id")));

        assertTrue(result.stream()
                .anyMatch(link -> link.getRel().value()
                        .equals("create-bibliographical-data")));
    }

}