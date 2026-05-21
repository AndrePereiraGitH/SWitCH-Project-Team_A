package TOPSECRET.dto.publisher.response;

import TOPSECRET.domain.ValueObjects.PublisherID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.RepresentationModel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PublisherDTO}.
 */
class PublisherDTOTest {

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
        PublisherDTO dto = new PublisherDTO(name, publisherId);

        // Assert
        assertEquals(name, dto.getName());
        assertEquals(publisherId, dto.getPublisherId());
    }

    @Test
    void constructor_ShouldThrowWhenNameIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> new PublisherDTO(null, publisherId));
    }

    @Test
    void constructor_ShouldThrowWhenPublisherIdIsNull() {
        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> new PublisherDTO(name, null));
    }

    // ===============================================
    // ============= RepresentationModel =============
    // ===============================================

    @Test
    void shouldBeInstanceOfRepresentationModel() {
        // Act
        PublisherDTO dto = new PublisherDTO(name, publisherId);

        // Assert
        assertInstanceOf(RepresentationModel.class, dto);
    }

    // ===============================================
    // =============== Record behaviour ===============
    // ===============================================

    @Test
    void shouldBeEqual_whenDTOsHaveSameValues() {
        // Arrange
        PublisherDTO dto1 = new PublisherDTO(name, publisherId);
        PublisherDTO dto2 = new PublisherDTO(name, publisherId);

        // Act & Assert
        assertEquals(dto1, dto2);
    }

    @Test
    void shouldNotBeEqual_whenDTOsHaveDifferentValues() {
        // Arrange
        PublisherID otherId = mock(PublisherID.class);

        PublisherDTO dto1 = new PublisherDTO(name, publisherId);
        PublisherDTO dto2 = new PublisherDTO("other", otherId);

        // Act & Assert
        assertNotEquals(dto1, dto2);
    }

    @Test
    void shouldHaveSameHashCode_whenDTOsAreEqual() {
        // Arrange
        PublisherDTO dto1 = new PublisherDTO(name, publisherId);
        PublisherDTO dto2 = new PublisherDTO(name, publisherId);

        // Act
        int hash1 = dto1.hashCode();
        int hash2 = dto2.hashCode();

        // Assert
        assertEquals(hash1, hash2);
    }

    @Test
    void shouldReturnMeaningfulToString() {
        // Arrange
        PublisherDTO dto = new PublisherDTO(name, publisherId);

        // Act
        String result = dto.toString();

        // Assert
        assertNotNull(result);
        assertFalse(result.isBlank());
        assertTrue(result.contains(name));
        assertTrue(result.contains("publisherId"));
    }
}