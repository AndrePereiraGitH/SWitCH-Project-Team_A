package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Dimensions} value object.
 * <p>
 * This test suite validates:
 * <ul>
 *     <li>Successful creation of {@code Dimensions} when all values are strictly positive.</li>
 *     <li>Rejection of zero or negative values through {@link IllegalArgumentException}.</li>
 *     <li>Correct behavior of getter methods.</li>
 *     <li>Correct implementation of the {@code equals} method,
 *     including reflexivity, symmetry, null comparison and value comparison.</li>
 * </ul>
 * <p>
 * Parameterized tests are used to validate multiple boundary and invalid cases,
 * ensuring robustness and compliance with domain invariants.
 */

class DimensionsTest {

    @Test
    void shouldReturnCorrectDimensions() {
        // Arrange
        double height = 10.0;
        double width = 0.2;
        double depth = 5.2;

        // Act
        Dimensions dimensions = new Dimensions(height, width, depth);

        // Assert
        assertEquals(height, dimensions.getHeight(), 0.0);
        assertEquals(width, dimensions.getWidth(), 0.0);
        assertEquals(depth, dimensions.getDepth(), 0.0);
    }

// -------------------------
// PARAMETERIZED TESTS
// -------------------------
    static Stream<Arguments> validDimensionsProvider() {
        return Stream.of(
                // normal positive values
                Arguments.of(1.0, 1.0, 1.0),
                Arguments.of(3.5, 5.2, 15.1)
        );
    }

    static Stream<Arguments> invalidDimensionsProvider() {
        return Stream.of(
                // boundary at zero
                Arguments.of(0.0, 1.0, 1.0),
                Arguments.of(1.0, 0.0, 1.0),
                Arguments.of(1.0, 1.0, 0.0),

                // all zero
                Arguments.of(0.0, 0.0, 0.0),

                // negatives
                Arguments.of(-1.0, 1.0, 1.0),
                Arguments.of(1.0, -1.0, 1.0),
                Arguments.of(1.0, 1.0, -1.0),

                // mixed invalid values
                Arguments.of(0.0, -1.0, 1.0),
                Arguments.of(-1.0, 0.0, 1.0),
                Arguments.of(1.0, 0.0, -1.0),
                Arguments.of(-1.0, -1.0, 1.0),
                Arguments.of(1.0, -1.0, -1.0)
        );
    }

    @ParameterizedTest
    @MethodSource("validDimensionsProvider")
    void shouldCreateDimensionsWhenAllValuesArePositive(
            // Arrange
            double height,
            double width,
            double depth
    ) {
        // Act
        Dimensions dimensions = new Dimensions(height, width, depth);

        // Assert
        assertEquals(height, dimensions.getHeight(), 0.01);
        assertEquals(width, dimensions.getWidth(), 0.01);
        assertEquals(depth, dimensions.getDepth(), 0.01);
    }

    @ParameterizedTest
    @MethodSource("invalidDimensionsProvider")
    void shouldThrowExceptionWhenAnyValueIsZeroOrNegative(
            double height,
            double width,
            double depth
    ) {
        // Act & Assert
        assertThrows(
                IllegalArgumentException.class, () -> new Dimensions(height, width, depth)
        );
    }


// -------------------------
// equals
// -------------------------

    @Test
    void equals_shouldReturnFalse_whenOtherIsNull() {
        // Arrange
        Dimensions dimensions = new Dimensions(10.0, 0.2, 5.2);

        // Act
        boolean result = dimensions.equals(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_shouldReturnFalse_whenOtherIsDifferentClass() {
        // Arrange
        Dimensions dimensions = new Dimensions(10.0, 0.2, 5.2);

        // Act
        boolean result = dimensions.equals("not dimensions");

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_shouldReturnTrue_whenSameValues() {
        // Arrange
        Dimensions dimensions = new Dimensions(10.0, 0.2, 5.2);
        Dimensions dimensions2 = new Dimensions(10.0, 0.2, 5.2);

        // Act
        boolean result1 = dimensions.equals(dimensions2);
        boolean result2 = dimensions2.equals(dimensions);

        // Assert
        assertTrue(result1);
        assertTrue(result2);
    }

    @Test
    void equals_shouldReturnFalse_whenHeightDiffers() {
        // Arrange
        Dimensions dimensions = new Dimensions(10.0, 0.2, 5.2);
        Dimensions dimensions2 = new Dimensions(99.0, 0.2, 5.2);

        // Act
        boolean result = dimensions.equals(dimensions2);

        // Assert
        assertFalse(result);
    }
}

