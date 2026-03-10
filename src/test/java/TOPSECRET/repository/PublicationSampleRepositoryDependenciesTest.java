package TOPSECRET.repository;


import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import TOPSECRET.dto.PublicationSampleDTO_US016;
import TOPSECRET.dto.PublicationSampleDTO_US018;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;


class PublicationSampleRepositoryDependenciesTest {

    // BibliographicalData
    CountryFactory countryFactory = new CountryFactory();

    Country country1 = countryFactory.create("Denmark");
    Country country2 = countryFactory.create("Germany");


    Profile profileAuthor1 = new Profile("Tove Ditlevsen", country1, LocalDate.of(1917, 12, 14));

    AuthorFactory authorFactory = new AuthorFactory();
    Author author1 = authorFactory.createWithExistingProfile(profileAuthor1, "valid bio1");
    Address address1 = new Address("R Maria", "12", "156-5", country2);
    Email email1 = new Email("Maria1", "gmail.com");
    Contact contact1 = new Contact("915945695", "+351", email1, address1);
//    Publisher publisher1 = new Publisher("Some Publisher", "PT123456789", contact1);
    BibliographicalData bibliographicalData1 = new BibliographicalData(
            "Childhood, Youth, Dependency: The Copenhagen Trilogy",
            List.of(author1),
            Locale.ENGLISH,
//            publisher1,
            LocalDate.of(2020, 1, 1),
            List.of(new Genre ("Fiction")),
//            200,
            "Some text");
    BibliographicalData bibliographicalData2 = new BibliographicalData(
            "Childhood, Youth, Dependency: The Copenhagen Trilogy",
            List.of(author1),
            Locale.ENGLISH,
//            publisher1,
            LocalDate.of(2020, 1, 1),
            List.of(new Genre ("Fiction")),
//            200,
            "Some text");
    BibliographicalData bibliographicalData3 = new BibliographicalData(
            "Childhood, Youth, Dependency: The Copenhagen Trilogy",
            List.of(author1),
            Locale.ENGLISH,
//            publisher1,
            LocalDate.of(2020, 1, 1),
            List.of(new Genre ("Fiction")),
//            200,
            "Some text");

    // User
    Country country4 = countryFactory.create("Uganda");
    Profile profile2 = new Profile(
            "Test Profile",
            country4,
            LocalDate.of(1997, 8, 15)
    );
    UserRole userRole1 = UserRole.ADMIN;
    User user1 = UserFactory.create("coolGuy", contact1, profile2, userRole1);
    User user2 = UserFactory.create("coolGuy2", contact1, profile2, userRole1);
    User user3 = UserFactory.create("coolGuy3", contact1, profile2, userRole1);

    User userTest = UserFactory.create("userTest", contact1, profile2, userRole1);

    //PublicationStatus
    PublicationStatus pubStatus1 = PublicationStatus.READING;
    PublicationStatus pubStatus2 = PublicationStatus.READ;
    PublicationStatus pubStatus3 = PublicationStatus.READING;


    //Edition
//    String edition1 = "2nd Edition";
//    String edition2 = "1st Edition";
//    String edition3 = "3rd Edition";
    Code code = Code.fromIsbn13 ("9783161484123");
    Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
    PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
    PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.CASE_BINDING, "Some details");

    PublisherFactory publisherFactory = new PublisherFactory();
    Publisher publisher = publisherFactory.create("Porto Editora", "PT987654321", null);

    EditionFactory editionFactory = new EditionFactory();
    Edition edition1= editionFactory.create("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));
    Edition edition2= editionFactory.create("2st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));
    Edition edition3= editionFactory.create("3st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

    //Code
    Code code1 = Code.fromIsbn10("1234567890");
    Code code2 = Code.fromIsbn10("1234567891");
    Code code3 = Code.fromIsbn10("1234567892");

    //Condition
    Condition condition1 = Condition.LIKE_NEW;
    Condition condition2 = Condition.NEW;
    Condition condition3 = Condition.ACCEPTABLE;

    //PublicationType
    PublicationType publicationType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "Paperback");

    //Publications
    PublicationSample publication1 = new PublicationSample(bibliographicalData1, user1,
            pubStatus1, edition1, condition1);
    PublicationSample publication2 = new PublicationSample(bibliographicalData2, user2,
            pubStatus2, edition2, condition2);
    PublicationSample publication3 = new PublicationSample(bibliographicalData3, user3,
            pubStatus3, edition3, condition3);

    // Happy path

    @Test
    void ensureANewPublicationIsCreatedAndAdded() throws InstanceAlreadyExistsException {
        //Arrange
        PublicationSampleFactory factory = new PublicationSampleFactory();
        PublicationSampleRepository pr1 = new PublicationSampleRepository(factory);

        //Act
        PublicationSample realResult = pr1.createAndAdd(bibliographicalData1, user1,
                pubStatus1, edition1, condition1);

        // Assert
        assertNotNull(realResult);
        assertEquals(1, pr1.getAllPublications().size());
        assertTrue(pr1.getAllPublications().contains(realResult));
    }

    @Test
    void ensureThatADuplicatePublicationIsNotAdded() throws InstanceAlreadyExistsException {
        //Arrange
        PublicationSampleFactory factory = new PublicationSampleFactory();

        PublicationSampleRepository pr1 = new PublicationSampleRepository(factory);

        //Act
        PublicationSample realResult=pr1.createAndAdd(bibliographicalData1, user1,
                pubStatus1, edition1, condition1);

        // Assert
        assertNotNull(realResult);
        assertEquals(1, pr1.getAllPublications().size());
        assertTrue(pr1.getAllPublications().contains(realResult));

    }

    @Test
    void ifPublicationIsExistentShouldReturnTrue() {
        //Arrange
        PublicationSampleFactory factory = new PublicationSampleFactory();
        PublicationSampleRepository pr1 = new PublicationSampleRepository(factory);
        pr1.addPublication(publication1);

        //Act
        boolean realResult = pr1.isExistent(publication1);

        // Assert
        assertTrue(realResult);
    }


    @Test
    void ifPublicationIsNotExistentShouldReturnFalse(){

        //Arrange
        PublicationSampleFactory factory = new PublicationSampleFactory();
        PublicationSampleRepository pr1 = new PublicationSampleRepository(factory);

        //Act
        boolean realResult = pr1.isExistent(publication1);

        //Assert
        assertFalse(realResult);

    }


    @Test
    void ensureConstructorCopiesListNotOriginalReferences(){
        // Arrange
        PublicationSampleFactory factory = new PublicationSampleFactory();
        PublicationSampleRepository pr1 = new PublicationSampleRepository(factory);

        //Act
        boolean realResult=pr1.isExistent(publication1);

        //Assert
        assertFalse(realResult);

    }

    @Test
    void getAllPublicationsReturnsAll() {
        // Arrange
        PublicationSample pub1 = publication1;
        PublicationSample pub2 = publication2;
        PublicationSampleFactory factory = new PublicationSampleFactory();

        PublicationSampleRepository repo = new PublicationSampleRepository(factory);
        repo.addPublication(pub1);
        repo.addPublication(pub2);

        // Act
        List<PublicationSample> result = repo.getAllPublications();

        // Assert
        assertEquals(2, result.size());
        assertSame(pub1, result.get(0));
        assertSame(pub2, result.get(1));
    }

    @Test //This test is used to test immutability for the "getAllPublications()"
    void getAllPublicationsShouldReturnImmutableList(){

        PublicationSampleFactory factory = new PublicationSampleFactory();

        PublicationSampleRepository publicationRepository = new PublicationSampleRepository(factory);

        List<PublicationSample> resultingList = publicationRepository.getAllPublications();

        assertThrows(UnsupportedOperationException.class, () -> resultingList.add(publication1));
    }

    /*
    @Test //This test is used to test the return of publications not present in the library
    void getListOfDifferentPublicationsShouldReturnListOfPublicationsNotPresentInLibrary(){

        PublicationRepository publicationRepository = new PublicationRepository(List.of(publication1));

        Library library = new Library(new ArrayList<>(), userTest.getUserName() + "'s Library", userTest);
        library.addPublication(publication1);

        List<Publication> resultingList = publicationRepository.getListOfDifferentPublications(library.getAllPublications());

        assertTrue(resultingList.isEmpty());
    }

     */

    /*
    @Test //This test is used to test if having an empty library will result in a return of all publications
    void getListOfDifferentPublicationsShouldReturnListWithAllPublicationsInSystem(){

        PublicationRepository publicationRepository = new PublicationRepository(List.of(publication1, publication2, publication3));

        Library library = new Library(new ArrayList<>(), userTest.getUserName() + "'s Library", userTest);

        List<PublicationSample> resultingList = publicationRepository.getListOfDifferentPublications(library.getAllPublications());

        assertEquals(3, resultingList.size());
        assertTrue(resultingList.contains(publication1));
        assertTrue(resultingList.contains(publication2));
        assertTrue(resultingList.contains(publication3));
    }

     */

    /*
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

     */

    /*
    @Test //This test is used to test immutability for the "getListOfDifferentPublications()"
    void getListOfDifferentPublicationsShouldReturnImmutableList(){

        PublicationRepository publicationRepository = new PublicationRepository(List.of(publication1, publication2, publication3));

        Library library = new Library(new ArrayList<>(), userTest.getUserName() + "'s Library", userTest);
        library.addPublication(publication2);

        List<Publication> resultingList = publicationRepository.getListOfDifferentPublications(library.getAllPublications());

        assertThrows(UnsupportedOperationException.class, () -> resultingList.add(publication2));
    }

     */

    /**
     * US016
     */
    @Test
    void getPublicationsByDTO_shouldReturn_listOfPublicationSampleDTO() {

        // Arrange
        PublicationSampleFactory factory = new PublicationSampleFactory();

        PublicationSampleRepository pr1 = new PublicationSampleRepository(List.of(publication1), factory);

        List<PublicationSampleDTO_US016> pubsDto = new ArrayList<>();
        pubsDto.add(PublicationSampleDTO_US016.toDTO(publication1));

        // act
        List<PublicationSample> result = pr1.getPublicationsByDTO(pubsDto);

        // assert
        assertEquals(1, result.size());
    }

    /**
     * US016
     */
    @Test
    void getPublicationsByDTO_shouldReturn_listOfPublicationSampleDTO_andPublicationOwner_shouldMatchDtoOwner() {

        // Arrange
        PublicationSampleFactory factory = new PublicationSampleFactory();

        PublicationSampleRepository pr1 = new PublicationSampleRepository(List.of(publication1), factory);

        List<PublicationSampleDTO_US016> pubsDto = new ArrayList<>();
        pubsDto.add(PublicationSampleDTO_US016.toDTO(publication1));

        // act
        List<PublicationSample> result = pr1.getPublicationsByDTO(pubsDto);

        // assert
        assertEquals(publication1.getOwner().getUserName(), result.get(0).getOwner().getUserName());
    }

    @Test
    void getPublicationsByDTO_shouldThrows_ifPublicationsDto_is_Empty() {

        // Arrange
        PublicationSampleFactory factory = new PublicationSampleFactory();

        PublicationSampleRepository pr1 = new PublicationSampleRepository(factory);

        List<PublicationSampleDTO_US016> pubsDto = new ArrayList<>();
       // pubsDto.add(PublicationSampleDTO_US016.toDTO(publication1));

        // act + assert
        assertThrows(IllegalArgumentException.class, () ->pr1.getPublicationsByDTO(pubsDto));
    }


// -------------- US018

    /**
     * US018
     * Ensures that PublicationSampleDtoToDomain converts
     * a valid DTO into the correct PublicationSample.
     */
    @Test
    void publicationSampleDtoToDomain_shouldReturnCorrectPublicationSample_US018() {

        // Arrange
        PublicationSampleFactory factory = new PublicationSampleFactory();

        PublicationSampleRepository repo = new PublicationSampleRepository(factory);

        PublicationSampleDTO_US018 dto =
                new PublicationSampleDTO_US018(
                        publication1.getBibliographicalData().getTitle(),
                        publication1.getEdition().getEditionNumber(),
                        publication1.getOwner().getUserName()
                );

        LibraryFactory libraryFactory = new LibraryFactory();

        Library library = libraryFactory.create("TestLibrary", publication1.getOwner());
        library.addPublication(publication1);

        // Act
        PublicationSample result =
                repo.PublicationSampleDtoToDomain(library, dto);

        // Assert
        assertEquals(publication1, result);
    }

    @Test
    void publicationSampleDtoToDomain_shouldThrowException_whenLibraryIsNull_US018() {

        // ARRANGE
        PublicationSampleFactory factory = new PublicationSampleFactory();
        PublicationSampleRepository repo = new PublicationSampleRepository(new ArrayList<>(), factory);

        PublicationSampleDTO_US018 dto =
                new PublicationSampleDTO_US018("Title", "1", "user");

        // ACT + ARRANGE
        assertThrows(IllegalArgumentException.class, () ->
                repo.PublicationSampleDtoToDomain(null, dto)
        );
    }

    @Test
    void publicationSampleDtoToDomain_shouldThrowException_whenDtoIsNull_US018() {

        // ARRANGE
        PublicationSampleFactory factory = new PublicationSampleFactory();
        PublicationSampleRepository repo = new PublicationSampleRepository(new ArrayList<>(), factory);

        LibraryFactory libraryFactory = new LibraryFactory();
        Library library = libraryFactory.create("TestLibrary", publication1.getOwner());

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                repo.PublicationSampleDtoToDomain(library, null)
        );
    }

    @Test
    void publicationSampleDtoToDomain_shouldThrowException_whenPublicationNotFound_US018() {

        // ARRANGE
        List<PublicationSample> list = new ArrayList<>();
        list.add(publication1);

        PublicationSampleFactory factory = new PublicationSampleFactory();

        PublicationSampleRepository repo = new PublicationSampleRepository(list, factory);

        LibraryFactory libraryFactory = new LibraryFactory();
        Library library = libraryFactory.create("TestLibrary", publication1.getOwner());

        PublicationSampleDTO_US018 dto =
                new PublicationSampleDTO_US018(
                        publication1.getBibliographicalData().getTitle(),
                        publication1.getEdition().getEditionNumber(),
                        publication1.getOwner().getUserName()
                );

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                repo.PublicationSampleDtoToDomain(library, dto)
        );
    }


}