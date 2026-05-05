package TOPSECRET.services;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublicationsFactory;
import TOPSECRET.domain.CollectionOfPublications.ICollectionOfPublicationsRepository;
import TOPSECRET.domain.ValueObjects.GenreId;
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

    private GenreId genreIdDouble;
    private UserId userIdDouble;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        genreIdDouble = mock(GenreId.class);
        userIdDouble = mock(UserId.class);
    }

    @Test
    void constructorShouldThrowExceptionWhenRepositoryIsNull() {

        // Arrange
        ICollectionOfPublicationsRepository repo = null;
        CollectionOfPublicationsFactory factory = mock(CollectionOfPublicationsFactory.class);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new CollectionOfPublicationsService(repo, factory));

        assertEquals("collectionOfPublicationsRepository cannot be null", ex.getMessage());
    }

    @Test
    void constructorShouldThrowExceptionWhenFactoryIsNull() {

        // Arrange
        ICollectionOfPublicationsRepository repo = mock(ICollectionOfPublicationsRepository.class);
        CollectionOfPublicationsFactory factory = null;

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new CollectionOfPublicationsService(repo, factory));

        assertEquals("factory cannot be null", ex.getMessage());
    }

    @Test
    void constructorShouldCreateInstanceWhenArgumentsAreValid() {

        // Arrange
        ICollectionOfPublicationsRepository repo = mock(ICollectionOfPublicationsRepository.class);
        CollectionOfPublicationsFactory factory = mock(CollectionOfPublicationsFactory.class);

        // Act
        CollectionOfPublicationsService service =
                new CollectionOfPublicationsService(repo, factory);

        // Assert
        assertNotNull(service);
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
}