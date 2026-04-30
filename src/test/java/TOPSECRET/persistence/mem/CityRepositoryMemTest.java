package TOPSECRET.persistence.mem;

import TOPSECRET.domain.City.City;
import TOPSECRET.domain.City.ICityRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * Unit tests for {@link CityRepositoryMem}.
 *
 * <p>These tests verify the behavior of saving and preventing
 * duplicate {@link City} instances in the in-memory repository.</p>
 *
 * <p>The tests follow an opaque testing approach, meaning that only the
 * observable results of operations (returned values and exceptions)
 * are verified rather than interactions with dependencies.</p>
 */
class CityRepositoryMemTest {

    private final City anyCity = mock(City.class);

    @Test
    void shouldSaveCity() {
        // Arrange
        ICityRepository repository = new CityRepositoryMem();

        // Act
        City result = repository.save(anyCity);

        // Assert
        assertEquals(anyCity, result);
    }

    @Test
    void shouldThrowWhenSavingDuplicateCity() {
        // Arrange
        ICityRepository repository = new CityRepositoryMem();
        when(anyCity.sameAs(anyCity)).thenReturn(true);
        repository.save(anyCity);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> repository.save(anyCity));
    }

    @Test
    void shouldStoreOnlyOneInstanceOfCity() {
        // Arrange
        ICityRepository repository = new CityRepositoryMem();
        when(anyCity.sameAs(anyCity)).thenReturn(true);
        repository.save(anyCity);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> repository.save(anyCity));
        assertEquals("This city already exists in the system.", ex.getMessage());
    }
}