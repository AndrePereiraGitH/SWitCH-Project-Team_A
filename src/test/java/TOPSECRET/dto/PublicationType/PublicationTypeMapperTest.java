package TOPSECRET.dto.PublicationType;

import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import TOPSECRET.domain.ValueObjects.UserId;
import TOPSECRET.dto.PublicationType.response.PublicationTypeResponseDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link PublicationTypeMapper}.
 *
 * <p>
 * Verifies the correct conversion of {@link PublicationType}
 * domain entities into {@link PublicationTypeResponseDTO}
 * objects, including single object mapping and list mapping.
 * </p>
 *
 * <p>
 * Also validates exceptional scenarios such as null inputs
 * and empty collections.
 * </p>
 */
class PublicationTypeMapperTest {

    @Test
    void toDTOValidPublicationType() {

        // Arrange
        PublicationType publicationType = mock(PublicationType.class);
        PublicationTypeId publicationTypeId = mock(PublicationTypeId.class);

        when(publicationType.id()).thenReturn(publicationTypeId);
        when(publicationTypeId.getId()).thenReturn("1");
        when(publicationType.getMediaFormat()).thenReturn("Book");

        // Act
        PublicationTypeResponseDTO dto = PublicationTypeMapper.toDTO(publicationType);

        // Assert
        assertNotNull(dto);
        assertEquals("1", dto.getId());
        assertEquals("Book", dto.getMediaFormat());
    }

    @Test
    void toDTOThrowsWhenPublicationTypeIsNull() {

        // Act + Assert
        assertThrows(
                NullPointerException.class,
                () -> PublicationTypeMapper.toDTO(null)
        );
    }

    @Test
    void toDTOListThrowsWhenListIsNull() {

        // Act + Assert
        assertThrows(
                NullPointerException.class,
                () -> PublicationTypeMapper.toDTOList(null)
        );
    }

    @Test
    void toDTOListReturnsEmptyList() {
        // Act
        List<PublicationTypeResponseDTO> result =
                PublicationTypeMapper.toDTOList(List.of());

        // Assert
        assertNotNull(result);

        assertTrue(result.isEmpty());
    }

    @Test
    void toDTOListOneElement() {

        // Arrange
        PublicationType publicationType = mock(PublicationType.class);

        PublicationTypeId publicationTypeId = mock(PublicationTypeId.class);

        when(publicationType.id()).thenReturn(publicationTypeId);
        when(publicationTypeId.getId()).thenReturn("1");
        when(publicationType.getMediaFormat()).thenReturn("Book");

        // Act
        List<PublicationTypeResponseDTO> result =
                PublicationTypeMapper.toDTOList(
                        List.of(publicationType)
                );

        // Assert
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("Book", result.get(0).getMediaFormat());
    }

    @Test
    void toDTOListMultipleElements() {

        // Arrange
        PublicationType pt1 = mock(PublicationType.class);
        PublicationType pt2 = mock(PublicationType.class);

        PublicationTypeId id1 = mock(PublicationTypeId.class);
        PublicationTypeId id2 = mock(PublicationTypeId.class);

        when(pt1.id()).thenReturn(id1);
        when(id1.getId()).thenReturn("1");
        when(pt1.getMediaFormat()).thenReturn("Book");

        when(pt2.id()).thenReturn(id2);
        when(id2.getId()).thenReturn("2");
        when(pt2.getMediaFormat()).thenReturn("Magazine");

        // Act
        List<PublicationTypeResponseDTO> result =
                PublicationTypeMapper.toDTOList(List.of(pt1, pt2));

        // Assert
        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("Book", result.get(0).getMediaFormat());
        assertEquals("2", result.get(1).getId());
        assertEquals("Magazine", result.get(1).getMediaFormat());
    }

    // =========================================================
    // toUserId — SUCCESS
    // =========================================================

    @Test
    void shouldReturnUserIdWhenValidStringIsProvided() {

        // ARRANGE
        String rawUserId = "user-001";

        // ACT
        UserId result = PublicationTypeMapper.toUserId(rawUserId);

        // ASSERT
        assertNotNull(result);
    }

    @Test
    void shouldReturnUserIdWithMatchingValueWhenValidStringIsProvided() {

        // ARRANGE
        String rawUserId = "user-001";

        // ACT
        UserId result = PublicationTypeMapper.toUserId(rawUserId);

        // ASSERT
        assertEquals(rawUserId, result.getUserId());
    }

    // =========================================================
    // toUserId — INVALID INPUT
    // =========================================================

    @Test
    void shouldThrowWhenUserIdIsNull() {

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> PublicationTypeMapper.toUserId(null));
    }

    @Test
    void shouldThrowWhenUserIdIsBlank() {

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> PublicationTypeMapper.toUserId("   "));
    }

    @Test
    void shouldThrowWhenUserIdIsEmpty() {

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> PublicationTypeMapper.toUserId(""));
    }

    // =========================================================
    // toPublicationTypeId — SUCCESS
    // =========================================================

    @Test
    void shouldReturnPublicationTypeIdWhenValidStringIsProvided() {

        // ARRANGE
        String rawId = "pt-001";

        // ACT
        PublicationTypeId result = PublicationTypeMapper.toPublicationTypeId(rawId);

        // ASSERT
        assertNotNull(result);
    }

    @Test
    void shouldReturnPublicationTypeIdWithMatchingValueWhenValidStringIsProvided() {

        // ARRANGE
        String rawId = "pt-001";

        // ACT
        PublicationTypeId result = PublicationTypeMapper.toPublicationTypeId(rawId);

        // ASSERT
        assertEquals(rawId, result.getId());
    }

    // =========================================================
    // toPublicationTypeId — INVALID INPUT
    // =========================================================

    @Test
    void shouldThrowWhenPublicationTypeIdIsNull() {

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> PublicationTypeMapper.toPublicationTypeId(null));
    }

    @Test
    void shouldThrowWhenPublicationTypeIdIsBlank() {

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> PublicationTypeMapper.toPublicationTypeId("   "));
    }

    @Test
    void shouldThrowWhenPublicationTypeIdIsEmpty() {

        // ACT & ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> PublicationTypeMapper.toPublicationTypeId(""));
    }
}