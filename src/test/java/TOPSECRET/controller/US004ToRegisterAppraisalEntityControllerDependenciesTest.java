/*package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.Contact;
import TOPSECRET.dto.GenreDetailsDTO_US004;
import TOPSECRET.dto.PublicationTypeDTO_US004;
import TOPSECRET.repository.AppraisalEntityRepository;
import TOPSECRET.repository.AppraisalEntityRepositoryMem;
import TOPSECRET.repository.GenreRepositoryMem;
import TOPSECRET.repository.PublicationTypeRepositoryMem;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for US004ToRegisterAppraisalEntityController.
 * Validates:
 * - Retrieval of Genres and PublicationTypes as DTOs.
 * - Successful registration flow.
 * - Validation rules for empty selections.
 * - Duplicate AppraisalEntity prevention.
 * - Exception when Genre does not exist.
 * - Exception when PublicationType does not exist.

class US004ToRegisterAppraisalEntityControllerDependenciesTest {

    /**
     * Creates a GenreRepository with predefined Genres.

    private GenreRepositoryMem createGenreRepository() {
        GenreRepositoryMem repo = new GenreRepositoryMem(new GenreFactory());
        repo.addGenre(new GenreFactory().create("Fantasy"));
        repo.addGenre(new GenreFactory().create("History"));
        return repo;
    }

    /**
     * Creates a PublicationTypeRepositoryMem with predefined PublicationTypes.

    private PublicationTypeRepositoryMem createPublicationTypeRepository() {
        PublicationTypeFactory factory = new PublicationTypeFactory();
        PublicationTypeRepositoryMem repo = new PublicationTypeRepositoryMem(factory);
        repo.addToList(factory.create("Book"));
        repo.addToList(factory.create("Book"));
        return repo;
    }

    /**
     * Creates a valid AppraisalEntity Factory.

    private AppraisalEntityFactory createAppraisalEntityFactory (){
        return new AppraisalEntityFactory();
    }

    /**
     * Creates a valid Contact object.

    private Contact createContact() {
        return new Contact("912345678", "351", null, null);
    }

    /**
     * Creates the controller with repositories.

    private US004ToRegisterAppraisalEntityController createController() {

        GenreRepositoryMem genreRepo = createGenreRepository();
        PublicationTypeRepositoryMem pubRepo = createPublicationTypeRepository();
        AppraisalEntityFactory appraisalEntityFactory = createAppraisalEntityFactory();

        AppraisalEntityRepository appraisalRepo =
                new AppraisalEntityRepositoryMem(genreRepo, pubRepo, appraisalEntityFactory);

        return new US004ToRegisterAppraisalEntityController(
                genreRepo,
                pubRepo,
                appraisalRepo
        );
    }

    /**
     * Verifies that all genres are correctly retrieved as DTOs.

    @Test
    void shouldReturnAllGenresAsDTO() {

        // ARRANGE
        var controller = createController();

        // ACT
        List<GenreDetailsDTO_US004> result = controller.getAllGenres();

        // ASSERT
        assertEquals(2, result.size());
        assertEquals("Fantasy", result.get(0).getTitle());
        assertEquals("History", result.get(1).getTitle());
    }

    /**
     * Verifies that all publication types are retrieved as DTOs.

    @Test
    void shouldReturnAllPublicationTypesAsDTO() {

        // ARRANGE
        var controller = createController();

        // ACT
        List<PublicationTypeDTO_US004> result = controller.getAllPublicationTypes();

        // ASSERT
        assertEquals(2, result.size());
        assertEquals(mediaFormat, result.get(0).getMediaFormat());
    }

    /**
     * Verifies successful registration of AppraisalEntity.

    @Test
    void shouldRegisterAppraisalEntitySuccessfully() {

        // ARRANGE
        var controller = createController();

        var genreDTO = new GenreDetailsDTO_US004("Fantasy");
        var pubDTO = new PublicationTypeDTO_US004(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "Standard");

        // ACT
        AppraisalEntity entity = controller.registerAppraisalEntity(
                "Entity1",
                createContact(),
                List.of(genreDTO),
                List.of(pubDTO)
        );

        // ASSERT
        assertEquals("Entity1", entity.getName());
        assertEquals(1, entity.getGenres().size());
        assertEquals(1, entity.getPublicationTypes().size());
    }

    /**
     * Verifies exception when no genres are selected.

    @Test
    void shouldThrowExceptionWhenGenresAreEmpty() {

        // ARRANGE
        var controller = createController();

        // ACT + ASSERT
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                controller.registerAppraisalEntity(
                        "Entity",
                        createContact(),
                        List.of(),
                        List.of(new PublicationTypeDTO_US004(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "Standard"))
                )
        );

        assertEquals("At least one Genre must be selected.", ex.getMessage());
    }

    /**
     * Verifies exception when no publication types are selected.

    @Test
    void shouldThrowExceptionWhenPublicationTypesAreEmpty() {

        // ARRANGE
        var controller = createController();

        // ACT + ASSERT
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                controller.registerAppraisalEntity(
                        "Entity",
                        createContact(),
                        List.of(new GenreDetailsDTO_US004("Fantasy")),
                        List.of()
                )
        );

        assertEquals("At least one PublicationType must be selected.", ex.getMessage());
    }

    /**
     * Verifies that duplicate AppraisalEntity registration is not allowed.

    @Test
    void shouldThrowExceptionWhenDuplicateEntityIsRegistered() {

        // ARRANGE
        var controller = createController();

        var contact = createContact();

        var genreDTO = new GenreDetailsDTO_US004("Fantasy");
        var pubDTO = new PublicationTypeDTO_US004(
                MediaFormat.BOOK,
                TypeOfBinding.HARDBACK,
                "Standard"
        );

        controller.registerAppraisalEntity(
                "Entity1",
                createContact(),
                List.of(genreDTO),
                List.of(pubDTO)
        );

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                controller.registerAppraisalEntity(
                        "Entity1",
                        contact,
                        List.of(genreDTO),
                        List.of(pubDTO)
                )
        );
    }

    /**
     * Verifies exception when a selected Genre does not exist in the repository.

    @Test
    void shouldThrowExceptionWhenGenreDoesNotExist() {

        // ARRANGE
        var controller = createController();

        var genreDTO = new GenreDetailsDTO_US004("NonExisting");
        var pubDTO = new PublicationTypeDTO_US004(
                MediaFormat.BOOK,
                TypeOfBinding.HARDBACK,
                "Standard"
        );

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                controller.registerAppraisalEntity(
                        "Entity",
                        createContact(),
                        List.of(genreDTO),
                        List.of(pubDTO)
                )
        );
    }

    /**
     * Verifies exception when a selected PublicationType does not exist.

    @Test
    void shouldThrowExceptionWhenPublicationTypeDoesNotExist() {

        // ARRANGE
        var controller = createController();

        var genreDTO = new GenreDetailsDTO_US004("Fantasy");
        var pubDTO = new PublicationTypeDTO_US004(
                MediaFormat.MAGAZINE,
                TypeOfBinding.HARDBACK,
                "NonExisting"
        );

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                controller.registerAppraisalEntity(
                        "Entity",
                        createContact(),
                        List.of(genreDTO),
                        List.of(pubDTO)
                )
        );
    }

    @Test
    void shouldThrowWhenGenreRepositoryIsNull() {

        PublicationTypeRepositoryMem pubRepo = createPublicationTypeRepository();
        AppraisalEntityFactory appraisalEntityFactory = createAppraisalEntityFactory();
        AppraisalEntityRepository appraisalRepo =
                new AppraisalEntityRepositoryMem(createGenreRepository(), pubRepo, appraisalEntityFactory);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new US004ToRegisterAppraisalEntityController(
                        null,
                        pubRepo,
                        appraisalRepo
                )
        );

        assertEquals("GenreRepository cannot be null.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenPublicationTypeRepositoryIsNull() {

        GenreRepositoryMem genreRepo = createGenreRepository();
        AppraisalEntityFactory appraisalEntityFactory = createAppraisalEntityFactory();
        AppraisalEntityRepository appraisalRepo =
                new AppraisalEntityRepositoryMem(genreRepo, createPublicationTypeRepository(), appraisalEntityFactory);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new US004ToRegisterAppraisalEntityController(
                        genreRepo,
                        null,
                        appraisalRepo
                )
        );

        assertEquals("PublicationTypeRepositoryMem cannot be null.", ex.getMessage());
    }

    @Test
    void shouldThrowWhenAppraisalEntityRepositoryIsNull() {

        GenreRepositoryMem genreRepo = createGenreRepository();
        PublicationTypeRepositoryMem pubRepo = createPublicationTypeRepository();

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new US004ToRegisterAppraisalEntityController(
                        genreRepo,
                        pubRepo,
                        null
                )
        );

        assertEquals("AppraisalEntityRepository cannot be null.", ex.getMessage());
    }

}
*/
