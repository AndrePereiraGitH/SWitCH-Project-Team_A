package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DimensionsTest {


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

    // Helper
    // -------------------------
    private Dimensions validDimensions() {
        return new Dimensions(10.0, 0.2, 5.2);
    }

    // -------------------------
    // equals
    // -------------------------

    @Test
    void equals_shouldReturnTrue_whenSameReference() {
        Dimensions d = validDimensions();
        assertEquals(d, d); // same object
    }

    @Test
    void equals_shouldReturnFalse_whenOtherIsNull() {
        Dimensions d = validDimensions();
        assertNotEquals(null, d);
    }

    @Test
    void equals_shouldReturnFalse_whenOtherIsDifferentClass() {
        Dimensions d = validDimensions();
        assertNotEquals("not dimensions", d);
    }

    @Test
    void equals_shouldReturnTrue_whenSameValues() {
        Dimensions a = new Dimensions(10.0, 0.2, 5.2);
        Dimensions b = new Dimensions(10.0, 0.2, 5.2);

        assertEquals(a, b);
        assertEquals(b, a); // symmetry
    }

    @Test
    void equals_shouldReturnFalse_whenHeightDiffers() {
        Dimensions a = validDimensions();
        Dimensions b = new Dimensions(99.0, 0.2, 5.2);

        assertNotEquals(a, b);
    }

    @Test
    void equals_shouldReturnFalse_whenWidthDiffers() {
        Dimensions a = validDimensions();
        Dimensions b = new Dimensions(10.0, 9.9, 5.2);

        assertNotEquals(a, b);
    }

    @Test
    void equals_shouldReturnFalse_whenDepthDiffers() {
        Dimensions a = validDimensions();
        Dimensions b = new Dimensions(10.0, 0.2, 99.9);

        assertNotEquals(a, b);
    }
}

