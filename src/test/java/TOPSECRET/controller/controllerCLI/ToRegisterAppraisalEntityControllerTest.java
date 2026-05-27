package TOPSECRET.controller.controllerCLI;


import TOPSECRET.domain.AppraisalEntity.AppraisalEntity;
import TOPSECRET.domain.AppraisalEntity.IAppraisalEntityRepository;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.US004GenreDTO;
import TOPSECRET.dto.US004PublicationTypeDTO;
import TOPSECRET.services.AppraisalEntityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.InstanceAlreadyExistsException;
import java.nio.file.AccessDeniedException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the ToRegisterAppraisalEntityController.
 *
 * <p>
 * These tests validate:
 * <ul>
 *     <li>DTO mapping from repositories</li>
 *     <li>Input validation</li>
 *     <li>Delegation to repository</li>
 * </ul>
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class ToRegisterAppraisalEntityControllerTest {

    @Mock
    private IGenreRepository genreRepository;

    @Mock
    private IPublicationTypeRepository publicationTypeRepository;

    @Mock
    private IAppraisalEntityRepository appraisalEntityRepository;

    @Mock
    private AppraisalEntityService appraisalService;

    @InjectMocks
    private ToRegisterAppraisalEntityController controller;

    private UserId userId;

    // CONSTRUCTOR tests
    @Test
    void shouldThrowWhenGenreRepositoryIsNull() {

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> new ToRegisterAppraisalEntityController(
                        null,
                        publicationTypeRepository,
                        appraisalService));
    }

    @Test
    void shouldThrowWhenPublicationTypeRepositoryIsNull() {

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> new ToRegisterAppraisalEntityController(
                        genreRepository,
                        null,
                        appraisalService));
    }

    @Test
    void shouldThrowWhenAppraisalEntityServiceIsNull() {
        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> new ToRegisterAppraisalEntityController(
                        genreRepository,
                        publicationTypeRepository,
                        null));
    }

    // getAllGenres tests
    @Test
    void shouldReturnListOfGenreDTOs() {

        // ARRANGE
        Genre genre = mock(Genre.class);
        GenreId id = mock(GenreId.class);

        when(genre.id()).thenReturn(id);
        when(genre.getGenreType()).thenReturn("Drama");

        when(genreRepository.findAll()).thenReturn(List.of(genre));

        // ACT
        List<US004GenreDTO> result = controller.getAllGenres();

        // ASSERT
        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnEmptyGenreDTOListWhenNoGenresExist() {
        // ARRANGE
        when(genreRepository.findAll()).thenReturn(List.of());

        // ACT
        List<US004GenreDTO> result = controller.getAllGenres();

        // ASSERT
        assertTrue(result.isEmpty());
    }

    // getAllPublicationTypes tests
    @Test
    void shouldReturnListOfPublicationTypeDTOs() {

        // ARRANGE
        PublicationType pt = mock(PublicationType.class);
        PublicationTypeId id = mock(PublicationTypeId.class);

        when(pt.id()).thenReturn(id);
        when(pt.getMediaFormat()).thenReturn("Book");

        when(publicationTypeRepository.findAll()).thenReturn(List.of(pt));

        // ACT
        List<US004PublicationTypeDTO> result =
                controller.getAllPublicationTypes();

        // ASSERT
        assertEquals(1, result.size());
    }

    @Test
    void shouldReturnEmptyPublicationTypeDTOListWhenNoneExist() {

        // ARRANGE
        when(publicationTypeRepository.findAll()).thenReturn(List.of());

        // ACT
        List<US004PublicationTypeDTO> result = controller.getAllPublicationTypes();

        // ASSERT
        assertTrue(result.isEmpty());
    }

    // registerAppraisalEntity tests
    @Test
    void shouldThrowWhenNameIsNull() {

        // ARRANGE
        Contact contact = mock(Contact.class);

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> controller.registerAppraisalEntity(
                        null,
                        contact,
                        List.of(mock(GenreId.class)),
                        List.of(mock(PublicationTypeId.class)),
                        userId));
    }

    @Test
    void shouldThrowWhenNameIsEmpty() {

        // ARRANGE
        Contact contact = mock(Contact.class);

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> controller.registerAppraisalEntity(
                        "",
                        contact,
                        List.of(mock(GenreId.class)),
                        List.of(mock(PublicationTypeId.class)),
                        userId));
    }

    @Test
    void shouldThrowWhenContactIsNull() {

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> controller.registerAppraisalEntity(
                        "Name",
                        null,
                        List.of(mock(GenreId.class)),
                        List.of(mock(PublicationTypeId.class)),
                        userId));
    }

    @Test
    void shouldThrowWhenGenresListIsNull() {
        // ARRANGE
        Contact contact = mock(Contact.class);

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> controller.registerAppraisalEntity(
                        "Name",
                        contact,
                        null,
                        List.of(mock(PublicationTypeId.class)),
                        userId));
    }

    @Test
    void shouldThrowWhenGenresListIsEmpty() {

        // ARRANGE
        Contact contact = mock(Contact.class);

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> controller.registerAppraisalEntity(
                        "Name",
                        contact,
                        List.of(),
                        List.of(mock(PublicationTypeId.class)),
                        userId));
    }

    @Test
    void shouldThrowWhenPublicationTypesListIsNull() {
        // ARRANGE
        Contact contact = mock(Contact.class);

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> controller.registerAppraisalEntity(
                        "Name",
                        contact,
                        List.of(mock(GenreId.class)),
                        null,
                        userId));
    }

    @Test
    void shouldThrowWhenPublicationTypesListIsEmpty() {
        // ARRANGE
        Contact contact = mock(Contact.class);

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> controller.registerAppraisalEntity(
                        "Name",
                        contact,
                        List.of(mock(GenreId.class)),
                        List.of(),
                        userId));
    }

    @Test
    void shouldReturnCreatedAppraisalEntity() throws AccessDeniedException, InstanceAlreadyExistsException {
        // ARRANGE
        Contact contact = mock(Contact.class);
        GenreId genreId = mock(GenreId.class);
        PublicationTypeId ptId = mock(PublicationTypeId.class);
        AppraisalEntity entity = mock(AppraisalEntity.class);

        when(appraisalService.createAndSave(
                "Name",
                contact,
                List.of(ptId),
                List.of(genreId),
                userId
        )).thenReturn(entity);

        // ACT
        AppraisalEntity result = controller.registerAppraisalEntity(
                "Name",
                contact,
                List.of(genreId),
                List.of(ptId),
                userId
        );

        // ASSERT
        assertSame(entity, result);
    }
}
