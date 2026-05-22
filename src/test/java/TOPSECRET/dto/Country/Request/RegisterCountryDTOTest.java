package TOPSECRET.dto.Country.Request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

/**
 * Unit tests for {@link RegisterCountryDTO}.
 *
 * <p>This test class verifies the behavior of the request DTO used for registering a country.</p>
 *
 * <p>Since this is a simple record-based DTO, tests focus on:</p>
 * <ul>
 *     <li>Correct value storage and retrieval</li>
 *     <li>Record-generated equality and hashCode behavior</li>
 *     <li>Validation constraints applied to the country name field</li>
 * </ul>
 *
 * <p>Validation is handled via Jakarta Bean Validation annotations and verified using a {@link Validator}.</p>
 */
class RegisterCountryDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        // Arrange
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldCreateDTOWithValidCountryName() {

        // Arrange
        RegisterCountryDTO dto = new RegisterCountryDTO("Portugal");

        // Act
        String result = dto.countryName();

        // Assert
        assertEquals("Portugal", result);
    }

    @Test
    void shouldSupportRecordEquality() {

        // Arrange
        RegisterCountryDTO dto1 = new RegisterCountryDTO("Portugal");
        RegisterCountryDTO dto2 = new RegisterCountryDTO("Portugal");

        // Act
        boolean result = dto1.equals(dto2);

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldSupportRecordHashCode() {

        // Arrange
        RegisterCountryDTO dto1 = new RegisterCountryDTO("Portugal");
        RegisterCountryDTO dto2 = new RegisterCountryDTO("Portugal");

        // Act
        int hash1 = dto1.hashCode();
        int hash2 = dto2.hashCode();

        // Assert
        assertEquals(hash1, hash2);
    }

    @Test
    void shouldRejectNullCountryName() {

        // Arrange
        RegisterCountryDTO dto = new RegisterCountryDTO(null);

        // Act
        Set<ConstraintViolation<RegisterCountryDTO>> violations = validator.validate(dto);

        // Assert
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldRejectEmptyCountryName() {

        // Arrange
        RegisterCountryDTO dto = new RegisterCountryDTO("");

        // Act
        Set<ConstraintViolation<RegisterCountryDTO>> violations = validator.validate(dto);

        // Assert
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldRejectBlankCountryName() {

        // Arrange
        RegisterCountryDTO dto = new RegisterCountryDTO("   ");

        // Act
        Set<ConstraintViolation<RegisterCountryDTO>> violations = validator.validate(dto);

        // Assert
        assertFalse(violations.isEmpty());
    }
}