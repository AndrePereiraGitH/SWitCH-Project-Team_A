//package TOPSECRET.dto;
//
//import TOPSECRET.domain.*;
//import TOPSECRET.domain.Enum.*;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Locale;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Unit tests for the {@link PublicationDetailsDTO_US012} class.
// * <p>
// * This test class verifies the behavior of DTO construction and conversion
// * of PublicationSample domain objects to PublicationDetailsDTO_US012 objects.
// * </p>
// */
//class PublicationDetailsDTODependencies_US012Test {
//
//    // Countries
//    Country country1 = new Country("Denmark");
//    Country country2 = new Country("Germany");
//    Country country4 = new Country("Uganda");
//
//    // Profile and Author
//    Profile profileAuthor1 = new Profile("Tove Ditlevsen", country1, LocalDate.of(1917, 12, 14));
//    AuthorFactory factory = new AuthorFactory();
//    Author author1 = factory.createWithExistingProfile(profileAuthor1, "valid bio1");
//
//    // Contact information
//    Address address1 = new Address("R Maria", "12", "156-5", country2);
//    Email email1 = new Email("Maria1", "gmail.com");
//    Contact contact1 = new Contact("915945695", "+351", email1, address1);
//
//    // BibliographicalData
//    BibliographicalData bibliographicalData1 = new BibliographicalData(
//            "Childhood, Youth, Dependency: The Copenhagen Trilogy",
//            List.of(author1),
//            Locale.ENGLISH,
//            LocalDate.of(2020, 1, 1),
//            List.of(new Genre("Fiction")),
//            "Some text"
//    );
//
//    // User
//    Profile profile2 = new Profile("Test Profile", country4, LocalDate.of(1997, 8, 15));
//    User user1 = new User("coolGuy", contact1, profile2, UserRole.ADMIN);
//
//    // Codes
//    Code code1 = Code.fromIsbn13("1234567890111");
//    Code code2 = Code.fromIsbn13("1234567899111");
//
//    // Edition components
//    Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
//    PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);
//    PublicationType publicationType = new PublicationType(MediaFormat.BOOK, TypeOfBinding.CASE_BINDING, "Some details");
//    Publisher publisher = new Publisher("Porto Editora", "PT987654321", null);
//
//    // Editions
//    EditionFactory editionFactory = new EditionFactory();
//    Edition edition1 = editionFactory.create("1st", code1, physicalAttributes, publicationType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));
//    Edition edition2 = editionFactory.create("1st", code2, physicalAttributes, publicationType, publisher, Locale.ENGLISH, LocalDate.of(2020, 1, 1));
//
//    // PublicationSamples
//    PublicationSample publication1 = new PublicationSample(bibliographicalData1, user1, PublicationStatus.READING, edition1, Condition.POOR);
//    PublicationSample publication2 = new PublicationSample(bibliographicalData1, user1, PublicationStatus.READING, edition2, Condition.LIKE_NEW);
//
//
//    /**
//     * Tests that the DTO constructor correctly sets all fields.
//     * <p>
//     * Verifies that when a PublicationDetailsDTO_US012 is created with
//     * specific values, all fields are properly initialized.
//     * </p>
//     */
//    @Test
//    void ensureConstructorShouldSetFieldsCorrectly() {
//        // Arrange
//        String expectedTitle = "Os Maias";
//        List<String> expectedAuthors = List.of("Eça de Queirós");
//        String expectedCode = "978-1234567890";
//        String expectedType = "BOOK";
//
//        // Act
//        PublicationDetailsDTO_US012 dto = new PublicationDetailsDTO_US012(expectedTitle, expectedAuthors, expectedCode, expectedType);
//
//        // Assert
//        assertEquals(expectedTitle, dto.pubTitle);
//        assertEquals(expectedAuthors, dto.authorName);
//        assertEquals(expectedCode, dto.code);
//        assertEquals(expectedType, dto.publicationType);
//    }
//
//    /**
//     * Tests that a list of PublicationSample objects is correctly mapped to DTOs.
//     * <p>
//     * Verifies that:
//     * <ul>
//     *   <li>The conversion method returns a non-null list</li>
//     *   <li>The resulting list has the correct size</li>
//     *   <li>Each DTO contains the correct title, code, publication type, and author names</li>
//     *   <li>All fields are properly extracted from the domain objects</li>
//     * </ul>
//     * </p>
//     */
//    @Test
//    void getListOfPublicationsDTOShouldMapListOfObjects() {
//        // Arrange
//        List<PublicationSample> publicationList = List.of(publication1, publication2);
//
//        String expectedTitle1 = publication1.getBibliographicalData().getTitle();
//        String expectedCode1 = publication1.getEdition().getCode().toString();
//        String expectedType1 = publication1.getEdition().getPublicationType().getMediaFormat().toString();
//        List<String> expectedAuthors1 = publication1.getBibliographicalData().getAuthors()
//                .stream()
//                .map(author -> author.getProfile().getName())
//                .toList();
//
//        String expectedTitle2 = publication2.getBibliographicalData().getTitle();
//        String expectedCode2 = publication2.getEdition().getCode().toString();
//        String expectedType2 = publication2.getEdition().getPublicationType().getMediaFormat().toString();
//        List<String> expectedAuthors2 = publication2.getBibliographicalData().getAuthors()
//                .stream()
//                .map(author -> author.getProfile().getName())
//                .toList();
//
//        // Act
//        List<PublicationDetailsDTO_US012> resultList = PublicationDetailsDTO_US012.toDTOs(publicationList);
//
//        // Assert
//        assertNotNull(resultList);
//        assertEquals(2, resultList.size());
//
//        // Verify first DTO
//        PublicationDetailsDTO_US012 dto1 = resultList.get(0);
//        assertEquals(expectedTitle1, dto1.pubTitle);
//        assertEquals(expectedCode1, dto1.code);
//        assertEquals(expectedType1, dto1.publicationType);
//        assertIterableEquals(expectedAuthors1, dto1.authorName);
//
//        // Verify second DTO
//        PublicationDetailsDTO_US012 dto2 = resultList.get(1);
//        assertEquals(expectedTitle2, dto2.pubTitle);
//        assertEquals(expectedCode2, dto2.code);
//        assertEquals(expectedType2, dto2.publicationType);
//        assertIterableEquals(expectedAuthors2, dto2.authorName);
//    }
//
//    /**
//     * Tests that an empty list of publications returns an empty DTO list.
//     * <p>
//     * Verifies that the conversion method handles empty input gracefully
//     * and returns a non-null, empty list rather than null or throwing an exception.
//     * </p>
//     */
//    @Test
//    void getListOfPublicationsDTOEmptyListShouldReturnEmptyList() {
//        // Arrange
//        List<PublicationSample> emptyList = List.of();
//
//        // Act
//        List<PublicationDetailsDTO_US012> result = PublicationDetailsDTO_US012.toDTOs(emptyList);
//
//        // Assert
//        assertNotNull(result);
//        assertTrue(result.isEmpty());
//    }
//}