package TOPSECRET.repository;

import TOPSECRET.domain.Author.Author;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.BibliographicalDataFactory;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.GenreID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BibliographicalDataRepositoryMemTest {

    // =====================================================
    // =================== SetUP ===========================
    // =====================================================

    private BibliographicalData bd1Double;
    private BibliographicalData bd2Double;
    private BibliographicalData bd3Double;

    @BeforeEach
    void setup() {

        bd1Double = mock(BibliographicalData.class);
        bd2Double = mock(BibliographicalData.class);
        bd3Double = mock(BibliographicalData.class);
    }

    // =====================================================
    // =================== Tests ===========================
    // =====================================================

    // Constructor -----------------------------------------------------------------------------------------------------
    @Test
    void constructor_CopiesListDefensively() {
        // Arrange
        List<BibliographicalData> original = new ArrayList<>();
        original.add(bd1Double);
        original.add(bd2Double);

        BibliographicalDataFactory factory = mock(BibliographicalDataFactory.class);

        // Act
        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem(factory, original);
        original.add(bd3Double);                                                           // Modifies the original list

        // Assert
        assertEquals(2, repo.getAllPublicationsBD().size());        // The repository should have the same size
    }

    @Test
    void constructor_ThrowsExceptionWhenListIsNull() {
        // Arrange
        List<BibliographicalData> nullList = null;
        BibliographicalDataFactory factory = mock(BibliographicalDataFactory.class);

        // Act + Assert
        assertThrows(NullPointerException.class, () -> new BibliographicalDataRepositoryMem(factory, nullList));

    }

    @Test
    void constructor_AllowsEmptyList() {
        // Arrange
        List<BibliographicalData> empty = new ArrayList<>();
        BibliographicalDataFactory factory = mock(BibliographicalDataFactory.class);

        // Act
        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem(factory, empty);

        // Assert
        assertTrue(repo.getAllPublicationsBD().isEmpty());
    }

    // Method getAllPublicationsBD -------------------------------------------------------------------------------------

    @Test
    void getAllPublicationsBD_ReturnsAllItems() {
        // Arrange
        List<BibliographicalData> original = List.of(bd1Double, bd2Double);
        BibliographicalDataFactory factory = mock(BibliographicalDataFactory.class);
        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem(factory, original);

        // Act
        List<BibliographicalData> result = repo.getAllPublicationsBD();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(bd1Double));
        assertTrue(result.contains(bd2Double));
    }

    @Test
    void getAllPublicationsBD_ReturnsUnmodifiableList() {
        // Arrange
        List<BibliographicalData> original = List.of(bd1Double, bd2Double);
        BibliographicalDataFactory factory = mock(BibliographicalDataFactory.class);
        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem(factory, original);

        // Act
        List<BibliographicalData> result = repo.getAllPublicationsBD();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(bd3Double));
    }

    @Test
    void getAllPublicationsBD_ReturnsNewCopyEachTime() {
        // Arrange
        List<BibliographicalData> original = List.of(bd1Double, bd2Double);
        BibliographicalDataFactory factory = mock(BibliographicalDataFactory.class);
        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem(factory, original);

        // Act
        List<BibliographicalData> first = repo.getAllPublicationsBD();
        List<BibliographicalData> second = repo.getAllPublicationsBD();

        // Assert
        assertNotSame(first, second);
    }

    //============================================================
    // TESTING - CREATE AND ADD WITH FACTORY
    @Test
    void shouldReturnValidNewBibliographicalDataWithFactory () throws InstanceAlreadyExistsException {

        // Arrange
        BibliographicalData BibliographicalDataDouble = mock(BibliographicalData.class);
        String title = "Senhor do Anéis";
        AuthorId authorDouble = mock(AuthorId.class);
        List<AuthorId> authors = new ArrayList<>();
        authors.add(authorDouble);
        List<GenreID> genres = new ArrayList<>();
        GenreID genreDouble = mock(GenreID.class);
        genres.add(genreDouble);
        String chunksOfText = "this describes the book or magazine";

        BibliographicalDataFactory factory = mock(BibliographicalDataFactory.class);
        IBibliographicalDataRepository bdRepository = new BibliographicalDataRepositoryMem(factory);


        when(factory.create(title, authors, Locale.ENGLISH, LocalDate.of(2010, 10, 10), genres, chunksOfText)).thenReturn(BibliographicalDataDouble);

        // Act
        BibliographicalData result = bdRepository.createAndAdd(title, authors, Locale.ENGLISH, LocalDate.of(2010, 10, 10), genres, chunksOfText);

        // Assert
        verify(factory).create(title, authors, Locale.ENGLISH, LocalDate.of(2010, 10, 10), genres, chunksOfText);
        assertEquals(BibliographicalDataDouble, result);
        assertTrue(bdRepository.getAllPublicationsBD().contains(BibliographicalDataDouble));
        assertEquals(1, bdRepository.getAllPublicationsBD().size());
    }

    @Test
    void createAndAdd_shouldThrowException_whenTitleIsNull() {

        // Arrange

        BibliographicalDataFactory factory = mock(BibliographicalDataFactory.class);
        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem(factory);

        List<AuthorId> authors = List.of(mock(AuthorId.class));
        List<GenreID> genres = List.of(mock(GenreID.class));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.createAndAdd(
                        null,
                        authors,
                        Locale.ENGLISH,
                        LocalDate.of(2010, 10, 10),
                        genres,
                        "Some text"
                )
        );

        verify(factory, never())
                .create(any(), any(), any(), any(), any(), any());
    }

    @Test
    void createAndAdd_shouldThrowException_whenAuthorsIsNull() {

        // Arrange
        BibliographicalDataFactory factory = mock(BibliographicalDataFactory.class);
        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem(factory);

        List<GenreID> genres = List.of(mock(GenreID.class));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.createAndAdd(
                        "Title",
                        null,
                        Locale.ENGLISH,
                        LocalDate.of(2010, 10, 10),
                        genres,
                        "Some text"
                )
        );

        verify(factory, never())
                .create(any(), any(), any(), any(), any(), any());
    }

    @Test
    void createAndAdd_shouldThrowException_whenGenresIsNull() {

        // Arrange
        BibliographicalDataFactory factory = mock(BibliographicalDataFactory.class);
        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem(factory);

        List<AuthorId> authors = List.of(mock(AuthorId.class));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.createAndAdd(
                        "Title",
                        authors,
                        Locale.ENGLISH,
                        LocalDate.of(2010, 10, 10),
                        null,
                        "Some text"
                )
        );

        verify(factory, never())
                .create(any(), any(), any(), any(), any(), any());
    }

    @Test
    void createAndAdd_shouldThrowException_whenChunksOfTextIsNull() {

        // Arrange
        BibliographicalDataFactory factory = mock(BibliographicalDataFactory.class);
        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem(factory);

        List<AuthorId> authors = List.of(mock(AuthorId.class));
        List<GenreID> genres = List.of(mock(GenreID.class));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                repo.createAndAdd(
                        "Title",
                        authors,
                        Locale.ENGLISH,
                        LocalDate.of(2010, 10, 10),
                        genres,
                        null
                )
        );

        verify(factory, never())
                .create(any(), any(), any(), any(), any(), any());
    }

}