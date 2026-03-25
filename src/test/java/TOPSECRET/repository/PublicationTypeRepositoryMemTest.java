package TOPSECRET.repository;

import TOPSECRET.domain.PublicationTypeFactory;
import TOPSECRET.domain.PublicationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test with mocks for {@link IPublicationTypeRepository}
 * Used mock for {@link PublicationTypeFactory}
 */
class PublicationTypeRepositoryMemTest {

    /**
     * Initial setup for tests
     * Creation of PublicationTypeRepositoryMem object
     * Creation of {@link PublicationTypeFactory} mock
     */
    PublicationTypeFactory factory;
    IPublicationTypeRepository repo;

    @BeforeEach
    void setup()
    {
        factory = mock(PublicationTypeFactory.class);
        repo = new PublicationTypeRepositoryMem(factory);
    }

    /**
     * Creating publication types should delegate creation to factory
     */

    @Test
    void shouldThrowExceptionWhenAddingNullPublicationType(){
        assertThrows(IllegalArgumentException.class, () -> repo.addToList(null));
    }

    @Test
    void createPublicationTypeDelegatesToFactory()
    {
        String mediaFormat = "Book";

        PublicationType expected = mock(PublicationType.class);

        when(factory.create(mediaFormat)).thenReturn(expected);

        PublicationType actual = repo.create(mediaFormat);

        assertEquals(expected, actual);
//        verify(factory,  times(1)).create(mediaFormat);
//        verifyNoMoreInteractions(factory);
    }

    /**
     * Creating an invalid publication type should throw when creating
     */
    @Test
    void invalidPublicationShouldThrowWhenCreating()
    {
        String mediaFormat = "Book";

        when(factory.create(mediaFormat)).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class, () -> repo.create(mediaFormat));
//        verify(factory, times(1)).create(mediaFormat, typeOfBinding, details);
//        verifyNoMoreInteractions(factory);
    }

    @Test
    void shouldNotAddDuplicatedIgnoringCaseAndSpaces(){
        // Arrange
        IPublicationTypeRepository repo = new PublicationTypeRepositoryMem(factory);

        PublicationType pt1 = mock(PublicationType.class);
        when(pt1.getMediaFormat()).thenReturn("Book");
        PublicationType pt2 = mock(PublicationType.class);
        when(pt2.getMediaFormat()).thenReturn("   bOoK   ");

        repo.addToList(pt1);

        // Assert & Act
        assertThrows(IllegalArgumentException.class, () -> repo.addToList(pt2));
    }

    @Test
    void addToListShouldReturnPublicationType(){
        // Arrange
        IPublicationTypeRepository repo = new PublicationTypeRepositoryMem(factory);

        PublicationType pt = mock(PublicationType.class);
        when(pt.getMediaFormat()).thenReturn("BOOK");

        // Act
        PublicationType result = repo.addToList(pt);

        // Assert
        assertNotNull(result);
        assertEquals(pt, result);
    }

    @Test
    void shouldAddDifferentPublicationTypes(){
        // Arrange
        IPublicationTypeRepository repo = new PublicationTypeRepositoryMem(factory);

        PublicationType pt1 = mock(PublicationType.class);
        when(pt1.getMediaFormat()).thenReturn("Book");

        PublicationType pt2 = mock(PublicationType.class);
        when(pt2.getMediaFormat()).thenReturn("Magazine");

        repo.addToList(pt1);

        // Act & Assert
        assertDoesNotThrow(() -> repo.addToList(pt2));
    }

    @Test
    void shouldFindPublicationTypeByAttributes(){
        // Arrange
        PublicationType pt = mock(PublicationType.class);
        when(pt.getMediaFormat()).thenReturn("BOOK");

        repo.addToList(pt);

        // Act
        PublicationType result = repo.findByAttributes("BOOK");

        // Assert
        assertEquals(pt, result);
    }

    @Test
    void shouldThrowWhenPublicationTypeNotFound(){
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> repo.findByAttributes("MAGAZINE"));
    }

    @Test
    void shouldThrowWhenMediaFormatIsNull(){
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> repo.findByAttributes(null));
    }



}