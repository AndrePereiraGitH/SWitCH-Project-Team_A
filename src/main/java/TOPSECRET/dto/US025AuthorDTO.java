package TOPSECRET.dto;

/**
 * Data Transfer Object representing an author for use in US025.
 */
public class US025AuthorDTO {

    /**
     * The author's name.
     */
    private final String name;

    public US025AuthorDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        US025AuthorDTO other = (US025AuthorDTO) o;
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}