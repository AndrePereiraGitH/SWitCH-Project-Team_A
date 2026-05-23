package TOPSECRET.dto.country.Request;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object used when registering a new country in the system.
 *
 * <p>This request DTO is used by the controller/service layer to receive
 * input data required to create and persist a new {@code Country} domain object.</p>
 *
 * <p>The country name is validated to ensure it is not null, empty, or blank
 * using {@link jakarta.validation.constraints.NotBlank}. A custom validation
 * message is provided to improve API feedback.</p>
 *
 * @param countryName the name of the country to be registered
 */
public record RegisterCountryDTO(

    @NotBlank(message = "Country name cannot be blank or null!")
            String countryName
){

}