package TOPSECRET.dto;

import TOPSECRET.domain.Genre;

/**
 * Data Transfer Object (DTO) that carries genre information to the UI — US017.
 *
 * <p>In US017 the user needs to pick a genre before creating a collection.
 * The controller fetches genres from the repository and converts each {@link Genre}
 * into this DTO before sending them to the UI.
 *
 * <p>The reason for using a DTO instead of passing the real {@link Genre} object is
 * to keep the UI layer separate from the domain layer. The UI only needs the genre name —
 * it does not need (and should not have access to) the full domain object.
 *
 * <p>The conversion from {@link Genre} to this DTO happens inside this class itself,
 * in the {@link #toDTO(Genre)} method. There is no separate Mapper class needed
 * because the conversion is simple — just one field.
 */
public class GenreDTO_US017 {

    // the only data the UI needs — just the genre name as a plain String
    public final String name;

    /**
     * Private constructor — a DTO can only be created through {@link #toDTO(Genre)}.
     *
     * <p>Making the constructor private forces all creation to go through the factory method.
     * This way the DTO is always built from a real {@link Genre} object,
     * and we avoid creating DTOs with invalid or missing data.
     *
     * @param name the genre name to store in the DTO
     */
    private GenreDTO_US017(String name) {
        this.name = name;
    }

    /**
     * Converts a {@link Genre} domain object into a {@link GenreDTO_US017} — US017.
     *
     * <p>This is a static method because the conversion does not depend on any instance state —
     * it only needs the {@link Genre} object passed as a parameter.
     * Static factory methods like this are a common pattern when the constructor is private.
     *
     * @param genre the genre to convert; must not be null
     * @return a new DTO containing the genre name
     * @throws IllegalArgumentException if genre is null
     */
    // static — no instance needed to do the conversion; takes a Genre, returns a DTO
    public static GenreDTO_US017 toDTO(Genre genre) {
        // null genre would cause a NullPointerException on the next line — reject it early
        if (genre == null) {
            throw new IllegalArgumentException("Genre cannot be null.");
        }
        return new GenreDTO_US017(genre.getGenreType());
    }
}