package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;



import static org.junit.jupiter.api.Assertions.*;

class PriceValidatorTest {



    @ParameterizedTest
    @CsvSource({
            "8.55",
            "10.14",
            "0.01",
            "100.9",
            "101",
            "5"
    })
    public void shouldReturnDoubleWhenPriceIsValid(double expectedPrice){
        double realPrice = PriceValidator.validatePrice(expectedPrice);

        assertEquals(expectedPrice,realPrice);
    }

    @ParameterizedTest
    @CsvSource({
            "8.555",
            "10.1234",
            "0.001",
            "100.999"
    })
    void shouldThrowExceptionWhenPriceHasMoreThanTwoDecimals(double invalidPrice) {
        assertThrows(IllegalArgumentException.class, () -> {
            PriceValidator.validatePrice(invalidPrice);
        });
    }

    @ParameterizedTest
    @CsvSource({
            "-8.55",
            "-10.12",
            "-0.01",
            "-100.9"
    })
    void shouldThrowExceptionWhenPriceIsNegative(double invalidPrice) {
        assertThrows(IllegalArgumentException.class, () -> {
            PriceValidator.validatePrice(invalidPrice);
        });
    }

    @ParameterizedTest
    @CsvSource({
            "-8.554",
            "-10.127",
            "-0.019",
            "-100.947"
    })
    void shouldThrowExceptionWhenPriceIsNegativeAndHasMoreThanTwoDecimals(double invalidPrice) {
        assertThrows(IllegalArgumentException.class, () -> {
            PriceValidator.validatePrice(invalidPrice);
        });
    }

    @Test
    void shouldThrowExceptionWhenPriceIsZero() {
        double invalidPrice = 0;
        assertThrows(IllegalArgumentException.class, () -> {
            PriceValidator.validatePrice(invalidPrice);
        });
    }

}