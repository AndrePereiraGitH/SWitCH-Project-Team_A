package TOPSECRET.dto;

import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class US018CollectionOfPublicationsDTOTest {

    @Test
    void shouldCreateDTOSuccessfully() {

        // ARRANGE
        CollectionOfPublicationsID id = mock(CollectionOfPublicationsID.class);

        // ACT
        US018CollectionOfPublicationsDTO dto =
                new US018CollectionOfPublicationsDTO(id, "My Collection");

        // ASSERT
        assertEquals(id, dto.id());
        assertEquals("My Collection", dto.collectionName());
    }

    @Test
    void shouldThrowExceptionWhenIdIsNull() {

        // ARRANGE

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> new US018CollectionOfPublicationsDTO(null, "Name"));
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {

        // ARRANGE
        CollectionOfPublicationsID id = mock(CollectionOfPublicationsID.class);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> new US018CollectionOfPublicationsDTO(id, null));
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {

        // ARRANGE
        CollectionOfPublicationsID id = mock(CollectionOfPublicationsID.class);

        // ACT + ASSERT
        assertThrows(IllegalArgumentException.class,
                () -> new US018CollectionOfPublicationsDTO(id, " "));
    }
}