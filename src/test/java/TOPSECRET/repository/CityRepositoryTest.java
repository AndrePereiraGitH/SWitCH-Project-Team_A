//package TOPSECRET.repository;
//
//import TOPSECRET.domain.City;
//import TOPSECRET.domain.CityFactory;
//import TOPSECRET.domain.Country;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//
///**
// * Unit tests for {@link CityRepository}.
// * <p>
// * These tests verify the behavior of creating, adding, and handling
// * duplicate {@link City} objects using a mocked {@link CityFactory}.
// */
//
//class CityRepositoryTest {
//
//    private final Country anyCountry = new Country("Portugal");
//    private final String anyName = "Braga";
//
//    // Mocked city to be returned by the factory as the constructor is now protected
//    private final City anyCity = mock(City.class);
//    private final CityFactory factory = mock(CityFactory.class);
//
//    @Test
//    void shouldCreateAndAddCity() {
//        //Arrange
//        CityRepository repository = new CityRepository(factory);
//
//        when(factory.create(anyCountry, anyName)).thenReturn(anyCity);
//
//        //Act
//        City result = repository.createAndAdd(anyCountry, anyName);
//
//        //Assert
//        assertEquals(anyCity, result);
//        verify(factory).create(anyCountry, anyName);
//    }
//
//    @Test
//    void shouldThrowWhenCityAlreadyExists() {
//        //Arrange
//        CityRepository repository = new CityRepository(factory);
//
//        when(factory.create(anyCountry, anyName)).thenReturn(anyCity);
//
//        repository.createAndAdd(anyCountry, anyName);
//
//        //Act + assert
//        assertThrows(IllegalArgumentException.class,
//                () -> repository.createAndAdd(anyCountry, anyName));
//
//        // Indicates how many times a given class is used/call (and, as such, if it's used at all)
//        verify(factory, times(2)).create(anyCountry, anyName);
//    }
//
//    @Test
//    void shouldThrowExceptionIfCountryNull() {
//
//        //Arrange
//        CityRepository repository = new CityRepository(factory);
//
//        when(factory.create(null, anyName))
//                .thenThrow(new IllegalArgumentException());
//
//        //Act + assert
//        assertThrows(IllegalArgumentException.class,
//                () -> repository.createAndAdd(null, anyName));
//    }
//
//    @Test
//    void shouldThrowIfCityExists() {
//
//        //Arrange
//        CityRepository repository = new CityRepository(factory);
//
//        when(factory.create(anyCountry, anyName)).thenReturn(anyCity);
//
//        repository.createAndAdd(anyCountry, anyName);
//
//        //Act + assert
//        assertThrows(IllegalArgumentException.class,
//                () -> repository.createAndAdd(anyCountry, anyName));
//    }
//}