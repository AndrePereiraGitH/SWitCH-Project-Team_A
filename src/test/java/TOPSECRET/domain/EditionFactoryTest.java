package TOPSECRET.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EditionFactoryTest {
    private final EditionFactory factory = new EditionFactory();

    // -------------------------
    // Mocks
    // -------------------------

    private PublicationType validPublicationType() {
        return mock(PublicationType.class);
    }

    private Code validCode() {
        return mock(Code.class);
    }

    private PhysicalAttributes validPhysicalAttributes() {
        return mock(PhysicalAttributes.class);
    }

    private Contact validContact() {
        return mock(Contact.class);
    }

    private Publisher validPublisher() {
        Publisher publisher = mock(Publisher.class);
        when(publisher.getContact()).thenReturn(validContact());
        return publisher;
    }

    // -------------------------
    // Tests
    // -------------------------

    @Test
    void shouldCreateEdition_whenValidData() {
        Edition edition = factory.create(
                "1st",
                validCode(),
                validPhysicalAttributes(),
                validPublicationType(),
                validPublisher(),
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
                validCode(),
                validPhysicalAttributes(),
                validPublicationType(),
                validPublisher(),
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1)
        );

        assertEquals("1st", edition.getEditionNumber());
    }
}