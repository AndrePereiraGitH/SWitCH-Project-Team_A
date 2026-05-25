package TOPSECRET.domain.Enum;

public enum PublicationStatus {
    SOLD,
    FOR_SALE,
    READING,
    READ,
    CREATED;

    /**
     * Indicates whether a publication in this status is eligible to be put for sale.
     * <p>
     * A publication is considered available for sale when its current status does
     * not indicate that it is already listed for sale or has been sold.
     *
     * @return {@code true} if the publication may be listed for sale,
     *         {@code false} otherwise.
     */

    public boolean isAvailableForSale() {
        return this != SOLD && this != FOR_SALE;
    }
}
