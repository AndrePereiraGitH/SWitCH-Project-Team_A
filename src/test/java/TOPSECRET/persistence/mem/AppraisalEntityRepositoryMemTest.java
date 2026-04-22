package TOPSECRET.persistence.mem;


import TOPSECRET.domain.AppraisalEntity.AppraisalEntity;
import TOPSECRET.domain.AppraisalEntity.AppraisalEntityFactory;
import TOPSECRET.domain.AppraisalEntity.IAppraisalEntityRepository;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.Genre.IGenreRepository;
import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppraisalEntityRepositoryMemTest {

    @Test
    void shouldCreateAndAddAppraisalEntitySuccessfully() {

        // ARRANGE
        AppraisalEntityFactory factory = mock(AppraisalEntityFactory.class);

        IAppraisalEntityRepository sut =
                new AppraisalEntityRepositoryMem(
                        mock(IGenreRepository.class),
                        mock(IPublicationTypeRepository.class),
                        factory
                );

        Contact contact = mock(Contact.class);
        GenreID genreId = mock(GenreID.class);
        PublicationTypeId pubId = mock(PublicationTypeId.class);

        AppraisalEntity createdEntity = mock(AppraisalEntity.class);

        when(factory.create(eq("Test"), eq(contact), anyList(), anyList()))
                .thenReturn(createdEntity);

        // ACT
        AppraisalEntity result =
                sut.createAndAdd(
                        "Test",
                        contact,
                        List.of(genreId),
                        List.of(pubId)
                );

        // ASSERT
        assertEquals(createdEntity, result);
    }

    @Test
    void shouldThrowExceptionWhenEntityAlreadyExists() {

        // ARRANGE
        AppraisalEntityFactory factory = mock(AppraisalEntityFactory.class);

        IAppraisalEntityRepository sut =
                new AppraisalEntityRepositoryMem(
                        mock(IGenreRepository.class),
                        mock(IPublicationTypeRepository.class),
                        factory
                );

        Contact contact = mock(Contact.class);
        GenreID genreId = mock(GenreID.class);
        PublicationTypeId pubId = mock(PublicationTypeId.class);

        AppraisalEntity entity = mock(AppraisalEntity.class);

        when(entity.getName()).thenReturn("Test");
        when(entity.getContact()).thenReturn(contact);

        when(factory.create(anyString(), any(), anyList(), anyList()))
                .thenReturn(entity);

        // ACT
        sut.createAndAdd("Test", contact, List.of(genreId), List.of(pubId));

        // ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                sut.createAndAdd("Test", contact, List.of(genreId), List.of(pubId))
        );
    }

    @Test
    void shouldThrowExceptionWhenGenresAreNull() {

        // ARRANGE
        IAppraisalEntityRepository sut =
                new AppraisalEntityRepositoryMem(
                        mock(IGenreRepository.class),
                        mock(IPublicationTypeRepository.class),
                        mock(AppraisalEntityFactory.class)
                );

        Contact contact = mock(Contact.class);
        PublicationTypeId pubId = mock(PublicationTypeId.class);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                sut.createAndAdd(
                        "Test",
                        contact,
                        null,
                        List.of(pubId)
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenPublicationTypesAreEmpty() {

        // ARRANGE
        IAppraisalEntityRepository sut =
                new AppraisalEntityRepositoryMem(
                        mock(IGenreRepository.class),
                        mock(IPublicationTypeRepository.class),
                        mock(AppraisalEntityFactory.class)
                );

        Contact contact = mock(Contact.class);
        GenreID genreId = mock(GenreID.class);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () ->
                sut.createAndAdd(
                        "Test",
                        contact,
                        List.of(genreId),
                        List.of()
                )
        );
    }
}


