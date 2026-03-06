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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
        GenreRepository genreRepository = mock(GenreRepository.class);
        PublicationTypeRepository publicationTypeRepository = mock(PublicationTypeRepository.class);
        AppraisalEntityRepository appraisalRepo = mock(AppraisalEntityRepository.class);

        US004ToRegisterAppraisalEntityController sut =
                new US004ToRegisterAppraisalEntityController(
                        genreRepository,
                        publicationTypeRepository,
                        appraisalRepo
                );

        Genre genre = mock(Genre.class);
        when(genre.getTitle()).thenReturn("Fantasy");
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
        GenreRepository genreRepository = mock(GenreRepository.class);
        PublicationTypeRepository publicationTypeRepository = mock(PublicationTypeRepository.class);
        AppraisalEntityRepository appraisalRepo = mock(AppraisalEntityRepository.class);

        US004ToRegisterAppraisalEntityController sut =
                new US004ToRegisterAppraisalEntityController(
                        genreRepository,
                        publicationTypeRepository,
                        appraisalRepo
                );

        PublicationType type = mock(PublicationType.class);

        MediaFormat mediaFormat = MediaFormat.BOOK;
        TypeOfBinding binding = TypeOfBinding.HARDBACK;

        when(type.getMediaFormat()).thenReturn(mediaFormat);
        when(type.getTypeOfBinding()).thenReturn(binding);
        when(type.getDetails()).thenReturn("Special");

        when(publicationTypeRepository.getAllPublicationTypes())
                .thenReturn(List.of(type));

        // ACT
        List<PublicationTypeDTO_US004> result =
                sut.getAllPublicationTypes();

        // ASSERT
        assertEquals(mediaFormat, result.get(0).getMediaFormat());
        assertEquals(binding, result.get(0).getTypeOfBinding());
        assertEquals("Special", result.get(0).getDetails());
    }

    /**
     * Ensures exception is thrown when selectedGenresDTO is null.
     */
    @Test
    void shouldThrowExceptionWhenGenresAreNull() {

        // ARRANGE
        US004ToRegisterAppraisalEntityController sut =
                new US004ToRegisterAppraisalEntityController(
                        mock(GenreRepository.class),
                        mock(PublicationTypeRepository.class),
                        mock(AppraisalEntityRepository.class)
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
                        mock(GenreRepository.class),
                        mock(PublicationTypeRepository.class),
                        mock(AppraisalEntityRepository.class)
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
        GenreRepository genreRepository = mock(GenreRepository.class);
        PublicationTypeRepository publicationTypeRepository = mock(PublicationTypeRepository.class);
        AppraisalEntityRepository appraisalRepo = mock(AppraisalEntityRepository.class);

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

        when(appraisalRepo.createAndAddAppraisalEntity(
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