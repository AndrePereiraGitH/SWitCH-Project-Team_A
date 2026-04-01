package TOPSECRET.controller;

import TOPSECRET.domain.City.City;
import TOPSECRET.domain.Country.Country;
import TOPSECRET.repository.CityRepositoryMem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link US008ToRegisterACityCtrl}.
 *
 * <p>This test suite verifies the behavior of the controller responsible for
 * registering new cities in the system. The controller depends on
 * {@link CityRepositoryMem}, which is mocked in order to isolate the controller logic.</p>
 *
 * <p>The {@link City} domain object is mocked because its constructor is protected
 * and the controller only needs to forward the object returned by the repository.</p>
 */
class US008ToRegisterACityCtrlTest {


    // Collaborator
    private final CityRepositoryMem anyCityRepositoryMem = mock(CityRepositoryMem.class);

    //Variables important for testing
    private final String anyCityName = "Salamanca";
    private final City anyCity = mock(City.class);
    private final Country anyCountry = mock(Country.class);

    @Test
    void shouldThrowWhenRepositoryIsNull() {

        //The SUT here could be considered the ctrl class and the CityRepo class

        //Arrange
        CityRepositoryMem nullRepository = null;

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new US008ToRegisterACityCtrl(nullRepository));
    }

    @Test
    void shouldReturnCityCreatedByRepo() {

        // Arrange
        US008ToRegisterACityCtrl controller = new US008ToRegisterACityCtrl(anyCityRepositoryMem);

        when(anyCityRepositoryMem.createAndAdd(anyCountry, anyCityName)).thenReturn(anyCity);

        // Act, but I am not sure if this is valid to use later when asserting
        City result = controller.registerNewCity(anyCountry, anyCityName);

        // Assert
        assertEquals(anyCity, result);
    }

    @Test
    void shouldPropagateExceptionWhenCountryIsNull() {

        //Arrange, the SUT here would in theory be the ctrl and country
        US008ToRegisterACityCtrl controller = new US008ToRegisterACityCtrl(anyCityRepositoryMem);
        Country nullCountry = null;

        when(anyCityRepositoryMem.createAndAdd(nullCountry, anyCityName))
                .thenThrow(new IllegalArgumentException());

        //Act + assert
        assertThrows(IllegalArgumentException.class, () -> controller.registerNewCity(nullCountry, anyCityName));
    }
}