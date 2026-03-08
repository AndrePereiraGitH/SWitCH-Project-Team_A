package TOPSECRET.domain;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class EditionFactoryDependenciesTest {
    private final EditionFactory factory = new EditionFactory();

    // -------------------------
    // Helpers
    // -------------------------

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
        return new Publisher("Porto Editora", "123456789", validContact());
    }

    private PublicationType publicationTypeBook() {
        return new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "Hardcover edition");
    }

    private Code codeWithIsbn13() {
        return Code.fromIsbn13("978-3-16-148410-0");
    }

    // -------------------------
    // Tests
    // -------------------------

    @Test
    void create_shouldCreateEdition_whenValidData() {
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
    void create_shouldTrimEditionNumber() {
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