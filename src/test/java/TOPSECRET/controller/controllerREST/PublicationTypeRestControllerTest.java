package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.PublicationTypeLinkProvider;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.PublicationType.PublicationTypeMapper;
import TOPSECRET.dto.PublicationType.request.CreatePublicationTypeDTO;
import TOPSECRET.dto.PublicationType.response.PublicationTypeResponseDTO;
import TOPSECRET.services.PublicationTypeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublicationTypeRestControllerTest {

    @Mock
    private PublicationTypeService service;

    @Mock
    private PublicationTypeLinkProvider linkProvider;

    @Mock
    private CreatePublicationTypeDTO createDTO;

    @InjectMocks
    private PublicationTypeRestController controller;

    private UserId userId;

    @BeforeEach
    void setup() {
        userId = mock(UserId.class);
    }

    // =========================================================
    // CREATE SUCCESS
    // =========================================================

    @Test
    void shouldCreatePublicationTypeSuccessfully()
            throws Exception {

        // ARRANGE
        String format = "Book";

        PublicationType publicationType = mock(PublicationType.class);
        PublicationTypeResponseDTO responseDTO = mock(PublicationTypeResponseDTO.class);
        Link link = mock(Link.class);

        when(createDTO.mediaFormat()).thenReturn(format);
        when(service.createAndSave(format, userId)).thenReturn(publicationType);
        when(linkProvider.getLinksForPublicationType(userId)).thenReturn(List.of(link));

        try (MockedStatic<PublicationTypeMapper> mapper =
                     mockStatic(PublicationTypeMapper.class)) {

            mapper.when(() ->
                            PublicationTypeMapper.toDTO(publicationType))
                    .thenReturn(responseDTO);

            // ACT
            ResponseEntity<PublicationTypeResponseDTO> response =
                    controller.createPublicationType(createDTO, userId);

            // ASSERT
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals(responseDTO, response.getBody());
        }
    }

    // =========================================================
    // CREATE — LINKS ADDED TO RESPONSE DTO
    // =========================================================

    @Test
    void shouldAddLinksToResponseDTO()
            throws Exception {

        // ARRANGE
        String format = "Book";

        PublicationType publicationType = mock(PublicationType.class);

        PublicationTypeResponseDTO responseDTO =
                new PublicationTypeResponseDTO("1", "Book");

        Link providerLink = Link.of(
                "/publication-types",
                "create-publication-type"
        );

        when(createDTO.mediaFormat()).thenReturn(format);
        when(service.createAndSave(format, userId)).thenReturn(publicationType);
        when(linkProvider.getLinksForPublicationType(userId))
                .thenReturn(List.of(providerLink));

        try (MockedStatic<PublicationTypeMapper> mapper =
                     mockStatic(PublicationTypeMapper.class)) {

            mapper.when(() ->
                            PublicationTypeMapper.toDTO(publicationType))
                    .thenReturn(responseDTO);

            // ACT
            ResponseEntity<PublicationTypeResponseDTO> response =
                    controller.createPublicationType(createDTO, userId);

            // ASSERT
            assertNotNull(response.getBody());
            assertEquals(
                    2,
                    response.getBody().getLinks().toList().size()
            );
        }
    }

    // =========================================================
    // CREATE — LINK DO PROVIDER PRESENTE E COM REL CORRETO
    // =========================================================

    @Test
    void shouldAddProviderLinkWithCorrectRelToResponseDTO()
            throws Exception {

        // ARRANGE
        String format = "Book";

        PublicationType publicationType = mock(PublicationType.class);

        PublicationTypeResponseDTO responseDTO =
                new PublicationTypeResponseDTO("1", "Book");

        Link providerLink = Link.of(
                "/publication-types",
                "create-publication-type"
        );

        when(createDTO.mediaFormat()).thenReturn(format);
        when(service.createAndSave(format, userId)).thenReturn(publicationType);
        when(linkProvider.getLinksForPublicationType(userId))
                .thenReturn(List.of(providerLink));

        try (MockedStatic<PublicationTypeMapper> mapper =
                     mockStatic(PublicationTypeMapper.class)) {

            mapper.when(() ->
                            PublicationTypeMapper.toDTO(publicationType))
                    .thenReturn(responseDTO);

            // ACT
            ResponseEntity<PublicationTypeResponseDTO> response =
                    controller.createPublicationType(createDTO, userId);

            // ASSERT — o link do provider deve estar presente na lista
            assertNotNull(response.getBody());

            boolean hasProviderLink = response.getBody()
                    .getLinks()
                    .toList()
                    .stream()
                    .anyMatch(l -> l.getRel().value()
                            .equals("create-publication-type"));

            assertTrue(hasProviderLink);
        }
    }

    // =========================================================
    // CREATE BAD REQUEST
    // =========================================================

    @Test
    void shouldReturnBadRequestWhenIllegalArgumentExceptionOccurs()
            throws Exception {

        // ARRANGE
        String format = "Book";

        when(createDTO.mediaFormat()).thenReturn(format);
        when(service.createAndSave(format, userId))
                .thenThrow(new IllegalArgumentException());

        // ACT
        ResponseEntity<PublicationTypeResponseDTO> response =
                controller.createPublicationType(createDTO, userId);

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // =========================================================
    // CREATE CONFLICT
    // =========================================================

    @Test
    void shouldReturnConflictWhenPublicationTypeAlreadyExists()
            throws Exception {

        // ARRANGE
        String format = "Book";

        when(createDTO.mediaFormat()).thenReturn(format);
        when(service.createAndSave(format, userId))
                .thenThrow(new InstanceAlreadyExistsException());

        // ACT
        ResponseEntity<PublicationTypeResponseDTO> response =
                controller.createPublicationType(createDTO, userId);

        // ASSERT
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    // =========================================================
    // CREATE FORBIDDEN
    // =========================================================

    @Test
    void shouldReturnForbiddenWhenAccessDeniedOccurs()
            throws Exception {

        // ARRANGE
        String format = "Book";

        when(createDTO.mediaFormat()).thenReturn(format);
        when(service.createAndSave(format, userId))
                .thenThrow(new AccessDeniedException("Denied"));

        // ACT
        ResponseEntity<PublicationTypeResponseDTO> response =
                controller.createPublicationType(createDTO, userId);

        // ASSERT
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    // =========================================================
    // CREATE INTERNAL SERVER ERROR
    // =========================================================

    @Test
    void shouldReturnInternalServerErrorOnCreateWhenUnexpectedExceptionOccurs()
            throws Exception {

        // ARRANGE
        String format = "Book";

        when(createDTO.mediaFormat()).thenReturn(format);
        when(service.createAndSave(format, userId))
                .thenThrow(new RuntimeException());

        // ACT
        ResponseEntity<PublicationTypeResponseDTO> response =
                controller.createPublicationType(createDTO, userId);

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // =========================================================
    // GET ALL SUCCESS
    // =========================================================

    @Test
    void shouldReturnAllPublicationTypesSuccessfully() {

        // ARRANGE
        PublicationType pt1 = mock(PublicationType.class);
        PublicationType pt2 = mock(PublicationType.class);

        PublicationTypeId id1 = mock(PublicationTypeId.class);
        PublicationTypeId id2 = mock(PublicationTypeId.class);

        when(pt1.id()).thenReturn(id1);
        when(pt2.id()).thenReturn(id2);

        PublicationTypeResponseDTO dto1 =
                new PublicationTypeResponseDTO("1", "Book");
        PublicationTypeResponseDTO dto2 =
                new PublicationTypeResponseDTO("2", "Magazine");

        List<PublicationType> publicationTypes = List.of(pt1, pt2);

        when(service.findAll()).thenReturn(publicationTypes);

        try (MockedStatic<PublicationTypeMapper> mapper =
                     mockStatic(PublicationTypeMapper.class)) {

            mapper.when(() -> PublicationTypeMapper.toDTO(pt1)).thenReturn(dto1);
            mapper.when(() -> PublicationTypeMapper.toDTO(pt2)).thenReturn(dto2);

            // ACT
            ResponseEntity<List<PublicationTypeResponseDTO>> response =
                    controller.getAllPublicationTypes();

            // ASSERT
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(2, response.getBody().size());
            assertTrue(response.getBody().containsAll(List.of(dto1, dto2)));
        }
    }

    // =========================================================
    // GET ALL NO CONTENT
    // =========================================================

    @Test
    void shouldReturnNoContentWhenNoPublicationTypesExist() {

        // ARRANGE
        when(service.findAll()).thenReturn(List.of());

        // ACT
        ResponseEntity<List<PublicationTypeResponseDTO>> response =
                controller.getAllPublicationTypes();

        // ASSERT
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    // =========================================================
    // GET ALL BAD REQUEST
    // =========================================================

    @Test
    void shouldReturnBadRequestWhenGetAllThrowsIllegalArgumentException() {

        // ARRANGE
        when(service.findAll()).thenThrow(new IllegalArgumentException());

        // ACT
        ResponseEntity<List<PublicationTypeResponseDTO>> response =
                controller.getAllPublicationTypes();

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // =========================================================
    // GET ALL INTERNAL SERVER ERROR
    // =========================================================

    @Test
    void shouldReturnInternalServerErrorWhenGetAllThrowsUnexpectedException() {

        // ARRANGE
        when(service.findAll()).thenThrow(new RuntimeException());

        // ACT
        ResponseEntity<List<PublicationTypeResponseDTO>> response =
                controller.getAllPublicationTypes();

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // =========================================================
    // GET ALL — SELF LINK ADICIONADO A CADA DTO
    // =========================================================

    @Test
    void shouldAddSelfLinkToEachDTOWhenGettingAll() {

        // ARRANGE
        PublicationType pt1 = mock(PublicationType.class);

        PublicationTypeId id1 = mock(PublicationTypeId.class);
        when(pt1.id()).thenReturn(id1);

        PublicationTypeResponseDTO dto1 =
                new PublicationTypeResponseDTO("1", "Book");

        when(service.findAll()).thenReturn(List.of(pt1));

        try (MockedStatic<PublicationTypeMapper> mapper =
                     mockStatic(PublicationTypeMapper.class)) {

            mapper.when(() -> PublicationTypeMapper.toDTO(pt1)).thenReturn(dto1);

            // ACT
            controller.getAllPublicationTypes();

            // ASSERT
            assertFalse(dto1.getLinks().isEmpty());
            assertTrue(
                    dto1.getLinks()
                            .toList()
                            .stream()
                            .anyMatch(l -> l.getRel().value().equals("self"))
            );
        }
    }

    // =========================================================
    // GET BY ID SUCCESS
    // =========================================================

    @Test
    void shouldReturnPublicationTypeByIdSuccessfully() {

        // ARRANGE
        PublicationTypeId id = mock(PublicationTypeId.class);
        PublicationType publicationType = mock(PublicationType.class);
        PublicationTypeResponseDTO responseDTO = mock(PublicationTypeResponseDTO.class);

        when(service.findById(id)).thenReturn(Optional.of(publicationType));

        try (MockedStatic<PublicationTypeMapper> mapper =
                     mockStatic(PublicationTypeMapper.class)) {

            mapper.when(() ->
                            PublicationTypeMapper.toDTO(publicationType))
                    .thenReturn(responseDTO);

            // ACT
            ResponseEntity<PublicationTypeResponseDTO> response =
                    controller.getPublicationTypeId(id);

            // ASSERT
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(responseDTO, response.getBody());
        }
    }

    // =========================================================
    // GET BY ID NOT FOUND
    // =========================================================

    @Test
    void shouldReturnNotFoundWhenPublicationTypeDoesNotExist() {

        // ARRANGE
        PublicationTypeId id = mock(PublicationTypeId.class);

        when(service.findById(id)).thenReturn(Optional.empty());

        // ACT
        ResponseEntity<PublicationTypeResponseDTO> response =
                controller.getPublicationTypeId(id);

        // ASSERT
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // =========================================================
    // GET BY ID INTERNAL SERVER ERROR
    // =========================================================

    @Test
    void shouldReturnInternalServerErrorWhenGetByIdThrowsUnexpectedException() {

        // ARRANGE
        PublicationTypeId id = mock(PublicationTypeId.class);

        when(service.findById(id)).thenThrow(new RuntimeException());

        // ACT
        ResponseEntity<PublicationTypeResponseDTO> response =
                controller.getPublicationTypeId(id);

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // =========================================================
    // GET BY ID — LINKS COM RELS CORRETOS
    // =========================================================

    @Test
    void shouldAddSelfRelAndGetAllRelLinksWhenGettingById() {

        // ARRANGE
        PublicationTypeId id = mock(PublicationTypeId.class);
        PublicationType publicationType = mock(PublicationType.class);

        PublicationTypeResponseDTO responseDTO =
                new PublicationTypeResponseDTO("1", "Book");

        when(service.findById(id)).thenReturn(Optional.of(publicationType));

        try (MockedStatic<PublicationTypeMapper> mapper =
                     mockStatic(PublicationTypeMapper.class)) {

            mapper.when(() ->
                            PublicationTypeMapper.toDTO(publicationType))
                    .thenReturn(responseDTO);

            // ACT
            controller.getPublicationTypeId(id);

            // ASSERT
            List<Link> links = responseDTO.getLinks().toList();
            assertEquals(2, links.size());

            assertTrue(links.stream()
                    .anyMatch(l -> l.getRel().value().equals("self")));

            assertTrue(links.stream()
                    .anyMatch(l -> l.getRel().value()
                            .equals("get-all-publicationTypes")));
        }
    }
}