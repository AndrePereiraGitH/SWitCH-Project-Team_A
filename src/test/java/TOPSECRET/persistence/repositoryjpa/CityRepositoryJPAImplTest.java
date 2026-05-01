package TOPSECRET.persistence.repositoryjpa;

import TOPSECRET.domain.City.City;
import TOPSECRET.persistence.repositoryjpa.assembler.CityAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.CityDM;
import TOPSECRET.persistence.repositoryjpa.jpa.ICityRepositoryJPA;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CityRepositoryJPAImpl}.
 *
 * <p>This test suite verifies the behavior of the JPA repository implementation
 * responsible for persisting {@link City} domain objects. Both
 * {@link ICityRepositoryJPA} and {@link CityAssembler} are mocked to isolate
 * the repository logic from the database and conversion layers.</p>
 */
@ExtendWith(MockitoExtension.class)
class CityRepositoryJPAImplTest {

    @Mock
    private ICityRepositoryJPA doubleJpaRepository;
    @Mock
    private CityAssembler doubleAssembler;
    @InjectMocks
    private CityRepositoryJPAImpl repository;

    @Test
    void shouldSaveCityAndReturnIt() {
        // Arrange
        City doubleCity = mock(City.class);
        CityDM doubleCityDM = mock(CityDM.class);
        when(doubleAssembler.toDM(doubleCity)).thenReturn(doubleCityDM);
        when(doubleJpaRepository.save(doubleCityDM)).thenReturn(doubleCityDM);
        when(doubleAssembler.toDomain(doubleCityDM)).thenReturn(doubleCity);

        // Act
        City result = repository.save(doubleCity);

        // Assert
        assertEquals(doubleCity, result);
    }

    @Test
    void shouldPropagateExceptionWhenJpaThrows() {

        // Arrange
        City doubleCity = mock(City.class);
        CityDM doubleCityDM = mock(CityDM.class);
        when(doubleAssembler.toDM(doubleCity)).thenReturn(doubleCityDM);
        when(doubleJpaRepository.save(doubleCityDM))
                .thenThrow(new RuntimeException("Database error"));

        // Act + Assert
        assertThrows(RuntimeException.class, () -> repository.save(doubleCity));
    }
}