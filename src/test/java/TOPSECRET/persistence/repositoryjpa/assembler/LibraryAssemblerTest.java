package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.Library.LibraryFactory;
import TOPSECRET.domain.ValueObjects.LibraryId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.persistence.repositoryjpa.datamodel.LibraryDM;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link LibraryAssembler}.
 */
class LibraryAssemblerTest {

    // -------- Constructor Tests --------

    @Test
    void constructor_shouldThrowException_whenLibraryFactoryIsNull() {
        // Arrange & Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new LibraryAssembler(null));
    }

    // -------- toDomain Tests --------

    @Test
    void toDomain_shouldConvertLibraryDMToLibrary() {
        // Arrange
        LibraryFactory libraryFactory = mock(LibraryFactory.class);
        LibraryAssembler assembler = new LibraryAssembler(libraryFactory);

        LibraryId libraryId = new LibraryId("library-1");
        UserId userId = new UserId("user-1");

        LibraryDM libraryDM = mock(LibraryDM.class);
        Library library = mock(Library.class);

        when(libraryDM.getId()).thenReturn(libraryId);
        when(libraryDM.getLibraryName()).thenReturn("My Library");
        when(libraryDM.getUserId()).thenReturn(userId);
        when(libraryFactory.create(libraryId, "My Library", userId)).thenReturn(library);

        // Act
        Library result = assembler.toDomain(libraryDM);

        // Assert
        assertSame(library, result);
    }

    @Test
    void toDomain_shouldThrowException_whenLibraryDMIsNull() {
        // Arrange
        LibraryFactory libraryFactory = mock(LibraryFactory.class);
        LibraryAssembler assembler = new LibraryAssembler(libraryFactory);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> assembler.toDomain((LibraryDM) null));
    }

    @Test
    void toDomainList_shouldConvertAllLibraryDMsToLibraries() {
        // Arrange
        LibraryFactory libraryFactory = mock(LibraryFactory.class);
        LibraryAssembler assembler = new LibraryAssembler(libraryFactory);

        LibraryId libraryId1 = new LibraryId("library-1");
        UserId userId1 = new UserId("user-1");
        LibraryId libraryId2 = new LibraryId("library-2");
        UserId userId2 = new UserId("user-2");

        LibraryDM libraryDM1 = mock(LibraryDM.class);
        LibraryDM libraryDM2 = mock(LibraryDM.class);

        Library library1 = mock(Library.class);
        Library library2 = mock(Library.class);

        when(libraryDM1.getId()).thenReturn(libraryId1);
        when(libraryDM1.getLibraryName()).thenReturn("Library 1");
        when(libraryDM1.getUserId()).thenReturn(userId1);
        when(libraryFactory.create(libraryId1, "Library 1", userId1)).thenReturn(library1);

        when(libraryDM2.getId()).thenReturn(libraryId2);
        when(libraryDM2.getLibraryName()).thenReturn("Library 2");
        when(libraryDM2.getUserId()).thenReturn(userId2);
        when(libraryFactory.create(libraryId2, "Library 2", userId2)).thenReturn(library2);

        // Act
        List<Library> result = assembler.toDomainList(List.of(libraryDM1, libraryDM2));

        // Assert
        assertEquals(2, result.size());
        assertSame(library1, result.get(0));
        assertSame(library2, result.get(1));
    }

    @Test
    void toDomainList_shouldReturnEmpty_whenIterableIsEmpty() {
        // Arrange
        LibraryFactory libraryFactory = mock(LibraryFactory.class);
        LibraryAssembler assembler = new LibraryAssembler(libraryFactory);

        // Act
        List<Library> result = assembler.toDomainList(List.of());

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // -------- toDM Tests --------

    @Test
    void toDM_shouldConvertLibraryToLibraryDM() {
        // Arrange
        LibraryFactory libraryFactory = mock(LibraryFactory.class);
        LibraryAssembler assembler = new LibraryAssembler(libraryFactory);

        LibraryId libraryId = new LibraryId("library-1");
        UserId userId = new UserId("user-1");

        Library library = mock(Library.class);

        when(library.id()).thenReturn(libraryId);
        when(library.getLibraryName()).thenReturn("My Library");
        when(library.getUserId()).thenReturn(userId);

        // Act
        LibraryDM result = assembler.toDM(library);

        // Assert
        assertNotNull(result);
        assertEquals(libraryId, result.getId());
        assertEquals("My Library", result.getLibraryName());
        assertEquals(userId, result.getUserId());
    }

    @Test
    void toDM_shouldThrowException_whenLibraryIsNull() {
        // Arrange
        LibraryFactory libraryFactory = mock(LibraryFactory.class);
        LibraryAssembler assembler = new LibraryAssembler(libraryFactory);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> assembler.toDM(null));
    }
}