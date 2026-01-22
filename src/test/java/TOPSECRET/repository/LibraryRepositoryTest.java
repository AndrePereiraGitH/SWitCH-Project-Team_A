package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.UserRole;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LibraryRepositoryTest {

    @Test
    void create_shouldReturnFalseWhenUserAlreadyHasALibrary() {
        // Arrange
        String username = "username";

        Map<String, Library> libraries = new HashMap<>();
        libraries.put(username, library());

        LibraryRepository libraryRepository = new LibraryRepository(libraries);

        // Act
        boolean result = libraryRepository.create(username, library());

        // Assert
        assertFalse(result);
    }

    @Test
    void create_shouldReturnTrueWhenUserDoesNotHaveALibrary() {
        // Arrange
        String username = "username";

        Map<String, Library> libraries = new HashMap<>();
        libraries.put("other username", library());

        LibraryRepository libraryRepository = new LibraryRepository(libraries);

        // Act
        boolean result = libraryRepository.create(username, library());

        // Assert
        assertTrue(result);
    }

    private Library library() {
        Profile profile = new Profile("profile name", new Country("Portugal"), LocalDate.now());
        User user = new User("user name", new Contact(), profile, UserRole.NORMAL_USER);

        return new Library("library name", user);
    }

}