package TOPSECRET.persistence.repositoryjpa.assembler;

import TOPSECRET.domain.Listing.Listing;
import TOPSECRET.domain.Listing.ListingFactory;
import TOPSECRET.domain.ValueObjects.Picture;
import TOPSECRET.domain.ValueObjects.Price;
import TOPSECRET.persistence.repositoryjpa.datamodel.ListingDM;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * Assembler responsible for converting between {@link Listing} domain objects
 * and {@link ListingDM} persistence entities.
 *
 * <p>Acts as a translation layer between the domain and persistence layers,
 * ensuring neither layer is coupled to the other's representation.
 *
 * <p>Handles type conversions for:
 * <ul>
 *   <li>{@link Picture} ↔ {@code String} (picture URL)</li>
 *   <li>{@link Price} ↔ {@code String} (decimal string representation)</li>
 * </ul>
 */
@Service
public class ListingAssembler {

    private final ListingFactory factory;

    /**
     * Constructs a {@code ListingAssembler} with the required {@link ListingFactory}.
     *
     * @param factory the factory used to reconstruct {@link Listing} domain objects
     *                from persisted data; must not be {@code null}
     */
    public ListingAssembler(ListingFactory factory) {
        this.factory = factory;
    }


    /**
     * Converts a {@link Listing} domain object into a {@link ListingDM} persistence entity.
     *
     * @param listing the domain object to convert; must not be {@code null}
     * @return a {@link ListingDM} representing the given listing
     * @throws NullPointerException if {@code listing} is {@code null}
     */
    public ListingDM toDM(Listing listing){

        Objects.requireNonNull(listing, "Listing cannot be null");

        ListingDM listingDM = new ListingDM(
                listing.id(),
                listing.getShortDescription(),
                listing.getItemsId(),
                listing.getSellerId(),
                listing.getSaleType(),
                listing.getListingTitle(),
                convertPicturesToDM(listing.getPictures()),
                listing.getPrice().getPrice().toPlainString(),
                listing.getPublicationDate());

        return listingDM;
    }

    /**
     * Converts a {@link ListingDM} persistence entity into a {@link Listing} domain object.
     *
     * <p>Delegates instantiation to {@link ListingFactory} to ensure domain
     * construction rules are respected during reconstitution.
     *
     * @param dm the persistence entity to convert; must not be {@code null}
     * @return a {@link Listing} domain object reconstructed from the given entity
     * @throws NullPointerException     if {@code dm} is {@code null}
     * @throws IllegalArgumentException if the stored price string cannot be parsed
     */
    public Listing toDomain(ListingDM dm){

        Objects.requireNonNull(dm, "ListingDM cannot be null");

        Listing listing = factory.create(
                dm.getListingId(),
                dm.getListingTitle(),
                dm.getItemsId(),
                dm.getSaleType(),
                convertPriceToDomain(dm.getPrice()),
                dm.getShortDescription(),
                dm.getSellerId(),
                convertPicturesToDomain(dm.getPictures()),
                dm.getPublicationDate()
        );

        return listing;
    }

    // =========================================================================
    // HELPERS
    // =========================================================================

    private static List<Picture> convertPicturesToDomain(List<String> pictures){

        List<Picture> listingPictures =
                pictures.stream().map(Picture::new).toList();

        return listingPictures;
    }

    private static List<String> convertPicturesToDM(List<Picture> pictures){

        List<String> stringPic =
                pictures.stream().map(Picture::getPicture).toList();

        return stringPic;
    }

    private static Price convertPriceToDomain(String price) {

        try {
            return new Price(new BigDecimal(price));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid price format stored in persistence: " + price, e);
        }
    }

}
