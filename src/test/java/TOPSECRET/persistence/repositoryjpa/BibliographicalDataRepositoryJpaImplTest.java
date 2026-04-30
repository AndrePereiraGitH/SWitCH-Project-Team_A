package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.GenreID;
import TOPSECRET.persistence.repositoryjpa.assembler.BibliographicalDataAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.BibliographicalDataDM;
import TOPSECRET.persistence.repositoryjpa.jpa.IBibliographicalDataRepositoryJpa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BibliographicalDataRepositoryJpaImplTest {

    private IBibliographicalDataRepositoryJpa jpaRepository;
    private BibliographicalDataAssembler assembler;
    private BibliographicalData bd;
    private BibliographicalData savedBd;
    private BibliographicalDataDM bdDM;
    private BibliographicalDataDM savedBdDM;
    private BibliographicalDataID bdId;
    private GenreID genreId;
    private AuthorId authorId;

    private BibliographicalDataRepositoryJpaImpl repository;

    @BeforeEach
    void setUp() {
        jpaRepository = mock(IBibliographicalDataRepositoryJpa.class);
        assembler = mock(BibliographicalDataAssembler.class);
        bd = mock(BibliographicalData.class);
        savedBd = mock(BibliographicalData.class);
        bdDM = mock(BibliographicalDataDM.class);
        savedBdDM = mock(BibliographicalDataDM.class);
        bdId = mock(BibliographicalDataID.class);
        genreId = mock(GenreID.class);
        authorId = mock(AuthorId.class);
    }

    @Test
    void save_shouldThrowWhenBibliographicalDataIsNull() {
        // Arrange
        repository = new BibliographicalDataRepositoryJpaImpl(jpaRepository, assembler);

        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                repository.save(null)
        );
    }

    @Test
    void save_shouldThrowWhenBibliographicalDataAlreadyExists() throws InstanceAlreadyExistsException {
        // Arrange
        repository = new BibliographicalDataRepositoryJpaImpl(jpaRepository, assembler);

        when(bd.id()).thenReturn(bdId);
        when(jpaRepository.existsById(bdId)).thenReturn(true);

        // Act + Assert
        assertThrows(InstanceAlreadyExistsException.class, () ->
                repository.save(bd)
        );
    }

    @Test
    void save_shouldConvertSaveAndReturnDomainObject() throws InstanceAlreadyExistsException {
        // Arrange
        repository = new BibliographicalDataRepositoryJpaImpl(jpaRepository, assembler);

        when(bd.id()).thenReturn(bdId);
        when(jpaRepository.existsById(bdId)).thenReturn(false);
        when(assembler.toDM(bd)).thenReturn(bdDM);
        when(jpaRepository.save(bdDM)).thenReturn(savedBdDM);
        when(assembler.toDomain(savedBdDM)).thenReturn(savedBd);

        // Act
        BibliographicalData result = repository.save(bd);

        // Assert
        assertSame(savedBd, result);
    }

    @Test
    void findBibliographicalDataById_shouldThrowWhenIdIsNull() {
        // Arrange
        repository = new BibliographicalDataRepositoryJpaImpl(jpaRepository, assembler);

        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                repository.findBibliographicalDataById(null)
        );
    }

    @Test
    void findBibliographicalDataById_shouldReturnDomainObjectWhenFound() {
        // Arrange
        repository = new BibliographicalDataRepositoryJpaImpl(jpaRepository, assembler);

        when(jpaRepository.findById(bdId)).thenReturn(Optional.of(bdDM));
        when(assembler.toDomain(bdDM)).thenReturn(bd);

        // Act
        Optional<BibliographicalData> result =
                repository.findBibliographicalDataById(bdId);

        // Arrange
        assertTrue(result.isPresent());
        assertSame(bd, result.get());
    }

    @Test
    void findBibliographicalDataById_shouldReturnEmptyWhenNotFound() {
        // Arrange
        repository = new BibliographicalDataRepositoryJpaImpl(jpaRepository, assembler);

        when(jpaRepository.findById(bdId)).thenReturn(Optional.empty());

        // Act
        Optional<BibliographicalData> result =
                repository.findBibliographicalDataById(bdId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findBibliographicalDataIdByTitle_shouldThrowWhenTitleIsNull() {
        // Arrange
        repository = new BibliographicalDataRepositoryJpaImpl(jpaRepository, assembler);

        // Act + Assert
        assertThrows(NullPointerException.class, () ->
                repository.findBibliographicalDataIdByTitle(null)
        );
    }

    @Test
    void findBibliographicalDataIdByTitle_shouldThrowWhenTitleIsBlank() {
        // Arrange
        repository = new BibliographicalDataRepositoryJpaImpl(jpaRepository, assembler);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                repository.findBibliographicalDataIdByTitle("   ")
        );
    }

    @Test
    void findBibliographicalDataIdByTitle_shouldReturnIdWhenTitleExists() {
        // Arrange
        repository = new BibliographicalDataRepositoryJpaImpl(jpaRepository, assembler);

        when(jpaRepository.findByTitleIgnoreCase("Clean Code")).thenReturn(Optional.of(bdDM));
        when(bdDM.getId()).thenReturn(bdId);

        // Act
        Optional<BibliographicalDataID> result =
                repository.findBibliographicalDataIdByTitle("  Clean Code  ");

        // Assert
        assertTrue(result.isPresent());
        assertSame(bdId, result.get());
    }

    @Test
    void findBibliographicalDataIdByTitle_shouldReturnEmptyWhenTitleDoesNotExist() {
        // Arrange
        repository = new BibliographicalDataRepositoryJpaImpl(jpaRepository, assembler);

        when(jpaRepository.findByTitleIgnoreCase("Unknown")).thenReturn(Optional.empty());

        // Act
        Optional<BibliographicalDataID> result =
                repository.findBibliographicalDataIdByTitle("Unknown");

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllBibliographicalDataByGenreId_shouldThrowWhenGenreIdIsNull() {
        // Arrange
        repository = new BibliographicalDataRepositoryJpaImpl(jpaRepository, assembler);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                repository.findAllBibliographicalDataByGenreId(null)
        );
    }

    @Test
    void findAllBibliographicalDataByGenreId_shouldReturnIds() {
        // Arrange
        repository = new BibliographicalDataRepositoryJpaImpl(jpaRepository, assembler);

        BibliographicalDataDM bdDM2 = mock(BibliographicalDataDM.class);
        BibliographicalDataID bdId2 = mock(BibliographicalDataID.class);

        when(jpaRepository.findByGenreIdsContaining(genreId)).thenReturn(List.of(bdDM, bdDM2));

        when(bdDM.getId()).thenReturn(bdId);
        when(bdDM2.getId()).thenReturn(bdId2);

        // Act
        List<BibliographicalDataID> result =
                repository.findAllBibliographicalDataByGenreId(genreId);

        // Assert
        assertEquals(List.of(bdId, bdId2), result);
    }

    @Test
    void findBibliographicalDataIdByAuthorId_shouldThrowWhenAuthorIdIsNull() {
        // Arrange
        repository = new BibliographicalDataRepositoryJpaImpl(jpaRepository, assembler);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                repository.findBibliographicalDataIdByAuthorId(null)
        );
    }

    @Test
    void findBibliographicalDataIdByAuthorId_shouldReturnIds() {
        // Arrange
        repository = new BibliographicalDataRepositoryJpaImpl(jpaRepository, assembler);

        BibliographicalDataDM bdDM2 = mock(BibliographicalDataDM.class);
        BibliographicalDataID bdId2 = mock(BibliographicalDataID.class);

        when(jpaRepository.findByAuthorIdsContaining(authorId)).thenReturn(List.of(bdDM, bdDM2));

        when(bdDM.getId()).thenReturn(bdId);
        when(bdDM2.getId()).thenReturn(bdId2);

        // Act
        List<BibliographicalDataID> result =
                repository.findBibliographicalDataIdByAuthorId(authorId);

        // Assert
        assertEquals(List.of(bdId, bdId2), result);
    }

    @Test
    void getAllPublicationsBD_shouldReturnDomainObjects() {
        // Arrange
        repository = new BibliographicalDataRepositoryJpaImpl(jpaRepository, assembler);

        BibliographicalDataDM bdDM2 = mock(BibliographicalDataDM.class);
        BibliographicalData bd2 = mock(BibliographicalData.class);

        when(jpaRepository.findAll()).thenReturn(List.of(bdDM, bdDM2));
        when(assembler.toDomain(bdDM)).thenReturn(bd);
        when(assembler.toDomain(bdDM2)).thenReturn(bd2);

        // Act
        List<BibliographicalData> result = repository.getAllPublicationsBD();

        // Assert
        assertEquals(List.of(bd, bd2), result);
    }

    @Test
    void getAllTitles_shouldReturnTitles() {
        // Arrange
        repository = new BibliographicalDataRepositoryJpaImpl(jpaRepository, assembler);

        BibliographicalDataDM bdDM2 = mock(BibliographicalDataDM.class);

        when(jpaRepository.findAll()).thenReturn(List.of(bdDM, bdDM2));

        when(bdDM.getTitle()).thenReturn("Title 1");
        when(bdDM2.getTitle()).thenReturn("Title 2");

        // Act
        List<String> result = repository.getAllTitles();

        // Assert
        assertEquals(List.of("Title 1", "Title 2"), result);
    }
}