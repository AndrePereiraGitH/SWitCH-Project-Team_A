package TOPSECRET.domain;

import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

// Unit tests for CollectionOfPublicationsFactory - US017
class CollectionOfPublicationsFactoryTest {

    // Tests that the factory correctly delegates construction to CollectionOfPublications
    // MockedConstruction intercepts the constructor call so the factory is tested in isolation
    @Test
    void shouldCreateNonNullCollectionOfPublications() {
        // Arrange
        Genre genre = mock(Genre.class);
        User user = mock(User.class);

        try (MockedConstruction<CollectionOfPublications> mocked =
                     mockConstruction(CollectionOfPublications.class,
                             (mock, context) -> {
                                 when(mock.getCollectionName()).thenReturn("MyCollection");
                                 when(mock.getCollectionGenre()).thenReturn(genre);
                                 when(mock.getOwner()).thenReturn(user);
                                 when(mock.getIsPrivate()).thenReturn(true);
                             })) {

            // Act
            CollectionOfPublicationsFactory factory = new CollectionOfPublicationsFactory();
            CollectionOfPublications result = factory.create(
                    "MyCollection",
                    genre,
                    user
            );

            // Assert
            assertEquals("MyCollection", result.getCollectionName());
            assertEquals(genre, result.getCollectionGenre());
            assertEquals(user, result.getOwner());
            assertTrue(result.getIsPrivate());
        }
    }
}