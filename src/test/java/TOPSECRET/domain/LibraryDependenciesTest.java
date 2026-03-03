package TOPSECRET.domain;

import static org.junit.jupiter.api.Assertions.*;

import TOPSECRET.domain.Enum.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

/**
 * Unit tests for the {@link Library} class.
 * <p>
 * This test class verifies the behavior of the Library class including construction,
 * publication management, and getter methods.
 * </p>
 */
class LibraryDependenciesTest {

    private User user1;
    private Library library;

    private PublicationSample sample1;
    private PublicationSample sample2;
    private PublicationSample sample3;


    @BeforeEach
    void preparation() {
        Country country = new Country("Portugal");
        Profile profile = new Profile("profile1", country, LocalDate.of(1990, 1, 1));
        Contact contact = new Contact();
        user1 = new User("user1", contact, profile, UserRole.NORMAL_USER);

        library = new Library( "Test Library", user1);

        //Country
        Country country1 = new Country("Portugal");
        Country country2 = new Country("France");
        Country country3 = new Country("Germany");


        //Author - used for Bibliographical Data
        Author author1 = new Author("Author One", country1, LocalDate.of(1980, 1, 1), "Bio1");
        Author author2 = new Author("Author Two", country2, LocalDate.of(1985, 2, 2), "Bio2");
        Author author3 = new Author("Author Three", country3, LocalDate.of(1990, 3, 3), "Bio3");

        //Genre - used for Bibliographical Data
        Genre genre1 = new Genre("Fiction");
        Genre genre2 = new Genre("Adventure");
        Genre genre3 = new Genre("Mystery");

        //BibliographicalData
        BibliographicalData data1 = new BibliographicalData(
                "Title1", List.of(author1), Locale.ENGLISH,
                LocalDate.of(2020, 1, 1), List.of(genre1), "Some text 1"
        );
        BibliographicalData data2 = new BibliographicalData(
                "Title2", List.of(author2), Locale.ENGLISH,
                LocalDate.of(2021, 2, 2), List.of(genre2), "Some text 2"
        );

        BibliographicalData data3 = new BibliographicalData(
                "Title3", List.of(author3), Locale.ENGLISH,
                LocalDate.of(2022, 3, 3), List.of(genre3), "Some text 3"
        );

        //Users (using the empty constructor)
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();

        // Edition
        Code code = Code.fromIssn("97831614");
        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
        PublicationType pubType = new PublicationType(MediaFormat.MAGAZINE, TypeOfBinding.CASE_BINDING, "Some details");
        Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
        Edition edition = new Edition("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

        // PublicationStatus
        PublicationStatus pubStatus1 = PublicationStatus.SOLD;
        PublicationStatus pubStatus2 = PublicationStatus.FOR_SALE;
        PublicationStatus pubStatus3 = PublicationStatus.READING;

        // Condition
        Condition condition1 = Condition.NEW;
        Condition condition2 = Condition.LIKE_NEW;
        Condition condition3 = Condition.VERY_GOOD;

        //Creating some sample publications, dependent on a lot of different classes, created above for testing purposes

        sample1 = new PublicationSample(data1, user1, pubStatus1, edition, condition1);
        sample2 = new PublicationSample(data2, user2, pubStatus2, edition, condition2);
        sample3 = new PublicationSample(data3, user3, pubStatus3, edition, condition3);
    }


    // ============ Constructor Tests ============

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when library name is null or empty.
     */
    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowExceptionWhenLibraryNameIsInvalid(String libraryName) {
        // Arrange
        Profile profile = new Profile("name", new Country("Portugal"), LocalDate.now());
        User user = new User("valid name", new Contact(), profile, UserRole.NORMAL_USER);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Library(libraryName, user));
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when user is null.
     */
    @Test
    void shouldThrowExceptionWhenUserIsNull() {
        // Arrange
        String libraryName = "Some valid name";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Library(libraryName, null));
    }

    /**
     * Tests that a Library instance is properly initialized with valid parameters.
     */
    @Test
    void shouldInitializeInstanceWhenValidParameters() {
        // Arrange
        String libraryName = "Library name";
        Profile profile = new Profile("name", new Country("Portugal"), LocalDate.now());
        User user = new User("valid name", new Contact(), profile, UserRole.NORMAL_USER);

        // Act
        Library result = new Library(libraryName, user);

        // Assert
        assertNotNull(result);
    }


    // ============ Getter Tests ============

    /**
     * Tests that {@link Library#getLibraryName()} returns the correct library name.
     */
    @Test
    void getLibraryName_shouldReturnLibraryName() {
        // Act
        String result = library.getLibraryName();

        // Assert
        assertEquals("Test Library", result);
    }

    /**
     * Tests that {@link Library#getUser()} returns the correct user.
     */
    @Test
    void getUser_shouldReturnUser() {
        // Act
        User result = library.getUser();

        // Assert
        assertEquals(user1, result);
    }


    // ============ Add Publication Tests ============

    /**
     * Tests that a publication is successfully added to the library and returned.
     */
    @Test
    void addPublicationToTheLibrary_shouldReturnPublication() {
        // Act
        PublicationSample pub = library.addPublication(sample1);

        // Assert
        assertEquals(1, library.getAllPublications().size());
        assertTrue(library.containsPublication(sample1));
        assertEquals(pub, sample1);
    }

    /**
     * Tests that a {@link NullPointerException} is thrown when attempting to add a null publication.
     */
    @Test
    void addPublicationToTheLibrary_shouldThrowAnExceptionWhenPublicationIsNull() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> library.addPublication(null));
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when attempting to add a duplicate publication.
     */
    @Test
    void addPublicationToTheLibrary_shouldThrowAnExceptionWhenPublicationIsDuplicate() {
        // Arrange
        library.addPublication(sample1);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> library.addPublication(sample1));
    }


    // ============ Get All Publications Tests ============

    /**
     * Tests that {@link Library#getAllPublications()} returns an empty list when no publications are added.
     */
    @Test
    void getAllPublications_shouldReturnEmptyListIfLibraryEmpty() {
        // Act & Assert
        assertTrue(library.getAllPublications().isEmpty());
    }

    /**
     * Tests that {@link Library#getAllPublications()} returns a list with all added publications.
     */
    @Test
    void getAllPublications_shouldReturnListWithAddedPublications() {
        // Act
        library.addPublication(sample1);
        library.addPublication(sample2);
        List<PublicationSample> listWithPublications = library.getAllPublications();

        // Assert
        assertEquals(2, listWithPublications.size());
        assertTrue(listWithPublications.contains(sample1));
        assertTrue(listWithPublications.contains(sample2));
    }

    /**
     * Tests that {@link Library#getAllPublications()} returns an unmodifiable list.
     */
    @Test
    void getAllPublications_shouldReturnUnmodifiableList() {
        // Act
        library.addPublication(sample1);
        List<PublicationSample> listOfPublications = library.getAllPublications();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> listOfPublications.add(sample2));
    }


    // ============ Contains Publication Tests ============

    /**
     * Tests that {@link Library#containsPublication(PublicationSample)} returns true when publication exists.
     */
    @Test
    void containsPublication_shouldReturnTrueIfPublicationExists() {
        // Act
        library.addPublication(sample3);

        // Assert
        assertTrue(library.containsPublication(sample3));
    }

    /**
     * Tests that {@link Library#containsPublication(PublicationSample)} returns false when publication does not exist.
     */
    @Test
    void containsPublication_shouldReturnFalseIfPublicationDoesntExist() {
        // Act
        library.addPublication(sample3);

        // Assert
        assertFalse(library.containsPublication(sample1));
    }

    // ============ FindLibraryPublicationsAvailableForSale Tests ============

    @Test
    void findLibraryPublicationsAvailableForSale_ShouldReturnOnlyEligibleSamples() {
        // Arrange
        library.addPublication(sample1);                                // publicationStatus = SOLD
        library.addPublication(sample2);                                // publicationStatus = FOR_SALE
        library.addPublication(sample3);                                // publicationStatus = READING

        // Act
        List<PublicationSample> availablePublicationsForSale = library.findLibraryPublicationsAvailableForSale();

        // Assert
        assertEquals(1, availablePublicationsForSale.size());
        assertTrue(availablePublicationsForSale.contains(sample3));
    }

    @Test
    void findLibraryPublicationsAvailableForSale_ShouldReturnEmptyListWhenNoSamplesAreEligible() {
        // Arrange
        library.addPublication(sample1);                                // publicationStatus = SOLD
        library.addPublication(sample2);                                // publicationStatus = FOR_SALE

        // Act
        List<PublicationSample> availablePublicationsForSale = library.findLibraryPublicationsAvailableForSale();

        // Assert
        assertTrue(availablePublicationsForSale.isEmpty());
    }

    @Test
    void findLibraryPublicationsAvailableForSale_ShouldReturnEmptyListWhenLibraryIsEmpty() {
        // Act
        List<PublicationSample> availablePublicationsForSale = library.findLibraryPublicationsAvailableForSale();

        // Assert
        assertTrue(availablePublicationsForSale.isEmpty());
    }
}