package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.CollectionOfPublicationsLinkProvider;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.collectionOfPublications.CollectionOfPublicationsMapper;
import TOPSECRET.dto.collectionOfPublications.request.RequestCollectionOfPublicationsDTO;
import TOPSECRET.dto.collectionOfPublications.response.PublicCollectionOfPublicationsDTO;
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

import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
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

    String ownerId = "user-123";

    @Test
    void createCollectionOfPublications_shouldReturnCreated_onSuccess()
            throws Exception {

        // ARRANGE
        RequestCollectionOfPublicationsDTO requestDouble =
                mock(RequestCollectionOfPublicationsDTO.class);

        CollectionOfPublications collectionDouble =
                mock(CollectionOfPublications.class);

        CollectionOfPublicationsId collectionId =
                mock(CollectionOfPublicationsId.class);

        ResponseCollectionOfPublicationsDTO responseDTO =
                new ResponseCollectionOfPublicationsDTO("My Collection", "collection-123", true);

        when(requestDouble.collectionName()).thenReturn("My Collection");
        when(requestDouble.genreId()).thenReturn("genre-123");

        when(collectionService.createAndSave(
                anyString(),
                any(GenreId.class),
                any(UserId.class)
        )).thenReturn(collectionDouble);

        when(linkProvider.getOwnerLinks(any()))
                .thenReturn(new ArrayList<>());

        when(collectionId.toString()).thenReturn("collection-123");
        when(collectionDouble.id()).thenReturn(collectionId);

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() -> CollectionOfPublicationsMapper.toGenreId(any()))
                    .thenReturn(mock(GenreId.class));

            mapperMock.when(() -> CollectionOfPublicationsMapper.toUserId(any()))
                    .thenReturn(mock(UserId.class));

            mapperMock.when(() -> CollectionOfPublicationsMapper.toDTO(any()))
                    .thenReturn(responseDTO);

            // ACT
            ResponseEntity<ResponseCollectionOfPublicationsDTO> response =
                    controller.createCollectionOfPublications(requestDouble, ownerId);

            // ASSERT
            assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());
            assertEquals(responseDTO, response.getBody());
            assertTrue(response.getBody().hasLink("self"));
        }
    }

    @Test
    void createCollectionOfPublications_shouldAddLinksToResult_onSuccess()
            throws Exception {

        // ARRANGE
        RequestCollectionOfPublicationsDTO requestDouble =
                mock(RequestCollectionOfPublicationsDTO.class);

        CollectionOfPublications collectionDouble =
                mock(CollectionOfPublications.class);

        CollectionOfPublicationsId collectionId =
                mock(CollectionOfPublicationsId.class);

        ResponseCollectionOfPublicationsDTO responseDTO =
                new ResponseCollectionOfPublicationsDTO("My Collection", "collection-123", true);

        when(requestDouble.collectionName()).thenReturn("My Collection");
        when(requestDouble.genreId()).thenReturn("genre-123");

        when(collectionService.createAndSave(
                anyString(),
                any(GenreId.class),
                any(UserId.class)
        )).thenReturn(collectionDouble);

        when(linkProvider.getOwnerLinks(any()))
                .thenReturn(new ArrayList<>());

        when(collectionId.toString()).thenReturn("collection-123");
        when(collectionDouble.id()).thenReturn(collectionId);

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() -> CollectionOfPublicationsMapper.toGenreId(any()))
                    .thenReturn(mock(GenreId.class));

            mapperMock.when(() -> CollectionOfPublicationsMapper.toUserId(any()))
                    .thenReturn(mock(UserId.class));

            mapperMock.when(() -> CollectionOfPublicationsMapper.toDTO(any()))
                    .thenReturn(responseDTO);

            // ACT
            ResponseEntity<ResponseCollectionOfPublicationsDTO> response =
                    controller.createCollectionOfPublications(requestDouble, ownerId);

            // ASSERT
            assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());
            assertFalse(response.getBody().getLinks().isEmpty());
        }
    }

    @Test
    void createCollectionOfPublications_shouldReturnBadRequest_onIllegalArgument()
            throws Exception {

        // ARRANGE
        RequestCollectionOfPublicationsDTO requestDouble =
                mock(RequestCollectionOfPublicationsDTO.class);

        when(requestDouble.collectionName()).thenReturn("My Collection");
        when(requestDouble.genreId()).thenReturn("genre-123");

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() -> CollectionOfPublicationsMapper.toGenreId(any()))
                    .thenReturn(mock(GenreId.class));

            mapperMock.when(() -> CollectionOfPublicationsMapper.toUserId(any()))
                    .thenReturn(mock(UserId.class));

            when(collectionService.createAndSave(any(), any(), any()))
                    .thenThrow(new IllegalArgumentException());

            // ACT
            ResponseEntity<ResponseCollectionOfPublicationsDTO> response =
                    controller.createCollectionOfPublications(requestDouble, ownerId);

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

        when(requestDouble.genreId()).thenReturn("genre-123");
        when(requestDouble.collectionName()).thenReturn("My Collection");

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() -> CollectionOfPublicationsMapper.toGenreId(any()))
                    .thenReturn(mock(GenreId.class));

            mapperMock.when(() -> CollectionOfPublicationsMapper.toUserId(any()))
                    .thenReturn(mock(UserId.class));

            when(collectionService.createAndSave(any(), any(), any()))
                    .thenThrow(new AccessDeniedException("denied"));

            // ACT
            ResponseEntity<ResponseCollectionOfPublicationsDTO> response =
                    controller.createCollectionOfPublications(requestDouble, ownerId);

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

        when(requestDouble.collectionName()).thenReturn("My Collection");
        when(requestDouble.genreId()).thenReturn("genre-123");

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() -> CollectionOfPublicationsMapper.toGenreId(any()))
                    .thenReturn(mock(GenreId.class));

            mapperMock.when(() -> CollectionOfPublicationsMapper.toUserId(any()))
                    .thenReturn(mock(UserId.class));

            when(collectionService.createAndSave(any(), any(), any()))
                    .thenThrow(new RuntimeException());

            // ACT
            ResponseEntity<ResponseCollectionOfPublicationsDTO> response =
                    controller.createCollectionOfPublications(requestDouble, ownerId);

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
        String collectionId = "collection-123";
        String userId = "user-123";

        CollectionOfPublicationsId collectionIdVO =
                mock(CollectionOfPublicationsId.class);

        UserId userIdVO =
                mock(UserId.class);

        CollectionOfPublications collectionDouble =
                mock(CollectionOfPublications.class);

        ResponseCollectionOfPublicationsDTO responseDTO =
                new ResponseCollectionOfPublicationsDTO(
                        "My Collection",
                        "collection-123",
                        true
                );

        Link providerLink =
                Link.of("http://localhost/test")
                        .withRel("provider-link");

        when(collectionService.makeCollectionPublic(
                collectionIdVO,
                userIdVO))
                .thenReturn(true);

        when(collectionService.findById(collectionIdVO))
                .thenReturn(collectionDouble);

        when(linkProvider.getOwnerLinks(userIdVO))
                .thenReturn(new ArrayList<>(List.of(providerLink)));

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toCollectionId(any()))
                    .thenReturn(collectionIdVO);

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toUserId(any()))
                    .thenReturn(userIdVO);

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toDTO(any()))
                    .thenReturn(responseDTO);

            // ACT
            ResponseEntity<ResponseCollectionOfPublicationsDTO> response =
                    controller.makeCollectionPublic(
                            collectionId,
                            userId);

            // ASSERT
            assertEquals(HttpStatus.OK.value(),
                    response.getStatusCode().value());

            assertEquals(responseDTO,
                    response.getBody());

            assertTrue(response.getBody().hasLink("self"));
            assertTrue(response.getBody().hasLink("provider-link"));
        }
    }

    @Test
    void makeCollectionPublic_shouldReturnOkWithDTO_whenAlreadyPublic()
            throws Exception {

        // ARRANGE
        String collectionId = "collection-123";
        String userId = "user-123";

        CollectionOfPublicationsId collectionIdVO =
                mock(CollectionOfPublicationsId.class);

        UserId userIdVO =
                mock(UserId.class);

        CollectionOfPublications collectionDouble =
                mock(CollectionOfPublications.class);

        ResponseCollectionOfPublicationsDTO responseDTO =
                new ResponseCollectionOfPublicationsDTO(
                        "My Collection",
                        "collection-123",
                        true
                );

        when(collectionService.makeCollectionPublic(
                collectionIdVO,
                userIdVO))
                .thenReturn(false);

        when(collectionService.findById(collectionIdVO))
                .thenReturn(collectionDouble);

        when(linkProvider.getOwnerLinks(userIdVO))
                .thenReturn(new ArrayList<>());

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toCollectionId(any()))
                    .thenReturn(collectionIdVO);

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toUserId(any()))
                    .thenReturn(userIdVO);

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toDTO(any()))
                    .thenReturn(responseDTO);

            // ACT
            ResponseEntity<ResponseCollectionOfPublicationsDTO> response =
                    controller.makeCollectionPublic(
                            collectionId,
                            userId);

            // ASSERT
            assertEquals(HttpStatus.OK.value(),
                    response.getStatusCode().value());

            assertEquals(responseDTO,
                    response.getBody());
        }
    }

    @Test
    void makeCollectionPublic_shouldReturnBadRequest_onIllegalArgument()
            throws Exception {

        // ARRANGE
        String collectionId = "collection-123";
        String userId = "user-123";

        CollectionOfPublicationsId collectionIdVO =
                mock(CollectionOfPublicationsId.class);

        UserId userIdVO =
                mock(UserId.class);

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toCollectionId(any()))
                    .thenReturn(collectionIdVO);

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toUserId(any()))
                    .thenReturn(userIdVO);

            when(collectionService.makeCollectionPublic(
                    collectionIdVO,
                    userIdVO))
                    .thenThrow(new IllegalArgumentException());

            // ACT
            ResponseEntity<ResponseCollectionOfPublicationsDTO> response =
                    controller.makeCollectionPublic(
                            collectionId,
                            userId);

            // ASSERT
            assertEquals(HttpStatus.BAD_REQUEST.value(),
                    response.getStatusCode().value());

            assertNull(response.getBody());
        }
    }

    @Test
    void makeCollectionPublic_shouldReturnForbidden_onAccessDenied()
            throws Exception {

        // ARRANGE
        String collectionId = "collection-123";
        String userId = "user-123";

        CollectionOfPublicationsId collectionIdVO =
                mock(CollectionOfPublicationsId.class);

        UserId userIdVO =
                mock(UserId.class);

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toCollectionId(collectionId))
                    .thenReturn(collectionIdVO);

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toUserId(userId))
                    .thenReturn(userIdVO);

            when(collectionService.makeCollectionPublic(
                    collectionIdVO,
                    userIdVO))
                    .thenThrow(new AccessDeniedException("denied"));

            // ACT
            ResponseEntity<ResponseCollectionOfPublicationsDTO> response =
                    controller.makeCollectionPublic(collectionId, userId);

            // ASSERT
            assertEquals(
                    HttpStatus.FORBIDDEN.value(),
                    response.getStatusCode().value()
            );

            assertNull(response.getBody());
        }
    }

    @Test
    void makeCollectionPublic_shouldReturnInternalServerError_onException()
            throws Exception {

        // ARRANGE
        String collectionId = "collection-123";
        String userId = "user-123";

        CollectionOfPublicationsId collectionIdVO =
                mock(CollectionOfPublicationsId.class);

        UserId userIdVO =
                mock(UserId.class);

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toCollectionId(collectionId))
                    .thenReturn(collectionIdVO);

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toUserId(userId))
                    .thenReturn(userIdVO);

            when(collectionService.makeCollectionPublic(
                    collectionIdVO,
                    userIdVO))
                    .thenThrow(new RuntimeException());

            // ACT
            ResponseEntity<ResponseCollectionOfPublicationsDTO> response =
                    controller.makeCollectionPublic(collectionId, userId);

            // ASSERT
            assertEquals(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    response.getStatusCode().value()
            );

            assertNull(response.getBody());
        }
    }

    // -------------------------------------------------------------------------
    // makeCollectionPrivate
    // -------------------------------------------------------------------------

    @Test
    void makeCollectionPrivate_shouldReturnOk_onSuccess()
            throws Exception {

        // ARRANGE
        String collectionId = "collection-123";
        String userId = "user-123";

        CollectionOfPublicationsId collectionIdVO =
                mock(CollectionOfPublicationsId.class);

        UserId userIdVO =
                mock(UserId.class);

        CollectionOfPublications collectionDouble =
                mock(CollectionOfPublications.class);

        ResponseCollectionOfPublicationsDTO responseDTO =
                new ResponseCollectionOfPublicationsDTO(
                        "My Collection",
                        "collection-123",
                        false
                );

        Link providerLink =
                Link.of("http://localhost/test")
                        .withRel("provider-link");

        when(collectionService.makeCollectionPrivate(
                collectionIdVO,
                userIdVO))
                .thenReturn(true);

        when(collectionService.findById(collectionIdVO))
                .thenReturn(collectionDouble);

        when(linkProvider.getOwnerLinks(userIdVO))
                .thenReturn(new ArrayList<>(List.of(providerLink)));

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toCollectionId(any()))
                    .thenReturn(collectionIdVO);

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toUserId(any()))
                    .thenReturn(userIdVO);

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toDTO(any()))
                    .thenReturn(responseDTO);

            // ACT
            ResponseEntity<ResponseCollectionOfPublicationsDTO> response =
                    controller.makeCollectionPrivate(
                            collectionId,
                            userId);

            // ASSERT
            assertEquals(HttpStatus.OK.value(),
                    response.getStatusCode().value());

            assertEquals(responseDTO,
                    response.getBody());

            assertTrue(response.getBody().hasLink("self"));
            assertTrue(response.getBody().hasLink("provider-link"));
        }
    }

    @Test
    void makeCollectionPrivate_shouldReturnBadRequest_onIllegalArgument()
            throws Exception {

        // ARRANGE
        String collectionId = "collection-123";
        String userId = "user-123";

        CollectionOfPublicationsId collectionIdVO =
                mock(CollectionOfPublicationsId.class);

        UserId userIdVO =
                mock(UserId.class);

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toCollectionId(any()))
                    .thenReturn(collectionIdVO);

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toUserId(any()))
                    .thenReturn(userIdVO);

            when(collectionService.makeCollectionPrivate(
                    collectionIdVO,
                    userIdVO))
                    .thenThrow(new IllegalArgumentException());

            // ACT
            ResponseEntity<ResponseCollectionOfPublicationsDTO> response =
                    controller.makeCollectionPrivate(
                            collectionId,
                            userId);

            // ASSERT
            assertEquals(HttpStatus.BAD_REQUEST.value(),
                    response.getStatusCode().value());

            assertNull(response.getBody());
        }
    }

    @Test
    void makeCollectionPrivate_shouldReturnForbidden_onAccessDenied()
            throws Exception {

        // ARRANGE
        String collectionId = "collection-123";
        String userId = "user-123";

        CollectionOfPublicationsId collectionIdVO =
                mock(CollectionOfPublicationsId.class);

        UserId userIdVO =
                mock(UserId.class);

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toCollectionId(collectionId))
                    .thenReturn(collectionIdVO);

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toUserId(userId))
                    .thenReturn(userIdVO);

            when(collectionService.makeCollectionPrivate(
                    collectionIdVO,
                    userIdVO))
                    .thenThrow(new AccessDeniedException("denied"));

            // ACT
            ResponseEntity<ResponseCollectionOfPublicationsDTO> response =
                    controller.makeCollectionPrivate(collectionId, userId);

            // ASSERT
            assertEquals(
                    HttpStatus.FORBIDDEN.value(),
                    response.getStatusCode().value()
            );

            assertNull(response.getBody());
        }
    }

    @Test
    void makeCollectionPrivate_shouldReturnInternalServerError_onException()
            throws Exception {

        // ARRANGE
        String collectionId = "collection-123";
        String userId = "user-123";

        CollectionOfPublicationsId collectionIdVO =
                mock(CollectionOfPublicationsId.class);

        UserId userIdVO =
                mock(UserId.class);

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toCollectionId(collectionId))
                    .thenReturn(collectionIdVO);

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toUserId(userId))
                    .thenReturn(userIdVO);

            when(collectionService.makeCollectionPrivate(
                    collectionIdVO,
                    userIdVO))
                    .thenThrow(new RuntimeException());

            // ACT
            ResponseEntity<ResponseCollectionOfPublicationsDTO> response =
                    controller.makeCollectionPrivate(collectionId, userId);

            // ASSERT
            assertEquals(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    response.getStatusCode().value()
            );

            assertNull(response.getBody());
        }
    }

    // -------------------------------------------------------------------------
    // makeCollectionPublicUntil
    // -------------------------------------------------------------------------

    @Test
    void makeCollectionPublicUntil_shouldReturnOk_onSuccess()
            throws Exception {

        // ARRANGE
        String collectionId = "collection-123";
        String userId = "user-123";

        CollectionOfPublicationsId collectionIdVO =
                mock(CollectionOfPublicationsId.class);

        UserId userIdVO =
                mock(UserId.class);

        LocalDate publicUntil = LocalDate.now().plusDays(5);

        CollectionOfPublications collectionDouble =
                mock(CollectionOfPublications.class);

        ResponseCollectionOfPublicationsDTO responseDTO =
                new ResponseCollectionOfPublicationsDTO(
                        "My Collection",
                        "collection-123",
                        true
                );

        Link providerLink =
                Link.of("http://localhost/test")
                        .withRel("provider-link");

        when(collectionService.makeCollectionPublicUntil(
                collectionIdVO,
                publicUntil,
                userIdVO))
                .thenReturn(true);

        when(collectionService.findById(collectionIdVO))
                .thenReturn(collectionDouble);

        when(linkProvider.getOwnerLinks(userIdVO))
                .thenReturn(new ArrayList<>(List.of(providerLink)));

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toCollectionId(collectionId))
                    .thenReturn(collectionIdVO);

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toUserId(userId))
                    .thenReturn(userIdVO);

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toDTO(any()))
                    .thenReturn(responseDTO);

            // ACT
            ResponseEntity<ResponseCollectionOfPublicationsDTO> response =
                    controller.makeCollectionPublicUntil(
                            collectionId,
                            publicUntil,
                            userId);

            // ASSERT
            assertEquals(HttpStatus.OK.value(),
                    response.getStatusCode().value());

            assertEquals(responseDTO,
                    response.getBody());

            assertTrue(response.getBody().hasLink("self"));
            assertTrue(response.getBody().hasLink("provider-link"));
        }
    }

    @Test
    void makeCollectionPublicUntil_shouldReturnOkWithDTO_whenNotChanged()
            throws Exception {

        // ARRANGE
        String collectionId = "collection-123";
        String userId = "user-123";

        CollectionOfPublicationsId collectionIdVO =
                mock(CollectionOfPublicationsId.class);

        UserId userIdVO =
                mock(UserId.class);

        LocalDate publicUntil = LocalDate.now().plusDays(5);

        CollectionOfPublications collectionDouble =
                mock(CollectionOfPublications.class);

        ResponseCollectionOfPublicationsDTO responseDTO =
                new ResponseCollectionOfPublicationsDTO(
                        "My Collection",
                        "collection-123",
                        true
                );

        when(collectionService.makeCollectionPublicUntil(
                collectionIdVO,
                publicUntil,
                userIdVO))
                .thenReturn(false);

        when(collectionService.findById(collectionIdVO))
                .thenReturn(collectionDouble);

        when(linkProvider.getOwnerLinks(userIdVO))
                .thenReturn(new ArrayList<>());

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toCollectionId(collectionId))
                    .thenReturn(collectionIdVO);

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toUserId(userId))
                    .thenReturn(userIdVO);

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toDTO(any()))
                    .thenReturn(responseDTO);

            // ACT
            ResponseEntity<ResponseCollectionOfPublicationsDTO> response =
                    controller.makeCollectionPublicUntil(
                            collectionId,
                            publicUntil,
                            userId);

            // ASSERT
            assertEquals(HttpStatus.OK.value(),
                    response.getStatusCode().value());

            assertEquals(responseDTO,
                    response.getBody());
        }
    }

    @Test
    void makeCollectionPublicUntil_shouldReturnBadRequest_onIllegalArgument()
            throws Exception {

        String collectionId = "collection-123";
        String userId = "user-123";

        CollectionOfPublicationsId collectionIdVO =
                mock(CollectionOfPublicationsId.class);

        UserId userIdVO =
                mock(UserId.class);

        LocalDate publicUntil = LocalDate.now().plusDays(5);

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toCollectionId(collectionId))
                    .thenReturn(collectionIdVO);

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toUserId(userId))
                    .thenReturn(userIdVO);

            when(collectionService.makeCollectionPublicUntil(
                    collectionIdVO,
                    publicUntil,
                    userIdVO))
                    .thenThrow(new IllegalArgumentException());

            ResponseEntity<ResponseCollectionOfPublicationsDTO> response =
                    controller.makeCollectionPublicUntil(
                            collectionId,
                            publicUntil,
                            userId);

            assertEquals(
                    HttpStatus.BAD_REQUEST.value(),
                    response.getStatusCode().value()
            );

            assertNull(response.getBody());
        }
    }

    @Test
    void makeCollectionPublicUntil_shouldReturnForbidden_onAccessDenied()
            throws Exception {

        String collectionId = "collection-123";
        String userId = "user-123";

        CollectionOfPublicationsId collectionIdVO =
                mock(CollectionOfPublicationsId.class);

        UserId userIdVO =
                mock(UserId.class);

        LocalDate publicUntil = LocalDate.now().plusDays(5);

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toCollectionId(collectionId))
                    .thenReturn(collectionIdVO);

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toUserId(userId))
                    .thenReturn(userIdVO);

            when(collectionService.makeCollectionPublicUntil(
                    collectionIdVO,
                    publicUntil,
                    userIdVO))
                    .thenThrow(new AccessDeniedException("denied"));

            ResponseEntity<ResponseCollectionOfPublicationsDTO> response =
                    controller.makeCollectionPublicUntil(
                            collectionId,
                            publicUntil,
                            userId);

            assertEquals(
                    HttpStatus.FORBIDDEN.value(),
                    response.getStatusCode().value()
            );

            assertNull(response.getBody());
        }
    }

    @Test
    void makeCollectionPublicUntil_shouldReturnInternalServerError_onException()
            throws Exception {

        String collectionId = "collection-123";
        String userId = "user-123";

        CollectionOfPublicationsId collectionIdVO =
                mock(CollectionOfPublicationsId.class);

        UserId userIdVO =
                mock(UserId.class);

        LocalDate publicUntil = LocalDate.now().plusDays(5);

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toCollectionId(collectionId))
                    .thenReturn(collectionIdVO);

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toUserId(userId))
                    .thenReturn(userIdVO);

            when(collectionService.makeCollectionPublicUntil(
                    collectionIdVO,
                    publicUntil,
                    userIdVO))
                    .thenThrow(new RuntimeException());

            ResponseEntity<ResponseCollectionOfPublicationsDTO> response =
                    controller.makeCollectionPublicUntil(
                            collectionId,
                            publicUntil,
                            userId);

            assertEquals(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    response.getStatusCode().value()
            );

            assertNull(response.getBody());
        }
    }

// -------------------------------------------------------------------------
// findPublicCollectionsByGenreId
// -------------------------------------------------------------------------

    @Test
    void findPublicCollectionsByGenreId_shouldReturnOk_onSuccess() {

        // ARRANGE
        String genreId = "genre-123";

        GenreId genreIdDouble = mock(GenreId.class);
        CollectionOfPublications collectionDouble = mock(CollectionOfPublications.class);
        CollectionOfPublicationsId collectionIdDouble = mock(CollectionOfPublicationsId.class);
        UserId ownerIdDouble = mock(UserId.class);

        PublicCollectionOfPublicationsDTO dto =
                new PublicCollectionOfPublicationsDTO(
                        "collection-123",
                        "My Collection",
                        "ownerName",
                        null
                );

        when(collectionDouble.id()).thenReturn(collectionIdDouble);
        when(collectionIdDouble.toString()).thenReturn("collection-123");
        when(collectionDouble.getOwnerId()).thenReturn(ownerIdDouble);
        when(collectionService.findPublicCollectionsByGenreId(genreIdDouble)).thenReturn(List.of(collectionDouble));
        when(collectionService.getUserName(ownerIdDouble)).thenReturn("ownerName");
        when(linkProvider.getPublicLinks()).thenReturn(new ArrayList<>());

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toGenreId(any()))
                    .thenReturn(genreIdDouble);

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toPublicDTO(
                                    eq(collectionDouble),
                                    eq("ownerName")))
                    .thenReturn(dto);

            // ACT
            ResponseEntity<List<PublicCollectionOfPublicationsDTO>> response =
                    controller.findPublicCollectionsByGenreId(genreId);

            // ASSERT
            assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
            assertEquals(1, response.getBody().size());
        }
    }

    @Test
    void findPublicCollectionsByGenreId_shouldReturnBadRequest_onIllegalArgumentException() {

        // ARRANGE
        String genreId = "genre-123";

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toGenreId(any()))
                    .thenThrow(new IllegalArgumentException());

            // ACT
            ResponseEntity<List<PublicCollectionOfPublicationsDTO>> response =
                    controller.findPublicCollectionsByGenreId(genreId);

            // ASSERT
            assertEquals(HttpStatus.BAD_REQUEST.value(),
                    response.getStatusCode().value());

            assertNull(response.getBody());
        }
    }

    @Test
    void findPublicCollectionsByGenreId_shouldReturnInternalServerError_onException() {

        // ARRANGE
        String genreId = "genre-123";

        GenreId genreIdDouble = mock(GenreId.class);

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toGenreId(any()))
                    .thenReturn(genreIdDouble);

            when(collectionService.findPublicCollectionsByGenreId(genreIdDouble))
                    .thenThrow(new RuntimeException());

            // ACT
            ResponseEntity<List<PublicCollectionOfPublicationsDTO>> response =
                    controller.findPublicCollectionsByGenreId(genreId);

            // ASSERT
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    response.getStatusCode().value());

            assertNull(response.getBody());
        }
    }

// -------------------------------------------------------------------------
// findCollectionsByOwnerId
// -------------------------------------------------------------------------

    @Test
    void findCollectionsByOwnerId_shouldReturnOk_onSuccess() throws InstanceNotFoundException {

        // ARRANGE
        String userId = "user-123";

        UserId userIdDouble = mock(UserId.class);
        CollectionOfPublications collectionDouble = mock(CollectionOfPublications.class);
        CollectionOfPublicationsId collectionIdDouble = mock(CollectionOfPublicationsId.class);

        ResponseCollectionOfPublicationsDTO dto =
                new ResponseCollectionOfPublicationsDTO(
                        "My Collection",
                        "collection-123",
                        true
                );

        when(collectionDouble.id()).thenReturn(collectionIdDouble);
        when(collectionIdDouble.toString()).thenReturn("collection-123");
        when(collectionService.findAllByOwnerId(userIdDouble)).thenReturn(List.of(collectionDouble));
        when(linkProvider.getOwnerLinks(userIdDouble)).thenReturn(new ArrayList<>());

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toUserId(any()))
                    .thenReturn(userIdDouble);

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toDTO(collectionDouble))
                    .thenReturn(dto);

            // ACT
            ResponseEntity<List<ResponseCollectionOfPublicationsDTO>> response =
                    controller.findCollectionsByOwnerId(userId);

            // ASSERT
            assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
            assertEquals(1, response.getBody().size());
        }
    }

    @Test
    void findCollectionsByOwnerId_shouldReturnBadRequest_onIllegalArgument() {

        // ARRANGE
        String userId = "invalid-user";

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toUserId(any()))
                    .thenThrow(new IllegalArgumentException());

            // ACT
            ResponseEntity<List<ResponseCollectionOfPublicationsDTO>> response =
                    controller.findCollectionsByOwnerId(userId);

            // ASSERT
            assertEquals(
                    HttpStatus.BAD_REQUEST.value(),
                    response.getStatusCode().value()
            );

            assertNull(response.getBody());
        }
    }

    @Test
    void findCollectionsByOwnerId_shouldReturnInternalServerError_onException() {

        // ARRANGE
        String userId = "user-123";

        UserId userIdDouble =
                mock(UserId.class);

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toUserId(any()))
                    .thenReturn(userIdDouble);

            when(collectionService.findAllByOwnerId(userIdDouble))
                    .thenThrow(new RuntimeException());

            // ACT
            ResponseEntity<List<ResponseCollectionOfPublicationsDTO>> response =
                    controller.findCollectionsByOwnerId(userId);

            // ASSERT
            assertEquals(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    response.getStatusCode().value()
            );

            assertNull(response.getBody());
        }
    }

// -------------------------------------------------------------------------
// getCollectionById
// -------------------------------------------------------------------------

    @Test
    void getCollectionById_shouldReturnOk_onSuccess() throws Exception {

        // ARRANGE
        String collectionId = "collection-123";
        String userId = "user-123";

        CollectionOfPublicationsId collectionIdVO =
                mock(CollectionOfPublicationsId.class);

        CollectionOfPublications collectionDouble =
                mock(CollectionOfPublications.class);

        ResponseCollectionOfPublicationsDTO responseDTO =
                new ResponseCollectionOfPublicationsDTO(
                        "My Collection",
                        "collection-123",
                        true
                );

        when(collectionService.findById(collectionIdVO))
                .thenReturn(collectionDouble);

        when(linkProvider.getPublicLinks())
                .thenReturn(new ArrayList<>());

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toCollectionId(any()))
                    .thenReturn(collectionIdVO);

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toDTO(any()))
                    .thenReturn(responseDTO);

            // ACT
            ResponseEntity<ResponseCollectionOfPublicationsDTO> response =
                    controller.getCollectionById(collectionId);

            // ASSERT
            assertEquals(HttpStatus.OK.value(),
                    response.getStatusCode().value());

            assertEquals(responseDTO, response.getBody());

            assertTrue(response.getBody().hasLink("self"));
        }
    }

    @Test
    void getCollectionById_shouldReturnNotFound_whenCollectionDoesNotExist()
            throws Exception {

        // ARRANGE
        String collectionId = "collection-123";
        String userId = "user-123";

        CollectionOfPublicationsId collectionIdVO =
                mock(CollectionOfPublicationsId.class);

        when(collectionService.findById(collectionIdVO))
                .thenThrow(new InstanceNotFoundException("not found"));

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toCollectionId(any()))
                    .thenReturn(collectionIdVO);

            // ACT
            ResponseEntity<ResponseCollectionOfPublicationsDTO> response =
                    controller.getCollectionById(collectionId);

            // ASSERT
            assertEquals(HttpStatus.NOT_FOUND.value(),
                    response.getStatusCode().value());

            assertNull(response.getBody());
        }
    }

    @Test
    void getCollectionById_shouldReturnInternalServerError_onException()
            throws Exception {

        // ARRANGE
        String collectionId = "collection-123";
        String userId = "user-123";

        CollectionOfPublicationsId collectionIdVO =
                mock(CollectionOfPublicationsId.class);


        when(collectionService.findById(collectionIdVO))
                .thenThrow(new RuntimeException());

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toCollectionId(any()))
                    .thenReturn(collectionIdVO);

            // ACT
            ResponseEntity<ResponseCollectionOfPublicationsDTO> response =
                    controller.getCollectionById(collectionId);

            // ASSERT
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    response.getStatusCode().value());

            assertNull(response.getBody());
        }
    }

// -------------------------------------------------------------------------
// findAllPublicCollections
// -------------------------------------------------------------------------

    @Test
    void findAllPublicCollections_shouldReturnOk_onSuccess() throws Exception {

        // ARRANGE
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        CollectionOfPublicationsId collectionId = mock(CollectionOfPublicationsId.class);

        when(collection.id()).thenReturn(collectionId);
        when(collectionId.toString()).thenReturn("col-1");
        when(collection.getOwnerId()).thenReturn(mock(UserId.class));
        when(collectionService.getUserName(any())).thenReturn("owner-1");
        when(collectionService.findAllPublicCollections()).thenReturn(List.of(collection));
        when(linkProvider.getPublicLinks()).thenReturn(new ArrayList<>());

        // ACT
        ResponseEntity<List<PublicCollectionOfPublicationsDTO>> response =
                controller.findAllPublicCollections();

        // ASSERT
        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void findAllPublicCollections_shouldReturnOk_withEmptyList() {

        when(collectionService.findAllPublicCollections())
                .thenReturn(List.of());

        ResponseEntity<List<PublicCollectionOfPublicationsDTO>> response =
                controller.findAllPublicCollections();

        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void findAllPublicCollections_shouldReturnInternalServerError_onException() {

        when(collectionService.findAllPublicCollections())
                .thenThrow(new RuntimeException());

        ResponseEntity<List<PublicCollectionOfPublicationsDTO>> response =
                controller.findAllPublicCollections();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                response.getStatusCode().value());

        assertNull(response.getBody());
    }

// -------------------------------------------------------------------------
// findAllCollections
// -------------------------------------------------------------------------

    @Test
    void findAllCollections_shouldReturnOk_whenUserIsAdmin() {

        // ARRANGE
        String userId = "user-123";

        UserId userIdVO = mock(UserId.class);
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        CollectionOfPublicationsId collectionIdDouble = mock(CollectionOfPublicationsId.class);

        ResponseCollectionOfPublicationsDTO dto =
                new ResponseCollectionOfPublicationsDTO(
                        "My Collection",
                        "collection-123",
                        true
                );

        when(collection.id()).thenReturn(collectionIdDouble);
        when(collectionIdDouble.toString()).thenReturn("collection-123");
        when(collectionService.findAllCollections(userIdVO)).thenReturn(List.of(collection));
        when(linkProvider.getPublicLinks()).thenReturn(new ArrayList<>());

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toUserId(any()))
                    .thenReturn(userIdVO);

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toDTO(collection))
                    .thenReturn(dto);

            // ACT
            ResponseEntity<List<ResponseCollectionOfPublicationsDTO>> response =
                    controller.findAllCollections(userId);

            // ASSERT
            assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
            assertEquals(1, response.getBody().size());
        }
    }

    @Test
    void findAllCollections_shouldReturnForbidden_whenUserIsNotAdmin() {

        // ARRANGE
        String userId = "user-123";
        UserId userIdVO = mock(UserId.class);

        when(collectionService.findAllCollections(userIdVO))
                .thenThrow(new AccessDeniedException("denied"));

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toUserId(any()))
                    .thenReturn(userIdVO);

            // ACT
            ResponseEntity<List<ResponseCollectionOfPublicationsDTO>> response =
                    controller.findAllCollections(userId);

            // ASSERT
            assertEquals(HttpStatus.FORBIDDEN.value(),
                    response.getStatusCode().value());

            assertNull(response.getBody());
        }
    }

    @Test
    void findAllCollections_shouldReturnBadRequest_whenIllegalArgument() {

        // ARRANGE
        String userId = "invalid";

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toUserId(userId))
                    .thenThrow(new IllegalArgumentException());

            // ACT
            ResponseEntity<List<ResponseCollectionOfPublicationsDTO>> response =
                    controller.findAllCollections(userId);

            // ASSERT
            assertEquals(HttpStatus.BAD_REQUEST.value(),
                    response.getStatusCode().value());

            assertNull(response.getBody());
        }
    }

    @Test
    void findAllCollections_shouldReturnInternalServerError_whenExceptionOccurs() {

        // ARRANGE
        String userId = "user-123";
        UserId userIdVO = mock(UserId.class);

        when(collectionService.findAllCollections(userIdVO))
                .thenThrow(new RuntimeException());

        try (MockedStatic<CollectionOfPublicationsMapper> mapperMock =
                     mockStatic(CollectionOfPublicationsMapper.class)) {

            mapperMock.when(() ->
                            CollectionOfPublicationsMapper.toUserId(any()))
                    .thenReturn(userIdVO);

            // ACT
            ResponseEntity<List<ResponseCollectionOfPublicationsDTO>> response =
                    controller.findAllCollections(userId);

            // ASSERT
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    response.getStatusCode().value());

            assertNull(response.getBody());
        }
    }
}