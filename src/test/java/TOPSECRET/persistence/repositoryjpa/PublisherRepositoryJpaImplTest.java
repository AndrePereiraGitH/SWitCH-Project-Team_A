package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.ValueObjects.PublisherID;
import TOPSECRET.persistence.repositoryjpa.assembler.PublisherAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.PublisherDM;
import TOPSECRET.persistence.repositoryjpa.jpa.IPublisherRepositoryJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PublisherRepositoryJpaImpl}.
 *
 * <p>Tests are organised into the following sections:</p>
 * <ul>
 *   <li><b>save</b> — persisting a new publisher, duplicate detection, and null guards.</li>
 *   <li><b>existsById</b> — presence checks and null guards.</li>
 *   <li><b>findAll</b> — retrieval of zero or many publishers.</li>
 *   <li><b>findByVatNumber</b> — retrieval by VAT number, empty result, and null guards.</li>
 * </ul>
 */
class PublisherRepositoryJpaImplTest {

    @Mock
    private PublisherAssembler assembler;

    @Mock
    private IPublisherRepositoryJpa jpa;

    @InjectMocks
    private PublisherRepositoryJpaImpl repo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ========== Tests for save =================================

    @Test
    void save_shouldReturnPersistedPublisher_whenPublisherDoesNotExist() {
        // Arrange
        PublisherID id = mock(PublisherID.class);
        Publisher publisher = mock(Publisher.class);
        PublisherDM dm = mock(PublisherDM.class);
        PublisherDM savedDM = mock(PublisherDM.class);
        Publisher savedPublisher = mock(Publisher.class);

        when(publisher.id()).thenReturn(id);
        when(jpa.existsById(id)).thenReturn(false);
        when(assembler.toDM(publisher)).thenReturn(dm);
        when(jpa.save(dm)).thenReturn(savedDM);
        when(assembler.toDomain(savedDM)).thenReturn(savedPublisher);

        // Act
        Publisher result = repo.save(publisher);

        // Assert
        assertSame(savedPublisher, result);
    }

    @Test
    void save_shouldThrowException_whenPublisherIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class, () -> repo.save(null));
    }

    @Test
    void save_shouldThrowException_whenPublisherAlreadyExists() {
        // Arrange
        PublisherID id = mock(PublisherID.class);
        Publisher publisher = mock(Publisher.class);

        when(publisher.id()).thenReturn(id);
        when(jpa.existsById(id)).thenReturn(true);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> repo.save(publisher));
    }

    // ========== Tests for existsById ===========================

    @Test
    void existsById_shouldReturnTrue_whenPublisherExists() {
        // Arrange
        PublisherID id = mock(PublisherID.class);
        when(jpa.existsById(id)).thenReturn(true);

        // Act
        boolean result = repo.existsById(id);

        // Assert
        assertTrue(result);
    }

    @Test
    void existsById_shouldReturnFalse_whenPublisherDoesNotExist() {
        // Arrange
        PublisherID id = mock(PublisherID.class);
        when(jpa.existsById(id)).thenReturn(false);

        // Act
        boolean result = repo.existsById(id);

        // Assert
        assertFalse(result);
    }

    @Test
    void existsById_shouldThrowException_whenIdIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class, () -> repo.existsById(null));
    }

    // ========== Tests for findAll ==============================

    @Test
    void findAll_shouldReturnEmptyList_whenRepositoryIsEmpty() {
        // Arrange
        when(jpa.findAll()).thenReturn(List.of());

        // Act
        List<Publisher> result = repo.findAll();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_shouldReturnAllPublishers_whenMultiplePublishersExist() {
        // Arrange
        PublisherDM dm1 = mock(PublisherDM.class);
        PublisherDM dm2 = mock(PublisherDM.class);
        Publisher p1 = mock(Publisher.class);
        Publisher p2 = mock(Publisher.class);

        when(jpa.findAll()).thenReturn(List.of(dm1, dm2));
        when(assembler.toDomain(dm1)).thenReturn(p1);
        when(assembler.toDomain(dm2)).thenReturn(p2);

        // Act
        List<Publisher> result = repo.findAll();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(p1));
        assertTrue(result.contains(p2));
    }

    // ========== Tests for findByVatNumber ======================

    @Test
    void findByVatNumber_shouldReturnPublisher_whenPublisherExists() {
        // Arrange
        PublisherID id = mock(PublisherID.class);
        PublisherDM dm = mock(PublisherDM.class);
        Publisher publisher = mock(Publisher.class);

        when(jpa.findById(id)).thenReturn(Optional.of(dm));
        when(assembler.toDomain(dm)).thenReturn(publisher);

        // Act
        Optional<Publisher> result = repo.findByVatNumber(id);

        // Assert
        assertTrue(result.isPresent());
        assertSame(publisher, result.get());
    }

    @Test
    void findByVatNumber_shouldReturnEmpty_whenPublisherDoesNotExist() {
        // Arrange
        PublisherID id = mock(PublisherID.class);
        when(jpa.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Publisher> result = repo.findByVatNumber(id);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findByVatNumber_shouldThrowException_whenIdIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class, () -> repo.findByVatNumber(null));
    }
}