package TOPSECRET.dto;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) for US021 - Get list of items on direct sale of a given genre.
 *
 * <p>This class encapsulates the essential information about a direct sale listing
 * for transfer between the controller and the presentation layer. All fields are
 * required and validated at construction time.</p>
 *
 * <p>This DTO is immutable — once created, its values cannot be changed.</p>
 */
public class US021DirectSaleDTO {
    private final String listingId;
    private final String listingTitle;
    private final BigDecimal price;
    private final String shortDescription;
    private final String sellerId;

    /**
     * Constructs a new US021DirectSaleDTO with the specified values.
     *
     * @param listingId        the unique identifier of the listing; must not be {@code null}
     * @param listingTitle     the title of the listing; must not be {@code null}
     * @param price            the sale price of the item; must not be {@code null}
     * @param shortDescription a brief description of the listing; must not be {@code null}
     * @param sellerId         the identifier of the seller; must not be {@code null}
     * @throws IllegalArgumentException if any parameter is {@code null}
     */
    public US021DirectSaleDTO(String listingId, String listingTitle, BigDecimal price,
                              String shortDescription, String sellerId) {
        if (listingId == null) {
            throw new IllegalArgumentException("listingId cannot be null");
        }
        if (listingTitle == null) {
            throw new IllegalArgumentException("listingTitle cannot be null");
        }
        if (price == null) {
            throw new IllegalArgumentException("price cannot be null");
        }
        if (shortDescription == null) {
            throw new IllegalArgumentException("shortDescription cannot be null");
        }
        if (sellerId == null) {
            throw new IllegalArgumentException("sellerId cannot be null");
        }

        this.listingId = listingId;
        this.listingTitle = listingTitle;
        this.price = price;
        this.shortDescription = shortDescription;
        this.sellerId = sellerId;
    }

    public String getListingId() {
        return listingId;
    }


    public String getListingTitle() {
        return listingTitle;
    }


    public BigDecimal getPrice() {
        return price;
    }


    public String getShortDescription() {
        return shortDescription;
    }


    public String getSellerId() {
        return sellerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        US021DirectSaleDTO that = (US021DirectSaleDTO) o;

        if (!listingId.equals(that.listingId)) return false;
        if (!listingTitle.equals(that.listingTitle)) return false;
        if (!price.equals(that.price)) return false;
        if (!shortDescription.equals(that.shortDescription)) return false;
        return sellerId.equals(that.sellerId);
    }

    @Override
    public int hashCode() {
        int result = listingId.hashCode();
        result = 31 * result + listingTitle.hashCode();
        result = 31 * result + price.hashCode();
        result = 31 * result + shortDescription.hashCode();
        result = 31 * result + sellerId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "US021DirectSaleDTO{" +
                "listingId='" + listingId + '\'' +
                ", listingTitle='" + listingTitle + '\'' +
                ", price=" + price +
                ", shortDescription='" + shortDescription + '\'' +
                ", sellerId='" + sellerId + '\'' +
                "}";
    }

}
