package TOPSECRET.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link CollectionOfPublicationsDTO_US020}.
 * <p>
 * These tests verify that the DTO correctly handles valid input,
 * trims whitespace from fields, and throws exceptions when invalid
 * values are provided.
 */

class CollectionOfPublicationsDTO_US020Test {

    /**
     * Verifies that the DTO is created successfully when valid
     * list name and owner username are provided.
     * Also confirms that the stored values match the input.
     */

    @Test
    void shouldCreateDTOWithValidData()
    {
        CollectionOfPublicationsDTO_US020 dto =
                new CollectionOfPublicationsDTO_US020("List Name", "Raul");

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
        CollectionOfPublicationsDTO_US020 dto =
                new CollectionOfPublicationsDTO_US020("  List Name  ", "  Raul  ");

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
                new CollectionOfPublicationsDTO_US020(null, "Raul")
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
                new CollectionOfPublicationsDTO_US020("   ", "Raul")
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
                new CollectionOfPublicationsDTO_US020("List Name", null)
        );
    }

    /**
     * Verifies that an {@link IllegalArgumentException} is thrown
     * when the owner username is blank or contains only whitespace.
     */
    @Test
    void shouldThrowWhenOwnerUsernameIsBlank() {
        assertThrows(IllegalArgumentException.class, () ->
                new CollectionOfPublicationsDTO_US020("List Name", "   ")
        );
    }
}
