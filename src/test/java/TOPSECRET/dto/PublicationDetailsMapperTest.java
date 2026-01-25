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

class PublicationDetailsMapperTest {

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
    String edition1 = "2nd Edition";

    //Code
    Code code1 = Code.fromIsbn10("1234567890");
    Code code2 = Code.fromIsbn10("1234567899");

    //PublicationType
    PublicationType publicationType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "Paperback");


    Publication publication1 = new Publication(bibliographicalData1, publicationType, user1,
            pubStatus1, edition1, code1);

    Publication publication2= new Publication(bibliographicalData1, publicationType, user1,
            pubStatus1, edition1, code2);


    @Test
    void toDTOShouldMapAllFieldsCorrectly() {
        // Arrange
        String expectedTitle = bibliographicalData1.getTitle();
        String expectedIsbn = code1.toString();
        String expectedType = publicationType.getMediaFormat().toString();

        List<String> expectedAuthors = List.of(profileAuthor1.getName());

        // Act
        PublicationDetailsDTO result = PublicationDetailsMapper.toDTO(publication1);

        // Assert
        assertNotNull(result);

        assertEquals(expectedTitle, result.pubTitle);
        assertEquals(expectedIsbn, result.code);
        assertEquals(expectedType, result.publicationType);

        assertIterableEquals(expectedAuthors, result.authorName);
    }


    @Test
    void getListOfPublicationsDTOShouldMapListOfObjects() {
        // Arrange
        List<Publication> publicationList = List.of(publication1, publication2);


        String expectedTitle = bibliographicalData1.getTitle();
        String expectedType = publicationType.getMediaFormat().toString();
        List<String> expectedAuthors = List.of(profileAuthor1.getName());

        // Act
        List<PublicationDetailsDTO> resultList = PublicationDetailsMapper.toDTOs(publicationList);

        // Assert
        assertNotNull(resultList);
        assertEquals(2, resultList.size());


        PublicationDetailsDTO dto1 = resultList.get(0);

        assertEquals(expectedTitle, dto1.pubTitle);
        assertEquals(code1.toString(), dto1.code);
        assertEquals(expectedType, dto1.publicationType);
        assertIterableEquals(expectedAuthors, dto1.authorName);


        PublicationDetailsDTO dto2 = resultList.get(1);

        assertEquals(expectedTitle, dto2.pubTitle);
        assertEquals(code2.toString(), dto2.code);
        assertEquals(expectedType, dto2.publicationType);
        assertIterableEquals(expectedAuthors, dto2.authorName);
    }

    @Test
    void getListOfPublicationsDTOEmptyListShouldReturnEmptyList() {
        // Arrange
        List<Publication> emptyList = List.of();

        // Act
        List<PublicationDetailsDTO> result = PublicationDetailsMapper.toDTOs(emptyList);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}