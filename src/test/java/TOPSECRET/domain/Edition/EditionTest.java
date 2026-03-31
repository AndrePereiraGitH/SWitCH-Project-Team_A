package TOPSECRET.domain.Edition;

import TOPSECRET.domain.Contact;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.Publisher;
import TOPSECRET.domain.ValueObjects.Code;
import TOPSECRET.domain.ValueObjects.EditionID;
import TOPSECRET.domain.ValueObjects.PhysicalAttributes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link Edition} class using Mockito mocks for all dependencies.
 * <p>
 * Dependencies such as {@link PhysicalAttributes}, {@link Publisher},
 * {@link PublicationType}, and {@link Contact} are mocked to isolate
 * {@link Edition} logic from its collaborators.
 * <ul>
 *     <li>Constructor argument validation</li>
 *     <li>Code validation rules by media format and publication date</li>
 *     <li>Correct behaviour of getters</li>
 *     <li>Correct behaviour of {@code equals()}</li>
 * </ul>
 */
class EditionTest {

    private final PhysicalAttributes validPhysicalAttributes = mock(PhysicalAttributes.class);
    private final Publisher validPublisher = mock(Publisher.class);
    private final PublicationType publicationType = mock(PublicationType.class);

    private final Code codeWithIsbn13 = Code.fromIsbn13("978-3-16-148410-0");
    private final Code codeWithIsbn10 = Code.fromIsbn10("0-306-40615-2");

// ─────────────────────────────────────────────────────────────
// Constructor validation
// ─────────────────────────────────────────────────────────────

    @Test
    void shouldThrow_WhenEditionNumber_IsNull() {

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        when(publicationType.getMediaFormat()).thenReturn("Book");

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Edition(null, codeWithIsbn13, validPhysicalAttributes,
                        publicationType, validPublisher,
                        Locale.ENGLISH, LocalDate.of(2010, 1, 1), typeOfBindingDouble));

        // Assert
        assertNotNull(ex.getMessage());
        assertTrue(ex.getMessage().contains("Edition Number cannot be blank"));
    }

    @Test
    void shouldThrow_WhenEditionNumber_IsBlank() {

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        when(publicationType.getMediaFormat()).thenReturn("Book");

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Edition("   ", codeWithIsbn13, validPhysicalAttributes,
                        publicationType, validPublisher,
                        Locale.ENGLISH, LocalDate.of(2010, 1, 1), typeOfBindingDouble));

        // Assert
        assertTrue(ex.getMessage().contains("Edition Number cannot be blank"));
    }

    @Test
    void shouldThrow_WhenPhysicalAttributes_IsNull() {

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        when(publicationType.getMediaFormat()).thenReturn("Book");

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Edition("1", codeWithIsbn13, null,
                        publicationType, validPublisher,
                        Locale.ENGLISH, LocalDate.of(2010, 1, 1), typeOfBindingDouble));

        // Assert
        assertTrue(ex.getMessage().contains("Physical"));
    }

    @Test
    void shouldThrow_WhenPublicationType_IsNull() {

        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Edition("1", codeWithIsbn13, validPhysicalAttributes,
                        null, validPublisher,
                        Locale.ENGLISH, LocalDate.of(2010, 1, 1), typeOfBindingDouble));

        // Assert
        assertTrue(ex.getMessage().contains("publication"));
    }

    @Test
    void shouldThrow_WhenPublisher_IsNull() {

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        when(publicationType.getMediaFormat()).thenReturn("Book");

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Edition("1", codeWithIsbn13, validPhysicalAttributes,
                        publicationType, null,
                        Locale.ENGLISH, LocalDate.of(2010, 1, 1), typeOfBindingDouble));

        // Assert
        assertTrue(ex.getMessage().contains("publisher"));
    }

    @Test
    void shouldThrow_WhenEditionLanguage_IsNull() {

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        when(publicationType.getMediaFormat()).thenReturn("Book");

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Edition("1", codeWithIsbn13, validPhysicalAttributes,
                        publicationType, validPublisher,
                        null, LocalDate.of(2010, 1, 1), typeOfBindingDouble));

        // Assert
        assertTrue(ex.getMessage().contains("Edition language"));
    }

    @Test
    void shouldThrow_WhenEditionDate_IsNull() {

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        when(publicationType.getMediaFormat()).thenReturn("Book");

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Edition("1", codeWithIsbn13, validPhysicalAttributes,
                        publicationType, validPublisher,
                        Locale.ENGLISH, null, typeOfBindingDouble));

        // Assert
        assertTrue(ex.getMessage().contains("Edition date"));
    }

    @Test
    void shouldThrow_whenTypeOfBinding_isNull() {

        // Arrange
        when(publicationType.getMediaFormat()).thenReturn("Book");

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Edition("1", codeWithIsbn13, validPhysicalAttributes,
                        publicationType, validPublisher,
                        Locale.ENGLISH, LocalDate.of(2000, 11, 1), null));

        // Assert
        assertTrue(ex.getMessage().contains("TypeOfBinding"));

    }

// ─────────────────────────────────────────────────────────────
// BOOK ≥ 2007 → ISBN-13 mandatory
// ─────────────────────────────────────────────────────────────

    /**
     * Parameterized test that runs the same test logic with different input values.
     * <p>
     * Each value provided by {@code @ValueSource} is passed to the test method,
     * allowing the same test to verify the behavior for multiple years without
     * duplicating test code.
     */
    @ParameterizedTest
    @ValueSource(ints = {2007, 2020})
    void constructor_bookAfter2007_withIsbn13_succeeds(int year) {

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        when(publicationType.getMediaFormat()).thenReturn("Book");

        // Act + Assert
        assertDoesNotThrow(() ->
                new Edition("1st", codeWithIsbn13, validPhysicalAttributes,
                        publicationType, validPublisher,
                        Locale.ENGLISH, LocalDate.of(year, 1, 1), typeOfBindingDouble));
    }

    @ParameterizedTest
    @ValueSource(ints = {2007, 2020})
    void constructor_bookAfter2007_missingIsbn13_throws(int year) {

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        Code code = mock(Code.class);
        when(code.getIsbn13()).thenReturn(null);
        when(publicationType.getMediaFormat()).thenReturn("Book");

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Edition("1st", code, validPhysicalAttributes,
                        publicationType, validPublisher,
                        Locale.ENGLISH, LocalDate.of(year, 1, 1), typeOfBindingDouble));

        // Assert
        assertNotNull(ex.getMessage());
        assertTrue(ex.getMessage().contains("ISBN-13"));
    }

// ─────────────────────────────────────────────────────────────
// BOOK 1970–2006 → ISBN-10 mandatory
// ─────────────────────────────────────────────────────────────

    @ParameterizedTest
    @ValueSource(ints = {1970, 1990, 2006})
    void constructor_book1970to2006_withIsbn10_succeeds(int year) {

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        when(publicationType.getMediaFormat()).thenReturn("Book");

        // Act + Assert
        assertDoesNotThrow(() ->
                new Edition("1st", codeWithIsbn10, validPhysicalAttributes,
                        publicationType, validPublisher,
                        Locale.ENGLISH, LocalDate.of(year, 1, 1), typeOfBindingDouble));
    }

    @ParameterizedTest
    @ValueSource(ints = {1970, 1985, 2006})
    void constructor_book1970to2006_missingIsbn10_throws(int year) {

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        Code code = mock(Code.class);
        when(code.getIsbn10()).thenReturn(null);
        when(publicationType.getMediaFormat()).thenReturn("Book");

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Edition("1st", code, validPhysicalAttributes,
                        publicationType, validPublisher,
                        Locale.ENGLISH, LocalDate.of(year, 1, 1), typeOfBindingDouble));

        // Assert
        assertNotNull(ex.getMessage());
        assertTrue(ex.getMessage().contains("ISBN-10"));
    }

// ─────────────────────────────────────────────────────────────
// BOOK < 1970 → no code required
// ─────────────────────────────────────────────────────────────

    @ParameterizedTest
    @ValueSource(ints = {1900, 1950, 1969})
    void constructor_bookBefore1970_noCodeRequired_succeeds(int year) {

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        when(publicationType.getMediaFormat()).thenReturn("Book");

        // Act + Assert
        assertDoesNotThrow(() ->
                new Edition("1st", null, validPhysicalAttributes,
                        publicationType, validPublisher,
                        Locale.ENGLISH, LocalDate.of(year, 1, 1), typeOfBindingDouble));
    }

// ─────────────────────────────────────────────────────────────
// MAGAZINE ≥ 1976 → ISSN mandatory
// ─────────────────────────────────────────────────────────────

    @ParameterizedTest
    @ValueSource(ints = {1976, 2000})
    void constructor_magazineAfter1976_withIssn_succeeds(int year) {

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        when(publicationType.getMediaFormat()).thenReturn("Magazine");
        Code codeWithIssn = Code.fromIssn("0378-5955");

        // Act + Assert
        assertDoesNotThrow(() ->
                new Edition("1st", codeWithIssn, validPhysicalAttributes,
                        publicationType, validPublisher,
                        Locale.ENGLISH, LocalDate.of(year, 1, 1), typeOfBindingDouble));
    }

    @ParameterizedTest
    @ValueSource(ints = {1976, 2000})
    void constructor_magazineAfter1976_missingIssn_throws(int year) {

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        Code code = mock(Code.class);
        when(code.getIssn()).thenReturn(null);
        when(publicationType.getMediaFormat()).thenReturn("Magazine");

        // Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new Edition("1st", code, validPhysicalAttributes,
                        publicationType, validPublisher,
                        Locale.ENGLISH, LocalDate.of(year, 1, 1), typeOfBindingDouble));

        // Assert
        assertNotNull(ex.getMessage());
        assertTrue(ex.getMessage().contains("ISSN"));
    }

// ─────────────────────────────────────────────────────────────
// MAGAZINE < 1976 → no code required
// ─────────────────────────────────────────────────────────────

    @ParameterizedTest
    @ValueSource(ints = {1900, 1970, 1975})
    void constructor_magazineBefore1976_noCodeRequired_succeeds(int year) {

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        when(publicationType.getMediaFormat()).thenReturn("Magazine");

        // Act + Assert
        assertDoesNotThrow(() ->
                new Edition("1st", null, validPhysicalAttributes,
                        publicationType, validPublisher,
                        Locale.ENGLISH, LocalDate.of(year, 1, 1), typeOfBindingDouble));
    }

// ─────────────────────────────────────────────────────────────
// Getters
// ─────────────────────────────────────────────────────────────

    @Test
    void shouldReturnEditionNumber() {

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        when(publicationType.getMediaFormat()).thenReturn("Book");

        Edition edition = new Edition("1st", codeWithIsbn13,
                validPhysicalAttributes, publicationType,
                validPublisher, Locale.ENGLISH,
                LocalDate.of(2020, 1, 1), typeOfBindingDouble);

        // Act
        String result = edition.getEditionNumber();

        // Assert
        assertEquals("1st", result);
    }

    @Test
    void shouldReturnCode() {

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        when(publicationType.getMediaFormat()).thenReturn("Book");

        Edition edition = new Edition("1st", codeWithIsbn13,
                validPhysicalAttributes, publicationType,
                validPublisher, Locale.ENGLISH,
                LocalDate.of(2020, 1, 1), typeOfBindingDouble);

        // Act
        Code result = edition.getCode();

        // Assert
        assertEquals(codeWithIsbn13, result);
    }

    @Test
    void shouldReturnPublicationType() {

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        when(publicationType.getMediaFormat()).thenReturn("Book");

        Edition edition = new Edition("1st", codeWithIsbn13,
                validPhysicalAttributes, publicationType,
                validPublisher, Locale.ENGLISH,
                LocalDate.of(2020, 1, 1), typeOfBindingDouble);

        // Act
        PublicationType result = edition.getPublicationType();

        // Assert
        assertEquals(publicationType, result);
    }

    @Test
    void shouldReturnPublisher() {

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        when(publicationType.getMediaFormat()).thenReturn("Book");

        Edition edition = new Edition("1st", codeWithIsbn13,
                validPhysicalAttributes, publicationType,
                validPublisher, Locale.ENGLISH,
                LocalDate.of(2020, 1, 1), typeOfBindingDouble);

        // Act
        Publisher result = edition.getPublisher();

        // Assert
        assertEquals(validPublisher, result);
    }

    @Test
    void shouldReturnEditionLanguage() {

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        when(publicationType.getMediaFormat()).thenReturn("Book");

        Edition edition = new Edition("1st", codeWithIsbn13,
                validPhysicalAttributes, publicationType,
                validPublisher, Locale.ENGLISH,
                LocalDate.of(2020, 1, 1), typeOfBindingDouble);

        // Act
        Locale result = edition.getEditionLanguage();

        // Assert
        assertEquals(Locale.ENGLISH, result);
    }

    @Test
    void shouldReturnEditionDate() {

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        when(publicationType.getMediaFormat()).thenReturn("Book");

        LocalDate expectedDate = LocalDate.of(2020, 1, 1);
        Edition edition = new Edition("1st", codeWithIsbn13,
                validPhysicalAttributes, publicationType,
                validPublisher, Locale.ENGLISH, expectedDate, typeOfBindingDouble);

        // Act
        LocalDate result = edition.getEditionDate();

        // Assert
        assertEquals(expectedDate, result);
    }

    @Test
    void shouldReturnTypeOfBinding() {

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        when(publicationType.getMediaFormat()).thenReturn("Book");

        LocalDate expectedDate = LocalDate.of(2020, 1, 1);
        Edition edition = new Edition("1st", codeWithIsbn13,
                validPhysicalAttributes, publicationType,
                validPublisher, Locale.ENGLISH, expectedDate, typeOfBindingDouble);

        // Act
        TypeOfBinding result = edition.getTypeOfBinding();

        // Assert
        assertEquals(typeOfBindingDouble, result);
    }
/*
// ─────────────────────────────────────────────────────────────
// equals
// ─────────────────────────────────────────────────────────────

    @Test
    void equals_sameReference_returnsTrue() {

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        when(publicationType.getMediaFormat()).thenReturn("Book");
        Edition e = new Edition("1st", codeWithIsbn13,
                validPhysicalAttributes, publicationType,
                validPublisher, Locale.ENGLISH,
                LocalDate.of(2020, 1, 1), typeOfBindingDouble);

        // Act
        boolean result = e.equals(e);

        // Assert
        assertTrue(result);
    }

    @Test
    void equals_twoIdenticalEditions_returnsTrue() {

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        when(publicationType.getMediaFormat()).thenReturn("Book");
        LocalDate date = LocalDate.of(2020, 1, 1);

        Edition e1 = new Edition("1st", codeWithIsbn13,
                validPhysicalAttributes, publicationType,
                validPublisher, Locale.ENGLISH, date, typeOfBindingDouble);

        Edition e2 = new Edition("1st", codeWithIsbn13,
                validPhysicalAttributes, publicationType,
                validPublisher, Locale.ENGLISH, date, typeOfBindingDouble);

        // Act
        boolean result = e1.equals(e2);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_differentEditionNumber_returnsFalse() {

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        when(publicationType.getMediaFormat()).thenReturn("Book");
        LocalDate date = LocalDate.of(2020, 1, 1);

        Edition e1 = new Edition("1st", codeWithIsbn13,
                validPhysicalAttributes, publicationType,
                validPublisher, Locale.ENGLISH, date, typeOfBindingDouble);

        Edition e2 = new Edition("2nd", codeWithIsbn13,
                validPhysicalAttributes, publicationType,
                validPublisher, Locale.ENGLISH, date, typeOfBindingDouble);

        // Act
        boolean result = e1.equals(e2);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_differentCode_returnsFalse() {

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        when(publicationType.getMediaFormat()).thenReturn("Book");
        LocalDate date = LocalDate.of(2020, 1, 1);
        LocalDate date2 = LocalDate.of(2001, 1, 1);

        Edition e1 = new Edition("1st", codeWithIsbn13,
                validPhysicalAttributes, publicationType,
                validPublisher, Locale.ENGLISH, date, typeOfBindingDouble);

        Edition e2 = new Edition("1st", codeWithIsbn10,
                validPhysicalAttributes, publicationType,
                validPublisher, Locale.ENGLISH, date2, typeOfBindingDouble);

        // Act
        boolean result = e1.equals(e2);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_differentPhysicalAttributes_returnsFalse() {

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        PhysicalAttributes otherPhysicalAttributes = mock(PhysicalAttributes.class);
        when(publicationType.getMediaFormat()).thenReturn("Book");
        LocalDate date = LocalDate.of(2020, 1, 1);

        Edition e1 = new Edition("1st", codeWithIsbn13,
                validPhysicalAttributes, publicationType,
                validPublisher, Locale.ENGLISH, date, typeOfBindingDouble);

        Edition e2 = new Edition("1st", codeWithIsbn13,
                otherPhysicalAttributes, publicationType,
                validPublisher, Locale.ENGLISH, date, typeOfBindingDouble);

        // Act
        boolean result = e1.equals(e2);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_differentPublicationType_returnsFalse() {

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        PublicationType otherPublicationType = mock(PublicationType.class);
        when(publicationType.getMediaFormat()).thenReturn("Book");
        when(otherPublicationType.getMediaFormat()).thenReturn("Book");
        LocalDate date = LocalDate.of(2020, 1, 1);

        Edition e1 = new Edition("1st", codeWithIsbn13,
                validPhysicalAttributes, publicationType,
                validPublisher, Locale.ENGLISH, date, typeOfBindingDouble);

        Edition e2 = new Edition("1st", codeWithIsbn13,
                validPhysicalAttributes, otherPublicationType,
                validPublisher, Locale.ENGLISH, date, typeOfBindingDouble);

        // Act
        boolean result = e1.equals(e2);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_differentPublisher_returnsFalse() {

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        Publisher otherPublisher = mock(Publisher.class);
        when(publicationType.getMediaFormat()).thenReturn("Book");
        LocalDate date = LocalDate.of(2020, 1, 1);

        Edition e1 = new Edition("1st", codeWithIsbn13,
                validPhysicalAttributes, publicationType,
                validPublisher, Locale.ENGLISH, date, typeOfBindingDouble);

        Edition e2 = new Edition("1st", codeWithIsbn13,
                validPhysicalAttributes, publicationType,
                otherPublisher, Locale.ENGLISH, date, typeOfBindingDouble);

        // Act
        boolean result = e1.equals(e2);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_differentLocale_returnsFalse() {

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        when(publicationType.getMediaFormat()).thenReturn("Book");
        LocalDate date = LocalDate.of(2020, 1, 1);

        Edition e1 = new Edition("1st", codeWithIsbn13,
                validPhysicalAttributes, publicationType,
                validPublisher, Locale.ENGLISH, date, typeOfBindingDouble);

        Edition e2 = new Edition("1st", codeWithIsbn13,
                validPhysicalAttributes, publicationType,
                validPublisher, Locale.FRENCH, date, typeOfBindingDouble);

        // Act
        boolean result = e1.equals(e2);

        // Assert
        assertFalse(result);
    }

    @Test
    void equals_differentDate_returnsFalse() {

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        when(publicationType.getMediaFormat()).thenReturn("Book");

        Edition e1 = new Edition("1st", codeWithIsbn13,
                validPhysicalAttributes, publicationType,
                validPublisher, Locale.ENGLISH,
                LocalDate.of(2020, 1, 1), typeOfBindingDouble);

        Edition e2 = new Edition("1st", codeWithIsbn13,
                validPhysicalAttributes, publicationType,
                validPublisher, Locale.ENGLISH,
                LocalDate.of(2021, 1, 1), typeOfBindingDouble);

        // Act
        boolean result = e1.equals(e2);

        // Assert
        assertFalse(result);
    }*/

// ─────────────────────────────────────────────────────────────
// Identity (DDD)
// ─────────────────────────────────────────────────────────────

    @Test
    void sameIdentityAs_sameInstance_returnsTrue() {

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        when(publicationType.getMediaFormat()).thenReturn("Book");

        Edition e = new Edition("1st", codeWithIsbn13,
                validPhysicalAttributes, publicationType,
                validPublisher, Locale.ENGLISH,
                LocalDate.of(2020, 1, 1), typeOfBindingDouble);

        // Act
        boolean result = e.sameAs(e);

        // Assert
        assertTrue(result);
    }

    @Test
    void sameAs_differentInstances_returnsTrue() {

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        when(publicationType.getMediaFormat()).thenReturn("Book");

        Edition e1 = new Edition("1st", codeWithIsbn13,
                validPhysicalAttributes, publicationType,
                validPublisher, Locale.ENGLISH,
                LocalDate.of(2020, 1, 1), typeOfBindingDouble);

        Edition e2 = new Edition("1st", codeWithIsbn13,
                validPhysicalAttributes, publicationType,
                validPublisher, Locale.ENGLISH,
                LocalDate.of(2020, 1, 1), typeOfBindingDouble);

        // Act
        boolean result = e1.sameAs(e2);

        // Assert
        assertTrue(result);
    }

    @Test
    void givenValidEditionThenReturnValidEdition(){
        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        when(publicationType.getMediaFormat()).thenReturn("Book");

        Edition e = new Edition("1st", codeWithIsbn13,
                validPhysicalAttributes, publicationType,
                validPublisher, Locale.ENGLISH,
                LocalDate.of(2020, 1, 1), typeOfBindingDouble);

        // Assert
        EditionID id = e.id();
        assertFalse(id.getID().isBlank());
        assertDoesNotThrow(() -> UUID.fromString(id.getID()));
    }

}