package TOPSECRET.domain.Edition;

import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.Publisher;
import TOPSECRET.domain.ValueObjects.Code;
import TOPSECRET.domain.ValueObjects.PhysicalAttributes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link EditionFactory} class.
 *
 * <p>This test suite verifies that the {@code EditionFactory} correctly creates
 * {@link Edition} objects when provided with valid input data. It also ensures
 * that specific behaviors such as trimming the edition number and correctly
 * assigning language are respected.</p>
 *
 * <p>Dependencies required by the factory are mocked to isolate the factory
 * behavior from external components.</p>
 */
class EditionFactoryTest {

// -------------------------
// Mocks
// -------------------------

    private PublicationType validPublicationType;
    private Code validCode;
    private final PhysicalAttributes validPhysicalAttributes = mock(PhysicalAttributes.class);
    private final Publisher validPublisher = mock(Publisher.class);

    @BeforeEach
    void setUp() {
        validPublicationType = mock(PublicationType.class);
        when(validPublicationType.getMediaFormat()).thenReturn("Book");
        validCode = mock(Code.class);
        when(validCode.getIsbn13()).thenReturn("9781234567890");
    }
    
// -------------------------
// Tests
// -------------------------

    @Test
    void shouldCreateEdition_whenValidEditionNumber() {
        EditionFactory factory = new EditionFactory();

        Edition edition = factory.create(
                "1st",
                validCode,
                validPhysicalAttributes,
                validPublicationType,
                validPublisher,
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1),
                TypeOfBinding.HARDBACK
        );

        assertNotNull(edition);
        assertEquals("1st", edition.getEditionNumber());

    }

    @Test
    void shouldCreateEdition_whenValidEditionLanguage() {
        EditionFactory factory = new EditionFactory();

        Edition edition = factory.create(
                "1st",
                validCode,
                validPhysicalAttributes,
                validPublicationType,
                validPublisher,
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1),
                TypeOfBinding.HARDBACK
        );

        assertNotNull(edition);
        assertEquals(Locale.ENGLISH, edition.getEditionLanguage());
    }

    @Test
    void shouldCreateEdition_whenValidEditionDate() {
        EditionFactory factory = new EditionFactory();

        Edition edition = factory.create(
                "1st",
                validCode,
                validPhysicalAttributes,
                validPublicationType,
                validPublisher,
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1),
                TypeOfBinding.HARDBACK
        );

        assertNotNull(edition);
        assertEquals(LocalDate.of(2020, 1, 1), edition.getEditionDate());
    }



    @Test
    void shouldTrimEditionNumber() {
        EditionFactory factory = new EditionFactory();
        Edition edition = factory.create(
                "   1st   ",
                validCode,
                validPhysicalAttributes,
                validPublicationType,
                validPublisher,
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1),
                TypeOfBinding.HARDBACK
        );

        assertEquals("1st", edition.getEditionNumber());
    }
}