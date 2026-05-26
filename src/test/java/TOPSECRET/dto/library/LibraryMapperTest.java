package TOPSECRET.dto.library;

import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.ValueObjects.LibraryId;
import TOPSECRET.dto.library.response.ResponseLibraryDTO;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link LibraryMapper}.
 */
class LibraryMapperTest {

    @Test
    void shouldThrowExceptionWhenInstantiatingUtilityClass() throws Exception {

        // ARRANGE
        Constructor<LibraryMapper> constructor =
                LibraryMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // ACT
        InvocationTargetException ex =
                assertThrows(InvocationTargetException.class, constructor::newInstance);

        // ASSERT
        assertInstanceOf(UnsupportedOperationException.class, ex.getCause());
        assertEquals("Utility class", ex.getCause().getMessage());
    }

    @Test
    void shouldMapLibraryToDTO() {

        // ARRANGE
        Library library = mock(Library.class);
        LibraryId LibraryId = mock(LibraryId.class);

        when(library.getLibraryName()).thenReturn("My Library");
        when(library.id()).thenReturn(LibraryId);
        when(LibraryId.getId()).thenReturn("LIB-1");

        // ACT
        ResponseLibraryDTO dto = LibraryMapper.toDTO(library);

        // ASSERT
        assertEquals("My Library", dto.getLibraryName());
        assertEquals("LIB-1", dto.getLibraryId());
    }

    @Test
    void shouldThrowExceptionWhenLibraryIsNull() {

        // ACT + ASSERT
        NullPointerException exception =
                assertThrows(NullPointerException.class,
                        () -> LibraryMapper.toDTO(null));

        assertEquals("Library cannot be null", exception.getMessage());
    }

    @Test
    void shouldMapListOfLibrariesToDTOList() {

        // ARRANGE
        Library lib1 = mock(Library.class);
        Library lib2 = mock(Library.class);

        LibraryId id1 = mock(LibraryId.class);
        LibraryId id2 = mock(LibraryId.class);

        when(lib1.getLibraryName()).thenReturn("Library 1");
        when(lib2.getLibraryName()).thenReturn("Library 2");

        when(lib1.id()).thenReturn(id1);
        when(lib2.id()).thenReturn(id2);

        when(id1.getId()).thenReturn("LIB-1");
        when(id2.getId()).thenReturn("LIB-2");

        List<Library> libraries = List.of(lib1, lib2);

        // ACT
        List<ResponseLibraryDTO> result = LibraryMapper.toDTOList(libraries);

        // ASSERT
        assertEquals(2, result.size());
        assertEquals("LIB-1", result.get(0).getLibraryId());
        assertEquals("LIB-2", result.get(1).getLibraryId());
    }

    @Test
    void shouldThrowExceptionWhenListIsNull() {

        // ACT + ASSERT
        NullPointerException exception =
                assertThrows(NullPointerException.class,
                        () -> LibraryMapper.toDTOList(null));

        assertEquals("Libraries list cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenListContainsNullLibrary() {

        // ARRANGE
        Library validLibrary = mock(Library.class);
        LibraryId id = mock(LibraryId.class);

        when(validLibrary.getLibraryName()).thenReturn("Valid Library");
        when(validLibrary.id()).thenReturn(id);
        when(id.toString()).thenReturn("LIB-1");

        List<Library> libraries = Arrays.asList(validLibrary, null);

        // ACT + ASSERT
        NullPointerException exception =
                assertThrows(NullPointerException.class,
                        () -> LibraryMapper.toDTOList(libraries));

        assertEquals("Library cannot be null", exception.getMessage());
    }

    @Test
    void shouldReturnImmutableList() {

        // ARRANGE
        Library library = mock(Library.class);
        LibraryId id = mock(LibraryId.class);

        when(library.getLibraryName()).thenReturn("Lib");
        when(library.id()).thenReturn(id);
        when(id.toString()).thenReturn("LIB-1");

        // ACT
        List<ResponseLibraryDTO> result =
                LibraryMapper.toDTOList(List.of(library));

        // ASSERT
        assertThrows(UnsupportedOperationException.class,
                () -> result.add(mock(ResponseLibraryDTO.class)));
    }
}