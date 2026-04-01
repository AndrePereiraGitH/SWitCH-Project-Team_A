//package TOPSECRET.dto;
//
//import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
//import TOPSECRET.domain.Genre.Genre;
//import TOPSECRET.domain.User.User;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
///**
// * Unit tests for CollectionOfPublicationsDTO_US018.
// *
// * Validates:
// * - Constructor and getters
// * - Conversion from domain CollectionOfPublications to DTO
// * - Behavior with empty lists
// * - Behavior with multiple collections
// *
// * Tests follow the AAA pattern.
// */
//class CollectionOfPublicationsDTO_US018Test {
//
//    // ============================
//    // Helpers
//    // ============================
//
//    private CollectionOfPublications createMockCollection(
//            String name,
//            String genre,
//            String ownerName) {
//
//        CollectionOfPublications collection = mock(CollectionOfPublications.class);
//        Genre genreMock = mock(Genre.class);
//        User owner = mock(User.class);
//
//        when(collection.getCollectionName()).thenReturn(name);
//        when(collection.getCollectionGenre()).thenReturn(genreMock);
//        when(collection.getOwner()).thenReturn(owner);
//
//        when(genreMock.getGenreType()).thenReturn(genre);
//        when(owner.getUserName()).thenReturn(ownerName);
//
//        return collection;
//    }
//
//    // ============================
//    // Tests
//    // ============================
//
//    @Test
//    void shouldCreateDTOWithCorrectValues() {
//
//        // ARRANGE
//        CollectionOfPublicationsDTO_US018 dto =
//                new CollectionOfPublicationsDTO_US018(
//                        "My Collection",
//                        "Fantasy",
//                        "user1"
//                );
//
//        // ACT + ASSERT
//        assertEquals("My Collection", dto.getName());
//        assertEquals("Fantasy", dto.getGenreName());
//        assertEquals("user1", dto.getOwnerName());
//    }
//
//    @Test
//    void shouldConvertCollectionToDTO() {
//
//        // ARRANGE
//        CollectionOfPublications collection =
//                createMockCollection("My Collection", "Fantasy", "user1");
//
//        // ACT
//        List<CollectionOfPublicationsDTO_US018> result =
//                CollectionOfPublicationsDTO_US018.collectionOfPublicationToDTO(
//                        List.of(collection)
//                );
//
//        // ASSERT
//        assertEquals(1, result.size());
//        assertEquals("My Collection", result.get(0).getName());
//        assertEquals("Fantasy", result.get(0).getGenreName());
//        assertEquals("user1", result.get(0).getOwnerName());
//    }
//
//    @Test
//    void shouldConvertMultipleCollectionsToDTO() {
//
//        // ARRANGE
//        CollectionOfPublications c1 =
//                createMockCollection("Collection 1", "Fantasy", "user1");
//
//        CollectionOfPublications c2 =
//                createMockCollection("Collection 2", "Sci-Fi", "user2");
//
//        // ACT
//        List<CollectionOfPublicationsDTO_US018> result =
//                CollectionOfPublicationsDTO_US018.collectionOfPublicationToDTO(
//                        List.of(c1, c2)
//                );
//
//        // ASSERT
//        assertEquals(2, result.size());
//
//        assertEquals("Collection 1", result.get(0).getName());
//        assertEquals("Fantasy", result.get(0).getGenreName());
//        assertEquals("user1", result.get(0).getOwnerName());
//
//        assertEquals("Collection 2", result.get(1).getName());
//        assertEquals("Sci-Fi", result.get(1).getGenreName());
//        assertEquals("user2", result.get(1).getOwnerName());
//    }
//
//    @Test
//    void shouldReturnEmptyListWhenCollectionListIsEmpty() {
//
//        // ARRANGE
//        List<CollectionOfPublications> emptyList = List.of();
//
//        // ACT
//        List<CollectionOfPublicationsDTO_US018> result =
//                CollectionOfPublicationsDTO_US018.collectionOfPublicationToDTO(emptyList);
//
//        // ASSERT
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    void shouldThrowExceptionWhenCollectionListIsNull() {
//
//        // ARRANGE
//        List<CollectionOfPublications> collections = null;
//
//        // ACT + ASSERT
//        assertThrows(
//                NullPointerException.class,
//                () -> CollectionOfPublicationsDTO_US018.collectionOfPublicationToDTO(collections)
//        );
//    }
//
//}