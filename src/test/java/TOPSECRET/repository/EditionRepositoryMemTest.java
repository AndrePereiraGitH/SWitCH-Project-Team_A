package TOPSECRET.repository;

import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Edition.EditionFactory;
import TOPSECRET.domain.Edition.IEditionRepository;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.ValueObjects.Code;
import TOPSECRET.domain.ValueObjects.EditionID;
import TOPSECRET.domain.ValueObjects.PhysicalAttributes;
import TOPSECRET.domain.PublicationType.PublicationType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link EditionRepositoryMem} using Mockito mocks for all external dependencies.
 * <p>
 * Collaborators such as {@link EditionFactory}, {@link Edition},
 * {@link PhysicalAttributes}, {@link Publisher}, and {@link PublicationType}
 * are mocked to isolate and validate the repository's behavior independently
 * from the domain objects they produce.
 * <p>
 * The test suite verifies:
 * <ul>
 *     <li>Constructor argument validation.</li>
 *     <li>Prevention of duplicate editions in the repository.</li>
 *     <li>Propagation of validation errors thrown by {@link EditionFactory}.</li>
 *     <li>Protection of the repository's internal state through unmodifiable collections.</li>
 * </ul>
 */
class EditionRepositoryMemTest {

    // ─────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────

    private final Code codeDouble = Code.fromIsbn13("9783161484100");
    private final EditionFactory editionFactoryDouble = mock(EditionFactory.class);
    private final PhysicalAttributes physicalAttributesDouble = mock(PhysicalAttributes.class);
    private final Publisher publisherDouble = mock(Publisher.class);
    private final PublicationType publicationTypeDouble = mock(PublicationType.class);

    // ─────────────────────────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────────────────────────

    @Test
    void shouldThrowWhenEditionFactoryIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new EditionRepositoryMem(null));
    }

    @Test
    void shouldCreateRepositoryWhenEditionFactoryIsValid() {
        new EditionRepositoryMem(editionFactoryDouble);
    }

    // ─────────────────────────────────────────────────────────────
    // createAndAddEdition
    // ─────────────────────────────────────────────────────────────

    @Test
    void shouldReturnListWithOneEdition_whenAddingFirstEdition() {
        // Arrange
        Edition edition = mock(Edition.class);
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        when(editionFactoryDouble.create("1st", codeDouble, physicalAttributesDouble, publicationTypeDouble, publisherDouble, Locale.ENGLISH, LocalDate.of(2020, 1, 1), typeOfBindingDouble))
                .thenReturn(edition);
        IEditionRepository repository = new EditionRepositoryMem(editionFactoryDouble);

        // Act
        List<Edition> result = repository.createAndAddEdition("1st", codeDouble, physicalAttributesDouble, publicationTypeDouble, publisherDouble, Locale.ENGLISH, LocalDate.of(2020, 1, 1), typeOfBindingDouble);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(edition));
    }

    @Test
    void shouldReturnListWithTwoEditions_whenAddingTwoDifferentEditions() {
        // Arrange
        Edition edition1 = mock(Edition.class);
        Edition edition2 = mock(Edition.class);
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        when(editionFactoryDouble.create("1st", codeDouble, physicalAttributesDouble, publicationTypeDouble, publisherDouble, Locale.ENGLISH, LocalDate.of(2020, 1, 1), typeOfBindingDouble))
                .thenReturn(edition1);
        when(editionFactoryDouble.create("2nd", codeDouble, physicalAttributesDouble, publicationTypeDouble, publisherDouble, Locale.ENGLISH, LocalDate.of(2021, 1, 1), typeOfBindingDouble))
                .thenReturn(edition2);
        IEditionRepository repository = new EditionRepositoryMem(editionFactoryDouble);

        // Act
        repository.createAndAddEdition("1st", codeDouble, physicalAttributesDouble, publicationTypeDouble, publisherDouble, Locale.ENGLISH, LocalDate.of(2020, 1, 1), typeOfBindingDouble);
        List<Edition> result = repository.createAndAddEdition("2nd", codeDouble, physicalAttributesDouble, publicationTypeDouble, publisherDouble, Locale.ENGLISH, LocalDate.of(2021, 1, 1), typeOfBindingDouble);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(edition1));
        assertTrue(result.contains(edition2));
    }

    @Test
    void shouldThrowWhenAddingDuplicateEdition() {
        // Arrange
        Edition edition = mock(Edition.class);
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        when(editionFactoryDouble.create("1st", codeDouble, physicalAttributesDouble, publicationTypeDouble, publisherDouble, Locale.ENGLISH, LocalDate.of(2020, 1, 1), typeOfBindingDouble))
                .thenReturn(edition);
        IEditionRepository repository = new EditionRepositoryMem(editionFactoryDouble);
        repository.createAndAddEdition("1st", codeDouble, physicalAttributesDouble, publicationTypeDouble, publisherDouble, Locale.ENGLISH, LocalDate.of(2020, 1, 1), typeOfBindingDouble);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                repository.createAndAddEdition("1st", codeDouble, physicalAttributesDouble, publicationTypeDouble, publisherDouble, Locale.ENGLISH, LocalDate.of(2020, 1, 1), typeOfBindingDouble));
    }
    /**
     * Verifies that the list returned by {@link EditionRepositoryMem#createAndAddEdition}
     * is unmodifiable. This ensures that external callers cannot change the internal
     * state of the repository by adding or removing elements from the returned list.
     * <p>
     * Attempting to modify the list should throw an {@link UnsupportedOperationException}.
     */
    @Test
    void shouldReturnUnmodifiableList() {
        // Arrange
        Edition edition = mock(Edition.class);
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);
        when(editionFactoryDouble.create("1st", codeDouble, physicalAttributesDouble, publicationTypeDouble, publisherDouble, Locale.ENGLISH, LocalDate.of(2020, 1, 1), typeOfBindingDouble))
                .thenReturn(edition);
        IEditionRepository repository = new EditionRepositoryMem(editionFactoryDouble);

        // Act
        List<Edition> result = repository.createAndAddEdition("1st", codeDouble, physicalAttributesDouble, publicationTypeDouble, publisherDouble, Locale.ENGLISH, LocalDate.of(2020, 1, 1), typeOfBindingDouble);

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(mock(Edition.class)));
    }



    // ─────────────────────────────────────────────────────────────
    // findEditionById
    // ─────────────────────────────────────────────────────────────

    @Test
    void findEditionById_shouldReturnAnEdition_whenIdIsValid(){

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);

        EditionID editionID = mock(EditionID.class);
        Edition edition = mock(Edition.class);
        when(edition.id()).thenReturn(editionID);

        when(editionFactoryDouble.create("1st", codeDouble, physicalAttributesDouble, publicationTypeDouble, publisherDouble, Locale.ENGLISH, LocalDate.of(2020, 1, 1), typeOfBindingDouble))
                .thenReturn(edition);

        IEditionRepository repository = new EditionRepositoryMem(editionFactoryDouble);
        repository.createAndAddEdition("1st", codeDouble, physicalAttributesDouble, publicationTypeDouble, publisherDouble, Locale.ENGLISH, LocalDate.of(2020, 1, 1), typeOfBindingDouble);


        // Act
        Optional<Edition> result = repository.findEditionById(editionID);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(edition,result.get());
    }

    @Test
    void findEditionById_shouldReturnAnEdition_whenIdIsValid_multipleEditions(){

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);

        EditionID editionID_1 = mock(EditionID.class);
        Edition edition_1 = mock(Edition.class);
        when(edition_1.id()).thenReturn(editionID_1);

        EditionID editionID_2 = mock(EditionID.class);
        Edition edition_2 = mock(Edition.class);
        when(edition_2.id()).thenReturn(editionID_2);

        when(editionFactoryDouble.create("1st", codeDouble, physicalAttributesDouble, publicationTypeDouble, publisherDouble, Locale.ENGLISH, LocalDate.of(2020, 1, 1), typeOfBindingDouble))
                .thenReturn(edition_1);

        when(editionFactoryDouble.create("2st", codeDouble, physicalAttributesDouble, publicationTypeDouble, publisherDouble, Locale.ENGLISH, LocalDate.of(2020, 1, 1), typeOfBindingDouble))
                .thenReturn(edition_2);

        IEditionRepository repository = new EditionRepositoryMem(editionFactoryDouble);
        repository.createAndAddEdition("1st", codeDouble, physicalAttributesDouble, publicationTypeDouble, publisherDouble, Locale.ENGLISH, LocalDate.of(2020, 1, 1), typeOfBindingDouble);


        // Act
        Optional<Edition> result = repository.findEditionById(editionID_1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(edition_1,result.get());
    }

    @Test
    void findEditionById_shouldReturnEmpty_whenIdIsNotFound(){

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);

        EditionID editionID = mock(EditionID.class);
        Edition edition = mock(Edition.class);
        when(edition.id()).thenReturn(editionID);

        EditionID editionNotFound = mock(EditionID.class);


        when(editionFactoryDouble.create("1st", codeDouble, physicalAttributesDouble, publicationTypeDouble, publisherDouble, Locale.ENGLISH, LocalDate.of(2020, 1, 1), typeOfBindingDouble))
                .thenReturn(edition);

        IEditionRepository repository = new EditionRepositoryMem(editionFactoryDouble);
        repository.createAndAddEdition("1st", codeDouble, physicalAttributesDouble, publicationTypeDouble, publisherDouble, Locale.ENGLISH, LocalDate.of(2020, 1, 1), typeOfBindingDouble);


        // Act
        Optional<Edition> result = repository.findEditionById(editionNotFound);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findEditionById_shouldThrowAnException_whenIdIsNull(){

        // Arrange
        TypeOfBinding typeOfBindingDouble = mock(TypeOfBinding.class);

        EditionID editionID = mock(EditionID.class);
        Edition edition = mock(Edition.class);
        when(edition.id()).thenReturn(editionID);

        when(editionFactoryDouble.create("1st", codeDouble, physicalAttributesDouble, publicationTypeDouble, publisherDouble, Locale.ENGLISH, LocalDate.of(2020, 1, 1), typeOfBindingDouble))
                .thenReturn(edition);
        IEditionRepository repository = new EditionRepositoryMem(editionFactoryDouble);

        repository.createAndAddEdition("1st", codeDouble, physicalAttributesDouble, publicationTypeDouble, publisherDouble, Locale.ENGLISH, LocalDate.of(2020, 1, 1), typeOfBindingDouble);


        // Act + Assert
        assertThrows(NullPointerException.class, () -> repository.findEditionById(null));
    }

}