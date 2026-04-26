package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.CountryID;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * JPA data model that represents an author in the persistence layer.
 * <p>
 * This entity is mapped to the {@code Authors} table and stores the author's
 * identifier, name, country, date of birth and biography.
 * </p>
 */
@Entity
@Table(name = "Authors")
@Getter
@NoArgsConstructor
public class AuthorDM {
    @EmbeddedId
    private AuthorId id;
    private String name;
    @Embedded
    private CountryID countryId;
    private LocalDate dateOfBirth;
    private String biography;

    /**
     * Creates an {@code AuthorDM} with all required fields.
     *
     * @param id          the unique identifier of the author
     * @param name        the name of the author
     * @param countryId   the identifier of the author's country
     * @param dateOfBirth the author's date of birth
     * @param biography   the author's biography
     * @throws IllegalArgumentException if any argument is {@code null}
     */
    public AuthorDM(AuthorId id, String name, CountryID countryId, LocalDate dateOfBirth, String biography) {

        if (id == null || name == null || countryId == null || dateOfBirth == null || biography == null) {
            throw new IllegalArgumentException("All fields must not be null.");
        }

        this.id = id;
        this.name = name;
        this.countryId = countryId;
        this.dateOfBirth = dateOfBirth;
        this.biography = biography;
    }

}
