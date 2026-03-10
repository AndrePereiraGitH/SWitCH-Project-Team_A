package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class EditionFactoryTest {
    private final EditionFactory factory = new EditionFactory();

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
    void shouldCreateEdition_whenValidData() {
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
        assertEquals(Locale.ENGLISH, edition.getEditionLanguage());
        assertEquals(LocalDate.of(2020, 1, 1), edition.getEditionDate());
    }

    @Test
    void shouldTrimEditionNumber() {
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