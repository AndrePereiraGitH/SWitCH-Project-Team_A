package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.domain.ValueObjects.UserId;

import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Link;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link PublicationTypeLinkProvider}.
 */
class PublicationTypeLinkProviderTest {

    @Test
    void shouldReturnPublicationTypeLinks() {

        // ARRANGE
        PublicationTypeLinkProvider provider =
                new PublicationTypeLinkProvider();

        UserId userId = mock(UserId.class);

        // ACT
        List<Link> links =
                provider.getLinksForPublicationType(userId);

        // ASSERT
        assertNotNull(links);

        assertFalse(links.isEmpty());

        assertEquals(1, links.size());

        assertEquals(
                "create-publication-type",
                links.get(0).getRel().value()
        );
    }
}