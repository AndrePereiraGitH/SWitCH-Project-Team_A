package TOPSECRET.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import TOPSECRET.domain.Enum.*;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.List;

/**
 * Unit tests for the {@link Library} class.
 * <p>
 * This test class verifies the behavior of the Library class including construction,
 * publication management, and getter methods.
 * </p>
 */
class LibraryTest {


    // ============ Constructor Tests ============

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when library name is null or empty.
     */
    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowExceptionWhenLibraryNameIsInvalid(String libraryName) {
        // Arrange
        User userDouble = createMockUser("Nome1");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Library(libraryName, userDouble));
    }

    /**
     * Tests that an {@link IllegalArgumentException} is thrown when user is null.
     */
    @Test
    void shouldThrowExceptionWhenUserIsNull() {
        // Arrange
        String libraryName = "Some valid name";

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new Library(libraryName, null));
    }

    /**
     * Tests that a Library instance is properly initialized with valid parameters.
     */
    @Test
    void shouldInitializeInstanceWhenValidParameters() {
        // Arrange
        String libraryName = "Library name";
        User userDouble = createMockUser("User1");

        // Act
        Library result = new Library(libraryName, userDouble);

        // Assert
        assertEquals(libraryName, result.getLibraryName());
        assertEquals(result.getUser().getUserName(),userDouble.getUserName());
    }

    // ============ Getter Tests ============

    /**
     * Tests that {@link Library#getLibraryName()} returns the correct library name.
     */
    @Test
    void getLibraryName_shouldReturnLibraryName() {

        //Arrange
        String expectedLibraryName = "lib1";
        Library library = new Library(expectedLibraryName,createMockUser("User1"));

        // Act
        String result = library.getLibraryName();

        // Assert
        assertEquals(expectedLibraryName, result);
    }

    // ============ FindLibraryPublicationsAvailableForSale Tests ============

    @Test
    void findLibraryPublicationsAvailableForSale_ShouldReturnOnlyEligibleSamples() {

        // Arrange
        PublicationSample pubSample1 = mock(PublicationSample.class);
        when(pubSample1.getPublicationStatus()).thenReturn(PublicationStatus.SOLD);
        PublicationSample pubSample2 = mock(PublicationSample.class);
        when(pubSample2.getPublicationStatus()).thenReturn(PublicationStatus.FOR_SALE);
        PublicationSample pubSample3 = mock(PublicationSample.class);
        when(pubSample3.getPublicationStatus()).thenReturn(PublicationStatus.READING);

        Library library = new Library("LibraryName1", createMockUser("User1"));
        library.addPublication(pubSample1);
        library.addPublication(pubSample2);
        library.addPublication(pubSample3);

        // Act
        List<PublicationSample> availablePublications = library.findLibraryPublicationsAvailableForSale();

        // Assert
        assertEquals(1, availablePublications.size());
        assertTrue(availablePublications.contains(pubSample3));
    }

    @Test
    void findLibraryPublicationsAvailableForSale_ShouldReturnEmptyListWhenNoSamplesAreEligible() {

        // Arrange
        PublicationSample pubSample1 = mock(PublicationSample.class);
        when(pubSample1.getPublicationStatus()).thenReturn(PublicationStatus.SOLD);
        PublicationSample pubSample2 = mock(PublicationSample.class);
        when(pubSample2.getPublicationStatus()).thenReturn(PublicationStatus.FOR_SALE);

        Library library = new Library("LibraryName1", createMockUser("User1"));
        library.addPublication(pubSample1);
        library.addPublication(pubSample2);

        // Act
        List<PublicationSample> availablePublications = library.findLibraryPublicationsAvailableForSale();

        // Assert
        assertTrue(availablePublications.isEmpty());
    }

    @Test
    void findLibraryPublicationsAvailableForSale_ShouldReturnEmptyListWhenLibraryIsEmpty() {

        // Arrange
        Library library = new Library("LibraryName1", createMockUser("User1"));

        // Act
        List<PublicationSample> availablePublications = library.findLibraryPublicationsAvailableForSale();

        // Assert
        assertTrue(availablePublications.isEmpty());

    }

    // -------------------------------------------------------------------------
    // Test helpers
    // -------------------------------------------------------------------------

    private User createMockUser(String name){
        User userDouble = mock(User.class);
        when(userDouble.getUserName()).thenReturn(name);
        return userDouble;

    }
}