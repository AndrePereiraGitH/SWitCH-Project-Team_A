package TOPSECRET.domain;

import TOPSECRET.domain.BibliographicalData.BibliographicalData;
import TOPSECRET.domain.Enum.Condition;
import TOPSECRET.domain.Enum.PublicationStatus;

import java.util.Objects;

/**
 * Represents a specific physical copy of a given Publication.
 * <p>
 * A {@code PublicationSample} is uniquely defined by its BibliographicalData,
 * its Owner (a User), its PublicationStatus, Edition,
 * and Condition.
 * <p>
 * No attribute must be left null when creating a PublicationSample.
 */


public class PublicationSample {

    private final BibliographicalData _bd;
    private final User _owner;
    private PublicationStatus _publicationStatus;
    private final Edition _edition;
    private final Condition _condition;

    protected PublicationSample(BibliographicalData bd, User owner, PublicationStatus publicationStatus, Edition edition,
                                Condition condition) {

        if (bd == null || owner == null || publicationStatus == null || edition == null || condition == null) {
            throw new IllegalArgumentException("All criteria must be filled");
        }

        // Before, Edition and Code could be left blank/null, but this is no longer the case

        this._bd = bd;
        this._owner = owner;
        this._publicationStatus = publicationStatus;
        this._edition = edition;
        this._condition = condition;
    }

    public BibliographicalData getBibliographicalData() {
        return _bd;
    }

    public User getOwner() {
        return _owner;
    }

    public PublicationStatus getPublicationStatus() {
        return _publicationStatus;
    }

    public Edition getEdition() {
        return _edition;
    }

    public boolean changeStatusToForSale() {

        if (_publicationStatus == PublicationStatus.FOR_SALE) {

            return false;
        }

        _publicationStatus = PublicationStatus.FOR_SALE;
        return true;
    }

    public Condition getCondition() {
        return _condition;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PublicationSample that = (PublicationSample) o;
        return Objects.equals(_bd, that._bd) && Objects.equals(_owner, that._owner)
                && _publicationStatus == that._publicationStatus && _edition == that._edition
                && _condition == that._condition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_bd, _owner, _publicationStatus, _edition, _condition);
    }

    //US021 Start
    public boolean hasGenre(Genre genre) {
        return _bd.hasGenre(genre);
    }
    //US021 End

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
     * publication's bibliographical data, owner, status, edition,
     * and condition.
     */

    public PublicationSample.PublicationDetails getDetails() {
        return new PublicationSample.PublicationDetails(
                _bd,
                _owner,
                _publicationStatus,
                _edition,
                _condition
        );
    }

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
            BibliographicalData bibliographicalData,
            User owner,
            PublicationStatus publicationStatus,
            Edition edition,
            Condition condition
    ) {
    }

    // US023 End ----------------------------------------------------------------------------------------------------
}