package TOPSECRET.repository;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.BibliographicalData.BibliographicalDataFactory;
import TOPSECRET.domain.BibliographicalData.IBibliographicalDataRepository;
import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.BibliographicalDataID;
import TOPSECRET.domain.ValueObjects.GenreID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
    void constructor_AllowsEmptyList() {
        // Arrange
        List<BibliographicalData> empty = new ArrayList<>();
        BibliographicalDataFactory factory = mock(BibliographicalDataFactory.class);

        // Act
        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem(factory);

        // Assert
        assertTrue(repo.getAllPublicationsBD().isEmpty());
    }

    // Method findBibliographicalDataById ------------------------------------------------------------------------------
    @Test
    void findBibliographicalDataById_shouldReturnAValidBibliographicalData_whenIdIsValid() throws InstanceAlreadyExistsException {

        // Arrange
        BibliographicalData bibliographicalDataDouble = mock(BibliographicalData.class);
        String title = "Senhor do Anéis";
        AuthorId authorDouble = mock(AuthorId.class);
        List<AuthorId> authors = new ArrayList<>();
        authors.add(authorDouble);
        List<GenreID> genres = new ArrayList<>();
        GenreID genreDouble = mock(GenreID.class);
        genres.add(genreDouble);
        String chunksOfText = "this describes the book or magazine";

        BibliographicalDataID bdID = mock(BibliographicalDataID.class);
        when(bibliographicalDataDouble.id()).thenReturn(bdID);

        BibliographicalDataFactory factory = mock(BibliographicalDataFactory.class);
        when(factory.create(title, authors, Locale.ENGLISH, LocalDate.of(2010, 10, 10), genres, chunksOfText))
                .thenReturn(bibliographicalDataDouble);

        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem(factory);
        repo.createAndAdd(title, authors, Locale.ENGLISH, LocalDate.of(2010, 10, 10), genres, chunksOfText);

        // Act
        Optional<BibliographicalData> result = repo.findBibliographicalDataById(bdID);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(bibliographicalDataDouble, result.get());
    }

    @Test
    void findBibliographicalDataById_shouldReturnAValidBibliographicalData_whenIdIsValid_multipleBDs() throws InstanceAlreadyExistsException {

        // Arrange
        BibliographicalDataID bdID_1 = mock(BibliographicalDataID.class);
        BibliographicalDataID bdID_2 = mock(BibliographicalDataID.class);

        BibliographicalData bibliographicalDataDouble_1 = mock(BibliographicalData.class);
        when(bibliographicalDataDouble_1.id()).thenReturn(bdID_1);

        BibliographicalData bibliographicalDataDouble_2 = mock(BibliographicalData.class);
        when(bibliographicalDataDouble_2.id()).thenReturn(bdID_2);

        String title = "Senhor do Anéis";
        AuthorId authorDouble = mock(AuthorId.class);
        List<AuthorId> authors = new ArrayList<>();
        authors.add(authorDouble);
        List<GenreID> genres = new ArrayList<>();
        GenreID genreDouble = mock(GenreID.class);
        genres.add(genreDouble);
        String chunksOfText = "this describes the book or magazine";


        BibliographicalDataFactory factory = mock(BibliographicalDataFactory.class);

        when(factory.create(title, authors, Locale.ENGLISH, LocalDate.of(2010, 10, 10), genres, chunksOfText))
                .thenReturn(bibliographicalDataDouble_1);

        when(factory.create(title, authors, Locale.ENGLISH, LocalDate.of(2012, 10, 10), genres, chunksOfText))
                .thenReturn(bibliographicalDataDouble_2);

        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem(factory);
        repo.createAndAdd(title, authors, Locale.ENGLISH, LocalDate.of(2010, 10, 10), genres, chunksOfText);

        // Act
        Optional<BibliographicalData> result = repo.findBibliographicalDataById(bdID_1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(bibliographicalDataDouble_1, result.get());
    }

    @Test
    void findBibliographicalDataById_shouldReturnAEmpty_whenIdIsNotFound() throws InstanceAlreadyExistsException {

        // Arrange
        BibliographicalData bibliographicalDataDouble = mock(BibliographicalData.class);
        String title = "Senhor do Anéis";
        AuthorId authorDouble = mock(AuthorId.class);
        List<AuthorId> authors = new ArrayList<>();
        authors.add(authorDouble);
        List<GenreID> genres = new ArrayList<>();
        GenreID genreDouble = mock(GenreID.class);
        genres.add(genreDouble);
        String chunksOfText = "this describes the book or magazine";

        BibliographicalDataID bdID = mock(BibliographicalDataID.class);
        when(bibliographicalDataDouble.id()).thenReturn(bdID);

        BibliographicalDataID notFoundID = mock(BibliographicalDataID.class);

        BibliographicalDataFactory factory = mock(BibliographicalDataFactory.class);
        when(factory.create(title, authors, Locale.ENGLISH, LocalDate.of(2010, 10, 10), genres, chunksOfText))
                .thenReturn(bibliographicalDataDouble);

        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem(factory);
        repo.createAndAdd(title, authors, Locale.ENGLISH, LocalDate.of(2010, 10, 10), genres, chunksOfText);

        // Act
        Optional<BibliographicalData> result = repo.findBibliographicalDataById(notFoundID);

        // Assert
        assertTrue(result.isEmpty());
    }


    @Test
    void findBibliographicalDataById_shouldThrowAnException_whenIdIsNull() throws InstanceAlreadyExistsException {

        // Arrange
        BibliographicalData bibliographicalDataDouble = mock(BibliographicalData.class);
        String title = "Senhor do Anéis";
        AuthorId authorDouble = mock(AuthorId.class);
        List<AuthorId> authors = new ArrayList<>();
        authors.add(authorDouble);
        List<GenreID> genres = new ArrayList<>();
        GenreID genreDouble = mock(GenreID.class);
        genres.add(genreDouble);
        String chunksOfText = "this describes the book or magazine";

        BibliographicalDataFactory factory = mock(BibliographicalDataFactory.class);
        when(factory.create(title, authors, Locale.ENGLISH, LocalDate.of(2010, 10, 10), genres, chunksOfText))
                .thenReturn(bibliographicalDataDouble);

        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem(factory);
        repo.createAndAdd(title, authors, Locale.ENGLISH, LocalDate.of(2010, 10, 10), genres, chunksOfText);

        // Act + Assert
        assertThrows(NullPointerException.class, () -> repo.findBibliographicalDataById(null));
    }

    // Method findBibliographicalDataIdByTitle -------------------------------------------------------------------------


    @Test
    void findBibliographicalDataIdByTitle_shouldReturnBDId_whenTitleMatch() throws InstanceAlreadyExistsException {

        //Arrange
        BibliographicalDataID bdID_1 = mock(BibliographicalDataID.class);
        BibliographicalData bibliographicalDataDouble1 = mock(BibliographicalData.class);
        when(bibliographicalDataDouble1.id()).thenReturn(bdID_1);
        when(bibliographicalDataDouble1.getTitle()).thenReturn("Title1");

        BibliographicalDataID bdID_2 = mock(BibliographicalDataID.class);
        BibliographicalData bibliographicalDataDouble2 = mock(BibliographicalData.class);
        when(bibliographicalDataDouble2.id()).thenReturn(bdID_2);
        when(bibliographicalDataDouble2.getTitle()).thenReturn("Title2");


        AuthorId authorDouble = mock(AuthorId.class);
        List<AuthorId> authors = new ArrayList<>();
        authors.add(authorDouble);
        List<GenreID> genres = new ArrayList<>();
        GenreID genreDouble = mock(GenreID.class);


        BibliographicalDataFactory factory = mock(BibliographicalDataFactory.class);
        when(factory.create("Title1", authors, Locale.ENGLISH, LocalDate.of(2010, 10, 10), genres, "this describes the book or magazine"))
                .thenReturn(bibliographicalDataDouble1);
        when(factory.create("Title2", authors, Locale.ENGLISH, LocalDate.of(2010, 10, 10), genres, "this describes the book or magazine"))
                .thenReturn(bibliographicalDataDouble2);

        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem(factory);
        repo.createAndAdd("Title1", authors, Locale.ENGLISH, LocalDate.of(2010, 10, 10), genres, "this describes the book or magazine");
        repo.createAndAdd("Title2", authors, Locale.ENGLISH, LocalDate.of(2010, 10, 10), genres, "this describes the book or magazine");


        //Act
        Optional<BibliographicalDataID> result = repo.findBibliographicalDataIdByTitle("Title1");


        //Assert
        assertEquals(bdID_1,result.get());

    }


    @Test
    void findBibliographicalDataIdByTitle_shouldReturnEmpty_whenTitleDoesNotMatch() throws InstanceAlreadyExistsException {

        //Arrange
        BibliographicalDataID bdID_1 = mock(BibliographicalDataID.class);
        BibliographicalData bibliographicalDataDouble1 = mock(BibliographicalData.class);
        when(bibliographicalDataDouble1.id()).thenReturn(bdID_1);
        when(bibliographicalDataDouble1.getTitle()).thenReturn("Title1");

        BibliographicalDataID bdID_2 = mock(BibliographicalDataID.class);
        BibliographicalData bibliographicalDataDouble2 = mock(BibliographicalData.class);
        when(bibliographicalDataDouble2.id()).thenReturn(bdID_2);
        when(bibliographicalDataDouble2.getTitle()).thenReturn("Title2");


        AuthorId authorDouble = mock(AuthorId.class);
        List<AuthorId> authors = new ArrayList<>();
        authors.add(authorDouble);
        List<GenreID> genres = new ArrayList<>();
        GenreID genreDouble = mock(GenreID.class);


        BibliographicalDataFactory factory = mock(BibliographicalDataFactory.class);
        when(factory.create("Title1", authors, Locale.ENGLISH, LocalDate.of(2010, 10, 10), genres, "this describes the book or magazine"))
                .thenReturn(bibliographicalDataDouble1);
        when(factory.create("Title2", authors, Locale.ENGLISH, LocalDate.of(2010, 10, 10), genres, "this describes the book or magazine"))
                .thenReturn(bibliographicalDataDouble2);

        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem(factory);
        repo.createAndAdd("Title1", authors, Locale.ENGLISH, LocalDate.of(2010, 10, 10), genres, "this describes the book or magazine");
        repo.createAndAdd("Title2", authors, Locale.ENGLISH, LocalDate.of(2010, 10, 10), genres, "this describes the book or magazine");


        //Act
        Optional<BibliographicalDataID> result = repo.findBibliographicalDataIdByTitle("Title3");


        //Assert
        assertEquals(Optional.empty(), result);

    }


    @Test
    void findBibliographicalDataIdByTitle_shouldReturnId_whenTitleMatchesCaseInsensitively() throws InstanceAlreadyExistsException {

        //Arrange
        BibliographicalDataID bdID_1 = mock(BibliographicalDataID.class);
        BibliographicalData bibliographicalDataDouble1 = mock(BibliographicalData.class);
        when(bibliographicalDataDouble1.id()).thenReturn(bdID_1);
        when(bibliographicalDataDouble1.getTitle()).thenReturn("Title1");

        BibliographicalDataID bdID_2 = mock(BibliographicalDataID.class);
        BibliographicalData bibliographicalDataDouble2 = mock(BibliographicalData.class);
        when(bibliographicalDataDouble2.id()).thenReturn(bdID_2);
        when(bibliographicalDataDouble2.getTitle()).thenReturn("Title2");


        AuthorId authorDouble = mock(AuthorId.class);
        List<AuthorId> authors = new ArrayList<>();
        authors.add(authorDouble);
        List<GenreID> genres = new ArrayList<>();
        GenreID genreDouble = mock(GenreID.class);


        BibliographicalDataFactory factory = mock(BibliographicalDataFactory.class);
        when(factory.create("Title1", authors, Locale.ENGLISH, LocalDate.of(2010, 10, 10), genres, "this describes the book or magazine"))
                .thenReturn(bibliographicalDataDouble1);
        when(factory.create("Title2", authors, Locale.ENGLISH, LocalDate.of(2010, 10, 10), genres, "this describes the book or magazine"))
                .thenReturn(bibliographicalDataDouble2);

        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem(factory);
        repo.createAndAdd("Title1", authors, Locale.ENGLISH, LocalDate.of(2010, 10, 10), genres, "this describes the book or magazine");
        repo.createAndAdd("Title2", authors, Locale.ENGLISH, LocalDate.of(2010, 10, 10), genres, "this describes the book or magazine");


        //Act
        Optional<BibliographicalDataID> result = repo.findBibliographicalDataIdByTitle("TITLE1");


        //Assert
        assertEquals(bdID_1,result.get());

    }

    @Test
    void findBibliographicalDataIdByTitle_shouldReturnId_whenTitleMatchesWithLeadingOrTrailingWhitespace() throws InstanceAlreadyExistsException {

        //Arrange
        BibliographicalDataID bdID_1 = mock(BibliographicalDataID.class);
        BibliographicalData bibliographicalDataDouble1 = mock(BibliographicalData.class);
        when(bibliographicalDataDouble1.id()).thenReturn(bdID_1);
        when(bibliographicalDataDouble1.getTitle()).thenReturn("Title1");

        BibliographicalDataID bdID_2 = mock(BibliographicalDataID.class);
        BibliographicalData bibliographicalDataDouble2 = mock(BibliographicalData.class);
        when(bibliographicalDataDouble2.id()).thenReturn(bdID_2);
        when(bibliographicalDataDouble2.getTitle()).thenReturn("Title2");


        AuthorId authorDouble = mock(AuthorId.class);
        List<AuthorId> authors = new ArrayList<>();
        authors.add(authorDouble);
        List<GenreID> genres = new ArrayList<>();
        GenreID genreDouble = mock(GenreID.class);


        BibliographicalDataFactory factory = mock(BibliographicalDataFactory.class);
        when(factory.create("Title1", authors, Locale.ENGLISH, LocalDate.of(2010, 10, 10), genres, "this describes the book or magazine"))
                .thenReturn(bibliographicalDataDouble1);
        when(factory.create("Title2", authors, Locale.ENGLISH, LocalDate.of(2010, 10, 10), genres, "this describes the book or magazine"))
                .thenReturn(bibliographicalDataDouble2);

        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem(factory);
        repo.createAndAdd("Title1", authors, Locale.ENGLISH, LocalDate.of(2010, 10, 10), genres, "this describes the book or magazine");
        repo.createAndAdd("Title2", authors, Locale.ENGLISH, LocalDate.of(2010, 10, 10), genres, "this describes the book or magazine");


        //Act
        Optional<BibliographicalDataID> result = repo.findBibliographicalDataIdByTitle("     TITLE1    ");


        //Assert
        assertEquals(bdID_1,result.get());

    }

    @Test
    void findBibliographicalDataIdByTitle_shouldThrowNullPointerException_whenTitleIsNull() {

        //Arrange
        BibliographicalDataFactory factory = mock(BibliographicalDataFactory.class);
        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem(factory);


        //Act + Assert
        assertThrows(NullPointerException.class, () -> repo.findBibliographicalDataIdByTitle(null));

    }

    @Test
    void findBibliographicalDataIdByTitle_shouldThrowIllegalArgumentException_whenTitleIsBlank() {

        //Arrange
        BibliographicalDataFactory factory = mock(BibliographicalDataFactory.class);
        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem(factory);


        //Act + Assert
        assertThrows(IllegalArgumentException.class, () -> repo.findBibliographicalDataIdByTitle(" "));

    }

    // Method getAllPublicationsBD -------------------------------------------------------------------------------------

    @Test
    void getAllPublicationsBD_ReturnsAllItems() {
        // Arrange
        BibliographicalDataFactory factory = mock(BibliographicalDataFactory.class);
        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem(factory);

        // Act
        List<BibliographicalData> result = repo.getAllPublicationsBD();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllPublicationsBD_ReturnsUnmodifiableList() {
        // Arrange
        BibliographicalDataFactory factory = mock(BibliographicalDataFactory.class);
        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem(factory);

        // Act
        List<BibliographicalData> result = repo.getAllPublicationsBD();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> result.add(bd3Double));
    }

    @Test
    void getAllPublicationsBD_ReturnsNewCopyEachTime() throws Exception {
        // Arrange
        BibliographicalDataFactory factoryDouble = mock(BibliographicalDataFactory.class);
        when(factoryDouble.create(any(), any(), any(), any(), any(), any())).thenReturn(bd1Double);

        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem(factoryDouble);
        repo.createAndAdd("title1", List.of(), null, null, List.of(), "");

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

    // Method getAllTitles -------------------------------------------------------------------------------------

    @Test
    void getAllTitles_shouldReturnAllTitlesFromBibliographicalDataInTheSystem() throws InstanceAlreadyExistsException {

        BibliographicalData bd1 = mock(BibliographicalData.class);
        BibliographicalData bd2 = mock(BibliographicalData.class);
        BibliographicalData bd3 = mock(BibliographicalData.class);

        BibliographicalDataFactory factory = mock(BibliographicalDataFactory.class);
        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem(factory);

        List<AuthorId> authors = List.of(mock(AuthorId.class));
        List<GenreID> genres = List.of(mock(GenreID.class));

        when(factory.create("Title 1",authors,Locale.ENGLISH,LocalDate.of(2010, 10, 10),genres,"Some text"))
                .thenReturn(bd1);
        when(factory.create("Title 2",authors,Locale.ENGLISH,LocalDate.of(2010, 10, 10),genres,"Some text"))
                .thenReturn(bd2);
        when(factory.create("Title 3",authors,Locale.ENGLISH,LocalDate.of(2010, 10, 10),genres,"Some text"))
                .thenReturn(bd3);

        repo.createAndAdd("Title 1",authors,Locale.ENGLISH,LocalDate.of(2010, 10, 10),genres,"Some text");
        repo.createAndAdd("Title 2",authors,Locale.ENGLISH,LocalDate.of(2010, 10, 10),genres,"Some text");
        repo.createAndAdd("Title 3",authors,Locale.ENGLISH,LocalDate.of(2010, 10, 10),genres,"Some text");


        //Act
        List<String> result = repo.getAllTitles();


        //Assert
        assertEquals(result.size(),3);
    }

    // Method findBibliographicalDataIdByAuthorId -------------------------------------------------------------------------

    @Test
    void findBibliographicalDataIdByAuthorId_shouldReturnBibliographicalDataIds_whenAuthorExistsInMultipleBibliographicalData() throws Exception {
        // Arrange
        AuthorId authorIdDouble = mock(AuthorId.class);
        AuthorId otherAuthorIdDouble = mock(AuthorId.class);

        BibliographicalDataID bdId1Double = mock(BibliographicalDataID.class);
        BibliographicalDataID bdId2Double = mock(BibliographicalDataID.class);
        BibliographicalDataID bdId3Double = mock(BibliographicalDataID.class);

        when(bd1Double.getAuthors()).thenReturn(List.of(authorIdDouble, otherAuthorIdDouble));
        when(bd2Double.getAuthors()).thenReturn(List.of(authorIdDouble));
        when(bd3Double.getAuthors()).thenReturn(List.of(otherAuthorIdDouble));

        when(bd1Double.id()).thenReturn(bdId1Double);
        when(bd2Double.id()).thenReturn(bdId2Double);
        when(bd3Double.id()).thenReturn(bdId3Double);

        BibliographicalDataFactory factoryDouble = mock(BibliographicalDataFactory.class);
        when(factoryDouble.create(any(), any(), any(), any(), any(), any()))
                .thenReturn(bd1Double, bd2Double, bd3Double);

        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem(factoryDouble);
        repo.createAndAdd("title1", List.of(authorIdDouble), null, null, List.of(), "");
        repo.createAndAdd("title2", List.of(authorIdDouble), null, null, List.of(), "");
        repo.createAndAdd("title3", List.of(otherAuthorIdDouble), null, null, List.of(), "");

        // Act
        List<BibliographicalDataID> result = repo.findBibliographicalDataIdByAuthorId(authorIdDouble);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(bdId1Double));
        assertTrue(result.contains(bdId2Double));
    }

    @Test
    void findBibliographicalDataIdByAuthorId_shouldReturnEmptyList_whenAuthorDoesNotExistInAnyBibliographicalData() {
        // Arrange
        AuthorId authorId = mock(AuthorId.class);
        AuthorId otherAuthorId = mock(AuthorId.class);

        when(bd1Double.getAuthors()).thenReturn(List.of(otherAuthorId));
        when(bd2Double.getAuthors()).thenReturn(List.of(otherAuthorId));
        when(bd3Double.getAuthors()).thenReturn(List.of(otherAuthorId));

        BibliographicalDataFactory factory = mock(BibliographicalDataFactory.class);
        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem(
                factory);

        // Act
        List<BibliographicalDataID> result = repo.findBibliographicalDataIdByAuthorId(authorId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findBibliographicalDataIdByAuthorId_shouldReturnSingleBibliographicalDataId_whenAuthorExistsInOneBibliographicalData() throws Exception {
        // Arrange
        AuthorId authorIdDouble = mock(AuthorId.class);
        AuthorId otherAuthorIdDouble = mock(AuthorId.class);

        BibliographicalDataID bdId2Double = mock(BibliographicalDataID.class);

        when(bd1Double.getAuthors()).thenReturn(List.of(otherAuthorIdDouble));
        when(bd2Double.getAuthors()).thenReturn(List.of(authorIdDouble));
        when(bd3Double.getAuthors()).thenReturn(List.of(otherAuthorIdDouble));

        when(bd2Double.id()).thenReturn(bdId2Double);

        BibliographicalDataFactory factoryDouble = mock(BibliographicalDataFactory.class);
        when(factoryDouble.create(any(), any(), any(), any(), any(), any()))
                .thenReturn(bd1Double, bd2Double, bd3Double);

        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem(factoryDouble);
        repo.createAndAdd("title1", List.of(otherAuthorIdDouble), null, null, List.of(), "");
        repo.createAndAdd("title2", List.of(authorIdDouble), null, null, List.of(), "");
        repo.createAndAdd("title3", List.of(otherAuthorIdDouble), null, null, List.of(), "");

        // Act
        List<BibliographicalDataID> result = repo.findBibliographicalDataIdByAuthorId(authorIdDouble);

        // Assert
        assertEquals(1, result.size());
        assertEquals(bdId2Double, result.get(0));
    }

    @Test
    void findBibliographicalDataIdByAuthorId_shouldThrowIllegalArgumentException_whenAuthorIdIsNull() {
        // Arrange
        BibliographicalDataFactory factory = mock(BibliographicalDataFactory.class);
        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem(
                factory);

        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> repo.findBibliographicalDataIdByAuthorId(null)
        );

        assertEquals("authorId cannot be null.", exception.getMessage());
    }

    @Test
    void findAllBibliographicalDataByGenreId_shouldThrowException_whenGenreIdIsNull() {

        // Arrange
        BibliographicalDataFactory factory = mock(BibliographicalDataFactory.class);
        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem(factory);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> repo.findAllBibliographicalDataByGenreId(null));
    }

    @Test
    void findAllBibliographicalDataByGenreId_shouldReturnIds_whenGenreMatches() throws Exception {
        // Arrange
        BibliographicalDataFactory factoryDouble = mock(BibliographicalDataFactory.class);
        GenreID genreIdDouble = mock(GenreID.class);

        BibliographicalData bdDouble = mock(BibliographicalData.class);
        BibliographicalDataID bdIdDouble = mock(BibliographicalDataID.class);

        when(bdDouble.getGenres()).thenReturn(List.of(genreIdDouble));
        when(bdDouble.id()).thenReturn(bdIdDouble);
        when(factoryDouble.create(any(), any(), any(), any(), any(), any())).thenReturn(bdDouble);

        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem(factoryDouble);
        repo.createAndAdd("title1", List.of(), null, null, List.of(), "");

        // Act
        List<BibliographicalDataID> result = repo.findAllBibliographicalDataByGenreId(genreIdDouble);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(bdIdDouble));
    }

    @Test
    void findAllBibliographicalDataByGenreId_shouldReturnEmpty_whenNoMatch() {

        // Arrange
        BibliographicalDataFactory factory = mock(BibliographicalDataFactory.class);

        GenreID genreDouble1 = mock(GenreID.class);
        GenreID genreDouble2 = mock(GenreID.class);

        BibliographicalData bdDouble = mock(BibliographicalData.class);
        when(bdDouble.getGenres()).thenReturn(List.of(genreDouble1));

        IBibliographicalDataRepository repo =
                new BibliographicalDataRepositoryMem(factory);

        // Act
        List<BibliographicalDataID> result =
                repo.findAllBibliographicalDataByGenreId(genreDouble2);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllBibliographicalDataByGenreId_shouldReturnMultipleIds_whenMultipleMatch() throws Exception {
        // Arrange
        BibliographicalDataFactory factoryDouble = mock(BibliographicalDataFactory.class);
        GenreID genreIdDouble = mock(GenreID.class);

        BibliographicalData bdDouble1 = mock(BibliographicalData.class);
        BibliographicalData bdDouble2 = mock(BibliographicalData.class);

        BibliographicalDataID idDouble1 = mock(BibliographicalDataID.class);
        BibliographicalDataID idDouble2 = mock(BibliographicalDataID.class);

        when(bdDouble1.getGenres()).thenReturn(List.of(genreIdDouble));
        when(bdDouble2.getGenres()).thenReturn(List.of(genreIdDouble));

        when(bdDouble1.id()).thenReturn(idDouble1);
        when(bdDouble2.id()).thenReturn(idDouble2);

        when(factoryDouble.create(any(), any(), any(), any(), any(), any()))
                .thenReturn(bdDouble1, bdDouble2);

        IBibliographicalDataRepository repo = new BibliographicalDataRepositoryMem(factoryDouble);
        repo.createAndAdd("title1", List.of(), null, null, List.of(), "");
        repo.createAndAdd("title2", List.of(), null, null, List.of(), "");

        // Act
        List<BibliographicalDataID> result = repo.findAllBibliographicalDataByGenreId(genreIdDouble);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(idDouble1));
        assertTrue(result.contains(idDouble2));
    }
}