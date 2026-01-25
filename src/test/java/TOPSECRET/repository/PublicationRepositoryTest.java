package TOPSECRET.repository;


import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.util.Locale.ENGLISH;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


class PublicationRepositoryTest {

    // BibliographicalData
    Country country1 = new Country("Denmark");
    Country country2 = new Country("Germany");


    Profile profileAuthor1 = new Profile("Tove Ditlevsen", country1, LocalDate.of(1917, 12, 14));
    Author author1 = new Author(profileAuthor1, "valid bio1");
    Address address1 = new Address("R Maria", "12", "156-5", country2);
    Email email1 = new Email("Maria1", "gmail.com");
    Contact contact1 = new Contact("915945695", "+351", email1, address1);
    Publisher publisher1 = new Publisher("Some Publisher", "PT123456789", contact1);
    BibliographicalData bibliographicalData1 = new BibliographicalData(
            "Childhood, Youth, Dependency: The Copenhagen Trilogy",
            List.of(author1),
            Locale.ENGLISH,
            publisher1,
            LocalDate.of(2020, 1, 1),
            List.of(new Genre ("Fiction")),
            200,
            "Some text");
    BibliographicalData bibliographicalData2 = new BibliographicalData(
            "Childhood, Youth, Dependency: The Copenhagen Trilogy",
            List.of(author1),
            Locale.ENGLISH,
            publisher1,
            LocalDate.of(2020, 1, 1),
            List.of(new Genre ("Fiction")),
            200,
            "Some text");
    BibliographicalData bibliographicalData3 = new BibliographicalData(
            "Childhood, Youth, Dependency: The Copenhagen Trilogy",
            List.of(author1),
            Locale.ENGLISH,
            publisher1,
            LocalDate.of(2020, 1, 1),
            List.of(new Genre ("Fiction")),
            200,
            "Some text");

    // User
    Country country4 = new Country("Uganda");
    Profile profile2 = new Profile(
            "Test Profile",
            country4,
            LocalDate.of(1997, 8, 15)
    );
    UserRole userRole1 = UserRole.ADMIN;
    User user1 = new User("coolGuy", contact1, profile2, userRole1);
    User user2 = new User("coolGuy2", contact1, profile2, userRole1);
    User user3 = new User("coolGuy3", contact1, profile2, userRole1);

    User userTest = new User ("userTest", contact1, profile2, userRole1);

    //PublicationStatus
    PublicationStatus pubStatus1 = PublicationStatus.READING;
    PublicationStatus pubStatus2 = PublicationStatus.READ;
    PublicationStatus pubStatus3 = PublicationStatus.READING;


    //Edition
    String edition1 = "2nd Edition";
    String edition2 = "1st Edition";
    String edition3 = "3rd Edition";

    //Code
    Code code1 = Code.fromIsbn10("1234567890");
    Code code2 = Code.fromIsbn10("1234567891");
    Code code3 = Code.fromIsbn10("1234567892");


    //PublicationType
    PublicationType publicationType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "Paperback");

    //Publications
    Publication publication1 = new Publication(bibliographicalData1, publicationType, user1,
            pubStatus1, edition1, code1);
    Publication publication2 = new Publication(bibliographicalData2, publicationType, user2,
            pubStatus2, edition2, code2);
    Publication publication3 = new Publication(bibliographicalData3, publicationType, user3,
            pubStatus3, edition3, code3);

    // Happy path

    @Test
    void ensureANewPublicationIsCreatedAndAdded() {
        //Arrange
        List<Publication> emptyList = new ArrayList<>();
        PublicationRepository pr1 = new PublicationRepository(emptyList);

        //Act
        boolean realResult = pr1.createAndAdd(bibliographicalData1, publicationType, user1,
                pubStatus1, edition1, code1);

        // Assert
        assertTrue(realResult);

    }

    @Test
    void ensureThatADuplicatePublicationIsNotAdded(){
        //Arrange
        List<Publication> list = new ArrayList<>();
        list.add(publication1);
        PublicationRepository pr1 = new PublicationRepository(list);

        //Act
        boolean realResult=pr1.createAndAdd(bibliographicalData1, publicationType, user1,
                pubStatus1, edition1, code1);

        // Assert
        assertFalse(realResult);




    }

    @Test
    void ifPublicationIsExistentShouldReturnTrue() {
        //Arrange
        List<Publication> list = new ArrayList<>();
        list.add(publication1);
        PublicationRepository pr1 = new PublicationRepository(list);

        //Act
        boolean realResult = pr1.isExistent(publication1);

        // Assert
        assertTrue(realResult);
    }


    @Test
    void ifPublicationIsNotExistentShouldReturnFalse(){

        //Arrange
        List<Publication> list = new ArrayList<>();
        PublicationRepository pr1 = new PublicationRepository(list);

        //Act
        boolean realResult = pr1.isExistent(publication1);

        //Assert
        assertFalse(realResult);

    }


    @Test
    void ensureConstructorCopiesListNotOriginalReferences(){
        // Arrange
        List<Publication> list = new ArrayList<>();
        PublicationRepository pr1 = new PublicationRepository(list);
        list.add(publication1);

        //Act
        boolean realResult=pr1.isExistent(publication1);

        //Assert
        assertFalse(realResult);

    }

    @Test
    void getAllPublicationsReturnsAll() {
        // Arrange
        Publication pub1 = publication1;
        Publication pub2 = publication2;

        PublicationRepository repo = new PublicationRepository(List.of(pub1, pub2));

        // Act
        List<Publication> result = repo.getAllPublications();

        // Assert
        assertEquals(2, result.size());
        assertSame(pub1, result.get(0));
        assertSame(pub2, result.get(1));
    }

    @Test //This test is used to test immutability for the "getAllPublications()"
    void getAllPublicationsShouldReturnImmutableList(){

        PublicationRepository publicationRepository = new PublicationRepository(List.of(publication1));

        List<Publication> resultingList = publicationRepository.getAllPublications();

        assertThrows(UnsupportedOperationException.class, () -> resultingList.add(publication1));
    }

    @Test //This test is used to test the return of publications not present in the library
    void getListOfDifferentPublicationsShouldReturnListOfPublicationsNotPresentInLibrary(){

        PublicationRepository publicationRepository = new PublicationRepository(List.of(publication1));

        Library library = new Library(new ArrayList<>(), userTest.getUserName() + "'s Library", userTest);
        library.addPublication(publication1);

        List<Publication> resultingList = publicationRepository.getListOfDifferentPublications(library.getAllPublications());

        assertTrue(resultingList.isEmpty());
    }

    @Test //This test is used to test if having an empty library will result in a return of all publications
    void getListOfDifferentPublicationsShouldReturnListWithAllPublicationsInSystem(){

        PublicationRepository publicationRepository = new PublicationRepository(List.of(publication1, publication2, publication3));

        Library library = new Library(new ArrayList<>(), userTest.getUserName() + "'s Library", userTest);

        List<Publication> resultingList = publicationRepository.getListOfDifferentPublications(library.getAllPublications());

        assertEquals(3, resultingList.size());
        assertTrue(resultingList.contains(publication1));
        assertTrue(resultingList.contains(publication2));
        assertTrue(resultingList.contains(publication3));
    }

    @Test //This test is used to test if by having some publications in the library, it returns a list with the ones that are not in the library
    void getAListOfDifferentPublicationsShouldReturnListWithPublicationsNotInLibrary(){

        PublicationRepository publicationRepository = new PublicationRepository(List.of(publication1, publication2, publication3));

        Library library = new Library(new ArrayList<>(), userTest.getUserName() + "'s Library", userTest);
        library.addPublication(publication2);

        List<Publication> resultingList = publicationRepository.getListOfDifferentPublications(library.getAllPublications());

        assertEquals(2, resultingList.size());
        assertTrue(resultingList.contains(publication1));
        assertTrue(resultingList.contains(publication3));

    }

    @Test //This test is used to test immutability for the "getListOfDifferentPublications()"
    void getListOfDifferentPublicationsShouldReturnImmutableList(){

        PublicationRepository publicationRepository = new PublicationRepository(List.of(publication1, publication2, publication3));

        Library library = new Library(new ArrayList<>(), userTest.getUserName() + "'s Library", userTest);
        library.addPublication(publication2);

        List<Publication> resultingList = publicationRepository.getListOfDifferentPublications(library.getAllPublications());

        assertThrows(UnsupportedOperationException.class, () -> resultingList.add(publication2));
    }
}