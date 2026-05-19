package TOPSECRET.persistence.repositoryjpa.jpa;

import TOPSECRET.domain.ValueObjects.AuctionId;
import TOPSECRET.domain.ValueObjects.ListingId;
import TOPSECRET.persistence.repositoryjpa.datamodel.AuctionDM;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for managing {@link AuctionDM} entities.
 * <p>
 * This interface provides persistence operations for {@link AuctionDM},
 * leveraging the capabilities of {@link JpaRepository}.
 * </p>
 * <p>
 * It acts as the data access layer for auction persistence, supporting
 * standard CRUD operations as well as custom query methods.
 * </p>
 */
public interface IAuctionRepositoryJPA extends JpaRepository<AuctionDM, AuctionId> {

    /**
     * Retrieves all {@link AuctionDM} entities whose {@link ListingId}
     * is contained in the given collection.
     *
     * @param listingIds the collection of listing identifiers; must not be {@code null}
     * @return a list of matching {@link AuctionDM} entities; may be empty
     */
    List<AuctionDM> findByListingIdIn(List<ListingId> listingIds);

    /**
     * Retrieves all auction data models associated with the given listing identifiers,
     * sorted in ascending order by auction deadline.
     *
     * @param listingIds the identifiers of the listings associated with the auctions
     * @return a list of auction data models sorted by auction end date time ascending
     */
    List<AuctionDM> findByListingIdInOrderByAuctionEndDateTimeAsc(List<ListingId> listingIds);


}
