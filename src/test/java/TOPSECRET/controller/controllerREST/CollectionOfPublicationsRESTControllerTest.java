package TOPSECRET.controller.controllerREST;

import TOPSECRET.controller.controllerREST.root.linkProvider.CollectionOfPublicationsLinkProvider;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;

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
                    controller.createCollectionOfPublications(requestDouble, userIdDouble, genreIdDouble);

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
                    controller.createCollectionOfPublications(requestDouble, userIdDouble, genreIdDouble);

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
                    controller.createCollectionOfPublications(requestDouble, userIdDouble, genreIdDouble);

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
                    controller.createCollectionOfPublications(requestDouble, userIdDouble, genreIdDouble);

            // ASSERT
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode().value());
            assertNull(response.getBody());
        }
    }
}