package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Code} class.
 *
 * <p>A {@code Code} represents the identification code of a publication.
 * It supports three standard formats:
 * <ul>
 *   <li><b>ISBN-10</b> — 10 characters, used in older books</li>
 *   <li><b>ISBN-13</b> — 13 digits, the current standard for books</li>
 *   <li><b>ISSN</b> — 8 characters, used for magazines and journals</li>
 * </ul>
 *
 * <p>Each instance holds exactly one type — the other two fields are always {@code null}.
 * Objects are created through static factory methods ({@code fromIsbn10}, {@code fromIsbn13},
 * {@code fromIssn}), which validate the format before creating anything.
 *
 * <p>Tests are grouped by scenario:
 * <ul>
 *   <li>Valid codes without hyphens — basic happy path for each type</li>
 *   <li>Valid codes with hyphens — hyphens must be stripped before validation</li>
 *   <li>Errors: null or blank — missing input must be rejected</li>
 *   <li>Errors: wrong length — too short or too long must be rejected</li>
 *   <li>Errors: invalid characters — letters in wrong positions must be rejected</li>
 *   <li>Special valid cases — 'X' as last character, exact boundary length</li>
 *   <li>equals — same value must mean equal objects</li>
 *   <li>toString — must return whichever field is set</li>
 * </ul>
 */
class CodeTest {


    // --- Valid codes without hyphens ---

    // The basic happy path for each code type.
    // We also check that only the correct field is set — the other two must be null.
    // This confirms that each Code instance holds exactly one type, never two at the same time.

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

    // Real codes are often written with hyphens (e.g. "978-3-16-148410-0").
    // The class removes hyphens before checking length and format,
    // so inputs with hyphens must also be accepted.
    // The original value (with hyphens) is stored as-is — only validation uses the cleaned version.

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

    // A code that is null or blank has no value at all — it must be rejected immediately.
    // We test one case per code type to confirm each factory method performs this check.

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

    // Each code type has a fixed length (10, 13, or 8 after removing hyphens).
    // A code that is too short does not match the standard — it must be rejected.

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

    // A code that is too long also does not match the standard — it must be rejected.

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

    // The digits of each code must all be numbers (0-9).
    // Only the last character of ISBN-10 and ISSN can be 'X' — it is a valid check digit.
    // Any other letter anywhere must be rejected.

    @Test
    void shouldFailWhenIsbn10HasLetterInMiddle() {
        // Act & Assert — 'A' at position 6 is not a digit and is not the last character
        assertThrows(IllegalArgumentException.class, () ->
                Code.fromIsbn10("12345A7890")
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


    // --- Special valid cases ---

    // ISBN-10 and ISSN allow 'X' as the last character — it is a standard check digit.
    // We also confirm the boundary: an ISSN with exactly 8 characters must be accepted.

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

    // The equals method must compare codes by their value, not by memory address.
    // Two Code objects created with the same value must be equal.
    // A Code must not be equal to null or to an object of a different type.

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

    // toString must return whichever code type is stored in the instance.
    // Since only one field is non-null, it finds that field and returns its value.

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

}