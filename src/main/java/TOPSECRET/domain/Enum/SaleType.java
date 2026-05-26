package TOPSECRET.domain.Enum;

import java.util.Arrays;

public enum SaleType {

        DIRECT_SALE("Direct Sale"),
        AUCTION("Auction");

    private final String description;

    SaleType(String description) {
        this.description = description;
    }

    public String getDescription() {
            return description;
    }

    /**
     * Converts a string representation of a sale type into the corresponding
     * {@link SaleType} enum value.
     *
     * @param value the sale type description to convert
     * @return the corresponding {@link SaleType}
     * @throws IllegalArgumentException if no matching sale type is found
     */
    public static SaleType fromString(String value) {
        return Arrays.stream(SaleType.values())
                .filter(st -> st.getDescription().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown Sale Type: " + value));
    }
}
