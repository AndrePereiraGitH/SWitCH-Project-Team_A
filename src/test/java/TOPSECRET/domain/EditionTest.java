package TOPSECRET.domain;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Edition} class.
 * <p>
 * This test suite validates:
 * <ul>
 *     <li>Constructor argument validation</li>
 *     <li>Correct creation of valid objects</li>
 *     <li>Correct behaviour of the {@code equals} method</li>
 * </ul>
 */

class EditionTest {

    // ─────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────

    private Edition validEdition() {
        return new Edition("1st", validCode(), validPhysicalAttributes(),
                validPublicationType(), validPublisher(), Locale.ENGLISH,
                LocalDate.of(2020, 1, 1));
    }

    private PublicationType validPublicationType() {
        return publicationTypeBook();
    }

    private Code validCode() {
        return codeWithIsbn13();
    }

    private Dimensions validDimensions() {
        return new Dimensions(20.0, 15.0, 3.0);
    }

    private PhysicalAttributes validPhysicalAttributes() {
        return new PhysicalAttributes(validDimensions(), 0.5, 200);
    }

    private Contact validContact() {
        return new Contact("912345678", "351", null, null);
    }

    private Publisher validPublisher() {
        return new Publisher("Editora Exemplo", "123456789", validContact());
    }

    private PublicationType publicationTypeBook() {
        return new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "Hardcover edition");
    }

    private PublicationType publicationTypeMagazine() {
        return new PublicationType(MediaFormat.MAGAZINE, TypeOfBinding.PAPERBACK, "Monthly magazine");
    }

    private Code codeWithIsbn13() {
        return Code.fromIsbn13("978-3-16-148410-0");
    }

    private Code codeWithIsbn10() {
        return Code.fromIsbn10("0-306-40615-2");
    }

    private Code codeWithIssn() {
        return Code.fromIssn("0378-5955");
    }

    private Code emptyCode() {
        return new Code(null, null, null);
    }

    // ─────────────────────────────────────────────────────────────
    // Construtor
    // ─────────────────────────────────────────────────────────────

    @Test
    void constructor_throwsWhenEditionNumberIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Edition(null, codeWithIsbn13(), validPhysicalAttributes(),
                        publicationTypeBook(), validPublisher(), Locale.ENGLISH,
                        LocalDate.of(2010, 1, 1)));
        assertTrue(ex.getMessage().contains("Edition Number cannot be blank"));
    }

    @Test
    void constructor_throwsWhenEditionNumberIsBlank() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Edition("   ", codeWithIsbn13(), validPhysicalAttributes(),
                        publicationTypeBook(), validPublisher(), Locale.ENGLISH,
                        LocalDate.of(2010, 1, 1)));
        assertTrue(ex.getMessage().contains("Edition Number cannot be blank"));
    }

    @Test
    void constructor_throwsWhenPhysicalAttributesIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Edition("1", codeWithIsbn13(), null,
                        publicationTypeBook(), validPublisher(), Locale.ENGLISH,
                        LocalDate.of(2010, 1, 1)));
    }

    @Test
    void constructor_throwsWhenPublicationTypeIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Edition("1", codeWithIsbn13(), validPhysicalAttributes(),
                        null, validPublisher(), Locale.ENGLISH,
                        LocalDate.of(2010, 1, 1)));
    }

    @Test
    void constructor_throwsWhenPublisherIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Edition("1", codeWithIsbn13(), validPhysicalAttributes(),
                        publicationTypeBook(), null, Locale.ENGLISH,
                        LocalDate.of(2010, 1, 1)));
    }

    @Test
    void constructor_throwsWhenEditionLanguageIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Edition("1", codeWithIsbn13(), validPhysicalAttributes(),
                        publicationTypeBook(), validPublisher(), null,
                        LocalDate.of(2010, 1, 1)));
        assertTrue(ex.getMessage().contains("Edition language cannot be null"));
    }

    @Test
    void constructor_throwsWhenEditionDateIsNull() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Edition("1", codeWithIsbn13(), validPhysicalAttributes(),
                        publicationTypeBook(), validPublisher(), Locale.ENGLISH, null));
        assertTrue(ex.getMessage().contains("Edition date cannot be null"));
    }

    @Test
    void constructor_book_exactlyIn2007_withIsbn13_succeeds() {
        assertNotNull(new Edition("1st", codeWithIsbn13(), validPhysicalAttributes(),
                publicationTypeBook(), validPublisher(), Locale.ENGLISH,
                LocalDate.of(2007, 1, 1)));
    }

    @Test
    void constructor_book_after2007_withIsbn13_succeeds() {
        assertNotNull(new Edition("1st", codeWithIsbn13(), validPhysicalAttributes(),
                publicationTypeBook(), validPublisher(), Locale.ENGLISH,
                LocalDate.of(2020, 6, 15)));
    }

    @Test
    void constructor_book_after2007_missingIsbn13_throws() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Edition("1st", emptyCode(), validPhysicalAttributes(),
                        publicationTypeBook(), validPublisher(), Locale.ENGLISH,
                        LocalDate.of(2020, 1, 1)));
        assertTrue(ex.getMessage().contains("ISBN-13"));
    }

    // ─────────────────────────────────────────────────────────────
    // validateCode — BOOK 1970–2006 (ISBN-10 mandatory)
    // ─────────────────────────────────────────────────────────────

    @Test
    void constructor_book_exactlyIn1970_withIsbn10_succeeds() {
        assertNotNull(new Edition("1st", codeWithIsbn10(), validPhysicalAttributes(),
                publicationTypeBook(), validPublisher(), Locale.ENGLISH,
                LocalDate.of(1970, 1, 1)));
    }

    @Test
    void constructor_book_between1970and2006_withIsbn10_succeeds() {
        assertNotNull(new Edition("1st", codeWithIsbn10(), validPhysicalAttributes(),
                publicationTypeBook(), validPublisher(), Locale.ENGLISH,
                LocalDate.of(1990, 1, 1)));
    }

    @Test
    void constructor_book_between1970and2006_missingIsbn10_throws() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Edition("1st", emptyCode(), validPhysicalAttributes(),
                        publicationTypeBook(), validPublisher(), Locale.ENGLISH,
                        LocalDate.of(1985, 1, 1)));
        assertTrue(ex.getMessage().contains("ISBN-10"));
    }

    // ─────────────────────────────────────────────────────────────
    // validateCode — BOOK < 1970 (no mandatory code)
    // ─────────────────────────────────────────────────────────────

    @Test
    void constructor_book_before1970_noCodeRequired_succeeds() {
        assertNotNull(new Edition("1st", emptyCode(), validPhysicalAttributes(),
                publicationTypeBook(), validPublisher(), Locale.ENGLISH,
                LocalDate.of(1950, 1, 1)));
    }

    // ─────────────────────────────────────────────────────────────
    // validateCode — MAGAZINE >= 1976 (ISSN mandatory)
    // ─────────────────────────────────────────────────────────────

    @Test
    void constructor_magazine_exactlyIn1976_withIssn_succeeds() {
        assertNotNull(new Edition("1st", codeWithIssn(), validPhysicalAttributes(),
                publicationTypeMagazine(), validPublisher(), Locale.ENGLISH,
                LocalDate.of(1976, 1, 1)));
    }

    @Test
    void constructor_magazine_after1976_withIssn_succeeds() {
        assertNotNull(new Edition("1st", codeWithIssn(), validPhysicalAttributes(),
                publicationTypeMagazine(), validPublisher(), Locale.ENGLISH,
                LocalDate.of(2000, 1, 1)));
    }

    @Test
    void constructor_magazine_after1976_missingIssn_throws() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Edition("1st", emptyCode(), validPhysicalAttributes(),
                        publicationTypeMagazine(), validPublisher(), Locale.ENGLISH,
                        LocalDate.of(2000, 1, 1)));
        assertTrue(ex.getMessage().contains("ISSN"));
    }

    // ─────────────────────────────────────────────────────────────
    // validateCode — MAGAZINE < 1976 (no code is mandatory)
    // ─────────────────────────────────────────────────────────────

    @Test
    void constructor_magazine_before1976_noCodeRequired_succeeds() {
        assertNotNull(new Edition("1st", emptyCode(), validPhysicalAttributes(),
                publicationTypeMagazine(), validPublisher(), Locale.ENGLISH,
                LocalDate.of(1970, 1, 1)));
    }

    // -------------------------
    // getters
    // -------------------------

    /**
     * Tests that the correct publication type is returned from an edition.
     * <p>
     * Verifies that the {@link Edition#getPublicationType()} method returns
     * the publication type that was set during edition creation.
     * </p>
     */
    @Test
    void shouldReturnPublicationType() {
        //Arrange
        PublicationType expectedPubType = validPublicationType();

        //Act
        PublicationType realPubType = validEdition().getPublicationType();

        //Assert
        assertEquals(expectedPubType, realPubType);
    }

    /**
     * Tests that the correct code is returned from an edition.
     * <p>
     * Verifies that the {@link Edition#getCode()} method returns
     * the code (ISBN or other identifier) that was set during edition creation.
     * </p>
     */
    @Test
    void shouldReturnCode() {
        //Arrange
        Code expectedCode = validCode();

        //Act
        Code realCode = validEdition().getCode();

        //Assert
        assertEquals(expectedCode, realCode);
    }

    @Test
    void getPublisher_returnsThePublisherPassedInConstructor() {
        Publisher publisher = validPublisher();
        Edition e = new Edition("1st", codeWithIsbn13(), validPhysicalAttributes(),
                publicationTypeBook(), publisher, Locale.ENGLISH,
                LocalDate.of(2020, 1, 1));
        assertEquals(publisher, e.getPublisher());
    }

    // ─────────────────────────────────────────────────────────────
    // equals
    // ─────────────────────────────────────────────────────────────

    @Test
    void equals_sameReference_returnsTrue() {
        Edition e = new Edition("1st", codeWithIsbn13(), validPhysicalAttributes(),
                publicationTypeBook(), validPublisher(), Locale.ENGLISH,
                LocalDate.of(2020, 1, 1));
        assertEquals(e, e);
    }

    @Test
    void equals_twoIdenticalEditions_returnsTrue() {
        PhysicalAttributes pa = new PhysicalAttributes(validDimensions(), 0.5, 200);
        Publisher pub = validPublisher();
        Code code = codeWithIsbn13();
        PublicationType type = publicationTypeBook();
        LocalDate date = LocalDate.of(2020, 1, 1);

        Edition e1 = new Edition("1st", code, pa, type, pub, Locale.ENGLISH, date);
        Edition e2 = new Edition("1st", code, pa, type, pub, Locale.ENGLISH, date);

        assertEquals(e1, e2);
    }

    @Test
    void equals_differentEditionNumber_returnsFalse() {
        PhysicalAttributes pa = new PhysicalAttributes(validDimensions(), 0.5, 200);
        Publisher pub = validPublisher();
        Code code = codeWithIsbn13();
        PublicationType type = publicationTypeBook();
        LocalDate date = LocalDate.of(2020, 1, 1);

        Edition e1 = new Edition("1st", code, pa, type, pub, Locale.ENGLISH, date);
        Edition e2 = new Edition("2nd", code, pa, type, pub, Locale.ENGLISH, date);

        assertNotEquals(e1, e2);
    }

    @Test
    void equals_differentDate_returnsFalse() {
        PhysicalAttributes pa = new PhysicalAttributes(validDimensions(), 0.5, 200);
        Publisher pub = validPublisher();
        Code code = codeWithIsbn13();
        PublicationType type = publicationTypeBook();

        Edition e1 = new Edition("1st", code, pa, type, pub, Locale.ENGLISH, LocalDate.of(2020, 1, 1));
        Edition e2 = new Edition("1st", code, pa, type, pub, Locale.ENGLISH, LocalDate.of(2021, 1, 1));

        assertNotEquals(e1, e2);
    }

    @Test
    void equals_differentLocale_returnsFalse() {
        PhysicalAttributes pa = new PhysicalAttributes(validDimensions(), 0.5, 200);
        Publisher pub = validPublisher();
        Code code = codeWithIsbn13();
        PublicationType type = publicationTypeBook();
        LocalDate date = LocalDate.of(2020, 1, 1);

        Edition e1 = new Edition("1st", code, pa, type, pub, Locale.ENGLISH, date);
        Edition e2 = new Edition("1st", code, pa, type, pub, Locale.FRENCH, date);

        assertNotEquals(e1, e2);
    }

    @Test
    void equals_nullObject_returnsFalse() {
        Edition e = new Edition("1st", codeWithIsbn13(), validPhysicalAttributes(),
                publicationTypeBook(), validPublisher(), Locale.ENGLISH,
                LocalDate.of(2020, 1, 1));
        assertNotEquals(null, e);
    }

    @Test
    void equals_differentClass_returnsFalse() {
        Edition e = new Edition("1st", codeWithIsbn13(), validPhysicalAttributes(),
                publicationTypeBook(), validPublisher(), Locale.ENGLISH,
                LocalDate.of(2020, 1, 1));
        assertNotEquals("uma string qualquer", e);
    }
}
