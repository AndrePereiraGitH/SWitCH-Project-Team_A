package TOPSECRET.controller;

import static org.junit.jupiter.api.Assertions.*;

import TOPSECRET.domain.Author;
import TOPSECRET.domain.Genre;
import TOPSECRET.domain.Country;
import TOPSECRET.domain.BibliographicalData;
import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.Library;
import TOPSECRET.domain.Publication;
import TOPSECRET.domain.PublicationType;
import TOPSECRET.domain.Publisher;
import TOPSECRET.domain.Contact;
import TOPSECRET.domain.User;
import TOPSECRET.domain.Code;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.repository.PublicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.time.LocalDate;
import java.util.Locale;


class AddPublicationToLibraryControllerTest {
    //verify that the controller asks the library for the publications
    //verify that the controller asks the repository for the remaining publications
    //verify that the controller returns the correct list

    private Library library;
    private PublicationRepository publicationRepository;
    private AddPublicationToLibraryController controller;

    private Publication p1;
    private Publication p2;
    private Publication p3;

    private PublicationType type;
    private Country country1, country2, country3;
    private Author author1, author2, author3;
    private Genre genre1, genre2, genre3;
    private Contact contact1, contact2, contact3;
    private Publisher publisher1, publisher2, publisher3;
    private BibliographicalData data1, data2, data3;

    @BeforeEach
    void preparation() {
        library = new Library();
        publicationRepository = new PublicationRepository();
        controller = new AddPublicationToLibraryController(library, publicationRepository);

        //Creating a valid PublicationType
        PublicationType type = new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "Details1");

        //Creating valid countries
        Country country1 = new Country("Portugal");
        Country country2 = new Country("France");
        Country country3 = new Country("Germany");

        //Creating valid Authors
        Author author1 = new Author("Author One", country1, LocalDate.of(1980, 1, 1), "Bio1");
        Author author2 = new Author("Author Two", country2, LocalDate.of(1985, 2, 2), "Bio2");
        Author author3 = new Author("Author Three", country3, LocalDate.of(1990, 3, 3), "Bio3");

        //Creating valid Genres
        Genre genre1 = new Genre("Fiction");
        Genre genre2 = new Genre("Adventure");
        Genre genre3 = new Genre("Mystery");

        //Creating valid Publisher
        Contact contact1 = new Contact();
        Contact contact2 = new Contact();
        Contact contact3 = new Contact();

        Publisher publisher1 = new Publisher("Publisher1", "VAT001", contact1);
        Publisher publisher2 = new Publisher("Publisher2", "VAT002", contact2);
        Publisher publisher3 = new Publisher("Publisher3", "VAT003", contact3);

        //Creating BibliographicalData
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

        //Creating Users
        User user1 = new User();
        User user2 = new User();
        User user3 = new User();

        //Creating some sample publications, using empty users from the empty constructor in User class
        p1 = new Publication(data1, type, user1, PublicationStatus.READ, "1st", Code.fromIsbn10("001"));
        p2 = new Publication(data2, type, user2, PublicationStatus.READ, "1st", Code.fromIsbn13("002"));
        p3 = new Publication(data3, type, user3, PublicationStatus.READ, "1st", Code.fromIsbn10("003"));

        //Add samples to repository
        publicationRepository.addPublication(p1);
        publicationRepository.addPublication(p2);
        publicationRepository.addPublication(p3);

        //Add publication to library
        library.addPublication(p1);
    }
    @Test //This test should return a list with 2 publications since we added 1 publication to the library
    void getListOfOtherPublications_shouldReturnPublicationsNotInLibrary(){
        //Act
        List<Publication> otherPublications = controller.getListOfOtherPublications();

        //Assert
        assertEquals(2, otherPublications.size(), "There should be 2 publications not in the Library");
        assertTrue(otherPublications.contains(p2), "The list should contain p2");
        assertTrue(otherPublications.contains(p3), "The list should contain p3");
        assertFalse(otherPublications.contains(p1), "The list should not contain the p1 because it's in the library");
    }

    @Test //This test should return all publications since we create a new library but without any publication
    void getListOfOtherPublications_shouldReturnAllPublications_emptyLibrary(){
        //Make sure the library is clear
        Library clearedLibrary = new Library();
        AddPublicationToLibraryController controller2 = new AddPublicationToLibraryController(clearedLibrary, publicationRepository);

        List<Publication> otherPublications = controller2.getListOfOtherPublications();

        assertEquals(3, otherPublications.size(), "There should be 3 publications, none of which are in the Library");
        assertTrue(otherPublications.contains(p1), "The list should contain p1");
        assertTrue(otherPublications.contains(p2), "The list should contain p2");
        assertTrue(otherPublications.contains(p3), "The list should contain p3");
        assertFalse(otherPublications.contains(null), "The list must have publications");
    }

    @Test //This test should return an empty list since we add the remaining 2 publications to the library
    void getListOfOtherPublications_shouldReturnNothing_libraryHasAllPublications(){
        //Make sure the library has all publications (add the missing ones)
        library.addPublication(p2);
        library.addPublication(p3);

        AddPublicationToLibraryController controller3 = new AddPublicationToLibraryController(library, publicationRepository);

        List<Publication> otherPublications = controller3.getListOfOtherPublications();

        assertTrue(otherPublications.isEmpty(), "There are no publications outside the library");
    }

    @Test //This test verifies that the getAll returns a copy of the original list so it can't be modified externally
    void getListOfOtherPublications_shouldNotBeAbleToModifyTheList(){
        List<Publication> originalListReadOnly = library.getAll();
        assertThrows(UnsupportedOperationException.class, () -> originalListReadOnly.add(p2),
                "The list from the library is unmodifiable!");
    }
}