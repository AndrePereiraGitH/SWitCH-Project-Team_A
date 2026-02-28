package TOPSECRET.dto;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.Enum.UserRole;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class LibraryPublicationMapperTest_US015 {

    // BibliographicalData
    Country country1 = new Country("Denmark");
    Country country2 = new Country("Germany");
    AuthorFactory authorFactory = new AuthorFactory();
    EditionFactory editionFactory = new EditionFactory();




    Profile profileAuthor1 = new Profile("Tove Ditlevsen", country1, LocalDate.of(1917, 12, 14));
    Author author1 = authorFactory.createAuthorWithExistingProfile(profileAuthor1, "valid bio1");
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

    // User
    Country country4 = new Country("Uganda");
    Profile profile2 = new Profile(
            "Test Profile",
            country4,
            LocalDate.of(1997, 8, 15)
    );


    UserRole userRole1 = UserRole.ADMIN;
    User user1 = new User("coolGuy", contact1, profile2, userRole1);

    //PublicationStatus
    PublicationStatus pubStatus1 = PublicationStatus.READING;

    //Edition
//    String edition1 = "2nd Edition";
    // Edition
    Code code = Code.fromIsbn13 ("9783161484123");
    Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
    PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
    PublicationType pubType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.CASE_BINDING, "Some details");
    Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
    Edition edition1= editionFactory.create("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

    //Code
    Code code1 = Code.fromIsbn10("1234567890");
    Code code2 = Code.fromIsbn10("1234567899");

    //PublicationType
    PublicationType publicationType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "Paperback");


    Publication publication1 = new Publication(bibliographicalData1, publicationType, user1,
            pubStatus1, edition1, code1);

    Publication publication2= new Publication(bibliographicalData1, publicationType, user1,
            pubStatus1, edition1, code2);


    // Test 1 - toDTO(Publication) --------------------------------------------------------------------------------------
    @Test
    void ensureSinglePublicationIsMappedCorrectly() {
        // Arrange
        Publication pub = publication1;

        // Act
        LibraryPublicationDTO_US015 dto = LibraryPublicationMapper_US015.toDTO(pub);

        // Assert
        assertEquals("Childhood, Youth, Dependency: The Copenhagen Trilogy", dto.pubTitle);
        assertEquals(List.of("Tove Ditlevsen"), dto.authorName);
        assertEquals("1234567890", dto.code);
        assertEquals("Paperback", dto.publicationType);
    }


    // Test 2 - toDTO(List<Publication>) ------------------------------------------------------------------------------
    @Test void ensureListOfPublicationsIsMappedCorrectly() {
        // Arrange
        List<Publication> publications = List.of(publication1, publication2);

        // Act
        List<LibraryPublicationDTO_US015> dtos = LibraryPublicationMapper_US015.toDTO(publications);

        // Assert
        assertEquals(2, dtos.size());
        assertEquals("1234567890", dtos.get(0).code);
        assertEquals("1234567899", dtos.get(1).code);
    }


    // Test 3 - Empty list ---------------------------------------------------------------------------------------------
    @Test void ensureEmptyListReturnsEmptyDTOList() {
        // Arrange
        List<Publication> emptyList = List.of();

        // Act
        List<LibraryPublicationDTO_US015> dtos = LibraryPublicationMapper_US015.toDTO(emptyList);

        // Assert
        assertTrue(dtos.isEmpty());
    }
}