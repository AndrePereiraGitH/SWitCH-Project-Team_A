package TOPSECRET.domain.Library;

import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;

class LibraryFactoryTest {

    @Test
    void returnsNewlyConstructedLibraryInstance() {

        String libraryName = "lib1";
        UserId userIdDouble = mock(UserId.class);

        try (MockedConstruction<Library> mocked =
                     mockConstruction(Library.class, (mock, context) -> {
                     })) {

            LibraryFactory libraryFactory = new LibraryFactory();

            // Act
            Library library = libraryFactory.create(libraryName, userIdDouble);

            // Assert
            assertNotNull(library);
            assertEquals(1, mocked.constructed().size());
        }
    }

    @Test
    void createsLibraryUsingGivenParameters() {

        String libraryName = "lib1";
        UserId userIdDouble = mock(UserId.class);

        LibraryFactory libraryFactory = new LibraryFactory();

        // Act
        Library library = libraryFactory.create(libraryName, userIdDouble);

        // Assert
        assertEquals(libraryName, library.getLibraryName());
        assertNotNull(library.id());
    }
}