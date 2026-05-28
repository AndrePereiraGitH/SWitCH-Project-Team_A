package TOPSECRET.dto.edition.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object used when registering a new edition in the system.
 *
 * <p>This request DTO is used by the controller layer to receive
 * input data required to create and persist a new {@link TOPSECRET.domain.Edition.Edition}
 * domain object.</p>
 *
 * @param editionNumber    the edition number identifier
 * @param isbn10           the ISBN-10 code (optional)
 * @param isbn13           the ISBN-13 code (optional)
 * @param issn             the ISSN code (optional)
 * @param height           the height of the edition in cm
 * @param width            the width of the edition in cm
 * @param depth            the depth of the edition in cm
 * @param weight           the weight of the edition in grams
 * @param numberOfPages    the number of pages
 * @param publicationTypeId the publication type identifier
 * @param publisherId      the publisher identifier
 * @param mediaFormat      the media format (Book or Magazine)
 * @param editionLanguage  the language of the edition (e.g. "en", "pt")
 * @param editionDate      the publication date in ISO format (yyyy-MM-dd)
 * @param typeOfBinding    the type of binding
 */
public record CreateEditionDTO(
        @NotBlank(message = "Edition number cannot be blank")
        String editionNumber,
        String isbn10,
        String isbn13,
        String issn,
        double height,
        double width,
        double depth,
        double weight,
        int numberOfPages,
        @NotBlank(message = "Publication type ID cannot be blank")
        String publicationTypeId,
        @NotBlank(message = "Publisher ID cannot be blank")
        String publisherId,
        @NotBlank(message = "Media format cannot be blank")
        String mediaFormat,
        @NotBlank(message = "Edition language cannot be blank")
        String editionLanguage,
        @NotBlank(message = "Edition date cannot be blank")
        String editionDate,
        @NotBlank(message = "Type of binding cannot be blank")
        String typeOfBinding
) {

}
