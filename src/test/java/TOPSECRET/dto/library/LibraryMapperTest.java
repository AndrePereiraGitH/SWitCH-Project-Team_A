package TOPSECRET.dto.library;

import TOPSECRET.domain.Library.Library;
import TOPSECRET.domain.ValueObjects.UserId;
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
        UserId userId = mock(UserId.class);

        when(library.getLibraryName()).thenReturn("My Library");
        when(library.getUserId()).thenReturn(userId);
        when(userId.toString()).thenReturn("USER-1");

        // ACT
        ResponseLibraryDTO dto = LibraryMapper.toDTO(library);

        // ASSERT
        assertEquals("My Library", dto.getLibraryName());
        assertEquals("USER-1", dto.getUserId());
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
    void shouldMapListOfLibrariesToDTOs() {

        // ARRANGE
        Library lib1 = mock(Library.class);
        Library lib2 = mock(Library.class);

        UserId userId1 = mock(UserId.class);
        UserId userId2 = mock(UserId.class);

        when(lib1.getLibraryName()).thenReturn("Library 1");
        when(lib2.getLibraryName()).thenReturn("Library 2");

        when(lib1.getUserId()).thenReturn(userId1);
        when(lib2.getUserId()).thenReturn(userId2);

        when(userId1.toString()).thenReturn("USER-1");
        when(userId2.toString()).thenReturn("USER-2");

        List<Library> libraries = List.of(lib1, lib2);

        // ACT
        List<ResponseLibraryDTO> result = LibraryMapper.toDTOs(libraries);

        // ASSERT
        assertEquals(2, result.size());

    }

    @Test
    void shouldThrowExceptionWhenListIsNull() {

        // ACT + ASSERT
        NullPointerException exception =
                assertThrows(NullPointerException.class,
                        () -> LibraryMapper.toDTOs(null));

        assertEquals("Libraries list cannot be null", exception.getMessage());
    }

    @Test
    void shouldReturnImmutableList() {

        // ARRANGE
        Library library = mock(Library.class);
        UserId userId = mock(UserId.class);

        when(library.getLibraryName()).thenReturn("Lib");
        when(library.getUserId()).thenReturn(userId);
        when(userId.toString()).thenReturn("USER-1");

        List<ResponseLibraryDTO> result =
                LibraryMapper.toDTOs(List.of(library));

        // ASSERT
        assertThrows(UnsupportedOperationException.class,
                () -> result.add(mock(ResponseLibraryDTO.class)));
    }
}