package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.DomainID;

/**
 * Represents a unique identifier for a Transaction.
 * <p>
 * This value object encapsulates the transaction ID as a non-null,
 * non-blank {@link String}. It is immutable and defines equality
 * based solely on the encapsulated identifier value.
 * <p>
 * Two {@code TransactionID} instances are considered equal if their
 * underlying ID values are equal.
 */

public class TransactionID implements DomainID {

    private final String transaction_id;

    /**
     * Constructs a {@code TransactionID} with the specified identifier.
     *
     * @param id the unique transaction identifier
     * @throws IllegalArgumentException if {@code id} is null or blank
     */

    public TransactionID(String id) {

        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID cannot be null or blank");
        }

        this.transaction_id = id;
    }

    /**
     * Compares this {@code TransactionID} to another object for equality.
     * <p>
     * Two {@code TransactionID} instances are equal if:
     * <ul>
     *     <li>They are the same instance, or</li>
     *     <li>The other object is a {@code TransactionID} with the same ID value</li>
     * </ul>
     *
     * @param object the object to compare with
     * @return {@code true} if the objects are equal; {@code false} otherwise
     */

    @Override
    public boolean equals(Object object) {

        if (this == object)
            return true;

        if (object instanceof TransactionID) {
            TransactionID transactionId = (TransactionID) object;

            if (this.transaction_id.equals(transactionId.transaction_id))
                return true;
        }
        return false;
    }

    /**
     * Returns the string representation of this transaction ID.
     *
     * @return the transaction ID as a {@link String}
     */

    @Override
    public String toString() {
        // change to getID?

        return transaction_id;
    }

}
