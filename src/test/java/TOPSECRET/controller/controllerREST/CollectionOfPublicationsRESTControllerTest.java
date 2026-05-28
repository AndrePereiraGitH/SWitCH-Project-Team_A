package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.CollectionOfPublicationsLinkProvider;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.collectionOfPublications.CollectionOfPublicationsMapper;
import TOPSECRET.dto.collectionOfPublications.request.RequestCollectionOfPublicationsDTO;
import TOPSECRET.dto.collectionOfPublications.response.ResponseCollectionOfPublicationsDTO;
import TOPSECRET.services.CollectionOfPublicationsService;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CollectionOfPublicationsRESTController}.
 */
@ExtendWith(MockitoExtension.class)
class CollectionOfPublicationsRESTControllerTest {


    @Mock
    private CollectionOfPublicationsService collectionService;

    @Mock
    private CollectionOfPublicationsLinkProvider linkProvider;

    @InjectMocks
    private CollectionOfPublicationsRESTController controller;

    @Test
    void createCollectionOfPublications_shouldReturnCreated_onSuccess()
            throws Exception {

        // ARRANGE
        RequestCollectionOfPublicationsDTO requestDouble =
                mock(RequestCollectionOfPublicationsDTO.class);
        UserId userIdDouble = mock(UserId.class);
        GenreId genreIdDouble = mock(GenreId.class);
        CollectionOfPublications collectionDouble = mock(CollectionOfPublications.class);
        ResponseCollectionOfPublicationsDTO responseDTO =
                mock(ResponseCollectionOfPublicationsDTO.class);

        when(requestDouble.collectionName()).thenReturn("My Collection");

        when(collectionService.createAndSave(any(), any(), any()))
                .thenReturn(collectionDouble);

        when(linkProvider.getLinksForCollectionOfPublications(any()))
                .thenReturn(new ArrayList<>());

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() -> CollectionOfPublicationsMapper.toDTO(any()))
                    .thenReturn(responseDTO);

            // ACT
            ResponseEntity<ResponseCollectionOfPublicationsDTO> response =
                    controller.createCollectionOfPublications(requestDouble, userIdDouble);

            // ASSERT
            assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());
            assertEquals(responseDTO, response.getBody());
        }
    }

    @Test
    void createCollectionOfPublications_shouldReturnBadRequest_onIllegalArgument()
            throws Exception {

        // ARRANGE
        RequestCollectionOfPublicationsDTO requestDouble =
                mock(RequestCollectionOfPublicationsDTO.class);
        UserId userIdDouble = mock(UserId.class);
        GenreId genreIdDouble = mock(GenreId.class);

        when(requestDouble.collectionName()).thenReturn("My Collection");

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() -> CollectionOfPublicationsMapper.toDTO(any()))
                    .thenReturn(mock(ResponseCollectionOfPublicationsDTO.class));

            when(collectionService.createAndSave(any(), any(), any()))
                    .thenThrow(new IllegalArgumentException());

            // ACT
            ResponseEntity<ResponseCollectionOfPublicationsDTO> response =
                    controller.createCollectionOfPublications(requestDouble, userIdDouble);

            // ASSERT
            assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
            assertNull(response.getBody());
        }
    }

    @Test
    void createCollectionOfPublications_shouldReturnForbidden_onAccessDenied()
            throws Exception {

        // ARRANGE
        RequestCollectionOfPublicationsDTO requestDouble =
                mock(RequestCollectionOfPublicationsDTO.class);
        UserId userIdDouble = mock(UserId.class);
        GenreId genreIdDouble = mock(GenreId.class);

        when(requestDouble.collectionName()).thenReturn("My Collection");

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() -> CollectionOfPublicationsMapper.toDTO(any()))
                    .thenReturn(mock(ResponseCollectionOfPublicationsDTO.class));

            when(collectionService.createAndSave(any(), any(), any()))
                    .thenThrow(new AccessDeniedException("denied"));

            // ACT
            ResponseEntity<ResponseCollectionOfPublicationsDTO> response =
                    controller.createCollectionOfPublications(requestDouble, userIdDouble);

            // ASSERT
            assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode().value());
            assertNull(response.getBody());
        }
    }

    @Test
    void createCollectionOfPublications_shouldReturnInternalServerError_onException()
            throws Exception {

        // ARRANGE
        RequestCollectionOfPublicationsDTO requestDouble =
                mock(RequestCollectionOfPublicationsDTO.class);
        UserId userIdDouble = mock(UserId.class);
        GenreId genreIdDouble = mock(GenreId.class);

        when(requestDouble.collectionName()).thenReturn("My Collection");

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() -> CollectionOfPublicationsMapper.toDTO(any()))
                    .thenReturn(mock(ResponseCollectionOfPublicationsDTO.class));

            when(collectionService.createAndSave(any(), any(), any()))
                    .thenThrow(new RuntimeException());

            // ACT
            ResponseEntity<ResponseCollectionOfPublicationsDTO> response =
                    controller.createCollectionOfPublications(requestDouble, userIdDouble);

            // ASSERT
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode().value());
            assertNull(response.getBody());
        }
    }

    // -------------------------------------------------------------------------
// makeCollectionPublic
// -------------------------------------------------------------------------

    @Test
    void makeCollectionPublic_shouldReturnOk_onSuccess()
            throws Exception {

        // ARRANGE
        CollectionOfPublicationsId collectionIdDouble = mock(CollectionOfPublicationsId.class);
        UserId userIdDouble = mock(UserId.class);

        when(collectionService.makeCollectionPublic(collectionIdDouble, userIdDouble))
                .thenReturn(true);

        // ACT
        ResponseEntity<Boolean> response =
                controller.makeCollectionPublic(collectionIdDouble, userIdDouble);

        // ASSERT
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertTrue(response.getBody());
    }

    @Test
    void makeCollectionPublic_shouldReturnOkWithFalse_whenAlreadyPublic()
            throws Exception {

        // ARRANGE
        CollectionOfPublicationsId collectionIdDouble = mock(CollectionOfPublicationsId.class);
        UserId userIdDouble = mock(UserId.class);

        when(collectionService.makeCollectionPublic(collectionIdDouble, userIdDouble))
                .thenReturn(false);

        // ACT
        ResponseEntity<Boolean> response =
                controller.makeCollectionPublic(collectionIdDouble, userIdDouble);

        // ASSERT
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertFalse(response.getBody());
    }

    @Test
    void makeCollectionPublic_shouldReturnBadRequest_onIllegalArgument()
            throws Exception {

        // ARRANGE
        CollectionOfPublicationsId collectionIdDouble = mock(CollectionOfPublicationsId.class);
        UserId userIdDouble = mock(UserId.class);

        when(collectionService.makeCollectionPublic(collectionIdDouble, userIdDouble))
                .thenThrow(new IllegalArgumentException());

        // ACT
        ResponseEntity<Boolean> response =
                controller.makeCollectionPublic(collectionIdDouble, userIdDouble);

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void makeCollectionPublic_shouldReturnForbidden_onAccessDenied()
            throws Exception {

        // ARRANGE
        CollectionOfPublicationsId collectionIdDouble = mock(CollectionOfPublicationsId.class);
        UserId userIdDouble = mock(UserId.class);

        when(collectionService.makeCollectionPublic(collectionIdDouble, userIdDouble))
                .thenThrow(new AccessDeniedException("denied"));

        // ACT
        ResponseEntity<Boolean> response =
                controller.makeCollectionPublic(collectionIdDouble, userIdDouble);

        // ASSERT
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void makeCollectionPublic_shouldReturnInternalServerError_onException()
            throws Exception {

        // ARRANGE
        CollectionOfPublicationsId collectionIdDouble = mock(CollectionOfPublicationsId.class);
        UserId userIdDouble = mock(UserId.class);

        when(collectionService.makeCollectionPublic(collectionIdDouble, userIdDouble))
                .thenThrow(new RuntimeException());

        // ACT
        ResponseEntity<Boolean> response =
                controller.makeCollectionPublic(collectionIdDouble, userIdDouble);

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode().value());
        assertNull(response.getBody());
    }

// -------------------------------------------------------------------------
// makeCollectionPublicUntil
// -------------------------------------------------------------------------

    @Test
    void makeCollectionPublicUntil_shouldReturnOk_onSuccess()
            throws Exception {

        // ARRANGE
        CollectionOfPublicationsId collectionIdDouble = mock(CollectionOfPublicationsId.class);
        UserId userIdDouble = mock(UserId.class);
        LocalDateTime publicUntil = LocalDateTime.now().plusDays(5);

        when(collectionService.makeCollectionPublicUntil(collectionIdDouble, publicUntil, userIdDouble))
                .thenReturn(true);

        // ACT
        ResponseEntity<Boolean> response =
                controller.makeCollectionPublicUntil(collectionIdDouble, publicUntil, userIdDouble);

        // ASSERT
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertTrue(response.getBody());
    }

    @Test
    void makeCollectionPublicUntil_shouldReturnOkWithFalse_whenNotChanged()
            throws Exception {

        // ARRANGE
        CollectionOfPublicationsId collectionIdDouble = mock(CollectionOfPublicationsId.class);
        UserId userIdDouble = mock(UserId.class);
        LocalDateTime publicUntil = LocalDateTime.now().plusDays(5);

        when(collectionService.makeCollectionPublicUntil(collectionIdDouble, publicUntil, userIdDouble))
                .thenReturn(false);

        // ACT
        ResponseEntity<Boolean> response =
                controller.makeCollectionPublicUntil(collectionIdDouble, publicUntil, userIdDouble);

        // ASSERT
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertFalse(response.getBody());
    }

    @Test
    void makeCollectionPublicUntil_shouldReturnBadRequest_onIllegalArgument()
            throws Exception {

        // ARRANGE
        CollectionOfPublicationsId collectionIdDouble = mock(CollectionOfPublicationsId.class);
        UserId userIdDouble = mock(UserId.class);
        LocalDateTime publicUntil = LocalDateTime.now().plusDays(5);

        when(collectionService.makeCollectionPublicUntil(collectionIdDouble, publicUntil, userIdDouble))
                .thenThrow(new IllegalArgumentException());

        // ACT
        ResponseEntity<Boolean> response =
                controller.makeCollectionPublicUntil(collectionIdDouble, publicUntil, userIdDouble);

        // ASSERT
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void makeCollectionPublicUntil_shouldReturnForbidden_onAccessDenied()
            throws Exception {

        // ARRANGE
        CollectionOfPublicationsId collectionIdDouble = mock(CollectionOfPublicationsId.class);
        UserId userIdDouble = mock(UserId.class);
        LocalDateTime publicUntil = LocalDateTime.now().plusDays(5);

        when(collectionService.makeCollectionPublicUntil(collectionIdDouble, publicUntil, userIdDouble))
                .thenThrow(new AccessDeniedException("denied"));

        // ACT
        ResponseEntity<Boolean> response =
                controller.makeCollectionPublicUntil(collectionIdDouble, publicUntil, userIdDouble);

        // ASSERT
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void makeCollectionPublicUntil_shouldReturnInternalServerError_onException()
            throws Exception {

        // ARRANGE
        CollectionOfPublicationsId collectionIdDouble = mock(CollectionOfPublicationsId.class);
        UserId userIdDouble = mock(UserId.class);
        LocalDateTime publicUntil = LocalDateTime.now().plusDays(5);

        when(collectionService.makeCollectionPublicUntil(collectionIdDouble, publicUntil, userIdDouble))
                .thenThrow(new RuntimeException());

        // ACT
        ResponseEntity<Boolean> response =
                controller.makeCollectionPublicUntil(collectionIdDouble, publicUntil, userIdDouble);

        // ASSERT
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void createCollectionOfPublications_shouldAddLinksToResult_onSuccess()
            throws Exception {

        // ARRANGE
        RequestCollectionOfPublicationsDTO requestDouble =
                mock(RequestCollectionOfPublicationsDTO.class);
        UserId userIdDouble = mock(UserId.class);
        GenreId genreIdDouble = mock(GenreId.class);
        CollectionOfPublications collectionDouble = mock(CollectionOfPublications.class);
        ResponseCollectionOfPublicationsDTO responseDTO =
                new ResponseCollectionOfPublicationsDTO("My Collection", "collection-123");

        Link link = Link.of("http://localhost/collection-of-publications").withRel("create-collection-of-publications");

        when(requestDouble.collectionName()).thenReturn("My Collection");
        when(collectionService.createAndSave(any(), any(), any())).thenReturn(collectionDouble);
        when(linkProvider.getLinksForCollectionOfPublications(any())).thenReturn(List.of(link));

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() -> CollectionOfPublicationsMapper.toDTO(any()))
                    .thenReturn(responseDTO);

            // ACT
            ResponseEntity<ResponseCollectionOfPublicationsDTO> response =
                    controller.createCollectionOfPublications(requestDouble, userIdDouble);

            // ASSERT
            assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());
            assertFalse(response.getBody().getLinks().isEmpty());
        }
    }
}