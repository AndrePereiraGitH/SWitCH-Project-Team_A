package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidatorTest {

    @Test
    void noArgs_returnsFalse() {
        assertFalse(Validator.isNullOrBlank());
    }

    @Test
    void allNonBlank_returnsFalse() {
        assertFalse(Validator.isNullOrBlank("a", "hello"));
    }

    @Test
    void containsNull_returnsTrue() {
        assertTrue(Validator.isNullOrBlank("a", null, "b"));
    }

    @Test
    void containsBlank_returnsTrue() {
        assertTrue(Validator.isNullOrBlank("a", "   ", "b"));
    }

    @Test
    void singleBlank_returnsTrue() {
        assertTrue(Validator.isNullOrBlank(""));
    }
}