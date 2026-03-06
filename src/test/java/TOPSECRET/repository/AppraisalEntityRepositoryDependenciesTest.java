package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.dto.GenreDetailsDTO_US004;
import TOPSECRET.dto.PublicationTypeDTO_US004;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AppraisalEntityRepository.
 *
 * This test class verifies:
 * - Successful creation of AppraisalEntity.
 * - Prevention of duplicate entities (same name + contact).
 * - Exception when Genre does not exist in repository.
 * - Exception when PublicationType does not exist in repository.
 *
 * The tests follow AAA structure and reuse helpers
 * to reduce duplication.
 */
class AppraisalEntityRepositoryDependenciesTest {

    /**
     * Helper method to create a valid Contact.
     * @return valid Contact
     */
    private Contact createValidContact() {
        return new Contact("912345678", "351", null, null);
    }

    /**
     * Creates a GenreRepository containing one valid Genre.
     * @return GenreRepository instance.
     */
    private GenreRepository createGenreRepository() {
        GenreRepository repo = new GenreRepository(new ArrayList<>(),  new GenreFactory());
        repo.addGenre(new Genre("Fantasy"));
        return repo;
    }

    /**
     * Creates a PublicationTypeRepository containing one valid PublicationType.
     * @return PublicationTypeRepository instance.
     */
    private PublicationTypeRepository createPublicationTypeRepository() {
        PublicationTypeRepository repo = new PublicationTypeRepository();
        repo.addToList(new PublicationType(
                MediaFormat.BOOK,
                TypeOfBinding.HARDBACK,
                "Standard"
        ));
        return repo;
    }

    /**
     * Creates a valid list of Genre DTOs.
     *
     * @return List containing one valid GenreDetailsDTO_US004.
     */
    private List<GenreDetailsDTO_US004> createValidGenreDTOList() {
        return List.of(new GenreDetailsDTO_US004("Fantasy"));
    }

    /**
     * Creates a valid list of PublicationType DTOs.
     * @return List containing one valid PublicationTypeDTO_US004.
     */
    private List<PublicationTypeDTO_US004> createValidPublicationTypeDTOList() {
        return List.of(
                new PublicationTypeDTO_US004(
                        MediaFormat.BOOK,
                        TypeOfBinding.HARDBACK,
                        "Standard"
                )
        );
    }

    /**
     * Creates a valid AppraisalEntity Factory.
     */
    private AppraisalEntityFactory createAppraisalEntityFactory (){
        return new AppraisalEntityFactory();
    }

    /**
     * Verifies repository successfully creates and stores a new AppraisalEntity.
     */
    @Test
    void shouldCreateAndAddAppraisalEntitySuccessfully() {

        // ARRANGE
        GenreRepository genreRepo = createGenreRepository();
        PublicationTypeRepository pubRepo = createPublicationTypeRepository();
        AppraisalEntityFactory appraisalEntityFactory = createAppraisalEntityFactory();
        AppraisalEntityRepository repository =
                new AppraisalEntityRepository(genreRepo, pubRepo, appraisalEntityFactory);

        String name = "Entity One";
        Contact contact = createValidContact();

        // ACT
        AppraisalEntity result =
                repository.createAndAddAppraisalEntity(
                        name,
                        contact,
                        createValidGenreDTOList(),
                        createValidPublicationTypeDTOList()
                );

        // ASSERT
        assertEquals(name, result.getName());
        assertEquals(contact, result.getContact());
        assertEquals(1, result.getGenres().size());
        assertEquals(1, result.getPublicationTypes().size());
    }

    /**
     * Verifies repository throws exception when trying to create duplicate AppraisalEntity
     * using the same name + contact.
     */
    @Test
    void shouldThrowExceptionWhenAppraisalEntityAlreadyExists() {

        // ARRANGE
        GenreRepository genreRepo = createGenreRepository();
        PublicationTypeRepository pubRepo = createPublicationTypeRepository();
        AppraisalEntityFactory appraisalEntityFactory = createAppraisalEntityFactory();
        AppraisalEntityRepository repository =
                new AppraisalEntityRepository(genreRepo, pubRepo, appraisalEntityFactory);

        String name = "Entity One";
        Contact contact = createValidContact();

        repository.createAndAddAppraisalEntity(
                name,
                contact,
                createValidGenreDTOList(),
                createValidPublicationTypeDTOList()
        );

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                repository.createAndAddAppraisalEntity(
                        name,
                        contact,
                        createValidGenreDTOList(),
                        createValidPublicationTypeDTOList()
                )
        );

    }

    /**
     * Verifies that an exception is thrown
     * when the selected Genre does not exist
     * in the GenreRepository.
     */
    @Test
    void shouldThrowExceptionWhenGenreDoesNotExist() {

        // ARRANGE
        GenreRepository emptyGenreRepo = new GenreRepository(new ArrayList<>(),  new GenreFactory());

        PublicationTypeRepository pubRepo = createPublicationTypeRepository();

        AppraisalEntityFactory appraisalEntityFactory = createAppraisalEntityFactory();

        AppraisalEntityRepository repository = new AppraisalEntityRepository(emptyGenreRepo, pubRepo, appraisalEntityFactory);

        List<GenreDetailsDTO_US004> invalidGenre = List.of(new GenreDetailsDTO_US004("NonExisting"));

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                repository.createAndAddAppraisalEntity(
                        "Entity",
                        createValidContact(),
                        invalidGenre,
                        createValidPublicationTypeDTOList()
                )
        );
    }

    /**
     * Verifies that an exception is thrown
     * when the selected PublicationType does not exist
     * in the PublicationTypeRepository.
     */
    @Test
    void shouldThrowExceptionWhenPublicationTypeDoesNotExist() {

        // ARRANGE
        GenreRepository genreRepo = createGenreRepository();

        PublicationTypeRepository emptyPubRepo = new PublicationTypeRepository();

        AppraisalEntityFactory appraisalEntityFactory = createAppraisalEntityFactory();

        AppraisalEntityRepository repository = new AppraisalEntityRepository(genreRepo, emptyPubRepo, appraisalEntityFactory);

        List<PublicationTypeDTO_US004> invalidType =
                List.of(
                        new PublicationTypeDTO_US004(
                                MediaFormat.MAGAZINE,
                                TypeOfBinding.PAPERBACK,
                                "Invalid"
                        )
                );

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                repository.createAndAddAppraisalEntity(
                        "Entity",
                        createValidContact(),
                        createValidGenreDTOList(),
                        invalidType
                )
        );
    }

    /**
     * Verifies exception when Genre list is null.
     */
    @Test
    void shouldThrowExceptionWhenGenreListIsNull() throws IllegalArgumentException {

        GenreRepository genreRepo = createGenreRepository();
        PublicationTypeRepository pubRepo = createPublicationTypeRepository();
        AppraisalEntityFactory appraisalEntityFactory = createAppraisalEntityFactory();

        AppraisalEntityRepository repository =
                new AppraisalEntityRepository(genreRepo, pubRepo, appraisalEntityFactory);

        assertThrows(IllegalArgumentException.class, () ->
                repository.createAndAddAppraisalEntity(
                        "Entity",
                        createValidContact(),
                        null,
                        createValidPublicationTypeDTOList()
                )
        );
    }

    /**
     * Verifies exception when Genre list is empty.
     */
    @Test
    void shouldThrowExceptionWhenGenreListIsEmpty() {

        GenreRepository genreRepo = createGenreRepository();
        PublicationTypeRepository pubRepo = createPublicationTypeRepository();
        AppraisalEntityFactory appraisalEntityFactory = createAppraisalEntityFactory();

        AppraisalEntityRepository repository =
                new AppraisalEntityRepository(genreRepo, pubRepo, appraisalEntityFactory);

        assertThrows(IllegalArgumentException.class, () ->
                repository.createAndAddAppraisalEntity(
                        "Entity",
                        createValidContact(),
                        List.of(),
                        createValidPublicationTypeDTOList()
                )
        );
    }

    /**
     * Verifies exception when PublicationType list is null.
     */
    @Test
    void shouldThrowExceptionWhenPublicationTypeListIsNull() throws IllegalArgumentException  {

        GenreRepository genreRepo = createGenreRepository();
        PublicationTypeRepository pubRepo = createPublicationTypeRepository();
        AppraisalEntityFactory appraisalEntityFactory = createAppraisalEntityFactory();

        AppraisalEntityRepository repository =
                new AppraisalEntityRepository(genreRepo, pubRepo, appraisalEntityFactory);

        assertThrows(IllegalArgumentException.class, () ->
                repository.createAndAddAppraisalEntity(
                        "Entity",
                        createValidContact(),
                        createValidGenreDTOList(),
                        null
                )
        );
    }

    /**
     * Verifies exception when PublicationType list is empty.
     */
    @Test
    void shouldThrowExceptionWhenPublicationTypeListIsEmpty() throws IllegalArgumentException {

        GenreRepository genreRepo = createGenreRepository();
        PublicationTypeRepository pubRepo = createPublicationTypeRepository();
        AppraisalEntityFactory appraisalEntityFactory = createAppraisalEntityFactory();

        AppraisalEntityRepository repository =
                new AppraisalEntityRepository(genreRepo, pubRepo, appraisalEntityFactory);

        assertThrows(IllegalArgumentException.class, () ->
                repository.createAndAddAppraisalEntity(
                        "Entity",
                        createValidContact(),
                        createValidGenreDTOList(),
                        List.of()
                )
        );
    }
}