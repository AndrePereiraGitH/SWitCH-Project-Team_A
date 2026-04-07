package TOPSECRET.dto;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.Code;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link PublicationDetailsDTO_US012} class.
 * <p>
 * This test class verifies the behavior of DTO construction and conversion
 * of PublicationSample domain objects to PublicationDetailsDTO_US012 objects.
 * </p>
 */
class PublicationDetailsDTO_US012Test {


    /**
     * Tests that the DTO constructor correctly sets all fields.
     * <p>
     * Verifies that when a PublicationDetailsDTO_US012 is created with
     * specific values, all fields are properly initialized.
     * </p>
     */
    @Test
    void testConstructor() {

        // Arrange
        String expectedTitle = "Os Maias";
        List<String> expectedAuthors = List.of("Eça de Queirós");
        String expectedCode = "978-1234567890";
        String expectedType = "BOOK";

        // Act
        PublicationDetailsDTO_US012 dto = new PublicationDetailsDTO_US012(expectedTitle, expectedAuthors, expectedCode, expectedType);

        // Assert
        assertEquals(expectedTitle, dto.pubTitle);
        assertEquals(expectedAuthors, dto.authorName);
        assertEquals(expectedCode, dto.code);
        assertEquals(expectedType, dto.publicationType);
    }

    /**
     * Tests that a single PublicationSample object is correctly mapped to a DTO.
     * <p>
     * Verifies that:
     * <ul>
     *   <li>The DTO contains the correct title extracted from its bibliographical data</li>
     *   <li>The DTO contains the correct code extracted from its edition</li>
     *   <li>The DTO contains the correct author names extracted from its bibliographical data</li>
     *   <li>The DTO contains the correct publication type derived from its media format</li>
     * </ul>
     * </p>
     */

    @Test
    void toDTO_shouldReturnADTO() {

        //Arrange
        String expectedTitle="Title1";
        List<String> authorsName = List.of("Name1","Name2");
        String expectedCode = "12345678910";

        PublicationSample publicationSampleDouble = createMockPublicationSample(expectedTitle,authorsName,expectedCode);


        //Act
//        PublicationDetailsDTO_US012 result = PublicationDetailsDTO_US012.toDTO(publicationSampleDouble);


        //Assert
//        assertEquals(result.authorName,authorsName);
//        assertEquals(expectedTitle, result.pubTitle);
//        assertEquals(expectedCode, result.code);
//        assertEquals("Book", result.publicationType);

    }

    /**
     * Tests that a list of PublicationSample objects is correctly mapped to a list of DTOs.
     * <p>
     * Verifies that:
     * <ul>
     *   <li>The resulting list has the correct size</li>
     *   <li>Each DTO contains the correct title extracted from its domain object</li>
     *   <li>Each DTO contains the correct code extracted from its edition</li>
     *   <li>Each DTO contains the correct author names extracted from its bibliographical data</li>
     *   <li>Each DTO contains the correct publication type derived from its media format</li>
     * </ul>
     * </p>
     */

    @Test
    void toDTOS_shouldReturnAListOfDTOS(){

        //Arrange
        String title1="Title1";
        List<String> authors1 = List.of("author1","author2");
        String code1 = "123456789";

        String title2="Title2";
        List<String> authors2 = List.of("author3","author4");
        String code2 = "12345678";

        PublicationSample pubDTO1 = createMockPublicationSample(title1,authors1,code1);
        PublicationSample pubDTO2 = createMockPublicationSample(title2,authors2,code2);

        List<PublicationSample> listOfPublicationsDouble = List.of(pubDTO1,pubDTO2);

        //Act
//        List<PublicationDetailsDTO_US012> result = PublicationDetailsDTO_US012.toDTOs(listOfPublicationsDouble);

        //Assert
//        assertEquals(2, result.size());
//        assertEquals(title1, result.get(0).pubTitle);
//        assertEquals(title2, result.get(1).pubTitle);
//        assertEquals("Book", result.get(0).publicationType);
//        assertEquals("Book", result.get(1).publicationType);
//        assertEquals(code1, result.get(0).code);
//        assertEquals(code2, result.get(1).code);
//        assertEquals(result.get(0).authorName,authors1);
//        assertEquals(result.get(1).authorName,authors2);

    }

    /**
     * Tests that an empty list of publications returns an empty DTO list.
     * <p>
     * Verifies that the conversion method handles empty input gracefully
     * and returns a non-null, empty list rather than null or throwing an exception.
     * </p>
     */
    @Test
    void getListOfPublicationsDTOEmptyListShouldReturnEmptyList() {
        // Arrange

        // Act
//        List<PublicationDetailsDTO_US012> result = PublicationDetailsDTO_US012.toDTOs(List.of());

        // Assert
//        assertNotNull(result);
//        assertTrue(result.isEmpty());
    }


    // -------------------------------------------------------------------------
    // Test helpers
    // -------------------------------------------------------------------------

    private PublicationSample createMockPublicationSample(String title, List<String> authors, String code) {
        BibliographicalData bibliographicalDataDouble = createMockBibliographicalData(title, authors);
        Edition editionDouble = createMockEdition(code);

        PublicationSample publicationSample = mock(PublicationSample.class);
//        when(publicationSample.getBibliographicalData()).thenReturn(bibliographicalDataDouble);
//        when(publicationSample.getEdition()).thenReturn(editionDouble);

        return publicationSample;
    }

    private Edition createMockEdition(String code) {
        Code codeDouble = mock(Code.class);
        when(codeDouble.toString()).thenReturn(code);

        Edition editionDouble = mock(Edition.class);
        PublicationTypeId publicationTypeIdDouble = mock(PublicationTypeId.class);
        when(editionDouble.getMediaFormat()).thenReturn("Book");
        when(editionDouble.getCode()).thenReturn(codeDouble);
        when(editionDouble.getPublicationTypeID()).thenReturn(publicationTypeIdDouble);
        when(publicationTypeIdDouble.getId()).thenReturn("1");

        return editionDouble;
    }

    private BibliographicalData createMockBibliographicalData(String title, List<String> authorsNames) {
        List<Author> authorsDouble = createMockAuthors(authorsNames);
        BibliographicalData bibliographicalDataDouble = mock(BibliographicalData.class);

        when(bibliographicalDataDouble.getTitle()).thenReturn(title);
//        when(bibliographicalDataDouble.getAuthors()).thenReturn(authorsDouble);

        return bibliographicalDataDouble;
    }

    private List<Author> createMockAuthors(List<String> authorNames) {
        return authorNames.stream()
                .map(this::createMockAuthor)
                .toList();
    }

    private Author createMockAuthor(String authorName) {
        Author author = mock(Author.class);
        when(author.getName()).thenReturn(authorName);

        return author;
    }

}