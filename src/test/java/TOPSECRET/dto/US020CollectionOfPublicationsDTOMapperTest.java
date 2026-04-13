package TOPSECRET.dto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import TOPSECRET.domain.CollectionOfPublications.CollectionOfPublications;
import TOPSECRET.domain.User.IUserRepository;
import TOPSECRET.domain.User.User;
import TOPSECRET.domain.ValueObjects.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;


/**
 * Unit tests for {@link US020CollectionOfPublicationsDTOMapper}.
 * <p>
 * These tests verify that the DTO mapper correctly handles valid input,
 * trims whitespace from fields, and throws exceptions when invalid
 * values are provided.
 */


class US020CollectionOfPublicationsDTOMapperTest {

    IUserRepository userRepo;
    UserId userId;
    User user;
    CollectionOfPublications collectionDouble;

    @BeforeEach
    void setUp() {
        userRepo = mock(IUserRepository.class);
        userId = mock(UserId.class);
        user = mock(User.class);
        collectionDouble = mock(CollectionOfPublications.class);
    }

    /**
     * Verifies that the DTO is created successfully when valid
     * collections and user repository is provided.
     * Also confirms that the stored values match the input.
     */
    @Test
    void shouldCreateDTOWithValidData()
    {
        // Arrange
        List<CollectionOfPublications> collections = List.of(collectionDouble);
        when(collectionDouble.getOwnerId()).thenReturn(userId);
        when(userRepo.findUserByID(userId)).thenReturn(Optional.ofNullable(user));
        when(collectionDouble.getCollectionName()).thenReturn("Collection Name");
        when(user.getUserName()).thenReturn("Antonio");

        // Act
        List<US020CollectionOfPublicationsDTO> dto =
                US020CollectionOfPublicationsDTOMapper.toDTO(collections, userRepo);

        // Assert
        assertNotNull(dto);
        assertEquals("Collection Name", dto.get(0).collectionName());
        assertEquals("Antonio", dto.get(0).userName());
    }

    @Test
    void shouldTrimCollectionNameAndUserName() {
        // Arrange
        List<CollectionOfPublications> collections = List.of(collectionDouble);

        when(collectionDouble.getCollectionName()).thenReturn("  Collection Name  ");
        when(collectionDouble.getOwnerId()).thenReturn(userId);
        when(userRepo.findUserByID(userId)).thenReturn(Optional.of(user));
        when(user.getUserName()).thenReturn("  Antonio  ");

        // Act
        List<US020CollectionOfPublicationsDTO> dto =
                US020CollectionOfPublicationsDTOMapper.toDTO(collections, userRepo);

        // Assert
        assertEquals("Collection Name", dto.get(0).collectionName());
        assertEquals("Antonio", dto.get(0).userName());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(collectionDouble.getCollectionName()).thenReturn("Collection name");
        when(collectionDouble.getOwnerId()).thenReturn(userId);

        when(userRepo.findUserByID(userId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> US020CollectionOfPublicationsDTOMapper.toDTO(List.of(collectionDouble), userRepo)
        );

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void shouldReturnEmptyListWhenInputIsEmpty() {
        // Act
        List<US020CollectionOfPublicationsDTO> result =
                US020CollectionOfPublicationsDTOMapper.toDTO(List.of(), userRepo);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
