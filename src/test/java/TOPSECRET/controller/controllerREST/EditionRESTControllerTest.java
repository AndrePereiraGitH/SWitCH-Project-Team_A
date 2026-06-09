package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.EditionLinkProvider;
import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.ValueObjects.EditionId;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.domain.ValueObjects.PublisherID;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.edition.EditionMapper;
import TOPSECRET.dto.edition.request.CreateEditionDTO;
import TOPSECRET.dto.edition.response.EditionDTO;
import TOPSECRET.dto.edition.response.EditionIdResponseDTO;
import TOPSECRET.services.EditionService;

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

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

/**
 * Unit tests for {@link EditionRESTController}.
 *
 * <p>Updated for Sprint 7: {@code provider.getLinks(userId)} replaced by
 * {@code provider.getPublicLinks()} and {@code provider.getPrivateLinks(userId)}.</p>
 */
@ExtendWith(MockitoExtension.class)
class EditionRESTControllerTest {

    @Mock
    private EditionService service;

    @Mock
    private EditionLinkProvider provider;

    @Mock
    private CreateEditionDTO createDTO;

    @InjectMocks
    private EditionRESTController controller;

    // =========================================================
    // CREATE — SUCCESS
    // =========================================================

    @Test
    void shouldCreateEditionSuccessfully() throws Exception {

        // ARRANGE
        String userIdStr = "user-001";
        UserId userId = mock(UserId.class);

        Edition edition = mock(Edition.class);
        EditionId editionId = mock(EditionId.class);
        EditionDTO responseDTO = mock(EditionDTO.class);
        Link link = mock(Link.class);

        when(edition.id()).thenReturn(editionId);
        when(editionId.getID()).thenReturn("edition-1");

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toUserId(userIdStr)).thenReturn(userId);
            mapper.when(() -> EditionMapper.toDTO(edition)).thenReturn(responseDTO);
            stubCreateDTO(mapper, edition);
            stubProviderLinks(userId, List.of(link), List.of());

            // ACT
            ResponseEntity<EditionDTO> response =
                    controller.createEdition(createDTO, userIdStr);

            // ASSERT
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals(responseDTO, response.getBody());
        }
    }

    @Test
    void shouldAddPublicAndPrivateLinksToResponseDTOOnCreate() throws Exception {

        // ARRANGE
        String userIdStr = "user-001";
        UserId userId = mock(UserId.class);

        Edition edition = mock(Edition.class);
        EditionId editionId = mock(EditionId.class);
        EditionDTO responseDTO = buildEditionDTO("1");

        Link publicLink = Link.of("/editions", "find-all-editions");
        Link privateLink = Link.of("/editions", "create-edition");

        when(edition.id()).thenReturn(editionId);
        when(editionId.getID()).thenReturn("1");

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toUserId(userIdStr)).thenReturn(userId);
            mapper.when(() -> EditionMapper.toDTO(edition)).thenReturn(responseDTO);
            stubCreateDTO(mapper, edition);
            stubProviderLinks(userId, List.of(publicLink), List.of(privateLink));

            // ACT
            controller.createEdition(createDTO, userIdStr);

            // ASSERT — public link + private link + selfRel = 3
            assertEquals(3, responseDTO.getLinks().toList().size());
        }
    }

    @Test
    void shouldAddCreateEditionRelOnCreate() throws Exception {

        // ARRANGE
        String userIdStr = "user-001";
        UserId userId = mock(UserId.class);

        Edition edition = mock(Edition.class);
        EditionId editionId = mock(EditionId.class);
        EditionDTO responseDTO = buildEditionDTO("1");

        Link privateLink = Link.of("/editions", "create-edition");

        when(edition.id()).thenReturn(editionId);
        when(editionId.getID()).thenReturn("1");

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toUserId(userIdStr)).thenReturn(userId);
            mapper.when(() -> EditionMapper.toDTO(edition)).thenReturn(responseDTO);
            stubCreateDTO(mapper, edition);
            stubProviderLinks(userId, List.of(), List.of(privateLink));

            // ACT
            controller.createEdition(createDTO, userIdStr);

            // ASSERT
            assertTrue(responseDTO.getLinks().toList().stream()
                    .anyMatch(l -> l.getRel().value().equals("create-edition")));
        }
    }

    // =========================================================
    // CREATE — ERROR CASES
    // =========================================================

    @Test
    void shouldReturnForbiddenWhenAccessDeniedOnCreate() throws Exception {

        // ARRANGE
        String userIdStr = "user-001";
        UserId userId = mock(UserId.class);

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toUserId(userIdStr)).thenReturn(userId);
            stubCreateDTOThrows(mapper, new AccessDeniedException("Denied"));

            // ACT
            ResponseEntity<EditionDTO> response =
                    controller.createEdition(createDTO, userIdStr);

            // ASSERT
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        }
    }

    @Test
    void shouldReturnBadRequestWhenIllegalArgumentOnCreate() throws Exception {

        // ARRANGE
        String userIdStr = "user-001";
        UserId userId = mock(UserId.class);

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toUserId(userIdStr)).thenReturn(userId);
            stubCreateDTOThrows(mapper, new IllegalArgumentException());

            // ACT
            ResponseEntity<EditionDTO> response =
                    controller.createEdition(createDTO, userIdStr);

            // ASSERT
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    void shouldReturnInternalServerErrorOnCreateWhenUnexpectedExceptionOccurs()
            throws Exception {

        // ARRANGE
        String userIdStr = "user-001";
        UserId userId = mock(UserId.class);

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toUserId(userIdStr)).thenReturn(userId);
            stubCreateDTOThrows(mapper, new RuntimeException());

            // ACT
            ResponseEntity<EditionDTO> response =
                    controller.createEdition(createDTO, userIdStr);

            // ASSERT
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }

    // =========================================================
    // FIND BY ID — SUCCESS
    // =========================================================

    @Test
    void shouldReturnEditionByIdSuccessfully() {

        // ARRANGE
        String rawId = "edition-1";
        EditionId editionId = mock(EditionId.class);
        Edition edition = mock(Edition.class);
        EditionDTO responseDTO = mock(EditionDTO.class);

        when(edition.id()).thenReturn(editionId);
        when(editionId.getID()).thenReturn("edition-1");
        when(service.findById(editionId)).thenReturn(edition);
        when(provider.getPublicLinks()).thenReturn(List.of());

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toEditionId(rawId)).thenReturn(editionId);
            mapper.when(() -> EditionMapper.toDTO(edition)).thenReturn(responseDTO);

            // ACT
            ResponseEntity<EditionDTO> response = controller.findById(rawId);

            // ASSERT
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(responseDTO, response.getBody());
        }
    }

    @Test
    void shouldAddSelfRelAndPublicLinksWhenFindingById() {

        // ARRANGE
        String rawId = "edition-1";
        EditionId editionId = mock(EditionId.class);
        Edition edition = mock(Edition.class);
        EditionDTO responseDTO = buildEditionDTO("1");

        when(edition.id()).thenReturn(editionId);
        when(editionId.getID()).thenReturn("edition-1");
        when(service.findById(editionId)).thenReturn(edition);
        when(provider.getPublicLinks()).thenReturn(
                List.of(Link.of("/editions", "find-all-editions")));

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toEditionId(rawId)).thenReturn(editionId);
            mapper.when(() -> EditionMapper.toDTO(edition)).thenReturn(responseDTO);

            // ACT
            controller.findById(rawId);

            // ASSERT — public link + selfRel = 2
            List<Link> links = responseDTO.getLinks().toList();
            assertEquals(2, links.size());
            assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("self")));
            assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("find-all-editions")));
        }
    }

    // =========================================================
    // FIND BY ID — ERROR CASES
    // =========================================================

    @Test
    void shouldReturnNotFoundWhenEditionDoesNotExist() {

        // ARRANGE
        String rawId = "edition-999";
        EditionId editionId = mock(EditionId.class);

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toEditionId(rawId)).thenReturn(editionId);
            when(service.findById(editionId))
                    .thenThrow(new IllegalArgumentException("Edition not found"));

            // ACT
            ResponseEntity<EditionDTO> response = controller.findById(rawId);

            // ASSERT
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Test
    void shouldReturnInternalServerErrorWhenFindByIdThrowsUnexpectedException() {

        // ARRANGE
        String rawId = "edition-1";
        EditionId editionId = mock(EditionId.class);

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toEditionId(rawId)).thenReturn(editionId);
            when(service.findById(editionId)).thenThrow(new RuntimeException());

            // ACT
            ResponseEntity<EditionDTO> response = controller.findById(rawId);

            // ASSERT
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }

    // =========================================================
    // FIND ALL — SUCCESS
    // =========================================================

    @Test
    void shouldReturnAllEditionsSuccessfully() {

        // ARRANGE
        Edition e1 = mock(Edition.class);
        Edition e2 = mock(Edition.class);
        EditionId id1 = mock(EditionId.class);
        EditionId id2 = mock(EditionId.class);

        when(e1.id()).thenReturn(id1);
        when(e2.id()).thenReturn(id2);
        when(id1.getID()).thenReturn("1");
        when(id2.getID()).thenReturn("2");

        EditionDTO dto1 = buildEditionDTO("1");
        EditionDTO dto2 = buildEditionDTO("2");

        when(service.findAll()).thenReturn(List.of(e1, e2));
        when(provider.getPublicLinks()).thenReturn(List.of());

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toDTO(e1)).thenReturn(dto1);
            mapper.when(() -> EditionMapper.toDTO(e2)).thenReturn(dto2);

            // ACT
            ResponseEntity<List<EditionDTO>> response = controller.findAll();

            // ASSERT
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(2, response.getBody().size());
            assertTrue(response.getBody().containsAll(List.of(dto1, dto2)));
        }
    }

    @Test
    void shouldAddSelfLinkToEachDTOWhenFindingAll() {

        // ARRANGE
        Edition e1 = mock(Edition.class);
        EditionId id1 = mock(EditionId.class);
        when(e1.id()).thenReturn(id1);
        when(id1.getID()).thenReturn("1");

        EditionDTO dto1 = buildEditionDTO("1");
        when(service.findAll()).thenReturn(List.of(e1));
        when(provider.getPublicLinks()).thenReturn(List.of());

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toDTO(e1)).thenReturn(dto1);

            // ACT
            controller.findAll();

            // ASSERT
            assertTrue(dto1.getLinks().toList().stream()
                    .anyMatch(l -> l.getRel().value().equals("self")));
        }
    }

    // =========================================================
    // FIND ALL — ERROR CASES
    // =========================================================

    @Test
    void shouldReturnNoContentWhenNoEditionsExist() {

        // ARRANGE
        when(service.findAll()).thenReturn(List.of());

        // ACT
        ResponseEntity<List<EditionDTO>> response = controller.findAll();

        // ASSERT
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void shouldReturnInternalServerErrorWhenFindAllThrowsUnexpectedException() {

        // ARRANGE
        when(service.findAll()).thenThrow(new RuntimeException());

        // ACT
        ResponseEntity<List<EditionDTO>> response = controller.findAll();

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // =========================================================
    // FIND EDITION IDS BY PUBLISHER ID — SUCCESS
    // =========================================================

    @Test
    void shouldReturnEditionIdsByPublisherIdSuccessfully() {

        // ARRANGE
        String rawPublisherId = "publisher-1";
        PublisherID publisherID = mock(PublisherID.class);

        EditionId editionId1 = mock(EditionId.class);
        EditionId editionId2 = mock(EditionId.class);
        when(editionId1.getID()).thenReturn("edition-1");
        when(editionId2.getID()).thenReturn("edition-2");

        EditionIdResponseDTO dto1 = new EditionIdResponseDTO("edition-1");
        EditionIdResponseDTO dto2 = new EditionIdResponseDTO("edition-2");

        when(service.findIdsByPublisherId(publisherID))
                .thenReturn(List.of(editionId1, editionId2));

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toPublisherId(rawPublisherId))
                    .thenReturn(publisherID);
            mapper.when(() -> EditionMapper.toEditionIdDTO(editionId1)).thenReturn(dto1);
            mapper.when(() -> EditionMapper.toEditionIdDTO(editionId2)).thenReturn(dto2);

            // ACT
            ResponseEntity<List<EditionIdResponseDTO>> response =
                    controller.findEditionIdsByPublisherId(rawPublisherId);

            // ASSERT
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(2, response.getBody().size());
            assertTrue(response.getBody().contains(dto1));
            assertTrue(response.getBody().contains(dto2));
        }
    }

    @Test
    void shouldReturnNoContentWhenNoEditionIdsFoundForPublisher() {

        // ARRANGE
        String rawPublisherId = "publisher-1";
        PublisherID publisherID = mock(PublisherID.class);

        when(service.findIdsByPublisherId(publisherID)).thenReturn(List.of());

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toPublisherId(rawPublisherId))
                    .thenReturn(publisherID);

            // ACT
            ResponseEntity<List<EditionIdResponseDTO>> response =
                    controller.findEditionIdsByPublisherId(rawPublisherId);

            // ASSERT
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        }
    }

    @Test
    void shouldReturnBadRequestWhenPublisherIdIsInvalidForIdLookup() {

        // ARRANGE
        String rawPublisherId = "invalid";

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toPublisherId(rawPublisherId))
                    .thenThrow(new IllegalArgumentException());

            // ACT
            ResponseEntity<List<EditionIdResponseDTO>> response =
                    controller.findEditionIdsByPublisherId(rawPublisherId);

            // ASSERT
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    void shouldReturnInternalServerErrorWhenFindIdsByPublisherIdThrowsUnexpectedException() {

        // ARRANGE
        String rawPublisherId = "publisher-1";
        PublisherID publisherID = mock(PublisherID.class);

        when(service.findIdsByPublisherId(publisherID)).thenThrow(new RuntimeException());

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toPublisherId(rawPublisherId))
                    .thenReturn(publisherID);

            // ACT
            ResponseEntity<List<EditionIdResponseDTO>> response =
                    controller.findEditionIdsByPublisherId(rawPublisherId);

            // ASSERT
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }

    // =========================================================
    // FIND BY PUBLISHER ID — SUCCESS
    // =========================================================

    @Test
    void shouldReturnEditionsByPublisherIdSuccessfully() {

        // ARRANGE
        String rawPublisherId = "publisher-1";
        PublisherID publisherID = mock(PublisherID.class);

        Edition e1 = mock(Edition.class);
        EditionId id1 = mock(EditionId.class);
        when(e1.id()).thenReturn(id1);
        when(id1.getID()).thenReturn("edition-1");

        EditionDTO dto1 = buildEditionDTO("edition-1");

        when(service.findByPublisherId(publisherID)).thenReturn(List.of(e1));
        when(provider.getPublicLinks()).thenReturn(List.of());

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toPublisherId(rawPublisherId))
                    .thenReturn(publisherID);
            mapper.when(() -> EditionMapper.toDTO(e1)).thenReturn(dto1);

            // ACT
            ResponseEntity<List<EditionDTO>> response =
                    controller.findByPublisherId(rawPublisherId);

            // ASSERT
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
        }
    }

    @Test
    void shouldReturnMappedEditionDTOWhenFindingByPublisherId() {

        // ARRANGE
        String rawPublisherId = "publisher-1";
        PublisherID publisherID = mock(PublisherID.class);

        Edition edition = mock(Edition.class);
        EditionId editionId = mock(EditionId.class);
        when(edition.id()).thenReturn(editionId);
        when(editionId.getID()).thenReturn("edition-1");

        EditionDTO dto = buildEditionDTO("edition-1");
        when(service.findByPublisherId(publisherID)).thenReturn(List.of(edition));
        when(provider.getPublicLinks()).thenReturn(List.of());

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toPublisherId(rawPublisherId))
                    .thenReturn(publisherID);
            mapper.when(() -> EditionMapper.toDTO(edition)).thenReturn(dto);

            // ACT
            ResponseEntity<List<EditionDTO>> response =
                    controller.findByPublisherId(rawPublisherId);

            // ASSERT
            assertNotNull(response.getBody());
            assertSame(dto, response.getBody().get(0));
        }
    }

    @Test
    void shouldAddSelfRelWhenFindingByPublisherId() {

        // ARRANGE
        String rawPublisherId = "publisher-1";
        PublisherID publisherID = mock(PublisherID.class);

        Edition e1 = mock(Edition.class);
        EditionId id1 = mock(EditionId.class);
        when(e1.id()).thenReturn(id1);
        when(id1.getID()).thenReturn("edition-1");

        EditionDTO dto1 = buildEditionDTO("edition-1");
        when(service.findByPublisherId(publisherID)).thenReturn(List.of(e1));
        when(provider.getPublicLinks()).thenReturn(List.of());

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toPublisherId(rawPublisherId))
                    .thenReturn(publisherID);
            mapper.when(() -> EditionMapper.toDTO(e1)).thenReturn(dto1);

            // ACT
            controller.findByPublisherId(rawPublisherId);

            // ASSERT
            assertTrue(dto1.getLinks().toList().stream()
                    .anyMatch(l -> l.getRel().value().equals("self")));
        }
    }

    @Test
    void shouldReturnNoContentWhenNoEditionsFoundForPublisher() {

        // ARRANGE
        String rawPublisherId = "publisher-1";
        PublisherID publisherID = mock(PublisherID.class);

        when(service.findByPublisherId(publisherID)).thenReturn(List.of());

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toPublisherId(rawPublisherId))
                    .thenReturn(publisherID);

            // ACT
            ResponseEntity<List<EditionDTO>> response =
                    controller.findByPublisherId(rawPublisherId);

            // ASSERT
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        }
    }

    @Test
    void shouldReturnBadRequestWhenPublisherIdIsInvalid() {

        // ARRANGE
        String rawPublisherId = "invalid";

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toPublisherId(rawPublisherId))
                    .thenThrow(new IllegalArgumentException());

            // ACT
            ResponseEntity<List<EditionDTO>> response =
                    controller.findByPublisherId(rawPublisherId);

            // ASSERT
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    void shouldReturnInternalServerErrorWhenFindByPublisherIdThrowsUnexpectedException() {

        // ARRANGE
        String rawPublisherId = "publisher-1";
        PublisherID publisherID = mock(PublisherID.class);

        when(service.findByPublisherId(publisherID)).thenThrow(new RuntimeException());

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toPublisherId(rawPublisherId))
                    .thenReturn(publisherID);

            // ACT
            ResponseEntity<List<EditionDTO>> response =
                    controller.findByPublisherId(rawPublisherId);

            // ASSERT
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }

    // =========================================================
    // FIND BY PUBLICATION TYPE ID — SUCCESS
    // =========================================================

    @Test
    void shouldReturnEditionsByPublicationTypeIdSuccessfully() {

        // ARRANGE
        String rawTypeId = "type-1";
        PublicationTypeId publicationTypeId = mock(PublicationTypeId.class);

        Edition e1 = mock(Edition.class);
        EditionId id1 = mock(EditionId.class);
        when(e1.id()).thenReturn(id1);
        when(id1.getID()).thenReturn("edition-1");

        EditionDTO dto1 = buildEditionDTO("edition-1");
        when(service.findByPublicationTypeId(publicationTypeId)).thenReturn(List.of(e1));
        when(provider.getPublicLinks()).thenReturn(List.of());

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toPublicationTypeId(rawTypeId))
                    .thenReturn(publicationTypeId);
            mapper.when(() -> EditionMapper.toDTO(e1)).thenReturn(dto1);

            // ACT
            ResponseEntity<List<EditionDTO>> response =
                    controller.findByPublicationTypeId(rawTypeId);

            // ASSERT
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
        }
    }

    @Test
    void shouldReturnMappedEditionDTOWhenFindingByPublicationTypeId() {

        // ARRANGE
        String rawTypeId = "type-1";
        PublicationTypeId publicationTypeId = mock(PublicationTypeId.class);

        Edition edition = mock(Edition.class);
        EditionId editionId = mock(EditionId.class);
        when(edition.id()).thenReturn(editionId);
        when(editionId.getID()).thenReturn("edition-1");

        EditionDTO dto = buildEditionDTO("edition-1");
        when(service.findByPublicationTypeId(publicationTypeId)).thenReturn(List.of(edition));
        when(provider.getPublicLinks()).thenReturn(List.of());

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toPublicationTypeId(rawTypeId))
                    .thenReturn(publicationTypeId);
            mapper.when(() -> EditionMapper.toDTO(edition)).thenReturn(dto);

            // ACT
            ResponseEntity<List<EditionDTO>> response =
                    controller.findByPublicationTypeId(rawTypeId);

            // ASSERT
            assertNotNull(response.getBody());
            assertSame(dto, response.getBody().get(0));
        }
    }

    @Test
    void shouldAddSelfRelWhenFindingByPublicationTypeId() {

        // ARRANGE
        String rawTypeId = "type-1";
        PublicationTypeId publicationTypeId = mock(PublicationTypeId.class);

        Edition e1 = mock(Edition.class);
        EditionId id1 = mock(EditionId.class);
        when(e1.id()).thenReturn(id1);
        when(id1.getID()).thenReturn("edition-1");

        EditionDTO dto1 = buildEditionDTO("edition-1");
        when(service.findByPublicationTypeId(publicationTypeId)).thenReturn(List.of(e1));
        when(provider.getPublicLinks()).thenReturn(List.of());

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toPublicationTypeId(rawTypeId))
                    .thenReturn(publicationTypeId);
            mapper.when(() -> EditionMapper.toDTO(e1)).thenReturn(dto1);

            // ACT
            controller.findByPublicationTypeId(rawTypeId);

            // ASSERT
            assertTrue(dto1.getLinks().toList().stream()
                    .anyMatch(l -> l.getRel().value().equals("self")));
        }
    }

    @Test
    void shouldReturnNoContentWhenNoEditionsFoundForPublicationType() {

        // ARRANGE
        String rawTypeId = "type-1";
        PublicationTypeId publicationTypeId = mock(PublicationTypeId.class);

        when(service.findByPublicationTypeId(publicationTypeId)).thenReturn(List.of());

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toPublicationTypeId(rawTypeId))
                    .thenReturn(publicationTypeId);

            // ACT
            ResponseEntity<List<EditionDTO>> response =
                    controller.findByPublicationTypeId(rawTypeId);

            // ASSERT
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        }
    }

    @Test
    void shouldReturnBadRequestWhenPublicationTypeIdIsInvalid() {

        // ARRANGE
        String rawTypeId = "invalid";

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toPublicationTypeId(rawTypeId))
                    .thenThrow(new IllegalArgumentException());

            // ACT
            ResponseEntity<List<EditionDTO>> response =
                    controller.findByPublicationTypeId(rawTypeId);

            // ASSERT
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Test
    void shouldReturnInternalServerErrorWhenFindByPublicationTypeIdThrowsUnexpectedException() {

        // ARRANGE
        String rawTypeId = "type-1";
        PublicationTypeId publicationTypeId = mock(PublicationTypeId.class);

        when(service.findByPublicationTypeId(publicationTypeId))
                .thenThrow(new RuntimeException());

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toPublicationTypeId(rawTypeId))
                    .thenReturn(publicationTypeId);

            // ACT
            ResponseEntity<List<EditionDTO>> response =
                    controller.findByPublicationTypeId(rawTypeId);

            // ASSERT
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }

    // =========================================================
    // HELPERS
    // =========================================================

    /**
     * Stubs provider public and private links for create-endpoint tests.
     * Uses doAnswer to bypass the checked InstanceNotFoundException on getPrivateLinks.
     */
    private void stubProviderLinks(UserId userId, List<Link> publicLinks, List<Link> privateLinks)
            throws Exception {
        when(provider.getPublicLinks()).thenReturn(publicLinks);
        lenient().doAnswer(inv -> privateLinks).when(provider).getPrivateLinks(userId);
        lenient().doAnswer(inv -> privateLinks).when(provider).getPrivateLinks(any());
    }

    /**
     * Stubs EditionMapper static methods so service.createAndSave() succeeds,
     * returning the provided edition.
     */
    private void stubCreateDTO(MockedStatic<EditionMapper> mapper, Edition edition)
            throws Exception {

        mapper.when(() -> EditionMapper.toCode(createDTO)).thenReturn(null);
        mapper.when(() -> EditionMapper.toPhysicalAttributes(createDTO)).thenReturn(null);
        mapper.when(() -> EditionMapper.toPublicationTypeId(any())).thenReturn(null);
        mapper.when(() -> EditionMapper.toPublisherId(any())).thenReturn(null);
        mapper.when(() -> EditionMapper.toEditionLanguage(any())).thenReturn(null);
        mapper.when(() -> EditionMapper.toTypeOfBinding(any())).thenReturn(null);

        when(service.createAndSave(any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any()))
                .thenReturn(edition);
    }

    /**
     * Stubs EditionMapper static methods and makes service.createAndSave()
     * throw the provided exception.
     */
    private void stubCreateDTOThrows(MockedStatic<EditionMapper> mapper,
                                     Exception exception) throws Exception {

        mapper.when(() -> EditionMapper.toCode(createDTO)).thenReturn(null);
        mapper.when(() -> EditionMapper.toPhysicalAttributes(createDTO)).thenReturn(null);
        mapper.when(() -> EditionMapper.toPublicationTypeId(any())).thenReturn(null);
        mapper.when(() -> EditionMapper.toPublisherId(any())).thenReturn(null);
        mapper.when(() -> EditionMapper.toEditionLanguage(any())).thenReturn(null);
        mapper.when(() -> EditionMapper.toTypeOfBinding(any())).thenReturn(null);

        when(service.createAndSave(any(), any(), any(), any(), any(),
                any(), any(), any(), any(), any()))
                .thenThrow(exception);
    }

    /**
     * Builds an {@link EditionDTO} with stub values for use in link-assertion tests.
     */
    private EditionDTO buildEditionDTO(String editionId) {
        return new EditionDTO(
                editionId, "1", "CODE-001", "type-1", "publisher-1",
                "Book", "pt", LocalDate.of(2024, 1, 1),
                "Hardcover", 20.0, 15.0, 3.0, 500.0, 300
        );
    }
}
