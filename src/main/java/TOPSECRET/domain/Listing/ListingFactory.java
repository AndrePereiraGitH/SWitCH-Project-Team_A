package TOPSECRET.domain.Listing;

import TOPSECRET.domain.Enum.SaleType;
import TOPSECRET.domain.ValueObjects.Picture;
import TOPSECRET.domain.ValueObjects.Price;
import TOPSECRET.domain.ValueObjects.PublicationSampleId;
import TOPSECRET.domain.ValueObjects.UserId;

import java.util.List;

/**
 * Factory responsible for creating instances of {@link Listing}.
 * <p>
 * This factory encapsulates the creation logic for listings, ensuring that
 * the domain rules enforced by the {@link Listing} constructor are respected, such as:
 * <ul>
 *     <li>title length constraints</li>
 *     <li>minimum description length</li>
 *     <li>non-null price and pictures</li>
 * </ul>
 */
public class ListingFactory {

    public Listing create(
            String listingTitle,
            List<PublicationSampleId> itemsId,
            SaleType saleType,
            Price price,
            String shortDescription,
            UserId sellerId,
            List<Picture> pictures) {

        return new Listing(listingTitle, itemsId, saleType, price, shortDescription, sellerId, pictures);
    }
}
