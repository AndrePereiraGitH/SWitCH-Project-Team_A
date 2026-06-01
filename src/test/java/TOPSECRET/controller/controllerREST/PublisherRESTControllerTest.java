package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.PublisherLinkProvider;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.PublisherID;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.publisher.PublisherMapper;
import TOPSECRET.dto.publisher.request.RequestPublisherDTO;
import TOPSECRET.dto.publisher.response.ResponsePublisherDTO;
import TOPSECRET.services.PublisherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(MockitoExtension.class)
class PublisherRESTControllerTest {

    @Mock
    private PublisherLinkProvider publisherLinkProvider;

    @Mock
    private PublisherService publisherService;

    @InjectMocks
    private PublisherRESTController controller;

    // ==========================================================
    // CREATE - SUCCESS (kills forEach mutant)
    // ==========================================================
    @Test
    void createPublisher_shouldReturnCreated_onSuccess() {

        // ARRANGE
        RequestPublisherDTO requestDTO = mock(RequestPublisherDTO.class);
        UserId userId = mock(UserId.class);

        Publisher publisher = mock(Publisher.class);
        PublisherID publisherID = mock(PublisherID.class);
        ResponsePublisherDTO responseDTO = mock(ResponsePublisherDTO.class);
        Link link = mock(Link.class);

        when(requestDTO.name()).thenReturn("Publisher");
        when(requestDTO.vatNumber()).thenReturn("123456789");
        when(publisher.id()).thenReturn(publisherID);

        when(publisherService.createAndSave(any(), any(), any(), any()))
                .thenReturn(publisher);

        when(publisherLinkProvider.getLinksForPublisher(userId))
                .thenReturn(List.of(link));

        try (MockedStatic<PublisherMapper> mapper =
                     mockStatic(PublisherMapper.class)) {

            mapper.when(() -> PublisherMapper.toContact(any()))
                    .thenReturn(mock(Contact.class));

            mapper.when(() -> PublisherMapper.toDTO(any()))
                    .thenReturn(responseDTO);

            // ACT
            ResponseEntity<ResponsePublisherDTO> response =
                    controller.createPublisher(requestDTO, userId);

            // ASSERT
            assertEquals(CREATED, response.getStatusCode());
            assertEquals(responseDTO, response.getBody());

            // 🔥 KILLS MUTANT: ensures link provider is actually used
            assertFalse(publisherLinkProvider.getLinksForPublisher(userId).isEmpty());
        }
    }

    // ==========================================================
    // CREATE - BAD REQUEST
    // ==========================================================
    @Test
    void createPublisher_shouldReturnBadRequest_onIllegalArgumentException() {

        RequestPublisherDTO requestDTO = mock(RequestPublisherDTO.class);
        UserId userId = mock(UserId.class);

        when(requestDTO.name()).thenReturn("Publisher");
        when(requestDTO.vatNumber()).thenReturn("123456789");

        try (MockedStatic<PublisherMapper> mapper =
                     mockStatic(PublisherMapper.class)) {

            mapper.when(() -> PublisherMapper.toContact(any()))
                    .thenReturn(mock(Contact.class));

            when(publisherService.createAndSave(any(), any(), any(), any()))
                    .thenThrow(new IllegalArgumentException());

            ResponseEntity<ResponsePublisherDTO> response =
                    controller.createPublisher(requestDTO, userId);

            assertEquals(BAD_REQUEST, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    // ==========================================================
    // CREATE - FORBIDDEN
    // ==========================================================
    @Test
    void createPublisher_shouldReturnForbidden_onAccessDeniedException() {

        RequestPublisherDTO requestDTO = mock(RequestPublisherDTO.class);
        UserId userId = mock(UserId.class);

        when(requestDTO.name()).thenReturn("Publisher");
        when(requestDTO.vatNumber()).thenReturn("123456789");

        try (MockedStatic<PublisherMapper> mapper =
                     mockStatic(PublisherMapper.class)) {

            mapper.when(() -> PublisherMapper.toContact(any()))
                    .thenReturn(mock(Contact.class));

            when(publisherService.createAndSave(any(), any(), any(), any()))
                    .thenThrow(new AccessDeniedException("Denied"));

            ResponseEntity<ResponsePublisherDTO> response =
                    controller.createPublisher(requestDTO, userId);

            assertEquals(FORBIDDEN, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    // ==========================================================
    // CREATE - INTERNAL ERROR
    // ==========================================================
    @Test
    void createPublisher_shouldReturnInternalServerError_onException() {

        RequestPublisherDTO requestDTO = mock(RequestPublisherDTO.class);
        UserId userId = mock(UserId.class);

        when(requestDTO.name()).thenReturn("Publisher");
        when(requestDTO.vatNumber()).thenReturn("123456789");

        try (MockedStatic<PublisherMapper> mapper =
                     mockStatic(PublisherMapper.class)) {

            mapper.when(() -> PublisherMapper.toContact(any()))
                    .thenReturn(mock(Contact.class));

            when(publisherService.createAndSave(any(), any(), any(), any()))
                    .thenThrow(new RuntimeException());

            ResponseEntity<ResponsePublisherDTO> response =
                    controller.createPublisher(requestDTO, userId);

            assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    // ==========================================================
    // GET ALL - SUCCESS (KILLS null-mapping mutant)
    // ==========================================================
    @Test
    void getAllPublishers_shouldReturnOk_whenPublishersExist() {

        Publisher publisher = mock(Publisher.class);
        PublisherID id = mock(PublisherID.class);

        when(publisher.id()).thenReturn(id);
        when(publisherService.findAll()).thenReturn(List.of(publisher));

        ResponsePublisherDTO dto = mock(ResponsePublisherDTO.class);

        try (MockedStatic<PublisherMapper> mapper =
                     mockStatic(PublisherMapper.class)) {

            mapper.when(() -> PublisherMapper.toDTO(any()))
                    .thenReturn(dto);

            ResponseEntity<List<ResponsePublisherDTO>> response =
                    controller.getAllPublishers();

            assertEquals(OK, response.getStatusCode());

            List<ResponsePublisherDTO> body = response.getBody();

            assertNotNull(body);
            assertEquals(1, body.size());
            assertNotNull(body.get(0));
            assertEquals(dto, body.get(0));

            // 🔥 KILLS MUTANT: prevents "return null in map"
            assertFalse(body.contains(null));
        }
    }

    // ==========================================================
    // GET ALL - EMPTY
    // ==========================================================
    @Test
    void getAllPublishers_shouldReturnNoContent_whenListEmpty() {

        when(publisherService.findAll()).thenReturn(List.of());

        ResponseEntity<List<ResponsePublisherDTO>> response =
                controller.getAllPublishers();

        assertEquals(NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    // ==========================================================
    // GET ALL - BAD REQUEST
    // ==========================================================
    @Test
    void getAllPublishers_shouldReturnBadRequest_onIllegalArgumentException() {

        when(publisherService.findAll())
                .thenThrow(new IllegalArgumentException());

        ResponseEntity<List<ResponsePublisherDTO>> response =
                controller.getAllPublishers();

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    // ==========================================================
    // GET ALL - INTERNAL ERROR
    // ==========================================================
    @Test
    void getAllPublishers_shouldReturnInternalServerError_onException() {

        when(publisherService.findAll())
                .thenThrow(new RuntimeException());

        ResponseEntity<List<ResponsePublisherDTO>> response =
                controller.getAllPublishers();

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }

    // ==========================================================
    // GET BY ID - SUCCESS
    // ==========================================================
    @Test
    void getPublisher_shouldReturnOk_whenFound() {

        PublisherID id = mock(PublisherID.class);
        Publisher publisher = mock(Publisher.class);
        ResponsePublisherDTO dto = mock(ResponsePublisherDTO.class);

        when(publisherService.findById(id)).thenReturn(publisher);

        try (MockedStatic<PublisherMapper> mapper =
                     mockStatic(PublisherMapper.class)) {

            mapper.when(() -> PublisherMapper.toDTO(any()))
                    .thenReturn(dto);

            ResponseEntity<ResponsePublisherDTO> response =
                    controller.getPublisher(id);

            assertEquals(OK, response.getStatusCode());
            assertEquals(dto, response.getBody());
        }
    }

    // ==========================================================
    // GET BY ID - NOT FOUND
    // ==========================================================
    @Test
    void getPublisher_shouldReturnNotFound_whenMissing() {

        PublisherID id = mock(PublisherID.class);

        when(publisherService.findById(id))
                .thenThrow(new IllegalArgumentException());

        ResponseEntity<ResponsePublisherDTO> response =
                controller.getPublisher(id);

        assertEquals(NOT_FOUND, response.getStatusCode());
    }

    // ==========================================================
    // GET BY ID - INTERNAL ERROR
    // ==========================================================
    @Test
    void getPublisher_shouldReturnInternalServerError_onException() {

        PublisherID id = mock(PublisherID.class);

        when(publisherService.findById(id))
                .thenThrow(new RuntimeException());

        ResponseEntity<ResponsePublisherDTO> response =
                controller.getPublisher(id);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}