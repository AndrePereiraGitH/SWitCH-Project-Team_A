package TOPSECRET.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


class CollectionOfPublicationsDTOTest {

    @Test
    void shouldCreateDTOWithValidData()
    {
        CollectionOfPublicationsDTO dto =
                new CollectionOfPublicationsDTO("List Name", "Raul");

        assertNotNull(dto);
        assertEquals("List Name", dto.getListName());
        assertEquals("Raul", dto.getOwnerUsername());
    }

    @Test
    void shouldTrimInputValues()
    {
        CollectionOfPublicationsDTO dto =
                new CollectionOfPublicationsDTO("  List Name  ", "  Raul  ");

        assertEquals("List Name", dto.getListName());
        assertEquals("Raul", dto.getOwnerUsername());
    }

    @Test
    void shouldThrowWhenListNameIsNull()
    {
        assertThrows(IllegalArgumentException.class, () ->
                new CollectionOfPublicationsDTO(null, "Raul")
        );
    }

    @Test
    void shouldThrowWhenListNameIsBlank()
    {
        assertThrows(IllegalArgumentException.class, () ->
                new CollectionOfPublicationsDTO("   ", "Raul")
        );
    }

    @Test
    void shouldThrowWhenOwnerUsernameIsNull()
    {
        assertThrows(IllegalArgumentException.class, () ->
                new CollectionOfPublicationsDTO("List Name", null)
        );
    }

    @Test
    void shouldThrowWhenOwnerUsernameIsBlank()
    {
        assertThrows(IllegalArgumentException.class, () ->
                new CollectionOfPublicationsDTO("List Name", "   ")
        );
    }
}
