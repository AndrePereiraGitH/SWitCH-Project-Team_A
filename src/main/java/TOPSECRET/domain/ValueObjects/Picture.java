package TOPSECRET.domain.ValueObjects;

import TOPSECRET.ddd.ValueObject;

/**
 * Value Object representing a picture associated with a listing.
 * <p>
 * A {@code Picture} encapsulates the path or URL of an image and ensures
 * that the value is always valid. This object is immutable and equality
 * is based solely on its underlying string value.
 * </p>
 */
public final class Picture implements ValueObject {

    private final String _picture; // path or URL

    // ============================================================
    // ============== CONSTRUCTOR =================================
    // ============================================================

    /**
     * Creates a new {@code Picture} with the given path or URL.
     * <p>
     * The provided string must not be {@code null} or blank. This ensures
     * that every {@code Picture} instance represents a valid image reference.
     * </p>
     *
     * @param picture the path or URL of the picture
     * @throws IllegalArgumentException if {@code picture} is {@code null} or blank
     */
    public Picture(String picture) {
        if (picture == null || picture.isBlank()) {
            throw new IllegalArgumentException("Picture cannot be null or blank");
        }
        this._picture = picture;
    }


    // ============================================================
    // ============== GETTERS =====================================
    // ============================================================

    public String getPicture() {
        return _picture;
    }

    // ============================================================
    // ============== EQUALS ======================================
    // ============================================================

    /**
     * Compares this {@code Picture} with another object for equality.
     * <p>
     * Two {@code Picture} instances are considered equal if and only if
     * their underlying picture reference (path or URL) is equal. This
     * comparison is case-sensitive and follows standard value-object
     * semantics, meaning equality is based solely on the encapsulated value.
     * </p>
     *
     * @param o the object to compare with this {@code Picture}
     * @return {@code true} if the given object is a {@code Picture} with the same
     *         underlying picture string; {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Picture other)) return false;
        return _picture.equals(other._picture);
    }
}
