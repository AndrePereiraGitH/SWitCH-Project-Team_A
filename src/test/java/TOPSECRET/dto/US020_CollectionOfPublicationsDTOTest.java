package TOPSECRET.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link US020_CollectionOfPublicationsDTO}.
 * <p>
 * These tests verify that the DTO correctly handles valid input,
 * trims whitespace from fields, and throws exceptions when invalid
 * values are provided.
 */

class US020_CollectionOfPublicationsDTOTest {

    /**
     * Verifies that the DTO is created successfully when valid
     * list name and owner username are provided.
     * Also confirms that the stored values match the input.
     */

    @Test
    void shouldCreateDTOWithValidData()
    {
        US020_CollectionOfPublicationsDTO dto =
                new US020_CollectionOfPublicationsDTO("List Name", "Raul");

        assertNotNull(dto);
        assertEquals("List Name", dto.getListName());
        assertEquals("Raul", dto.getOwnerUsername());
    }

    /**
     * Ensures that leading and trailing whitespace in the constructor
     * parameters is automatically trimmed before storing the values.
     */
    @Test
    void shouldTrimInputValues()
    {
        US020_CollectionOfPublicationsDTO dto =
                new US020_CollectionOfPublicationsDTO("  List Name  ", "  Raul  ");

        assertEquals("List Name", dto.getListName());
        assertEquals("Raul", dto.getOwnerUsername());
    }

    /**
     * Verifies that an {@link IllegalArgumentException} is thrown
     * when the list name is {@code null}.
     */
    @Test
    void shouldThrowWhenListNameIsNull()
    {
        assertThrows(IllegalArgumentException.class, () ->
                new US020_CollectionOfPublicationsDTO(null, "Raul")
        );
    }

    /**
     * Verifies that an {@link IllegalArgumentException} is thrown
     * when the list name is blank or contains only whitespace.
     */
    @Test
    void shouldThrowWhenListNameIsBlank()
    {
        assertThrows(IllegalArgumentException.class, () ->
                new US020_CollectionOfPublicationsDTO("   ", "Raul")
        );
    }

    /**
     * Verifies that an {@link IllegalArgumentException} is thrown
     * when the owner username is {@code null}.
     */
    @Test
    void shouldThrowWhenOwnerUsernameIsNull()
    {
        assertThrows(IllegalArgumentException.class, () ->
                new US020_CollectionOfPublicationsDTO("List Name", null)
        );
    }

    /**
     * Verifies that an {@link IllegalArgumentException} is thrown
     * when the owner username is blank or contains only whitespace.
     */
    @Test
    void shouldThrowWhenOwnerUsernameIsBlank() {
        assertThrows(IllegalArgumentException.class, () ->
                new US020_CollectionOfPublicationsDTO("List Name", "   ")
        );
    }
}
