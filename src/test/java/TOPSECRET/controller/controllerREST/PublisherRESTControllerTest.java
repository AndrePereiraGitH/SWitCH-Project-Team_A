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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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

        try (MockedStatic<PublisherMapper> mapperMock =
                     mockStatic(PublisherMapper.class)) {

            mapperMock.when(() -> PublisherMapper.toContact(any()))
                    .thenReturn(mock(Contact.class));

            mapperMock.when(() -> PublisherMapper.toDTO(any()))
                    .thenReturn(responseDTO);

            // ACT
            ResponseEntity<ResponsePublisherDTO> response =
                    controller.createPublisher(requestDTO, userId);

            // ASSERT
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals(responseDTO, response.getBody());
        }
    }

    // ==========================================================
    // CREATE - BAD REQUEST
    // ==========================================================
    @Test
    void createPublisher_shouldReturnBadRequest_onIllegalArgumentException() {

        // ARRANGE
        RequestPublisherDTO requestDTO = mock(RequestPublisherDTO.class);
        UserId userId = mock(UserId.class);

        when(requestDTO.name()).thenReturn("Publisher");
        when(requestDTO.vatNumber()).thenReturn("123456789");

        try (MockedStatic<PublisherMapper> mapperMock =
                     mockStatic(PublisherMapper.class)) {

            mapperMock.when(() -> PublisherMapper.toContact(any()))
                    .thenReturn(mock(Contact.class));

            when(publisherService.createAndSave(any(), any(), any(), any()))
                    .thenThrow(new IllegalArgumentException());

            // ACT
            ResponseEntity<ResponsePublisherDTO> response =
                    controller.createPublisher(requestDTO, userId);

            // ASSERT
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    // ==========================================================
    // CREATE - FORBIDDEN
    // ==========================================================
    @Test
    void createPublisher_shouldReturnForbidden_onAccessDeniedException() {

        // ARRANGE
        RequestPublisherDTO requestDTO = mock(RequestPublisherDTO.class);
        UserId userId = mock(UserId.class);

        when(requestDTO.name()).thenReturn("Publisher");
        when(requestDTO.vatNumber()).thenReturn("123456789");

        try (MockedStatic<PublisherMapper> mapperMock =
                     mockStatic(PublisherMapper.class)) {

            mapperMock.when(() -> PublisherMapper.toContact(any()))
                    .thenReturn(mock(Contact.class));

            when(publisherService.createAndSave(any(), any(), any(), any()))
                    .thenThrow(new AccessDeniedException("Denied"));

            // ACT
            ResponseEntity<ResponsePublisherDTO> response =
                    controller.createPublisher(requestDTO, userId);

            // ASSERT
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    // ==========================================================
    // CREATE - INTERNAL ERROR
    // ==========================================================
    @Test
    void createPublisher_shouldReturnInternalServerError_onException() {

        // ARRANGE
        RequestPublisherDTO requestDTO = mock(RequestPublisherDTO.class);
        UserId userId = mock(UserId.class);

        when(requestDTO.name()).thenReturn("Publisher");
        when(requestDTO.vatNumber()).thenReturn("123456789");

        try (MockedStatic<PublisherMapper> mapperMock =
                     mockStatic(PublisherMapper.class)) {

            mapperMock.when(() -> PublisherMapper.toContact(any()))
                    .thenReturn(mock(Contact.class));

            when(publisherService.createAndSave(any(), any(), any(), any()))
                    .thenThrow(new RuntimeException());

            // ACT
            ResponseEntity<ResponsePublisherDTO> response =
                    controller.createPublisher(requestDTO, userId);

            // ASSERT
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    // ==========================================================
    // GET ALL - SUCCESS
    // ==========================================================
    @Test
    void getAllPublishers_shouldReturnOk_whenPublishersExist() {

        // ARRANGE
        Publisher publisher = mock(Publisher.class);
        PublisherID publisherID = mock(PublisherID.class);
        ResponsePublisherDTO responseDTO = mock(ResponsePublisherDTO.class);

        when(publisher.id()).thenReturn(publisherID);
        when(publisherService.findAll()).thenReturn(List.of(publisher));

        try (MockedStatic<PublisherMapper> mapperMock =
                     mockStatic(PublisherMapper.class)) {

            mapperMock.when(() -> PublisherMapper.toDTO(any()))
                    .thenReturn(responseDTO);

            // ACT
            ResponseEntity<List<ResponsePublisherDTO>> response =
                    controller.getAllPublishers();

            // ASSERT
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    // ==========================================================
    // GET ALL - NO CONTENT
    // ==========================================================
    @Test
    void getAllPublishers_shouldReturnNoContent_whenListEmpty() {

        // ARRANGE
        when(publisherService.findAll()).thenReturn(new ArrayList<>());

        // ACT
        ResponseEntity<List<ResponsePublisherDTO>> response =
                controller.getAllPublishers();

        // ASSERT
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    // ==========================================================
    // GET ALL - BAD REQUEST
    // ==========================================================
    @Test
    void getAllPublishers_shouldReturnBadRequest_onIllegalArgumentException() {

        // ARRANGE
        when(publisherService.findAll())
                .thenThrow(new IllegalArgumentException());

        // ACT
        ResponseEntity<List<ResponsePublisherDTO>> response =
                controller.getAllPublishers();

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    // ==========================================================
    // GET ALL - INTERNAL ERROR
    // ==========================================================
    @Test
    void getAllPublishers_shouldReturnInternalServerError_onException() {

        // ARRANGE
        when(publisherService.findAll())
                .thenThrow(new RuntimeException());

        // ACT
        ResponseEntity<List<ResponsePublisherDTO>> response =
                controller.getAllPublishers();

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // ==========================================================
    // GET BY ID - SUCCESS
    // ==========================================================
    @Test
    void getPublisher_shouldReturnOk_whenFound() {

        // ARRANGE
        PublisherID id = mock(PublisherID.class);
        Publisher publisher = mock(Publisher.class);
        ResponsePublisherDTO responseDTO = mock(ResponsePublisherDTO.class);

        when(publisherService.findById(id)).thenReturn(publisher);

        try (MockedStatic<PublisherMapper> mapperMock =
                     mockStatic(PublisherMapper.class)) {

            mapperMock.when(() -> PublisherMapper.toDTO(any()))
                    .thenReturn(responseDTO);

            // ACT
            ResponseEntity<ResponsePublisherDTO> response =
                    controller.getPublisher(id);

            // ASSERT
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(responseDTO, response.getBody());
        }
    }

    // ==========================================================
    // GET BY ID - NOT FOUND
    // ==========================================================
    @Test
    void getPublisher_shouldReturnNotFound_whenMissing() {

        // ARRANGE
        PublisherID id = mock(PublisherID.class);

        when(publisherService.findById(id))
                .thenThrow(new IllegalArgumentException());

        // ACT
        ResponseEntity<ResponsePublisherDTO> response =
                controller.getPublisher(id);

        // ASSERT
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // ==========================================================
    // GET BY ID - INTERNAL ERROR
    // ==========================================================
    @Test
    void getPublisher_shouldReturnInternalServerError_onException() {

        // ARRANGE
        PublisherID id = mock(PublisherID.class);

        when(publisherService.findById(id))
                .thenThrow(new RuntimeException());

        // ACT
        ResponseEntity<ResponsePublisherDTO> response =
                controller.getPublisher(id);

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}