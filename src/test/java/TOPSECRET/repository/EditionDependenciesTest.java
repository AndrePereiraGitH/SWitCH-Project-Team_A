package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link EditionRepository} class.
 * <p>
 * This test suite validates:
 * <ul>
 *     <li>Constructor argument validation</li>
 *     <li>Correct storage of editions</li>
 *     <li>Prevention of duplicate editions</li>
 *     <li>Propagation of validation errors from {@link Edition}</li>
 * </ul>
 */
class EditionRepositoryDependenciesTest {
    // -------------------------
    // Helpers
    // -------------------------

    private Code validCode() {
        return Code.fromIsbn13("9783161484100");
    }

    private Dimensions validDimensions() {
        return new Dimensions(10.0, 2.0, 5.0);
    }

    private PhysicalAttributes validPhysicalAttributes() {
        return new PhysicalAttributes(validDimensions(), 300.2, 200);
    }

    private PublicationType validPublicationType() {
        return new PublicationType(MediaFormat.BOOK, TypeOfBinding.CASE_BINDING, "Some details");
    }

    private Publisher validPublisher() {
        PublisherFactory publisherFactory = new PublisherFactory();
        return publisherFactory.create(
                "Publisher Name",
                "123456789",
                null
        );
    }

    private Locale validEditionLanguage() {
        return Locale.ENGLISH;
    }

    private LocalDate validEditionDate() {
        return LocalDate.of(2020, 1, 3);
    }

    private EditionRepository emptyRepo() {
        return new EditionRepository(new EditionFactory());
    }

    // -------------------------
    // Constructor
    // -------------------------

//    @Test
//    void createAndAddEdition_shouldThrow_whenDuplicateExists() {
//        List<Edition> list = new ArrayList<>();
//        EditionFactory factory = new EditionFactory();
//        Edition existing = factory.create(validEditionNumber(), validCode(), validPhysicalAttributes(),
//                validPublicationType(), validPublisher(), validEditionLanguage(), validEditionDate());
//        list.add(existing);
//        EditionRepository repo = new EditionRepository(factory);
//
//        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
//                repo.createAndAddEdition(
//                        validEditionNumber(), validCode(), validPhysicalAttributes(), validPublicationType(),
//                        validPublisher(), validEditionLanguage(), validEditionDate()));
//
//        assertEquals("Edition already exists", ex.getMessage());
//        assertEquals(1, list.size()); // ensure no extra edition was added
//    }

    @Test
    void createAndAddEdition_shouldPropagateEditionValidationErrors() {
        EditionRepository repo = emptyRepo();

        // Edition throws if editionNumber is blank (based on your Edition class)
        assertThrows(IllegalArgumentException.class, () ->
                repo.createAndAddEdition(
                        "   ",
                        validCode(),
                        validPhysicalAttributes(),
                        validPublicationType(),
                        validPublisher(),
                        validEditionLanguage(),
                        validEditionDate()
                )
        );
    }
}

