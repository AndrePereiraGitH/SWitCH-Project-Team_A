package TOPSECRET.dto.collectionOfPublications.response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ResponseCollectionOfPublicationsDTO}.
 *
 * <p>Verifies correct storage, equality, hash code, and string
 * representation of the response DTO class.</p>
 */
class ResponseCollectionOfPublicationsDTOTest {

    private String collectionName;
    private String collectionId;

    @BeforeEach
    void setUp() {
        collectionName = "My Collection";
        collectionId = "collection-123";
    }

    @Test
    void dtoShouldStoreValuesCorrectly() {
        // ARRANGE
        ResponseCollectionOfPublicationsDTO dto = new ResponseCollectionOfPublicationsDTO(
                collectionName, collectionId);

        // ACT
        String actualCollectionName = dto.getCollectionName();
        String actualCollectionId = dto.getCollectionId();

        // ASSERT
        assertEquals(collectionName, actualCollectionName);
        assertEquals(collectionId, actualCollectionId);
    }

    @Test
    void dtoShouldBeEqualWhenValuesAreSame() {
        // ARRANGE
        ResponseCollectionOfPublicationsDTO dto1 = new ResponseCollectionOfPublicationsDTO(
                collectionName, collectionId);
        ResponseCollectionOfPublicationsDTO dto2 = new ResponseCollectionOfPublicationsDTO(
                collectionName, collectionId);

        // ACT
        boolean result = dto1.equals(dto2);

        // ASSERT
        assertTrue(result);
    }

    @Test
    void dtoShouldNotBeEqualWhenValuesAreDifferent() {
        // ARRANGE
        ResponseCollectionOfPublicationsDTO dto1 = new ResponseCollectionOfPublicationsDTO(
                collectionName, collectionId);
        ResponseCollectionOfPublicationsDTO dto2 = new ResponseCollectionOfPublicationsDTO(
                "Other Collection", collectionId);

        // ACT
        boolean result = dto1.equals(dto2);

        // ASSERT
        assertFalse(result);
    }

    @Test
    void dtoShouldHaveConsistentHashCode() {
        // ARRANGE
        ResponseCollectionOfPublicationsDTO dto1 = new ResponseCollectionOfPublicationsDTO(
                collectionName, collectionId);
        ResponseCollectionOfPublicationsDTO dto2 = new ResponseCollectionOfPublicationsDTO(
                collectionName, collectionId);

        // ACT
        int hashCode1 = dto1.hashCode();
        int hashCode2 = dto2.hashCode();

        // ASSERT
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void toStringShouldContainValues() {
        // ARRANGE
        ResponseCollectionOfPublicationsDTO dto = new ResponseCollectionOfPublicationsDTO(
                collectionName, collectionId);

        // ACT
        String result = dto.toString();

        // ASSERT
        assertTrue(result.contains(collectionName));
        assertTrue(result.contains(collectionId));
    }
}