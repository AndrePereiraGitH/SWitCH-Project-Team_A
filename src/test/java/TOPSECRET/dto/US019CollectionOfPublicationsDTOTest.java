package TOPSECRET.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link US019CollectionOfPublicationsDTO}.
 *
 * <p>Verifies the correct behaviour of the DTO record:
 * <ul>
 *   <li>Object creation with valid data</li>
 *   <li>Validation of null constraints on all fields</li>
 *   <li>Correct retrieval of field values via accessor methods</li>
 * </ul>
 */
class US019CollectionOfPublicationsDTOTest {

    // -------------------------
    // Constructor
    // -------------------------

    @Test
    void constructor_ShouldCreateDTO_WhenAllFieldsAreValid() {
        // Act
        US019CollectionOfPublicationsDTO dto = new US019CollectionOfPublicationsDTO(
                "My Collection",
                "Fiction",
                "alice"
        );

        // Assert
        assertNotNull(dto);
    }

    @Test
    void constructor_ShouldThrowIllegalArgumentException_WhenNameIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US019CollectionOfPublicationsDTO(null, "Fiction", "alice")
        );
    }

    @Test
    void constructor_ShouldThrowIllegalArgumentException_WhenGenreNameIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US019CollectionOfPublicationsDTO("My Collection", null, "alice")
        );
    }

    @Test
    void constructor_ShouldThrowIllegalArgumentException_WhenOwnerNameIsNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US019CollectionOfPublicationsDTO("My Collection", "Fiction", null)
        );
    }

    @Test
    void constructor_ShouldThrowIllegalArgumentException_WhenAllFieldsAreNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US019CollectionOfPublicationsDTO(null, null, null)
        );
    }

    // -------------------------
    // Accessors (name)
    // -------------------------

    @Test
    void name_ShouldReturnCorrectValue_WhenDTOIsCreated() {
        // Arrange
        US019CollectionOfPublicationsDTO dto = new US019CollectionOfPublicationsDTO(
                "My Collection",
                "Fiction",
                "alice"
        );

        // Act
        String result = dto.name();

        // Assert
        assertEquals("My Collection", result);
    }

    @Test
    void name_ShouldReturnEmptyString_WhenNameIsEmpty() {
        // Arrange
        US019CollectionOfPublicationsDTO dto = new US019CollectionOfPublicationsDTO(
                "",
                "Fiction",
                "alice"
        );

        // Act
        String result = dto.name();

        // Assert
        assertEquals("", result);
    }

    @Test
    void name_ShouldReturnStringWithSpaces_WhenNameHasSpaces() {
        // Arrange
        US019CollectionOfPublicationsDTO dto = new US019CollectionOfPublicationsDTO(
                "  My Collection  ",
                "Fiction",
                "alice"
        );

        // Act
        String result = dto.name();

        // Assert
        assertEquals("  My Collection  ", result);
    }

    // -------------------------
    // Accessors (genreName)
    // -------------------------

    @Test
    void genreName_ShouldReturnCorrectValue_WhenDTOIsCreated() {
        // Arrange
        US019CollectionOfPublicationsDTO dto = new US019CollectionOfPublicationsDTO(
                "My Collection",
                "Fiction",
                "alice"
        );

        // Act
        String result = dto.genreName();

        // Assert
        assertEquals("Fiction", result);
    }

    @Test
    void genreName_ShouldReturnEmptyString_WhenGenreNameIsEmpty() {
        // Arrange
        US019CollectionOfPublicationsDTO dto = new US019CollectionOfPublicationsDTO(
                "My Collection",
                "",
                "alice"
        );

        // Act
        String result = dto.genreName();

        // Assert
        assertEquals("", result);
    }

    // -------------------------
    // Accessors (ownerName)
    // -------------------------

    @Test
    void ownerName_ShouldReturnCorrectValue_WhenDTOIsCreated() {
        // Arrange
        US019CollectionOfPublicationsDTO dto = new US019CollectionOfPublicationsDTO(
                "My Collection",
                "Fiction",
                "alice"
        );

        // Act
        String result = dto.ownerName();

        // Assert
        assertEquals("alice", result);
    }

    @Test
    void ownerName_ShouldReturnEmptyString_WhenOwnerNameIsEmpty() {
        // Arrange
        US019CollectionOfPublicationsDTO dto = new US019CollectionOfPublicationsDTO(
                "My Collection",
                "Fiction",
                ""
        );

        // Act
        String result = dto.ownerName();

        // Assert
        assertEquals("", result);
    }

    // -------------------------
    // equals and hashCode
    // -------------------------

    @Test
    void equals_ShouldReturnTrue_WhenAllFieldsAreEqual() {
        // Arrange
        US019CollectionOfPublicationsDTO dto1 = new US019CollectionOfPublicationsDTO(
                "My Collection",
                "Fiction",
                "alice"
        );
        US019CollectionOfPublicationsDTO dto2 = new US019CollectionOfPublicationsDTO(
                "My Collection",
                "Fiction",
                "alice"
        );

        // Act + Assert
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void equals_ShouldReturnFalse_WhenNameIsDifferent() {
        // Arrange
        US019CollectionOfPublicationsDTO dto1 = new US019CollectionOfPublicationsDTO(
                "My Collection",
                "Fiction",
                "alice"
        );
        US019CollectionOfPublicationsDTO dto2 = new US019CollectionOfPublicationsDTO(
                "Other Collection",
                "Fiction",
                "alice"
        );

        // Act + Assert
        assertNotEquals(dto1, dto2);
    }

    @Test
    void equals_ShouldReturnFalse_WhenGenreNameIsDifferent() {
        // Arrange
        US019CollectionOfPublicationsDTO dto1 = new US019CollectionOfPublicationsDTO(
                "My Collection",
                "Fiction",
                "alice"
        );
        US019CollectionOfPublicationsDTO dto2 = new US019CollectionOfPublicationsDTO(
                "My Collection",
                "Mystery",
                "alice"
        );

        // Act + Assert
        assertNotEquals(dto1, dto2);
    }

    @Test
    void equals_ShouldReturnFalse_WhenOwnerNameIsDifferent() {
        // Arrange
        US019CollectionOfPublicationsDTO dto1 = new US019CollectionOfPublicationsDTO(
                "My Collection",
                "Fiction",
                "alice"
        );
        US019CollectionOfPublicationsDTO dto2 = new US019CollectionOfPublicationsDTO(
                "My Collection",
                "Fiction",
                "bob"
        );

        // Act + Assert
        assertNotEquals(dto1, dto2);
    }

    @Test
    void equals_ShouldReturnFalse_WhenComparedWithNull() {
        // Arrange
        US019CollectionOfPublicationsDTO dto = new US019CollectionOfPublicationsDTO(
                "My Collection",
                "Fiction",
                "alice"
        );

        // Act + Assert
        assertNotEquals(null, dto);
    }

    @Test
    void equals_ShouldReturnFalse_WhenComparedWithDifferentType() {
        // Arrange
        US019CollectionOfPublicationsDTO dto = new US019CollectionOfPublicationsDTO(
                "My Collection",
                "Fiction",
                "alice"
        );

        // Act + Assert
        assertNotEquals("some string", dto);
    }

    // -------------------------
    // toString
    // -------------------------

    @Test
    void toString_ShouldContainAllFieldValues() {
        // Arrange
        US019CollectionOfPublicationsDTO dto = new US019CollectionOfPublicationsDTO(
                "My Collection",
                "Fiction",
                "alice"
        );

        // Act
        String result = dto.toString();

        // Assert
        assertTrue(result.contains("My Collection"));
        assertTrue(result.contains("Fiction"));
        assertTrue(result.contains("alice"));
    }
}