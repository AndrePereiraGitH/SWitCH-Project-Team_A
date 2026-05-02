package TOPSECRET.persistence.mem;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.GenreID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BibliographicalDataRepositoryMemTest {

    // =====================================================
    // =================== SetUp ===========================
    // =====================================================

    private BibliographicalData bd1Double;
    private BibliographicalData bd2Double;
    private BibliographicalData bd3Double;
    private BibliographicalDataID bd1ID;
    private BibliographicalDataID bd2ID;
    private BibliographicalDataID bd3ID;

    @BeforeEach
    void setup() {

        bd1Double = mock(BibliographicalData.class);
        bd2Double = mock(BibliographicalData.class);
        bd3Double = mock(BibliographicalData.class);

        bd1ID = mock(BibliographicalDataID.class);
        bd2ID = mock(BibliographicalDataID.class);
        bd3ID = mock(BibliographicalDataID.class);

        when(bd1Double.id()).thenReturn(bd1ID);
        when(bd2Double.id()).thenReturn(bd2ID);
        when(bd3Double.id()).thenReturn(bd3ID);
    }

    // =====================================================
    // =================== Tests ===========================
    // =====================================================

    // Method save -----------------------------------------------------------------------------------------------------
    @Test
    void shouldSaveBibliographicalDataToRepositoryWhenItDoesNotExist() throws InstanceAlreadyExistsException {
        // Arrange
        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem();

        // Act
        repo.save(bd1Double);
        repo.save(bd2Double);
        repo.save(bd3Double);

        // Assert
        assertEquals(3, repo.getAllTitles().size());
        assertTrue(repo.findAll().contains(bd1Double));
        assertTrue(repo.findAll().contains(bd2Double));
        assertTrue(repo.findAll().contains(bd3Double));
        assertEquals(bd1ID, bd1Double.id());
        assertEquals(bd2ID, bd2Double.id());
        assertEquals(bd3ID, bd3Double.id());
    }

    @Test
    void shouldThrowExceptionWhenAddingDuplicateBibliographicalData() throws InstanceAlreadyExistsException {
        // Arrange
        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem();
        BibliographicalData bd4Double = mock(BibliographicalData.class);
        when(bd4Double.id()).thenReturn(bd1ID);

        // Act
        repo.save(bd1Double);

        // Assert
        assertThrows(InstanceAlreadyExistsException.class, () -> repo.save(bd4Double));
        assertThrows(InstanceAlreadyExistsException.class, () -> repo.save(bd1Double));
    }

    @Test
    void saveShouldReturnSameInstance() throws InstanceAlreadyExistsException {
        // Arrange
        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem();

        // Act
        var result = repo.save(bd1Double);

        // Assert
        assertSame(bd1Double, result);
    }

    // Method findBibliographicalDataById ----------------------------------------------------------------------------
    @Test
    void findBibliographicalDataById_shouldReturnAValidBibliographicalData_whenIdIsValid() throws InstanceAlreadyExistsException {
        // Arrange
        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem();

        // Act
        repo.save(bd1Double);
        Optional<BibliographicalData> result = repo.findById(bd1ID);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(bd1Double, result.get());
    }

    @Test
    void findBibliographicalDataById_shouldReturnAValidBibliographicalData_whenIdIsValid_multipleBDs() throws InstanceAlreadyExistsException {
        // Arrange
        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem();

        // Act
        repo.save(bd1Double);
        repo.save(bd2Double);
        repo.save(bd3Double);
        Optional<BibliographicalData> result = repo.findById(bd2ID);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(bd2Double, result.get());
    }

    @Test
    void findBibliographicalDataById_shouldReturnAnEmpty_whenIdIsNotFound() throws InstanceAlreadyExistsException {
        // Arrange
        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem();

        // Act
        repo.save(bd2Double);
        repo.save(bd3Double);
        Optional<BibliographicalData> result = repo.findById(bd1ID);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findBibliographicalDataById_shouldThrowAnException_whenIdIsNull() throws InstanceAlreadyExistsException {
        // Arrange
        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem();

        // Act + Assert
        assertThrows(NullPointerException.class, () -> repo.findById(null));
    }


    // Method findBibliographicalDataIdByTitle -------------------------------------------------------------------------
    @Test
    void findBibliographicalDataIdByTitle_shouldReturnBDId_whenTitleMatch() throws InstanceAlreadyExistsException {
        //Arrange
        when(bd1Double.getTitle()).thenReturn("double1");
        when(bd2Double.getTitle()).thenReturn("double2");

        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem();

        //Act
        repo.save(bd1Double);
        repo.save(bd2Double);
        Optional<BibliographicalDataID> result = repo.findIdByTitle("double2");

        //Assert
        assertTrue(result.isPresent());
        assertEquals(bd2ID, result.get());
    }

    @Test
    void findBibliographicalDataIdByTitle_shouldReturnEmpty_whenTitleDoesNotMatch() throws InstanceAlreadyExistsException {
        //Arrange
        when(bd1Double.getTitle()).thenReturn("double1");
        when(bd2Double.getTitle()).thenReturn("double2");

        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem();

        //Act
        repo.save(bd1Double);
        repo.save(bd2Double);
        Optional<BibliographicalDataID> result = repo.findIdByTitle("Title3");

        //Assert
        assertEquals(Optional.empty(), result);
    }

    @Test
    void findBibliographicalDataIdByTitle_shouldReturnId_whenTitleMatchesCaseInsensitively() throws InstanceAlreadyExistsException {
        //Arrange
        when(bd1Double.getTitle()).thenReturn("double1");
        when(bd2Double.getTitle()).thenReturn("double2");

        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem();

        //Act
        repo.save(bd1Double);
        repo.save(bd2Double);
        Optional<BibliographicalDataID> result = repo.findIdByTitle("DOUBLE1");

        //Assert
        assertTrue(result.isPresent());
        assertEquals(bd1ID,result.get());
    }

    @Test
    void findBibliographicalDataIdByTitle_shouldReturnId_whenTitleMatchesWithLeadingOrTrailingWhitespace() throws InstanceAlreadyExistsException {
        //Arrange
        when(bd1Double.getTitle()).thenReturn("double1");
        when(bd2Double.getTitle()).thenReturn("double2");

        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem();

        //Act
        repo.save(bd1Double);
        repo.save(bd2Double);
        Optional<BibliographicalDataID> result = repo.findIdByTitle("     DOUBLE2    ");

        //Assert
        assertTrue(result.isPresent());
        assertEquals(bd2ID,result.get());
    }

    @Test
    void findBibliographicalDataIdByTitle_shouldThrowNullPointerException_whenTitleIsNull() {
        //Arrange
        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem();

        //Act + Assert
        assertThrows(NullPointerException.class, () -> repo.findIdByTitle(null));
    }

    @Test
    void findBibliographicalDataIdByTitle_shouldThrowIllegalArgumentException_whenTitleIsBlank() {
        //Arrange
        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem();

        //Act + Assert
        assertThrows(IllegalArgumentException.class, () -> repo.findIdByTitle(" "));
    }


    // Method getAllPublicationsBD -------------------------------------------------------------------------------------
    @Test
    void getAllPublicationsBD_ReturnsAllItems() throws InstanceAlreadyExistsException {
        // Arrange
        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem();

        // Act
        repo.save(bd1Double);
        repo.save(bd2Double);
        List<BibliographicalData> result = repo.findAll();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(bd1Double));
        assertTrue(result.contains(bd2Double));
    }

    @Test
    void getAllPublicationsBD_ReturnsUnmodifiableList() throws InstanceAlreadyExistsException {
        // Arrange
        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem();

        // Act
        repo.save(bd1Double);
        repo.save(bd2Double);
        List<BibliographicalData> result = repo.findAll();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(bd3Double));
    }

    @Test
    void getAllPublicationsBD_ReturnsNewCopyEachTime() throws InstanceAlreadyExistsException {
        // Arrange
        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem();

        // Act
        repo.save(bd1Double);
        repo.save(bd2Double);
        List<BibliographicalData> first = repo.findAll();
        List<BibliographicalData> second = repo.findAll();

        // Assert
        assertNotSame(first, second);
    }


    // Method getAllTitles ---------------------------------------------------------------------------------------------
    @Test
    void getAllTitles_shouldReturnAllTitlesFromBibliographicalDataInTheSystem() throws InstanceAlreadyExistsException {
        // Arrange
        when(bd1Double.getTitle()).thenReturn("double1");
        when(bd2Double.getTitle()).thenReturn("double2");
        when(bd3Double.getTitle()).thenReturn("double3");

        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem();

        //Act
        repo.save(bd1Double);
        repo.save(bd2Double);
        repo.save(bd3Double);
        List<String> result = repo.getAllTitles();

        //Assert
        assertEquals(3, result.size());
    }


    // Method findBibliographicalDataIdByAuthorId ----------------------------------------------------------------------
    @Test
    void findBibliographicalDataIdByAuthorId_shouldReturnBibliographicalDataIds_whenAuthorExistsInMultipleBibliographicalData() throws InstanceAlreadyExistsException {
        // Arrange
        AuthorId authorId = mock(AuthorId.class);

        when(bd1Double.getAuthors()).thenReturn(List.of(authorId));
        when(bd2Double.getAuthors()).thenReturn(List.of(authorId));

        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem();

        // Act
        repo.save(bd1Double);
        repo.save(bd2Double);
        List<BibliographicalDataID> result = repo.findIdByAuthorId(authorId);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(bd1ID));
        assertTrue(result.contains(bd2ID));
    }

    @Test
    void findBibliographicalDataIdByAuthorId_shouldReturnEmptyList_whenAuthorDoesNotExistInAnyBibliographicalData() {
        // Arrange
        AuthorId authorId = mock(AuthorId.class);
        AuthorId otherAuthorId = mock(AuthorId.class);

        when(bd1Double.getAuthors()).thenReturn(List.of(otherAuthorId));
        when(bd2Double.getAuthors()).thenReturn(List.of(otherAuthorId));
        when(bd3Double.getAuthors()).thenReturn(List.of(otherAuthorId));

        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem();

        // Act
        List<BibliographicalDataID> result = repo.findIdByAuthorId(authorId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findBibliographicalDataIdByAuthorId_shouldReturnSingleBibliographicalDataId_whenAuthorExistsInOneBibliographicalData() throws InstanceAlreadyExistsException {
        // Arrange
        AuthorId authorId = mock(AuthorId.class);
        AuthorId otherAuthorId = mock(AuthorId.class);

        BibliographicalDataID bdId2 = mock(BibliographicalDataID.class);

        when(bd1Double.getAuthors()).thenReturn(List.of(otherAuthorId));
        when(bd2Double.getAuthors()).thenReturn(List.of(authorId));
        when(bd3Double.getAuthors()).thenReturn(List.of(otherAuthorId));

        when(bd2Double.id()).thenReturn(bdId2);

        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem();

        // Act
        repo.save(bd1Double);
        repo.save(bd2Double);
        repo.save(bd3Double);
        List<BibliographicalDataID> result = repo.findIdByAuthorId(authorId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(bdId2, result.get(0));
    }

    @Test
    void findBibliographicalDataIdByAuthorId_shouldThrowIllegalArgumentException_whenAuthorIdIsNull() {
        // Arrange
        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem();

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> repo.findIdByAuthorId(null));
    }


    // Method findAllBibliographicalDataByGenreId ----------------------------------------------------------------------
    @Test
    void findAllBibliographicalDataByGenreId_shouldThrowException_whenGenreIdIsNull() {
        // Arrange
        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem();

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> repo.findAllByGenreId(null));
    }

    @Test
    void findAllBibliographicalDataByGenreId_shouldReturnIds_whenGenreMatches() throws InstanceAlreadyExistsException {
        // Arrange
        GenreID genreIdDouble = mock(GenreID.class);

        when(bd1Double.getGenres()).thenReturn(List.of(genreIdDouble));
        when(bd1Double.id()).thenReturn(bd1ID);

        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem();

        // Act
        repo.save(bd1Double);
        List<BibliographicalDataID> result =
                repo.findAllByGenreId(genreIdDouble);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(bd1ID));
    }

    @Test
    void findAllBibliographicalDataByGenreId_shouldReturnEmpty_whenNoMatch() throws InstanceAlreadyExistsException {
        // Arrange
        GenreID genreDouble1 = mock(GenreID.class);
        GenreID genreDouble2 = mock(GenreID.class);

        when(bd1Double.getGenres()).thenReturn(List.of(genreDouble1));

        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem();

        // Act
        repo.save(bd1Double);
        List<BibliographicalDataID> result =
                repo.findAllByGenreId(genreDouble2);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllBibliographicalDataByGenreId_shouldReturnMultipleIds_whenMultipleMatch() throws InstanceAlreadyExistsException {
        // Arrange
        GenreID genreIdDouble = mock(GenreID.class);

        when(bd1Double.getGenres()).thenReturn(List.of(genreIdDouble));
        when(bd2Double.getGenres()).thenReturn(List.of(genreIdDouble));

        when(bd1Double.id()).thenReturn(bd1ID);
        when(bd2Double.id()).thenReturn(bd2ID);

        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem();

        // Act
        repo.save(bd1Double);
        repo.save(bd2Double);
        List<BibliographicalDataID> result =
                repo.findAllByGenreId(genreIdDouble);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(bd1ID));
        assertTrue(result.contains(bd2ID));
    }

}
