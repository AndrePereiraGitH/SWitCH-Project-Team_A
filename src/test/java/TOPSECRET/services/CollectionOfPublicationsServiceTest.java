package TOPSECRET.services;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublicationsFactory;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CollectionOfPublicationsService}.
 *
 * <p>Covers two areas:</p>
 * <ul>
 *   <li><b>createAndSave — happy path</b>: verifies that a
 *       {@link CollectionOfPublications} is correctly created via
 *       {@link CollectionOfPublicationsFactory} and persisted via
 *       {@link ICollectionOfPublicationsRepository}, returning the saved instance.</li>
 *
 *   <li><b>createAndSave — null rejection</b>: verifies that all mandatory
 *       parameters ({@code collectionName}, {@code collectionGenreId},
 *       {@code userId}) throw {@link NullPointerException}
 *       when {@code null}.</li>
 * </ul>
 */
class CollectionOfPublicationsServiceTest {

    @Mock
    ICollectionOfPublicationsRepository collectionOfPublicationsRepository;

    @Mock
    CollectionOfPublicationsFactory factory;

    @InjectMocks
    CollectionOfPublicationsService service;

    private GenreID genreIdDouble;
    private UserId userIdDouble;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        genreIdDouble = mock(GenreID.class);
        userIdDouble = mock(UserId.class);
    }

    @Test
    void createAndSaveShouldReturnACollectionOfPublications() {

        // Arrange
        String collectionName = "Works by Frank Herbert";

        CollectionOfPublications collectionDouble =
                mock(CollectionOfPublications.class);

        when(factory.create(
                collectionName,
                genreIdDouble,
                userIdDouble
        )).thenReturn(collectionDouble);

        when(collectionOfPublicationsRepository.save(collectionDouble))
                .thenReturn(collectionDouble);

        // Act
        CollectionOfPublications result =
                service.createAndSave(
                        collectionName,
                        genreIdDouble,
                        userIdDouble
                );

        // Assert
        assertEquals(collectionDouble, result);
    }

    @Test
    void createAndSaveShouldThrowWhenCollectionNameIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                service.createAndSave(
                        null,
                        genreIdDouble,
                        userIdDouble
                ));
    }

    @Test
    void createAndSaveShouldThrowWhenCollectionGenreIdIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                service.createAndSave(
                        "Works by Isaac Asimov",
                        null,
                        userIdDouble
                ));
    }

    @Test
    void createAndSaveShouldThrowWhenUserIdIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                service.createAndSave(
                        "Works by Joe Abercrombie",
                        genreIdDouble,
                        null
                ));
    }
}