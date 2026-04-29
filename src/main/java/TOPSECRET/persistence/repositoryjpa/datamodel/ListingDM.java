package TOPSECRET.persistence.repositoryjpa.datamodel;

import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.ValueObjects.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

/**
 * JPA data model representing a listing in the persistence layer.
 *
 * <p>A listing groups one or more {@link PublicationSampleId publication samples}
 * under a single offer, created by a seller and associated with a sale type,
 * price, title, description, and pictures.
 *
 * <p>Mapped to three tables:
 * <ul>
 *   <li>{@code listings} - core listing data</li>
 *   <li>{@code listing_items} - one row per grouped publication sample</li>
 *   <li>{@code listing_pictures} - one row per picture URL</li>
 * </ul>
 *
 * <p>The no-arg constructor is protected and reserved for JPA use only.
 * Use {@link #ListingDM(ListingId, String, List, UserId, SaleType, String, List, String)}
 * to create instances programmatically.
 */
@Entity
@Table(name = "listings")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ListingDM {

    @EmbeddedId
    @AttributeOverride(name = "_listingId", column = @Column(name = "listing_id"))
    private ListingId listingId;

    @ElementCollection
    @CollectionTable(name = "listing_items", joinColumns = @JoinColumn(name = "listing_id"))
    @AttributeOverride(name = "pubSampleID", column = @Column(name = "pubSample_id"))
    private List<PublicationSampleId> itemsId;

    @Embedded
    @AttributeOverride(name = "userId", column = @Column(name = "user_id"))
    private UserId sellerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "sale_type", nullable = false)
    private SaleType saleType;

    @Column(name = "listing_title")
    private String listingTitle;

    @ElementCollection
    @CollectionTable(name = "listing_pictures", joinColumns = @JoinColumn(name = "listing_id"))
    @Column(name = "picture_url")
    private List<String> pictures;

    @Column(name = "price")
    private String price;

    @Column(name = "short_description")
    private String shortDescription;


    /**
     * Creates a new {@code ListingDM} with all required fields.
     *
     * @param listingId       the unique identifier for this listing; must not be {@code null}
     * @param shortDescription a brief description of the listing; must not be {@code null}
     * @param itemsId         the list of publication sample IDs grouped in this listing; must not be {@code null}
     * @param sellerId        the ID of the seller creating the listing; must not be {@code null}
     * @param saleType        the type of sale; must not be {@code null}
     * @param listingTitle    the display title of the listing; must not be {@code null}
     * @param pictures        the list of picture URLs for the listing; must not be {@code null}
     * @param price           the price of the listing; must not be {@code null}
     * @throws NullPointerException if any parameter is {@code null}
     */
    public ListingDM(ListingId listingId, String shortDescription,
                     List<PublicationSampleId> itemsId, UserId sellerId,
                     SaleType saleType, String listingTitle,
                     List<String> pictures, String price) {

        this.listingId = Objects.requireNonNull(listingId);
        this.shortDescription = Objects.requireNonNull(shortDescription);
        this.itemsId = Objects.requireNonNull(itemsId);
        this.sellerId = Objects.requireNonNull(sellerId);
        this.saleType = Objects.requireNonNull(saleType);
        this.listingTitle = Objects.requireNonNull(listingTitle);
        this.pictures = Objects.requireNonNull(pictures);
        this.price = Objects.requireNonNull(price);
    }
}