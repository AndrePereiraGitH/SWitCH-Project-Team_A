// NEED TO CHANGE THE POM??

package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DimensionsTest {

    // ---------------- VALID CASES ----------------
    static Stream<Arguments> validDimensionsProvider() {
        return Stream.of(
                // normal positive values
                Arguments.of(1.0, 1.0, 1.0),
                Arguments.of(10.0, 0.2, 5.2),

                // just above zero
                Arguments.of(Double.MIN_VALUE, 1.0, 1.0),
                Arguments.of(1.0, Double.MIN_VALUE, 1.0),
                Arguments.of(1.0, 1.0, Double.MIN_VALUE)
        );
    }

    // ---------------- INVALID CASES ----------------
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

    // ---------------- PARAMETERIZED TESTS ----------------
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
        assertEquals(height, dimensions.getHeight(), 0.0);
        assertEquals(width, dimensions.getWidth(), 0.0);
        assertEquals(depth, dimensions.getDepth(), 0.0);
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

    // ---------------- SIMPLE GETTER TEST ----------------
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
}

