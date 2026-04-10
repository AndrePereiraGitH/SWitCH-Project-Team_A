package TOPSECRET.dto;

/**
 * Data Transfer Object representing an author with just the name.
 */
public class AuthorDTO_US025 {

    private String name;

    public AuthorDTO_US025(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}