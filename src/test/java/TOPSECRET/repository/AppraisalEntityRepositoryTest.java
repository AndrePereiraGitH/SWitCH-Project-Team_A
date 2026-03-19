package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.MediaFormat;
import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.dto.GenreDetailsDTO_US004;
import TOPSECRET.dto.PublicationTypeDTO_US004;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AppraisalEntityRepository}.
 *
 * SUT: AppraisalEntityRepository
 * Dependencies are mocked to isolate repository behavior.
 */
class AppraisalEntityRepositoryTest {

    /**
     * Ensures a new AppraisalEntity is created and stored successfully
     * when valid input is provided.
     */
    @Test
    void shouldCreateAndAddAppraisalEntitySuccessfully() {

        // ARRANGE
        GenreRepository genreRepository = mock(GenreRepository.class);
        PublicationTypeRepository publicationTypeRepository = mock(PublicationTypeRepository.class);
        AppraisalEntityFactory appraisalEntityFactory = mock(AppraisalEntityFactory.class);

        AppraisalEntityRepository sut =
                new AppraisalEntityRepositoryMem(genreRepository, publicationTypeRepository, appraisalEntityFactory);

        Contact contact = mock(Contact.class);

        // DTO mocks are created separately to allow proper configuration
        GenreDetailsDTO_US004 genreDTO = mock(GenreDetailsDTO_US004.class);
        when(genreDTO.getTitle()).thenReturn("Fantasy");

        PublicationTypeDTO_US004 publicationDTO = mock(PublicationTypeDTO_US004.class);

        MediaFormat mediaFormat = MediaFormat.BOOK;
        TypeOfBinding binding = TypeOfBinding.HARDBACK;

        when(publicationDTO.getMediaFormat()).thenReturn(mediaFormat);
        when(publicationDTO.getTypeOfBinding()).thenReturn(binding);
        when(publicationDTO.getDetails()).thenReturn("Special Edition");

        List<GenreDetailsDTO_US004> genreDTOs = List.of(genreDTO);
        List<PublicationTypeDTO_US004> publicationDTOs = List.of(publicationDTO);

        Genre genre = mock(Genre.class);
        PublicationType publicationType = mock(PublicationType.class);

        when(genreRepository.findByTitle("Fantasy")).thenReturn(genre);
        when(publicationTypeRepository.findByAttributes(
                mediaFormat, binding, "Special Edition"))
                .thenReturn(publicationType);

        AppraisalEntity createdEntity = mock(AppraisalEntity.class);
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
                    sut.createAndAddAppraisalEntity(
                            "Test",
                            contact,
                            List.of(genreDTO),
                            List.of(publicationDTO)
                    );

            // ASSERT
            assertEquals(createdEntity, result);
        }
    }

    /**
     * Ensures an exception is thrown when an AppraisalEntity
     * with the same name and contact already exists.
     */
    @Test
    void shouldThrowExceptionWhenEntityAlreadyExists() {

        // Arrange
        GenreRepository genreRepository = mock(GenreRepository.class);
        PublicationTypeRepository publicationTypeRepository = mock(PublicationTypeRepository.class);
        AppraisalEntityFactory appraisalEntityFactory = mock(AppraisalEntityFactory.class);

        AppraisalEntityRepository sut =
                new AppraisalEntityRepositoryMem(genreRepository, publicationTypeRepository, appraisalEntityFactory);

        Contact contact = mock(Contact.class);

        GenreDetailsDTO_US004 genreDTO = mock(GenreDetailsDTO_US004.class);
        when(genreDTO.getTitle()).thenReturn("Fantasy");

        PublicationTypeDTO_US004 publicationDTO = mock(PublicationTypeDTO_US004.class);

        MediaFormat mediaFormat = MediaFormat.BOOK;
        TypeOfBinding binding = TypeOfBinding.HARDBACK;

        when(publicationDTO.getMediaFormat()).thenReturn(mediaFormat);
        when(publicationDTO.getTypeOfBinding()).thenReturn(binding);
        when(publicationDTO.getDetails()).thenReturn("Special Edition");

        List<GenreDetailsDTO_US004> genreDTOs = List.of(genreDTO);
        List<PublicationTypeDTO_US004> publicationDTOs = List.of(publicationDTO);

        Genre genre = mock(Genre.class);
        PublicationType publicationType = mock(PublicationType.class);

        when(genreRepository.findByTitle("Fantasy")).thenReturn(genre);
        when(publicationTypeRepository.findByAttributes(
                mediaFormat, binding, "Special Edition"))
                .thenReturn(publicationType);

        AppraisalEntity createdEntity = mock(AppraisalEntity.class);
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
            sut.createAndAddAppraisalEntity(
                    "Test",
                    contact,
                    genreDTOs,
                    publicationDTOs
            );

            // ACT + ASSERT
            assertThrows(IllegalArgumentException.class, () ->
                    sut.createAndAddAppraisalEntity(
                            "Test",
                            contact,
                            genreDTOs,
                            publicationDTOs
                    ));
        }
    }

    /**
     * Ensures exception is thrown when Genre DTO list is null.
     */
    @Test
    void shouldThrowExceptionWhenGenresAreNull() {

        // ARRANGE
        GenreRepository genreRepository = mock(GenreRepository.class);
        PublicationTypeRepository publicationTypeRepository = mock(PublicationTypeRepository.class);
        AppraisalEntityFactory appraisalEntityFactory = mock(AppraisalEntityFactory.class);

        AppraisalEntityRepository sut =
                new AppraisalEntityRepositoryMem(genreRepository, publicationTypeRepository, appraisalEntityFactory);

        Contact contact = mock(Contact.class);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                sut.createAndAddAppraisalEntity(
                        "Test",
                        contact,
                        null,
                        List.of(mock(PublicationTypeDTO_US004.class))
                ));
    }

    /**
     * Ensures an exception is thrown when the publication type DTO list is empty.
     */
    @Test
    void shouldThrowExceptionWhenPublicationTypesAreEmpty() {

        // ARRANGE
        GenreRepository genreRepository = mock(GenreRepository.class);
        PublicationTypeRepository publicationTypeRepository = mock(PublicationTypeRepository.class);
        AppraisalEntityFactory appraisalEntityFactory = mock(AppraisalEntityFactory.class);

        AppraisalEntityRepository sut =
                new AppraisalEntityRepositoryMem(genreRepository, publicationTypeRepository, appraisalEntityFactory);

        Contact contact = mock(Contact.class);

        GenreDetailsDTO_US004 genreDTO = mock(GenreDetailsDTO_US004.class);
        when(genreDTO.getTitle()).thenReturn("Fantasy");

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                sut.createAndAddAppraisalEntity(
                        "Test",
                        contact,
                        List.of(genreDTO),
                        List.of()
                ));
    }

}