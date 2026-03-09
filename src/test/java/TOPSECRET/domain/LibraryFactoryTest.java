package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LibraryFactoryTest {

    @Test
    void create_returnsNewlyConstructedLibraryInstance() {

        String libraryName ="lib1";
        User userDouble = mock(User.class);

        try(MockedConstruction<Library> mocked =
                mockConstruction(Library.class,(mock, context) -> {
                })) {

        LibraryFactory libraryFactory = new LibraryFactory();

        //Act
        Library library = libraryFactory.create(libraryName, userDouble);

        // Assert
        assertNotNull(library);
        assertEquals(1, mocked.constructed().size());

        Library constructedLibrary = mocked.constructed().get(0);
        assertSame(library, constructedLibrary);

        }
    }
}