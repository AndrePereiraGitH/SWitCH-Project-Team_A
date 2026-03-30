package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.AppraisalEntity.AppraisalEntity;
import TOPSECRET.domain.AppraisalEntity.AppraisalEntityFactory;
import TOPSECRET.domain.AppraisalEntity.IAppraisalEntityRepository;
import TOPSECRET.domain.Contact;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.dto.GenreDetailsDTO_US004;
import TOPSECRET.dto.PublicationTypeDTO_US004;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AppraisalEntityRepositoryMem}.
 *
 * <p>
 * Validates creation, validation, and duplication rules
 * for AppraisalEntity repository.
 * </p>
 *
 * <p>
 * Mocks are used to isolate dependencies such as repositories and factory.
 * </p>
 */
class AppraisalEntityRepositoryMemTest {

    @Test
    void shouldCreateAndAddAppraisalEntitySuccessfully() {

        // ARRANGE
        IGenreRepository genreRepository = mock(IGenreRepository.class);
        IPublicationTypeRepository publicationTypeRepository = mock(IPublicationTypeRepository.class);
        AppraisalEntityFactory appraisalEntityFactory = mock(AppraisalEntityFactory.class);

        IAppraisalEntityRepository sut =
                new AppraisalEntityRepositoryMem(genreRepository, publicationTypeRepository, appraisalEntityFactory);

        Contact contact = mock(Contact.class);

        // DTO mocks are created separately to allow proper configuration
        GenreDetailsDTO_US004 genreDTO = mock(GenreDetailsDTO_US004.class);
        when(genreDTO.getTitle()).thenReturn("Fantasy");

        PublicationTypeDTO_US004 publicationDTO = mock(PublicationTypeDTO_US004.class);

        String mediaFormat = "Book";
        when(publicationDTO.getMediaFormat()).thenReturn(mediaFormat);

        Genre genre = mock(Genre.class);
        PublicationType publicationType = mock(PublicationType.class);

        when(genreRepository.findByTitle("Fantasy")).thenReturn(genre);
        when(publicationTypeRepository.findByAttributes(mediaFormat)).thenReturn(publicationType);

        AppraisalEntity createdEntity = mock(AppraisalEntity.class);
        when(appraisalEntityFactory.create(eq("Test"), eq(contact), anyList(), anyList()))
                .thenReturn(createdEntity);

        // ACT
        AppraisalEntity result = sut.createAndAdd(
                "Test",
                contact,
                List.of(genreDTO),
                List.of(publicationDTO)
        );

        //ASSERT
        assertEquals(createdEntity, result);
        verify(appraisalEntityFactory).create(eq("Test"), eq(contact), anyList(), anyList());
    }


    @Test
    void shouldThrowExceptionWhenEntityAlreadyExists() {

        // ARRANGE
        IGenreRepository genreRepository = mock(IGenreRepository.class);
        IPublicationTypeRepository publicationTypeRepository = mock(PublicationTypeRepositoryMem.class);
        AppraisalEntityFactory appraisalEntityFactory = mock(AppraisalEntityFactory.class);

        IAppraisalEntityRepository sut =
                new AppraisalEntityRepositoryMem(genreRepository, publicationTypeRepository, appraisalEntityFactory);

        Contact contact = mock(Contact.class);

        GenreDetailsDTO_US004 genreDTO = mock(GenreDetailsDTO_US004.class);
        when(genreDTO.getTitle()).thenReturn("Fantasy");

        PublicationTypeDTO_US004 publicationDTO = mock(PublicationTypeDTO_US004.class);
        String mediaFormat = "Book";
        when(publicationDTO.getMediaFormat()).thenReturn(mediaFormat);

        Genre genre = mock(Genre.class);
        PublicationType publicationType = mock(PublicationType.class);
        when(genreRepository.findByTitle("Fantasy")).thenReturn(genre);
        when(publicationTypeRepository.findByAttributes(mediaFormat)).thenReturn(publicationType);

        AppraisalEntity createdEntity = mock(AppraisalEntity.class);
        when(createdEntity.getName()).thenReturn("Test");
        when(createdEntity.getContact()).thenReturn(contact);

        when(appraisalEntityFactory.create(anyString(), any(), anyList(), anyList()))
                .thenReturn(createdEntity);

        // ACT
        sut.createAndAdd(
                    "Test",
                    contact,
                    List.of(genreDTO),
                    List.of(publicationDTO)
            );

        // ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                sut.createAndAdd("Test", contact, List.of(genreDTO), List.of(publicationDTO))
        );
        }


    @Test
    void shouldThrowExceptionWhenGenresAreNull() {

        // ARRANGE
        IGenreRepository genreRepository = mock(IGenreRepository.class);
        IPublicationTypeRepository publicationTypeRepository = mock(IPublicationTypeRepository.class);
        AppraisalEntityFactory appraisalEntityFactory = mock(AppraisalEntityFactory.class);

        IAppraisalEntityRepository sut =
                new AppraisalEntityRepositoryMem(genreRepository, publicationTypeRepository, appraisalEntityFactory);

        Contact contact = mock(Contact.class);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                sut.createAndAdd(
                        "Test",
                        contact,
                        null,
                        List.of(mock(PublicationTypeDTO_US004.class))
                )
        );
    }


    @Test
    void shouldThrowExceptionWhenPublicationTypesAreEmpty() {

        // ARRANGE
        IGenreRepository genreRepository = mock(IGenreRepository.class);
        IPublicationTypeRepository publicationTypeRepository = mock(IPublicationTypeRepository.class);
        AppraisalEntityFactory appraisalEntityFactory = mock(AppraisalEntityFactory.class);

        IAppraisalEntityRepository sut =
                new AppraisalEntityRepositoryMem(genreRepository, publicationTypeRepository, appraisalEntityFactory);

        Contact contact = mock(Contact.class);

        GenreDetailsDTO_US004 genreDTO = mock(GenreDetailsDTO_US004.class);
        when(genreDTO.getTitle()).thenReturn("Fantasy");

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                sut.createAndAdd(
                        "Test",
                        contact,
                        List.of(genreDTO),
                        List.of()
                ));
    }

}


