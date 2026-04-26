package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.ValueObjects.AuthorId;
import TOPSECRET.domain.ValueObjects.CountryID;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Table(name = "Authors")
@Getter
public class AuthorDM {
    @EmbeddedId
    private AuthorId id;
    private String name;
    @Embedded
    private CountryID countryId;
    private LocalDate dateOfBirth;
    private String biography;

    public AuthorDM() {}

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
