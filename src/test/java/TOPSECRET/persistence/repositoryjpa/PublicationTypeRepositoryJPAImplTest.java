package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.PublicationType.PublicationTypeFactory;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.persistence.repositoryjpa.assembler.PublicationTypeAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.PublicationTypeDM;
import TOPSECRET.persistence.repositoryjpa.jpa.IPublicationTypeRepositoryJPA;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PublicationTypeRepositoryJPAImplTest {

    // HELPER
    private final IPublicationTypeRepositoryJPA jpaRepository = mock(IPublicationTypeRepositoryJPA.class);
    private final PublicationTypeAssembler assembler = mock(PublicationTypeAssembler.class);

    private final PublicationTypeRepositoryJPAImpl repository =
            new PublicationTypeRepositoryJPAImpl(jpaRepository, assembler);

    // SAVE
    @Test
    void shouldSavePublicationType() {

        // ARRANGE
        PublicationType domain = mock(PublicationType.class);
        PublicationTypeDM dm = mock(PublicationTypeDM.class);

        when(assembler.toDataModel(domain)).thenReturn(dm);
        when(jpaRepository.save(dm)).thenReturn(dm);
        when(assembler.toDomain(dm)).thenReturn(domain);

        // ACT
        PublicationType result = repository.save(domain);

        // ASSERT
        assertEquals(domain, result);
        verify(jpaRepository).save(dm);
    }

    @Test
    void shouldThrowWhenSavingNull() {

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> repository.save(null));
    }

    // FIND ALL
    @Test
    void shouldReturnAllPublicationTypes() {

        // ARRANGE
        PublicationTypeDM dm = mock(PublicationTypeDM.class);
        PublicationType domain = mock(PublicationType.class);

        when(jpaRepository.findAll()).thenReturn(List.of(dm));
        when(assembler.toDomain(dm)).thenReturn(domain);

        // ACT
        List<PublicationType> result = repository.findAll();

        // ASSERT
        assertEquals(1, result.size());
        assertEquals(domain, result.get(0));
    }

    // FIND BY ID
    @Test
    void shouldFindByIdSuccessfully() {

        // ARRANGE
        PublicationTypeId id = new PublicationTypeId("1");
        PublicationTypeDM dm = mock(PublicationTypeDM.class);
        PublicationType domain = mock(PublicationType.class);

        when(jpaRepository.findById(id)).thenReturn(Optional.of(dm));
        when(assembler.toDomain(dm)).thenReturn(domain);

        // ACT
        Optional<PublicationType> result = repository.findById(id);

        // ASSERT
        assertTrue(result.isPresent());
        assertEquals(domain, result.get());
    }

    @Test
    void shouldReturnEmptyWhenIdNotFound() {

        // ARRANGE
        PublicationTypeId id = new PublicationTypeId("1");

        when(jpaRepository.findById(id)).thenReturn(Optional.empty());

        // ACT
        Optional<PublicationType> result = repository.findById(id);

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowWhenIdIsNull() {

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> repository.findById(null));
    }

    // FIND BY MEDIA FORMAT
    @Test
    void shouldFindByMediaFormat() {

        // ARRANGE
        PublicationTypeDM dm = mock(PublicationTypeDM.class);
        PublicationType domain = mock(PublicationType.class);

        when(jpaRepository.findAll()).thenReturn(List.of(dm));
        when(assembler.toDomain(dm)).thenReturn(domain);
        when(domain.getMediaFormat()).thenReturn("Book");

        // ACT
        Optional<PublicationType> result = repository.findByMediaFormat("Book");

        // ASSERT
        assertTrue(result.isPresent());
        assertEquals(domain, result.get());
    }

    @Test
    void shouldReturnEmptyWhenMediaFormatNotFound() {

        // ARRANGE
        PublicationTypeDM dm = mock(PublicationTypeDM.class);
        PublicationType domain = mock(PublicationType.class);

        when(jpaRepository.findAll()).thenReturn(List.of(dm));
        when(assembler.toDomain(dm)).thenReturn(domain);
        when(domain.getMediaFormat()).thenReturn("Magazine");

        // ACT
        Optional<PublicationType> result = repository.findByMediaFormat("Book");

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowWhenMediaFormatIsInvalid() {

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> repository.findByMediaFormat(" "));
    }

    @Test
    void shouldThrowWhenJpaRepositoryIsNull() {

        // ARRANGE
        PublicationTypeAssembler assembler =
                new PublicationTypeAssembler(new PublicationTypeFactory());

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> new PublicationTypeRepositoryJPAImpl(null, assembler));
    }

    @Test
    void shouldThrowWhenAssemblerIsNull() {

        // ARRANGE
        IPublicationTypeRepositoryJPA jpaRepository = mock(IPublicationTypeRepositoryJPA.class);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> new PublicationTypeRepositoryJPAImpl(jpaRepository, null));
    }
}