package TOPSECRET.dto;

/**
 * Data Transfer Object representing a genre.
 * <p>
 * This record is used to transfer genre data across application boundaries,
 * ensuring the genre name is always valid and non-blank.
 * </p>
 */

public record US013GenreDTO(String genreId, String genreName) {}