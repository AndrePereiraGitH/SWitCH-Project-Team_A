package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.Enum.TypeOfBinding;
import TOPSECRET.domain.ValueObjects.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;

/**
 * JPA data model representing an Edition entity in the persistence layer.
 *
 * <p>
 * This class maps the {@link TOPSECRET.domain.Edition.Edition} aggregate to a relational
 * database structure using JPA annotations. It is responsible solely for persistence
 * concerns and should not contain any business logic.
 * </p>
 *
 * <p>
 * Domain concepts such as {@link EditionID}, {@link PublicationTypeId}, and
 * {@link PublisherID} are embedded to preserve identity and value object semantics
 * while enabling database storage.
 * </p>
 *
 * <p>
 * Primitive fields (e.g., ISBN, ISSN, physical attributes) are stored in a format
 * suitable for persistence and later reconstructed into domain objects via
 * the {@link TOPSECRET.persistence.repositoryjpa.assembler.EditionAssembler}.
 * </p>
 *
 * <p>
 * The default constructor is protected to comply with JPA requirements while
 * preventing improper instantiation outside the persistence context.
 * </p>
 *
 * <p>
 * This class enforces basic validation rules (e.g., non-null constraints and
 * non-blank edition number) to ensure data integrity before persistence.
 * </p>
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "Editions")
public class EditionDM {

    @EmbeddedId
    @AttributeOverride(name = "editionID", column = @Column(name = "edition_id"))
    private EditionID  editionID;
    @Column(name = "edition_number", nullable = false)
    private String editionNumber;
    @Embedded
    private Code code;
    @Embedded
    private PhysicalAttributes physicalAttributes;
    @Embedded
    @AttributeOverride(name = "pbType_id", column = @Column(name = "publication_type_id"))
    private PublicationTypeId publicationTypeId;
    @Embedded
    @AttributeOverride(name = "_publisherID", column = @Column(name = "publisher_id"))
    private PublisherID publisherId;
    @Column(name = "media_format", nullable = false)
    private String mediaFormat;
    private Locale editionLanguage;
    private LocalDate editionDate;
    @Enumerated(EnumType.STRING)
    private TypeOfBinding typeOfBinding;

    /**
     * Creates a new EditionDM instance with the given attributes.
     *
     * <p>
     * This constructor is used to map domain data into a persistence-ready
     * representation. It enforces basic validation rules to ensure that
     * mandatory fields are not null and that the edition number is not blank.
     * </p>
     *
     * @param editionID the unique identifier of the edition; must not be null
     * @param editionNumber the edition number; must not be null or blank
     * @param code the ISBN-13, ISBN-10 or ISSN code(optional)
     * @param physicalAttributes the physical attributes of the edition; must not be null
     * @param publicationTypeId the publication type identifier; must not be null
     * @param publisherId the publisher identifier; must not be null
     * @param mediaFormat the media format of the edition; must not be null
     * @param editionLanguage the language of the edition; must not be null
     * @param editionDate the publication date; must not be null
     * @param typeOfBinding the type of binding; must not be null
     *
     * @throws NullPointerException if any mandatory parameter is null
     * @throws IllegalArgumentException if {@code editionNumber} is blank
     */
    public EditionDM(
            EditionID editionID,
            String editionNumber,
            Code code,
            PhysicalAttributes physicalAttributes,
            PublicationTypeId publicationTypeId,
            PublisherID publisherId,
            String mediaFormat,
            Locale editionLanguage,
            LocalDate editionDate,
            TypeOfBinding typeOfBinding
    ){

        this.editionID = Objects.requireNonNull(editionID, "EditionID cannot be null");
        this.editionNumber = Objects.requireNonNull(editionNumber, "Edition number cannot be null");
        if(editionNumber.isBlank()){
            throw new IllegalArgumentException("Edition number cannot be blank");
        }
        this.code = code;
        this.physicalAttributes = Objects.requireNonNull(physicalAttributes, "Physical Attributes cannot be null");
        this.publicationTypeId = Objects.requireNonNull(publicationTypeId, "Publication Type Id cannot be null");
        this.publisherId = Objects.requireNonNull(publisherId, "Publisher Id cannot be null");
        this.mediaFormat = Objects.requireNonNull(mediaFormat, "Media Format cannot be null");
        this.editionLanguage = Objects.requireNonNull(editionLanguage, "Edition Language cannot be null");
        this.editionDate = Objects.requireNonNull(editionDate, "Edition Date cannot be null");
        this.typeOfBinding = Objects.requireNonNull(typeOfBinding, "Type of Binding cannot be null");

    }

}
