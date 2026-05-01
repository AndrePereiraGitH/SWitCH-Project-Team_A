package TOPSECRET.persistence.mem;

import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.PublicationType.PublicationTypeFactory;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link PublicationTypeRepositoryMem}.
 *
 * <p>
 * Validates in-memory persistence behavior including:
 * </p>
 *
 * <ul>
 *     <li>Saving publication types</li>
 *     <li>Retrieving all entities</li>
 *     <li>Searching by ID</li>
 *     <li>Searching by media format</li>
 *     <li>Validation of invalid inputs</li>
 * </ul>
 *
 * <p>
 * Ensures correct handling of edge cases such as null values,
 * empty collections, and non-existing entities.
 * </p>
 */
class PublicationTypeRepositoryMemTest {

    @Test
    void shouldSavePublicationType() {

        // ARRANGE
        IPublicationTypeRepository repo = new PublicationTypeRepositoryMem();
        PublicationType pt = new PublicationTypeFactory().create("Book");

        // ACT
        PublicationType result = repo.save(pt);

        // ASSERT
        assertNotNull(result);
        assertEquals(pt, result);
    }

    @Test
    void shouldThrowWhenSavingNull() {

        // ARRANGE
        IPublicationTypeRepository repo = new PublicationTypeRepositoryMem();

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> repo.save(null));
    }

    @Test
    void shouldReturnAllPublicationTypes() {

        // ARRANGE
        IPublicationTypeRepository repo = new PublicationTypeRepositoryMem();
        PublicationType pt = new PublicationTypeFactory().create("Book");

        repo.save(pt);

        // ACT
        List<PublicationType> result = repo.findAll();

        // ASSERT
        assertEquals(1, result.size());
        assertEquals(pt, result.get(0));
    }

    @Test
    void shouldReturnEmptyListWhenRepositoryIsEmpty() {

        // ARRANGE
        IPublicationTypeRepository repo = new PublicationTypeRepositoryMem();

        // ACT
        List<PublicationType> result = repo.findAll();

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllShouldReturnDefensiveCopy() {

        // ARRANGE
        IPublicationTypeRepository repo = new PublicationTypeRepositoryMem();
        PublicationType pt = new PublicationTypeFactory().create("Book");

        repo.save(pt);

        // ACT
        List<PublicationType> result = repo.findAll();

        // ASSERT
        assertThrows(UnsupportedOperationException.class,
                () -> result.add(new PublicationTypeFactory().create("Magazine")));
    }

    @Test
    void shouldFindByIdWhenExists() {

        // ARRANGE
        IPublicationTypeRepository repo = new PublicationTypeRepositoryMem();
        PublicationType pt = new PublicationTypeFactory().create("Book");

        repo.save(pt);
        PublicationTypeId id = pt.id();

        // ACT
        Optional<PublicationType> result = repo.findById(id);

        // ASSERT
        assertTrue(result.isPresent());
        assertEquals(pt, result.get());
    }

    @Test
    void shouldReturnEmptyWhenIdDoesNotExist() {

        // ARRANGE
        IPublicationTypeRepository repo = new PublicationTypeRepositoryMem();
        PublicationTypeId id = new PublicationTypeId("non-existing");

        // ACT
        Optional<PublicationType> result = repo.findById(id);

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowWhenIdIsNull() {

        // ARRANGE
        IPublicationTypeRepository repo = new PublicationTypeRepositoryMem();

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> repo.findById(null));
    }

    @Test
    void shouldFindByMediaFormatIgnoringCaseAndSpaces() {

        // ARRANGE
        IPublicationTypeRepository repo = new PublicationTypeRepositoryMem();
        PublicationType pt = new PublicationTypeFactory().create("Book");

        repo.save(pt);

        // ACT
        Optional<PublicationType> result = repo.findByMediaFormat("   book   ");

        // ASSERT
        assertTrue(result.isPresent());
        assertEquals(pt, result.get());
    }

    @Test
    void shouldReturnEmptyWhenMediaFormatDoesNotExist() {

        // ARRANGE
        IPublicationTypeRepository repo = new PublicationTypeRepositoryMem();

        // ACT
        Optional<PublicationType> result = repo.findByMediaFormat("Magazine");

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowWhenMediaFormatIsNullOrBlank() {

        // ARRANGE
        IPublicationTypeRepository repo = new PublicationTypeRepositoryMem();

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> repo.findByMediaFormat(null));

        assertThrows(IllegalArgumentException.class,
                () -> repo.findByMediaFormat(" "));
    }

    @Test
    void shouldReturnCorrectElementWhenMultipleIdsExist() {

        // ARRANGE
        PublicationTypeRepositoryMem repo = new PublicationTypeRepositoryMem();

        PublicationType pt1 = new PublicationTypeFactory().create("Book");
        PublicationType pt2 = new PublicationTypeFactory().create("Magazine");

        repo.save(pt1);
        repo.save(pt2);

        // ACT
        Optional<PublicationType> result = repo.findById(pt2.id());

        // ASSERT
        assertTrue(result.isPresent());
        assertEquals(pt2, result.get());
    }

    @Test
    void shouldReturnCorrectElementWhenMultipleFormatsExist() {

        // ARRANGE
        IPublicationTypeRepository repo = new PublicationTypeRepositoryMem();

        PublicationType pt1 = new PublicationTypeFactory().create("Book");
        PublicationType pt2 = new PublicationTypeFactory().create("Magazine");

        repo.save(pt1);
        repo.save(pt2);

        // ACT
        Optional<PublicationType> result = repo.findByMediaFormat("Magazine");

        // ASSERT
        assertTrue(result.isPresent());
        assertEquals(pt2, result.get());
    }
}