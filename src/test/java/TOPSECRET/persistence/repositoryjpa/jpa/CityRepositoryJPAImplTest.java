package TOPSECRET.persistence.repositoryjpa.jpa;

import TOPSECRET.domain.City.City;
import TOPSECRET.persistence.repositoryjpa.assembler.CityAssembler;
import TOPSECRET.persistence.repositoryjpa.datamodel.CityDM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
class CityRepositoryJPAImplTest {

    private ICityRepositoryJPA doubleJpaRepository;
    private CityAssembler doubleAssembler;
    private CityRepositoryJPAImpl repository;

    @BeforeEach
    void setUp() {
        doubleJpaRepository = mock(ICityRepositoryJPA.class);
        doubleAssembler = mock(CityAssembler.class);
        repository = new CityRepositoryJPAImpl(doubleJpaRepository, doubleAssembler);
    }

    @Test
    void shouldSaveCityAndReturnIt() {

        // Arrange
        City doubleCity = mock(City.class);
        CityDM doubleCityDM = mock(CityDM.class);
        when(doubleAssembler.DomainToDM(doubleCity)).thenReturn(doubleCityDM);

        // Act
        City result = repository.save(doubleCity);

        // Assert
        assertEquals(doubleCity, result);
        verify(doubleAssembler).DomainToDM(doubleCity);
        verify(doubleJpaRepository).save(doubleCityDM);
    }

    @Test
    void shouldPropagateExceptionWhenJpaThrows() {

        // Arrange
        City doubleCity = mock(City.class);
        CityDM doubleCityDM = mock(CityDM.class);
        when(doubleAssembler.DomainToDM(doubleCity)).thenReturn(doubleCityDM);
        when(doubleJpaRepository.save(doubleCityDM))
                .thenThrow(new RuntimeException("Database error"));

        // Act + Assert
        assertThrows(RuntimeException.class, () -> repository.save(doubleCity));
    }
}