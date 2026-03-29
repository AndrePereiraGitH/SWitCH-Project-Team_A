package TOPSECRET.controller;

import TOPSECRET.domain.*;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;

import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.Code;
import TOPSECRET.domain.PublicationType;



import javax.management.InstanceNotFoundException;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link US012GetListOfPublicationsInLibraryController} class.
 * <p>
 * This test class verifies the controller's behavior when retrieving and converting
 * publications from a user's library to DTOs, including validation and edge cases.
 * </p>
 */
class US012GetListOfPublicationsInLibraryControllerTest {


    // ============ Tests ============

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when the username is empty.
     * <p>
     * Validates that the controller properly rejects empty username inputs.
     * </p>
     */
//    @Test
//    void shouldThrowExceptionWhenUserNameIsEmpty() {
//        // Arrange
//        String emptyUser = "";
//
//        ILibraryRepository libraryRepositoryDouble = mock(ILibraryRepository.class);
//        US012GetListOfPublicationsInLibraryController ctrl = new US012GetListOfPublicationsInLibraryController(libraryRepositoryDouble);
//
//
//        // Act & Assert
//        assertThrows(IllegalArgumentException.class, () -> ctrl.getListOfPublicationsInLibraryDTO(emptyUser));
//    }

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
//    @Test
//    void ensureListOfPublicationsInLibraryIsEmptyWhenLibraryReturnsEmptyAList() throws InstanceNotFoundException {
//        // Arrange
//        String username = "User1";
//
//        ILibraryRepository libraryRepositoryDouble = mock(ILibraryRepository.class);
//        when(libraryRepositoryDouble.getAllPublicationsFromUserLibrary(username)).thenReturn(List.of());
//
//
//        US012GetListOfPublicationsInLibraryController ctrl = new US012GetListOfPublicationsInLibraryController(libraryRepositoryDouble);
//
//        // Act
//        List<PublicationDetailsDTO_US012> result = ctrl.getListOfPublicationsInLibraryDTO(username);
//
//        // Assert
//        assertNotNull(result);
//        assertTrue(result.isEmpty());
//    }


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
//    @Test
//    void ensureListOfPublicationsInLibraryReturnsDTOs() throws InstanceNotFoundException {
//        // Arrange
//        String username = "User1";
//        String expectedTitle1 = "Title1";
//        String expectedTitle2 = "Title2";
//        List<String> expectedAuthors1 = List.of("Author1","Author2");
//        List<String> expectedAuthors2 = List.of("Author3","Author4");
//        String expectedCode1 = "123456789";
//        String expectedCode2 = "123456780";
//
//
//        PublicationSample pub1 = createMockPublicationSample(expectedTitle1,expectedAuthors1,expectedCode1);
//        PublicationSample pub2 = createMockPublicationSample(expectedTitle2,expectedAuthors2,expectedCode2);
//
//        ILibraryRepository libraryRepositoryDouble = mock(ILibraryRepository.class);
//        when(libraryRepositoryDouble.getAllPublicationsFromUserLibrary(username)).thenReturn(List.of(pub1,pub2));
//
//
//        US012GetListOfPublicationsInLibraryController ctrl = new US012GetListOfPublicationsInLibraryController(libraryRepositoryDouble);
//
//        // Act
//        List<PublicationDetailsDTO_US012> result = ctrl.getListOfPublicationsInLibraryDTO(username);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(2, result.size());
//
//        // Verify first DTO
//        PublicationDetailsDTO_US012 dto1 = result.get(0);
//        assertEquals(expectedTitle1, dto1.pubTitle);
//        assertEquals(expectedCode1, dto1.code);
//        assertEquals("Book", dto1.publicationType);
//        assertIterableEquals(expectedAuthors1, dto1.authorName);
//
//        // Verify second DTO
//        PublicationDetailsDTO_US012 dto2 = result.get(1);
//        assertEquals(expectedTitle2, dto2.pubTitle);
//        assertEquals(expectedCode2, dto2.code);
//        assertEquals("Book", dto2.publicationType);
//        assertIterableEquals(expectedAuthors2, dto2.authorName);
//    }
    // -------------------------------------------------------------------------
    // Test helpers
    // -------------------------------------------------------------------------

    private PublicationSample createMockPublicationSample(String title, List<String> authors, String code) {
        BibliographicalData bibliographicalDataDouble = createMockBibliographicalData(title, authors);
        Edition editionDouble = createMockEdition(code);

        PublicationSample publicationSample = mock(PublicationSample.class);
        when(publicationSample.getBibliographicalData()).thenReturn(bibliographicalDataDouble);
        when(publicationSample.getEdition()).thenReturn(editionDouble);

        return publicationSample;
    }

    private Edition createMockEdition(String code) {
        PublicationType publicationTypeDouble = mock(PublicationType.class);
        when(publicationTypeDouble.getMediaFormat()).thenReturn("Book");

        Code codeDouble = mock(Code.class);
        when(codeDouble.toString()).thenReturn(code);

        Edition editionDouble = mock(Edition.class);
        when(editionDouble.getCode()).thenReturn(codeDouble);
        when(editionDouble.getPublicationType()).thenReturn(publicationTypeDouble);

        return editionDouble;
    }

    private BibliographicalData createMockBibliographicalData(String title, List<String> authorsNames) {
        List<Author> authorsDouble = createMockAuthors(authorsNames);
        BibliographicalData bibliographicalDataDouble = mock(BibliographicalData.class);

        when(bibliographicalDataDouble.getTitle()).thenReturn(title);
        when(bibliographicalDataDouble.getAuthors()).thenReturn(authorsDouble);

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