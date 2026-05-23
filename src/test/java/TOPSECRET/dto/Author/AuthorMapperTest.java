package TOPSECRET.dto.Author;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.AuthorFactory;
import TOPSECRET.domain.Country.Country;
import TOPSECRET.domain.Country.ICountryRepository;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.CountryId;
import TOPSECRET.dto.Author.request.CreateAuthorDTO;
import TOPSECRET.dto.Author.response.AuthorDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AuthorMapper}.
 *
 * <p>This test class verifies the correct mapping behavior between
 * {@link CreateAuthorDTO} request objects, {@link Author} domain objects,
 * and {@link AuthorDTO} response objects.</p>
 *
 * <p>The tests validate:</p>
 * <ul>
 *     <li>Conversion of a request DTO into a domain object</li>
 *     <li>Conversion of a domain object into a response DTO</li>
 *     <li>Correct transfer of field values</li>
 *     <li>Validation of null arguments</li>
 *     <li>Handling of invalid country IDs</li>
 *     <li>Handling of invalid date formats</li>
 * </ul>
 *
 * <p>Mockito is used to isolate the mapper from domain object implementations.</p>
 */
class AuthorMapperTest {

    private ICountryRepository countryRepoDouble;
    private AuthorFactory authorFactoryDouble;
    private AuthorMapper mapper;

    @BeforeEach
    void setUp() {
        countryRepoDouble = mock(ICountryRepository.class);
        authorFactoryDouble = mock(AuthorFactory.class);
        mapper = new AuthorMapper(countryRepoDouble, authorFactoryDouble);
    }

    // -------------------------------------------------------------------------
    // toDomain()
    // -------------------------------------------------------------------------

    @Test
    void toDomainShouldReturnAuthorWhenDTOIsValid() {

        // Arrange
        CreateAuthorDTO dto = new CreateAuthorDTO(
                "José Saramago",
                "PORTUGAL",
                "1922-11-16",
                "Portuguese Nobel Prize-winning author."
        );

        CountryId countryIdDouble = mock(CountryId.class);
        Country countryDouble = mock(Country.class);
        Author authorDouble = mock(Author.class);

        when(countryRepoDouble.findByID(any(CountryId.class)))
                .thenReturn(Optional.of(countryDouble));
        when(authorFactoryDouble.create(
                eq("José Saramago"),
                any(CountryId.class),
                eq(LocalDate.of(1922, 11, 16)),
                eq("Portuguese Nobel Prize-winning author.")))
                .thenReturn(authorDouble);

        // Act
        Author result = mapper.toDomain(dto);

        // Assert
        assertEquals(authorDouble, result);
    }

    @Test
    void toDomainShouldThrowExceptionWhenDTOIsNull() {

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> mapper.toDomain(null));
    }

    @Test
    void toDomainShouldThrowExceptionWhenCountryIdIsInvalid() {

        // Arrange
        CreateAuthorDTO dto = new CreateAuthorDTO(
                "José Saramago",
                "INVALID_COUNTRY",
                "1922-11-16",
                "Portuguese Nobel Prize-winning author."
        );

        when(countryRepoDouble.findByID(any(CountryId.class)))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> mapper.toDomain(dto));
    }

    @Test
    void toDomainShouldThrowExceptionWhenDateOfBirthIsInvalidFormat() {

        // Arrange
        CreateAuthorDTO dto = new CreateAuthorDTO(
                "José Saramago",
                "PORTUGAL",
                "16-11-1922",
                "Portuguese Nobel Prize-winning author."
        );

        when(countryRepoDouble.findByID(any(CountryId.class)))
                .thenReturn(Optional.of(mock(Country.class)));

        // Act + Assert
        assertThrows(Exception.class,
                () -> mapper.toDomain(dto));
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
}
