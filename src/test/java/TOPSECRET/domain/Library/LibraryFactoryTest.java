package TOPSECRET.domain.Library;

import TOPSECRET.domain.ValueObjects.LibraryId;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class LibraryFactoryTest {

    // -------- create(String, UserId) Tests --------

    @Test
    void create_shouldCreateLibraryWithGeneratedId() {
        LibraryFactory factory = new LibraryFactory();

        UserId userIdDouble = mock(UserId.class);

        Library result = factory.create("lib1", userIdDouble);

        assertNotNull(result);
        assertNotNull(result.id());
        assertEquals("lib1", result.getLibraryName());
        assertSame(userIdDouble, result.getUserId());
    }

    @Test
    void create_shouldThrowException_whenLibraryNameIsNull() {
        LibraryFactory factory = new LibraryFactory();

        UserId userIdDouble = mock(UserId.class);

        assertThrows(IllegalArgumentException.class,
                () -> factory.create(null, userIdDouble));
    }

    @Test
    void create_shouldThrowException_whenLibraryNameIsBlank() {
        LibraryFactory factory = new LibraryFactory();

        UserId userIdDouble = mock(UserId.class);

        assertThrows(IllegalArgumentException.class,
                () -> factory.create("   ", userIdDouble));
    }

    @Test
    void create_shouldThrowException_whenUserIdIsNull() {
        LibraryFactory factory = new LibraryFactory();

        assertThrows(IllegalArgumentException.class,
                () -> factory.create("lib1", null));
    }

    // -------- create(LibraryId, String, UserId) Tests --------

    @Test
    void createWithId_shouldCreateLibraryWithGivenId() {
        LibraryFactory factory = new LibraryFactory();

        LibraryId libraryIdDouble = mock(LibraryId.class);
        UserId userIdDouble = mock(UserId.class);

        Library result = factory.create(libraryIdDouble, "lib1", userIdDouble);

        assertNotNull(result);
        assertSame(libraryIdDouble, result.id());
        assertEquals("lib1", result.getLibraryName());
        assertSame(userIdDouble, result.getUserId());
    }

    @Test
    void createWithId_shouldThrowException_whenLibraryIdIsNull() {
        LibraryFactory factory = new LibraryFactory();

        UserId userIdDouble = mock(UserId.class);

        assertThrows(IllegalArgumentException.class,
                () -> factory.create(null, "lib1", userIdDouble));
    }

    @Test
    void createWithId_shouldThrowException_whenLibraryNameIsNull() {
        LibraryFactory factory = new LibraryFactory();

        LibraryId libraryIdDouble = mock(LibraryId.class);
        UserId userIdDouble = mock(UserId.class);

        assertThrows(IllegalArgumentException.class,
                () -> factory.create(libraryIdDouble, null, userIdDouble));
    }

    @Test
    void createWithId_shouldThrowException_whenLibraryNameIsBlank() {
        LibraryFactory factory = new LibraryFactory();

        LibraryId libraryIdDouble = mock(LibraryId.class);
        UserId userIdDouble = mock(UserId.class);

        assertThrows(IllegalArgumentException.class,
                () -> factory.create(libraryIdDouble, "   ", userIdDouble));
    }

    @Test
    void createWithId_shouldThrowException_whenUserIdIsNull() {
        LibraryFactory factory = new LibraryFactory();

        LibraryId libraryIdDouble = mock(LibraryId.class);

        assertThrows(IllegalArgumentException.class,
                () -> factory.create(libraryIdDouble, "lib1", null));
    }
}