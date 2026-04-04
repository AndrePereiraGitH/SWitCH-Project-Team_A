package TOPSECRET.repository;

import TOPSECRET.domain.PublicationType.IPublicationTypeRepository;
import TOPSECRET.domain.PublicationType.PublicationTypeFactory;
import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PublicationTypeRepositoryMem}.
 *
 * <p>
 * Validates creation, duplication rules, retrieval,
 * and validation of PublicationType entities.
 * </p>
 *
 * <p>
 * Uses a mocked factory to isolate repository behavior.
 * </p>
 */
class PublicationTypeRepositoryMemTest {


    PublicationTypeFactory factory;
    IPublicationTypeRepository repo;

    @BeforeEach
    void setup()
    {
        factory = mock(PublicationTypeFactory.class);
        repo = new PublicationTypeRepositoryMem(factory);
    }

    @Test
    void shouldThrowExceptionWhenAddingNullPublicationType(){
        assertThrows(IllegalArgumentException.class, () -> repo.addToList(null));
    }

    @Test
    void createPublicationTypeDelegatesToFactory()
    {
        // ARRANGE
        String mediaFormat = "Book";

        PublicationType expected = mock(PublicationType.class);
        when(expected.getMediaFormat()).thenReturn(mediaFormat);
        when(factory.create(mediaFormat)).thenReturn(expected);

        // ACT
        PublicationType actual = repo.create(mediaFormat);

        // ASSERT
        assertEquals(expected, actual);
        verify(factory).create(mediaFormat);
    }


    @Test
    void invalidPublicationShouldThrowWhenCreating()
    {
        // ARRANGE
        String mediaFormat = "Book";
        when(factory.create(mediaFormat)).thenThrow(new IllegalArgumentException());

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () -> repo.create(mediaFormat));
    }

    @Test
    void shouldNotAddDuplicatedIgnoringCaseAndSpaces(){

        // ARRANGE
        PublicationType pt1 = mock(PublicationType.class);
        when(pt1.getMediaFormat()).thenReturn("Book");

        PublicationType pt2 = mock(PublicationType.class);
        when(pt2.getMediaFormat()).thenReturn("   bOoK   ");

        repo.addToList(pt1);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () -> repo.addToList(pt2));
    }

    @Test
    void addToListShouldReturnPublicationType(){

        // ARRANGE
        IPublicationTypeRepository repo = new PublicationTypeRepositoryMem(factory);

        PublicationType pt = mock(PublicationType.class);
        when(pt.getMediaFormat()).thenReturn("BOOK");

        // ACT
        PublicationType result = repo.addToList(pt);

        // ASSERT
        assertNotNull(result);
        assertEquals(pt, result);
    }

    @Test
    void shouldAddDifferentPublicationTypes(){

        // ARRANGE
        IPublicationTypeRepository repo = new PublicationTypeRepositoryMem(factory);

        PublicationType pt1 = mock(PublicationType.class);
        when(pt1.getMediaFormat()).thenReturn("Book");

        PublicationType pt2 = mock(PublicationType.class);
        when(pt2.getMediaFormat()).thenReturn("Magazine");

        repo.addToList(pt1);

        // ACT + ASSERT
        assertDoesNotThrow(() -> repo.addToList(pt2));
    }

    @Test
    void shouldFindPublicationTypeByAttributes(){
        // ARRANGE
        PublicationType pt = mock(PublicationType.class);
        when(pt.getMediaFormat()).thenReturn("BOOK");

        repo.addToList(pt);

        // ACT
        PublicationType result = repo.findByAttributes("BOOK");

        // ASSERT
        assertEquals(pt, result);
    }

    @Test
    void shouldThrowWhenPublicationTypeNotFound(){

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> repo.findByAttributes("MAGAZINE"));
    }

    @Test
    void shouldThrowWhenMediaFormatIsNull(){

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> repo.findByAttributes(null));
    }

    @Test
    void shouldReturnAllPublicationTypes() {

        // ARRANGE
        PublicationType pt = mock(PublicationType.class);
        when(pt.getMediaFormat()).thenReturn("Book");

        repo.addToList(pt);

        // ACT
        List<PublicationType> result = repo.getAllPublicationTypes();

        // ASSERT
        assertEquals(1, result.size());
        assertEquals(pt, result.get(0));
    }

    @Test
    void shouldFindPublicationTypeIgnoringCaseAndSpaces() {

        // ARRANGE
        PublicationType pt = mock(PublicationType.class);
        when(pt.getMediaFormat()).thenReturn("   Book   ");

        repo.addToList(pt);

        // ACT
        PublicationType result = repo.findByAttributes("book");

        // ASSERT
        assertEquals(pt, result);
    }

    @Test
    void shouldThrowExceptionWhenMediaFormatIsBlank() {

        // ACT
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> repo.findByAttributes("   ")
        );

        // ASSERT
        assertEquals("MediaFormat can't be null.", exception.getMessage());
    }

    // ===========================================================
    // ===== Tests for findPublicationTypeById ===================
    // ===========================================================

    @Test
    void findPublicationTypeById_shouldThrowNullPointerException_whenIdIsNull() {

        // ACT + ASSERT
        assertThrows(NullPointerException.class,
                () -> repo.findPublicationTypeById(null));
    }

    @Test
    void findPublicationTypeById_shouldReturnEmpty_whenIdDoesNotExist() {

        // ARRANGE
        PublicationTypeId pubTypeId = mock(PublicationTypeId.class);

        // ACT
        Optional<PublicationType> result = repo.findPublicationTypeById(pubTypeId);

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void findPublicationTypeById_shouldReturnPublicationType_whenIdExists() {

        // ARRANGE
        PublicationTypeId pubTypeId = mock(PublicationTypeId.class);

        PublicationType pt = mock(PublicationType.class);
        when(pt.getMediaFormat()).thenReturn("Book");
        when(pt.id()).thenReturn(pubTypeId);

        repo.addToList(pt);

        // ACT
        Optional<PublicationType> result = repo.findPublicationTypeById(pubTypeId);

        // ASSERT
        assertTrue(result.isPresent());
        assertEquals(pt, result.get());
    }

    @Test
    void findPublicationTypeById_shouldReturnEmpty_whenRepositoryIsEmpty() {

        // ARRANGE
        PublicationTypeId pubTypeId = mock(PublicationTypeId.class);

        // ACT
        Optional<PublicationType> result = repo.findPublicationTypeById(pubTypeId);

        // ASSERT
        assertTrue(result.isEmpty());
    }

    @Test
    void findPublicationTypeById_shouldReturnCorrectPublicationType_whenMultipleExist() {

        // ARRANGE
        PublicationTypeId id1 = mock(PublicationTypeId.class);
        PublicationTypeId id2 = mock(PublicationTypeId.class);

        PublicationType pt1 = mock(PublicationType.class);
        when(pt1.getMediaFormat()).thenReturn("Book");
        when(pt1.id()).thenReturn(id1);

        PublicationType pt2 = mock(PublicationType.class);
        when(pt2.getMediaFormat()).thenReturn("Magazine");
        when(pt2.id()).thenReturn(id2);

        repo.addToList(pt1);
        repo.addToList(pt2);

        // ACT
        Optional<PublicationType> result = repo.findPublicationTypeById(id2);

        // ASSERT
        assertTrue(result.isPresent());
        assertEquals(pt2, result.get());
    }


}