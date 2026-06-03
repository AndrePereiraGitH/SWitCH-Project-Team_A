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

import java.util.ArrayList;
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
    // CREATE - SUCCESS
    // ==========================================================
    @Test
    void createPublisher_shouldReturnCreated_onSuccess() {

        // Arrange
        RequestPublisherDTO requestDTO = mock(RequestPublisherDTO.class);
        UserId userId = mock(UserId.class);
        Publisher publisher = mock(Publisher.class);
        PublisherID publisherID = mock(PublisherID.class);
        Link providerLink = mock(Link.class);

        when(requestDTO.name()).thenReturn("Publisher");
        when(requestDTO.vatNumber()).thenReturn("123456789");
        when(publisher.id()).thenReturn(publisherID);
        when(publisherService.createAndSave(any(), any(), any(), any())).thenReturn(publisher);
        when(publisherLinkProvider.getLinksForPublisher(userId)).thenReturn(List.of(providerLink));

        try (MockedStatic<PublisherMapper> mapper = mockStatic(PublisherMapper.class)) {

            ResponsePublisherDTO dto = mock(ResponsePublisherDTO.class);
            List<Link> captured = new ArrayList<>();
            doAnswer(inv -> { captured.add(inv.getArgument(0)); return dto; }).when(dto).add(any(Link.class));

            mapper.when(() -> PublisherMapper.toUserId(any())).thenReturn(userId);
            mapper.when(() -> PublisherMapper.toContact(any())).thenReturn(mock(Contact.class));
            mapper.when(() -> PublisherMapper.toDTO(publisher)).thenReturn(dto);

            ResponseEntity<ResponsePublisherDTO> response =
                    controller.createPublisher(requestDTO, "user-1");

            // Assert
            assertEquals(CREATED, response.getStatusCode());
            assertEquals(dto, response.getBody());
            assertFalse(captured.isEmpty());
            assertTrue(captured.contains(providerLink));
        }
    }

    // ==========================================================
    // CREATE - BAD REQUEST
    // ==========================================================
    @Test
    void createPublisher_shouldReturnBadRequest_onIllegalArgumentException() {

        // Arrange
        RequestPublisherDTO requestDTO = mock(RequestPublisherDTO.class);

        when(requestDTO.name()).thenReturn("Publisher");
        when(requestDTO.vatNumber()).thenReturn("123456789");

        try (MockedStatic<PublisherMapper> mapper = mockStatic(PublisherMapper.class)) {

            mapper.when(() -> PublisherMapper.toUserId(any())).thenReturn(mock(UserId.class));
            mapper.when(() -> PublisherMapper.toContact(any())).thenReturn(mock(Contact.class));
            when(publisherService.createAndSave(any(), any(), any(), any()))
                    .thenThrow(new IllegalArgumentException());

            ResponseEntity<ResponsePublisherDTO> response =
                    controller.createPublisher(requestDTO, "user-1");

            // Assert
            assertEquals(BAD_REQUEST, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    // ==========================================================
    // CREATE - FORBIDDEN
    // ==========================================================
    @Test
    void createPublisher_shouldReturnForbidden_onAccessDeniedException() {

        // Arrange
        RequestPublisherDTO requestDTO = mock(RequestPublisherDTO.class);

        when(requestDTO.name()).thenReturn("Publisher");
        when(requestDTO.vatNumber()).thenReturn("123456789");

        try (MockedStatic<PublisherMapper> mapper = mockStatic(PublisherMapper.class)) {

            mapper.when(() -> PublisherMapper.toUserId(any())).thenReturn(mock(UserId.class));
            mapper.when(() -> PublisherMapper.toContact(any())).thenReturn(mock(Contact.class));
            when(publisherService.createAndSave(any(), any(), any(), any()))
                    .thenThrow(new AccessDeniedException("Denied"));

            // Act
            ResponseEntity<ResponsePublisherDTO> response =
                    controller.createPublisher(requestDTO, "user-1");

            // Assert
            assertEquals(FORBIDDEN, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    // ==========================================================
    // CREATE - INTERNAL ERROR
    // ==========================================================
    @Test
    void createPublisher_shouldReturnInternalServerError_onException() {

        // Arrange
        RequestPublisherDTO requestDTO = mock(RequestPublisherDTO.class);

        when(requestDTO.name()).thenReturn("Publisher");
        when(requestDTO.vatNumber()).thenReturn("123456789");

        try (MockedStatic<PublisherMapper> mapper = mockStatic(PublisherMapper.class)) {

            mapper.when(() -> PublisherMapper.toUserId(any())).thenReturn(mock(UserId.class));
            mapper.when(() -> PublisherMapper.toContact(any())).thenReturn(mock(Contact.class));
            when(publisherService.createAndSave(any(), any(), any(), any()))
                    .thenThrow(new RuntimeException());

            // Act
            ResponseEntity<ResponsePublisherDTO> response =
                    controller.createPublisher(requestDTO, "user-1");

            // Assert
            assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    // ==========================================================
    // GET ALL - SUCCESS
    // ==========================================================
    @Test
    void getAllPublishers_shouldReturnOk_whenPublishersExist() {

        // Arrange
        Publisher publisher = mock(Publisher.class);
        PublisherID id = mock(PublisherID.class);

        when(publisher.id()).thenReturn(id);
        when(publisherService.findAll()).thenReturn(List.of(publisher));

        ResponsePublisherDTO dto = mock(ResponsePublisherDTO.class);

        try (MockedStatic<PublisherMapper> mapper = mockStatic(PublisherMapper.class)) {

            mapper.when(() -> PublisherMapper.toDTO(any())).thenReturn(dto);

            // Act
            ResponseEntity<List<ResponsePublisherDTO>> response = controller.getAllPublishers();


            // Assert
            assertEquals(OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
            assertEquals(dto, response.getBody().get(0));
        }
    }

    // ==========================================================
    // GET ALL - EMPTY
    // ==========================================================
    @Test
    void getAllPublishers_shouldReturnNoContent_whenListEmpty() {

        // Arrange
        when(publisherService.findAll()).thenReturn(List.of());

        // Act
        ResponseEntity<List<ResponsePublisherDTO>> response = controller.getAllPublishers();

        // Assert
        assertEquals(NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    // ==========================================================
    // GET ALL - BAD REQUEST
    // ==========================================================
    @Test
    void getAllPublishers_shouldReturnBadRequest_onIllegalArgumentException() {

        when(publisherService.findAll()).thenThrow(new IllegalArgumentException());

        // Act
        ResponseEntity<List<ResponsePublisherDTO>> response = controller.getAllPublishers();

        // Assert
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    // ==========================================================
    // GET ALL - INTERNAL ERROR
    // ==========================================================
    @Test
    void getAllPublishers_shouldReturnInternalServerError_onException() {

        when(publisherService.findAll()).thenThrow(new RuntimeException());

        // Act
        ResponseEntity<List<ResponsePublisherDTO>> response = controller.getAllPublishers();

        // Assert
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

        try (MockedStatic<PublisherMapper> mapper = mockStatic(PublisherMapper.class)) {

            mapper.when(() -> PublisherMapper.toPublisherId(any())).thenReturn(id);
            mapper.when(() -> PublisherMapper.toDTO(any())).thenReturn(dto);

            // Act
            ResponseEntity<ResponsePublisherDTO> response = controller.getPublisher("pub-1");

            // Assert
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

        when(publisherService.findById(id)).thenThrow(new IllegalArgumentException());

        try (MockedStatic<PublisherMapper> mapper = mockStatic(PublisherMapper.class)) {

            mapper.when(() -> PublisherMapper.toPublisherId(any())).thenReturn(id);

            // Act
            ResponseEntity<ResponsePublisherDTO> response = controller.getPublisher("pub-1");

            // Assert
            assertEquals(NOT_FOUND, response.getStatusCode());
        }
    }

    // ==========================================================
    // GET BY ID - INTERNAL ERROR
    // ==========================================================
    @Test
    void getPublisher_shouldReturnInternalServerError_onException() {

        PublisherID id = mock(PublisherID.class);

        when(publisherService.findById(id)).thenThrow(new RuntimeException());

        try (MockedStatic<PublisherMapper> mapper = mockStatic(PublisherMapper.class)) {

            mapper.when(() -> PublisherMapper.toPublisherId(any())).thenReturn(id);

            // Act
            ResponseEntity<ResponsePublisherDTO> response = controller.getPublisher("pub-1");

            // Assert
            assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
    }

}