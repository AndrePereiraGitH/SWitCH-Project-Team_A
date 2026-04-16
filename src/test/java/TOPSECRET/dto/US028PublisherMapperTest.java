package TOPSECRET.dto;

import TOPSECRET.domain.Publisher.Publisher;
import TOPSECRET.domain.ValueObjects.PublisherID;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link US028PublisherMapper}.
 *
 * <p>Verifies the correct behaviour of the mapping methods:
 * <ul>
 *   <li>{@link US028PublisherMapper#toDTO(Publisher)} - maps a single {@link Publisher}
 *   to a {@link US028PublisherDTO}</li>
 *   <li>{@link US028PublisherMapper#toDTOs(List)} - maps a list of {@link Publisher}
 *   to a list of {@link US028PublisherDTO}</li>
 * </ul>
 *
 * <p>All {@link Publisher} dependencies are replaced with Mockito doubles
 * to ensure tests are isolated from domain logic.
 */
class US028PublisherMapperTest {

    // -------------------------
    // Constructor
    // -------------------------

    @Test
    void constructor_ShouldInstantiateSuccessfully() throws Exception {
        // Arrange
        Constructor<US028PublisherMapper> constructor = US028PublisherMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // Act
        US028PublisherMapper mapper = constructor.newInstance();

        // Assert
        assertNotNull(mapper);
    }

    // -------------------------
    // toDTO
    // -------------------------

    @Test
    void toDTO_ShouldThrowIllegalArgumentException_WhenPublisherIsNull() {
        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> US028PublisherMapper.toDTO(null));

        assertEquals("Publisher cannot be null", ex.getMessage());
    }

    @Test
    void toDTO_ShouldReturnDTO_WhenPublisherIsValid() {
        // Arrange
        PublisherID publisherId = mock(PublisherID.class);
        Publisher publisherDouble = createPublisherDouble("Porto Editora", publisherId);

        // Act
        US028PublisherDTO result = US028PublisherMapper.toDTO(publisherDouble);

        // Assert
        assertNotNull(result);
        assertEquals("Porto Editora", result.name());
        assertEquals(publisherId, result.publisherId());
    }

    // -------------------------
    // toDTOs
    // -------------------------

    @Test
    void toDTOs_ShouldThrowIllegalArgumentException_WhenListIsNull() {
        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> US028PublisherMapper.toDTOs(null));

        assertEquals("Publishers List cannot be null", ex.getMessage());
    }

    @Test
    void toDTOs_ShouldReturnEmptyList_WhenListIsEmpty() {
        // Arrange
        List<Publisher> publishers = List.of();

        // Act
        List<US028PublisherDTO> result = US028PublisherMapper.toDTOs(publishers);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void toDTOs_ShouldReturnListWithOneDTO_WhenListHasOnePublisher() {
        // Arrange
        PublisherID publisherId = mock(PublisherID.class);
        Publisher publisherDouble = createPublisherDouble("Porto Editora", publisherId);
        List<Publisher> publishers = List.of(publisherDouble);

        // Act
        List<US028PublisherDTO> result = US028PublisherMapper.toDTOs(publishers);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Porto Editora", result.get(0).name());
        assertEquals(publisherId, result.get(0).publisherId());
    }

    @Test
    void toDTOs_ShouldReturnListWithMultipleDTOs_WhenListHasMultiplePublishers() {
        // Arrange
        PublisherID publisherId1 = mock(PublisherID.class);
        PublisherID publisherId2 = mock(PublisherID.class);
        PublisherID publisherId3 = mock(PublisherID.class);

        Publisher publisher1 = createPublisherDouble("Publisher 1", publisherId1);
        Publisher publisher2 = createPublisherDouble("Publisher 2", publisherId2);
        Publisher publisher3 = createPublisherDouble("Publisher 3", publisherId3);

        List<Publisher> publishers = List.of(publisher1, publisher2, publisher3);

        // Act
        List<US028PublisherDTO> result = US028PublisherMapper.toDTOs(publishers);

        // Assert
        assertEquals(3, result.size());

        assertEquals("Publisher 1", result.get(0).name());
        assertEquals(publisherId1, result.get(0).publisherId());

        assertEquals("Publisher 2", result.get(1).name());
        assertEquals(publisherId2, result.get(1).publisherId());

        assertEquals("Publisher 3", result.get(2).name());
        assertEquals(publisherId3, result.get(2).publisherId());
    }

    @Test
    void toDTOs_ShouldThrowIllegalArgumentException_WhenInputListContainsNullPublisher() {
        // Arrange
        List<Publisher> publishers = new ArrayList<>();
        publishers.add(null);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> US028PublisherMapper.toDTOs(publishers));

        assertEquals("Publisher cannot be null", ex.getMessage());
    }

    // -------------------------
    // Helpers
    // -------------------------

    private Publisher createPublisherDouble(String name, PublisherID publisherId) {
        Publisher publisherDouble = mock(Publisher.class);

        when(publisherDouble.getName()).thenReturn(name);
        when(publisherDouble.id()).thenReturn(publisherId);

        return publisherDouble;
    }
}