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
 * <p>This test class verifies the validation behavior of the request DTO
 * used to register a new country in the system.</p>
 *
 * <p>The DTO is a simple record enriched with Bean Validation constraints,
 * so tests focus on validation rules rather than business logic.</p>
 *
 * <p>Specifically, these tests ensure that invalid input values are properly
 * rejected according to the constraints defined on the DTO fields.</p>
 *
 * <p>Validation is performed using Jakarta Bean Validation and a
 * {@link Validator} instance created from {@link ValidatorFactory}.</p>
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