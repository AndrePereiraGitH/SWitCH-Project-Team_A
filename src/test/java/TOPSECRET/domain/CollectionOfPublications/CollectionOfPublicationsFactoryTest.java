package TOPSECRET.domain.CollectionOfPublications;

import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;

/**
 * Unit tests for {@link CollectionOfPublicationsFactory}.
 *
 * <p>Verifies that the factory correctly creates
 * {@link CollectionOfPublications} domain objects using both creation
 * paths:
 * <ul>
 *     <li>Creation of a new collection (new aggregate)</li>
 *     <li>Recreation of an existing collection (persistence rebuild)</li>
 * </ul>
 *
 * <p>The factory delegates validation to the
 * {@link CollectionOfPublications} constructor, therefore tests focus on
 * successful creation and constructor validation propagation.</p>
 */
class CollectionOfPublicationsFactoryTest {

    private CollectionOfPublicationsFactory factory;

    private GenreId genreIdDouble;
    private UserId userIdDouble;
    private CollectionOfPublicationsId collectionIdDouble;

    @BeforeEach
    void setUp() {

        genreIdDouble = mock(GenreId.class);
        userIdDouble = mock(UserId.class);
        collectionIdDouble = mock(CollectionOfPublicationsId.class);
    }

    // ===========================================================
    // ========== Tests for create (new object) ==================
    // ===========================================================

    @Test
    void createShouldReturnValidCollection() {

        // Arrange
        String name = "Fantasy Collection";
        factory = new CollectionOfPublicationsFactory();

        // Act
        CollectionOfPublications result =
                factory.create(name, genreIdDouble, userIdDouble);

        // Assert
        assertNotNull(result);
        assertEquals(name, result.getCollectionName());
        assertEquals(genreIdDouble, result.getCollectionGenre());
        assertEquals(userIdDouble, result.getOwnerId());
        assertTrue(result.getIsPrivate());
    }

    @Test
    void createShouldThrowExceptionWhenNameIsNull() {
        //Arrange
        factory = new CollectionOfPublicationsFactory();

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(null, genreIdDouble, userIdDouble));
    }

    @Test
    void createShouldThrowExceptionWhenNameIsBlank() {
        //Arrange
        factory = new CollectionOfPublicationsFactory();

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create("   ", genreIdDouble, userIdDouble));
    }

    @Test
    void createShouldThrowExceptionWhenGenreIdIsNull() {
        //Arrange
        factory = new CollectionOfPublicationsFactory();

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create("Fantasy Collection", null, userIdDouble));
    }

    @Test
    void createShouldThrowExceptionWhenUserIdIsNull() {
        //Arrange
        factory = new CollectionOfPublicationsFactory();

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create("Fantasy Collection", genreIdDouble, null));
    }

// ===========================================================
// ========== Tests for create (recreate object) =============
// ===========================================================

    @Test
    void recreateShouldReturnValidCollection() {

        // Arrange
        String name = "Fantasy Collection";
        boolean isPrivate = true;
        LocalDateTime publicUntil = LocalDateTime.now().plusDays(5);
        factory = new CollectionOfPublicationsFactory();


        // Act
        CollectionOfPublications result =
                factory.create(
                        collectionIdDouble,
                        name,
                        genreIdDouble,
                        userIdDouble,
                        isPrivate,
                        publicUntil
                );

        // Assert
        assertNotNull(result);
        assertEquals(name, result.getCollectionName());
        assertEquals(genreIdDouble, result.getCollectionGenre());
        assertEquals(userIdDouble, result.getOwnerId());
        assertEquals(isPrivate, result.getIsPrivate());
    }

    @Test
    void recreateShouldThrowExceptionWhenIdIsNull() {

        LocalDateTime publicUntil = LocalDateTime.now().plusDays(5);

        factory = new CollectionOfPublicationsFactory();

        assertThrows(IllegalArgumentException.class,
                () -> factory.create(
                        null,
                        "Fantasy Collection",
                        genreIdDouble,
                        userIdDouble,
                        true,
                        publicUntil
                ));
    }

    @Test
    void recreateShouldThrowExceptionWhenNameIsNull() {
        // arrange
        LocalDateTime publicUntil = LocalDateTime.now().plusDays(5);
        factory = new CollectionOfPublicationsFactory();

        // act + assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(
                        collectionIdDouble,
                        null,
                        genreIdDouble,
                        userIdDouble,
                        true,
                        publicUntil
                ));
    }

    @Test
    void recreateShouldThrowExceptionWhenGenreIdIsNull() {

        // arrange
        LocalDateTime publicUntil = LocalDateTime.now().plusDays(5);
        factory = new CollectionOfPublicationsFactory();

        // act + assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(
                        collectionIdDouble,
                        "Fantasy Collection",
                        null,
                        userIdDouble,
                        true,
                        publicUntil
                ));
    }

    @Test
    void recreateShouldThrowExceptionWhenUserIdIsNull() {
        // arrange
        LocalDateTime publicUntil = LocalDateTime.now().plusDays(5);
        factory = new CollectionOfPublicationsFactory();

        // act + assert
        assertThrows(IllegalArgumentException.class,
                () -> factory.create(
                        collectionIdDouble,
                        "Fantasy Collection",
                        genreIdDouble,
                        null,
                        true,
                        publicUntil
                ));
    }
}