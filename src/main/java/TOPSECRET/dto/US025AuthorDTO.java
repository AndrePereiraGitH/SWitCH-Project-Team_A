package TOPSECRET.dto;

import lombok.Value;

/**
 * Data Transfer Object representing an author for use in US025.
 */
@Value
public class US025AuthorDTO {

    /**
     * The author's name.
     */
    String name;
}