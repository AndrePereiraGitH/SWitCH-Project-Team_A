package TOPSECRET.controller;

import TOPSECRET.domain.AppraisalEntity.AppraisalEntity;
import TOPSECRET.domain.AppraisalEntity.IAppraisalEntityRepository;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;
import TOPSECRET.dto.GenreDetailsDTO_US004;
import TOPSECRET.dto.PublicationTypeDTO_US004;
import TOPSECRET.repository.GenreRepositoryMem;
import TOPSECRET.domain.Genre.IGenreRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Unit tests for US004 - To register an AppraisalEntity to the system
 * SUT: US004ToRegisterAppraisalEntityController
 * Repository dependencies are mocked to isolate controller logic.
 */
class US004ToRegisterAppraisalEntityControllerTest {

    /**
     * Ensures all Genres are retrieved and converted to DTOs.
     */
    @Test
    void shouldReturnAllGenresAsDTOs() {

        // ARRANGE
        IGenreRepository genreRepository = mock(IGenreRepository.class);
        IPublicationTypeRepository publicationTypeRepository = mock(IPublicationTypeRepository.class);
        IAppraisalEntityRepository appraisalRepo = mock(IAppraisalEntityRepository.class);

        US004ToRegisterAppraisalEntityController sut =
                new US004ToRegisterAppraisalEntityController(
                        genreRepository,
                        publicationTypeRepository,
                        appraisalRepo
                );

        Genre genre = mock(Genre.class);
        when(genre.getGenreType()).thenReturn("Fantasy");
        when(genreRepository.getAllGenres()).thenReturn(List.of(genre));

        // ACT
        List<GenreDetailsDTO_US004> result = sut.getAllGenres();

        // ASSERT
        assertEquals("Fantasy", result.get(0).getTitle());
    }

    /**
     * Ensures all PublicationTypes are retrieved and converted to DTOs.
     */
    @Test
    void shouldReturnAllPublicationTypesAsDTOs() {

        // ARRANGE
        IGenreRepository genreRepository = mock(IGenreRepository.class);
        IPublicationTypeRepository publicationTypeRepository = mock(IPublicationTypeRepository.class);
        IAppraisalEntityRepository appraisalRepo = mock(IAppraisalEntityRepository.class);

        US004ToRegisterAppraisalEntityController sut =
                new US004ToRegisterAppraisalEntityController(
                        genreRepository,
                        publicationTypeRepository,
                        appraisalRepo
                );

        PublicationType type = mock(PublicationType.class);

        String mediaFormat = "Book";


        when(type.getMediaFormat()).thenReturn(mediaFormat);

        when(publicationTypeRepository.getAllPublicationTypes())
                .thenReturn(List.of(type));

        // ACT
        List<PublicationTypeDTO_US004> result =
                sut.getAllPublicationTypes();

        // ASSERT
        assertEquals(mediaFormat, result.get(0).getMediaFormat());
    }

    /**
     * Ensures exception is thrown when selectedGenresDTO is null.
     */
    @Test
    void shouldThrowExceptionWhenGenresAreNull() {

        // ARRANGE
        US004ToRegisterAppraisalEntityController sut =
                new US004ToRegisterAppraisalEntityController(
                        mock(GenreRepositoryMem.class),
                        mock(IPublicationTypeRepository.class),
                        mock(IAppraisalEntityRepository.class)
                );

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> sut.registerAppraisalEntity(
                        "Test",
                        mock(Contact.class),
                        null,
                        List.of(mock(PublicationTypeDTO_US004.class))
                ));
    }

    /**
     * Ensures exception is thrown when selectedPublicationTypesDTO is empty.
     */
    @Test
    void shouldThrowExceptionWhenPublicationTypesAreEmpty() {

        // ARRANGE
        US004ToRegisterAppraisalEntityController sut =
                new US004ToRegisterAppraisalEntityController(
                        mock(GenreRepositoryMem.class),
                        mock(IPublicationTypeRepository.class),
                        mock(IAppraisalEntityRepository.class)
                );

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> sut.registerAppraisalEntity(
                        "Test",
                        mock(Contact.class),
                        List.of(mock(GenreDetailsDTO_US004.class)),
                        List.of()
                ));
    }

    /**
     * Ensures successful registration delegates to repository.
     */
    @Test
    void shouldDelegateRegistrationToRepository() {

        // ARRANGE
        IGenreRepository genreRepository = mock(IGenreRepository.class);
        IPublicationTypeRepository publicationTypeRepository = mock(IPublicationTypeRepository.class);
        IAppraisalEntityRepository appraisalRepo = mock(IAppraisalEntityRepository.class);

        US004ToRegisterAppraisalEntityController sut =
                new US004ToRegisterAppraisalEntityController(
                        genreRepository,
                        publicationTypeRepository,
                        appraisalRepo
                );

        Contact contact = mock(Contact.class);

        GenreDetailsDTO_US004 genreDTO = mock(GenreDetailsDTO_US004.class);
        PublicationTypeDTO_US004 pubDTO = mock(PublicationTypeDTO_US004.class);

        AppraisalEntity expected = mock(AppraisalEntity.class);

        when(appraisalRepo.createAndAdd(
                eq("Test"),
                eq(contact),
                anyList(),
                anyList()
        )).thenReturn(expected);

        // ACT
        AppraisalEntity result =
                sut.registerAppraisalEntity(
                        "Test",
                        contact,
                        List.of(genreDTO),
                        List.of(pubDTO)
                );

        // ASSERT
        assertEquals(expected, result);
    }
}
