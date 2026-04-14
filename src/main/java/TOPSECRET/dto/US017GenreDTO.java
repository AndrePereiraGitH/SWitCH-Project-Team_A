package TOPSECRET.dto;

/**
 * Data Transfer Object that carries genre information to the UI — US017.
 *
 * <p>In US017 the user needs to pick a genre before creating a collection.
 * The UI only needs the genre name — it does not need the full domain object.
 * Conversion from domain to DTO is handled by {@link US017GenreMapper}.
 */
public record US017GenreDTO(String name) {

    public US017GenreDTO {
        if (name == null) {
            throw new NullPointerException("Name cannot be null.");
        }
    }
}
