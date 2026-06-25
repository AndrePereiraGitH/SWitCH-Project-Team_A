package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.PublicationTypeLinkProvider;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.PublicationType.PublicationTypeMapper;
import TOPSECRET.dto.PublicationType.request.CreatePublicationTypeDTO;
import TOPSECRET.dto.PublicationType.response.PublicationTypeResponseDTO;
import TOPSECRET.services.PublicationTypeService;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.hateoas.Link;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;

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

    // =========================================================
    // CREATE SUCCESS
    // =========================================================

    @Test
    void shouldCreatePublicationTypeSuccessfully() throws Exception {

        // ARRANGE
        String userIdStr = "user-001";
        String format = "Book";

        UserId userId = mock(UserId.class);

        PublicationType publicationType = mock(PublicationType.class);
        PublicationTypeId publicationTypeId = mock(PublicationTypeId.class);

        when(publicationType.id()).thenReturn(publicationTypeId);
        when(publicationTypeId.toString()).thenReturn("pt-001");

        PublicationTypeResponseDTO responseDTO =
                new PublicationTypeResponseDTO("pt-001", "Book");

        when(createDTO.mediaFormat()).thenReturn(format);
        when(service.createAndSave(format, userId)).thenReturn(publicationType);

        when(linkProvider.getPublicLinks())
                .thenReturn(new ArrayList<>());

        try (MockedStatic<PublicationTypeMapper> mapper =
                     mockStatic(PublicationTypeMapper.class)) {

            mapper.when(() ->
                            PublicationTypeMapper.toUserId(userIdStr))
                    .thenReturn(userId);

            mapper.when(() ->
                            PublicationTypeMapper.toDTO(publicationType))
                    .thenReturn(responseDTO);

            // ACT
            ResponseEntity<PublicationTypeResponseDTO> response =
                    controller.createPublicationType(createDTO, userIdStr);

            // ASSERT
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals(responseDTO, response.getBody());
        }
    }

    // =========================================================
    // CREATE — LINKS ADDED TO RESPONSE DTO
    // =========================================================

    @Test
    void shouldAddLinksToResponseDTO() throws Exception {

        // ARRANGE
        String userIdStr = "user-001";
        String format = "Book";

        UserId userId = mock(UserId.class);

        PublicationType publicationType = mock(PublicationType.class);
        PublicationTypeId publicationTypeId = mock(PublicationTypeId.class);

        when(publicationType.id()).thenReturn(publicationTypeId);
        when(publicationTypeId.toString()).thenReturn("pt-001");

        PublicationTypeResponseDTO responseDTO =
                new PublicationTypeResponseDTO("1", "Book");

        Link publicLink =
                Link.of("/publication-types",
                        "get-all-publication-types");

        when(createDTO.mediaFormat()).thenReturn(format);
        when(service.createAndSave(format, userId))
                .thenReturn(publicationType);

        when(linkProvider.getPublicLinks())
                .thenReturn(new ArrayList<>(List.of(publicLink)));

        try (MockedStatic<PublicationTypeMapper> mapper =
                     mockStatic(PublicationTypeMapper.class)) {

            mapper.when(() ->
                            PublicationTypeMapper.toUserId(userIdStr))
                    .thenReturn(userId);

            mapper.when(() ->
                            PublicationTypeMapper.toDTO(publicationType))
                    .thenReturn(responseDTO);

            // ACT
            ResponseEntity<PublicationTypeResponseDTO> response =
                    controller.createPublicationType(createDTO, userIdStr);

            // ASSERT
            assertNotNull(response.getBody());
            assertEquals(2,
                    response.getBody().getLinks().toList().size());
        }
    }

    // =========================================================
    // CREATE — PROVIDER LINK PRESENT WITH CORRECT REL
    // =========================================================

    @Test
    void shouldAddPublicLinkWithCorrectRelToResponseDTO() throws Exception {

        // ARRANGE
        String userIdStr = "user-001";
        String format = "Book";

        UserId userId = mock(UserId.class);

        PublicationType publicationType = mock(PublicationType.class);
        PublicationTypeId publicationTypeId = mock(PublicationTypeId.class);

        when(publicationType.id()).thenReturn(publicationTypeId);
        when(publicationTypeId.toString()).thenReturn("pt-001");

        PublicationTypeResponseDTO responseDTO =
                new PublicationTypeResponseDTO("1", "Book");

        Link publicLink =
                Link.of("/publication-types",
                        "get-all-publication-types");

        when(createDTO.mediaFormat()).thenReturn(format);
        when(service.createAndSave(format, userId))
                .thenReturn(publicationType);

        when(linkProvider.getPublicLinks())
                .thenReturn(new ArrayList<>(List.of(publicLink)));

        try (MockedStatic<PublicationTypeMapper> mapper =
                     mockStatic(PublicationTypeMapper.class)) {

            mapper.when(() ->
                            PublicationTypeMapper.toUserId(userIdStr))
                    .thenReturn(userId);

            mapper.when(() ->
                            PublicationTypeMapper.toDTO(publicationType))
                    .thenReturn(responseDTO);

            // ACT
            ResponseEntity<PublicationTypeResponseDTO> response =
                    controller.createPublicationType(createDTO, userIdStr);

            boolean hasPublicLink =
                    response.getBody()
                            .getLinks()
                            .toList()
                            .stream()
                            .anyMatch(link ->
                                    link.getRel().value()
                                            .equals("get-all-publication-types"));

            // ASSERT
            assertTrue(hasPublicLink);
        }
    }

    // =========================================================
    // CREATE BAD REQUEST
    // =========================================================

    @Test
    void shouldReturnBadRequestWhenIllegalArgumentExceptionOccurs() throws Exception {

        // ARRANGE
        String userIdStr = "user-001";
        String format = "Book";

        UserId userId = mock(UserId.class);

        when(createDTO.mediaFormat()).thenReturn(format);
        when(service.createAndSave(format, userId)).thenThrow(new IllegalArgumentException());

        try (MockedStatic<PublicationTypeMapper> mapper = mockStatic(PublicationTypeMapper.class)) {

            mapper.when(() -> PublicationTypeMapper.toUserId(userIdStr)).thenReturn(userId);

            // ACT
            ResponseEntity<PublicationTypeResponseDTO> response =
                    controller.createPublicationType(createDTO, userIdStr);

            // ASSERT
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    // =========================================================
    // CREATE CONFLICT
    // =========================================================

    @Test
    void shouldReturnConflictWhenPublicationTypeAlreadyExists() throws Exception {

        // ARRANGE
        String userIdStr = "user-001";
        String format = "Book";

        UserId userId = mock(UserId.class);

        when(createDTO.mediaFormat()).thenReturn(format);
        when(service.createAndSave(format, userId)).thenThrow(new InstanceAlreadyExistsException());

        try (MockedStatic<PublicationTypeMapper> mapper = mockStatic(PublicationTypeMapper.class)) {

            mapper.when(() -> PublicationTypeMapper.toUserId(userIdStr)).thenReturn(userId);

            // ACT
            ResponseEntity<PublicationTypeResponseDTO> response =
                    controller.createPublicationType(createDTO, userIdStr);

            // ASSERT
            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        }
    }

    // =========================================================
    // CREATE FORBIDDEN
    // =========================================================

    @Test
    void shouldReturnForbiddenWhenAccessDeniedOccurs() throws Exception {

        // ARRANGE
        String userIdStr = "user-001";
        String format = "Book";

        UserId userId = mock(UserId.class);

        when(createDTO.mediaFormat()).thenReturn(format);
        when(service.createAndSave(format, userId)).thenThrow(new AccessDeniedException("Denied"));

        try (MockedStatic<PublicationTypeMapper> mapper = mockStatic(PublicationTypeMapper.class)) {

            mapper.when(() -> PublicationTypeMapper.toUserId(userIdStr)).thenReturn(userId);

            // ACT
            ResponseEntity<PublicationTypeResponseDTO> response =
                    controller.createPublicationType(createDTO, userIdStr);

            // ASSERT
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }
    }

    // =========================================================
    // CREATE INTERNAL SERVER ERROR
    // =========================================================

    @Test
    void shouldReturnInternalServerErrorOnCreateWhenUnexpectedExceptionOccurs() throws Exception {

        // ARRANGE
        String userIdStr = "user-001";
        String format = "Book";

        UserId userId = mock(UserId.class);

        when(createDTO.mediaFormat()).thenReturn(format);
        when(service.createAndSave(format, userId)).thenThrow(new RuntimeException());

        try (MockedStatic<PublicationTypeMapper> mapper = mockStatic(PublicationTypeMapper.class)) {

            mapper.when(() -> PublicationTypeMapper.toUserId(userIdStr)).thenReturn(userId);

            // ACT
            ResponseEntity<PublicationTypeResponseDTO> response =
                    controller.createPublicationType(createDTO, userIdStr);

            // ASSERT
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
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
        when(id1.toString()).thenReturn("pt-001");
        when(id2.toString()).thenReturn("pt-002");

        PublicationTypeResponseDTO dto1 = new PublicationTypeResponseDTO("1", "Book");
        PublicationTypeResponseDTO dto2 = new PublicationTypeResponseDTO("2", "Magazine");

        when(service.findAll()).thenReturn(List.of(pt1, pt2));

        try (MockedStatic<PublicationTypeMapper> mapper = mockStatic(PublicationTypeMapper.class)) {

            mapper.when(() -> PublicationTypeMapper.toDTO(pt1)).thenReturn(dto1);
            mapper.when(() -> PublicationTypeMapper.toDTO(pt2)).thenReturn(dto2);

            // ACT
            ResponseEntity<List<PublicationTypeResponseDTO>> response =
                    controller.findAll();

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
                controller.findAll();

        // ASSERT
        assertEquals(HttpStatus.OK, response.getStatusCode());
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
                controller.findAll();

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
                controller.findAll();

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // =========================================================
    // GET ALL — SELF LINK ADDED TO EACH DTO
    // =========================================================

    @Test
    void shouldAddSelfLinkToEachDTOWhenGettingAll() {

        // ARRANGE
        PublicationType pt1 = mock(PublicationType.class);
        PublicationTypeId id1 = mock(PublicationTypeId.class);

        when(pt1.id()).thenReturn(id1);
        when(id1.toString()).thenReturn("pt-001");

        PublicationTypeResponseDTO dto1 = new PublicationTypeResponseDTO("1", "Book");

        when(service.findAll()).thenReturn(List.of(pt1));

        try (MockedStatic<PublicationTypeMapper> mapper = mockStatic(PublicationTypeMapper.class)) {

            mapper.when(() -> PublicationTypeMapper.toDTO(pt1)).thenReturn(dto1);

            // ACT
            controller.findAll();

            // ASSERT
            assertFalse(dto1.getLinks().isEmpty());
            assertTrue(
                    dto1.getLinks().toList().stream()
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
        String idStr = "pt-001";

        PublicationTypeId publicationTypeId = mock(PublicationTypeId.class);
        PublicationType publicationType = mock(PublicationType.class);
        PublicationTypeId domainId = mock(PublicationTypeId.class);

        PublicationTypeResponseDTO responseDTO =
                new PublicationTypeResponseDTO("pt-001", "Book");

        when(service.findById(publicationTypeId))
                .thenReturn(publicationType);

        when(publicationType.id())
                .thenReturn(domainId);

        when(domainId.toString())
                .thenReturn("pt-001");

        when(linkProvider.getPublicLinks())
                .thenReturn(new ArrayList<>());

        try (MockedStatic<PublicationTypeMapper> mapper =
                     mockStatic(PublicationTypeMapper.class)) {

            mapper.when(() ->
                            PublicationTypeMapper.toPublicationTypeId(idStr))
                    .thenReturn(publicationTypeId);

            mapper.when(() ->
                            PublicationTypeMapper.toDTO(publicationType))
                    .thenReturn(responseDTO);

            // ACT
            ResponseEntity<PublicationTypeResponseDTO> response =
                    controller.findById(idStr);

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
        String idStr = "pt-001";

        PublicationTypeId publicationTypeId = mock(PublicationTypeId.class);

        when(service.findById(publicationTypeId))
                .thenThrow(new IllegalArgumentException("PublicationType not found"));

        try (MockedStatic<PublicationTypeMapper> mapper = mockStatic(PublicationTypeMapper.class)) {

            mapper.when(() -> PublicationTypeMapper.toPublicationTypeId(idStr))
                    .thenReturn(publicationTypeId);

            // ACT
            ResponseEntity<PublicationTypeResponseDTO> response =
                    controller.findById(idStr);

            // ASSERT
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    // =========================================================
    // GET BY ID INTERNAL SERVER ERROR
    // =========================================================

    @Test
    void shouldReturnInternalServerErrorWhenGetByIdThrowsUnexpectedException() {

        // ARRANGE
        String idStr = "pt-001";

        PublicationTypeId publicationTypeId = mock(PublicationTypeId.class);

        when(service.findById(publicationTypeId)).thenThrow(new RuntimeException());

        try (MockedStatic<PublicationTypeMapper> mapper = mockStatic(PublicationTypeMapper.class)) {

            mapper.when(() -> PublicationTypeMapper.toPublicationTypeId(idStr))
                    .thenReturn(publicationTypeId);

            // ACT
            ResponseEntity<PublicationTypeResponseDTO> response =
                    controller.findById(idStr);

            // ASSERT
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }

    // =========================================================
    // GET BY ID — LINKS WITH CORRECT RELS
    // =========================================================

    @Test
    void shouldAddSelfRelAndGetAllRelLinksWhenGettingById() {

        // ARRANGE
        String idStr = "pt-001";

        PublicationTypeId publicationTypeId = mock(PublicationTypeId.class);

        PublicationType publicationType = mock(PublicationType.class);

        when(publicationType.id()).thenReturn(publicationTypeId);
        when(publicationTypeId.toString()).thenReturn(idStr);

        PublicationTypeResponseDTO responseDTO =
                new PublicationTypeResponseDTO("1", "Book");

        Link publicLink =
                Link.of("/publication-types",
                        "get-all-publication-types");

        when(service.findById(publicationTypeId))
                .thenReturn(publicationType);

        when(linkProvider.getPublicLinks())
                .thenReturn(new ArrayList<>(List.of(publicLink)));

        try (MockedStatic<PublicationTypeMapper> mapper =
                     mockStatic(PublicationTypeMapper.class)) {

            mapper.when(() ->
                            PublicationTypeMapper.toPublicationTypeId(idStr))
                    .thenReturn(publicationTypeId);

            mapper.when(() ->
                            PublicationTypeMapper.toDTO(publicationType))
                    .thenReturn(responseDTO);

            controller.findById(idStr);

            // ACT
            List<Link> links =
                    responseDTO.getLinks().toList();

            // ASSERT
            assertEquals(2, links.size());

            assertTrue(
                    links.stream()
                            .anyMatch(l ->
                                    l.getRel().value().equals("self"))
            );

            assertTrue(
                    links.stream()
                            .anyMatch(l ->
                                    l.getRel().value()
                                            .equals("get-all-publication-types"))
            );
        }
    }

    @Test
    void shouldAddCreateLinkToResponseDTOWhenCreatingPublicationType() throws Exception {

        // ARRANGE
        String userIdStr = "user-001";
        String format = "Book";

        UserId userId = mock(UserId.class);

        PublicationType publicationType = mock(PublicationType.class);
        PublicationTypeId publicationTypeId = mock(PublicationTypeId.class);

        when(publicationType.id()).thenReturn(publicationTypeId);
        when(publicationTypeId.toString()).thenReturn("pt-001");

        PublicationTypeResponseDTO responseDTO =
                new PublicationTypeResponseDTO("1", "Book");

        Link createLink =
                Link.of("/publication-types",
                        "create-publication-type");

        when(createDTO.mediaFormat()).thenReturn(format);

        when(service.createAndSave(format, userId))
                .thenReturn(publicationType);

        when(linkProvider.getPublicLinks())
                .thenReturn(new ArrayList<>());

        when(linkProvider.getCreateLinks(userId))
                .thenReturn(new ArrayList<>(List.of(createLink)));

        try (MockedStatic<PublicationTypeMapper> mapper =
                     mockStatic(PublicationTypeMapper.class)) {

            mapper.when(() ->
                            PublicationTypeMapper.toUserId(userIdStr))
                    .thenReturn(userId);

            mapper.when(() ->
                            PublicationTypeMapper.toDTO(publicationType))
                    .thenReturn(responseDTO);

            // ACT
            ResponseEntity<PublicationTypeResponseDTO> response =
                    controller.createPublicationType(createDTO, userIdStr);

            // ASSERT
            assertNotNull(response.getBody());

            assertTrue(
                    response.getBody()
                            .getLinks()
                            .toList()
                            .stream()
                            .anyMatch(link ->
                                    link.getRel().value()
                                            .equals("create-publication-type"))
            );
        }
    }

    @Test
    void shouldReturnNotFoundWhenServiceThrowsEntityNotFoundException() {

        // ARRANGE
        when(service.findById(any()))
                .thenThrow(new EntityNotFoundException());

        // ACT
        ResponseEntity<PublicationTypeResponseDTO> result =
                controller.findById("123");

        // ASSERT
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void shouldReturnNotFoundWhenGetAllThrowsEntityNotFoundException() {

        // ARRANGE
        when(service.findAll())
                .thenThrow(new EntityNotFoundException());

        // ACT
        ResponseEntity<List<PublicationTypeResponseDTO>> result =
                controller.findAll();

        // ASSERT
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void shouldReturnAllowedMethodsForPublicationTypesCollection() throws InstanceNotFoundException {

        String userIdStr = "user-1";
        UserId userId = mock(UserId.class);

        try (MockedStatic<PublicationTypeMapper> mapper =
                     mockStatic(PublicationTypeMapper.class)) {

            mapper.when(() -> PublicationTypeMapper.toUserId(userIdStr))
                    .thenReturn(userId);

            when(service.canCreatePublicationType(userId))
                    .thenReturn(true);

            ResponseEntity<Void> response =
                    controller.optionsPublicationTypes(userIdStr);

            assertEquals(HttpStatus.OK, response.getStatusCode());

            assertTrue(response.getHeaders().getAllow().contains(HttpMethod.GET));
            assertTrue(response.getHeaders().getAllow().contains(HttpMethod.POST));
            assertTrue(response.getHeaders().getAllow().contains(HttpMethod.OPTIONS));
        }
    }

    @Test
    void shouldNotExposePostWhenUserCannotCreatePublicationType() throws InstanceNotFoundException {

        String userIdStr = "user-1";
        UserId userId = mock(UserId.class);

        try (MockedStatic<PublicationTypeMapper> mapper =
                     mockStatic(PublicationTypeMapper.class)) {

            mapper.when(() -> PublicationTypeMapper.toUserId(userIdStr))
                    .thenReturn(userId);

            when(service.canCreatePublicationType(userId))
                    .thenReturn(false);

            ResponseEntity<Void> response =
                    controller.optionsPublicationTypes(userIdStr);

            assertEquals(HttpStatus.OK, response.getStatusCode());

            assertTrue(response.getHeaders().getAllow().contains(HttpMethod.GET));
            assertTrue(response.getHeaders().getAllow().contains(HttpMethod.OPTIONS));

            assertFalse(response.getHeaders().getAllow().contains(HttpMethod.POST));
        }
    }

}