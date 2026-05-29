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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PublisherRESTController}.
 */
@ExtendWith(MockitoExtension.class)
class PublisherRESTControllerTest {

    @Mock
    private PublisherLinkProvider publisherLinkProvider;

    @Mock
    private PublisherService publisherService;

    @InjectMocks
    private PublisherRESTController controller;

    // ========== Tests for createPublisher =========================

    @Test
    void createPublisher_shouldReturnCreated_onSuccess() {

        // ARRANGE
        RequestPublisherDTO requestDouble = mock(RequestPublisherDTO.class);
        UserId userIdDouble = mock(UserId.class);
        Publisher publisherDouble = mock(Publisher.class);
        ResponsePublisherDTO responseDTO = mock(ResponsePublisherDTO.class);

        when(requestDouble.name()).thenReturn("Name");
        when(requestDouble.vatNumber()).thenReturn("VAT");
        when(publisherService.createAndSave(any(), any(), any(), any()))
                .thenReturn(publisherDouble);
        when(publisherLinkProvider.getLinksForPublisher(any()))
                .thenReturn(new ArrayList<>());

        try (MockedStatic<PublisherMapper> mapperMock = mockStatic(PublisherMapper.class)) {
            mapperMock.when(() -> PublisherMapper.toContact(any())).thenReturn(mock(Contact.class));
            mapperMock.when(() -> PublisherMapper.toDTO(any())).thenReturn(responseDTO);

            // ACT
            ResponseEntity<ResponsePublisherDTO> response =
                    controller.createPublisher(requestDouble, userIdDouble);

            // ASSERT
            assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());
            assertEquals(responseDTO, response.getBody());
        }
    }

    @Test
    void createPublisher_shouldReturnBadRequest_onIllegalArgument() {

        // ARRANGE
        RequestPublisherDTO requestDouble = mock(RequestPublisherDTO.class);
        UserId userIdDouble = mock(UserId.class);

        when(requestDouble.name()).thenReturn("Name");
        when(requestDouble.vatNumber()).thenReturn("VAT");

        try (MockedStatic<PublisherMapper> mapperMock = mockStatic(PublisherMapper.class)) {
            mapperMock.when(() -> PublisherMapper.toContact(any())).thenReturn(mock(Contact.class));
            when(publisherService.createAndSave(any(), any(), any(), any()))
                    .thenThrow(new IllegalArgumentException());

            // ACT
            ResponseEntity<ResponsePublisherDTO> response =
                    controller.createPublisher(requestDouble, userIdDouble);

            // ASSERT
            assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
            assertNull(response.getBody());
        }
    }

    @Test
    void createPublisher_shouldReturnForbidden_onAccessDenied() {

        // ARRANGE
        RequestPublisherDTO requestDouble = mock(RequestPublisherDTO.class);
        UserId userIdDouble = mock(UserId.class);

        when(requestDouble.name()).thenReturn("Name");
        when(requestDouble.vatNumber()).thenReturn("VAT");

        try (MockedStatic<PublisherMapper> mapperMock = mockStatic(PublisherMapper.class)) {
            mapperMock.when(() -> PublisherMapper.toContact(any())).thenReturn(mock(Contact.class));
            when(publisherService.createAndSave(any(), any(), any(), any()))
                    .thenThrow(new AccessDeniedException("denied"));

            // ACT
            ResponseEntity<ResponsePublisherDTO> response =
                    controller.createPublisher(requestDouble, userIdDouble);

            // ASSERT
            assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode().value());
            assertNull(response.getBody());
        }
    }

    @Test
    void createPublisher_shouldReturnInternalError_onException() {

        // ARRANGE
        RequestPublisherDTO requestDouble = mock(RequestPublisherDTO.class);
        UserId userIdDouble = mock(UserId.class);

        when(requestDouble.name()).thenReturn("Name");
        when(requestDouble.vatNumber()).thenReturn("VAT");

        try (MockedStatic<PublisherMapper> mapperMock = mockStatic(PublisherMapper.class)) {
            mapperMock.when(() -> PublisherMapper.toContact(any())).thenReturn(mock(Contact.class));
            when(publisherService.createAndSave(any(), any(), any(), any()))
                    .thenThrow(new RuntimeException());

            // ACT
            ResponseEntity<ResponsePublisherDTO> response =
                    controller.createPublisher(requestDouble, userIdDouble);

            // ASSERT
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode().value());
            assertNull(response.getBody());
        }
    }

    // ========== Tests for getAllPublishers =========================

    @Test
    void getAllPublishers_shouldReturnOk_whenPublishersExist() {

        // ARRANGE
        Publisher publisherDouble = mock(Publisher.class);
        ResponsePublisherDTO responseDTO = mock(ResponsePublisherDTO.class);

        when(publisherService.findAll()).thenReturn(List.of(publisherDouble));

        try (MockedStatic<PublisherMapper> mapperMock = mockStatic(PublisherMapper.class)) {
            mapperMock.when(() -> PublisherMapper.toDTO(any())).thenReturn(responseDTO);

            // ACT
            ResponseEntity<List<ResponsePublisherDTO>> response = controller.getAllPublishers();

            // ASSERT
            assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
            assertNotNull(response.getBody());
            assertEquals(1, response.getBody().size());
        }
    }

    @Test
    void getAllPublishers_shouldReturnNoContent_whenNoPublishersExist() {

        // ARRANGE
        when(publisherService.findAll()).thenReturn(new ArrayList<>());

        // ACT
        ResponseEntity<List<ResponsePublisherDTO>> response = controller.getAllPublishers();

        // ASSERT
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void getAllPublishers_shouldReturnInternalError_onException() {

        // ARRANGE
        when(publisherService.findAll()).thenThrow(new RuntimeException());

        // ACT
        ResponseEntity<List<ResponsePublisherDTO>> response = controller.getAllPublishers();

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode().value());
    }

    // ========== Tests for getPublisher =========================

    @Test
    void getPublisher_shouldReturnOk_whenPublisherExists() {

        // ARRANGE
        PublisherID idDouble = mock(PublisherID.class);
        Publisher publisherDouble = mock(Publisher.class);
        ResponsePublisherDTO responseDTO = mock(ResponsePublisherDTO.class);

        when(publisherService.findById(idDouble)).thenReturn(Optional.of(publisherDouble));

        try (MockedStatic<PublisherMapper> mapperMock = mockStatic(PublisherMapper.class)) {
            mapperMock.when(() -> PublisherMapper.toDTO(any())).thenReturn(responseDTO);

            // ACT
            ResponseEntity<ResponsePublisherDTO> response = controller.getPublisher(idDouble);

            // ASSERT
            assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
            assertEquals(responseDTO, response.getBody());
        }
    }

    @Test
    void getPublisher_shouldReturnNotFound_whenPublisherDoesNotExist() {

        // ARRANGE
        PublisherID idDouble = mock(PublisherID.class);

        when(publisherService.findById(idDouble)).thenReturn(Optional.empty());

        // ACT
        ResponseEntity<ResponsePublisherDTO> response = controller.getPublisher(idDouble);

        // ASSERT
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void getPublisher_shouldReturnInternalError_onException() {

        // ARRANGE
        PublisherID idDouble = mock(PublisherID.class);

        when(publisherService.findById(idDouble)).thenThrow(new RuntimeException());

        // ACT
        ResponseEntity<ResponsePublisherDTO> response = controller.getPublisher(idDouble);

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode().value());
    }
}