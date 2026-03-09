package TOPSECRET.repository;

import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.PublicationTypeFactory;
import TOPSECRET.domain.PublicationType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Unit tests for {@link PublicationTypeRepository}.
 * <p>
 * This test class verifies:
 * <ul>
 *     <li>Creation of publication types</li>
 *     <li>Successful addition of valid publication types</li>
 *     <li>Prevention of duplicate publication types</li>
 *     <li>Prevention of null publication types</li>
 *     <li>Correct retrieval of stored publication types</li>
 * </ul>
 */

public class PublicationTypeRepositoryDependenciesTest {

    /**
     * Publication type sample parameters to be used for tests
     */
    MediaFormat mediaFormat = MediaFormat.BOOK;
    TypeOfBinding typeOfBinding = TypeOfBinding.HARDBACK;
    String details = "Book with gold engravings";
    PublicationTypeFactory factory = new PublicationTypeFactory();


    /**
     * Verifies that a {@link PublicationTypeRepository} can be created,
     * a {@link PublicationType} can be added successfully,
     * and its attributes are correctly stored.
     */
    @Test
    void createPublicationTypeRepositoryAndAddPublicationTest()
    {
        PublicationType pubType;

        PublicationTypeRepository pubTypeRepo = new PublicationTypeRepository(factory);

        pubType = pubTypeRepo.create(mediaFormat, typeOfBinding, details);
        pubTypeRepo.addToList(pubType);

        assertNotNull(pubTypeRepo);
        assertEquals(pubType, pubTypeRepo.getAllPublicationTypes().get(0));
        assertEquals(mediaFormat, pubType.getMediaFormat());
        assertEquals(typeOfBinding, pubType.getTypeOfBinding());
        assertEquals(details, pubType.getDetails());
    }


    /**
     * Verifies that all added publication types are returned correctly
     * and that the repository size matches the number of inserted elements.
     */
    @Test
    void shouldReturnAllPublicationTypes()
    {
        PublicationTypeRepository pubTypeRepo = new PublicationTypeRepository(factory);

        PublicationType pt1;
        PublicationType pt2;

        pt1 = pubTypeRepo.create(mediaFormat, typeOfBinding, details);
        pubTypeRepo.addToList(pt1);

        pt2 = pubTypeRepo.create(mediaFormat, typeOfBinding, "different book");
        pubTypeRepo.addToList(pt2);


        List<PublicationType> result = pubTypeRepo.getAllPublicationTypes();

        assertEquals(2, result.size()); // If the 2 publication types were added, the size of the list should be 2
        assertTrue(result.contains(pt1));
        assertTrue(result.contains(pt2));
    }


    /**
     * Verifies that an empty repository returns a non-null,
     * but empty, list of publication types.
     */
    @Test
    void shouldReturnEmptyListWhenNoPublicationTypesExist()
    {
        PublicationTypeRepository repo = new PublicationTypeRepository(factory);

        List<PublicationType> result = repo.getAllPublicationTypes();

        assertNotNull(repo);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    /**
     * Verifies that adding a duplicate {@link PublicationType}
     * results in an {@link IllegalArgumentException}
     * and does not increase the repository size.
     */
    @Test
    void shouldReturnExceptionWhenAddingDuplicatePublicationType()
    {
        PublicationTypeRepository pubTypeRepo = new PublicationTypeRepository(factory);

        PublicationType pt1;
        PublicationType pt2;

        pt1 = pubTypeRepo.create(mediaFormat, typeOfBinding, details);
        pubTypeRepo.addToList(pt1);

        pt2 = pubTypeRepo.create(mediaFormat, typeOfBinding, details);

        assertThrows(IllegalArgumentException.class, () -> pubTypeRepo.addToList(pt2));
        assertEquals(1, pubTypeRepo.getAllPublicationTypes().size());
    }


    /**
     * Verifies that attempting to add a null publication type
     * throws an {@link IllegalArgumentException}.
     */
    @Test
    void shouldThrowWhenAddingNullPubType() {
        PublicationTypeRepository repo = new PublicationTypeRepository(factory);

        assertThrows(IllegalArgumentException.class, () -> repo.addToList(null));
    }


    /**
     * Verifies that the {@code addToList} method returns
     * the same {@link PublicationType} instance that was added.
     */
    @Test
    void shouldReturnTheAddedPublicationTypeWhenAddingToList() {

        PublicationTypeRepository repo = new PublicationTypeRepository(factory);

        PublicationType pubType = repo.create(mediaFormat, typeOfBinding, details);

        PublicationType returned = repo.addToList(pubType);

        assertNotNull(returned);
        assertSame(pubType, returned);
    }

    // US004 starts -------------

    /**
     * Verifies that findByAttributes returns correct PublicationType.
     */
    @Test
    void shouldReturnPublicationTypeWhenExists() {

        // ARRANGE
        PublicationTypeRepository repository =
                new PublicationTypeRepository(factory);

        PublicationType type =
                new PublicationType(
                        MediaFormat.BOOK,
                        TypeOfBinding.HARDBACK,
                        "Standard"
                );

        repository.addToList(type);

        // ACT
        PublicationType result =
                repository.findByAttributes(
                        MediaFormat.BOOK,
                        TypeOfBinding.HARDBACK,
                        "Standard"
                );

        // ASSERT
        assertEquals(type, result);
    }

    /**
     * Verifies that findByAttributes throws exception when not found.
     */
    @Test
    void shouldThrowExceptionWhenPublicationTypeNotFound() {

        // ARRANGE
        PublicationTypeRepository repository =
                new PublicationTypeRepository(factory);

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                repository.findByAttributes(
                        MediaFormat.MAGAZINE,
                        TypeOfBinding.PAPERBACK,
                        "Unknown"
                )
        );
    }

    /**
     * Verifies exception when mediaFormat is null.
     */
    @Test
    void shouldThrowExceptionWhenMediaFormatIsNull() {

        // ARRANGE
        PublicationTypeRepository repository =
                new PublicationTypeRepository(factory);

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                repository.findByAttributes(
                        null,
                        TypeOfBinding.HARDBACK,
                        "Standard"
                )
        );
    }

    /**
     * Verifies exception when typeOfBinding is null.
     */
    @Test
    void shouldThrowExceptionWhenTypeOfBindingIsNull() {

        // ARRANGE
        PublicationTypeRepository repository =
                new PublicationTypeRepository(factory);

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                repository.findByAttributes(
                        MediaFormat.BOOK,
                        null,
                        "Standard"
                )
        );
    }

    /**
     * Verifies exception when details is null.
     */
    @Test
    void shouldThrowExceptionWhenDetailsIsNull() {

        // ARRANGE
        PublicationTypeRepository repository =
                new PublicationTypeRepository(factory);

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                repository.findByAttributes(
                        MediaFormat.BOOK,
                        TypeOfBinding.HARDBACK,
                        null
                )
        );
    }

    /**
     * Verifies exception when details is blank.
     */
    @Test
    void shouldThrowExceptionWhenDetailsIsBlank() {

        // ARRANGE
        PublicationTypeRepository repository =
                new PublicationTypeRepository(factory);

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                repository.findByAttributes(
                        MediaFormat.BOOK,
                        TypeOfBinding.HARDBACK,
                        "   "
                )
        );
    }

    // ends ---------------------
}
