package TOPSECRET.dto.author;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.ICountryRepository;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.dto.author.response.AuthorDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AuthorMapper}.
 *
 * <p>This test class verifies the correct mapping behavior between
 * {@link Author} domain objects and {@link AuthorDTO} response objects.</p>
 *
 * <p>The tests validate:</p>
 * <ul>
 *     <li>Conversion of a single domain object into a response DTO</li>
 *     <li>Conversion of collections of domain objects</li>
 *     <li>Correct transfer of field values</li>
 *     <li>Handling of empty collections</li>
 *     <li>Validation of null arguments</li>
 *     <li>Handling of unresolvable country IDs</li>
 * </ul>
 *
 * <p>Mockito is used to isolate the mapper from domain object implementations.</p>
 */
class AuthorMapperTest {

    private ICountryRepository countryRepoDouble;
    private AuthorMapper mapper;

    @BeforeEach
    void setUp() {
        countryRepoDouble = mock(ICountryRepository.class);
        mapper = new AuthorMapper(countryRepoDouble);
    }

    // -------------------------------------------------------------------------
    // toDTO()
    // -------------------------------------------------------------------------

    @Test
    void toDTOShouldReturnAuthorDTOWhenAuthorIsValid() {

        // Arrange
        Author authorDouble = mock(Author.class);
        AuthorId authorIdDouble = mock(AuthorId.class);
        CountryId countryIdDouble = mock(CountryId.class);
        Country countryDouble = mock(Country.class);

        when(authorIdDouble.getId()).thenReturn("some-uuid");
        when(authorDouble.id()).thenReturn(authorIdDouble);
        when(authorDouble.getName()).thenReturn("José Saramago");
        when(authorDouble.getCountryID()).thenReturn(countryIdDouble);
        when(authorDouble.getDateOfBirth()).thenReturn(LocalDate.of(1922, 11, 16));
        when(authorDouble.getBiography()).thenReturn("Portuguese Nobel Prize-winning author.");

        when(countryIdDouble.getCountryID()).thenReturn("PORTUGAL");
        when(countryRepoDouble.findByID(countryIdDouble)).thenReturn(Optional.of(countryDouble));
        when(countryDouble.getCountryName()).thenReturn("Portugal");

        // Act
        AuthorDTO result = mapper.toDTO(authorDouble);

        // Assert
        assertEquals("some-uuid", result.getId());
        assertEquals("José Saramago", result.getName());
        assertEquals("Portugal", result.getCountryName());
        assertEquals("1922-11-16", result.getDateOfBirth());
        assertEquals("Portuguese Nobel Prize-winning author.", result.getBiography());
    }

    @Test
    void toDTOShouldThrowExceptionWhenAuthorIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> mapper.toDTO(null));
    }

    @Test
    void toDTOShouldThrowExceptionWhenCountryNotFound() {

        // Arrange
        Author authorDouble = mock(Author.class);
        CountryId countryIdDouble = mock(CountryId.class);

        when(authorDouble.getCountryID()).thenReturn(countryIdDouble);
        when(countryIdDouble.getCountryID()).thenReturn("UNKNOWN");
        when(countryRepoDouble.findByID(countryIdDouble)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> mapper.toDTO(authorDouble));
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

        CountryId countryIdDouble = mock(CountryId.class);
        Country countryDouble = mock(Country.class);

        when(idDouble1.getId()).thenReturn("uuid-1");
        when(idDouble2.getId()).thenReturn("uuid-2");

        when(authorDouble1.id()).thenReturn(idDouble1);
        when(authorDouble2.id()).thenReturn(idDouble2);

        when(authorDouble1.getName()).thenReturn("José Saramago");
        when(authorDouble2.getName()).thenReturn("Fernando Pessoa");

        when(authorDouble1.getCountryID()).thenReturn(countryIdDouble);
        when(authorDouble2.getCountryID()).thenReturn(countryIdDouble);

        when(authorDouble1.getDateOfBirth()).thenReturn(LocalDate.of(1922, 11, 16));
        when(authorDouble2.getDateOfBirth()).thenReturn(LocalDate.of(1888, 6, 13));

        when(authorDouble1.getBiography()).thenReturn("Nobel Prize-winning author.");
        when(authorDouble2.getBiography()).thenReturn("Portuguese modernist poet.");

        when(countryIdDouble.getCountryID()).thenReturn("PORTUGAL");
        when(countryRepoDouble.findByID(countryIdDouble)).thenReturn(Optional.of(countryDouble));
        when(countryDouble.getCountryName()).thenReturn("Portugal");

        // Act
        List<AuthorDTO> result = mapper.toDTOs(List.of(authorDouble1, authorDouble2));

        // Assert
        assertEquals(2, result.size());
        assertEquals("uuid-1", result.get(0).getId());
        assertEquals("uuid-2", result.get(1).getId());
    }

    @Test
    void toDTOsShouldReturnEmptyListWhenInputIsEmpty() {

        // Act
        List<AuthorDTO> result = mapper.toDTOs(List.of());

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void toDTOsShouldThrowExceptionWhenListIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> mapper.toDTOs(null));
    }
}