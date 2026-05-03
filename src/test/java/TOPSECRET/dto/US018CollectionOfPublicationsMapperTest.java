package TOPSECRET.dto;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class US018CollectionOfPublicationsMapperTest {

    @Test
    void shouldConvertListToDTO() {

        // ARRANGE
        CollectionOfPublications collection = mock(CollectionOfPublications.class);
        CollectionOfPublicationsId id = mock(CollectionOfPublicationsId.class);

        when(collection.id()).thenReturn(id);
        when(collection.getCollectionName()).thenReturn("Collection");

        // ACT
        List<US018CollectionOfPublicationsDTO> result =
                US018CollectionOfPublicationsMapper.toDTOs(List.of(collection));

        // ASSERT
        assertEquals(1, result.size());
        assertEquals(id, result.get(0).id());
        assertEquals("Collection", result.get(0).collectionName());
    }

    @Test
    void shouldReturnEmptyListWhenInputIsNull() {

        // ACT
        List<US018CollectionOfPublicationsDTO> result =
                US018CollectionOfPublicationsMapper.toDTOs(null);

        // ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenSingleCollectionIsNull() {

        // ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> US018CollectionOfPublicationsMapper.toDTO(null));
    }

    @Test
    void shouldReturnMutableListWhenInputIsNull() {

        // ACT
        List<US018CollectionOfPublicationsDTO> result =
                US018CollectionOfPublicationsMapper.toDTOs(null);

        // ASSERT
        assertNotNull(result);
        assertTrue(result.isEmpty());

        assertDoesNotThrow(() -> result.add(mock(US018CollectionOfPublicationsDTO.class)));
    }


}