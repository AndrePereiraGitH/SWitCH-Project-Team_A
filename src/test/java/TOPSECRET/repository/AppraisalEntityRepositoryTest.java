package TOPSECRET.repository;

import TOPSECRET.domain.AppraisalEntity.AppraisalEntity;
import TOPSECRET.domain.AppraisalEntity.AppraisalEntityFactory;
import TOPSECRET.domain.AppraisalEntity.IAppraisalEntityRepository;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.ValueObjects.AppraisalEntityId;
import TOPSECRET.dto.GenreDetailsDTO_US004;
import TOPSECRET.dto.PublicationTypeDTO_US004;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;

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

        // DTO mocks are created separately to allow proper configuration
        GenreDetailsDTO_US004 genreDTO = mock(GenreDetailsDTO_US004.class);
        when(genreDTO.getTitle()).thenReturn("Fantasy");

        PublicationTypeDTO_US004 publicationDTO = mock(PublicationTypeDTO_US004.class);

        String mediaFormat = "Book";
        when(publicationDTO.getMediaFormat()).thenReturn(mediaFormat);

        List<GenreDetailsDTO_US004> genreDTOs = List.of(genreDTO);
        List<PublicationTypeDTO_US004> publicationDTOs = List.of(publicationDTO);

        Genre genre = mock(Genre.class);
        PublicationType publicationType = mock(PublicationType.class);
        AppraisalEntityId id = mock(AppraisalEntityId.class);

        when(genreRepository.findByTitle("Fantasy")).thenReturn(genre);
        when(publicationTypeRepository.findByAttributes(
                mediaFormat))
                .thenReturn(publicationType);

        AppraisalEntity createdEntity = mock(AppraisalEntity.class);
        when(createdEntity.getId()).thenReturn(id);
        when(createdEntity.getName()).thenReturn("Test");
        when(createdEntity.getContact()).thenReturn(contact);

        try (MockedStatic<AppraisalEntityFactory> mockedFactory =
                     mockStatic(AppraisalEntityFactory.class)) {

            mockedFactory.when(() -> appraisalEntityFactory.create(
                            eq("Test"),
                            eq(contact),
                            anyList(),
                            anyList()
                    )
            ).thenReturn(createdEntity);

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

        GenreDetailsDTO_US004 genreDTO = mock(GenreDetailsDTO_US004.class);
        when(genreDTO.getTitle()).thenReturn("Fantasy");

        PublicationTypeDTO_US004 publicationDTO = mock(PublicationTypeDTO_US004.class);

        String mediaFormat = "Book";

        when(publicationDTO.getMediaFormat()).thenReturn(mediaFormat);

        List<GenreDetailsDTO_US004> genreDTOs = List.of(genreDTO);
        List<PublicationTypeDTO_US004> publicationDTOs = List.of(publicationDTO);

        Genre genre = mock(Genre.class);
        PublicationType publicationType = mock(PublicationType.class);
        AppraisalEntityId id = mock(AppraisalEntityId.class);

        when(genreRepository.findByTitle("Fantasy")).thenReturn(genre);
        when(publicationTypeRepository.findByAttributes(
                mediaFormat))
                .thenReturn(publicationType);

        AppraisalEntity createdEntity = mock(AppraisalEntity.class);
        when(createdEntity.getId()).thenReturn(id);
        when(createdEntity.getName()).thenReturn("Test");
        when(createdEntity.getContact()).thenReturn(contact);


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
                    genreDTOs,
                    publicationDTOs
            );

            // ACT + ASSERT
            assertThrows(IllegalArgumentException.class, () ->
                    sut.createAndAdd(
                            "Test",
                            contact,
                            genreDTOs,
                            publicationDTOs
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
                        List.of(mock(PublicationTypeDTO_US004.class))
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
