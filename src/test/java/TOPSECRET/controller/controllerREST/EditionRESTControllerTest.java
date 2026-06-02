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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

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
    // CREATE SUCCESS
    // =========================================================

    @Test
    void shouldCreateEditionSuccessfully() throws Exception {

        // ARRANGE
        String userIdStr = "user-001";
        UserId userId = mock(UserId.class);

        Edition edition = mock(Edition.class);
        EditionDTO responseDTO = mock(EditionDTO.class);
        Link link = mock(Link.class);

        when(provider.getLinks(userId)).thenReturn(List.of(link));

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toUserId(userIdStr)).thenReturn(userId);
            mapper.when(() -> EditionMapper.toDTO(edition)).thenReturn(responseDTO);
            stubCreateDTO(mapper, edition);

            // ACT
            ResponseEntity<EditionDTO> response =
                    controller.createEdition(createDTO, userIdStr);

            // ASSERT
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals(responseDTO, response.getBody());
        }
    }

    // =========================================================
    // CREATE — LINKS ADICIONADOS (provider + selfRel = 2)
    // =========================================================

    @Test
    void shouldAddLinksToResponseDTOOnCreate() throws Exception {

        // ARRANGE
        String userIdStr = "user-001";
        UserId userId = mock(UserId.class);

        Edition edition = mock(Edition.class);
        EditionDTO responseDTO = buildEditionDTO("1");
        Link providerLink = Link.of("/editions", "find-all-editions");

        when(provider.getLinks(userId)).thenReturn(List.of(providerLink));

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toUserId(userIdStr)).thenReturn(userId);
            mapper.when(() -> EditionMapper.toDTO(edition)).thenReturn(responseDTO);
            stubCreateDTO(mapper, edition);

            // ACT
            ResponseEntity<EditionDTO> response =
                    controller.createEdition(createDTO, userIdStr);

            // ASSERT
            assertNotNull(response.getBody());
            assertEquals(2, response.getBody().getLinks().toList().size());
        }
    }

    // =========================================================
    // CREATE — PROVIDER LINK COM REL CORRETO
    // =========================================================

    @Test
    void shouldAddProviderLinkWithCorrectRelOnCreate() throws Exception {

        // ARRANGE
        String userIdStr = "user-001";
        UserId userId = mock(UserId.class);

        Edition edition = mock(Edition.class);
        EditionDTO responseDTO = buildEditionDTO("1");
        Link providerLink = Link.of("/editions", "find-all-editions");

        when(provider.getLinks(userId)).thenReturn(List.of(providerLink));

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toUserId(userIdStr)).thenReturn(userId);
            mapper.when(() -> EditionMapper.toDTO(edition)).thenReturn(responseDTO);
            stubCreateDTO(mapper, edition);

            // ACT
            controller.createEdition(createDTO, userIdStr);

            // ASSERT
            assertTrue(responseDTO.getLinks().toList().stream()
                    .anyMatch(l -> l.getRel().value().equals("find-all-editions")));
        }
    }

    // =========================================================
    // CREATE FORBIDDEN
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

    // =========================================================
    // CREATE BAD REQUEST
    // =========================================================

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

    // =========================================================
    // CREATE INTERNAL SERVER ERROR
    // =========================================================

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
    // FIND BY ID SUCCESS
    // =========================================================

    @Test
    void shouldReturnEditionByIdSuccessfully() {

        // ARRANGE
        String rawId = "edition-1";
        EditionId editionId = mock(EditionId.class);
        Edition edition = mock(Edition.class);
        EditionDTO responseDTO = mock(EditionDTO.class);

        when(service.findById(editionId)).thenReturn(edition);

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toEditionId(rawId)).thenReturn(editionId);
            mapper.when(() -> EditionMapper.toDTO(edition)).thenReturn(responseDTO);

            // ACT
            ResponseEntity<EditionDTO> response =
                    controller.findById(rawId);

            // ASSERT
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(responseDTO, response.getBody());
        }
    }

    // =========================================================
    // FIND BY ID — 2 LINKS (selfRel + find-all)
    // =========================================================

    @Test
    void shouldAddTwoLinksToResponseDTOWhenFindingById() {

        // ARRANGE
        String rawId = "edition-1";
        EditionId editionId = mock(EditionId.class);
        Edition edition = mock(Edition.class);
        EditionDTO responseDTO = buildEditionDTO("1");

        when(service.findById(editionId)).thenReturn(edition);

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toEditionId(rawId)).thenReturn(editionId);
            mapper.when(() -> EditionMapper.toDTO(edition)).thenReturn(responseDTO);

            // ACT
            controller.findById(rawId);

            // ASSERT
            assertEquals(2, responseDTO.getLinks().toList().size());
        }
    }

    // =========================================================
    // FIND BY ID — RELS CORRETOS
    // =========================================================

    @Test
    void shouldAddSelfRelAndFindAllRelWhenFindingById() {

        // ARRANGE
        String rawId = "edition-1";
        EditionId editionId = mock(EditionId.class);
        Edition edition = mock(Edition.class);
        EditionDTO responseDTO = buildEditionDTO("1");

        when(service.findById(editionId)).thenReturn(edition);

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toEditionId(rawId)).thenReturn(editionId);
            mapper.when(() -> EditionMapper.toDTO(edition)).thenReturn(responseDTO);

            // ACT
            controller.findById(rawId);

            // ASSERT
            List<Link> links = responseDTO.getLinks().toList();
            assertEquals(2, links.size());
            assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("self")));
            assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("find-all")));
        }
    }

    // =========================================================
    // FIND BY ID NOT FOUND
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
            ResponseEntity<EditionDTO> response =
                    controller.findById(rawId);

            // ASSERT
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    // =========================================================
    // FIND BY ID INTERNAL SERVER ERROR
    // =========================================================

    @Test
    void shouldReturnInternalServerErrorWhenFindByIdThrowsUnexpectedException() {

        // ARRANGE
        String rawId = "edition-1";
        EditionId editionId = mock(EditionId.class);

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toEditionId(rawId)).thenReturn(editionId);
            when(service.findById(editionId)).thenThrow(new RuntimeException());

            // ACT
            ResponseEntity<EditionDTO> response =
                    controller.findById(rawId);

            // ASSERT
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }

    // =========================================================
    // FIND ALL SUCCESS
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

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toDTO(e1)).thenReturn(dto1);
            mapper.when(() -> EditionMapper.toDTO(e2)).thenReturn(dto2);

            // ACT
            ResponseEntity<List<EditionDTO>> response =
                    controller.findAll();

            // ASSERT
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(2, response.getBody().size());
            assertTrue(response.getBody().containsAll(List.of(dto1, dto2)));
        }
    }

    // =========================================================
    // FIND ALL NO CONTENT
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

    // =========================================================
    // FIND ALL — SELF LINK EM CADA DTO
    // =========================================================

    @Test
    void shouldAddSelfLinkToEachDTOWhenFindingAll() {

        // ARRANGE
        Edition e1 = mock(Edition.class);
        EditionId id1 = mock(EditionId.class);
        when(e1.id()).thenReturn(id1);
        when(id1.getID()).thenReturn("1");

        EditionDTO dto1 = buildEditionDTO("1");
        when(service.findAll()).thenReturn(List.of(e1));

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
    // FIND ALL INTERNAL SERVER ERROR
    // =========================================================

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
            mapper.when(() -> EditionMapper.toEditionIdDTO(editionId1))
                    .thenReturn(dto1);
            mapper.when(() -> EditionMapper.toEditionIdDTO(editionId2))
                    .thenReturn(dto2);

            ResponseEntity<List<EditionIdResponseDTO>> response =
                    controller.findEditionIdsByPublisherId(rawPublisherId);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(2, response.getBody().size());
            assertTrue(response.getBody().contains(dto1));
            assertTrue(response.getBody().contains(dto2));
        }
    }

    // =========================================================
    // FIND EDITION IDS BY PUBLISHER ID — NO CONTENT
    // =========================================================

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

    // =========================================================
    // FIND EDITION IDS BY PUBLISHER ID — BAD REQUEST
    // =========================================================

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

    // =========================================================
    // FIND EDITION IDS BY PUBLISHER ID — INTERNAL SERVER ERROR
    // =========================================================

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

    // =========================================================
    // FIND BY PUBLISHER ID — LINKS (selfRel + find-by-publisher)
    // =========================================================

    @Test
    void shouldAddSelfRelAndFindByPublisherRelWhenFindingByPublisherId() {

        // ARRANGE
        String rawPublisherId = "publisher-1";
        PublisherID publisherID = mock(PublisherID.class);

        Edition e1 = mock(Edition.class);
        EditionId id1 = mock(EditionId.class);
        when(e1.id()).thenReturn(id1);
        when(id1.getID()).thenReturn("edition-1");

        EditionDTO dto1 = buildEditionDTO("edition-1");

        when(service.findByPublisherId(publisherID)).thenReturn(List.of(e1));

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toPublisherId(rawPublisherId))
                    .thenReturn(publisherID);
            mapper.when(() -> EditionMapper.toDTO(e1)).thenReturn(dto1);

            // ACT
            controller.findByPublisherId(rawPublisherId);

            // ASSERT
            List<Link> links = dto1.getLinks().toList();
            assertEquals(2, links.size());
            assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("self")));
            assertTrue(links.stream()
                    .anyMatch(l -> l.getRel().value().equals("find-by-publisher")));
        }
    }

    // =========================================================
    // FIND BY PUBLISHER ID — NO CONTENT
    // =========================================================

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

    // =========================================================
    // FIND BY PUBLISHER ID — BAD REQUEST
    // =========================================================

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

    // =========================================================
    // FIND BY PUBLISHER ID — INTERNAL SERVER ERROR
    // =========================================================

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

    // =========================================================
    // FIND BY PUBLICATION TYPE ID — LINKS (selfRel + find-by-publication-type)
    // =========================================================

    @Test
    void shouldAddSelfRelAndFindByPublicationTypeRelWhenFindingByPublicationTypeId() {

        // ARRANGE
        String rawTypeId = "type-1";
        PublicationTypeId publicationTypeId = mock(PublicationTypeId.class);

        Edition e1 = mock(Edition.class);
        EditionId id1 = mock(EditionId.class);
        when(e1.id()).thenReturn(id1);
        when(id1.getID()).thenReturn("edition-1");

        EditionDTO dto1 = buildEditionDTO("edition-1");

        when(service.findByPublicationTypeId(publicationTypeId)).thenReturn(List.of(e1));

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toPublicationTypeId(rawTypeId))
                    .thenReturn(publicationTypeId);
            mapper.when(() -> EditionMapper.toDTO(e1)).thenReturn(dto1);

            // ACT
            controller.findByPublicationTypeId(rawTypeId);

            // ASSERT
            List<Link> links = dto1.getLinks().toList();
            assertEquals(2, links.size());
            assertTrue(links.stream().anyMatch(l -> l.getRel().value().equals("self")));
            assertTrue(links.stream()
                    .anyMatch(l -> l.getRel().value().equals("find-by-publication-type")));
        }
    }

    // =========================================================
    // FIND BY PUBLICATION TYPE ID — NO CONTENT
    // =========================================================

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

    // =========================================================
    // FIND BY PUBLICATION TYPE ID — BAD REQUEST
    // =========================================================

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

    // =========================================================
    // FIND BY PUBLICATION TYPE ID — INTERNAL SERVER ERROR
    // =========================================================

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
    // FIND BY PUBLISHER ID — MAPPED DTO
    // =========================================================

    @Test
    void shouldReturnMappedEditionDTOWhenFindingByPublisherId() {

        String rawPublisherId = "publisher-1";
        PublisherID publisherID = mock(PublisherID.class);

        Edition edition = mock(Edition.class);
        EditionId editionId = mock(EditionId.class);

        when(edition.id()).thenReturn(editionId);
        when(editionId.getID()).thenReturn("edition-1");

        EditionDTO dto = buildEditionDTO("edition-1");

        when(service.findByPublisherId(publisherID)).thenReturn(List.of(edition));

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toPublisherId(rawPublisherId))
                    .thenReturn(publisherID);
            mapper.when(() -> EditionMapper.toDTO(edition)).thenReturn(dto);

            ResponseEntity<List<EditionDTO>> response =
                    controller.findByPublisherId(rawPublisherId);

            assertNotNull(response.getBody());
            assertSame(dto, response.getBody().get(0));
        }
    }

    // =========================================================
    // FIND BY PUBLICATION TYPE ID — MAPPED DTO
    // =========================================================

    @Test
    void shouldReturnMappedEditionDTOWhenFindingByPublicationTypeId() {

        String rawTypeId = "type-1";
        PublicationTypeId publicationTypeId = mock(PublicationTypeId.class);

        Edition edition = mock(Edition.class);
        EditionId editionId = mock(EditionId.class);

        when(edition.id()).thenReturn(editionId);
        when(editionId.getID()).thenReturn("edition-1");

        EditionDTO dto = buildEditionDTO("edition-1");

        when(service.findByPublicationTypeId(publicationTypeId)).thenReturn(List.of(edition));

        try (MockedStatic<EditionMapper> mapper = mockStatic(EditionMapper.class)) {

            mapper.when(() -> EditionMapper.toPublicationTypeId(rawTypeId))
                    .thenReturn(publicationTypeId);
            mapper.when(() -> EditionMapper.toDTO(edition)).thenReturn(dto);

            ResponseEntity<List<EditionDTO>> response =
                    controller.findByPublicationTypeId(rawTypeId);

            assertNotNull(response.getBody());
            assertSame(dto, response.getBody().get(0));
        }
    }

    // =========================================================
    // HELPERS
    // =========================================================

    /**
     * Configura os mocks estáticos do EditionMapper para que service.createAndSave()
     * seja chamado com sucesso, retornando a edition fornecida.
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
     * Configura os mocks estáticos do EditionMapper e faz service.createAndSave()
     * lançar a exceção fornecida.
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
     * Cria um {@link EditionDTO} com valores stub para uso nos testes.
     */
    private EditionDTO buildEditionDTO(String editionId) {
        return new EditionDTO(
                editionId,
                "1",
                "CODE-001",
                "type-1",
                "publisher-1",
                "Book",
                "pt",
                LocalDate.of(2024, 1, 1),
                "Hardcover",
                20.0,
                15.0,
                3.0,
                500.0,
                300
        );
    }
}