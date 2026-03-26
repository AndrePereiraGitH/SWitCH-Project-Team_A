/*package TOPSECRET.domain;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.BibliographicalDataFactory;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.domain.ValueObjects.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link CollectionOfPublications}.
 *
 * <p>These tests use <b>real objects</b> — no mocks. Every domain class involved
 * (User, Genre, CollectionOfPublications, PublicationSample) is created for real.
 * This means the tests check that the classes work correctly together, not just in isolation.
 *
 * <p>This file covers tests for three user stories that all use {@link CollectionOfPublications}:
 * <ul>
 *   <li><b>US017</b> — creating a collection: constructor validation, empty start, private flag,
 *       equals/hashCode contract, defensive copy</li>
 *   <li><b>US018</b> — adding publications: success, duplicate prevention, null handling</li>
 *   <li><b>US019</b> — changing privacy: making a collection public or private</li>
 * </ul>
 *
 * <p>Helper methods at the top ({@code createValidUser}, {@code createValidGenre},
 * {@code createValidPublication}, {@code createBaseCollection}) are shared across all sections
 * to avoid repeating the same setup code in every test.

class CollectionOfPublicationsDependenciesTest {

    // ========================= Test helpers =========================

    /**
     * Creates a valid {@link User} for testing — used across US017, US018, and US019 tests.
     *
     * @return a valid User with complete profile and contact information

    private User createValidUser()
    {
        Contact contact = new Contact("912222222", "351", null, null);
        Country country = new Country("PORTUGAL");
        LocalDate birthday = LocalDate.of(1980, 4, 29);
        Profile profile = new Profile("John Doe", country, birthday);

        return new User("John Doe", contact, profile, UserRole.NORMAL_USER);
    }

    /**
     * Creates a valid {@link Genre} for testing — used across US017, US018, and US019 tests.
     *
     * @return a Genre with type "Fiction"

    private Genre createValidGenre()
    {
        return new Genre("Fiction");
    }

    /**
     * Creates a valid {@link PublicationSample} for testing — used in US018 tests.
     *
     * @return a complete PublicationSample with all required attributes

    private PublicationSample createValidPublication()
    {
        // Create Bibliographical data for publication

        BibliographicalDataFactory bdFactory = new BibliographicalDataFactory();

        Country country = new Country("PORTUGAL");

        Profile profile = new Profile("John Doe", country, LocalDate.of(1980, 4, 29));

        Author author = new Author(
                "John Doe", country, LocalDate.of(1980, 4, 29),"Test author for testing"
        );

        Publisher publisher = new Publisher(
                "Test Publisher",
                "PT123456789",
                new Contact("912345678", "351", null, null)
        );

        BibliographicalData bibliographicalData = bdFactory.create(
                "Test Title",
                List.of(author),
                Locale.ENGLISH,
//                publisher,
                LocalDate.of(2020, 1, 1),
                List.of(new Genre("Fiction")),
//                200,
                "Sample text"
        );

        // Create a publication type
        PublicationType publicationType = new PublicationType(
                MediaFormat.BOOK,
                TypeOfBinding.HARDBACK,
                "Standard publication"
        );

        // Create a user
        User user = new User(
                "validUser",
                new Contact("911111111", "351", null, null),
                profile,
                UserRole.ADMIN   // adjust enum value if needed
        );

        // Create a code
        Code code = Code.fromIsbn13("9783161484100");


        PublicationStatus publicationStatus = PublicationStatus.FOR_SALE;
//        String edition = "First Edition";
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.CASE_BINDING, "Some details");
        Edition edition= new Edition("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));


        // Create condition
        Condition condition1 = Condition.NEW;
        Condition condition2 = Condition.LIKE_NEW;
        Condition condition3 = Condition.VERY_GOOD;


        // Create publicationSample
        return new PublicationSample(
                bibliographicalData,
                user,
                publicationStatus,
                edition,
                condition1
        );
    }

    /**
     * Creates a base valid {@link CollectionOfPublications} for testing — used across all sections.
     *
     * @return a CollectionOfPublications named "Favorites" with genre "Fiction"

    private CollectionOfPublications createBaseCollection()
    {
        return new CollectionOfPublications(
                "Favorites",
                createValidGenre(),
                createValidUser()
        );
    }


    // ============================== US017 ==============================

    // US017 defines the creation of a collection — name, genre, owner, and the private flag.
    // The constructor must reject invalid input, the collection must start empty,
    // and equals/hashCode must work correctly so duplicates can be detected.

    // --- Constructor validation ---

    // A collection must always have a name — null is not valid.
    // The constructor must throw before creating anything.
    @Test
    void shouldThrowExceptionWhenCollectionNameIsNull()
    {
        // Arrange
        Genre genre = createValidGenre();
        User user = createValidUser();

        // Act & Assert — null name must throw
        assertThrows(IllegalArgumentException.class, () -> {
            new CollectionOfPublications(null, genre, user);
        });
    }

    // A name with only spaces is considered blank — it is not a real name.
    // The constructor must also reject this.
    @Test
    void shouldThrowExceptionWhenCollectionNameIsBlank()
    {
        // Arrange
        Genre genre = createValidGenre();
        User user = createValidUser();

        // Act & Assert — blank name (only spaces) must throw
        assertThrows(IllegalArgumentException.class, () -> {
            new CollectionOfPublications("   ", genre, user);
        });
    }

    // US017 requires the user to pick a genre — a collection without a genre is not valid.
    @Test
    void shouldThrowExceptionWhenGenreIsNull()
    {
        // Arrange
        User user = createValidUser();

        // Act & Assert — null genre must throw
        assertThrows(IllegalArgumentException.class, () -> {
            new CollectionOfPublications("Favorites", null, user);
        });
    }

    // Every collection must have an owner — a collection without a user makes no sense.
    @Test
    void shouldThrowExceptionWhenUserIsNull()
    {
        // Arrange
        Genre genre = createValidGenre();

        // Act & Assert — null user must throw
        assertThrows(IllegalArgumentException.class, () -> {
            new CollectionOfPublications("Favorites", genre, null);
        });
    }

    // --- Empty start ---

    // US017 rule: when a collection is created, it must start with no publications.
    // Publications are added later in US018 — the collection is just a container at first.
    @Test
    void shouldCreateEmptyCollection_US017() {
        // Arrange & Act
        CollectionOfPublications collection = createBaseCollection();

        // Assert — the list exists but is empty
        assertNotNull(collection.getPublications());
        assertTrue(collection.getPublications().isEmpty());
    }

    // --- equals and hashCode contract ---

    // equals and hashCode were overridden so Java can compare collections by their data,
    // not by memory address. Without this, two collections with the same content would look
    // different to Java. The duplicate check in the repository (US017) depends on this.

    // Two collections with the same name, genre, owner, and privacy must be equal.
    // A collection with a different name must not be equal.
    // hashCode must also match when two collections are equal.
    @Test
    void shouldUseEqualsAndHashCode_US017() {
        // Arrange
        User user = createValidUser();
        Genre genre = createValidGenre();
        CollectionOfPublications c1 = new CollectionOfPublications("MyCollection", genre, user);
        CollectionOfPublications c2 = new CollectionOfPublications("MyCollection", genre, user);
        CollectionOfPublications c3 = new CollectionOfPublications("DifferentName", genre, user);

        // Act & Assert — same data = equal, different name = not equal, hashCode matches equals
        assertEquals(c1, c2);
        assertNotEquals(c1, c3);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    // equals must be reflexive — an object must always equal itself.
    @Test
    void equals_shouldReturnTrue_whenComparingToSelf_US017() {
        // Arrange
        CollectionOfPublications collection = createBaseCollection();

        // Act & Assert — same reference must be equal
        assertEquals(collection, collection);
    }

    // equals must return false when compared to null — never throw a NullPointerException.
    @Test
    void equals_shouldReturnFalse_whenComparingToNull_US017() {
        // Arrange
        CollectionOfPublications collection = createBaseCollection();

        // Act & Assert — null comparison must return false, not crash
        assertNotEquals(null, collection);
    }

    // equals must return false when compared to a completely different type.
    @Test
    void equals_shouldReturnFalse_whenComparingToDifferentType_US017() {
        // Arrange
        CollectionOfPublications collection = createBaseCollection();

        // Act & Assert — a String is not a CollectionOfPublications
        assertNotEquals("uma string qualquer", collection);
    }

    // Two collections with the same name and owner but different genre must not be equal.
    // Genre is part of what makes a collection unique in US017.
    @Test
    void equals_shouldReturnFalse_whenGenreIsDifferent_US017() {
        // Arrange
        User user = createValidUser();
        CollectionOfPublications c1 = new CollectionOfPublications("Favorites", new Genre("Fiction"), user);
        CollectionOfPublications c2 = new CollectionOfPublications("Favorites", new Genre("History"), user);

        // Act & Assert — different genre means different collection
        assertNotEquals(c1, c2);
    }

    // Two collections with the same name and genre but owned by different users must not be equal.
    // The owner is also part of what makes a collection unique.
    @Test
    void equals_shouldReturnFalse_whenOwnerIsDifferent_US017() {
        // Arrange
        User user1 = createValidUser();
        User user2 = new User(
                "otherUser",
                new Contact("913333333", "351", null, null),
                new Profile("Other", new Country("PORTUGAL"), LocalDate.of(1985, 1, 1)),
                UserRole.NORMAL_USER
        );
        Genre genre = createValidGenre();
        CollectionOfPublications c1 = new CollectionOfPublications("Favorites", genre, user1);
        CollectionOfPublications c2 = new CollectionOfPublications("Favorites", genre, user2);

        // Act & Assert — different owner means different collection
        assertNotEquals(c1, c2);
    }

    // The privacy flag is also part of equality — a private and a public collection
    // with the same name, genre, and owner are NOT considered the same.
    @Test
    void equals_shouldReturnFalse_whenIsPrivateDiffers_US017() {
        // Arrange
        User user = createValidUser();
        Genre genre = createValidGenre();
        CollectionOfPublications privateCollection = new CollectionOfPublications("Favorites", genre, user);
        CollectionOfPublications publicCollection = new CollectionOfPublications("Favorites", genre, user);
        publicCollection.set_isPrivate(false);

        // Act & Assert — privacy flag is part of equality
        assertNotEquals(privateCollection, publicCollection);
    }

    // hashCode must differ when the collections are not equal.
    // If equal objects must have the same hash, then different objects should have different hashes.
    @Test
    void hashCode_shouldDiffer_whenCollectionsAreDifferent_US017() {
        // Arrange
        User user = createValidUser();
        CollectionOfPublications c1 = new CollectionOfPublications("Favorites", new Genre("Fiction"), user);
        CollectionOfPublications c2 = new CollectionOfPublications("Favorites", new Genre("History"), user);

        // Act & Assert — different collections should also have different hash codes
        assertNotEquals(c1.hashCode(), c2.hashCode());
    }

    // --- Defensive copy ---

    // getPublications() must return a copy of the internal list, not the real one.
    // If the caller modifies the returned list, the collection must stay unchanged.
    // This protects the collection's data from being changed from outside the class.
    @Test
    void getPublications_shouldReturnDefensiveCopy_US017() {
        // Arrange
        CollectionOfPublications collection = createBaseCollection();

        // Act — get the list and try to modify it
        List<PublicationSample> returned = collection.getPublications();
        returned.add(null); // change the returned list on purpose

        // Assert — the real collection must still be empty
        assertTrue(collection.getPublications().isEmpty());
    }

    // ============================== end US017 ==============================


    // ============================== US018 ==============================

    // SUCCESS TESTS
    // Verifies that two collections with the same data are equal
    @Test
    void shouldCreateEqualCollectionsSuccessfully()            // US018
    {
        CollectionOfPublications expected = createBaseCollection();
        CollectionOfPublications result   = createBaseCollection();

        assertEquals(expected, result);   // Collections must be equal
    }

    // Verifies that a publication can be added to the collection
    @Test
    void shouldAddPublicationSuccessfully() {                   // US018
        // US018: Add a publication to an existing collection

        // ARRANGE
        CollectionOfPublications expected = createBaseCollection();
        CollectionOfPublications result   = createBaseCollection();
        PublicationSample publication = createValidPublication();

        // ACT
        expected.addPublication(publication);
        result.addPublication(publication);

        // ASSERT
        assertEquals(expected, result);   // Both collections must contain the same publication
    }

    // Verifies that duplicate publications are not added
    @Test
    void shouldNotAddDuplicatePublication()                     // US018
    {
        CollectionOfPublications expected = createBaseCollection();
        CollectionOfPublications result   = createBaseCollection();
        PublicationSample publication = createValidPublication();

        expected.addPublication(publication);
        result.addPublication(publication);
        result.addPublication(publication);   // Try to add duplicate

        assertEquals(expected, result);   // Collection should contain only one publication
    }

    // Verifies that adding null publication does not change the collection
    @Test
    void shouldNotAddNullPublication()                          // US018
    {
        CollectionOfPublications expected = createBaseCollection();
        CollectionOfPublications result   = createBaseCollection();

        result.addPublication(null);   // Attempt to add null

        assertEquals(expected, result);   // Collection must remain unchanged
    }

    @Test
    void shouldReturnTrueWhenPublicationIsAdded() {                // US018
        CollectionOfPublications collection = createBaseCollection();
        PublicationSample publication = createValidPublication();

        boolean result = collection.addPublication(publication);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenPublicationIsDuplicate() {           // US018
        CollectionOfPublications collection = createBaseCollection();
        PublicationSample publication = createValidPublication();

        collection.addPublication(publication);

        boolean result = collection.addPublication(publication);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenPublicationIsNull() {                // US018
        CollectionOfPublications collection = createBaseCollection();

        boolean result = collection.addPublication(null);

        assertFalse(result);
    }

    @Test
    void containsPublication_shouldReturnTrueWhenPublicationExists() {   // US018
        CollectionOfPublications collection = createBaseCollection();
        PublicationSample publication = createValidPublication();

        collection.addPublication(publication);

        assertTrue(collection.containsPublication(publication));
    }

    @Test
    void containsPublication_shouldReturnFalseWhenPublicationDoesNotExist() {   // US018
        CollectionOfPublications collection = createBaseCollection();
        PublicationSample publication = createValidPublication();

        assertFalse(collection.containsPublication(publication));
    }

    // ============================== end US018 ==============================


    // ============================== US019 ==============================

    /**
     * US019 set_isPrivate false test - make collection public

    @Test
    void set_isPrivate_shouldChangeIsPrivateToFalse_whenFalseIsPassed() {               // US019
        // arrange
        CollectionOfPublications collection = createBaseCollection();

        // act
        collection.set_isPrivate(false);

        // assert
        assertFalse(collection.getIsPrivate());
    }

    /**
     * US019 set_isPrivate test - make collection private

    @Test
    void set_isPrivate_shouldChangeIsPrivateToTrue_whenTrueIsPassed() {                 // US019
        // arrange
        CollectionOfPublications collection = createBaseCollection();
        collection.set_isPrivate(false);

        // act
        collection.set_isPrivate(true);

        // assert
        assertTrue(collection.getIsPrivate());
    }

    // ============================== end US019 ==============================

}*/