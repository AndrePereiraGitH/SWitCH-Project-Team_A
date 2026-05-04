package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.ValueObjects.AppraisalEntityId;
import TOPSECRET.domain.ValueObjects.Contact;
import TOPSECRET.domain.ValueObjects.GenreId;
import TOPSECRET.domain.ValueObjects.PublicationTypeId;
import jakarta.persistence.*;

import lombok.*;

import java.util.List;

/**
 * Data Model (DM) representing an Appraisal Entity persisted in the database.
 *
 * <p>
 * This class maps directly to the "appraisal_entities" table and is responsible
 * only for persistence concerns.
 * </p>
 *
 * <p>
 * Value Objects such as AppraisalEntityId and Contact are embedded directly,
 * allowing a richer domain model without flattening data into primitive types.
 * </p>
 *
 * <p>
 * This class must not contain business logic.
 * </p>
 */

@Entity
@Table(name = "appraisal_entities")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppraisalEntityDM {

    @EmbeddedId
    private AppraisalEntityId id;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    @AttributeOverrides({
            // Contact
            @AttributeOverride(name = "_phoneNum", column = @Column(name = "contact_phone")),
            @AttributeOverride(name = "_extension", column = @Column(name = "contact_extension")),
            // Email
            @AttributeOverride(name = "_email.userName", column = @Column(name = "contact_email_username")),
            @AttributeOverride(name = "_email.domain", column = @Column(name = "contact_email_domain")),
            // Address
            @AttributeOverride(name = "_location.street", column = @Column(name = "contact_street")),
            @AttributeOverride(name = "_location.number", column = @Column(name = "contact_number")),
            @AttributeOverride(name = "_location.zipCode", column = @Column(name = "contact_zipcode")),
            // CityID
            @AttributeOverride(name = "_location.cityID.value", column = @Column(name = "contact_city_id"))
    })
    private Contact contact;

    @ElementCollection
    @CollectionTable(name = "appraisal_entity_publication_types", joinColumns = @JoinColumn(name = "appraisal_entity_id"))
    @AttributeOverride(name = "value", column = @Column(name = "publication_type_id"))
    @OrderColumn(name = "order_index")
    private List<PublicationTypeId> publicationTypeIds;

    @ElementCollection
    @CollectionTable(name = "appraisal_entity_genres", joinColumns = @JoinColumn(name = "appraisal_entity_id"))
    @AttributeOverride(name = "value", column = @Column(name = "genre_id"))
    @OrderColumn(name = "order_index")
    private List<GenreId> genreIds;

    /**
     * Constructs an AppraisalEntityDM with all required fields.
     *
     * @param id unique identifier of the appraisal entity
     * @param name name of the appraisal entity
     * @param contact contact information (e.g., email or phone)
     * @param publicationTypeIds list of associated publication type identifiers
     * @param genreIds list of associated genre identifiers
     */
    public AppraisalEntityDM (AppraisalEntityId id,
                             String name,
                             Contact contact,
                             List<PublicationTypeId> publicationTypeIds,
                             List<GenreId> genreIds

    ) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        this.id = id;
        this.name = name;
        this.contact = contact;
        this.publicationTypeIds = publicationTypeIds;
        this.genreIds = genreIds;
    }

}
