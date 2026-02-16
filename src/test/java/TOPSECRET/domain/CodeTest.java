package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CodeTest {


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
        Code code = Code.fromIssn("2049-3630");

        assertNull(code.getIsbn10());
        assertNull(code.getIsbn13());
        assertEquals("2049-3630", code.getIssn());
    }

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
                Code.fromIssn(" ")
        );
    }

    //-------------
    //--- Equals---
    //-------------

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