package TOPSECRET.domain.CollectionOfPublications;

import TOPSECRET.domain.User.User;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link CollectionOfPublications} — US017.
 *
 * <p>These tests use <b>mocks</b> for {@link Genre} and {@link User}.
 * This means the tests only check the constructor logic of {@link CollectionOfPublications} itself —
 * not the behaviour of Genre or User. If a test fails here, the bug is in this class.
 *
 * <p>Why mocks and not real objects? The constructor of {@code CollectionOfPublications}
 * receives Genre and User as parameters. By mocking them, we remove any risk of those classes
 * causing a failure — so we can be sure the result comes only from this class.
 *
 * <p>All tests in this file belong to <b>US017</b>, which defines what a collection is
 * and what rules apply when it is created:
 * <ul>
 *   <li>Name must not be null or blank</li>
 *   <li>Genre must not be null</li>
 *   <li>User (owner) must not be null</li>
 *   <li>Collection always starts as private</li>
 *   <li>Collection always starts with no publications</li>
 * </ul>
 */
class CollectionOfPublicationsTest {


    // ============================== US017 ==============================

    @Test
    void shouldThrowExceptionWhenCollectionNameIsNull() {
        // Arrange
        GenreId genreDouble = mock(GenreId.class);
        UserId userDouble = mock(UserId.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new CollectionOfPublications(null, genreDouble, userDouble));
    }


    @Test
    void shouldThrowExceptionWhenCollectionNameIsBlank() {
        // Arrange
        GenreId genreDouble = mock(GenreId.class);
        UserId userDouble = mock(UserId.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new CollectionOfPublications(" ", genreDouble, userDouble));
    }

    @Test
    void shouldThrowExceptionWhenGenreIsNull() {
        // Arrange
        UserId userDouble = mock(UserId.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new CollectionOfPublications("Favorites", null, userDouble));
    }

    @Test
    void shouldThrowExceptionWhenUserIsNull() {
        // Arrange
        GenreId genreDouble = mock(GenreId.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new CollectionOfPublications("Favorites", genreDouble, null));
    }

    @Test
    void shouldInitializeInstanceWhenValidParameters() {
        // Arrange
        String name = "Favorites";
        GenreId genreDouble = mock(GenreId.class);
        UserId userDouble = mock(UserId.class);

        // Act
        CollectionOfPublications result = new CollectionOfPublications(name, genreDouble, userDouble);

        // Assert
        assertEquals(name, result.getCollectionName());
        assertEquals(genreDouble, result.getCollectionGenre());
        assertEquals(userDouble, result.getOwnerId());
        assertTrue(result.getIsPrivate());          // US017 rule: always private at creation
    }

    // ============================== end US017 ==============================


    @Test
    void shouldReturnNonNullId() {

        //Arrange
        GenreId genre = mock(GenreId.class);
        UserId user = mock(UserId.class);

        CollectionOfPublications result
                = new CollectionOfPublications("Cool books", genre, user);

        //Act + assert
        assertNotNull(result.id());
    }

    @Test
    void equals_shouldReturnTrueWhenSameInstance() {

        //Arrange
        UserId user1 = mock(UserId.class);
        GenreId genre1 = mock(GenreId.class);

        CollectionOfPublications sameCollection =
                new CollectionOfPublications("Nice books", genre1, user1);

        //Act + assert
        assertTrue(sameCollection.equals(sameCollection));
    }

    @Test
    void shouldReturnFalseWhenNull() {

        //Arrange
        UserId user1 = mock(UserId.class);
        GenreId genre1 = mock(GenreId.class);

        CollectionOfPublications anyCollection =
                new CollectionOfPublications("Not null", genre1, user1);

        //Act + assert
        assertFalse(anyCollection.equals(null));
    }

    @Test
    void equalsShouldReturnFalseWhenComparedToNotACollection() {

        //Arrange
        UserId user1 = mock(UserId.class);
        GenreId genre1 = mock(GenreId.class);

        CollectionOfPublications realCollection =
                new CollectionOfPublications("I have books", genre1, user1);

        //Act + assert
        assertFalse(realCollection.equals("This is not a collection"));
    }

    @Test
    void equalsShouldReturnFalseWhenDifferentIds() {

        //Arrange
        GenreId genre1 = mock(GenreId.class);
        UserId user1 = mock(UserId.class);

        CollectionOfPublications anyCollection =
                new CollectionOfPublications("I also have books", genre1, user1);

        CollectionOfPublications anotherCollection =
                new CollectionOfPublications("I also have books", genre1, user1);

        //Act + assert
        assertFalse(anyCollection.equals(anotherCollection));
    }

    @Test
    void equalsShouldReturnTrueWhenIdsAreEqual() throws Exception {

        //Arrange
        GenreId genre = mock(GenreId.class);
        UserId user = mock(UserId.class);

        CollectionOfPublications collection007 = new CollectionOfPublications("A", genre, user);
        CollectionOfPublications collection008 = new CollectionOfPublications("B", genre, user);

        //Act
        var field = CollectionOfPublications.class.getDeclaredField("_id");
        field.setAccessible(true);
        field.set(collection008, field.get(collection007)); // force same ID

        //Assert
        assertTrue(collection007.equals(collection008));
    }

    @Test
    void hashCodeShouldMatchWhenIdsAreEqual() throws Exception {

        //Arrange
        GenreId genre = mock(GenreId.class);
        UserId user = mock(UserId.class);

        CollectionOfPublications collectionMerry =
                new CollectionOfPublications("Favorites", genre, user);
        CollectionOfPublications collectionPippin =
                new CollectionOfPublications("Favorites", genre, user);

        //Act
        var field = CollectionOfPublications.class.getDeclaredField("_id");
        field.setAccessible(true);
        field.set(collectionPippin, field.get(collectionMerry));

        //Assert
        assertEquals(collectionMerry.hashCode(), collectionPippin.hashCode());
    }

    @Test
    void sameAsShouldReturnTrueWhenAllAttributesAreEqual() {

        //Arrange
        GenreId genre1 = mock(GenreId.class);
        UserId user1 = mock(UserId.class);

        CollectionOfPublications collectionGrace =
                new CollectionOfPublications("Collection, I am", genre1, user1);

        CollectionOfPublications collectionRocky =
                new CollectionOfPublications("Collection, I am", genre1, user1);

        //Act + assert
        assertTrue(collectionGrace.sameAs(collectionRocky));
    }

    @Test
    void equalsShouldReturnFalseWhenIdsDiffer() {

        //Assert
        GenreId genre = mock(GenreId.class);
        UserId user = mock(UserId.class);

        CollectionOfPublications collectionLegolas =
                new CollectionOfPublications("A", genre, user);

        CollectionOfPublications collectionGimli =
                new CollectionOfPublications("A", genre, user);

        //Act + assert
        assertFalse(collectionLegolas.equals(collectionGimli));
    }

    @Test
    void sameAsShouldReturnFalseWhenDifferentName() {

        //Arrange
        GenreId genre1 = mock(GenreId.class);
        UserId user1 = mock(UserId.class);

        CollectionOfPublications collectionDante =
                new CollectionOfPublications("I am here", genre1, user1);

        CollectionOfPublications collectionVergil =
                new CollectionOfPublications("I am there", genre1, user1);

        //Act + assert
        assertFalse(collectionDante.sameAs(collectionVergil));
    }

    @Test
    void sameAsShouldReturnFalseWhenDifferentGenre() {

        //Arrange
        GenreId genre1 = mock(GenreId.class);
        GenreId genre2 = mock(GenreId.class);
        UserId user1 = mock(UserId.class);

        CollectionOfPublications collectionFrodo =
                new CollectionOfPublications("Really Cool Books", genre1, user1);

        CollectionOfPublications collectionAragorn =
                new CollectionOfPublications("Really Cool Books", genre2, user1);

        //Act + assert
        assertFalse(collectionFrodo.sameAs(collectionAragorn));
    }

    @Test
    void hashCodeShouldBeEqualForSameDataIgnoringCase() {

        //Arrange
        GenreId genre = mock(GenreId.class);
        UserId user = mock(UserId.class);

        CollectionOfPublications collectionEdgar =
                new CollectionOfPublications("EAP", genre, user);

        CollectionOfPublications collectionAllenPoe =
                new CollectionOfPublications("EAP", genre, user);

        //Act + assert
        assertEquals(collectionEdgar.hashCode(), collectionAllenPoe.hashCode());
    }

    @Test
    void hashCodeShouldBeConsistent() {

        //Arrange
        CollectionOfPublications collectionGondor =
                new CollectionOfPublications("Gondor", mock(GenreId.class), mock(UserId.class));

        int hash1 = collectionGondor.hashCode();
        int hash2 = collectionGondor.hashCode();

        //Act + assert
        assertEquals(hash1, hash2);
    }

    @Test
    void hashCodeShouldDifferForDifferentObjects() {

        //Arrange
        CollectionOfPublications collectionRohan =
                new CollectionOfPublications("Rohan", mock(GenreId.class), mock(UserId.class));


        CollectionOfPublications collectionShire =
                new CollectionOfPublications("Gohan", mock(GenreId.class), mock(UserId.class));

        //Act + assert
        assertNotEquals(collectionRohan.hashCode(), collectionShire.hashCode());
    }
}