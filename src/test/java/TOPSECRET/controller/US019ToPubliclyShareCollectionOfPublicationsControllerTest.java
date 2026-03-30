package TOPSECRET.controller;

import TOPSECRET.domain.CollectionOfPublications;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.User;
import TOPSECRET.dto.CollectionOfPublicationsDTO_US019;
import TOPSECRET.repository.ICollectionOfPublicationsRepository;
import org.junit.jupiter.api.Test;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Unit tests for {@link US019ToPubliclyShareCollectionOfPublicationsController}.
 *
 * These tests verify the controller's behaviour in isolation using mock doubles
 * for {@link ICollectionOfPublicationsRepository}, ensuring the controller
 * correctly delegates to its dependencies without testing their internal logic.
 */
class US019ToPubliclyShareCollectionOfPublicationsControllerTest {

    ICollectionOfPublicationsRepository repo = mock(ICollectionOfPublicationsRepository.class);


    @Test
    void testConstructor() {
        // Arrange
        ICollectionOfPublicationsRepository repoDouble =
                mock(ICollectionOfPublicationsRepository.class);

        // Act
        US019ToPubliclyShareCollectionOfPublicationsController controller =
                new US019ToPubliclyShareCollectionOfPublicationsController(repoDouble);

        // Assert
        assertNotNull(controller);
    }

    @Test
    void requestAllCollectionOfPublications_shouldReturnEmptyList_ifUserHasNoCollections() {
        // Arrange
        ICollectionOfPublicationsRepository repoDouble =
                mock(ICollectionOfPublicationsRepository.class);
        User ownerDouble = mock(User.class);

        when(repoDouble.getAllCollectionOfPublicationsByUser(ownerDouble))
                .thenReturn(new ArrayList<>());

        US019ToPubliclyShareCollectionOfPublicationsController controller =
                new US019ToPubliclyShareCollectionOfPublicationsController(repoDouble);

        // Act
        List<CollectionOfPublicationsDTO_US019> result =
                controller.requestAllCollectionsOfPublicationsByUser(ownerDouble);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }


    /**
     * Verifies that requesting collections for a valid user with existing collections
     * returns a list with the correct number of DTOs.
     * The repository mock is configured to return a list with one collection double,
     * which is then converted to a DTO by the controller.
     */
    @Test
    void requestAllCollectionOfPublications_shouldReturnList_forUserWithCollections() {
        // Arrange
        ICollectionOfPublicationsRepository repoDouble =
                mock(ICollectionOfPublicationsRepository.class);
        User ownerDouble = mock(User.class);

        CollectionOfPublications collectionDouble = mock(CollectionOfPublications.class);
        Genre genreDouble = mock(Genre.class);

        when(collectionDouble.getCollectionName()).thenReturn("My funny Collection");
        when(collectionDouble.getCollectionGenre()).thenReturn(genreDouble);
        when(genreDouble.getGenreType()).thenReturn("Fiction");
        when(collectionDouble.getOwner()).thenReturn(ownerDouble);
        when(ownerDouble.getUserName()).thenReturn("alice"); // <-- isto é o único acrescento
        when(repoDouble.getAllCollectionOfPublicationsByUser(ownerDouble))
                .thenReturn(List.of(collectionDouble));

        US019ToPubliclyShareCollectionOfPublicationsController controller =
                new US019ToPubliclyShareCollectionOfPublicationsController(repoDouble);

        // Act
        List<CollectionOfPublicationsDTO_US019> result =
                controller.requestAllCollectionsOfPublicationsByUser(ownerDouble);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }


    @Test
    void requestAllCollectionOfPublications_shouldThrow_ifUserIsNull() {
        // Arrange
        ICollectionOfPublicationsRepository repoDouble =
                mock(ICollectionOfPublicationsRepository.class);
        US019ToPubliclyShareCollectionOfPublicationsController controller =
                new US019ToPubliclyShareCollectionOfPublicationsController(repoDouble);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> controller.requestAllCollectionsOfPublicationsByUser(null)
        );
        assertEquals("User cannot be null", exception.getMessage());
    }



    /**
     * Verifies that sharing a collection using a valid DTO returns {@code true}.
     * The repository mock is configured to return a collection double for the given DTO,
     * simulating a successful lookup.
     */
    @Test
    void shareCollectionOfPublications_shouldReturnTrue_ifCollectionExists() throws Exception {
        // Arrange
        ICollectionOfPublicationsRepository repoDouble = mock(ICollectionOfPublicationsRepository.class);

        CollectionOfPublicationsDTO_US019 dtoDouble = mock(CollectionOfPublicationsDTO_US019.class);

        CollectionOfPublications collectionDouble = mock(CollectionOfPublications.class);

        when(repoDouble.getCollectionByDTO(dtoDouble)).thenReturn(collectionDouble);

        US019ToPubliclyShareCollectionOfPublicationsController controller =
                new US019ToPubliclyShareCollectionOfPublicationsController(repoDouble);

        // Act
        boolean result = controller.shareCollectionOfPublications(dtoDouble);

        // Assert
        assertTrue(result);
    }


    @Test
    void shareCollectionOfPublications_shouldThrow_ifCollectionIsNull() {
        // Arrange
        ICollectionOfPublicationsRepository repoDouble =
                mock(ICollectionOfPublicationsRepository.class);
        US019ToPubliclyShareCollectionOfPublicationsController controller =
                new US019ToPubliclyShareCollectionOfPublicationsController(repoDouble);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> controller.shareCollectionOfPublications(null)
        );
        assertEquals("you must send a collection to share", exception.getMessage());
    }



    /**
     * Verifies that sharing a DTO that does not match any existing collection
     * throws {@link InstanceNotFoundException}.
     * The repository mock is configured to throw the exception when queried
     * with the given DTO double.
     */
    @Test
    void shareCollectionOfPublications_shouldThrow_ifCollectionNotFound()
            throws Exception {
        // Arrange
        ICollectionOfPublicationsRepository repoDouble =
                mock(ICollectionOfPublicationsRepository.class);
        CollectionOfPublicationsDTO_US019 dtoDouble =
                mock(CollectionOfPublicationsDTO_US019.class);

        when(repoDouble.getCollectionByDTO(dtoDouble))
                .thenThrow(new InstanceNotFoundException());

        US019ToPubliclyShareCollectionOfPublicationsController controller =
                new US019ToPubliclyShareCollectionOfPublicationsController(repoDouble);

        // Act & Assert
        assertThrows(InstanceNotFoundException.class,
                () -> controller.shareCollectionOfPublications(dtoDouble));
    }

}