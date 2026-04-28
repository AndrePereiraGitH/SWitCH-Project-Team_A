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

        factory = new EditionFactory();

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
        EditionID editionIDDouble = mock(EditionID.class);

        // Act
        Edition edition = factory.create(
                editionIDDouble,
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
        assertEquals(editionIDDouble, edition.id());
    }

    @Test
    void reCreate_shouldKeepSameAttributes() {

        // Arrange
        EditionID editionIDDouble = mock(EditionID.class);

        // Act
        Edition edition = factory.create(
                editionIDDouble,
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
        EditionID editionIdDouble = mock(EditionID.class);

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
        EditionID editionIdDouble = mock(EditionID.class);

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