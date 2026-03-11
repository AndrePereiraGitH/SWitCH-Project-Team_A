package TOPSECRET.dto;

import TOPSECRET.domain.*;
import TOPSECRET.domain.Enum.UserRole;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CollectionOfPublicationsDTO_US018.
 *
 * <p>
 * Validates:
 * </p>
 * <ul>
 *   <li>Constructor and getters</li>
 *   <li>Conversion from domain to DTO</li>
 *   <li>Behavior with empty lists</li>
 * </ul>
 *
 * Tests follow the AAA pattern.
 */
class CollectionOfPublicationsDTO_US018DependenciesTest {

    @Test
    void shouldCreateDTOWithCorrectValues() {

        // Arrange
        CollectionOfPublicationsDTO_US018 dto =
                new CollectionOfPublicationsDTO_US018("Favorites", "Fiction", "user1");

        // Act & Assert
        assertEquals("Favorites", dto.getName());
        assertEquals("Fiction", dto.getGenreName());
        assertEquals("user1", dto.getOwnerName());
    }

    @Test
    void shouldConvertDomainCollectionToDTO() {

        // Arrange
        User user = createValidUser();

        CollectionOfPublications collection =
                new CollectionOfPublicationsFactory().create("Favorites", new GenreFactory().create("Fiction"), user);

        // Act
        List<CollectionOfPublicationsDTO_US018> result =
                CollectionOfPublicationsDTO_US018.collectionOfPublicationToDTO(
                        List.of(collection)
                );

        // Assert
        assertEquals(1, result.size());
        assertEquals("Favorites", result.get(0).getName());
        assertEquals("Fiction", result.get(0).getGenreName());
        assertEquals(user.getUserName(), result.get(0).getOwnerName());
    }

    @Test
    void shouldReturnEmptyListWhenDomainListIsEmpty() {

        // Arrange
        List<CollectionOfPublications> emptyList = List.of();

        // Act
        List<CollectionOfPublicationsDTO_US018> result =
                CollectionOfPublicationsDTO_US018.collectionOfPublicationToDTO(emptyList);

        // Assert
        assertTrue(result.isEmpty());
    }

    private User createValidUser() {

        CountryFactory countryFactory = new CountryFactory();
        Country country = countryFactory.create("PORTUGAL");

        Profile profile = new Profile(
                "Test User",
                country,
                LocalDate.of(1990, 1, 1)
        );

        return UserFactory.create(
                "user1",
                new Contact("912345678", "351", null, null),
                profile,
                UserRole.ADMIN
        );
    }
}