package TOPSECRET.domain.PublicationSample;

import TOPSECRET.ddd.AggregateRoot;
import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Edition.Edition;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;
import TOPSECRET.domain.Genre.Genre;
import TOPSECRET.domain.User;
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
    private final EditionID _editionID; // Implement ID
    private final Condition _condition;
    private LibraryId _libraryID;
//    private CollectionOfPublicationsID _collecOfPubID;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

//    /**
//     * Creates a new {@code PublicationSample} with the given attributes.
//     *
//     * @param bd                  the bibliographical data of the publication; must not be null
//     * @param owner               the user who owns this copy; must not be null
//     * @param publicationStatus   the initial publication status; must not be null
//     * @param edition             the edition of the publication; must not be null
//     * @param condition           the physical condition of this copy; must not be null
//     * @throws IllegalArgumentException if any argument is null
//     */
    protected PublicationSample(BibliographicalDataID bdID, UserId ownerID, PublicationStatus publicationStatus,
                                EditionID editionID, Condition condition) {
        if (bdID == null || ownerID == null || publicationStatus == null || editionID == null || condition == null) {
            throw new IllegalArgumentException("All criteria must be filled");
        }

        _pubSampleID = new PublicationSampleID(UUID.randomUUID().toString());
        _libraryID = null;
        _bdID = bdID;
        _ownerID = ownerID;
        _publicationStatus = publicationStatus;
        _editionID = editionID;
        _condition = condition;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    /**
     * Returns the bibliographical data associated with this publication.
     *
     * @return the {@link BibliographicalData} of this publication
     */
    public BibliographicalDataID getBibliographicalData() {
        return _bdID;
    }

    /**
     * Returns the user who owns this publication copy.
     *
     * @return the {@link User} owner of this publication
     */
    public UserId getOwner() {
        return _ownerID;
    }

    /**
     * Returns the current publication status of this copy.
     *
     * @return the {@link PublicationStatus} of this publication
     */
    public PublicationStatus getPublicationStatus() {
        return _publicationStatus;
    }

    /**
     * Returns the edition associated with this publication copy.
     *
     * @return the {@link Edition} of this publication
     */
    public EditionID getEdition() {
        return _editionID;
    }

    /**
     * Returns the physical condition of this publication copy.
     *
     * @return the {@link Condition} of this publication
     */
    public Condition getCondition() {
        return _condition;
    }

    public LibraryId getLibraryID(){
        return _libraryID;
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
     */
    public LibraryId addPublicationToLibrary(LibraryId libraryId) {
        _libraryID = libraryId;
        return libraryId;
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

    // -------------------------------------------------------------------------
    // Identity (AggregateRoot)
    // -------------------------------------------------------------------------

    /**
     * Returns the unique identifier of this publication copy.
     *
     * @return the {@link PublicationSampleID} of this instance
     */
    @Override
    public PublicationSampleID id() {
        return _pubSampleID;
    }

    /**
     * Determines whether this publication copy shares the same identity
     * as the given object, based solely on {@link PublicationSampleID}.
     *
     * @param o the object to compare against
     * @return {@code true} if both objects are {@code PublicationSample} instances
     *         with equal identifiers; {@code false} otherwise
     */

    @Override
    public boolean sameAs(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PublicationSample that = (PublicationSample) o;
        return Objects.equals(_bdID, that._bdID) && Objects.equals(_ownerID, that._ownerID) &&
                _publicationStatus == that._publicationStatus && Objects.equals(_editionID, that._editionID) &&
                _condition == that._condition && Objects.equals(_libraryID, that._libraryID);
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