package TOPSECRET.dto.library;

import TOPSECRET.domain.Library.Library;
import TOPSECRET.dto.library.response.ResponseLibraryDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LibraryMapperTest {

    @Test
    void shouldMapLibraryToDTO() {

        // ARRANGE
        Library library = mock(Library.class);

        when(library.getLibraryName()).thenReturn("My Library");

        // ACT
        ResponseLibraryDTO dto = LibraryMapper.toDTO(library);

        // ASSERT
        assertEquals("My Library", dto.getLibraryName());
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

        when(lib1.getLibraryName()).thenReturn("Library 1");
        when(lib2.getLibraryName()).thenReturn("Library 2");

        List<Library> libraries = List.of(lib1, lib2);

        // ACT
        List<ResponseLibraryDTO> result = LibraryMapper.toDTOList(libraries);

        // ASSERT
        assertEquals(2, result.size());

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
    void shouldReturnImmutableList() {

        // ARRANGE
        Library library = mock(Library.class);
 

        when(library.getLibraryName()).thenReturn("Lib");

        List<ResponseLibraryDTO> result =
                LibraryMapper.toDTOList(List.of(library));

        // ASSERT
        assertThrows(UnsupportedOperationException.class,
                () -> result.add(mock(ResponseLibraryDTO.class)));
    }
}