package TOPSECRET.repository;

import TOPSECRET.domain.*;
import TOPSECRET.domain.PublicationSample.PublicationSample;
import TOPSECRET.domain.ValueObjects.Price;
import TOPSECRET.dto.AuthorDTO_US026;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Contract for managing {@link Auction} objects.
 * <p>
 * Defines operations for creating, retrieving, and verifying auctions.
 * </p>
 */
public interface IAuctionRepository {


    /**
     * Returns the complete list of available auctions.
     *
     * @return a list containing all auctions
     */
    List<Auction> getAllAuctions();

    /**
     * Creates a new auction and adds it to the repository if it does not already exist.
     *
     * @param listing the listing associated with the auction
     * @param initialDate the auction start date and time
     * @param finalDate the auction end date and time
     * @param buyoutPrice the buyout price of the auction
     * @return {@code true} if the auction was created and added, {@code false} if it already exists
     */
    Auction createAndAdd(Listing listing, LocalDateTime initialDate, LocalDateTime finalDate, Price buyoutPrice) throws InstanceAlreadyExistsException;

    /**
     * Returns the list of auctions containing at least one item whose publication
     * belongs to the given genre.
     *
     * @param genre the genre used to filter auctions
     * @return a list of auctions that contain at least one item with the given genre
     * @throws IllegalArgumentException if {@code genre} is {@code null}
     */
    List<Auction> getListOfItemsOnAuctionGivenGenre(Genre genre);

    /**
     * Returns the list of auctions containing at least one item whose publication
     * is associated with the given author.
     *
     * @param author the author DTO used to filter auctions
     * @return a list of auctions that contain at least one item by the given author
     * @throws IllegalArgumentException if {@code author} is {@code null} or its name is blank
     */
    List<Auction> getListOfItemsOnAuctionGivenAuthor(AuthorDTO_US026 author);

    /**
     * Retrieves all {@link PublicationSample} instances currently on auction
     * that match the given publication title.
     *
     * <p>Iterates over all active auctions, inspecting each auction's
     * {@link Listing} and its associated items. An item is included in the
     * result if its bibliographical title is non-{@code null} and equals
     * the provided {@code publicationDTO} identifier.</p>
     *
     * <p>Auctions with a {@code null} listing or a {@code null} item list
     * are silently skipped.</p>
     *
     * @param publicationDTO the title of the publication to filter by;
     *                       a {@code null} value will result in an empty list
     * @return a {@link List} of {@link PublicationSample} objects whose
     *         bibliographical title matches {@code publicationDTO};
     *         never {@code null}, but may be empty if no matches are found
     */
    List<PublicationSample> getListOfItemsOnAuctionByGivenPublication(String publicationDTO);
}
