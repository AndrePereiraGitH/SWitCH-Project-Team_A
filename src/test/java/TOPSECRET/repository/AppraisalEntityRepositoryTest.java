package TOPSECRET.repository;

import TOPSECRET.domain.AppraisalEntity.AppraisalEntity;
import TOPSECRET.domain.AppraisalEntity.AppraisalEntityFactory;
import TOPSECRET.domain.AppraisalEntity.IAppraisalEntityRepository;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.ValueObjects.AppraisalEntityId;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.dto.US004GenreDTO;
import TOPSECRET.dto.US004PublicationTypeDTO;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class AppraisalEntityRepositoryTest {


    @Test
    void shouldCreateAndAddAppraisalEntitySuccessfully() {

        // ARRANGE
        GenreRepositoryMem genreRepository = mock(GenreRepositoryMem.class);
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
            AppraisalEntity result =
                    sut.createAndAdd(
                            "Test",
                            contact,
                            List.of(genreDTO),
                            List.of(publicationDTO)
                    );

            // ASSERT
            assertEquals(createdEntity, result);

    }

    @Test
    void shouldThrowExceptionWhenEntityAlreadyExists() {

        // Arrange
        GenreRepositoryMem genreRepository = mock(GenreRepositoryMem.class);
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


        try (MockedStatic<AppraisalEntityFactory> mockedFactory =
                     mockStatic(AppraisalEntityFactory.class)) {

            mockedFactory.when(() ->
                    appraisalEntityFactory.create(
                            anyString(),
                            any(),
                            anyList(),
                            anyList()
                    )
            ).thenReturn(createdEntity);

            // First creation (should succeed)
            sut.createAndAdd(
                    "Test",
                    contact,
                    List.of(genreDTO),
                    List.of(publicationDTO)
            );

            // ACT + ASSERT
            assertThrows(IllegalArgumentException.class, () ->
                    sut.createAndAdd(
                            "Test",
                            contact,
                            List.of(genreDTO),
                            List.of(publicationDTO)
                    ));
        }
    }

    @Test
    void shouldThrowExceptionWhenGenresAreNull() {

        // ARRANGE
        GenreRepositoryMem genreRepository = mock(GenreRepositoryMem.class);
        IPublicationTypeRepository publicationTypeRepository = mock(IPublicationTypeRepository.class);
        AppraisalEntityFactory appraisalEntityFactory = mock(AppraisalEntityFactory.class);

        IAppraisalEntityRepository sut =
                new AppraisalEntityRepositoryMem(genreRepository, publicationTypeRepository, appraisalEntityFactory);

        AppraisalEntityId id = mock(AppraisalEntityId.class);
        Contact contact = mock(Contact.class);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                sut.createAndAdd(
                        "Test",
                        contact,
                        null,
                        List.of(mock(US004PublicationTypeDTO.class))
                ));
    }


    @Test
    void shouldThrowExceptionWhenPublicationTypesAreEmpty() {

        // ARRANGE
        GenreRepositoryMem genreRepository = mock(GenreRepositoryMem.class);
        IPublicationTypeRepository publicationTypeRepository = mock(IPublicationTypeRepository.class);
        AppraisalEntityFactory appraisalEntityFactory = mock(AppraisalEntityFactory.class);

        IAppraisalEntityRepository sut =
                new AppraisalEntityRepositoryMem(genreRepository, publicationTypeRepository, appraisalEntityFactory);

        Contact contact = mock(Contact.class);
        AppraisalEntityId id = mock(AppraisalEntityId.class);

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
