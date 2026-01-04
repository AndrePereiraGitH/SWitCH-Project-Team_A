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

    @Test
    void shouldReturnDimensions() {
        Dimensions dimensions = new Dimensions(10.0, 0.20, 5.2);
        assertEquals(10.0, dimensions.getHeight());
        assertEquals(0.20, dimensions.getWidth());
        assertEquals(5.2, dimensions.getDepth());
    }

     static Stream<Arguments> invalidDimensionsProvider() {
        return Stream.of(
                Arguments.of(0.0, 0.0, 0.0),
                Arguments.of(1.0, 0.0, 0.0),
                Arguments.of(0.0, 1.0, 0.0),
                Arguments.of(0.0, 0.0, 1.0),
                Arguments.of(-5.0, 2.0, 1.0),
                Arguments.of(1.0, -2.0, 5.0),
                Arguments.of(3.4, 1.0, -6.0),
                Arguments.of(-5.0, -10.0, -30.0),
                Arguments.of(-1.0, 0.0, 0.0),
                Arguments.of(0.0, -1.0, 0.0),
                Arguments.of(0.0, 0.0, -1.0),
                Arguments.of(-5.0, 0.0, 0.0),
                Arguments.of(0.0, -2.0, 0.0),
                Arguments.of(0.0, 0.0, -6.0)

        );
    }

    @ParameterizedTest
    @MethodSource("invalidDimensionsProvider")
    void allInvalidShouldThrowException(double height, double width, double depth) {
        assertThrows(IllegalArgumentException.class, () -> new Dimensions(height, width, depth));
    }
}

