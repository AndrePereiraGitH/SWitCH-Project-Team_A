package TOPSECRET.controller;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.dto.GenreDetailsDTO_US004;
import TOPSECRET.dto.PublicationTypeDTO_US004;
import TOPSECRET.repository.AppraisalEntityRepository;
import TOPSECRET.repository.GenreRepository;
import TOPSECRET.repository.PublicationTypeRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for US004ToRegisterAppraisalEntityController.
 * Validates:
 * - Retrieval of Genres and PublicationTypes
 * - Registration flow
 * - Validation rules
 */
class US004ToRegisterAppraisalEntityControllerTest {

    private GenreRepository createGenreRepository() {
        GenreRepository repo = new GenreRepository(new ArrayList<>());
        repo.addGenre(new Genre("Fantasy"));
        repo.addGenre(new Genre("History"));
        return repo;
    }

    private PublicationTypeRepository createPublicationTypeRepository() {
        PublicationTypeRepository repo = new PublicationTypeRepository();
        repo.addToList(new PublicationType(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "Standard"));
        repo.addToList(new PublicationType(MediaFormat.BOOK, TypeOfBinding.PAPERBACK, "Pocket"));
        return repo;
    }

    private Contact createContact() {
        return new Contact("912345678", "351", null, null);
    }

    // Helper to create a valid PublicationType
    private PublicationType createValidPublicationType() {
        return new PublicationType(
                MediaFormat.BOOK,
                TypeOfBinding.HARDBACK,
                "Standard publication");}

    private US004ToRegisterAppraisalEntityController createController() {
        return new US004ToRegisterAppraisalEntityController(
                createGenreRepository(),
                createPublicationTypeRepository(),
                new AppraisalEntityRepository(),
                new GenreDetailsDTO_US004("temp"),
                new PublicationTypeDTO_US004(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "temp")
        );
    }

    /**
     * Verifies that all genres are correctly retrieved as DTOs.
     */
    @Test
    void shouldReturnAllGenresAsDTO() {

        // Arrange
        var controller = createController();

        // Act
        List<GenreDetailsDTO_US004> result = controller.getAllGenres();

        // Assert
        assertEquals(2, result.size());
    }

    /**
     * Verifies that all publication types are retrieved as DTOs.
     */
    @Test
    void shouldReturnAllPublicationTypesAsDTO() {

        // Arrange
        var controller = createController();

        // Act
        List<PublicationTypeDTO_US004> result = controller.getAllPublicationTypes();

        // Assert
        assertEquals(2, result.size());
    }

    /**
     * Verifies successful registration of AppraisalEntity.
     */
    @Test
    void shouldRegisterAppraisalEntitySuccessfully() {

        // Arrange
        var controller = createController();

        var genreDTO = new GenreDetailsDTO_US004("Fantasy");
        var pubDTO = new PublicationTypeDTO_US004(MediaFormat.BOOK, TypeOfBinding.HARDBACK, "Standard");

        // Act
        AppraisalEntity entity = controller.registerAppraisalEntity(
                "Entity1",
                createContact(),
                List.of(genreDTO),
                List.of(pubDTO)
        );

        // Assert
        assertEquals("Entity1", entity.getName());
        assertEquals(1, entity.getGenres().size());
        assertEquals(1, entity.getPublicationTypes().size());
    }

    /**
     * Verifies exception when no genres are selected.
     */
    @Test
    void shouldThrowExceptionWhenGenresAreEmpty() {

        // Arrange
        var controller = createController();

        // Act + Assert
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
     */
    @Test
    void shouldThrowExceptionWhenPublicationTypesAreEmpty() {

        // Arrange
        var controller = createController();

        // Act + Assert
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
}

