package TOPSECRET.dto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link US020CollectionOfPublicationsDTOMapper}.
 * <p>
 * These tests verify that the DTO mapper correctly handles valid input,
 * trims whitespace from fields, and throws exceptions when invalid
 * values are provided.
 */


class US020CollectionOfPublicationsDTOMapperTest {

    /**
     * Verifies that the DTO is created successfully when valid
     * list name and owner username are provided.
     * Also confirms that the stored values match the input.
     */
    @Test
    void shouldCreateDTOWithValidData()
    {
        // Arrange
        String collectionName = "Collection name";
        String userName = "Antonio";

        // Act
        US020CollectionOfPublicationsDTO dto =
                US020CollectionOfPublicationsDTOMapper.toDTO(collectionName, userName);

        // Assert
        assertNotNull(dto);
        assertEquals(collectionName, dto.collectionName());
        assertEquals(userName, dto.userName());
    }

    /**
     * Ensures that leading and trailing whitespace in the constructor
     * parameters is automatically trimmed before storing the values.
     */
    @Test
    void shouldTrimInputValues()
    {
        US020CollectionOfPublicationsDTO dto =
                US020CollectionOfPublicationsDTOMapper.toDTO("  List Name  ", "  Raul  ");

        assertEquals("List Name", dto.collectionName());
        assertEquals("Raul", dto.userName());
    }

    /**
     * Verifies that an {@link IllegalArgumentException} is thrown
     * when the list name is {@code null}.
     */
    @Test
    void shouldThrowWhenListNameIsNull()
    {
        assertThrows(IllegalArgumentException.class, () ->
                US020CollectionOfPublicationsDTOMapper.toDTO(null, "Raul")
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
                US020CollectionOfPublicationsDTOMapper.toDTO("   ", "Raul")
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
                US020CollectionOfPublicationsDTOMapper.toDTO("List Name", null)
        );
    }

    /**
     * Verifies that an {@link IllegalArgumentException} is thrown
     * when the owner username is blank or contains only whitespace.
     */
    @Test
    void shouldThrowWhenOwnerUsernameIsBlank() {
        assertThrows(IllegalArgumentException.class, () ->
                US020CollectionOfPublicationsDTOMapper.toDTO("List Name", "   ")
        );
    }

}