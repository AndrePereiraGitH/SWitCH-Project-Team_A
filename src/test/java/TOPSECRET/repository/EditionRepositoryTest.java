package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.MediaFormat;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link EditionRepository} using Mockito mocks for all dependencies.
 * <p>
 * Dependencies such as {@link EditionFactory}, {@link Edition},
 * {@link PhysicalAttributes}, {@link Publisher}, and {@link PublicationType}
 * are mocked to isolate repository logic from its collaborators.
 * <ul>
 *     <li>Constructor argument validation</li>
 *     <li>Duplicate edition prevention</li>
 *     <li>Propagation of validation errors from {@link EditionFactory}</li>
 * </ul>
 */
class EditionRepositoryTest {

    // ─────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────
    private Code validCode() { return Code.fromIsbn13("9783161484100"); }
    private PhysicalAttributes validPhysicalAttributes() { return mock(PhysicalAttributes.class); }
    private Publisher validPublisher() { return mock(Publisher.class); }
    private Locale validEditionLanguage() { return Locale.ENGLISH; }
    private LocalDate validEditionDate() { return LocalDate.of(2020, 1, 3); }

    private PublicationType validPublicationType() {
        PublicationType mock = mock(PublicationType.class);
        when(mock.getMediaFormat()).thenReturn(MediaFormat.BOOK);
        return mock;
    }

    // ─────────────────────────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────────────────────────


    // ─────────────────────────────────────────────────────────────
    // createAndAddEdition
    // ─────────────────────────────────────────────────────────────


//    /**
//     * Verifies that {@link EditionRepository#createAndAddEdition} throws
//     * {@link IllegalArgumentException} when the produced edition already exists.
//     *
//     * <p>{@code contains()} relies on {@code equals()}, which cannot be stubbed
//     * in Mockito. Instead, the same mock instance returned by the factory is
//     * pre-loaded into the list — {@code contains()} then finds it via reference
//     * equality, correctly triggering the duplicate check.</p>
//     */
//    @Test
//    void createAndAddEdition_shouldThrow_whenDuplicateExists() {
//        // Arrange
//        Edition existingEdition = mock(Edition.class);
//
//        EditionFactory factory = mock(EditionFactory.class);
//        when(factory.create(any(), any(), any(), any(), any(), any(), any()))
//                .thenReturn(existingEdition); // same instance = contains() returns true
//
//        List<Edition> list = new ArrayList<>();
//        list.add(existingEdition);
//
//        EditionRepository repo = new EditionRepository(factory);
//
//        // Act & Assert
//        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
//                repo.createAndAddEdition(validEditionNumber(), validCode(), validPhysicalAttributes(),
//                        validPublicationType(), validPublisher(), validEditionLanguage(), validEditionDate()));
//
//        assertEquals("Edition already exists", ex.getMessage());
//        assertEquals(1, list.size());
//    }

    /**
     * Verifies that {@link EditionRepository#createAndAddEdition} propagates
     * {@link IllegalArgumentException} thrown by {@link EditionFactory#create}.
     *
     * <p>The factory mock is stubbed to throw before the repository can add
     * anything, ensuring validation errors from the factory bubble up correctly.</p>
     */
    @Test
    void createAndAddEdition_shouldPropagateEditionValidationErrors() {
        // Arrange
        EditionFactory factory = mock(EditionFactory.class);           // single instance
        when(factory.create(any(), any(), any(), any(), any(), any(), any()))
                .thenThrow(new IllegalArgumentException("Edition Number cannot be blank"));

        EditionRepository repo = new EditionRepository(factory); // same factory

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.createAndAddEdition("   ", validCode(), validPhysicalAttributes(),
                        validPublicationType(), validPublisher(), validEditionLanguage(), validEditionDate()));
    }
}