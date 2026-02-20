package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Code class.
 * These tests validate the creation of ISBN-10, ISBN-13 and ISSN codes,
 * including valid cases and error cases.
 */
class CodeTest {

    // Valid codes without hyphens

    @Test
    void shouldCreateCodeWithIsbn10() {
        Code code = Code.fromIsbn10("1234567890");

        assertEquals("1234567890", code.getIsbn10());
        assertNull(code.getIsbn13());
        assertNull(code.getIssn());
    }

    @Test
    void shouldCreateCodeWithIsbn13() {
        Code code = Code.fromIsbn13("9781234567897");

        assertNull(code.getIsbn10());
        assertEquals("9781234567897", code.getIsbn13());
        assertNull(code.getIssn());
    }

    @Test
    void shouldCreateCodeWithIssn() {
        Code code = Code.fromIssn("20493630");

        assertNull(code.getIsbn10());
        assertNull(code.getIsbn13());
        assertEquals("20493630", code.getIssn());
    }

    // Valid codes with hyphens

    @Test
    void shouldAcceptIsbn10WithHyphens() {
        Code code = Code.fromIsbn10("0-306-40615-2");

        assertEquals("0-306-40615-2", code.getIsbn10());
    }

    @Test
    void shouldAcceptIsbn13WithHyphens() {
        Code code = Code.fromIsbn13("978-3-16-148410-0");

        assertEquals("978-3-16-148410-0", code.getIsbn13());
    }

    @Test
    void shouldAcceptIssnWithHyphen() {
        Code code = Code.fromIssn("2049-3630");

        assertEquals("2049-3630", code.getIssn());
    }

    // Errors: null or blank values

    @Test
    void shouldFailWhenIsbn10IsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                Code.fromIsbn10(null)
        );
    }

    @Test
    void shouldFailWhenIsbn13IsBlank() {
        assertThrows(IllegalArgumentException.class, () ->
                Code.fromIsbn13(" ")
        );
    }

    @Test
    void shouldFailWhenIssnIsBlank() {
        assertThrows(IllegalArgumentException.class, () ->
                Code.fromIssn("")
        );
    }

    // Errors: too few characters

    @Test
    void shouldFailWhenIsbn10IsTooShort() {
        assertThrows(IllegalArgumentException.class, () ->
                Code.fromIsbn10("123")
        );
    }

    @Test
    void shouldFailWhenIsbn13IsTooShort() {
        assertThrows(IllegalArgumentException.class, () ->
                Code.fromIsbn13("123")
        );
    }

    @Test
    void shouldFailWhenIssnIsTooShort() {
        assertThrows(IllegalArgumentException.class, () ->
                Code.fromIssn("1234")
        );
    }

    // Errors: too many characters

    @Test
    void shouldFailWhenIsbn10IsTooLong() {
        assertThrows(IllegalArgumentException.class, () ->
                Code.fromIsbn10("12345678901234")
        );
    }

    @Test
    void shouldFailWhenIsbn13IsTooLong() {
        assertThrows(IllegalArgumentException.class, () ->
                Code.fromIsbn13("97812345678901234")
        );
    }

    @Test
    void shouldFailWhenIssnIsTooLong() {
        assertThrows(IllegalArgumentException.class, () ->
                Code.fromIssn("123456789")
        );
    }

    // Errors: invalid characters

    @Test
    void shouldFailWhenIsbn10HasLetterInMiddle() {
        assertThrows(IllegalArgumentException.class, () ->
                Code.fromIsbn10("12345A7890")
        );
    }

    @Test
    void shouldFailWhenIsbn10EndsWithInvalidLetter() {
        assertThrows(IllegalArgumentException.class, () ->
                Code.fromIsbn10("123456789A")
        );
    }

    @Test
    void shouldFailWhenIsbn13HasLetters() {
        assertThrows(IllegalArgumentException.class, () ->
                Code.fromIsbn13("97812345A7890")
        );
    }

    @Test
    void shouldFailWhenIssnHasLetterInMiddle() {
        assertThrows(IllegalArgumentException.class, () ->
                Code.fromIssn("2049A630")
        );
    }

    // Equals tests

    @Test
    void equals_shouldBeFalse_whenNull() {
        Code code = Code.fromIsbn13("9783161484100");
        assertNotEquals(null, code);
    }

    @Test
    void equals_shouldBeFalse_whenDifferentClass() {
        Code code = Code.fromIsbn13("9783161484100");
        assertNotEquals("not a code", code);
    }

    @Test
    void equals_shouldBeTrue_whenSameIsbn13() {
        Code a = Code.fromIsbn13("9783161484100");
        Code b = Code.fromIsbn13("9783161484100");

        assertEquals(a, b);
        assertEquals(b, a);
    }

    @Test
    void equals_shouldBeFalse_whenDifferentIsbn13() {
        Code a = Code.fromIsbn13("9783161484100");
        Code b = Code.fromIsbn13("9783161484101");

        assertNotEquals(a, b);
    }
}