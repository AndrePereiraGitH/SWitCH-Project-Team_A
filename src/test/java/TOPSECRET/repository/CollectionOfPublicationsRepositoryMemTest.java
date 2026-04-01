package TOPSECRET.repository;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublicationsFactory;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.CollectionOfPublicationsDTO_US018;

import TOPSECRET.dto.CollectionOfPublicationsDTO_US019;
import org.junit.jupiter.api.Test;

import javax.management.InstanceNotFoundException;
import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Unit tests for {@link CollectionOfPublicationsRepositoryMem} — covers US017, US018, and US020.
 *
 * <p>These tests use <b>mocks</b> for all domain collaborators ({@link User}, {@link Genre},
 * {@link CollectionOfPublications}, {@link CollectionOfPublicationsFactory}).
 * This means every test only checks the logic inside the repository itself —
 * not the behaviour of Genre, User, or CollectionOfPublications.
 * If a test fails here, the bug is in the repository.
 *
 * <p>Why mocks? The repository receives its collaborators from the outside.
 * By replacing them with mocks, we control exactly what they return,
 * so the test result depends only on what the repository does with that data.
 *
 * <p>Some tests (US018) use <b>Java Reflection</b> to pre-populate the private
 * {@code _collectionOfPublicationsList} field. The field has no setter — that is intentional,
 * to protect the internal list. Reflection is the only way to inject data in a unit test
 * without going through {@code createIfNotExists()}, which would add noise to the test.
 *
 * <p>Tests are grouped by user story:
 * <ul>
 *   <li>US017 — {@code createIfNotExists()}: factory null, null validations, happy path, duplicate check, same name different genre</li>
 *   <li>US018 — {@code addPublicationToCollection()}, {@code getUserCollections()}, {@code collectionDTOToDomain()}</li>
 *   <li>US020 — {@code findPublicByGenre()}</li>
 * </ul>
 */
class CollectionOfPublicationsRepositoryMemTest {


    // ============================== US017 ==============================

    // The repository constructor requires a factory — without it, it cannot create collections.
    // We pass null to check that the constructor rejects it immediately.
    @Test
    void shouldThrowExceptionWhenFactoryIsNull_US017() {
        assertThrows(IllegalArgumentException.class,
                () -> new CollectionOfPublicationsRepositoryMem(null));
    }

    // This is the main happy path for US017: a user picks a name and a genre, and the repository
    // delegates creation to the factory and stores the result.
    // We mock the factory so it returns our mock collection, then check that the repository
    // passes the data through correctly: name, genre, isPrivate=true, publications empty.
    // If any of these assertions fail, the repository is not forwarding or storing the result right.
//    @Test
//    void shouldCreateCollectionSuccessfully_US017() {
//        // Arrange — mock factory returns a mock collection when called with the right arguments
//        User mockUser = mock(User.class);
//        Genre mockGenre = mock(Genre.class);
//        CollectionOfPublications mockCollection = mock(CollectionOfPublications.class);
//        CollectionOfPublicationsFactory mockFactory = mock(CollectionOfPublicationsFactory.class);
//
//        when(mockFactory.create("Favorites", mockGenre, mockUser)).thenReturn(mockCollection);
//        when(mockCollection.getCollectionName()).thenReturn("Favorites");
//        when(mockCollection.getCollectionGenre()).thenReturn(mockGenre);
//        when(mockCollection.getIsPrivate()).thenReturn(true);
//        when(mockCollection.getPublications()).thenReturn(List.of());
//
//        CollectionOfPublicationsRepositoryMem sut =
//                new CollectionOfPublicationsRepositoryMem(mockFactory);

        // Act — ask the repository to create the collection
//        CollectionOfPublications result =
//                sut.createIfNotExists(mockUser, "Favorites", mockGenre);

        // Assert — result must exist, carry the correct name and genre, be private, and be empty
//        assertNotNull(result);
//        assertEquals("Favorites", result.getCollectionName());
//        assertEquals(mockGenre, result.getCollectionGenre());
//        assertTrue(result.getIsPrivate());          // US017 rule: always starts private
//        assertTrue(result.getPublications().isEmpty()); // US017 rule: always starts empty
//    }

    // Every collection must have an owner — without a user it belongs to nobody.
    // The repository must check this before delegating to the factory.
//    @Test
//    void shouldThrowExceptionWhenUserIsNull_US017() {
//        // Arrange — valid factory, null user on purpose
//        CollectionOfPublicationsRepositoryMem sut = new CollectionOfPublicationsRepositoryMem(mock(CollectionOfPublicationsFactory.class));
//        Genre mockGenre = mock(Genre.class);
//
//        // Act & Assert — null user must be rejected before creation starts
//        assertThrows(IllegalArgumentException.class,
//                () -> sut.createIfNotExists(null, "Favorites", mockGenre));
//    }

    // A collection must always have a name — null means there is nothing to identify it by.
    // The repository must catch this before passing it to the factory.
//    @Test
//    void shouldThrowExceptionWhenNameIsNull_US017() {
//        // Arrange — valid factory and user, null name on purpose
//        CollectionOfPublicationsRepositoryMem sut = new CollectionOfPublicationsRepositoryMem(mock(CollectionOfPublicationsFactory.class));
//        User mockUser = mock(User.class);
//        Genre mockGenre = mock(Genre.class);
//
//        // Act & Assert — null name must be rejected before creation starts
//        assertThrows(IllegalArgumentException.class,
//                () -> sut.createIfNotExists(mockUser, null, mockGenre));
//    }

    // US017 requires the user to pick a genre — a collection without a genre is not valid.
    // The repository must reject null genre before delegating to the factory.
//    @Test
//    void shouldThrowExceptionWhenGenreIsNull_US017() {
//        // Arrange — valid factory and user, null genre on purpose
//        CollectionOfPublicationsRepositoryMem sut = new CollectionOfPublicationsRepositoryMem(mock(CollectionOfPublicationsFactory.class));
//        User mockUser = mock(User.class);
//
//        // Act & Assert — null genre must be rejected before creation starts
//        assertThrows(IllegalArgumentException.class,
//                () -> sut.createIfNotExists(mockUser, "Favorites", null));
//    }

    // A user can have two collections with the same name if the genre is different.
    // For example: "Favorites / Fiction" and "Favorites / Science" are not the same collection.
    // Two separate mock Genre objects have different references, so the duplicate check must treat them as different.
    // The second call must succeed and return a non-null collection.
//    @Test
//    void shouldAllowSameNameWithDifferentGenre_US017() {
//        // Arrange — same user, same name, but two different mock genres (different references)
//        User mockUser = mock(User.class);
//        Genre mockGenre1 = mock(Genre.class);
//        Genre mockGenre2 = mock(Genre.class);
//
//        CollectionOfPublications mockCollection1 = mock(CollectionOfPublications.class);
//        CollectionOfPublications mockCollection2 = mock(CollectionOfPublications.class);
//
//        CollectionOfPublicationsFactory mockFactory = mock(CollectionOfPublicationsFactory.class);
//        when(mockFactory.create("Favorites", mockGenre1, mockUser)).thenReturn(mockCollection1);
//        when(mockFactory.create("Favorites", mockGenre2, mockUser)).thenReturn(mockCollection2);
//
//        // The repo reads these fields from the stored collection to check duplicates
//        when(mockCollection1.getOwner()).thenReturn(mockUser);
//        when(mockCollection1.getCollectionName()).thenReturn("Favorites");
//        when(mockCollection1.getCollectionGenre()).thenReturn(mockGenre1);
//
//        CollectionOfPublicationsRepositoryMem sut =
//                new CollectionOfPublicationsRepositoryMem(mockFactory);
//
//        // First creation — stored in the repository list
//        sut.createIfNotExists(mockUser, "Favorites", mockGenre1);
//
//        // Act — same name, different genre — must not throw because it is not a duplicate
//        CollectionOfPublications result =
//                sut.createIfNotExists(mockUser, "Favorites", mockGenre2);
//
//        // Assert — second collection was created successfully
//        assertNotNull(result);
//    }

    // A user cannot have two collections with the exact same name AND same genre.
    // The repository must detect this and throw before creating a second one.
    // To make the duplicate check work, we stub the mock collection to return the same owner,
    // name, and genre that the duplicate check will read — otherwise the mock would return
    // default values and the repository would not detect the collision.
//    @Test
//    void shouldThrowExceptionWhenDuplicateCollectionExists_US017() {
//        // Arrange — mock factory returns the same mock collection on first call
//        User mockUser = mock(User.class);
//        Genre mockGenre = mock(Genre.class);
//        CollectionOfPublications mockCollection = mock(CollectionOfPublications.class);
//        CollectionOfPublicationsFactory mockFactory = mock(CollectionOfPublicationsFactory.class);
//
//        when(mockFactory.create("Favorites", mockGenre, mockUser)).thenReturn(mockCollection);
//        // These stubs let the repository read back the stored collection and detect the duplicate
//        when(mockCollection.getOwner()).thenReturn(mockUser);
//        when(mockCollection.getCollectionName()).thenReturn("Favorites");
//        when(mockCollection.getCollectionGenre()).thenReturn(mockGenre);
//
//        CollectionOfPublicationsRepositoryMem sut =
//                new CollectionOfPublicationsRepositoryMem(mockFactory);
//
//        sut.createIfNotExists(mockUser, "Favorites", mockGenre); // first creation — stored in the list
//
//        // Act & Assert — same owner, name, and genre again — must be detected as duplicate and throw
//        assertThrows(IllegalArgumentException.class,
//                () -> sut.createIfNotExists(mockUser, "Favorites", mockGenre));
//    }

    // ============================== end US017 ==============================


    // ============================== US018 ==============================

    /**
     * Ensures publication is added successfully when:
     * - All parameters are valid
     * - User owns the collection
     * - User owns the library
     * - Publication exists in user library
     */
//    @Test
//    void shouldAddPublicationSuccessfully() {
//
//        // ARRANGE
//        CollectionOfPublicationsRepositoryMem sut =
//                new CollectionOfPublicationsRepositoryMem(mock(CollectionOfPublicationsFactory.class));
//
//        User user = mock(User.class);
//        Library library = mock(Library.class);
//        CollectionOfPublications collection = mock(CollectionOfPublications.class);
//        PublicationSample publication = mock(PublicationSample.class);
//
//        when(collection.getOwner()).thenReturn(user);
//        when(library.getUser()).thenReturn(user);
//        when(library.containsPublication(publication)).thenReturn(true);
//        when(collection.addPublication(publication)).thenReturn(true);
//
//        // ACT
//        boolean result = sut.addPublicationToCollection(
//                user, library, collection, publication);
//
//        // ASSERT
//        assertTrue(result);
//        verify(collection).addPublication(publication);
//    }

//    @Test
//    void shouldReturnFalseWhenCollectionRejectsPublication() {
//
//        // ARRANGE
//        CollectionOfPublicationsRepositoryMem sut =
//                new CollectionOfPublicationsRepositoryMem(mock(CollectionOfPublicationsFactory.class));
//
//        User user = mock(User.class);
//        Library library = mock(Library.class);
//        CollectionOfPublications collection = mock(CollectionOfPublications.class);
//        PublicationSample publication = mock(PublicationSample.class);
//
//        when(collection.getOwner()).thenReturn(user);
//        when(library.getUser()).thenReturn(user);
//        when(library.containsPublication(publication)).thenReturn(true);
//
//        // collection rejects publication
//        when(collection.addPublication(publication)).thenReturn(false);
//
//        // ACT
//        boolean result = sut.addPublicationToCollection(
//                user, library, collection, publication);
//
//        // ASSERT
//        assertFalse(result);
//    }

    /**
     * Ensures method returns false when any argument is null.
     */
//    @Test
//    void shouldReturnFalseWhenAnyParameterIsNull() {
//
//        // ARRANGE
//        CollectionOfPublicationsRepositoryMem sut =
//                new CollectionOfPublicationsRepositoryMem(mock(CollectionOfPublicationsFactory.class));
//
//        // ACT + ASSERT
//        assertFalse(sut.addPublicationToCollection(null, null, null, null));
//    }

    /**
     * Ensures method returns false when user does not own the collection.
     */
//    @Test
//    void shouldReturnFalseWhenUserIsNotOwner() {
//
//        // ARRANGE
//        CollectionOfPublicationsRepositoryMem sut =
//                new CollectionOfPublicationsRepositoryMem(mock(CollectionOfPublicationsFactory.class));
//
//        User user = mock(User.class);
//        User otherUser = mock(User.class);
//        Library library = mock(Library.class);
//        CollectionOfPublications collection = mock(CollectionOfPublications.class);
//        PublicationSample publication = mock(PublicationSample.class);
//
//        when(collection.getOwner()).thenReturn(otherUser);
//
//        // ACT
//        boolean result = sut.addPublicationToCollection(
//                user, library, collection, publication);
//
//        // ASSERT
//        assertFalse(result);
//    }

    /**
     * Ensures method returns false when publication
     * is not present in user library.
     */
//    @Test
//    void shouldReturnFalseWhenPublicationNotInLibrary() {
//
//        // ARRANGE
//        CollectionOfPublicationsRepositoryMem sut =
//                new CollectionOfPublicationsRepositoryMem(mock(CollectionOfPublicationsFactory.class));
//
//        User user = mock(User.class);
//        Library library = mock(Library.class);
//        CollectionOfPublications collection = mock(CollectionOfPublications.class);
//        PublicationSample publication = mock(PublicationSample.class);
//
//        when(collection.getOwner()).thenReturn(user);
//        when(library.getUser()).thenReturn(user);
//        when(library.containsPublication(publication)).thenReturn(false);
//
//        // ACT
//        boolean result = sut.addPublicationToCollection(
//                user, library, collection, publication);
//
//        // ASSERT
//        assertFalse(result);
//    }
//
//    @Test
//    void shouldReturnFalseWhenLibraryDoesNotBelongToUser() {
//
//        // ARRANGE
//        CollectionOfPublicationsRepositoryMem sut =
//                new CollectionOfPublicationsRepositoryMem(mock(CollectionOfPublicationsFactory.class));
//
//        User user = mock(User.class);
//        User otherUser = mock(User.class);
//
//        Library library = mock(Library.class);
//        CollectionOfPublications collection = mock(CollectionOfPublications.class);
//        PublicationSample publication = mock(PublicationSample.class);
//
//        when(collection.getOwner()).thenReturn(user);
//        when(library.getUser()).thenReturn(otherUser); // library owner diferente
//
//        // ACT
//        boolean result = sut.addPublicationToCollection(
//                user, library, collection, publication);
//
//        // ASSERT
//        assertFalse(result);
//    }

    /**
     * Ensures exception is thrown when user is null.
     */
    @Test
    void shouldThrowExceptionWhenUserIsNull() {

        // ARRANGE
        CollectionOfPublicationsRepositoryMem sut =
                new CollectionOfPublicationsRepositoryMem(mock(CollectionOfPublicationsFactory.class));

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> sut.getUserCollections(null));
    }

    @Test
    void shouldAddCollectionToRepositorySuccessfully() {

        // ARRANGE
        CollectionOfPublicationsRepositoryMem sut =
                new CollectionOfPublicationsRepositoryMem(mock(CollectionOfPublicationsFactory.class));

        CollectionOfPublications collection = mock(CollectionOfPublications.class);

        // ACT
        boolean result = sut.addCollectionToRepo(collection);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenCollectionAlreadyExistsInRepository() {

        // ARRANGE
        CollectionOfPublicationsRepositoryMem sut =
                new CollectionOfPublicationsRepositoryMem(mock(CollectionOfPublicationsFactory.class));

        CollectionOfPublications collection = mock(CollectionOfPublications.class);

        sut.addCollectionToRepo(collection);

        // ACT
        boolean result = sut.addCollectionToRepo(collection);

        // ASSERT
        assertFalse(result);
    }



    /**
     * Ensures user collections are retrieved correctly.
     */
    @Test
    void shouldReturnCollectionsOwnedByUser() throws Exception {

        // ARRANGE
        CollectionOfPublicationsRepositoryMem sut =
                new CollectionOfPublicationsRepositoryMem(mock(CollectionOfPublicationsFactory.class));

        UserId user = mock(UserId.class);
        UserId otherUser = mock(UserId.class);

        CollectionOfPublications collection1 = mock(CollectionOfPublications.class);
        CollectionOfPublications collection2 = mock(CollectionOfPublications.class);

        when(collection1.getOwner()).thenReturn(user);
        when(collection2.getOwner()).thenReturn(otherUser);

        // the list is private with no setter, so reflection is the only way to pre-populate it in a unit test
        Field field = CollectionOfPublicationsRepositoryMem.class
                .getDeclaredField("_collectionOfPublicationsList");
        field.setAccessible(true);

        List<CollectionOfPublications> list =
                (List<CollectionOfPublications>) field.get(sut);

        list.add(collection1);
        list.add(collection2);

        // ACT
        List<CollectionOfPublications> result =
                sut.getUserCollections(user);

        // ASSERT
        assertEquals(1, result.size());
        assertEquals(collection1, result.get(0));
    }

    /**
     * Ensures correct domain object is returned when DTO matches.
     */
//    @Test
//    void shouldReturnMatchingCollectionFromDTO()
//            throws Exception {
//
//        // ARRANGE
//        CollectionOfPublicationsRepositoryMem sut =
//                new CollectionOfPublicationsRepositoryMem(mock(CollectionOfPublicationsFactory.class));
//
//        User owner = mock(User.class);
//        when(owner.getUserName()).thenReturn("john");
//
//        CollectionOfPublications collection =
//                mock(CollectionOfPublications.class);
//
//        when(collection.getCollectionName()).thenReturn("MyCollection");
//        when(collection.getOwner()).thenReturn(owner);
//
//        // Insert mock into internal list
//        Field field = CollectionOfPublicationsRepositoryMem.class
//                .getDeclaredField("_collectionOfPublicationsList");
//        field.setAccessible(true);
//
//        List<CollectionOfPublications> list =
//                (List<CollectionOfPublications>) field.get(sut);
//
//        list.add(collection);
//
//        CollectionOfPublicationsDTO_US018 dto =
//                mock(CollectionOfPublicationsDTO_US018.class);
//
//        when(dto.getName()).thenReturn("MyCollection");
//        when(dto.getOwnerName()).thenReturn("john");
//
//        // ACT
//        CollectionOfPublications result =
//                sut.collectionDTOToDomain(dto);
//
//        // ASSERT
//        assertEquals(collection, result);
//    }

    /**
     * Ensures exception is thrown when DTO is null.
     */
//    @Test
//    void shouldThrowExceptionWhenDTOIsNull() {
//
//        // ARRANGE
//        CollectionOfPublicationsRepositoryMem sut =
//                new CollectionOfPublicationsRepositoryMem(mock(CollectionOfPublicationsFactory.class));
//
//        // ACT + ASSERT
//        assertThrows(IllegalArgumentException.class,
//                () -> sut.collectionDTOToDomain(null));
//    }

    /**
     * Ensures InstanceNotFoundException is thrown
     * when no collection matches the DTO.
     */
//    @Test
//    void shouldThrowExceptionWhenCollectionNotFound() {
//
//        // ARRANGE
//        CollectionOfPublicationsRepositoryMem sut =
//                new CollectionOfPublicationsRepositoryMem(mock(CollectionOfPublicationsFactory.class));
//
//        CollectionOfPublicationsDTO_US018 dto =
//                mock(CollectionOfPublicationsDTO_US018.class);
//
//        when(dto.getName()).thenReturn("Unknown");
//        when(dto.getOwnerName()).thenReturn("john");
//
//        // ACT + ASSERT
//        assertThrows(InstanceNotFoundException.class,
//                () -> sut.collectionDTOToDomain(dto));
//    }

    // ============================== end US018 ==============================

    // US019 ---START---------------------------------------------------------
    // getAllCollectionOfPublicationsByUser

    @Test
    void getAllCollectionOfPublicationsByUser_shouldThrowWhenOwnerIsNull() {
        CollectionOfPublicationsFactory factoryDouble = mock(CollectionOfPublicationsFactory.class);


        // Arrange
        CollectionOfPublicationsRepositoryMem repo = new CollectionOfPublicationsRepositoryMem(factoryDouble);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> repo.getAllCollectionOfPublicationsByUser(null));
    }

    @Test
    void getAllCollectionOfPublicationsByUser_shouldReturnEmptyListWhenNoCollections() {
        CollectionOfPublicationsFactory factoryDouble = mock(CollectionOfPublicationsFactory.class);

        // Arrange
        CollectionOfPublicationsRepositoryMem repo = new CollectionOfPublicationsRepositoryMem(factoryDouble);
        UserId ownerDouble = mock(UserId.class);

        // Act
        List<CollectionOfPublications> result = repo.getAllCollectionOfPublicationsByUser(ownerDouble);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    /**
     * Verifies that when the repository contains collections belonging to different owners,
     * only the collections owned by the given user are returned.
     * Two collections are added via the factory — one for each owner —
     * and only the one matching the queried owner should appear in the result.
     */
    @Test
    void getAllCollectionOfPublicationsByUser_shouldReturnOnlyCollectionsOfGivenOwner() {
        CollectionOfPublicationsFactory factoryDouble = mock(CollectionOfPublicationsFactory.class);


        // Arrange
        CollectionOfPublicationsRepositoryMem repo = new CollectionOfPublicationsRepositoryMem(factoryDouble);

        UserId ownerDouble = mock(UserId.class);
        UserId otherOwnerDouble = mock(UserId.class);

        CollectionOfPublications collection1Double = mock(CollectionOfPublications.class);
        CollectionOfPublications collection2Double = mock(CollectionOfPublications.class);

        when(collection1Double.getOwner()).thenReturn(ownerDouble);
        when(collection1Double.getCollectionName()).thenReturn("Collection 1");
        when(collection1Double.getCollectionGenre()).thenReturn(mock(GenreID.class));
        when(collection2Double.getOwner()).thenReturn(otherOwnerDouble);

        when(factoryDouble.create(any(), any(), any()))
                .thenReturn(collection1Double)
                .thenReturn(collection2Double);

        repo.createIfNotExists(ownerDouble, "Collection 1", mock(GenreID.class));
        repo.createIfNotExists(ownerDouble, "Collection 2", mock(GenreID.class));

        // Act
        List<CollectionOfPublications> result = repo.getAllCollectionOfPublicationsByUser(ownerDouble);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(collection1Double));
    }


    // getCollectionByDTO

//    @Test
//    void getCollectionByDTO_shouldThrowWhenNoDTOMatches() {
//        CollectionOfPublicationsFactory factoryDouble = mock(CollectionOfPublicationsFactory.class);
//
//        // Arrange
//        CollectionOfPublicationsRepositoryMem repo = new CollectionOfPublicationsRepositoryMem(factoryDouble);
//
//        CollectionOfPublicationsDTO_US019 dtoDouble = mock(CollectionOfPublicationsDTO_US019.class);
//        when(dtoDouble.get_name()).thenReturn("Unknown");
//        when(dtoDouble.get_genreName()).thenReturn("Fiction");
//        when(dtoDouble.getOwnerName()).thenReturn("alice");
//
//        // Act & Assert
//        assertThrows(InstanceNotFoundException.class,
//                () -> repo.getCollectionByDTO(dtoDouble));
//    }

    /**
     * Verifies that getCollectionByDTO returns the correct domain object when a collection
     * matching all three fields (name, genre, owner username) exists in the repository.
     * The collection mock is configured to return values that match the DTO double exactly.
     */
//    @Test
//    void getCollectionByDTO_shouldReturnMatchingCollection() throws Exception {
//        CollectionOfPublicationsFactory factoryDouble = mock(CollectionOfPublicationsFactory.class);
//
//        // Arrange
//        CollectionOfPublicationsRepositoryMem repo = new CollectionOfPublicationsRepositoryMem(factoryDouble);
//
//        User ownerDouble = mock(User.class);
//        Genre genreDouble = mock(Genre.class);
//        CollectionOfPublications collectionDouble = mock(CollectionOfPublications.class);
//
//        when(ownerDouble.getUserName()).thenReturn("alice");
//        when(genreDouble.getGenreType()).thenReturn("Fiction");
//        when(collectionDouble.getCollectionName()).thenReturn("My Collection");
//        when(collectionDouble.getCollectionGenre()).thenReturn(genreDouble);
//        when(collectionDouble.getOwner()).thenReturn(ownerDouble);
//
//        when(factoryDouble.create(any(), any(), any())).thenReturn(collectionDouble);
//        repo.createIfNotExists(ownerDouble,"My Collection", genreDouble);
//
//        CollectionOfPublicationsDTO_US019 dtoDouble = mock(CollectionOfPublicationsDTO_US019.class);
//        when(dtoDouble.get_name()).thenReturn("My Collection");
//        when(dtoDouble.get_genreName()).thenReturn("Fiction");
//        when(dtoDouble.getOwnerName()).thenReturn("alice");
//
//        // Act
//        CollectionOfPublications result = repo.getCollectionByDTO(dtoDouble);
//
//        // Assert
//        assertEquals(collectionDouble, result);
//    }


//    @Test
//    void getCollectionByDTO_shouldThrowWhenOnlyNameDoesNotMatch() throws Exception {
//        CollectionOfPublicationsFactory factoryDouble = mock(CollectionOfPublicationsFactory.class);
//
//        // Arrange
//        CollectionOfPublicationsRepositoryMem repo = new CollectionOfPublicationsRepositoryMem(factoryDouble);
//
//        User ownerDouble = mock(User.class);
//        Genre genreDouble = mock(Genre.class);
//        CollectionOfPublications collectionDouble = mock(CollectionOfPublications.class);
//
//        when(ownerDouble.getUserName()).thenReturn("alice");
//        when(genreDouble.getGenreType()).thenReturn("Fiction");
//        when(collectionDouble.getCollectionName()).thenReturn("My Collection");
//        when(collectionDouble.getCollectionGenre()).thenReturn(genreDouble);
//        when(collectionDouble.getOwner()).thenReturn(ownerDouble);
//
//        when(factoryDouble.create(any(), any(), any())).thenReturn(collectionDouble);
//        repo.createIfNotExists(ownerDouble, "My Collection", genreDouble);
//
//        CollectionOfPublicationsDTO_US019 dtoDouble = mock(CollectionOfPublicationsDTO_US019.class);
//        when(dtoDouble.get_name()).thenReturn("Different Name"); // <-- nome diferente
//        when(dtoDouble.get_genreName()).thenReturn("Fiction");   // genre igual
//        when(dtoDouble.getOwnerName()).thenReturn("alice");      // owner igual
//
//        // Act & Assert
//        assertThrows(InstanceNotFoundException.class,
//                () -> repo.getCollectionByDTO(dtoDouble));
//    }

    // makeCollectionOfPublicationsPublic

//    @Test
//    void makeCollectionOfPublicationsPublic_shouldThrowWhenDTOIsNull() {
//        CollectionOfPublicationsFactory factoryDouble = mock(CollectionOfPublicationsFactory.class);
//
//        // Arrange
//        CollectionOfPublicationsRepositoryMem repo = new CollectionOfPublicationsRepositoryMem(factoryDouble);
//
//        // Act & Assert
//        assertThrows(IllegalArgumentException.class,
//                () -> repo.makeCollectionOfPublicationsPublic(null));
//    }

//    @Test
//    void makeCollectionOfPublicationsPublic_shouldThrowWhenCollectionNotFound() {
//        CollectionOfPublicationsFactory factoryDouble = mock(CollectionOfPublicationsFactory.class);
//
//        // Arrange
//        CollectionOfPublicationsRepositoryMem repo = new CollectionOfPublicationsRepositoryMem(factoryDouble);
//
//        CollectionOfPublicationsDTO_US019 dtoDouble = mock(CollectionOfPublicationsDTO_US019.class);
//        when(dtoDouble.get_name()).thenReturn("Unknown");
//        when(dtoDouble.get_genreName()).thenReturn("Fiction");
//        when(dtoDouble.getOwnerName()).thenReturn("alice");
//
//        // Act & Assert
//        assertThrows(InstanceNotFoundException.class,
//                () -> repo.makeCollectionOfPublicationsPublic(dtoDouble));
//    }


    /**
     * Verifies that makeCollectionOfPublicationsPublic sets the collection's private flag to false.
     * The collection mock is configured to match the DTO, and set_isPrivate is verified
     * to have been called with false after the operation completes.
     */
//    @Test
//    void makeCollectionOfPublicationsPublic_shouldSetCollectionToPublic() throws Exception {
//        CollectionOfPublicationsFactory factoryDouble = mock(CollectionOfPublicationsFactory.class);
//
//        // Arrange
//        CollectionOfPublicationsRepositoryMem repo = new CollectionOfPublicationsRepositoryMem(factoryDouble);
//
//        User ownerDouble = mock(User.class);
//        Genre genreDouble = mock(Genre.class);
//        CollectionOfPublications collectionDouble = mock(CollectionOfPublications.class);
//
//        when(ownerDouble.getUserName()).thenReturn("alice");
//        when(genreDouble.getGenreType()).thenReturn("Fiction");
//        when(collectionDouble.getCollectionName()).thenReturn("My Collection");
//        when(collectionDouble.getCollectionGenre()).thenReturn(genreDouble);
//        when(collectionDouble.getOwner()).thenReturn(ownerDouble);
//
//        when(factoryDouble.create(any(), any(), any())).thenReturn(collectionDouble);
//        repo.createIfNotExists(ownerDouble, "My Collection", genreDouble);
//
//        CollectionOfPublicationsDTO_US019 dtoDouble = mock(CollectionOfPublicationsDTO_US019.class);
//        when(dtoDouble.get_name()).thenReturn("My Collection");
//        when(dtoDouble.get_genreName()).thenReturn("Fiction");
//        when(dtoDouble.getOwnerName()).thenReturn("alice");
//
//        // Act
//        boolean result = repo.makeCollectionOfPublicationsPublic(dtoDouble);
//
//        // Assert
//        assertTrue(result);
//        verify(collectionDouble).set_isPrivate(false);
//    }



    //----------------------END---------------------------------------------


    // ============================== US020 ==============================

    /**
     * US020 - Get list of public lists by a given genre
     */

    /**
     * Happy path
     * Ensures the method correctly lists all public collections of a user given genre
     */
//    @Test
//    void shouldReturnListOfPublicListByGivenGenre() {
//
//        CollectionOfPublicationsRepositoryMem sut = new CollectionOfPublicationsRepositoryMem(mock(CollectionOfPublicationsFactory.class));
//
//        // User setup
//        User owner = mock(User.class);
//        when(owner.getUserName()).thenReturn("john");
//
//        // Genre setup
//        Genre desiredGenre = mock(Genre.class);
//        when(desiredGenre.getGenreType()).thenReturn("Drama");
//
//        // Collection creation
//        CollectionOfPublications c1 = mock(CollectionOfPublications.class);
//        CollectionOfPublications c2 = mock(CollectionOfPublications.class);
//        CollectionOfPublications c3 = mock(CollectionOfPublications.class);
//
//        // Collection user
//        when(c1.getOwner()).thenReturn(owner);
//        when(c2.getOwner()).thenReturn(owner);
//        when(c3.getOwner()).thenReturn(owner);
//
//        // Collection privacy
//        when(c1.getIsPrivate()).thenReturn(true);
//        when(c2.getIsPrivate()).thenReturn(false);
//        when(c3.getIsPrivate()).thenReturn(false);
//
//        // Collection name
//        when(c1.getCollectionName()).thenReturn("Collection 1");
//        when(c2.getCollectionName()).thenReturn("Collection 2");
//        when(c3.getCollectionName()).thenReturn("Collection 3");
//
//        // Collection genre
//        when(c1.getCollectionGenre()).thenReturn(desiredGenre);
//        when(c2.getCollectionGenre()).thenReturn(desiredGenre);
//        when(c3.getCollectionGenre()).thenReturn(desiredGenre);
//
//        // Add collections to repo
//        sut.addCollectionToRepo(c1);
//        sut.addCollectionToRepo(c2);
//        sut.addCollectionToRepo(c3);
//
//        assertEquals(2, sut.findPublicByGenre(desiredGenre).size());
//
//    }

    /**
     * When a user inputs an invalid genre, method should throw illegal argument exception
     */
//    @Test
//    void shouldThrowExceptionWhenGenreIsNull() {
//
//        CollectionOfPublicationsRepositoryMem sut = new CollectionOfPublicationsRepositoryMem(mock(CollectionOfPublicationsFactory.class));
//
//        Genre desiredGenre = mock(Genre.class);
//        when(desiredGenre.getGenreType()).thenReturn(null);
//
//        assertNull(desiredGenre.getGenreType());
//        assertThrows(IllegalArgumentException.class, () -> sut.findPublicByGenre(desiredGenre));
//    }

    /**
     * When a user input is blank, method should throw illegal argument exception
     */
//    @Test
//    void shouldThrowExceptionWhenGenreTypeIsBlank() {
//
//        CollectionOfPublicationsRepositoryMem sut = new CollectionOfPublicationsRepositoryMem(mock(CollectionOfPublicationsFactory.class));
//
//        Genre desiredGenre = mock(Genre.class);
//        when(desiredGenre.getGenreType()).thenReturn(" ");
//
//        assertThrows(IllegalArgumentException.class, () -> sut.findPublicByGenre(desiredGenre));
//    }

    /**
     * Ensures the return list is empty when there are no public lists of the given genre
     */
//    @Test
//    void shouldReturnEmptyListWhenNoPublicListsExist() {
//
//        CollectionOfPublicationsRepositoryMem sut = new CollectionOfPublicationsRepositoryMem(mock(CollectionOfPublicationsFactory.class));
//
//        // Genre setup
//        Genre desiredGenre = mock(Genre.class);
//        when(desiredGenre.getGenreType()).thenReturn("Action");
//
//        Genre otherGenre = mock(Genre.class);
//        when(otherGenre.getGenreType()).thenReturn("Drama");
//
//
//        // Collection setup
//        CollectionOfPublications c1 = mock(CollectionOfPublications.class);
//        CollectionOfPublications c2 = mock(CollectionOfPublications.class);
//        CollectionOfPublications c3 = mock(CollectionOfPublications.class);
//
//        // Collection privacy
//        when(c1.getIsPrivate()).thenReturn(true);
//        when(c2.getIsPrivate()).thenReturn(true);
//        when(c3.getIsPrivate()).thenReturn(false);
//
//        // Collection genre
//        when(c1.getCollectionGenre()).thenReturn(desiredGenre);
//        when(c2.getCollectionGenre()).thenReturn(otherGenre);
//        when(c3.getCollectionGenre()).thenReturn(otherGenre);
//
//        // Adding collections to repo
//        sut.addCollectionToRepo(c1);
//        sut.addCollectionToRepo(c2);
//        sut.addCollectionToRepo(c3);
//
//        // Assert must be 0
//        assertEquals(0, sut.findPublicByGenre(desiredGenre).size());
//    }

    // ============================== end US020 ==============================

}