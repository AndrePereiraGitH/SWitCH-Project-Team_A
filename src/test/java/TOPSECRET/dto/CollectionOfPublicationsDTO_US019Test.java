//package TOPSECRET.dto;
//
//import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
//import TOPSECRET.domain.Genre.Genre;
//import TOPSECRET.domain.User.User;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//
///**
// * Unit tests for {@link CollectionOfPublicationsDTO_US019}.
// *
// * These tests verify that the DTO is correctly constructed and that
// * the toDTO() factory method correctly maps domain objects to DTOs.
// */
//class CollectionOfPublicationsDTO_US019Test {
//
//        @Test
//        void ensureDTOIsCreatedSuccessfully() {
//            // Act
//            CollectionOfPublicationsDTO_US019 dto =
//                    new CollectionOfPublicationsDTO_US019("My Collection", "Fiction", "alice");
//
//            // Assert
//            assertNotNull(dto);
//        }
//
//        @Test
//        void ensureGetNameReturnsCorrectValue() {
//            // Act
//            CollectionOfPublicationsDTO_US019 dto =
//                    new CollectionOfPublicationsDTO_US019("My Collection", "Fiction", "alice");
//
//            // Assert
//            assertEquals("My Collection", dto.get_name());
//        }
//
//        @Test
//        void ensureGetGenreNameReturnsCorrectValue() {
//            // Act
//            CollectionOfPublicationsDTO_US019 dto =
//                    new CollectionOfPublicationsDTO_US019("My Collection", "Fiction", "alice");
//
//            // Assert
//            assertEquals("Fiction", dto.get_genreName());
//        }
//
//        @Test
//        void ensureGetOwnerReturnsCorrectValue() {
//            // Act
//            CollectionOfPublicationsDTO_US019 dto =
//                    new CollectionOfPublicationsDTO_US019("My Collection", "Fiction", "alice");
//
//            // Assert
//            assertEquals("alice", dto.getOwnerName());
//        }
//
//    /**
//     * Verifies that toDTO() correctly maps a CollectionOfPublications domain object
//     * to a DTO, extracting the collection name, genre type, and owner username.
//     */
//    @Test
//    void ensureToDTOMapsCollectionCorrectly() {
//        // Arrange
//        CollectionOfPublications collectionDouble = mock(CollectionOfPublications.class);
//        Genre genreDouble = mock(Genre.class);
//        User ownerDouble = mock(User.class);
//
//        when(collectionDouble.getCollectionName()).thenReturn("My Collection");
//        when(collectionDouble.getCollectionGenre()).thenReturn(genreDouble);
//        when(genreDouble.getGenreType()).thenReturn("Fiction");
//        when(collectionDouble.getOwner()).thenReturn(ownerDouble);
//        when(ownerDouble.getUserName()).thenReturn("alice");
//
//        // Act
//        CollectionOfPublicationsDTO_US019 dto =
//                CollectionOfPublicationsDTO_US019.toDTO(collectionDouble);
//
//        // Assert
//        assertNotNull(dto);
//        assertEquals("My Collection", dto.get_name());
//        assertEquals("Fiction", dto.get_genreName());
//        assertEquals("alice", dto.getOwnerName());
//    }
//
//    /**
//     * Verifies that toDTO() throws an exception when passed a null collection.
//     */
//    @Test
//    void ensureToDTOThrowsExceptionWhenCollectionIsNull() {
//        assertThrows(Exception.class, () ->
//                CollectionOfPublicationsDTO_US019.toDTO(null));
//    }
//}