package TOPSECRET.repository;

import TOPSECRET.domain.AppraisalEntity.AppraisalEntity;
import TOPSECRET.domain.AppraisalEntity.AppraisalEntityFactory;
import TOPSECRET.domain.AppraisalEntity.IAppraisalEntityRepository;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.dto.US004GenreDTO;
import TOPSECRET.dto.US004PublicationTypeDTO;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

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

        // DTO doubles are created separately to allow proper configuration
        US004GenreDTO genreDTO = mock(US004GenreDTO.class);
        GenreID genreId = mock(GenreID.class);
        when(genreDTO.getId()).thenReturn(genreId);

        Genre genre = mock(Genre.class);
        when(genre.id()).thenReturn(genreId);
        when(genreRepository.findById(genreId)).thenReturn(genre);

        US004PublicationTypeDTO publicationDTO = mock(US004PublicationTypeDTO.class);
        PublicationTypeId pubId = mock(PublicationTypeId.class);
        when(publicationDTO.getId()).thenReturn(pubId);

        PublicationType publicationType = mock(PublicationType.class);
        when(publicationType.id()).thenReturn(pubId);
        when(publicationTypeRepository.findPublicationTypeById(pubId)).thenReturn(Optional.of(publicationType));

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
    }


    @Test
    void shouldThrowExceptionWhenEntityAlreadyExists() {

        // ARRANGE
        IGenreRepository genreRepository = mock(IGenreRepository.class);
        IPublicationTypeRepository publicationTypeRepository = mock(IPublicationTypeRepository.class);
        AppraisalEntityFactory appraisalEntityFactory = mock(AppraisalEntityFactory.class);

        IAppraisalEntityRepository sut =
                new AppraisalEntityRepositoryMem(genreRepository, publicationTypeRepository, appraisalEntityFactory);

        Contact contact = mock(Contact.class);

        US004GenreDTO genreDTO = mock(US004GenreDTO.class);
        GenreID genreId = mock(GenreID.class);
        when(genreDTO.getId()).thenReturn(genreId);

        Genre genre = mock(Genre.class);
        when(genre.id()).thenReturn(genreId);
        when(genreRepository.findById(genreId)).thenReturn(genre);

        US004PublicationTypeDTO publicationDTO = mock(US004PublicationTypeDTO.class);
        PublicationTypeId pubId = mock(PublicationTypeId.class);
        when(publicationDTO.getId()).thenReturn(pubId);

        PublicationType publicationType = mock(PublicationType.class);
        when(publicationType.id()).thenReturn(pubId);
        when(publicationTypeRepository.findPublicationTypeById(pubId)).thenReturn(Optional.of(publicationType));

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
                        List.of(mock(US004PublicationTypeDTO.class))
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenPublicationTypeNotFound() {

        // ARRANGE
        IGenreRepository genreRepository = mock(IGenreRepository.class);
        IPublicationTypeRepository publicationTypeRepository = mock(IPublicationTypeRepository.class);
        AppraisalEntityFactory factory = mock(AppraisalEntityFactory.class);

        IAppraisalEntityRepository sut =
                new AppraisalEntityRepositoryMem(genreRepository, publicationTypeRepository, factory);

        Contact contact = mock(Contact.class);

        US004GenreDTO genreDTO = mock(US004GenreDTO.class);
        when(genreDTO.getTitle()).thenReturn("Fantasy");

        US004PublicationTypeDTO publicationDTO = mock(US004PublicationTypeDTO.class);
        when(publicationDTO.getMediaFormat()).thenReturn("Book");

        Genre genre = mock(Genre.class);
        when(genreRepository.findByTitle("Fantasy")).thenReturn(genre);

        when(publicationTypeRepository.findPublicationTypeByAttributes("Book")).thenReturn(null);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                sut.createAndAdd(
                        "Test",
                        contact,
                        List.of(genreDTO),
                        List.of(publicationDTO)
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

        US004GenreDTO genreDTO = mock(US004GenreDTO.class);
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


