package TOPSECRET.controller;

import TOPSECRET.domain.AppraisalEntity.AppraisalEntity;
import TOPSECRET.domain.AppraisalEntity.IAppraisalEntityRepository;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.dto.US004GenreDTO;
import TOPSECRET.dto.US004PublicationTypeDTO;
import TOPSECRET.dto.US004GenreMapper;
import TOPSECRET.dto.US004PublicationTypeMapper;
import TOPSECRET.domain.Genre.IGenreRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Unit tests for US004ToRegisterAppraisalEntityController.
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
class US004ToRegisterAppraisalEntityControllerTest {

    @Test
    void shouldRegisterAValidAppraisalEntityToRepository() {

        // ARRANGE
        IGenreRepository genreRepository = mock(IGenreRepository.class);
        IPublicationTypeRepository publicationTypeRepository = mock(IPublicationTypeRepository.class);
        IAppraisalEntityRepository appraisalRepository = mock(IAppraisalEntityRepository.class);

        US004ToRegisterAppraisalEntityController controller =
                new US004ToRegisterAppraisalEntityController(
                        genreRepository,
                        publicationTypeRepository,
                        appraisalRepository
                );

        Contact contact = mock(Contact.class);
        AppraisalEntity expected = mock(AppraisalEntity.class);

        when(appraisalRepository.createAndAdd(
                eq("Test"),
                eq(contact),
                anyList(),
                anyList()
        )).thenReturn(expected);

        // ACT
        AppraisalEntity result = controller.registerAppraisalEntity(
                "Test",
                contact,
                List.of(mock(US004GenreDTO.class)),
                List.of(mock(US004PublicationTypeDTO.class))
        );

        // ASSERT
        assertEquals(expected, result);
    }

    @Test
    void shouldReturnAllGenresAsDTOs() {

        // ARRANGE
        IGenreRepository genreRepository = mock(IGenreRepository.class);
        IPublicationTypeRepository publicationTypeRepository = mock(IPublicationTypeRepository.class);
        IAppraisalEntityRepository appraisalRepository = mock(IAppraisalEntityRepository.class);

        US004ToRegisterAppraisalEntityController controller =
                new US004ToRegisterAppraisalEntityController(
                        genreRepository,
                        publicationTypeRepository,
                        appraisalRepository
                );

        Genre genre = mock(Genre.class);
        GenreID id = mock(GenreID.class);

        when(genre.id()).thenReturn(id);
        when(genre.getGenreType()).thenReturn("Fantasy");
        when(genreRepository.getAllGenres()).thenReturn(List.of(genre));

        // ACT
        List<US004GenreDTO> result = controller.getAllGenres();

        // ASSERT
        assertEquals(id, result.get(0).id());
        assertEquals("Fantasy", result.get(0).title());
    }

    @Test
    void shouldReturnAllPublicationTypesAsDTOs() {

        // ARRANGE
        IGenreRepository genreRepository = mock(IGenreRepository.class);
        IPublicationTypeRepository publicationTypeRepository = mock(IPublicationTypeRepository.class);
        IAppraisalEntityRepository appraisalRepository = mock(IAppraisalEntityRepository.class);

        US004ToRegisterAppraisalEntityController controller =
                new US004ToRegisterAppraisalEntityController(
                        genreRepository,
                        publicationTypeRepository,
                        appraisalRepository
                );

        PublicationType type = mock(PublicationType.class);
        PublicationTypeId id = mock(PublicationTypeId.class);

        when(type.id()).thenReturn(id);
        when(type.getMediaFormat()).thenReturn("Book");

        when(publicationTypeRepository.getAllPublicationTypes())
                .thenReturn(List.of(type));

        // ACT
        List<US004PublicationTypeDTO> result = controller.getAllPublicationTypes();

        // ASSERT
        assertEquals(id, result.get(0).id());
        assertEquals("Book", result.get(0).mediaFormat());
    }

    @Test
    void shouldThrowExceptionWhenGenresAreNull() {

        // ARRANGE
        US004ToRegisterAppraisalEntityController controller =
                new US004ToRegisterAppraisalEntityController(
                        mock(IGenreRepository.class),
                        mock(IPublicationTypeRepository.class),
                        mock(IAppraisalEntityRepository.class)
                );

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> controller.registerAppraisalEntity(
                        "Test",
                        mock(Contact.class),
                        null,
                        List.of(mock(US004PublicationTypeDTO.class))
                ));
    }

    @Test
    void shouldThrowExceptionWhenPublicationTypesAreEmpty() {

        // ARRANGE
        US004ToRegisterAppraisalEntityController controller =
                new US004ToRegisterAppraisalEntityController(
                        mock(IGenreRepository.class),
                        mock(IPublicationTypeRepository.class),
                        mock(IAppraisalEntityRepository.class)
                );

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> controller.registerAppraisalEntity(
                        "Test",
                        mock(Contact.class),
                        List.of(mock(US004GenreDTO.class)),
                        List.of()
                ));
    }
}
