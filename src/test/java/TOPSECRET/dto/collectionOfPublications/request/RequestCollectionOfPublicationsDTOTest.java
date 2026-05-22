package TOPSECRET.dto.collectionOfPublications.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestCollectionOfPublicationsDTOTest {

    private String collectionName;
    private String genreId;
    private String userId;
    private Boolean isPrivate;

    @BeforeEach
    void setUp() {
        collectionName = "My Collection";
        genreId = "Science Fiction";
        userId = "user123";
        isPrivate = true;
    }

    @Test
    void recordShouldStoreValuesCorrectly() {
        // ARRANGE
        RequestCollectionOfPublicationsDTO dto = new RequestCollectionOfPublicationsDTO(
                collectionName, genreId, userId, isPrivate);

        // ACT
        String actualCollectionName = dto.collectionName();
        String actualGenreId = dto.genreId();
        String actualUserId = dto.userId();
        Boolean actualIsPrivate = dto.isPrivate();

        // ASSERT
        assertEquals(collectionName, actualCollectionName);
        assertEquals(genreId, actualGenreId);
        assertEquals(userId, actualUserId);
        assertEquals(isPrivate, actualIsPrivate);
    }

    @Test
    void recordShouldBeEqualWhenValuesAreSame() {
        // ARRANGE
        RequestCollectionOfPublicationsDTO dto1 = new RequestCollectionOfPublicationsDTO(
                collectionName, genreId, userId, isPrivate);
        RequestCollectionOfPublicationsDTO dto2 = new RequestCollectionOfPublicationsDTO(
                collectionName, genreId, userId, isPrivate);

        // ASSERT
        assertEquals(dto1, dto2);
    }

    @Test
    void recordShouldNotBeEqualWhenValuesAreDifferent() {
        // ARRANGE
        RequestCollectionOfPublicationsDTO dto1 = new RequestCollectionOfPublicationsDTO(
                collectionName, genreId, userId, isPrivate);
        RequestCollectionOfPublicationsDTO dto2 = new RequestCollectionOfPublicationsDTO(
                "Other Collection", genreId, userId, isPrivate);

        // ASSERT
        assertNotEquals(dto1, dto2);
    }

    @Test
    void recordShouldHaveConsistentHashCode() {
        // ARRANGE
        RequestCollectionOfPublicationsDTO dto1 = new RequestCollectionOfPublicationsDTO(
                collectionName, genreId, userId, isPrivate);
        RequestCollectionOfPublicationsDTO dto2 = new RequestCollectionOfPublicationsDTO(
                collectionName, genreId, userId, isPrivate);

        // ASSERT
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void toStringShouldContainValues() {
        // ARRANGE
        RequestCollectionOfPublicationsDTO dto = new RequestCollectionOfPublicationsDTO(
                collectionName, genreId, userId, isPrivate);

        // ACT
        String result = dto.toString();

        // ASSERT
        assertTrue(result.contains(collectionName));
        assertTrue(result.contains(genreId));
        assertTrue(result.contains(userId));
        assertTrue(result.contains(isPrivate.toString()));
    }
}