package TOPSECRET.dto;

import TOPSECRET.domain.ValueObjects.PublisherID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link US028PublisherDTO}.
 */
class US028PublisherDTOTest {

    // ===============================================
    // ================= Set Up ======================
    // ===============================================

    private String name;
    private PublisherID publisherId;

    @BeforeEach
    void setUp() {
        name = "publisher name";
        publisherId = mock(PublisherID.class);
    }

    // ===============================================
    // =============== Constructor ====================
    // ===============================================

    @Test
    void constructor_ShouldStoreValuesCorrectly() {
        // Act
        US028PublisherDTO dto = new US028PublisherDTO(name, publisherId);

        // Assert
        assertEquals(name, dto.name());
        assertEquals(publisherId, dto.publisherId());
    }

    @Test
    void constructor_ShouldThrowWhenNameIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new US028PublisherDTO(null, publisherId));
    }

    @Test
    void constructor_ShouldThrowWhenNameIsBlank() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new US028PublisherDTO("   ", publisherId));
    }

    @Test
    void constructor_ShouldThrowWhenPublisherIdIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new US028PublisherDTO(name, null));
    }

    // ===============================================
    // =============== Record behaviour ===============
    // ===============================================

    @Test
    void shouldBeEqual_whenDTOsHaveSameValues() {
        // Arrange
        US028PublisherDTO dto1 = new US028PublisherDTO(name, publisherId);
        US028PublisherDTO dto2 = new US028PublisherDTO(name, publisherId);

        // Act & Assert
        assertEquals(dto1, dto2);
    }

    @Test
    void shouldNotBeEqual_whenDTOsHaveDifferentValues() {
        // Arrange
        PublisherID otherId = mock(PublisherID.class);

        US028PublisherDTO dto1 = new US028PublisherDTO(name, publisherId);
        US028PublisherDTO dto2 = new US028PublisherDTO("other", otherId);

        // Act & Assert
        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldHaveSameHashCode_whenDTOsAreEqual() {
        // Arrange
        US028PublisherDTO dto1 = new US028PublisherDTO(name, publisherId);
        US028PublisherDTO dto2 = new US028PublisherDTO(name, publisherId);

        // Act
        int hash1 = dto1.hashCode();
        int hash2 = dto2.hashCode();

        // Assert
        assertEquals(hash1, hash2);
    }

    @Test
    void shouldReturnMeaningfulToString() {
        // Arrange
        US028PublisherDTO dto = new US028PublisherDTO(name, publisherId);

        // Act
        String result = dto.toString();

        // Assert
        assertTrue(result.contains(name));
        assertTrue(result.contains("publisherId"));
    }
}