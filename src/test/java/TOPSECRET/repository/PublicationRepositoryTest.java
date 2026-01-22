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
        // bibliographicalData 1 (bd1)
        String title1 = "The Age of AI: And Our Human Future";
        List<Author> authors1 = new ArrayList<>();
        Country country1 = new Country("UNITED_STATES");
        Profile profile2 = new Profile("Henry A Kissinger", country1, LocalDate.of(1923,05,27));
        Profile profile3 = new Profile("Eric Schmidt III", country1,LocalDate.of(1955,04,27));
        Profile profile4 = new Profile("Daniel Huttenlocher", country1,LocalDate.of(1959,01,01));
        Author author2 = new Author(profile2, "valid bio2");
        Author author3 = new Author(profile3, "valid bio3");
        Author author4 = new Author(profile4, "valid bio4");
        authors1.add(author2);
        authors1.add(author3);
        authors1.add(author4);
        Locale language1 = ENGLISH;
        Country country2 = new Country("UNITED_KINGDOM");
        Address address1 = new Address("Carmelite House","50", "EC4Y 0DZ", country2);
        Email email1 = new Email("enquiries", "hachette.co.uk");
        Contact contact1 = new Contact("2031227222", "+44", email1, address1);
        Publisher publisher1 = new Publisher("John Murray Press", "GB205505305", contact1);
        LocalDate publishDate1 = LocalDate.of(2022, 8, 4);
        List<Genre> genres1 = new ArrayList<>();
        genres1.add(new Genre ("Fantasy"));
        int numberOfPages1 = 272;
        String chunksOfText1 = "The sweet spot is in partnership—AI handles scale, I bring empathy, wisdom, and context.";

        BibliographicalData bd1 = new BibliographicalData(title1, authors1, language1, publisher1, publishDate1, genres1, numberOfPages1, chunksOfText1);


        // BibliographicalData 2 (db2)
        String title2 = "Childhood, Youth, Dependency: The Copenhagen Trilogy";
        Country country3 = new Country("Denmark");
        Country country4 = new Country("Germany");
        Profile profileAuthor1 = new Profile("Tove Ditlevsen", country1, LocalDate.of(1917, 12, 14));
        List <Author> authors2 = new ArrayList<>();
        Author author = new Author(profileAuthor1, "valid bio1");
        authors2.add(author);
        Address address2 = new Address("R Maria", "12", "156-5", country2);
        Email email2 = new Email("Maria1", "gmail.com");
        Contact contact2 = new Contact("915945695", "+351", email1, address1);
        Publisher publisher2 = new Publisher("Some Publisher", "PT123456789", contact1);
        List <Genre> genres2 = new ArrayList<>();
        genres2.add(new Genre ("Fiction"));
        LocalDate publishDate2 = LocalDate.of(2020, 1, 1);
        int numberOfPages2 = 274;
        String chunksOfText2 = " ";

        BibliographicalData bd2 = new BibliographicalData(title2, authors2, language1, publisher2, publishDate2, genres2, numberOfPages2, chunksOfText2);


        // publicationType 1 (pt1)
        MediaFormat mediaFormat1 = MediaFormat.BOOK;
        TypeOfBinding typeOfBinding1 = TypeOfBinding.HARDBACK;
        String details1 = "Special Edition";

        PublicationType pubType1 = new PublicationType(mediaFormat1, typeOfBinding1, details1);

        // publicationType 2 (pt2)
        MediaFormat mediaFormat2 = MediaFormat.BOOK;
        TypeOfBinding typeOfBinding2 = TypeOfBinding.PAPERBACK;
        String details2 = "Nothing to add";

        PublicationType pubType2 = new PublicationType(mediaFormat2, typeOfBinding2, details2);

        // user
        String username = "username";
        Country country = new Country("Portugal");
        LocalDate birtDate = LocalDate.of(1997, 8, 15);
        Address address = new Address("Main Street", "10", "12345", new Country("PORTUGAL"));
        Email email = new Email("test.user", "example.com");
        Contact contact = new Contact("987654321", "351", email, address);
        Profile profile = new Profile(username, country,birtDate);
        UserRole userRole = UserRole.NORMAL_USER;

        User user = new User(username, contact, profile, userRole );

        // publication Status, Edition and code
        PublicationStatus pubStatus = PublicationStatus.FOR_SALE;
        String edition = "1st";
        Code code1 = Code.fromIsbn13("ABC123");
        Code code2 = Code.fromIsbn13("ABC124");

        Publication pub1 = new Publication(bd1, pubType1, user, pubStatus, edition, code1);
        Publication pub2 = new Publication(bd2, pubType2, user, pubStatus, edition, code2);

        PublicationRepository repo = new PublicationRepository(List.of(pub1, pub2));

        List<Publication> result = repo.getAllPublications();

        // Verify size
        assertEquals(2, result.size());

//        // Verify it's the same object
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

        Library library = new Library();
        library.addPublication(publication1);

        List<Publication> resultingList = publicationRepository.getListOfDifferentPublications(library.getAllPublications());

        assertTrue(resultingList.isEmpty());
    }

    @Test //This test is used to test if having an empty library will result in a return of all publications
    void getListOfDifferentPublicationsShouldReturnListWithAllPublicationsInSystem(){

        PublicationRepository publicationRepository = new PublicationRepository(List.of(publication1, publication2, publication3));

        Library library = new Library();

        List<Publication> resultingList = publicationRepository.getListOfDifferentPublications(library.getAllPublications());

        assertEquals(3, resultingList.size());
        assertTrue(resultingList.contains(publication1));
        assertTrue(resultingList.contains(publication2));
        assertTrue(resultingList.contains(publication3));
    }

    @Test //This test is used to test if by having some publications in the library, it returns a list with the ones that are not in the library
    void getAListOfDifferentPublicationsShouldReturnListWithPublicationsNotInLibrary(){

        PublicationRepository publicationRepository = new PublicationRepository(List.of(publication1, publication2, publication3));

        Library library = new Library();
        library.addPublication(publication2);

        List<Publication> resultingList = publicationRepository.getListOfDifferentPublications(library.getAllPublications());

        assertEquals(2, resultingList.size());
        assertTrue(resultingList.contains(publication1));
        assertTrue(resultingList.contains(publication3));

    }

    @Test //This test is used to test immutability for the "getListOfDifferentPublications()"
    void getListOfDifferentPublicationsShouldReturnImmutableList(){

        PublicationRepository publicationRepository = new PublicationRepository(List.of(publication1, publication2, publication3));

        Library library = new Library();
        library.addPublication(publication2);

        List<Publication> resultingList = publicationRepository.getListOfDifferentPublications(library.getAllPublications());

        assertThrows(UnsupportedOperationException.class, () -> resultingList.add(publication2));
    }
}