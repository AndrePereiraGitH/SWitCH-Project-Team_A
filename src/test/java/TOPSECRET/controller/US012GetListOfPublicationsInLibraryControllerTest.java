package TOPSECRET.controller;

import TOPSECRET.domain.*;

import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.Enum.UserRole;

import TOPSECRET.dto.PublicationDetailsDTO_US012;
import TOPSECRET.repository.LibraryRepository;
import org.junit.jupiter.api.Test;


import javax.management.InstanceNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link US012GetListOfPublicationsInLibraryController} class.
 * <p>
 * This test class verifies the controller's behavior when retrieving and converting
 * publications from a user's library to DTOs, including validation and edge cases.
 * </p>
 */
class US012GetListOfPublicationsInLibraryControllerTest {

    // Countries
    Country country1 = new Country("Denmark");
    Country country2 = new Country("Germany");
    Country country4 = new Country("Uganda");

    // Profile and Author
    Profile profileAuthor1 = new Profile("Tove Ditlevsen", country1, LocalDate.of(1917, 12, 14));
    Author author1 = new Author(profileAuthor1, "valid bio1");

    // Contact information
    Address address1 = new Address("R Maria", "12", "156-5", country2);
    Email email1 = new Email("Maria1", "gmail.com");
    Contact contact1 = new Contact("915945695", "+351", email1, address1);

    // BibliographicalData
    BibliographicalData bibliographicalData1 = new BibliographicalData(
            "Childhood, Youth, Dependency: The Copenhagen Trilogy",
            List.of(author1),
            Locale.ENGLISH,
            LocalDate.of(2020, 1, 1),
            List.of(new Genre("Fiction")),
            "Some text"
    );

    // Users
    Profile profile2 = new Profile("Test Profile", country4, LocalDate.of(1997, 8, 15));
    User user1 = new User("coolGuy", contact1, profile2, UserRole.ADMIN);
    User user2 = new User("niceGuy", contact1, profile2, UserRole.ADMIN);
    User user3 = new User("emptyGuy", contact1, profile2, UserRole.ADMIN);

    // Edition components
    Code code = Code.fromIsbn13("9783161484123");
    Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
    PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
    PublicationType pubType = new PublicationType(TypeOfBinding.CASE_BINDING, "Some details");
    Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
    Edition edition1 = new Edition("1st", code, physicalAttributes, pubType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));

    // PublicationSamples
    PublicationSample publication1 = new PublicationSample(bibliographicalData1, user1, PublicationStatus.READING, edition1, Condition.POOR);
    PublicationSample publication2 = new PublicationSample(bibliographicalData1, user1, PublicationStatus.READING, edition1, Condition.LIKE_NEW);

    // Libraries
    Library lib1 = new Library(List.of(publication1, publication2), "mylib1", user1);
    Library lib2 = new Library(List.of(publication1), "mylib2", user2);
    Library emptyLibrary = new Library(List.of(), "emptyLib", user3);

    // Repository with test data
    LibraryRepository userLib = new LibraryRepository(
            Map.of(
                    user1.getUserName(), lib1,
                    user2.getUserName(), lib2,
                    user3.getUserName(), emptyLibrary
            )
    );

    // System Under Test
    US012GetListOfPublicationsInLibraryController ctrl = new US012GetListOfPublicationsInLibraryController(userLib);


    /**
     * Tests that an {@link IllegalArgumentException} is thrown when the username is empty.
     * <p>
     * Validates that the controller properly rejects empty username inputs.
     * </p>
     */
    @Test
    void shouldThrowExceptionWhenUserNameIsEmpty() {
        // Arrange
        String emptyUser = "";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> ctrl.getListOfPublicationsInLibraryDTO(emptyUser));
    }

    /**
     * Tests that an empty list is returned when the user's library contains no publications.
     * <p>
     * Verifies that:
     * <ul>
     *   <li>The returned list is not null</li>
     *   <li>The returned list is empty</li>
     * </ul>
     * </p>
     *
     * @throws InstanceNotFoundException if the user's library cannot be found
     */
    @Test
    void ensureListOfPublicationsInLibraryIsEmptyWhenLibraryReturnsEmptyAList() throws InstanceNotFoundException {
        // Arrange
        String username = user3.getUserName();

        // Act
        List<PublicationDetailsDTO_US012> result = ctrl.getListOfPublicationsInLibraryDTO(username);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Tests that the controller correctly retrieves and converts publications to DTOs.
     * <p>
     * Verifies that:
     * <ul>
     *   <li>The returned list is not null</li>
     *   <li>The list contains the expected number of publications</li>
     *   <li>Each DTO contains the correct title, code, publication type, and author names</li>
     *   <li>All fields are properly extracted and converted from domain objects</li>
     * </ul>
     * </p>
     *
     * @throws InstanceNotFoundException if the user's library cannot be found
     */
    @Test
    void ensureListOfPublicationsInLibraryReturnsDTOs() throws InstanceNotFoundException {
        // Arrange
        String username = user1.getUserName();

        String expectedTitle1 = publication1.getBibliographicalData().getTitle();
        String expectedCode1 = publication1.getEdition().getCode().toString();
        String expectedType1 = publication1.getEdition().getPublicationType().getTypeOfBinding().toString();
        List<String> expectedAuthors1 = publication1.getBibliographicalData().getAuthors()
                .stream()
                .map(author -> author.getProfile().getName())
                .toList();

        String expectedTitle2 = publication2.getBibliographicalData().getTitle();
        String expectedCode2 = publication2.getEdition().getCode().toString();
        String expectedType2 = publication2.getEdition().getPublicationType().getTypeOfBinding().toString();
        List<String> expectedAuthors2 = publication2.getBibliographicalData().getAuthors()
                .stream()
                .map(author -> author.getProfile().getName())
                .toList();

        // Act
        List<PublicationDetailsDTO_US012> result = ctrl.getListOfPublicationsInLibraryDTO(username);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        // Verify first DTO
        PublicationDetailsDTO_US012 dto1 = result.get(0);
        assertEquals(expectedTitle1, dto1.pubTitle);
        assertEquals(expectedCode1, dto1.code);
        assertEquals(expectedType1, dto1.publicationType);
        assertIterableEquals(expectedAuthors1, dto1.authorName);

        // Verify second DTO
        PublicationDetailsDTO_US012 dto2 = result.get(1);
        assertEquals(expectedTitle2, dto2.pubTitle);
        assertEquals(expectedCode2, dto2.code);
        assertEquals(expectedType2, dto2.publicationType);
        assertIterableEquals(expectedAuthors2, dto2.authorName);
    }
}