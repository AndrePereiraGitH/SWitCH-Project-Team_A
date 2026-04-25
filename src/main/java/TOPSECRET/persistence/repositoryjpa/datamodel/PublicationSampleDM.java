package TOPSECRET.persistence.repositoryjpa.datamodel;


import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.ValueObjects.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;


/**
 * JPA data model representing a physical copy of a publication (a "sample")
 * as persisted in the {@code PublicationSamples} table.
 *
 * <p>This class is a persistence-layer entity and should not be used directly
 * in domain logic. Domain objects should be mapped to and from this class
 * via an assembler or mapper.</p>
 *
 * <p>Instantiation is restricted to the provided constructor — the no-argument
 * constructor is reserved for JPA's internal use only.</p>
 */
@Entity
@Table(name = "PublicationSamples")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PublicationSampleDM {

    @EmbeddedId
    @AttributeOverride(name = "pubSampleID", column = @Column(name = "publication_sample_id"))
    private PublicationSampleId pubSampleId;

    @Embedded
    @AttributeOverride(name = "bibliographicalDataID", column = @Column(name = "bibliographical_data_id"))
    private BibliographicalDataID bdId;

    @Embedded
    @AttributeOverride(name = "userId", column = @Column(name = "owner_id"))
    private UserId ownerId;

    @Embedded
    @AttributeOverride(name = "editionID", column = @Column(name = "edition_id"))
    private EditionID editionId;

    @Embedded
    @AttributeOverride(name = "_id", column = @Column(name = "library_id"))
    private LibraryId libraryId;

    @Embedded
    @AttributeOverride(name = "collectionOfPub_id", column = @Column(name = "collection_of_publications_id"))
    private CollectionOfPublicationsID collectionOfPublicationsId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PublicationStatus publicationStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Condition condition;


    /**
     * Constructs a fully initialized {@code PublicationSampleDM}.
     * All parameters are mandatory — passing {@code null} for any argument
     * will throw a {@link NullPointerException}.
     *
     * @param pubSampleId                the unique identifier for this sample; must not be {@code null}
     * @param bdId                       the bibliographical data identifier; must not be {@code null}
     * @param ownerId                    the identifier of the owning user; must not be {@code null}
     * @param editionId                  the edition identifier; must not be {@code null}
     * @param libraryId                  the library identifier; must not be {@code null}
     * @param collectionOfPublicationsId the collection identifier; must not be {@code null}
     * @param publicationStatus          the current publication status; must not be {@code null}
     * @param condition                  the physical condition of the sample; must not be {@code null}
     * @throws NullPointerException if any parameter is {@code null}
     */
    public PublicationSampleDM(PublicationSampleId pubSampleId, BibliographicalDataID bdId,
                               UserId ownerId, EditionID editionId, LibraryId libraryId,
                               CollectionOfPublicationsID collectionOfPublicationsId,
                               PublicationStatus publicationStatus, Condition condition) {

        this.pubSampleId = Objects.requireNonNull(pubSampleId,"PublicationSampleId cannot be null");
        this.bdId = Objects.requireNonNull(bdId,"BibliographicalDataId cannot be null");
        this.ownerId = Objects.requireNonNull(ownerId,"OwnerId cannot be null");
        this.editionId = Objects.requireNonNull(editionId, "EditionId cannot be null");
        this.libraryId = Objects.requireNonNull(libraryId, "LibraryId cannot be null");
        this.collectionOfPublicationsId = Objects.requireNonNull(collectionOfPublicationsId,"CollectionOfPublicationsId cannot be null");
        this.publicationStatus = Objects.requireNonNull(publicationStatus,"PublicationStatus cannot be null");
        this.condition = Objects.requireNonNull(condition,"Condition cannot be null");

    }
}
