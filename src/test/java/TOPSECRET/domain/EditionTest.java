package TOPSECRET.domain;

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

    // -------------------------
    // Helpers
    // -------------------------

    private Code validCode() {
        return Code.fromIsbn13("9783161484100");
    }

    private Code differentCode() {
        return Code.fromIsbn13("9783161484101");
    }

    private Dimensions validDimensions() {
        return new Dimensions(10.0, 2.0, 5.0);
    }

    private PhysicalAttributes validPhysicalAttributes() {
        return new PhysicalAttributes(validDimensions(), 300.2, 200);
    }

    private PhysicalAttributes differentPhysicalAttributes() {
        return new PhysicalAttributes(validDimensions(), 400.0, 250);
    }

    private PublicationType validPublicationType() {
        return new PublicationType(TypeOfBinding.CASE_BINDING, "Some details");
    }

    private PublicationType differentPublicationType() {
        return new PublicationType(TypeOfBinding.PAPERBACK, "Other details");
    }

    private Publisher validPublisher() {
        return new Publisher("Porto Editora", "PT123456789", null);
    }

    private Publisher differentPublisher() {
        return new Publisher("Outra Editora", "PT987654321", null);
    }
    private Locale validEditionLanguage() {
        return Locale.ENGLISH;
    }


    private LocalDate validEditionDate() {
        return LocalDate.of(2020, 1, 3);
    }

    private Edition validEdition() {
        return new Edition(
                "1st",
                validCode(),
                validPhysicalAttributes(),
                validPublicationType(),
                validPublisher(),
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1)
        );
    }

    // -------------------------
    // Constructor
    // -------------------------
    @Test
    void constructor_shouldThrow_whenEditionNumberIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Edition(null,
                        validCode(),
                        validPhysicalAttributes(),
                        validPublicationType(),
                        validPublisher(),
                        validEditionLanguage(),
                        validEditionDate())
        );
    }

    @Test
    void constructor_shouldThrow_whenEditionNumberIsBlank() {
        assertThrows(IllegalArgumentException.class, () ->
                new Edition("   ",
                        validCode(),
                        validPhysicalAttributes(),
                        validPublicationType(),
                        validPublisher(),
                        validEditionLanguage(),
                        validEditionDate())
        );
    }

    @Test
    void constructor_shouldThrow_whenCodeIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Edition("1st",
                        null,
                        validPhysicalAttributes(),
                        validPublicationType(),
                        validPublisher(),
                        validEditionLanguage(),
                        validEditionDate())
        );
    }

    @Test
    void constructor_shouldThrow_whenPhysicalAttributesIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Edition("1st",
                        validCode(),
                        null,
                        validPublicationType(),
                        validPublisher(),
                        validEditionLanguage(),
                        validEditionDate())
        );
    }

    @Test
    void constructor_shouldThrow_whenPublicationTypeIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Edition("1st",
                        validCode(),
                        validPhysicalAttributes(),
                        null,
                        validPublisher(),
                        validEditionLanguage(),
                        validEditionDate())
        );
    }

    @Test
    void constructor_shouldThrow_whenPublisherIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Edition("1st",
                        validCode(),
                        validPhysicalAttributes(),
                        validPublicationType(),
                        null,
                        validEditionLanguage(),
                        validEditionDate())
        );
    }

    @Test
    void constructor_shouldThrow_whenEditionLanguageIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Edition("1st",
                        validCode(),
                        validPhysicalAttributes(),
                        validPublicationType(),
                        validPublisher(),
                        null,
                        validEditionDate())
        );
    }

    @Test
    void constructor_shouldThrow_whenEditionDateIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Edition("1st",
                        validCode(),
                        validPhysicalAttributes(),
                        validPublicationType(),
                        validPublisher(),
                        validEditionLanguage(),
                        null)
        );
    }

    // -------------------------
    // Happy path
    // -------------------------

    @Test
    void constructor_shouldNotThrow_whenAllFieldsValid() {
        assertDoesNotThrow(() ->
                new Edition("1st",
                        validCode(),
                        validPhysicalAttributes(),
                        validPublicationType(),
                        validPublisher(),
                        validEditionLanguage(),
                        validEditionDate())
        );
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
    void shouldReturnPublicationType(){
        //Arrange
        PublicationType expectedPubType = validPublicationType();

        //Act
        PublicationType realPubType = validEdition().getPublicationType();

        //Assert
        assertEquals(expectedPubType,realPubType);
    }

    /**
     * Tests that the correct code is returned from an edition.
     * <p>
     * Verifies that the {@link Edition#getCode()} method returns
     * the code (ISBN or other identifier) that was set during edition creation.
     * </p>
     */
    @Test
    void shouldReturnCode(){
        //Arrange
        Code expectedCode = validCode();

        //Act
        Code realCode = validEdition().getCode();

        //Assert
        assertEquals(expectedCode, realCode);
    }

    // -------------------------
    // equals
    // -------------------------

    @Test
    void equals_shouldBeFalse_whenComparedWithNull() {
        Edition edition = validEdition();
        assertNotEquals(null, edition);
    }

    @Test
    void equals_shouldBeFalse_whenDifferentClass() {
        Edition edition = validEdition();
        assertNotEquals("not an edition", edition);
    }

    @Test
    void equals_shouldBeTrue_whenAllFieldsEqual() {
        Edition a = validEdition();
        Edition b = validEdition();

        assertEquals(a, b);
        assertEquals(b, a); // symmetry
    }

    @Test
    void equals_shouldBeFalse_whenEditionNumberDiffers() {
        Edition a = validEdition();
        Edition b = new Edition(
                "2nd",
                validCode(),
                validPhysicalAttributes(),
                validPublicationType(),
                validPublisher(),
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1)
        );

        assertNotEquals(a, b);
    }

    @Test
    void equals_shouldBeFalse_whenCodeDiffers() {
        Edition a = validEdition();
        Edition b = new Edition(
                "1st",
                differentCode(),
                validPhysicalAttributes(),
                validPublicationType(),
                validPublisher(),
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1)
        );

        assertNotEquals(a, b);
    }

    @Test
    void equals_shouldBeFalse_whenPhysicalAttributesDiffer() {
        Edition a = validEdition();
        Edition b = new Edition(
                "1st",
                validCode(),
                differentPhysicalAttributes(),
                validPublicationType(),
                validPublisher(),
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1)
        );

        assertNotEquals(a, b);
    }

    @Test
    void equals_shouldBeFalse_whenPublicationTypeDiffers() {
        Edition a = validEdition();
        Edition b = new Edition(
                "1st",
                validCode(),
                validPhysicalAttributes(),
                differentPublicationType(),
                validPublisher(),
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1)
        );

        assertNotEquals(a, b);
    }

    @Test
    void equals_shouldBeFalse_whenPublisherDiffers() {
        Edition a = validEdition();
        Edition b = new Edition(
                "1st",
                validCode(),
                validPhysicalAttributes(),
                validPublicationType(),
                differentPublisher(),
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1)
        );

        assertNotEquals(a, b);
    }

    @Test
    void equals_shouldBeFalse_whenLanguageDiffers() {
        Edition a = validEdition();
        Edition b = new Edition(
                "1st",
                validCode(),
                validPhysicalAttributes(),
                validPublicationType(),
                validPublisher(),
                Locale.FRENCH,
                LocalDate.of(2020, 1, 1)
        );

        assertNotEquals(a, b);
    }

    @Test
    void equals_shouldBeFalse_whenDateDiffers() {
        Code code = validCode();
        PhysicalAttributes pa = validPhysicalAttributes();
        PublicationType pt = validPublicationType();
        Publisher pub = validPublisher();
        Locale lang = validEditionLanguage();

        Edition a = new Edition("1st", code, pa, pt, pub, lang, LocalDate.of(2020, 1, 1));
        Edition b = new Edition("1st", code, pa, pt, pub, lang, LocalDate.of(2020, 1, 3));

        assertNotEquals(a, b);
    }
}
