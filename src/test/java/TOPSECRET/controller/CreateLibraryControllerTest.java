package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.UserRole;
import TOPSECRET.repository.LibraryRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CreateLibraryControllerTest {

    @Test
    void constructor_shouldThrowExceptionWhenNullLibraryRepository() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new CreateLibraryController(null));
    }

    @Test
    void constructor_doesNotThrowExceptionWhenLibraryRepositoryIsValid() {
        // Act & Assert
        assertDoesNotThrow(() -> new CreateLibraryController(new LibraryRepository(Map.of())));
    }

    @Test
    void createAndAddLibrary_shouldReturnFalseWhenCreateReturnsFalse() {
        // Arrange
        User user = user();
        LibraryRepository repository = new LibraryRepository(Map.of(user.getUserName(), libraryWithUser(user)));
        CreateLibraryController controller = new CreateLibraryController(repository);

        // Act
        boolean result = controller.createAndAddLibrary("libraryName", user);

        // Assert
        assertFalse(result);
    }

    @Test
    void createAndAddLibrary_shouldReturnTrueWhenCreateReturnsTrue() {
        // Arrange
        User user = user();
        LibraryRepository repository = new LibraryRepository(Map.of());
        CreateLibraryController controller = new CreateLibraryController(repository);

        // Act
        boolean result = controller.createAndAddLibrary("libraryName", user);

        // Assert
        assertTrue(result);
    }

    private User user() {
        Contact contact = new Contact();
        Profile profile = new Profile("profile name", new Country("Portugal"), LocalDate.now());
        return new User("user name", contact, profile, UserRole.NORMAL_USER);
    }

    private Library libraryWithUser(User user) {
        return new Library("library name", user);
    }

}
