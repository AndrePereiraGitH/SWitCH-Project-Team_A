package TOPSECRET.domain.Edition;

import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.ValueObjects.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link EditionFactory}.
 *
 * <p>
 * This test suite validates both creation and reconstruction of {@link Edition}
 * aggregates, ensuring:
 * <ul>
 *     <li>Correct object creation</li>
 *     <li>Identity generation and preservation</li>
 *     <li>Validation of domain invariants</li>
 * </ul>
 * </p>
 *
 * <p>
 * All dependencies (including Value Objects) are mocked to ensure full isolation.
 * </p>
 */
class EditionFactoryTest {

    private EditionFactory factory;

    private Code validCodeDouble;
    private PhysicalAttributes validPhysicalAttributesDouble;
    private PublicationTypeId validPublicationTypeIdDouble;
    private PublisherID validPublisherIdDouble;
    private TypeOfBinding typeOfBindingDouble;

    @BeforeEach
    void setUp() {

        validCodeDouble = mock(Code.class);
        when(validCodeDouble.getIsbn13()).thenReturn("9781234567890");

        validPhysicalAttributesDouble = mock(PhysicalAttributes.class);
        validPublicationTypeIdDouble = mock(PublicationTypeId.class);
        validPublisherIdDouble = mock(PublisherID.class);
        typeOfBindingDouble = mock(TypeOfBinding.class);
    }

    // ─────────────────────────────────────────────
    // CREATE - HAPPY PATH
    // ─────────────────────────────────────────────

    @Test
    void create_shouldCreateEdition_whenValidData() {

        // Arrange

        factory = new EditionFactory();

        // Act
        Edition edition = factory.create(
                "1st",
                validCodeDouble,
                validPhysicalAttributesDouble,
                validPublicationTypeIdDouble,
                validPublisherIdDouble,
                "Book",
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1),
                typeOfBindingDouble
        );

        // Assert
        assertNotNull(edition);
        assertEquals("1st", edition.getEditionNumber());
        assertEquals("Book", edition.getMediaFormat());
        assertEquals(Locale.ENGLISH, edition.getEditionLanguage());
        assertEquals(LocalDate.of(2020, 1, 1), edition.getEditionDate());
    }

    @Test
    void create_shouldGenerateDifferentIds() {

        // Arrange

        factory = new EditionFactory();

        // Act
        Edition e1 = factory.create(
                "1st",
                validCodeDouble,
                validPhysicalAttributesDouble,
                validPublicationTypeIdDouble,
                validPublisherIdDouble,
                "Book",
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1),
                typeOfBindingDouble
        );

        Edition e2 = factory.create(
                "1st",
                validCodeDouble,
                validPhysicalAttributesDouble,
                validPublicationTypeIdDouble,
                validPublisherIdDouble,
                "Book",
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1),
                typeOfBindingDouble
        );

        // Assert
        assertNotEquals(e1.id(), e2.id());
    }

    // ─────────────────────────────────────────────
    // CREATE - VALIDATIONS
    // ─────────────────────────────────────────────

    @Test
    void create_shouldThrow_whenEditionNumberIsNull() {

        // Arrange
        factory = new EditionFactory();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                factory.create(
                        null,
                        validCodeDouble,
                        validPhysicalAttributesDouble,
                        validPublicationTypeIdDouble,
                        validPublisherIdDouble,
                        "Book",
                        Locale.ENGLISH,
                        LocalDate.of(2020,1,1),
                        typeOfBindingDouble
                ));
    }

    @Test
    void create_shouldThrow_whenMediaFormatInvalid() {

        // Arrange
        factory = new EditionFactory();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                factory.create(
                        "1st",
                        validCodeDouble,
                        validPhysicalAttributesDouble,
                        validPublicationTypeIdDouble,
                        validPublisherIdDouble,
                        "INVALID",
                        Locale.ENGLISH,
                        LocalDate.of(2020,1,1),
                        typeOfBindingDouble
                ));
    }

    @Test
    void create_shouldValidateCodeRules() {

        // Arrange
        factory = new EditionFactory();

        Code invalidCodeDouble = mock(Code.class);
        when(invalidCodeDouble.getIsbn13()).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                factory.create(
                        "1st",
                        invalidCodeDouble,
                        validPhysicalAttributesDouble,
                        validPublicationTypeIdDouble,
                        validPublisherIdDouble,
                        "Book",
                        Locale.ENGLISH,
                        LocalDate.of(2020,1,1),
                        typeOfBindingDouble
                ));
    }

    // ─────────────────────────────────────────────
    // RECREATE - HAPPY PATH
    // ─────────────────────────────────────────────

    @Test
    void reCreate_shouldPreserveId() {

        // Arrange
        factory = new EditionFactory();

        EditionId editionIdDouble = mock(EditionId.class);

        // Act
        Edition edition = factory.create(
                editionIdDouble,
                "1st",
                validCodeDouble,
                validPhysicalAttributesDouble,
                validPublicationTypeIdDouble,
                validPublisherIdDouble,
                "Book",
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1),
                typeOfBindingDouble
        );

        // Assert
        assertEquals(editionIdDouble, edition.id());
    }

    @Test
    void reCreate_shouldKeepSameAttributes() {

        // Arrange
        factory = new EditionFactory();
        EditionId editionIdDouble = mock(EditionId.class);

        // Act
        Edition edition = factory.create(
                editionIdDouble,
                "1st",
                validCodeDouble,
                validPhysicalAttributesDouble,
                validPublicationTypeIdDouble,
                validPublisherIdDouble,
                "Book",
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1),
                typeOfBindingDouble
        );

        // Assert
        assertEquals("1st", edition.getEditionNumber());
        assertEquals(validCodeDouble, edition.getCode());
        assertEquals(validPhysicalAttributesDouble, edition.getPhysicalAttributes());
        assertEquals(validPublicationTypeIdDouble, edition.getPublicationTypeID());
        assertEquals(validPublisherIdDouble, edition.getPublisherID());
    }

    // ─────────────────────────────────────────────
    // RECREATE - VALIDATIONS
    // ─────────────────────────────────────────────

    @Test
    void reCreate_shouldThrow_whenIdIsNull() {

        // Arrange
        factory = new EditionFactory();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                factory.create(
                        null,
                        "1st",
                        validCodeDouble,
                        validPhysicalAttributesDouble,
                        validPublicationTypeIdDouble,
                        validPublisherIdDouble,
                        "Book",
                        Locale.ENGLISH,
                        LocalDate.of(2020, 1, 1),
                        typeOfBindingDouble
                ));
    }

    @Test
    void reCreate_shouldThrow_whenInvalidMediaFormat() {

        // Arrange
        factory = new EditionFactory();
        EditionId editionIdDouble = mock(EditionId.class);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                factory.create(
                        editionIdDouble,
                        "1st",
                        validCodeDouble,
                        validPhysicalAttributesDouble,
                        validPublicationTypeIdDouble,
                        validPublisherIdDouble,
                        "INVALID",
                        Locale.ENGLISH,
                        LocalDate.of(2020, 1, 1),
                        typeOfBindingDouble
                ));
    }

    @Test
    void reCreate_shouldValidateCodeRules() {

        // Arrange
        factory = new EditionFactory();
        EditionId editionIdDouble = mock(EditionId.class);

        Code invalidCodeDouble = mock(Code.class);
        when(invalidCodeDouble.getIsbn13()).thenReturn(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                factory.create(
                        editionIdDouble,
                        "1st",
                        invalidCodeDouble,
                        validPhysicalAttributesDouble,
                        validPublicationTypeIdDouble,
                        validPublisherIdDouble,
                        "Book",
                        Locale.ENGLISH,
                        LocalDate.of(2020, 1, 1),
                        typeOfBindingDouble
                ));
    }
}