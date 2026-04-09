package TOPSECRET.dto;

import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class US020_CollectionOfPublicationsDTOMapperTest {

    private final US020_CollectionOfPublicationsDTOMapper mapper = new US020_CollectionOfPublicationsDTOMapper();
    private CollectionOfPublications c1;
    private CollectionOfPublications c2;
    private UserId userId1;
    private UserId userId2;
    private Map<UserId, String> ownerNames;

    @BeforeEach
    void setUp() {
        c1 = mock(CollectionOfPublications.class);
        c2 = mock(CollectionOfPublications.class);
        userId1 = mock(UserId.class);
        userId2 = mock(UserId.class);

        when(c1.getOwnerId()).thenReturn(userId1);
        when(c2.getOwnerId()).thenReturn(userId2);

        when(c1.getCollectionName()).thenReturn("Collection A");
        when(c2.getCollectionName()).thenReturn("Collection B");

        ownerNames = new HashMap<>();
        ownerNames.put(userId1, "Alice");
        ownerNames.put(userId2, "Bob");
    }

    @Test
    void shouldConvertCollectionsToDTOs() {
        List<CollectionOfPublications> collections = List.of(c1, c2);

        // Act
        List<US020_CollectionOfPublicationsDTO> result =
                mapper.toDTO(collections, ownerNames);

        // Assert
        assertEquals(2, result.size());

        assertEquals("Collection A", result.get(0).getListName());
        assertEquals("Alice", result.get(0).getOwnerUsername());

        assertEquals("Collection B", result.get(1).getListName());
        assertEquals("Bob", result.get(1).getOwnerUsername());
    }

    @Test
    void shouldReturnEmptyListWhenCollectionsEmpty() {
        // Arrange
        List<CollectionOfPublications> collections = Collections.emptyList();
        Map<UserId, String> ownerNames = new HashMap<>();

        // Act
        List<US020_CollectionOfPublicationsDTO> result =
                mapper.toDTO(collections, ownerNames);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenOwnerMissing() {
        // Arrange
        CollectionOfPublications c1 = mock(CollectionOfPublications.class);
        UserId userId = mock(UserId.class);

        when(c1.getOwnerId()).thenReturn(userId);
        when(c1.getCollectionName()).thenReturn("Collection A");

        List<CollectionOfPublications> collections = List.of(c1);
        Map<UserId, String> ownerNames = new HashMap<>(); // empty map

        // Act & Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> mapper.toDTO(collections, ownerNames)
        );

        assertTrue(exception.getMessage().contains("User not found"));
    }

    @Test
    void shouldPreserveOrder() {
        // Arrange
        CollectionOfPublications c1 = mock(CollectionOfPublications.class);
        CollectionOfPublications c2 = mock(CollectionOfPublications.class);

        UserId userId1 = mock(UserId.class);
        UserId userId2 = mock(UserId.class);

        when(c1.getOwnerId()).thenReturn(userId1);
        when(c2.getOwnerId()).thenReturn(userId2);

        when(c1.getCollectionName()).thenReturn("First");
        when(c2.getCollectionName()).thenReturn("Second");

        Map<UserId, String> ownerNames = Map.of(
                userId1, "Owner1",
                userId2, "Owner2"
        );

        List<CollectionOfPublications> collections = List.of(c1, c2);

        // Act
        List<US020_CollectionOfPublicationsDTO> result =
                mapper.toDTO(collections, ownerNames);

        // Assert
        assertEquals("First", result.get(0).getListName());
        assertEquals("Second", result.get(1).getListName());
    }
}