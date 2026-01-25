package TOPSECRET.domain;

import static org.junit.jupiter.api.Assertions.*;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.Author;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.Enum.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class LibraryTest {
    //should test adding publications to the library
    //should test getting all publications present in the library
    //should test checking if a specific publication in is the library

    private User user1;

    private Library library;

    private Publication publication1;
    private Publication publication2;
    private Publication publication3;
    //Atributos do teste

    @BeforeEach //O que estiver neste BeforeEach será feito antes de cada teste
    void preparation(){
        Country country = new Country("Portugal");
        Profile profile = new Profile("profile1", country, LocalDate.of(1990, 1, 1));
        Contact contact = new Contact();
        user1 = new User("user1", contact, profile, UserRole.NORMAL_USER);

        library = new Library(new ArrayList<>(), "Test Library", user1);


        //PublicationType
        PublicationType publicationType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "HardCover");

        //Country
        Country country1 = new Country ("Portugal");
        Country country2 = new Country ("France");
        Country country3 = new Country ("Germany");


        //Author - used for Bibliographical Data
        Author author1 = new Author("Author One", country1, LocalDate.of(1980, 1, 1), "Bio1");
        Author author2 = new Author("Author Two", country2, LocalDate.of(1985, 2, 2), "Bio2");
        Author author3 = new Author("Author Three", country3, LocalDate.of(1990, 3, 3), "Bio3");

        //Genre - used for Bibliographical Data
        Genre genre1 = new Genre("Fiction");
        Genre genre2 = new Genre("Adventure");
        Genre genre3 = new Genre("Mystery");

        //Contact - used for Publisher
        Contact contact1 = new Contact();
        Contact contact2 = new Contact();
        Contact contact3 = new Contact();

        //Publisher - used for Bibliographical Data
        Publisher publisher1 = new Publisher("Publisher1", "VAT001", contact1);
        Publisher publisher2 = new Publisher("Publisher2", "VAT002", contact2);
        Publisher publisher3 = new Publisher("Publisher3", "VAT003", contact3);

        //BibliographicalData
        BibliographicalData data1 = new BibliographicalData(
                "Title1", List.of(author1), Locale.ENGLISH, publisher1,
                LocalDate.of(2020, 1, 1), List.of(genre1), 100, "Some text 1"
        );
        BibliographicalData data2 = new BibliographicalData(
                "Title2", List.of(author2), Locale.ENGLISH, publisher2,
                LocalDate.of(2021, 2, 2), List.of(genre2), 150, "Some text 2"
        );

        BibliographicalData data3 = new BibliographicalData(
                "Title3", List.of(author3), Locale.ENGLISH, publisher3,
                LocalDate.of(2022, 3, 3), List.of(genre3), 200, "Some text 3"
        );

        //Users (using the empty constructor)
        User user7 = new User();
        User user2 = new User();
        User user3 = new User();

        //Creating some sample publications, dependent on a lot of different classes, created above for testing purposes
        publication1 = new Publication(data1, publicationType, user1, PublicationStatus.READ, "1st", Code.fromIsbn10("001"));
        publication2 = new Publication(data2, publicationType, user2, PublicationStatus.READ, "1st", Code.fromIsbn13("002"));
        publication3 = new Publication(data3, publicationType, user3, PublicationStatus.READ, "1st", Code.fromIsbn10("003"));
    }

    //Now that we have the preparation setup, we begin the Tests


    @ParameterizedTest
    @NullAndEmptySource
    void constructor_shouldThrowExceptionWhenInvalidName(String libraryName) {
        // Arrange
        Profile profile = new Profile("name", new Country("Portugal"), LocalDate.now());
        User user = new User("valid name", new Contact(), profile, UserRole.NORMAL_USER);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Library(libraryName, user));
    }

    @Test
    void constructor_shouldThrowExceptionWhenInvalidUser() {
        // Arrange
        String libraryName = "Some valid name";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Library(libraryName, null));
    }

    @Test
    void constructor_initializesInstanceWhenValidParameters() {
        // Arrange
        String libraryName = "Library name";
        Profile profile = new Profile("name", new Country("Portugal"), LocalDate.now());
        User user = new User("valid name", new Contact(), profile, UserRole.NORMAL_USER);

        // Act
        Library result = new Library(libraryName, user);

        // Assert
        assertEquals(libraryName, result.getLibraryName());
        assertEquals(user, result.getUser());
        assertEquals(0, result.getAllPublications().size());
    }

    @Test
    void getLibraryName_shouldReturnLibraryName() {
        // Act
        String result = library.getLibraryName();

        // Assert
        assertEquals("Test Library", result);
    }

    @Test
    void getUser_shouldReturnUser() {
        // Act
        User result = library.getUser();

        // Assert
        assertEquals(user1, result);
    }

    @Test //This test sees if the publication1 gets added to the library using the method in Library
    void addPublicationToTheLibrary_shouldAddPublication(){
        library.addPublication(publication1);

        assertEquals(1, library.getAllPublications().size());
        assertTrue(library.containsPublication(publication1));
    }

    @Test //This test sees if adding the same publication more than one time doesn't create duplicates
    void addPublicationToTheLibrary_shouldNotAddIfDuplicate(){
        library.addPublication(publication1);
        library.addPublication(publication1);

        assertEquals(1, library.getAllPublications().size());
    }

    @Test //This test sees if, by asking to getAllPublications, we get an empty list, since no publication was added
    void getAllPublications_shouldReturnEmptyListIfLibraryEmpty(){
        assertTrue(library.getAllPublications().isEmpty());
    }

    @Test //This test sees if, by asking to getAllPublications, we get a list with the added publications
    void getAllPublications_shouldReturnListWithAddedPublications(){
        library.addPublication(publication1);
        library.addPublication(publication2);

        List<Publication> listWithPublications = library.getAllPublications();

        assertEquals(2, listWithPublications.size());
        assertTrue(listWithPublications.contains(publication1));
        assertTrue(listWithPublications.contains(publication2));
    }

    @Test //This test sees if the list that is provided by the getAllPublications is a copyOf, meaning its unmodifiable
    void getAllPublications_shouldReturnUnmodifiableList(){
        library.addPublication(publication1);

        List<Publication> listOfPublications = library.getAllPublications();

        assertThrows(UnsupportedOperationException.class, () -> listOfPublications.add(publication2), "Provided list can't be modified");
    }

    @Test //This test sees if the library contains a publication
    void containsPublication_shouldReturnTrueIfPublicationExists(){
        library.addPublication(publication3);

        assertTrue(library.containsPublication(publication3));
    }

    @Test //This test sees if the library has a publication (intended to respond false)
    void containsPublication_shouldReturnFalseIfPublicationDoesntExist(){
        library.addPublication(publication3);

        assertFalse(library.containsPublication(publication1));
    }
}