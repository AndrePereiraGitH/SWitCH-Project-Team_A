package TOPSECRET.dto.publisher.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object used to receive publisher creation requests.
 * <p>
 * This DTO contains all information required to create a Publisher,
 * including its basic data, contact information, email, and address.
 * <p>
 * It acts as an input model for the application layer and is mapped
 * into domain value objects by the {@code PublisherMapper}.
 *
 * @param name        the publisher name
 * @param vatNumber   the publisher VAT number
 * // Contact information
 * @param phoneNumber the publisher phone number
 * @param extension   the publisher phone extension
 * // Email information
 * @param username    the email username (part before '@')
 * @param domain      the email domain (part after '@')
 * // Address information
 * @param street      the street name
 * @param number      the street/building number
 * @param zipCode     the postal/ZIP code
 * @param cityId      the identifier of the city
 */

public record RequestPublisherDTO(

        @NotBlank(message = "Publisher name must not be blank")
        String name,

        @NotBlank(message = "VAT number must not be blank")
        String vatNumber,

        // Contact
        @NotBlank(message = "Phone number must not be blank")
        String phoneNumber,

        @NotBlank(message = "Extension must not be blank")
        String extension,

        // Email
        @NotBlank(message = "Email username must not be blank")
        String username,

        @NotBlank(message = "Email domain must not be blank")
        String domain,

        // Address
        @NotBlank(message = "Street must not be blank")
        String street,

        @NotBlank(message = "Number must not be blank")
        String number,

        @NotBlank(message = "ZipCode must not be blank")
        String zipCode,

        @NotBlank(message = "City must not be blank")
        String cityId

) {
}