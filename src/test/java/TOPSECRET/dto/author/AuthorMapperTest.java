package TOPSECRET.dto.author;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.dto.author.response.AuthorDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AuthorMapper}.
 *
 * <p>Verifies correct mapping between {@link Author} domain objects
 * and {@link AuthorDTO} response objects.</p>
 */
class AuthorMapperTest {

    // -------------------------------------------------------------------------
    // toDTO()
    // -------------------------------------------------------------------------

    @Test
    void toDTOShouldReturnAuthorDTOWhenAuthorAndCountryAreValid() {

        // Arrange
        Author authorDouble = mock(Author.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        Country countryDouble = mock(Country.class);

        when(authorIdDouble.getId()).thenReturn("some-uuid");
        when(authorDouble.id()).thenReturn(authorIdDouble);
        when(authorDouble.getName()).thenReturn("José Saramago");
        when(authorDouble.getDateOfBirth()).thenReturn(LocalDate.of(1922, 11, 16));
        when(authorDouble.getBiography()).thenReturn("Portuguese Nobel Prize-winning author.");
        when(countryDouble.getCountryName()).thenReturn("Portugal");

        // Act
        AuthorDTO result = AuthorMapper.toDTO(authorDouble, countryDouble);

        // Assert
        assertEquals("some-uuid", result.getId());
        assertEquals("José Saramago", result.getName());
        assertEquals("Portugal", result.getCountryName());
        assertEquals("1922-11-16", result.getDateOfBirth());
        assertEquals("Portuguese Nobel Prize-winning author.", result.getBiography());
    }

    @Test
    void toDTOShouldThrowExceptionWhenAuthorIsNull() {

        // Arrange
        Country countryDouble = mock(Country.class);

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> AuthorMapper.toDTO(null, countryDouble));
    }

    @Test
    void toDTOShouldThrowExceptionWhenCountryIsNull() {

        // Arrange
        Author authorDouble = mock(Author.class);

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> AuthorMapper.toDTO(authorDouble, null));
    }

    // -------------------------------------------------------------------------
    // toDTOs()
    // -------------------------------------------------------------------------

    @Test
    void toDTOsShouldConvertListOfAuthorsToListOfDTOs() {

        // Arrange
        Author authorDouble1 = mock(Author.class);
        Author authorDouble2 = mock(Author.class);
        AuthorId idDouble1 = mock(AuthorId.class);
        AuthorId idDouble2 = mock(AuthorId.class);
        Country countryDouble1 = mock(Country.class);
        Country countryDouble2 = mock(Country.class);

        when(idDouble1.getId()).thenReturn("uuid-1");
        when(idDouble2.getId()).thenReturn("uuid-2");
        when(authorDouble1.id()).thenReturn(idDouble1);
        when(authorDouble2.id()).thenReturn(idDouble2);
        when(authorDouble1.getName()).thenReturn("José Saramago");
        when(authorDouble2.getName()).thenReturn("Fernando Pessoa");
        when(authorDouble1.getDateOfBirth()).thenReturn(LocalDate.of(1922, 11, 16));
        when(authorDouble2.getDateOfBirth()).thenReturn(LocalDate.of(1888, 6, 13));
        when(authorDouble1.getBiography()).thenReturn("Nobel Prize-winning author.");
        when(authorDouble2.getBiography()).thenReturn("Portuguese modernist poet.");
        when(countryDouble1.getCountryName()).thenReturn("Portugal");
        when(countryDouble2.getCountryName()).thenReturn("Portugal");

        // Act
        List<AuthorDTO> result = AuthorMapper.toDTOs(
                List.of(authorDouble1, authorDouble2),
                List.of(countryDouble1, countryDouble2));

        // Assert
        assertEquals(2, result.size());
        assertEquals("uuid-1", result.get(0).getId());
        assertEquals("uuid-2", result.get(1).getId());
    }

    @Test
    void toDTOsShouldReturnEmptyListWhenInputIsEmpty() {

        // Act
        List<AuthorDTO> result = AuthorMapper.toDTOs(List.of(), List.of());

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void toDTOsShouldThrowExceptionWhenAuthorsListIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> AuthorMapper.toDTOs(null, List.of()));
    }

    @Test
    void toDTOsShouldThrowExceptionWhenCountriesListIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> AuthorMapper.toDTOs(List.of(), null));
    }
}