package TOPSECRET.dto.city.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object representing the request for creating a new city.
 *
 * <p>Used as input to the city creation use case.</p>
 *
 * @param cityName  the name of the city to be created; must not be blank
 * @param countryId the identifier of the country the city belongs to; must not be blank
 */
public record RequestCityDTO (

    @NotBlank(message = "City name must not be blank")
    String cityName,

    @NotBlank(message = "Country ID must not be blank")
    String countryId,

    @NotBlank(message = "Country name must not be blank")
    String countryName)
{}