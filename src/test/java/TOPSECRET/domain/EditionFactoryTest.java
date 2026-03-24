package TOPSECRET.domain;

import TOPSECRET.domain.ValueObjects.Code;
import TOPSECRET.domain.ValueObjects.PhysicalAttributes;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
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

    private final PublicationType validPublicationType = mock(PublicationType.class);
    private final Code validCode = mock(Code.class);
    private final PhysicalAttributes validPhysicalAttributes = mock(PhysicalAttributes.class);
    private final Publisher validPublisher = mock(Publisher.class);

    
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
                LocalDate.of(2020, 1, 1)
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
                LocalDate.of(2020, 1, 1)
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
                LocalDate.of(2020, 1, 1)
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
                LocalDate.of(2020, 1, 1)
        );

        assertEquals("1st", edition.getEditionNumber());
    }
}