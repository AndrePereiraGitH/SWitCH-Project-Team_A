package TOPSECRET.controller.controllerREST.root.linkProvider;

import TOPSECRET.controller.controllerREST.PublicationTypeRestController;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.ValueObjects.UserId;
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

import javax.management.InstanceAlreadyExistsException;
import java.nio.file.AccessDeniedException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import TOPSECRET.dto.PublicationType.PublicationTypeMapper;

/**
 * Unit tests for {@link PublicationTypeRestController}.
 */
@ExtendWith(MockitoExtension.class)
class PublicationTypeRestControllerTest {

    @Mock
    private PublicationTypeService service;

    @Mock
    private PublicationTypeLinkProvider linkProvider;

    @InjectMocks
    private PublicationTypeRestController controller;

    private CreatePublicationTypeDTO createDTO;
    private UserId userId;

    @BeforeEach
    void setup() {

        createDTO = mock(CreatePublicationTypeDTO.class);
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
                    controller.createPublicationType(
                            createDTO,
                            userId
                    );

            // ASSERT
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals(responseDTO,response.getBody());
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
        when(service.createAndSave(format, userId)).thenThrow(new IllegalArgumentException());

        // ACT
        ResponseEntity<PublicationTypeResponseDTO> response =
                controller.createPublicationType(
                        createDTO,
                        userId
                );

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
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
        when(service.createAndSave(format, userId)).thenThrow(new InstanceAlreadyExistsException());

        // ACT
        ResponseEntity<PublicationTypeResponseDTO> response =
                controller.createPublicationType(
                        createDTO,
                        userId
                );

        // ASSERT
        assertEquals(
                HttpStatus.CONFLICT,
                response.getStatusCode()
        );
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

        when(service.createAndSave(format, userId)).thenThrow(new AccessDeniedException("Denied"));

        // ACT
        ResponseEntity<PublicationTypeResponseDTO> response =
                controller.createPublicationType(
                        createDTO,
                        userId
                );

        // ASSERT
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    // =========================================================
    // CREATE INTERNAL SERVER ERROR
    // =========================================================

    @Test
    void shouldReturnInternalServerErrorWhenUnexpectedExceptionOccurs()
            throws Exception {

        // ARRANGE
        String format = "Book";

        when(createDTO.mediaFormat()).thenReturn(format);

        when(service.createAndSave(format, userId)).thenThrow(new RuntimeException());

        // ACT
        ResponseEntity<PublicationTypeResponseDTO> response =
                controller.createPublicationType(
                        createDTO,
                        userId
                );

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

        PublicationTypeResponseDTO dto1 =
                mock(PublicationTypeResponseDTO.class);

        PublicationTypeResponseDTO dto2 =
                mock(PublicationTypeResponseDTO.class);

        List<PublicationType> publicationTypes =
                List.of(pt1, pt2);

        List<PublicationTypeResponseDTO> dtoList =
                List.of(dto1, dto2);

        when(service.findAll()).thenReturn(publicationTypes);

        try (MockedStatic<PublicationTypeMapper> mapper =
                     mockStatic(PublicationTypeMapper.class)) {

            mapper.when(() ->
                            PublicationTypeMapper.toDTOList(
                                    publicationTypes))
                    .thenReturn(dtoList);

            // ACT
            ResponseEntity<List<PublicationTypeResponseDTO>>
                    response =
                    controller.getAllPublicationTypes();

            // ASSERT
            assertEquals(HttpStatus.OK, response.getStatusCode()
            );

            assertEquals(dtoList, response.getBody());
        }
    }

    // =========================================================
    // GET ALL BAD REQUEST
    // =========================================================

    @Test
    void shouldReturnBadRequestWhenGetAllThrowsIllegalArgumentException() {

        // ARRANGE
        when(service.findAll()).thenThrow(new IllegalArgumentException());

        // ACT
        ResponseEntity<List<PublicationTypeResponseDTO>>
                response =
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
        ResponseEntity<List<PublicationTypeResponseDTO>>
                response =
                controller.getAllPublicationTypes();

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
    @Test
    void shouldReturnPublicationTypeLinks() {

        // ARRANGE
        PublicationTypeLinkProvider provider = new PublicationTypeLinkProvider();

        UserId userId = mock(UserId.class);

        // ACT
        List<Link> links = provider.getLinksForPublicationType(userId);

        // ASSERT
        assertNotNull(links);
        assertFalse(links.isEmpty());
        assertEquals(1, links.size());
        assertEquals("create-publication-type",
                links.get(0).getRel().value()

        );

    }
}