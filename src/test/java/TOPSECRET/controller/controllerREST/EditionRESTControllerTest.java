package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.EditionLinkProvider;
import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.ValueObjects.*;
import TOPSECRET.dto.edition.request.CreateEditionDTO;
import TOPSECRET.dto.edition.response.EditionDTO;
import TOPSECRET.services.EditionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link EditionRESTController}.
 *
 * <p>Validates HTTP responses, exception handling and DTO mapping.</p>
 */
@ExtendWith(MockitoExtension.class)
class EditionRESTControllerTest {

    @Mock
    private EditionService service;

    @Mock
    private EditionLinkProvider provider;

    @Mock
    private Edition edition;

    @Mock
    private EditionId editionId;

    @Mock
    private Code code;

    @Mock
    private PhysicalAttributes physicalAttributes;

    @Mock
    private Dimensions dimensions;

    @Mock
    private PublicationTypeId publicationTypeId;

    @Mock
    private PublisherID publisherId;

    private final UserId userId = new UserId("user-1");
    private CreateEditionDTO dto;

    @BeforeEach
    void setup() {
        dto = new CreateEditionDTO(
                "1st Edition",
                null,
                "9780306406157",
                null,
                20.0, 15.0, 3.0,
                500.0, 300,
                "pub-type-1",
                "publisher-1",
                "Book",
                "en",
                "2010-01-01",
                "HARDBACK"
        );
    }

    private void setupEditionMocks() {
        when(editionId.getID()).thenReturn("edition-uuid");
        when(edition.id()).thenReturn(editionId);
        when(edition.getEditionNumber()).thenReturn("1st Edition");
        when(code.toString()).thenReturn("9780306406157");
        when(edition.getCode()).thenReturn(code);
        when(publicationTypeId.getId()).thenReturn("pub-type-1");
        when(edition.getPublicationTypeID()).thenReturn(publicationTypeId);
        when(publisherId.getId()).thenReturn("publisher-1");
        when(edition.getPublisherID()).thenReturn(publisherId);
        when(edition.getMediaFormat()).thenReturn("Book");
        when(edition.getEditionLanguage()).thenReturn(Locale.forLanguageTag("en"));
        when(edition.getEditionDate()).thenReturn(LocalDate.of(2010, 1, 1));
        when(edition.getTypeOfBinding()).thenReturn(TypeOfBinding.HARDBACK);
        when(dimensions.getHeight()).thenReturn(20.0);
        when(dimensions.getWidth()).thenReturn(15.0);
        when(dimensions.getDepth()).thenReturn(3.0);
        when(physicalAttributes.getDimensions()).thenReturn(dimensions);
        when(physicalAttributes.getWeight()).thenReturn(500.0);
        when(physicalAttributes.getNumberOfPages()).thenReturn(300);
        when(edition.getPhysicalAttributes()).thenReturn(physicalAttributes);
    }

    // =========================================================================
    // createEdition — success
    // =========================================================================

    @Test
    void createEdition_shouldReturnCreated_whenSuccess() throws Exception {

        // ARRANGE
        EditionRESTController sut = new EditionRESTController(service, provider);
        setupEditionMocks();

        when(service.createAndSave(any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any())).thenReturn(edition);
        when(provider.getLinks(any(UserId.class))).thenReturn(new ArrayList<>());

        // ACT
        ResponseEntity<EditionDTO> result = sut.createEdition(dto, userId);

        // ASSERT
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void createEdition_shouldAddLinks_whenProviderReturnsLinks() throws Exception {

        // ARRANGE
        EditionRESTController sut = new EditionRESTController(service, provider);
        setupEditionMocks();

        Link link = Link.of("/editions");
        List<Link> linkList = new ArrayList<>();
        linkList.add(link);

        when(service.createAndSave(any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any())).thenReturn(edition);
        when(provider.getLinks(any(UserId.class))).thenReturn(linkList);

        // ACT
        ResponseEntity<EditionDTO> result = sut.createEdition(dto, userId);

        // ASSERT
        assertNotNull(result.getBody());
        assertEquals(2, result.getBody().getLinks().toList().size());
    }

    // =========================================================================
    // createEdition — exceptions
    // =========================================================================

    @Test
    void createEdition_shouldReturnForbidden_whenAccessDenied() throws Exception {

        // ARRANGE
        EditionRESTController sut = new EditionRESTController(service, provider);

        when(service.createAndSave(any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any()))
                .thenThrow(new AccessDeniedException("denied"));

        // ACT
        ResponseEntity<EditionDTO> result = sut.createEdition(dto, userId);

        // ASSERT
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void createEdition_shouldReturnBadRequest_whenIllegalArgument() throws Exception {

        // ARRANGE
        EditionRESTController sut = new EditionRESTController(service, provider);

        when(service.createAndSave(any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any()))
                .thenThrow(new IllegalArgumentException());

        // ACT
        ResponseEntity<EditionDTO> result = sut.createEdition(dto, userId);

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNull(result.getBody());
    }

    @Test
    void createEdition_shouldReturnInternalServerError_whenUnexpectedException()
            throws Exception {

        // ARRANGE
        EditionRESTController sut = new EditionRESTController(service, provider);

        when(service.createAndSave(any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any()))
                .thenThrow(new RuntimeException());

        // ACT
        ResponseEntity<EditionDTO> result = sut.createEdition(dto, userId);

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNull(result.getBody());
    }
}