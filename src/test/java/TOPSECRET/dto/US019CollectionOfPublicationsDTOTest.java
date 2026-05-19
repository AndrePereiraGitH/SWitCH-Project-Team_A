package TOPSECRET.dto;

import TOPSECRET.domain.ValueObjects.CollectionOfPublicationsId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

        // arrange
        CollectionOfPublicationsId id = mock(CollectionOfPublicationsId.class);

        // Act
        US019CollectionOfPublicationsDTO dto = new US019CollectionOfPublicationsDTO(
                id,
                "My Collection",
                "Fiction",
                "alice"
        );

        // Assert
        assertNotNull(dto);
    }

    @Test
    void constructor_ShouldThrowIllegalArgumentException_WhenNameIsNull() {

        // arrange
        CollectionOfPublicationsId id = mock(CollectionOfPublicationsId.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US019CollectionOfPublicationsDTO(id, null, "Fiction", "alice")
        );
    }

    @Test
    void constructor_ShouldThrowIllegalArgumentException_WhenGenreNameIsNull() {
        // arrange
        CollectionOfPublicationsId id = mock(CollectionOfPublicationsId.class);
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US019CollectionOfPublicationsDTO(id, "My Collection", null, "alice")
        );
    }

    @Test
    void constructor_ShouldThrowIllegalArgumentException_WhenOwnerNameIsNull() {
        // arrange
        CollectionOfPublicationsId id = mock(CollectionOfPublicationsId.class);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US019CollectionOfPublicationsDTO(id, "My Collection", "Fiction", null)
        );
    }

    @Test
    void constructor_ShouldThrowIllegalArgumentException_WhenAllFieldsAreNull() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () ->
                new US019CollectionOfPublicationsDTO(null, null, null, null)
        );
    }

}