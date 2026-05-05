package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.CountryId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class AuthorDMTest {

    @Test
    void EmptyConstructor() {
        // arrange + act
        AuthorDM result = new AuthorDM();

        //assert
        assertNotNull(result);
    }

    @Test
    void shouldCreateAuthorDM_whenAllFieldsAreValid() {
        // arrange
        AuthorId authorIdDOuble = mock(AuthorId.class);
        CountryId countryIdDouble = mock(CountryId.class);
        String name = "Ken Follett";
        LocalDate birthday = LocalDate.of(1952, 10,3);
        String biography = "English novelist";

        // act
        AuthorDM result = new AuthorDM(authorIdDOuble, name, countryIdDouble, birthday, biography);

        // assert
        assertNotNull(result);
    }

    @Test
    void getId_shouldReturnAuthorId() {
        AuthorId authorIdDouble = mock(AuthorId.class);
        CountryId countryIdDouble = mock(CountryId.class);

        AuthorDM result = new AuthorDM(
                authorIdDouble,
                "Ken Follett",
                countryIdDouble,
                LocalDate.of(1952, 10, 3),
                "English novelist"
        );

        assertSame(authorIdDouble, result.getId());
    }

    @Test
    void getName_shouldReturnName() {
        AuthorId authorIdDouble = mock(AuthorId.class);
        CountryId countryIdDouble = mock(CountryId.class);
        String name = "Ken Follett";

        AuthorDM result = new AuthorDM(
                authorIdDouble,
                name,
                countryIdDouble,
                LocalDate.of(1952, 10, 3),
                "English novelist"
        );

        assertEquals(name, result.getName());
    }

    @Test
    void getCountryId_shouldReturnCountryId() {
        AuthorId authorIdDouble = mock(AuthorId.class);
        CountryId countryIdDouble = mock(CountryId.class);

        AuthorDM result = new AuthorDM(
                authorIdDouble,
                "Ken Follett",
                countryIdDouble,
                LocalDate.of(1952, 10, 3),
                "English novelist"
        );

        assertSame(countryIdDouble, result.getCountryId());
    }

    @Test
    void getDateOfBirth_shouldReturnDateOfBirth() {
        AuthorId authorIdDouble = mock(AuthorId.class);
        CountryId countryIdDouble = mock(CountryId.class);
        LocalDate birthday = LocalDate.of(1952, 10, 3);

        AuthorDM result = new AuthorDM(
                authorIdDouble,
                "Ken Follett",
                countryIdDouble,
                birthday,
                "English novelist"
        );

        assertEquals(birthday, result.getDateOfBirth());
    }

    @Test
    void getBiography_shouldReturnBiography() {
        AuthorId authorIdDouble = mock(AuthorId.class);
        CountryId countryIdDouble = mock(CountryId.class);
        String biography = "English novelist";

        AuthorDM result = new AuthorDM(
                authorIdDouble,
                "Ken Follett",
                countryIdDouble,
                LocalDate.of(1952, 10, 3),
                biography
        );

        assertEquals(biography, result.getBiography());
    }





    @ParameterizedTest
    @MethodSource("provideInvalidAuthorDMArguments")
    void constructor_shouldThrowException_whenAnyFieldIsNull(
            AuthorId id,
            String name,
            CountryId countryId,
            LocalDate dateOfBirth,
            String biography
    ) {
        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new AuthorDM(id, name, countryId, dateOfBirth, biography)
        );

        assertEquals("All fields must not be null.", exception.getMessage());
    }

    private static Stream<Arguments> provideInvalidAuthorDMArguments() {
        AuthorId validId = mock(AuthorId.class);
        CountryId validCountryId = mock(CountryId.class);
        LocalDate validDateOfBirth = LocalDate.of(1903, 6, 25);

        String validName = "Ken Follett";
        String validBiography = "English novelist.";

        return Stream.of(
                Arguments.of(null, validName, validCountryId, validDateOfBirth, validBiography),
                Arguments.of(validId, null, validCountryId, validDateOfBirth, validBiography),
                Arguments.of(validId, validName, null, validDateOfBirth, validBiography),
                Arguments.of(validId, validName, validCountryId, null, validBiography),
                Arguments.of(validId, validName, validCountryId, validDateOfBirth, null)
        );
    }

    @Test
    void shouldThrow_whenIdIsNull() {
        //arrange
        CountryId countryIdDouble = mock(CountryId.class);
        String name = "Ken Follett";
        LocalDate birthday = LocalDate.of(1952, 10,3);
        String biography = "English novelist";

        // act + assert
        IllegalArgumentException ex =  assertThrows(IllegalArgumentException.class, () -> new AuthorDM(null, name, countryIdDouble, birthday, biography));

        assertEquals("All fields must not be null.", ex.getMessage());

    }

}