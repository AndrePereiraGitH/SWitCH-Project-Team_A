package TOPSECRET.dto;

import TOPSECRET.domain.PublicationType.PublicationType;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class US014PublicationTypeMapperTest {



    @Test
    void toDTOValidPublicationType() {
        // Arrange
        PublicationType pt = mock(PublicationType.class);
        PublicationTypeId publicationTypeId = mock(PublicationTypeId.class);
        when(pt.getMediaFormat()).thenReturn("Book");
        when(publicationTypeId.getId()).thenReturn("1");
        when(pt.id()).thenReturn(publicationTypeId);

        US014PublicationTypeMapper mapper = new US014PublicationTypeMapper();

        // Act
        US014PublicationTypeDTO dto =
                mapper.toDTO(pt);

        // Assert
        assertEquals("Book", dto.mediaFormat());
        assertEquals("1", dto.id());
    }

    @Test
    void toDTOThrowsWhenNull() {

        // Arrange
        US014PublicationTypeMapper mapper = new US014PublicationTypeMapper();


        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                mapper.toDTO(null));
    }

    @Test
    void toDTOListThrowsWhenNullList() {

        // Arrange
        US014PublicationTypeMapper mapper = new US014PublicationTypeMapper();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                mapper.toDTOList(null));
    }

    @Test
    void toDTOListEmptyListReturnsEmptyList() {

        // Arrange
        US014PublicationTypeMapper mapper = new US014PublicationTypeMapper();

        // Act
        List<US014PublicationTypeDTO> result =
                mapper.toDTOList(List.of());

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void toDTOListOneElement() {
        // Arrange
        PublicationType pt = mock(PublicationType.class);
        PublicationTypeId publicationTypeId = mock(PublicationTypeId.class);
        when(pt.getMediaFormat()).thenReturn("Book");
        when(publicationTypeId.getId()).thenReturn("1");
        when(pt.id()).thenReturn(publicationTypeId);

        US014PublicationTypeMapper mapper = new US014PublicationTypeMapper();


        // Act
        List<US014PublicationTypeDTO> result =
                mapper.toDTOList(List.of(pt));

        // Assert
        assertEquals(1, result.size());
        assertEquals("Book", result.get(0).mediaFormat());
        assertEquals("1", result.get(0).id());
    }

    @Test
    void toDTOListMultipleElements() {

        // Arrange
        PublicationType pt1 = mock(PublicationType.class);
        PublicationType pt2 = mock(PublicationType.class);

        PublicationTypeId publicationTypeId1 = mock(PublicationTypeId.class);
        PublicationTypeId publicationTypeId2 = mock(PublicationTypeId.class);

        when(pt1.getMediaFormat()).thenReturn("Book");
        when(publicationTypeId1.getId()).thenReturn("1");
        when(pt1.id()).thenReturn(publicationTypeId1);

        when(pt2.getMediaFormat()).thenReturn("Magazine");
        when(publicationTypeId2.getId()).thenReturn("2");
        when(pt2.id()).thenReturn(publicationTypeId2);

        US014PublicationTypeMapper mapper = new US014PublicationTypeMapper();

        // Act
        List<US014PublicationTypeDTO> result =
                mapper.toDTOList(List.of(pt1, pt2));

        // Assert
        assertEquals(2, result.size());
        assertEquals("Book", result.get(0).mediaFormat());
        assertEquals("1", result.get(0).id());
        assertEquals("Magazine", result.get(1).mediaFormat());
        assertEquals("2", result.get(1).id());
    }
}
