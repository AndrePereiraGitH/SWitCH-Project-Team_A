package TOPSECRET.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.repository.PublicationSampleRepositoryMem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Unit tests for {@link US011AddPublicationToLibraryController} using Mockito.
 * <p>
 * This test class verifies the behavior of the controller in isolation by mocking
 * all its collaborators, namely {@link Library} and {@link PublicationSampleRepositoryMem}.
 * The goal is to ensure that the controller correctly delegates responsibilities
 * to its dependencies and returns the expected results.
 * </p>
 */
class US011AddPublicationToLibraryControllerTest {

    private Library libraryMock;
    private PublicationSampleRepositoryMem pubSampleRepositoryMock;
    private US011AddPublicationToLibraryController controller;

    private PublicationSample publication1;
    private PublicationSample publication2;
    private PublicationSample publication3;

    /**
     * Initializes the mocked collaborators and the controller before each test.
     * <p>
     * All dependencies of the controller are mocked so that the tests focus only
     * on the controller's logic and interactions.
     * </p>
     */
    @BeforeEach
    void preparation() {

        libraryMock = mock(Library.class);
        pubSampleRepositoryMock = mock(PublicationSampleRepositoryMem.class);

        publication1 = mock(PublicationSample.class);
        publication2 = mock(PublicationSample.class);
        publication3 = mock(PublicationSample.class);

        controller = new US011AddPublicationToLibraryController(libraryMock, pubSampleRepositoryMock);
    }

    /**
     * Verifies that the controller returns the list of publications
     * that are not present in the user's library.
     * <p>
     * The library is mocked to contain one publication, and the repository
     * returns the remaining publications that are not part of the library.
     * </p>
     */
    @Test
    void getListOfOtherPublications_shouldReturnPublicationsNotInLibrary() {

        List<PublicationSample> publicationsInLibrary = List.of(publication1);
        List<PublicationSample> expectedList = List.of(publication2, publication3);

        when(pubSampleRepositoryMock.getAllPublications()).thenReturn(publicationsInLibrary);
        when(pubSampleRepositoryMock.getListOfDifferentPublications(publicationsInLibrary)).thenReturn(expectedList);

        List<PublicationSample> result = controller.getListOfOtherPublications();

        //assert
        assertEquals(expectedList, result);
        assertEquals(2, result.size());
        assertTrue(result.contains(publication2));
        assertTrue(result.contains(publication3));
        assertFalse(result.contains(publication1));


    }

    /**
     * Verifies that when the library is empty, the controller returns
     * all publications available in the repository.
     */
    @Test
    void getListOfOtherPublications_shouldReturnAllPublications_emptyLibrary() {

        List<PublicationSample> emptyLibrary = List.of();
        List<PublicationSample> allPublications = List.of(publication1, publication2, publication3);

        when(pubSampleRepositoryMock.getAllPublications()).thenReturn(emptyLibrary);
        when(pubSampleRepositoryMock.getListOfDifferentPublications(emptyLibrary)).thenReturn(allPublications);

        List<PublicationSample> result = controller.getListOfOtherPublications();

        //assert
        assertEquals(3, result.size());
        assertTrue(result.contains(publication1));
        assertTrue(result.contains(publication2));
        assertTrue(result.contains(publication3));

    }

    /**
     * Verifies that the controller returns an empty list when all publications
     * from the repository are already present in the user's library.
     */
    @Test
    void getListOfOtherPublications_shouldReturnNothing_libraryHasAllPublications() {

        List<PublicationSample> libraryPublications = List.of(publication1, publication2, publication3);
        List<PublicationSample> emptyList = List.of();

        when(pubSampleRepositoryMock.getAllPublications()).thenReturn(libraryPublications);
        when(pubSampleRepositoryMock.getListOfDifferentPublications(libraryPublications)).thenReturn(emptyList);

        List<PublicationSample> result = controller.getListOfOtherPublications();

        //assert
        assertTrue(result.isEmpty());

    }

    /**
     * Verifies that the returned list of publications cannot be modified.
     * <p>
     * The repository returns an unmodifiable list and the test ensures that
     * attempting to modify it results in an {@link UnsupportedOperationException}.
     * </p>
     */
    @Test
    void getListOfOtherPublications_shouldNotBeAbleToModifyList() {


        List<PublicationSample> libraryList = List.of(publication1);
        List<PublicationSample> unmodifiableResult = List.of(publication2);

        when(pubSampleRepositoryMock.getAllPublications()).thenReturn(libraryList);
        when(pubSampleRepositoryMock.getListOfDifferentPublications(libraryList)).thenReturn(unmodifiableResult);

        List<PublicationSample> result = controller.getListOfOtherPublications();

        assertThrows(UnsupportedOperationException.class, () -> result.add(publication1));
    }

    /**
     * Verifies that a publication can be successfully added to the library
     * through the controller.
     */
    @Test
    void addPublicationToLib_shouldAddPublication() {

        boolean result = controller.addPublicationToLib(publication1);

        //assert
        assertTrue(result);

    }

    /**
     * Verifies that the controller throws an {@link IllegalArgumentException}
     * when attempting to add a {@code null} publication to the library.
     */
    @Test
    void addPublicationToLib_shouldThrowExceptionForNullPublication() {

        //assert
        assertThrows(IllegalArgumentException.class, () -> controller.addPublicationToLib(null));

    }
}
