package TOPSECRET.domain.PublicationSample;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.ValueObjects.*;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents a specific physical copy of a given Publication.
 * <p>
 * A {@code PublicationSample} is uniquely defined by its BibliographicalData,
 * its Owner (a User), its PublicationStatus, Edition,
 * and Condition.
 * <p>
 * No attribute must be left null when creating a PublicationSample.
 */
public class PublicationSample implements AggregateRoot<PublicationSampleID> {

    private final PublicationSampleID _pubSampleID;
    private final BibliographicalDataID _bdID;
    private final UserId _ownerID;
    private PublicationStatus _publicationStatus;
    private final EditionID _editionID;
    private final Condition _condition;
    private LibraryId _libraryID;
    private CollectionOfPublicationsID _collecOfPubID;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Creates a new {@code PublicationSample} with the given attributes.
     *
     * @param bdID                the bibliographical data ID of the publication; must not be null
     * @param ownerID             the ID of the user who owns this copy; must not be null
     * @param publicationStatus   the initial publication status; must not be null
     * @param editionID           the ID of the edition of the publication; must not be null
     * @param condition           the physical condition of this copy; must not be null
     * @throws IllegalArgumentException if any argument is null
     */
    protected PublicationSample(BibliographicalDataID bdID, UserId ownerID, PublicationStatus publicationStatus,
                                EditionID editionID, Condition condition) {
        if (bdID == null || ownerID == null || publicationStatus == null || editionID == null || condition == null) {
            throw new IllegalArgumentException("All criteria must not be null");
        }

        _pubSampleID = new PublicationSampleID(UUID.randomUUID().toString());
        _libraryID = null;
        _collecOfPubID = null;
        _bdID = bdID;
        _ownerID = ownerID;
        _publicationStatus = publicationStatus;
        _editionID = editionID;
        _condition = condition;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------


    public BibliographicalDataID getBibliographicalDataID() {
        return _bdID;
    }


    public UserId getOwnerID() {
        return _ownerID;
    }


    public PublicationStatus getPublicationStatus() {
        return _publicationStatus;
    }


    public EditionID getEditionID() {
        return _editionID;
    }


    public Condition getCondition() {
        return _condition;
    }


    public LibraryId getLibraryID(){
        return _libraryID;
    }

    public CollectionOfPublicationsID getCollecOfPubID(){
        return _collecOfPubID;
    }

    // -------------------------------------------------------------------------
    // Business methods
    // -------------------------------------------------------------------------

    /**
     * Changes the publication status of this copy to {@link PublicationStatus#FOR_SALE}.
     *
     * @return {@code true} if the status was successfully changed;
     *         {@code false} if the publication was already marked as for sale
     */
    public boolean changeStatusToForSale() {
        if (_publicationStatus == PublicationStatus.FOR_SALE) {
            return false;
        }

        _publicationStatus = PublicationStatus.FOR_SALE;
        return true;
    }

    /**
     * Associates this publication copy with the given library.
     *
     * @param libraryId the {@link LibraryId} of the library to associate with; must not be null
     * @return the provided {@link LibraryId} after assignment
     * @throws IllegalArgumentException if this copy is already assigned to a library
     * @throws NullPointerException if {@code libraryId} is null
     */
    public LibraryId addPublicationToLibrary(LibraryId libraryId) {
        if(libraryId == null){
            throw new NullPointerException("LibraryID can not be null");
        }
        if(_libraryID != null){
            throw new IllegalArgumentException("Already assigned to a library");
        }

        _libraryID = libraryId;
        return libraryId;
    }


    /**
     * Associates this publication copy with the given collection of publications.
     *
     * @param collectionOfPublicationsID the {@link CollectionOfPublicationsID} to associate with; must not be null
     * @return the provided {@link CollectionOfPublicationsID} after assignment
     * @throws IllegalArgumentException if this copy is already assigned to a collection
     * @throws NullPointerException if {@code collectionOfPublicationsID} is null
     */
    public CollectionOfPublicationsID addPublicationToCollection(CollectionOfPublicationsID collectionOfPublicationsID){
        if(collectionOfPublicationsID == null){
            throw new NullPointerException("CollectionOfPublicationsID can not be null");
        }
        if(_collecOfPubID != null){
            throw new IllegalArgumentException("Already assigned to a collection of publications");
        }

        if(_libraryID == null){
            throw new IllegalArgumentException("Publication must belong to a library before" +
                    " being assigned to a collection.");
        }

        _collecOfPubID = collectionOfPublicationsID;
        return collectionOfPublicationsID;
    }

    /**
     * Checks whether this publication belongs to the given genre.
     *
     * @param genre the {@link Genre} to check against
     * @return {@code true} if the bibliographical data contains the specified genre;
     *         {@code false} otherwise
     */
//    public boolean hasGenre(Genre genre) {
//        return _bdID.hasGenre(genre);
//    }

    /**
     * Retrieves the full set of details associated with this publication.
     *
     * <p>This method returns a {@link PublicationDetails} record containing
     * all relevant descriptive and ownership information of the publication,
     * including its bibliographical data, owner, publication status, edition,
     * and physical condition. It is typically used when a complete snapshot
     * of the publication's state is required for display, transfer, or
     * business logic operations.</p>
     *
     * @return a new {@link PublicationDetails} instance populated with this
     *         publication's bibliographical data, owner, status, edition,
     *         and condition
     */
    public PublicationSample.PublicationDetails getDetails() {
        return new PublicationSample.PublicationDetails(
                _bdID,
                _ownerID,
                _publicationStatus,
                _editionID,
                _condition
        );
    }

    /**
     * Determines whether this publication sample is currently eligible to be put for sale.
     * <p>
     * This method delegates the decision to the {@link PublicationStatus} enum,
     * ensuring that the business rule is evaluated consistently based on the
     * current status of the publication.
     * <p>
     * A publication sample is available for sale when its status is neither
     * {@code SOLD} nor {@code FOR_SALE}.
     *
     * @return {@code true} if this publication sample can be listed for sale,
     *         {@code false} otherwise.
     */
    public boolean isAvailableForSale() {
        return _publicationStatus.isAvailableForSale();
    }


    // -------------------------------------------------------------------------
    // Identity (AggregateRoot)
    // -------------------------------------------------------------------------

    @Override
    public PublicationSampleID id() {
        return _pubSampleID;
    }


    @Override
    public boolean sameAs(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PublicationSample that = (PublicationSample) o;
        return Objects.equals(_bdID, that._bdID) && Objects.equals(_ownerID, that._ownerID) &&
                Objects.equals(_editionID, that._editionID) &&
                _condition == that._condition;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PublicationSample that = (PublicationSample) o;
        return Objects.equals(_pubSampleID, that._pubSampleID);
    }


    // -------------------------------------------------------------------------
    // Inner types
    // -------------------------------------------------------------------------

    /**
     * Immutable data structure representing the complete details of a publication.
     *
     * <p>This record aggregates all key attributes that describe a publication,
     * including its bibliographical metadata, the user who owns it, its current
     * publication status, the edition being referenced, and its physical condition.
     * It is used as a safe and concise data carrier across the system whenever
     * publication information needs to be transferred or exposed.</p>
     *
     * @param bibliographicalData the bibliographical metadata of the publication
     * @param owner               the user who owns the publication
     * @param publicationStatus   the current status of the publication
     * @param edition             the edition associated with the publication
     * @param condition           the physical condition of the publication
     */
    public record PublicationDetails(
            BibliographicalDataID bibliographicalData,
            UserId owner,
            PublicationStatus publicationStatus,
            EditionID edition,
            Condition condition
    ) {
    }
}