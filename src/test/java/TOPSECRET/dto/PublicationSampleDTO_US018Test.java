package TOPSECRET.dto;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PublicationSampleDTO_US018.
 *
 * <p>
 * Validates:
 * </p>
 * <ul>
 *   <li>Constructor and getters</li>
 *   <li>Conversion from domain PublicationSample to DTO</li>
 *   <li>Behavior with empty lists</li>
 * </ul>
 *
 * Tests follow the AAA pattern.
 */
class PublicationSampleDTO_US018Test {


    // ============================
    // Helpers
    // ============================

    private User createValidUser() {
        Profile profile = new Profile(
                "Test User",
                new Country("PORTUGAL"),
                LocalDate.of(1990, 1, 1)
        );

        return new User(
                "user1",
                new Contact("912345678", "351", null, null),
                profile,
                UserRole.ADMIN
        );
    }

    private PublicationSample createValidPublicationSample(User user) {

        Profile authorProfile = new Profile(
                "Author",
                new Country("PORTUGAL"),
                LocalDate.of(1980, 1, 1)
        );

        Author author = authorFactory.createAuthorWithExistingProfile(authorProfile, "Short bio");

        BibliographicalData bd = new BibliographicalData(
                "Test Title",
                List.of(author),
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1),
                List.of(new Genre("Fiction")),
                "Sample text"
        );

        Code code = Code.fromIsbn13("9783161484100");

        Dimensions dimensions = new Dimensions(10.0, 2.0, 5.0);
        PhysicalAttributes physicalAttributes = new PhysicalAttributes(dimensions, 400.0, 250);

        PublicationType pubType = new PublicationType(
                MediaFormat.BOOK,
                TypeOfBinding.HARDBACK,
                "Standard"
        );

        Publisher publisher = new Publisher("Test Publisher", "PT123456789", null);

        Edition edition = editionFactory.create(
                "1st",
                code,
                physicalAttributes,
                pubType,
                publisher,
                Locale.ENGLISH,
                LocalDate.of(2020, 1, 1)
        );

        return new PublicationSample(
                bd,
                user,
                PublicationStatus.READING,
                edition,
                Condition.NEW
        );
    }

    private final AuthorFactory authorFactory = new AuthorFactory();
    private final EditionFactory editionFactory = new EditionFactory();

    @Test
    void shouldCreateDTOWithCorrectValues() {

        // Arrange
        PublicationSampleDTO_US018 dto =
                new PublicationSampleDTO_US018("Test Title", "1st", "user1");

        // Act & Assert
        assertEquals("Test Title", dto.getTitle());
        assertEquals("1st", dto.getEditionNumber());
        assertEquals("user1", dto.getOwnerUsername());
    }

    @Test
    void shouldConvertPublicationSampleToDTO() {

        // Arrange
        User user = createValidUser();
        PublicationSample sample = createValidPublicationSample(user);

        // Act
        List<PublicationSampleDTO_US018> result =
                PublicationSampleDTO_US018.publicationToDTO(List.of(sample));

        // Assert
        assertEquals(1, result.size());
        assertEquals("Test Title", result.get(0).getTitle());
        assertEquals("1st", result.get(0).getEditionNumber());
        assertEquals(user.getUserName(), result.get(0).getOwnerUsername());
    }

    @Test
    void shouldReturnEmptyListWhenPublicationListIsEmpty() {

        // Arrange
        List<PublicationSample> emptyList = List.of();

        // Act
        List<PublicationSampleDTO_US018> result =
                PublicationSampleDTO_US018.publicationToDTO(emptyList);

        // Assert
        assertTrue(result.isEmpty());
    }

}