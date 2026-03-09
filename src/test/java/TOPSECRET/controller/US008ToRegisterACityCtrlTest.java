//package TOPSECRET.controller;
//
//import TOPSECRET.domain.City;
//import TOPSECRET.domain.Country;
//import TOPSECRET.repository.CityRepository;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
///**
// * Unit tests for {@link US008ToRegisterACityCtrl}.
// *
// * <p>This test suite verifies the behavior of the controller responsible for
// * registering new cities in the system. The controller depends on
// * {@link CityRepository}, which is mocked in order to isolate the controller logic.</p>
// *
// * <p>The {@link City} domain object is mocked because its constructor is protected
// * and the controller only needs to forward the object returned by the repository.</p>
// */
//class US008ToRegisterACityCtrlTest {
//
//    private final Country anyCountry = new Country("Portugal");
//    private final String anyCityName = "Braga";
//
//    @Test
//    void shouldThrowWhenRepositoryIsNull() {
//
//        // Act + Assert
//        assertThrows(IllegalArgumentException.class,
//                () -> new US008ToRegisterACityCtrl(null));
//    }
//
//    @Test
//    void shouldCallUponRepositoryToCreateCity() {
//
//        // Arrange
//        CityRepository repository = mock(CityRepository.class);
//        US008ToRegisterACityCtrl controller = new US008ToRegisterACityCtrl(repository);
//
//        City city = mock(City.class);
//
//        when(repository.createAndAdd(anyCountry, anyCityName)).thenReturn(city);
//
//        // Act
//        controller.registerNewCity(anyCountry, anyCityName);
//
//        // Assert
//        verify(repository).createAndAdd(anyCountry, anyCityName);
//    }
//
//    @Test
//    void shouldReturnCityFromRepository() {
//
//        // Arrange
//        CityRepository repository = mock(CityRepository.class);
//        US008ToRegisterACityCtrl controller = new US008ToRegisterACityCtrl(repository);
//
//        City city = mock(City.class);
//
//        when(repository.createAndAdd(anyCountry, anyCityName)).thenReturn(city);
//
//        // Act
//        City result = controller.registerNewCity(anyCountry, anyCityName);
//
//        // Assert
//        assertEquals(city, result);
//    }
//
//    @Test
//    void shouldPropagateRepositoryException() {
//
//        // Arrange
//        CityRepository repository = mock(CityRepository.class);
//        US008ToRegisterACityCtrl controller = new US008ToRegisterACityCtrl(repository);
//
//        when(repository.createAndAdd(anyCountry, anyCityName))
//                .thenThrow(new IllegalArgumentException());
//
//        // Act + Assert
//        assertThrows(IllegalArgumentException.class,
//                () -> controller.registerNewCity(anyCountry, anyCityName));
//    }
//}