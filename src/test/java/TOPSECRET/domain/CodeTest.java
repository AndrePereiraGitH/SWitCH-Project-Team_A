package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Code}.
 * Code has no injected dependencies, so no mocks are needed — direct object creation is used.
 * Objects are created through static factory methods: {@code fromIsbn10}, {@code fromIsbn13}, {@code fromIssn}.
 */
class CodeTest {


    // --- Valid codes without hyphens ---

    // Happy path for each type. Only the matching field must be set — the other two must be null.

    @Test
    void shouldCreateCodeWithIsbn10() {
        // Arrange & Act
        Code code = Code.fromIsbn10("1234567890");

        // Assert — isbn10 is set, the other two must be null
        assertEquals("1234567890", code.getIsbn10());
        assertNull(code.getIsbn13());
        assertNull(code.getIssn());
    }

    @Test
    void shouldCreateCodeWithIsbn13() {
        // Arrange & Act
        Code code = Code.fromIsbn13("9781234567897");

        // Assert — isbn13 is set, the other two must be null
        assertNull(code.getIsbn10());
        assertEquals("9781234567897", code.getIsbn13());
        assertNull(code.getIssn());
    }

    @Test
    void shouldCreateCodeWithIssn() {
        // Arrange & Act
        Code code = Code.fromIssn("20493630");

        // Assert — issn is set, the other two must be null
        assertNull(code.getIsbn10());
        assertNull(code.getIsbn13());
        assertEquals("20493630", code.getIssn());
    }


    // --- Valid codes with hyphens ---

    // Hyphens are removed before validation, so codes with hyphens must also be accepted.
    // The original value (with hyphens) is what gets stored.

    @Test
    void shouldAcceptIsbn10WithHyphens() {
        // Arrange & Act
        Code code = Code.fromIsbn10("0-306-40615-2");

        // Assert — the original string including hyphens is stored
        assertEquals("0-306-40615-2", code.getIsbn10());
    }

    @Test
    void shouldAcceptIsbn13WithHyphens() {
        // Arrange & Act
        Code code = Code.fromIsbn13("978-3-16-148410-0");

        // Assert — the original string including hyphens is stored
        assertEquals("978-3-16-148410-0", code.getIsbn13());
    }

    @Test
    void shouldAcceptIssnWithHyphen() {
        // Arrange & Act
        Code code = Code.fromIssn("2049-3630");

        // Assert — the original string including hyphen is stored
        assertEquals("2049-3630", code.getIssn());
    }


    // --- Errors: null or blank values ---

    // Null or blank means there is no code at all — each factory method must reject it.

    @Test
    void shouldFailWhenIsbn10IsNull() {
        // Act & Assert — null must throw before any format check runs
        assertThrows(IllegalArgumentException.class, () ->
                Code.fromIsbn10(null)
        );
    }

    @Test
    void shouldFailWhenIsbn13IsBlank() {
        // Act & Assert — a string with only spaces has no real value
        assertThrows(IllegalArgumentException.class, () ->
                Code.fromIsbn13(" ")
        );
    }

    @Test
    void shouldFailWhenIssnIsBlank() {
        // Act & Assert — an empty string is also blank
        assertThrows(IllegalArgumentException.class, () ->
                Code.fromIssn("")
        );
    }


    // --- Errors: too few characters ---

    // Fixed lengths: ISBN-10 = 10, ISBN-13 = 13, ISSN = 8. Too short must be rejected.

    @Test
    void shouldFailWhenIsbn10IsTooShort() {
        // Act & Assert — "123" has only 3 characters, ISBN-10 needs exactly 10
        assertThrows(IllegalArgumentException.class, () ->
                Code.fromIsbn10("123")
        );
    }

    @Test
    void shouldFailWhenIsbn13IsTooShort() {
        // Act & Assert — "123" has only 3 characters, ISBN-13 needs exactly 13
        assertThrows(IllegalArgumentException.class, () ->
                Code.fromIsbn13("123")
        );
    }

    @Test
    void shouldFailWhenIssnIsTooShort() {
        // Act & Assert — "1234" has only 4 characters, ISSN needs exactly 8
        assertThrows(IllegalArgumentException.class, () ->
                Code.fromIssn("1234")
        );
    }


    // --- Errors: too many characters ---

    // Too long must also be rejected.

    @Test
    void shouldFailWhenIsbn10IsTooLong() {
        // Act & Assert — 14 characters, ISBN-10 needs exactly 10
        assertThrows(IllegalArgumentException.class, () ->
                Code.fromIsbn10("12345678901234")
        );
    }

    @Test
    void shouldFailWhenIsbn13IsTooLong() {
        // Act & Assert — 17 characters, ISBN-13 needs exactly 13
        assertThrows(IllegalArgumentException.class, () ->
                Code.fromIsbn13("97812345678901234")
        );
    }

    @Test
    void shouldFailWhenIssnIsTooLong() {
        // Act & Assert — 9 characters, ISSN needs exactly 8
        assertThrows(IllegalArgumentException.class, () ->
                Code.fromIssn("123456789")
        );
    }


    // --- Errors: invalid characters ---

    // All digits must be numbers. Only the last char of ISBN-10 and ISSN can be 'X'.

    @Test
    void shouldFailWhenIsbn10HasLetterInMiddle() {
        // Act & Assert — 'A' at position 6 is not a digit and is not the last character
        assertThrows(IllegalArgumentException.class, () ->
                Code.fromIsbn10("12345A7890")
        );
    }

    @Test
    void shouldFailWhenIsbn10HasLetterAtPosition9() {
        // Act & Assert — 'A' at index 8 (9th char) targets the loop boundary i < 9
        assertThrows(IllegalArgumentException.class, () ->
                Code.fromIsbn10("12345678A0")
        );
    }

    @Test
    void shouldFailWhenIsbn10EndsWithInvalidLetter() {
        // Act & Assert — the last character can only be a digit or 'X', not 'A'
        assertThrows(IllegalArgumentException.class, () ->
                Code.fromIsbn10("123456789A")
        );
    }

    @Test
    void shouldFailWhenIsbn13HasLetters() {
        // Act & Assert — ISBN-13 allows no letters at all, not even 'X'
        assertThrows(IllegalArgumentException.class, () ->
                Code.fromIsbn13("97812345A7890")
        );
    }

    @Test
    void shouldFailWhenIssnHasLetterInMiddle() {
        // Act & Assert — 'A' in the middle is always invalid
        assertThrows(IllegalArgumentException.class, () ->
                Code.fromIssn("2049A630")
        );
    }

    @Test
    void shouldFailWhenIssnEndsWithInvalidChar() {
        // Act & Assert — last char must be a digit or 'X', anything else is invalid
        assertThrows(IllegalArgumentException.class, () ->
                Code.fromIssn("2049363A")
        );
    }


    // --- Special valid cases ---

    // 'X' as last character is valid in ISBN-10 and ISSN (standard check digit).

    @Test
    void shouldAcceptIssnEndingWithX() {
        // Arrange & Act — 'X' is valid as the last character of an ISSN
        Code code = Code.fromIssn("2049363X");

        // Assert
        assertEquals("2049363X", code.getIssn());
    }

    @Test
    void shouldAcceptIssnWithExactly8Chars() {
        // Arrange & Act — 8 characters is the exact required length for ISSN
        Code code = Code.fromIssn("12345678");

        // Assert
        assertEquals("12345678", code.getIssn());
    }


    // --- equals tests ---

    // equals compares field values, not memory addresses.

    @Test
    void equals_shouldBeFalse_whenNull() {
        // Act & Assert — a code is never equal to null
        Code code = Code.fromIsbn13("9783161484100");
        assertNotEquals(null, code);
    }

    @Test
    void equals_shouldBeFalse_whenDifferentClass() {
        // Act & Assert — a Code is not equal to a plain String
        Code code = Code.fromIsbn13("9783161484100");
        assertNotEquals("not a code", code);
    }

    @Test
    void equals_shouldBeTrue_whenSameIsbn13() {
        // Arrange — two separate objects with the same ISBN-13 value
        Code a = Code.fromIsbn13("9783161484100");
        Code b = Code.fromIsbn13("9783161484100");

        // Assert — must be equal in both directions (a==b and b==a)
        assertEquals(a, b);
        assertEquals(b, a);
    }

    @Test
    void equals_shouldBeFalse_whenDifferentIsbn13() {
        // Arrange — two codes that differ by one digit
        Code a = Code.fromIsbn13("9783161484100");
        Code b = Code.fromIsbn13("9783161484101");

        // Assert — different value means not equal
        assertNotEquals(a, b);
    }

    @Test
    void equals_shouldBeFalse_whenDifferentIssn() {
        // Arrange — two ISSN codes that differ by one digit
        Code a = Code.fromIssn("20493630");
        Code b = Code.fromIssn("20493631");

        // Assert
        assertNotEquals(a, b);
    }


    // --- toString tests ---

    // Must return the value of whichever field is set.

    @Test
    void toString_shouldReturnIsbn10_whenIsbn10Exists() {
        // Act & Assert
        Code code = Code.fromIsbn10("1234567890");
        assertEquals("1234567890", code.toString());
    }

    @Test
    void toString_shouldReturnIsbn13_whenIsbn13Exists() {
        // Act & Assert
        Code code = Code.fromIsbn13("9783161484100");
        assertEquals("9783161484100", code.toString());
    }

    @Test
    void toString_shouldReturnIssn_whenIssnExists() {
        // Act & Assert
        Code code = Code.fromIssn("20493630");
        assertEquals("20493630", code.toString());
    }

    @Test
    void toString_shouldReturnNoCode_whenAllFieldsAreNull() {
        // Act & Assert — constructor with all nulls hits the "No Code" fallback
        Code code = new Code(null, null, null);
        assertEquals("No Code", code.toString());
    }


    // --- equals edge cases ---

    @Test
    void equals_shouldBeFalse_whenComparedDirectlyToNull() {
        // Act & Assert — calling equals(null) on the code itself must return false
        Code code = Code.fromIsbn13("9783161484100");
        assertFalse(code.equals(null));
    }

    @Test
    void equals_shouldBeFalse_whenComparedDirectlyToDifferentClass() {
        // Act & Assert — calling equals with a String must return false
        Code code = Code.fromIsbn13("9783161484100");
        assertFalse(code.equals("9783161484100"));
    }


    // --- hashCode ---

    @Test
    void hashCode_shouldBeEqualForEqualCodes() {
        // Arrange
        Code a = Code.fromIsbn13("9783161484100");
        Code b = Code.fromIsbn13("9783161484100");

        // Assert — equal objects must have the same hash
        assertEquals(a.hashCode(), b.hashCode());
    }

}