package TOPSECRET.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import TOPSECRET.domain.Enum.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import javax.swing.*;

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

    // -------------------------------------------------------------------------
    // Test helpers
    // -------------------------------------------------------------------------

    private User createMockUser(String name){
        User userDouble = mock(User.class);
        when(userDouble.getUserName()).thenReturn(name);
        return userDouble;

    }
}