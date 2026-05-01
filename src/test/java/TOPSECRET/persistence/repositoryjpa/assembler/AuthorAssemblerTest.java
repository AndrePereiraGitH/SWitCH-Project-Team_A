package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.Author.AuthorFactory;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.CountryID;
import TOPSECRET.persistence.repositoryjpa.datamodel.AuthorDM;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link AuthorAssembler}.
 *
 * <p>
 * Verifies the conversion between {@link Author} domain objects and
 * {@link AuthorDM} JPA data models, including null validation.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class AuthorAssemblerTest {

    @InjectMocks
    private AuthorAssembler assembler;

    @Mock
    private AuthorFactory authorFactory;

    @Mock
    private Author authorDouble;

    @Mock
    private AuthorId authorIdDouble;

    @Mock
    private CountryID countryIdDouble;

    private static final String name = "Ken Follett";
    private static final LocalDate dateOfBirth = LocalDate.of(1952, 10, 3);
    private static final String biography = "English novelist";


    // =======================================
    // ------ TO DM TESTs --------
    // =======================================

    @Test
    void toDM_shouldReturnAuthorDMWithSameId_whenAuthorIsValid() {
        // Arrange
        when(authorDouble.id()).thenReturn(authorIdDouble);
        when(authorDouble.getName()).thenReturn(name);
        when(authorDouble.getCountryID()).thenReturn(countryIdDouble);
        when(authorDouble.getDateOfBirth()).thenReturn(dateOfBirth);
        when(authorDouble.getBiography()).thenReturn(biography);

        // Act
        AuthorDM result = assembler.toDM(authorDouble);

        // Assert
        assertSame(authorIdDouble, result.getId());
    }

    @Test
    void toDM_shouldReturnAuthorDMWithSameName_whenAuthorIsValid() {
        // Arrange
        when(authorDouble.id()).thenReturn(authorIdDouble);
        when(authorDouble.getName()).thenReturn(name);
        when(authorDouble.getCountryID()).thenReturn(countryIdDouble);
        when(authorDouble.getDateOfBirth()).thenReturn(dateOfBirth);
        when(authorDouble.getBiography()).thenReturn(biography);

        // Act
        AuthorDM result = assembler.toDM(authorDouble);

        // Assert
        assertEquals(name, result.getName());
    }

    @Test
    void toDM_shouldReturnAuthorDMWithSameCountryId_whenAuthorIsValid() {
        // Arrange
        when(authorDouble.id()).thenReturn(authorIdDouble);
        when(authorDouble.getName()).thenReturn(name);
        when(authorDouble.getCountryID()).thenReturn(countryIdDouble);
        when(authorDouble.getDateOfBirth()).thenReturn(dateOfBirth);
        when(authorDouble.getBiography()).thenReturn(biography);

        // Act
        AuthorDM result = assembler.toDM(authorDouble);

        // Assert
        assertSame(countryIdDouble, result.getCountryId());
    }

    @Test
    void toDM_shouldReturnAuthorDMWithSameDateOfBirth_whenAuthorIsValid() {
        // Arrange
        when(authorDouble.id()).thenReturn(authorIdDouble);
        when(authorDouble.getName()).thenReturn(name);
        when(authorDouble.getCountryID()).thenReturn(countryIdDouble);
        when(authorDouble.getDateOfBirth()).thenReturn(dateOfBirth);
        when(authorDouble.getBiography()).thenReturn(biography);

        // Act
        AuthorDM result = assembler.toDM(authorDouble);

        // Assert
        assertEquals(dateOfBirth, result.getDateOfBirth());
    }

    @Test
    void toDM_shouldReturnAuthorDMWithSameBiography_whenAuthorIsValid() {
        // Arrange
        when(authorDouble.id()).thenReturn(authorIdDouble);
        when(authorDouble.getName()).thenReturn(name);
        when(authorDouble.getCountryID()).thenReturn(countryIdDouble);
        when(authorDouble.getDateOfBirth()).thenReturn(dateOfBirth);
        when(authorDouble.getBiography()).thenReturn(biography);

        // Act
        AuthorDM result = assembler.toDM(authorDouble);

        // Assert
        assertEquals(biography, result.getBiography());
    }

    @Test
    void toDM_shouldThrowException_whenAuthorIsNull() {
        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> assembler.toDM(null)
        );

        assertEquals("Author cannot be null", exception.getMessage());
    }

    // =======================================
    // ------ To Domain TESTs --------
    // =======================================

    @Test
    void toDomain_shouldReturnAuthor_whenAuthorDMIsValid() {
        // Arrange
        AuthorDM authorDM = new AuthorDM(
                authorIdDouble,
                name,
                countryIdDouble,
                dateOfBirth,
                biography
        );

        when(authorFactory.create(
                authorIdDouble,
                name,
                countryIdDouble,
                dateOfBirth,
                biography
        )).thenReturn(authorDouble);

        // Act
        Author result = assembler.toDomain(authorDM);

        // Assert
        assertSame(authorDouble, result);
    }

    @Test
    void toDomain_shouldThrowException_whenAuthorDMIsNull() {
        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> assembler.toDomain(null)
        );

        assertEquals("authorDM cannot be null", exception.getMessage());
    }

}